package com.classPackage.resources;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.classPackage.dbUtils.DBHelper;
import com.classPackage.user.logic.User;
import com.classPackage.room.logic.Room;
import com.classPackage.userInRoom.logic.UserInRoom;


public final class Initializer implements ServletContextListener {
	private DBHelper DBHelper;
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		try {
			DBHelper =  com.classPackage.dbUtils.DBHelper.getInstance();
			  sce.getServletContext().setAttribute("DBHelper", DBHelper);
			DBHelper.getUserDb().insertUserRecord(new User("rotem", "zaig", "123","aaa@aaa.com", true));
		        DBHelper.getUserDb().insertUserRecord(new User("keren", "f", "1233","bb@bb.com", true));
		        DBHelper.getUserDb().insertUserRecord(new User("stud", "stud", "1233","stud@stud.com", false));
		        DBHelper.getRoomDb().insertRoomRecord(new Room(1,"12", "math","2015/03/31 18:45:00", "2015/03/31 19:00:00", false, false));
		        DBHelper.getUserInRoomDb().insertUserInRoomRecord(new UserInRoom(1000,1));
		        DBHelper.getUserInRoomDb().insertUserInRoomRecord(new UserInRoom(1000,2));
		        DBHelper.getUserInRoomDb().insertUserInRoomRecord(new UserInRoom(1000,3));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
