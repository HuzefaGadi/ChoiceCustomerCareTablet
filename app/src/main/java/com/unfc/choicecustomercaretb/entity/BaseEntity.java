package com.unfc.choicecustomercaretb.entity;

import java.io.Serializable;

/**
 * Hai Nguyen - 8/28/15.
 */
public class BaseEntity implements Serializable {

	private int id;
	private String result;

	public String getResult() {
		return result;
	}

	public int getId() {
		return id;
	}
}
