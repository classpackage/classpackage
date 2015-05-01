package com.classPackage.pollAnswer.logic;

public class PollAnswer {
	final int pollId;
	int userId;
	int answer;

/**
	 * @param pollId
	 * @param userId
	 * @param answer
	 */
	public PollAnswer(int pollId, int userId, int answer) {
		super();
		this.pollId = pollId;
		this.userId = userId;
		this.answer = answer;
	}
/**
 * @return the pollId
 */
public int getPollId() {
	return pollId;
}

/**
 * @return the userId
 */
public int getUserId() {
	return userId;
}
/**
 * @param userId the userId to set
 */
public void setUserId(int userId) {
	this.userId = userId;
}
/**
 * @return the answer
 */
public int getAnswer() {
	return answer;
}
/**
 * @param answer the answer to set
 */
public void setAnswer(int answer) {
	this.answer = answer;
}
}
