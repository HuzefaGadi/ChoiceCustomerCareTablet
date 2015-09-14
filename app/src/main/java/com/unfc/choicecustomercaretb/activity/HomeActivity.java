package com.unfc.choicecustomercaretb.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.unfc.choicecustomercaretb.adapters.QueueAdapter;
import com.unfc.choicecustomercaretb.api.BaseApi;
import com.unfc.choicecustomercaretb.entity.BaseEntity;
import com.unfc.choicecustomercaretb.entity.MessageEntity;
import com.unfc.choicecustomercaretb.entity.RoomEntity;
import com.unfc.choicecustomercaretb.service.PlaySoundService;
import com.unfc.choicecustomercaretb.utility.Constants;
import com.unfc.choicecustomercaretb.R;
import com.unfc.choicecustomercaretb.utility.CustomPreferences;
import com.unfc.choicecustomercaretb.utility.RequestType;
import com.unfc.choicecustomercaretb.utility.Utilities;
import com.unfc.choicecustomercaretb.view.LoadingDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * 20/7/15.
 */
public class HomeActivity extends BaseActivity {

	private int mPatientId;
	private String mStrAnswers;
	private RoomEntity mRoomEntity, mBedEntity;
	private CountDownTimer timer;

	@Bind({R.id.home_food_btn, R.id.home_bathroom_btn, R.id.home_pain_btn, R.id.home_other_btn, R.id.home_team_btn,
			R.id.home_new_uic_btn, R.id.home_video_ad_btn, R.id.home_food_ad_btn})
	List<LinearLayout> requestOptions;

	@Bind(R.id.home_title_text)
	TextView txtTitle;

	@Bind(R.id.home_emergency_btn)
	TextView btnEmergency;

	@Bind(R.id.home_reset_btn)
	TextView btnReset;

	@Bind(R.id.home_queue_list_view)
	RecyclerView recyclerView;

	@Override
	protected int addLayoutView() {
		View decorView = getWindow().getDecorView();
		int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
		decorView.setSystemUiVisibility(uiOptions);
		return R.layout.activity_home;
	}

	@Override
	protected void initComponents() {
		super.initComponents();

		mPatientId = CustomPreferences.getPreferences(Constants.PREF_PATIENT_ID, 0);
		mStrAnswers = getIntent().getStringExtra(Constants.INTENT_PATIENT_QUESTION);
		String strBed = CustomPreferences.getPreferences(Constants.PREF_PATIENT_BED_NO, "");
		String strRoom = CustomPreferences.getPreferences(Constants.PREF_PATIENT_ROOM_NO, "");
		mBedEntity = new Gson().fromJson(strBed, RoomEntity.class);
		mRoomEntity = new Gson().fromJson(strRoom, RoomEntity.class);
		if (mBedEntity == null || mRoomEntity == null) {

			CustomPreferences.setPreferences(Constants.PREF_IS_FIRST_SETUP, true);
			startActivity(new Intent(this, FirstSetupActivity.class));
			finish();
			return;
		}

		boolean isEmergencyShowing = CustomPreferences.getPreferences(Constants.PREF_IS_EMERGENCY_SHOWING, false);
		if (isEmergencyShowing) {

			showEmergencyDialog();
		}

		CustomPreferences.setPreferences(Constants.PREF_PATIENT_QUESTION, mStrAnswers);
		ButterKnife.bind(this);
		txtTitle.setText(
				String.format(getString(R.string.home_title), mRoomEntity.getName(), mBedEntity.getBedNumber()));
		for (int i = 0; i < requestOptions.size(); i++) {

			LinearLayout item = requestOptions.get(i);
			item.setOnClickListener(this);
			item.setTag(i);
		}

		LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(mLayoutManager);
		btnEmergency.setOnClickListener(this);
		btnReset.setOnClickListener(this);
		getRequestQueue();
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);

