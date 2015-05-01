package com.classPackage.userInRoom.db;
import com.classPackage.userInRoom.logic.UserInRoom;
import java.util.List;

public interface UserInRoomDAO {
	public void createTable();
	public int insertUserInRoomRecord(UserInRoom UserInRoom);
	public void deleteUserInRoom(int userId, int roomId);
	public List <UserInRoom> getAllUsersInRoom(int roomId);
	public double getRateAvg(int roomId);
	public void updateUserInRoomRate(int userId, int roomId, int rate);
}

