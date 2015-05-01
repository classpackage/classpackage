package com.classPackage.question.db;

import java.util.List;

import com.classPackage.question.logic.Question;

public interface QuestionDAO {
	public void createTable();
	public int insertQuestionRecored(Question question);
	public void updateQuestionRating(int questionRating, int roomId, String timeAsked);
//	public List<Question> getAllQuesiotionsInRoom(int roomId);
	// changed - should be by roomId or userId--- check!!
	public List<Question> getAllQuesiotionsByCritetia(String columnName, int value);
	public int countQuestionInRoom(int roomId);
	
}
