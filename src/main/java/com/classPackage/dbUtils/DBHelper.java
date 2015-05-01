package com.classPackage.dbUtils;

import java.sql.Connection;

import com.classPackage.poll.db.PollDb;
import com.classPackage.question.db.QuestionDb;
import com.classPackage.user.db.UserDb;
import com.classPackage.userInRoom.db.UserInRoomDb;
import com.classPackage.room.db.RoomDb;
import com.classPackage.pollAnswer.db.PollAnswerDb;

public class DBHelper {
	private static DBHelper instance = null;
	private Connection conn;
	private UserDb userDb;
	private UserInRoomDb userInRoomDb;
	private RoomDb roomDb;
	private PollDb pollDb; 
	private QuestionDb questionDb; 
	private PollAnswerDb pollAnswerDb; 
	
	protected DBHelper() {
		this.conn = DBConn.getConnection();
		this.userDb = new UserDb(conn);
		this.userInRoomDb = new UserInRoomDb(conn);
		this.roomDb = new RoomDb(conn);
		this.pollDb = new PollDb(conn);
		this.questionDb = new QuestionDb(conn);
		this.pollAnswerDb = new PollAnswerDb(conn);
		createDBTables();
	}

	private void createDBTables()
	{
		this.userDb.createTable();
		this.roomDb.createTable();
		this.userInRoomDb.createTable();
		this.pollDb.createTable();
		this.questionDb.createTable();
		this.pollAnswerDb.createTable();
		
		System.out.println("tables are created or already exist");
	}
	public static DBHelper getInstance() {
		if (instance == null) {
			instance = new DBHelper();
		}
		return instance;
	}

	public UserDb getUserDb() {
		return this.userDb;
	}

	public UserInRoomDb getUserInRoomDb() {
		return this.userInRoomDb;
	}

	public RoomDb getRoomDb() {
		return this.roomDb;
	}
	
	public PollDb getpollDb() {
		return this.pollDb;
	}
	
	public QuestionDb getquestionsDb() {
		return this.questionDb;
	}
	
	public PollAnswerDb pollAnswerDb() {
		return this.pollAnswerDb;
	}
}
