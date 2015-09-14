package com.unfc.choicecustomercaretb.entity;

import java.util.List;

/**
 * Hai Nguyen - 8/30/15.
 */
public class QuestionEntity extends BaseEntity {

	private String name;
	private String title;
	private String value;
	private String questionId;
	private List<QuestionEntity> answers;

	public String getName() {
		return name;
	}

	public String getTitle() {
		return title;
	}

	public String getValue() {
		return value;
	}

	public String getQuestionId() {
		return questionId;
	}

	public List<QuestionEntity> getAnswers() {
		return answers;
	}
}
