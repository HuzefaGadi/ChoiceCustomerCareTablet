package com.unfc.choicecustomercaretb.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.unfc.choicecustomercaretb.R;

/**
 * 20/7/15.
 */
public class FoodDialog extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View decorView = getWindow().getDecorView();
		int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
		decorView.setSystemUiVisibility(uiOptions);
		setContentView(R.layout.food_dialog);

		Button btnOk = (Button) findViewById(R.id.btnOk);
		Button btnCancel = (Button) findViewById(R.id.btn_cancel);
		TextView tvClose = (TextView) findViewById(R.id.tv_close);
		tvClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sendRequest();
			}
		});

	}

	private void sendRequest() {
	}
}
