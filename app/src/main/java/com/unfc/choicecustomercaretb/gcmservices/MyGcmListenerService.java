/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.unfc.choicecustomercaretb.gcmservices;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;


import com.google.android.gms.gcm.GcmListenerService;
import com.unfc.choicecustomercaretb.R;
import com.unfc.choicecustomercaretb.activity.HomeActivity;
import com.unfc.choicecustomercaretb.utility.Constants;

import java.util.Date;

public class MyGcmListenerService extends GcmListenerService {

	/**
	 * Called when message is received.
	 *
	 * @param from
	 *            SenderID of the sender.
	 * @param data
	 *            Data bundle containing message data as key/value pairs. For
	 *            Set of keys use data.keySet().
	 */
	@Override
	public void onMessageReceived(String from, Bundle data) {

		Intent intent = new Intent(Constants.INTENT_UPDATE_REQUEST_LIST);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		sendNotification("Your request will be processed by"+data.getString("acceptedBy"));
	}

	/**
	 * Create and show a simple notification containing the received GCM
	 * message.
	 *
	 * @param message
	 *            GCM message received.
	 */
	 private void sendNotification(String message) {

	 Intent intent = new Intent(this, HomeActivity.class);
	 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
	 PendingIntent.FLAG_ONE_SHOT);

	 Uri defaultSoundUri =
	 RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	 NotificationCompat.Builder notificationBuilder = new
	 NotificationCompat.Builder(this)
	 .setSmallIcon(R.drawable.app_icon).setContentTitle("Your request has been accepted!!").setContentText(message)
	 .setAutoCancel(true).setSound(defaultSoundUri).setContentIntent(pendingIntent);

	 NotificationManager notificationManager = (NotificationManager)
	 getSystemService(Context.NOTIFICATION_SERVICE);
	 notificationManager.notify((int)new Date().getTime(), notificationBuilder.build());
	 }
}
