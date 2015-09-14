package com.unfc.choicecustomercaretb.api;

import com.unfc.choicecustomercaretb.utility.Constants;
import com.unfc.choicecustomercaretb.utility.CustomPreferences;

import retrofit.RestAdapter;
import retrofit.android.AndroidLog;

/**
 * Hai Nguyen - 8/27/15.
 */
public class BaseApi {

	protected ApiInterface mInterface;
	protected RestAdapter mRestAdapter;
	public BaseApi(boolean hasHeader) {

		String baseUrl = CustomPreferences.getPreferences(Constants.PREF_BASE_URL, "");
		RestAdapter.Builder builder = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL)
				.setLog(new AndroidLog("BaseApi:")).setEndpoint(baseUrl);
		if (hasHeader) {

			builder.setRequestInterceptor(new MyRequestInterceptor());
		}

		mRestAdapter = builder.build();
		mInterface = mRestAdapter.create(ApiInterface.class);
	}

	public ApiInterface getInterface() {
		return mInterface;
	}
}
