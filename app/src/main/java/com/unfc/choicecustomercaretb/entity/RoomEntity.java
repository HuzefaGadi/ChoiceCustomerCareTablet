package com.unfc.choicecustomercaretb.entity;

import java.util.List;

/**
 * Hai Nguyen - 8/30/15.
 */
public class RoomEntity extends BaseEntity {

	private String name;
	private String roomId;
	private String assetTag;
	private String bedNumber;
	private String locationId;
	private List<RoomEntity> beds;

	public RoomEntity() {
	}

	public RoomEntity(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getRoomId() {
		return roomId;
	}

	public String getAssetTag() {
		return assetTag;
	}

	public String getBedNumber() {
		return bedNumber;
	}

	public String getLocationId() {
		return locationId;
	}

	public List<RoomEntity> getBeds() {
		return beds;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBeds(List<RoomEntity> beds) {
		this.beds = beds;
	}

	public void setBedNumber(String bedNumber) {
		this.bedNumber = bedNumber;
	}
}
