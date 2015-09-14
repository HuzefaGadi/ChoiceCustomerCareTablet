package com.unfc.choicecustomercaretb.entity;

/**
 * Hai Nguyen - 8/28/15.
 */
public class MessageEntity extends BaseEntity {

	private int clientId;
	private int patientId;
	private int responderId;
	private int messageTypeId;

	private String sent;
	private String responded;
	private String fullFilled;
	private String messageText;

	public int getClientId() {
		return clientId;
	}

	public int getPatientId() {
		return patientId;
	}

	public int getResponderId() {
		return responderId;
	}

	public int getMessageTypeId() {
		return messageTypeId;
	}

	public String getSent() {
		return sent;
	}

	public String getResponded() {
		return responded;
	}

	public String getFullFilled() {
		return fullFilled;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageTypeId(int messageTypeId) {
		messageTypeId = messageTypeId;
	}

	public void setMessageText(String messageText) {
		messageText = messageText;
	}

}
