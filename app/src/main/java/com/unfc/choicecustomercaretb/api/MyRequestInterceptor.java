package com.unfc.choicecustomercaretb.api;

import retrofit.RequestInterceptor;

import com.unfc.choicecustomercaretb.utility.Constants;
import com.unfc.choicecustomercaretb.utility.CustomPreferences;

/**
 * Hai Nguyen - 8/27/15.
 */
public class MyRequestInterceptor implements RequestInterceptor {

	@Override
	public void intercept(RequestFacade requestFacade) {

		String accessToken = CustomPreferences.getPreferences(Constants.PREF_USER_TOKEN, "");
		requestFacade.addHeader("Authorization", "Bearer " + accessToken);
	}
}
