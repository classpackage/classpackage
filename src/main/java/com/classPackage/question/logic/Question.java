package com.classPackage.question.logic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.classPackage.utils.DateTimeUtils;

public class Question {
	
	int questionId;
	int roomId;
	int ownerId;
	String questionAsked;
	String timeQuestion;
	int questionRating;
	
	/**
	 * @param questionId
	 * @param roomId
	 * @param ownerId
	 * @param questionAsked
	 * @param timeQuestion
	 * @param questionRating
	 */
	public Question(int questionId, int roomId, int ownerId,
			String questionAsked, String timeQuestion, int questionRating) {
		this.questionId = questionId;
		this.roomId = roomId;
		this.ownerId = ownerId;
		this.questionAsked = questionAsked;
		this.timeQuestion = timeQuestion;
		this.questionRating = questionRating;
	}
	
	/**
	 * @param questionId
	 * @param roomId
	 * @param ownerId
	 * @param timeQuestion
	 */
	public Question(int roomId, int ownerId, String questionAsked, int questionRating) {
		this.roomId = roomId;
		this.ownerId = ownerId;
		this.questionAsked = questionAsked;
		this.questionRating = questionRating;
		setTimeQuestion();
	}
	/**
	 * @return the questionId
	 */
	public int getQuestionId() {
		return questionId;
	}
	/**
	 * @param questionId the questionId to set
	 */
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	/**
	 * @return the roomId
	 */
	public int getRoomId() {
		return roomId;
	}
	/**
	 * @param roomId the roomId to set
	 */
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	/**
	 * @return the ownerId
	 */
	public int getOwnerId() {
		return ownerId;
	}
	/**
	 * @param ownerId the ownerId to set
	 */
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	/**
	 * @return the timeQuestion
	 */
	public String getTimeQuestion() {
		return timeQuestion;
	}
	/**
	 * @param timeQuestion the timeQuestion to set
	 */
	public void setTimeQuestion() {
		DateFormat dateFormat = new SimpleDateFormat(DateTimeUtils.formater);
		Date date = new Date();
		this.timeQuestion = dateFormat.format(date);
	}
	/**
	 * @return the questionAsked
	 */
	public String getQuestionAsked() {
		return questionAsked;
	}
	/**
	 * @param questionAsked the questionAsked to set
	 */
	public void setQuestionAsked(String questionAsked) {
		this.questionAsked = questionAsked;
	}
	/**
	 * @return the questionRating
	 */
	public int getQuestionRating() {
		return questionRating;
	}
	/**
	 * @param questionRating the questionRating to set
	 */
	public void setQuestionRating(int questionRating) {
		this.questionRating = questionRating;
	}
}
