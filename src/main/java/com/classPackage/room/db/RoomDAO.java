package com.classPackage.room.db;

import java.util.List;

import com.classPackage.room.logic.Room;

public interface RoomDAO {
	public void createTable();
	public int insertRoomRecord(Room Room);
	public Room getRoomByNumber(int roomId);
	public Boolean checkJoinRoomValid(int roomId, String password);
	public void insertFeaturesToRoom(int roomId, boolean isPolls, boolean isQuestions);
	public void updateRoomData(String name, String end, String Start, int roomId);
	public void deleteRoom(int roomId);
	public List <Room> getAllRoomsByOwner(int ownerId);
	public void updateIsAutorized(int roomId, boolean isAutorized);
}
