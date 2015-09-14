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

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.unfc.choicecustomercaretb.R;
import com.unfc.choicecustomercaretb.utility.Constants;
import com.unfc.choicecustomercaretb.utility.CustomPreferences;

public class RegistrationIntentService extends IntentService {

	private static final String TAG = "RegIntentService";

	public RegistrationIntentService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		try {

			InstanceID instanceID = InstanceID.getInstance(this);
			String token = instanceID.getToken(getString(R.string.gcm_sender_id),
					GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
			CustomPreferences.setPreferences(Constants.PREF_PUSH_TOKEN, token);
		} catch (Exception e) {

			CustomPreferences.setPreferences(Constants.PREF_PUSH_TOKEN, "");
		}
	}
}
