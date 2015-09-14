package com.unfc.choicecustomercaretb.utility;

/**
 * Hai Nguyen - 8/29/15.
 */
public enum RequestType {

	FOOD(1), DRINK(2), BATHROOM(3), PAIN(4), OTHER(5);

	private final int mValue;

	RequestType(final int value) {
		this.mValue = value;
	}

	/**
	 * Get value
	 */
	public int getValue() {

		return mValue;
	}
}
