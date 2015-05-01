package com.classPackage.poll.db;

import com.classPackage.poll.logic.poll;
import java.util.List;

public interface PollDAO {
	public void createTable();
	public void insertPollRecord(poll poll);
	public void deletePoll(int roomId, int ordering);
	public List<poll> getAllPollsInRoom(int roomId);
}
