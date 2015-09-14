package com.unfc.choicecustomercaretb.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.unfc.choicecustomercaretb.R;
import com.unfc.choicecustomercaretb.adapters.RoomAdapter;
import com.unfc.choicecustomercaretb.api.BaseApi;
import com.unfc.choicecustomercaretb.entity.RoomEntity;
import com.unfc.choicecustomercaretb.utility.Constants;
import com.unfc.choicecustomercaretb.utility.CustomPreferences;
import com.unfc.choicecustomercaretb.utility.Utilities;
import com.unfc.choicecustomercaretb.view.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Hai Nguyen - 9/2/15.
 */
public class FirstSetupActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

	@Bind(R.id.setup_finish_btn)
	TextView btnComplete;

	@Bind(R.id.setup_bed_num_spn)
	Spinner spnBedNo;

	@Bind(R.id.setup_room_num_spn)
	Spinner spnRoomNo;

	@Bind(R.id.setup_wifi_connect)
	TextView btnWifiConnect;

	@Bind(R.id.setup_server_connect)
	TextView btnServerConnect;

	@Bind(R.id.setup_server_cancel)
	TextView btnServerCancel;

	@Bind(R.id.setup_wifi_status)
	TextView txtWifiStatus;

	@Bind(R.id.setup_server_status)
	TextView txtServerStatus;

	@Bind(R.id.setup_server_layout)
	LinearLayout llServerLayout;

	@Bind(R.id.setup_room_layout)
	LinearLayout llRoomLayout;

	@Bind(R.id.setup_server_url_edt)
	EditText edtServerUrl;

	private RoomEntity mBedEntity, mRoomEntity;

	@Override
	protected int addLayoutView() {
		View decorView = getWindow().getDecorView();
		int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
		decorView.setSystemUiVisibility(uiOptions);
		return R.layout.activity_first_setup;
	}

	@Override
	protected void initComponents() {
		super.initComponents();

		btnComplete.setOnClickListener(this);
		btnWifiConnect.setOnClickListener(this);
		spnBedNo.setOnItemSelectedListener(this);
		btnServerCancel.setOnClickListener(this);
		spnRoomNo.setOnItemSelectedListener(this);
		btnServerConnect.setOnClickListener(this);
		displayData(null);
		checkConnection();
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {

			case R.id.setup_finish_btn :

				finishSetup();
				break;
			case R.id.setup_wifi_connect :

				startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), Constants.REQUEST_CODE_WIFI);
				break;
			case R.id.setup_server_connect :

				if (edtServerUrl.getVisibility() == View.VISIBLE) {

					String strUrl = edtServerUrl.getText().toString();
					CustomPreferences.setPreferences(Constants.PREF_BASE_URL, strUrl);
					if (strUrl.equals("")) {

						txtServerStatus.setText(getString(R.string.setup_not_connected));
						llRoomLayout.setVisibility(View.INVISIBLE);
					} else {

						txtServerStatus.setText(getString(R.string.setup_server_connected) + strUrl);
						getQuestions();
					}

					txtServerStatus.setVisibility(View.VISIBLE);
					edtServerUrl.setVisibility(View.GONE);
					btnServerCancel.setVisibility(View.GONE);
				} else {

					txtServerStatus.setVisibility(View.GONE);
					edtServerUrl.setVisibility(View.VISIBLE);
					btnServerCancel.setVisibility(View.VISIBLE);
				}

				break;
			case R.id.setup_server_cancel :

				txtServerStatus.setVisibility(View.VISIBLE);
				edtServerUrl.setVisibility(View.GONE);
				btnServerCancel.setVisibility(View.GONE);
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == Constants.REQUEST_CODE_WIFI) {

			checkConnection();
		}
	}

	/**
	 * Display connected information
	 */
	private void checkConnection() {

		if (Utilities.isNetworkConnected(this)) {

			WifiManager wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
			String wifiName = wifiInfo.getSSID();
			if (wifiName == null || wifiName.equals("")) {

				txtWifiStatus.setText(getString(R.string.setup_connected));
			} else {

				txtWifiStatus.setText(getString(R.string.setup_wifi_connected) + wifiName);
			}

			llServerLayout.setVisibility(View.VISIBLE);
		} else {

			llServerLayout.setVisibility(View.INVISIBLE);
			txtWifiStatus.setText(getString(R.string.setup_not_connected));
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

		RoomAdapter adapter = (RoomAdapter) adapterView.getAdapter();
		RoomEntity room = (RoomEntity) adapter.getItem(i);
		if (adapterView.getId() == R.id.setup_room_num_spn) {

			spnBedNo.setAdapter(new RoomAdapter(FirstSetupActivity.this, room.getBeds(), false));
			mRoomEntity = room;
			return;
		}

		mBedEntity = room;
	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView) {

	}

	/**
	 * Get questions
	 */
	private void getQuestions() {

		final Dialog dialog = LoadingDialog.show(this);
		new BaseApi(false).getInterface().getRooms(new Callback<List<RoomEntity>>() {
			@Override
			public void success(List<RoomEntity> rooms, Response response) {

				displayData(rooms);
				Utilities.dismissDialog(dialog);
			}

			@Override
			public void failure(RetrofitError retrofitError) {

				Utilities.dismissDialog(dialog);
				Utilities.showAlertDialog(FirstSetupActivity.this, getString(R.string.app_name),
						getString(R.string.can_not_connect_to_server), getString(R.string.ok), "", null, null, true);
				llRoomLayout.setVisibility(View.INVISIBLE);
			}
		});
	}

	/*
	 * Display data
	 **/
	private void displayData(List<RoomEntity> apiRooms) {

		List<RoomEntity> rooms;
		if (apiRooms == null) {

			rooms = new ArrayList<>();
		} else {

			rooms = apiRooms;
			llRoomLayout.setVisibility(View.VISIBLE);
		}

		RoomEntity room = new RoomEntity(getString(R.string.room_no));
		List<RoomEntity> beds = new ArrayList<>();
		RoomEntity bed = new RoomEntity();
		bed.setBedNumber(getString(R.string.bed_no));
		beds.add(bed);
		room.setBeds(beds);
		rooms.add(0, room);
		spnRoomNo.setAdapter(new RoomAdapter(this, rooms, true));
		spnBedNo.setAdapter(new RoomAdapter(this, rooms.get(0).getBeds(), false));
	}

	/**
	 * Finish setup
	 */
	private void finishSetup() {

		// Check valid
		if (mBedEntity == null || mRoomEntity == null) {

			Utilities.showAlertDialog(this, getString(R.string.app_name), getString(R.string.please_input),
					getString(R.string.ok), "", null, null, false);
			return;
		}

		CustomPreferences.setPreferences(Constants.PREF_IS_FIRST_SETUP, false);
		CustomPreferences.setPreferences(Constants.PREF_PATIENT_BED_NO, new Gson().toJson(mBedEntity));
		CustomPreferences.setPreferences(Constants.PREF_PATIENT_ROOM_NO, new Gson().toJson(mRoomEntity));
		Intent intent = new Intent(this, QuestionActivity.class);
		startActivity(intent);
		finish();
	}
}
