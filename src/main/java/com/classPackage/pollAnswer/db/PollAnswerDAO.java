package com.classPackage.pollAnswer.db;

import com.classPackage.pollAnswer.logic.PollAnswer;

public interface PollAnswerDAO {
  public void createTable();
  public void insertpollAnswerRecord(PollAnswer pollAnswer);
  public int numberOfPollsInRoom(int roomId);
  public int numberOfParcipentsInPoll(int roomId, int pollId);
  public double getAverageOfPollAnswer(int roomId, int pollId);
  
}