package com.classPackage.resources;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.classPackage.user.logic.User;
import com.classPackage.utils.ServletUtils;
import com.classPackage.utils.GenericResponse;

@Path("/user")
public class ApiUser {
	/**
	 * This api create a user in the system. http://[hostname]:8080//user
	 * 
	 * @return a json with user_id.
	 */
	@Context
	ServletContext ctx;

	@POST
	@Path("/register")
	@Produces(MediaType.APPLICATION_JSON)
	public String insert(@Context javax.servlet.http.HttpServletRequest req,
			@FormParam("first_name") String firstName,
			@FormParam("last_name") String lastName,
			@FormParam("email") String email,
			@FormParam("password") String password,
			@FormParam("confirm-password") String confirmPassword,
			@FormParam("isLecturer") String isLecturer) {
		// insert to db
		User user = null;
		HttpSession session = req.getSession(true);
		session.setMaxInactiveInterval(60 * 60 * 24 * 14); // 2 weeks
		int userId = -1;
		try {
			checkPasswords(password, confirmPassword);
			System.out.println(isLecturer);
			if (isLecturer.equals("lecturer")) {
				user = new User(firstName, lastName, password, email, true);
			} else {
				user = new User(firstName, lastName, password, email, false);
			}
			userId = ServletUtils.getClassPackageDB(ctx).getUserDb()
					.insertUserRecord(user);
			if (userId < 1) {
				return GenericResponse.error("user already exists.");
			} else {
				session.setAttribute("userId", userId);
				session.setAttribute("email", email);
				session.setAttribute("isLecturer", isLecturer);
				user.setSerialNum(userId);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return GenericResponse.error(e.getMessage());
		}
		return GenericResponse.ok(user);
	}

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@Context javax.servlet.http.HttpServletRequest req,
			@FormParam("email") String email,
			@FormParam("password") String password) {
		try {
			// db login check
			System.out.println("login info " + email + " - " + password + " - "
					+ password);
			User user = ServletUtils.getClassPackageDB(ctx).getUserDb()
					.loginCheck(email, password);
			if (user != null) {
				String isLecturer = user.getIsLecturer() == true ? "lecturer"
						: "student";
				HttpSession session = req.getSession(true);
				session.setMaxInactiveInterval(60 * 60 * 24 * 14); // 2 weeks
				session.setAttribute("userId", user.getSerialNum());
				session.setAttribute("email", email);
				session.setAttribute("isLecturer", isLecturer);
				return Response.ok(GenericResponse.ok(user),
						MediaType.APPLICATION_JSON).build();
			}
			return Response.status(Response.Status.UNAUTHORIZED).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}


@GET
@Path("/updateProfileSetting/{serialNum}")
@Produces(MediaType.APPLICATION_JSON)
public String updateProfileSetting(
		@Context javax.servlet.http.HttpServletRequest req,
		@PathParam("serialNum") int serialNum,
		@QueryParam ("email") String email,
		@QueryParam ("oldPassword") String oldPassword,
		@QueryParam ("newPassword") String newPassword,
		@QueryParam ("type") String type
		) {
	
	String errMsg = "can't update field  in user profile";
	try {
		User user = ServletUtils.getClassPackageDB(ctx).getUserDb().getUserByKeyParam("serial_num", serialNum);
		
		if(!email.trim().isEmpty() && !email.equals(user.getEmail())) {
			ServletUtils.getClassPackageDB(ctx).getUserDb().updateUserRecord(serialNum,"email",email);
		}
		
		if( !oldPassword.trim().isEmpty() && !newPassword.trim().isEmpty()){
			 if(ServletUtils.getClassPackageDB(ctx).getUserDb().checkEqualPassword(serialNum, oldPassword) ==false) {
				 throw new IllegalArgumentException(errMsg);
			 }
			 ServletUtils.getClassPackageDB(ctx).getUserDb().updateUserRecord(serialNum,"password",newPassword);
		 }
		
		if(type.trim().isEmpty()==false) {
			boolean userType = type.equals("true")?true:false;
			if(userType != user.getIsLecturer()) {
				ServletUtils.getClassPackageDB(ctx).getUserDb().updateUserRecord(serialNum,"is_lecuterer",String.valueOf(userType));
			}
		}
		
		System.out.println("update user profile by user serial num query returned");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			return GenericResponse.error(e.getMessage());
		}

		return GenericResponse.ok("profile was updated");
	}

@GET
@Path("/{SerialNum}")
@Produces(MediaType.APPLICATION_JSON)
public String UserByNumber(
		@Context javax.servlet.http.HttpServletRequest req,
		@PathParam("SerialNum") int serialNum) {
	User user = null;
	try {
		user = ServletUtils.getClassPackageDB(ctx).getUserDb()
				.getUserByKeyParam("serial_num", serialNum);
		System.out.println("get user by serial number query returned");
	} catch (IllegalArgumentException e) {
		e.printStackTrace();
		System.err.println(e.getMessage());
		return GenericResponse.error(e.getMessage());
	}
	return GenericResponse.ok(user);
}


@GET
@Path("/deleteprofile/{serialNum}")
@Produces(MediaType.APPLICATION_JSON)
public String deleteProfile(
		@Context javax.servlet.http.HttpServletRequest req,
		@PathParam("serialNum") int serialNum){
	try {
		ServletUtils.getClassPackageDB(ctx).getUserDb()
		.deleteRecordFromUsers(serialNum);
		System.out.println("delete user by serial number query done");
	} catch (IllegalArgumentException e) {
		e.printStackTrace();
		System.err.println(e.getMessage());
		return GenericResponse.error(e.getMessage());
	}
	return GenericResponse.ok("user was deleted");
}
		
	private void checkPasswords(String password, String confirmPassword)
			throws IllegalArgumentException {
		if (!password.equals(confirmPassword)) {
			throw new IllegalArgumentException(
					"password and confirm password fields don't match");
		}
	}
}
