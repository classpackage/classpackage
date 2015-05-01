package com.classPackage.resources;

import java.util.List;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.classPackage.room.logic.Room;
import com.classPackage.userInRoom.logic.UserInRoom;
import com.classPackage.utils.GenericResponse;
import com.classPackage.utils.ServletUtils;

@Path("/room")
public class ApiRoom {
	/**
	 * This api create a room in the system. http://[hostname]:8080//room
	 * 
	 * @return a json with user_id.
	 */
	@Context
	ServletContext ctx;

	@GET
	@Path("/createRoom/{roomName}")
	@Produces(MediaType.APPLICATION_JSON)
	public String insert(@Context javax.servlet.http.HttpServletRequest req,
			@QueryParam("start") String startDate,
			@QueryParam("end") String endDate,
			@QueryParam("isPoll") boolean isPolls,
			@QueryParam("isQuestion") boolean isQuestions,
			@PathParam("roomName") String roomName) {

		System.out.println(startDate + "--" + endDate + "--" + roomName);
		Room room = null;
		HttpSession session = req.getSession(false);
		try {
			int ownerId = Integer.valueOf(session.getAttribute("userId")
					.toString());
			String password = genaratePassword();
			room = new Room(ownerId, password, roomName, startDate, endDate,
					isPolls, isQuestions);
			int serialNum = ServletUtils.getClassPackageDB(ctx).getRoomDb()
					.insertRoomRecord(room);
			System.out.println("insert room query returned, room number:: "
					+ serialNum);
			room.setRooomNumber(serialNum);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return GenericResponse.error(e.getMessage());
		}
		// need to check dates!!
		return GenericResponse.ok(room);
	}

	@GET
	@Path("/myRooms")
	@Produces(MediaType.APPLICATION_JSON)
	public String getMyRooms(@Context javax.servlet.http.HttpServletRequest req) {
		int ownerId = 0;
		List<Room> rooms = null;
		HttpSession session = req.getSession(false);
		if (session != null) {
			try {
				ownerId = Integer.valueOf(session.getAttribute("userId")
						.toString());
				System.out.println("Getting all the rooms of:: " + ownerId);
				rooms = ServletUtils.getClassPackageDB(ctx).getRoomDb()
						.getAllRoomsByOwner(ownerId);
				System.out
						.println("get all my rooms query returned, list size:: "
								+ rooms.size());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				System.err.println(e.getMessage());
				return GenericResponse.error(e.getMessage());
			}
			// need to check dates!!
			return GenericResponse.ok(rooms);
		}
		return GenericResponse.error("session is not initiated yet");
	}

	@GET
	@Path("/joinRoomLecturer/{roomNumber}")
	@Produces(MediaType.APPLICATION_JSON)
	public String joinRoomLecturer(
			@Context javax.servlet.http.HttpServletRequest req,
			@PathParam("roomNumber") int roomId) {
		try {
			ServletUtils.getClassPackageDB(ctx).getRoomDb()
					.updateIsAutorized(roomId, true);
			HttpSession session = req.getSession(true);
			if (session == null) {
				return GenericResponse.error("session is not exist");
			}
			session.setAttribute("roomNum", roomId);
			ServletUtils
					.getClassPackageDB(ctx)
					.getUserInRoomDb()
					.insertUserInRoomRecord(
							new UserInRoom(roomId, Integer.valueOf(session
									.getAttribute("userId").toString())));
		} catch (IllegalArgumentException ex) {
			System.out.println("error in update room number in session");
			return GenericResponse.error("session is not exist");
		}

		return GenericResponse.ok(GenericResponse.ok("session was updated"));
	}

	@GET
	@Path("/{roomNumber}")
	@Produces(MediaType.APPLICATION_JSON)
	public String RoomByNumber(
			@Context javax.servlet.http.HttpServletRequest req,
			@PathParam("roomNumber") int roomId) {
		Room room = null;
		try {
			room = ServletUtils.getClassPackageDB(ctx).getRoomDb()
					.getRoomByNumber(roomId);
			System.out.println("get room by room number query returned");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			return GenericResponse.error(e.getMessage());
		}
		// need to check dates!!
		return GenericResponse.ok(room);
	}

	@POST
	@Path("/joinRoom")
	@Produces(MediaType.APPLICATION_JSON)
	public String joinRoomStudent(
			@Context javax.servlet.http.HttpServletRequest req,
			@FormParam("join-room-number") int roomId,
			@FormParam("join-room-password") String password) {
		HttpSession session = req.getSession(true);
		try {
			if (ServletUtils.getClassPackageDB(ctx).getRoomDb()
					.checkJoinRoomValid(roomId, password)) {
				if(session!=null) {
					session.setAttribute("roomNum", roomId);
				}
				else {
					throw new IllegalArgumentException("session doesn't exist"); 
				}
			}
		} catch (IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
			return GenericResponse.error(ex.getMessage());
		}
		System.out.println("joining room is valid");
		ServletUtils
				.getClassPackageDB(ctx)
				.getUserInRoomDb()
				.insertUserInRoomRecord(
						new UserInRoom(roomId, Integer.valueOf(session
								.getAttribute("userId").toString())));
		return GenericResponse.ok("student can now join room");
	}

	private String genaratePassword() {
		final String signs = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final Random rnd = new Random();
		final int len = 5;

		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(signs.charAt(rnd.nextInt(signs.length())));
		return sb.toString();
	}

}
