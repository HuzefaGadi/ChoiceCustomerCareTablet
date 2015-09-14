package com.unfc.choicecustomercaretb.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.unfc.choicecustomercaretb.R;
import com.unfc.choicecustomercaretb.gcmservices.RegistrationIntentService;
import com.unfc.choicecustomercaretb.utility.Constants;
import com.unfc.choicecustomercaretb.utility.CustomPreferences;

public class SplashActivity extends BaseActivity {

	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	@Override
	protected int addLayoutView() {
		View decorView = getWindow().getDecorView();
		int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
		decorView.setSystemUiVisibility(uiOptions);
		return R.layout.activity_splash;
	}

	@Override
	protected void initComponents() {

		// Register gcm
		if (!checkPlayServices()) {

			return;
		}

		Intent intent = new Intent(this, RegistrationIntentService.class);
		startService(intent);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {

				boolean isFirstSetup = CustomPreferences.getPreferences(Constants.PREF_IS_FIRST_SETUP, true);
				if (isFirstSetup) {

					startActivity(new Intent(SplashActivity.this, FirstSetupActivity.class));
					finish();
					return;
				}

				String strQuestion = CustomPreferences.getPreferences(Constants.PREF_PATIENT_QUESTION, "");
				Intent intent;
				if (strQuestion == null || strQuestion.equals("")) {

					intent = new Intent(SplashActivity.this, QuestionActivity.class);
				} else {

					intent = new Intent(SplashActivity.this, HomeActivity.class);
					intent.putExtra(Constants.INTENT_PATIENT_QUESTION, strQuestion);
				}

				startActivity(intent);
				finish();
			}
		}, 2000);

	}

	/**
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {

		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {

				GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {

				finish();
			}

			return false;
		}

		return true;
	}
}
