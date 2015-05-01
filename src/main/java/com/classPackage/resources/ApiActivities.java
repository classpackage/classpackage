package com.classPackage.resources;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.classPackage.utils.GenericResponse;
import com.classPackage.utils.ServletUtils;

@Path("/activity")
public class ApiActivities {
	/**
	 * This api create a activity in room in the system. http://[hostname]:8080//room
	 * 
	 * @return a json with user_id.
	 */
	@Context
	ServletContext ctx;

	@GET
	@Path("/updateRate/{rate}")
	@Produces(MediaType.APPLICATION_JSON)
	public String updateRate(@Context javax.servlet.http.HttpServletRequest req,
			@PathParam("rate") int rate,
			@QueryParam("roomNum") int roomNum) {

		HttpSession session = req.getSession(false);
		try {
			if(session != null ) {
			int userId = Integer.valueOf(session.getAttribute("userId")
					.toString());
			System.out.println(userId+ " "+ roomNum + " " + rate);
			ServletUtils.getClassPackageDB(ctx).getUserInRoomDb().updateUserInRoomRate(userId, roomNum, rate);
			System.out.println("update rate query returned:: user id:" + userId + 
					"room number: "+ roomNum +", rate: "+ rate);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return GenericResponse.error(e.getMessage());
		}
		
		return GenericResponse.ok("rate updated");
	}
}
