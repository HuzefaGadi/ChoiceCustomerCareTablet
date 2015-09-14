package com.unfc.choicecustomercaretb.entity;

/**
 * Hai Nguyen - 8/28/15.
 */
public class PatientEntity extends BaseEntity {

	private String bedId;
	private String roomId;
	private String deviceId;
	private String lastName;
	private String firstName;

	public String getBedNo() {
		return bedId;
	}

	public String getRoomNo() {
		return roomId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setBedNo(String bedNo) {
		bedNo = bedNo;
	}

	public void setRoomNo(String roomNo) {
		roomNo = roomNo;
	}

	public void setDeviceId(String deviceId) {
		deviceId = deviceId;
	}

	public void setLastName(String lastName) {
		lastName = lastName;
	}

	public void setFirstName(String firstName) {
		firstName = firstName;
	}
}