		switch (view.getId()) {

			case R.id.home_emergency_btn :

				showEmergencyDialogCancelable();
				break;

			case R.id.home_reset_btn :

				startActivity(new Intent(this, QuestionActivity.class));
				finish();
				break;

			case R.id.home_food_btn :
			case R.id.home_pain_btn :
			case R.id.home_team_btn :
			case R.id.home_other_btn :
			case R.id.home_food_ad_btn :
			case R.id.home_new_uic_btn :
			case R.id.home_bathroom_btn :
			case R.id.home_video_ad_btn :

				try {

					int id = (int) view.getTag() + 1;
					String strRequest = RequestType.values()[id].toString().toLowerCase();
					sendRequest(id, strRequest);
				} catch (Exception ignore) {

				}

				break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
				new IntentFilter(Constants.INTENT_UPDATE_REQUEST_LIST));
	}

	/**
	 * Send emergency request
	 */
	private void sendEmergencyRequest() {

		final Dialog dialog = LoadingDialog.show(this);
		String message = String.format("Room %s Bed %s sent emergency request.", mRoomEntity.getName(),
				mBedEntity.getBedNumber());
		new BaseApi(false).getInterface().doPostEmergency(mRoomEntity.getId(), mRoomEntity.getId(), message,
				new Callback<BaseEntity>() {
					@Override
					public void success(BaseEntity baseEntity, Response response) {
						showEmergencyDialog();

						Utilities.dismissDialog(dialog);
					}

					@Override
					public void failure(RetrofitError retrofitError) {

						Utilities.dismissDialog(dialog);
					}
				});
	}

	/**
	 * Send request
	 */
	private void sendRequest(int messageType, String message) {

		final Dialog dialog = LoadingDialog.show(this);
		String strDeviceId = CustomPreferences.getPreferences(Constants.PREF_PUSH_TOKEN, "");
		String strRequest = String.format(getString(R.string.string_request), mRoomEntity.getName(),
				mBedEntity.getBedNumber(), message);

		// Message
		JsonObject jMessage = new JsonObject();
		jMessage.addProperty(Constants.MESSAGE_TEXT, strRequest);
		jMessage.addProperty(Constants.MESSAGE_TYPE_ID, messageType);

		JsonObject jPatient = new JsonObject();
		jPatient.addProperty(Constants.ID, mPatientId);
		jPatient.addProperty(Constants.DEVICE_ID, strDeviceId);
		jPatient.addProperty(Constants.ROOM_NO, mRoomEntity.getId());
		jPatient.addProperty(Constants.BED_NO, mBedEntity.getId());

		JsonArray jAnswer = new Gson().fromJson(mStrAnswers, JsonArray.class);

		JsonObject jsonObject = new JsonObject();
		jsonObject.add(Constants.MESSAGE, jMessage);
		jsonObject.add(Constants.PATIENT, jPatient);
		jsonObject.add(Constants.ANSWERS, jAnswer);

		new BaseApi(false).getInterface().doPostMessage(jsonObject, new Callback<MessageEntity>() {
			@Override
			public void success(MessageEntity message, Response response) {

				mPatientId = message.getPatientId();
				getRequestQueue();
				CustomPreferences.setPreferences(Constants.PREF_PATIENT_ID, mPatientId);
				Utilities.dismissDialog(dialog);
			}

			@Override
			public void failure(RetrofitError retrofitError) {

				Utilities.dismissDialog(dialog);
			}
		});
	}

	@Override
	protected void onPause() {

		LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
		super.onPause();
	}

	/**
	 * Get request queue
	 */
	private void getRequestQueue() {

		if (mPatientId == 0) {

			return;
		}

		new BaseApi(false).getInterface().getRequestQueue(mPatientId, new Callback<List<MessageEntity>>() {
			@Override
			public void success(List<MessageEntity> messages, Response response) {

				displayQueue(messages);
			}

			@Override
			public void failure(RetrofitError retrofitError) {

			}
		});
	}

	/**
	 * Display queue
	 */
	private void displayQueue(List<MessageEntity> messages) {

		QueueAdapter adapter = new QueueAdapter(messages);
		recyclerView.setAdapter(adapter);
	}

	/**
	 * Auth dialog
	 */
	private void showEmergencyDialog() {
		Log.i("ShowEmergency", "starts");
		CustomPreferences.setPreferences(Constants.PREF_IS_EMERGENCY_SHOWING, true);
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.layout_emergency_dialog);

		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		final EditText editText = ButterKnife.findById(dialog, R.id.emergency_pin_edt);
		TextView btnClose = ButterKnife.findById(dialog, R.id.emergency_close_btn);

		editText.clearFocus();
		btnClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				String strPin = editText.getText().toString();
				if (strPin.equals("")) {

					return;
				}

				doCloseEmergency(strPin, dialog);
			}
		});

		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);

		// Show
		dialog.show();
	}

	private void showEmergencyDialogCancelable() {
		Log.i("ShowCancelable", "starts");
		startService(new Intent(this, PlaySoundService.class));

		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.layout_emergency_dialog_cancelable);




		final TextView btnClose = ButterKnife.findById(dialog, R.id.emergency_close_btn);




		timer = new CountDownTimer(5000, 1000) {

			public void onTick(long millisUntilFinished) {
				btnClose.setText("Cancel Emergency Request (" + (millisUntilFinished / 1000) + ")");

			}

			public void onFinish() {
				sendEmergencyRequest();
				timer.cancel();
				dialog.dismiss();
			}
		};
		timer.start();

		btnClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				stopService(new Intent(HomeActivity.this, PlaySoundService.class));
				timer.cancel();

				dialog.dismiss();

			}
		});



		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);

		// Show
		dialog.show();
	}

	/**
	 * Do close emergency
	 */
	private void doCloseEmergency(String pinCode, final Dialog emergencyDialog) {

		final Dialog dialog = new Dialog(this);
		new BaseApi(false).getInterface().doCloseEmergency(mRoomEntity.getId(), mBedEntity.getId(), pinCode,
				new Callback<BaseEntity>() {
					@Override
					public void success(BaseEntity baseEntity, Response response) {

						CustomPreferences.setPreferences(Constants.PREF_IS_EMERGENCY_SHOWING, false);
						Utilities.dismissDialog(dialog);
						Utilities.dismissDialog(emergencyDialog);

						stopService(new Intent(HomeActivity.this, PlaySoundService.class));

					}

					@Override
					public void failure(RetrofitError retrofitError) {

						Utilities.dismissDialog(dialog);
					}
				});
	}

	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			getRequestQueue();
		}
	};
}
