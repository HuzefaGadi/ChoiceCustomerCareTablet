package com.unfc.choicecustomercaretb.utility;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Hai Nguyen - 8/25/15.
 */
public class Utilities {

	/**
	 * Checks whether mobile is connected to Internet and returns true if
	 * connected
	 *
	 * @param context
	 *            {@link Context}
	 * @return <code>true</code> if connected
	 */
	public static boolean isNetworkConnected(Context context) {

		ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
		return networkInfo != null;
	}

	/**
	 * Dismiss dialog
	 */
	public static void dismissDialog(Dialog dialog) {

		try {

			dialog.dismiss();
		} catch (Exception e) {

			LogUtil.e("Dismiss dialog", "Dismiss dialog");
		}
	}

	/**
	 * Show alert dialog
	 *
	 * @param title
	 *            Dialog title
	 * @param message
	 *            Dialog message
	 * @param positiveText
	 *            Positive button text
	 * @param negativeText
	 *            Negative button text
	 * @param positiveButtonClick
	 *            Positive button click listener
	 * @param negativeButtonClick
	 *            Negative button click listener
	 * @param isCancelAble
	 *            True if can cancel
	 */
	public static void showAlertDialog(Context context, String title, String message, String positiveText,
			String negativeText, DialogInterface.OnClickListener positiveButtonClick,
			DialogInterface.OnClickListener negativeButtonClick, boolean isCancelAble) {

		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
		dialogBuilder.setCancelable(isCancelAble);
		dialogBuilder.setMessage(message);
		if (!title.equals("")) {

			dialogBuilder.setTitle(title);
		}

		// Positive button
		if (!positiveText.equals("")) {

			if (positiveButtonClick != null) {

				dialogBuilder.setPositiveButton(positiveText, positiveButtonClick);
			} else {

				dialogBuilder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});
			}
		}

		// Negative button
		if (!negativeText.equals("")) {

			if (negativeButtonClick != null) {

				dialogBuilder.setNegativeButton(negativeText, negativeButtonClick);
			} else {

				dialogBuilder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});
			}
		}

		AlertDialog dialog = dialogBuilder.create();
		dialog.show();
	}

	/**
	 * Reset information
	 */
	public static void resetInfo() {

		CustomPreferences.setPreferences(Constants.PREF_PATIENT_ID, 0);
		CustomPreferences.setPreferences(Constants.PREF_PATIENT_QUESTION, "");
	}
}
