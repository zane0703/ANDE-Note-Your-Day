package sg.LIZ.backend.controller;

import java.sql.Date;
import java.sql.SQLException;

import io.jsonwebtoken.Claims;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sg.LIZ.backend.Auth;
import sg.LIZ.backend.model.utilityBean.TaskDB;
import sg.LIZ.backend.model.valueBean.Task;

@Path("task")
public class TaskAPI {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Task[] getTask(@HeaderParam("Authorization")final String token) throws SQLException {
		Claims claims = Auth.verifyToken(token);
		return TaskDB.getTaskByUser(claims.get(Auth.KEY_ID, Integer.class));
	}
	@Path("friend")
	@Produces(MediaType.APPLICATION_JSON)
	public int[] getFirendSchedule(@HeaderParam("Authorization")final String token,@QueryParam("year")final int year , @QueryParam("month")final int month) throws SQLException {
		Claims claims = Auth.verifyToken(token);
		return TaskDB.getFirendSchedule(claims.get(Auth.KEY_ID, Integer.class), year, month);
	}
	
	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Task getTaskById(@HeaderParam("Authorization")final String token,@PathParam("id") final int id) throws SQLException {
		Claims claims = Auth.verifyToken(token);
		return TaskDB.getTask(id,claims.get(Auth.KEY_ID,Integer.class));
	}
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addTask(@HeaderParam("Authorization") final String token ,@FormParam("title")final String title,@FormParam("description") final String description ,@FormParam("venue")final String venue,@FormParam("start")final long start ,@FormParam("end")final long end,@FormParam("allDay") boolean allDay) throws SQLException {
		Claims claims = Auth.verifyToken(token);
		TaskDB.addTask(new Task(claims.get(Auth.KEY_ID,Integer.class), title, description, venue, new Date(start), new Date(end),allDay));
		return Response.status(201).build();
	}
	@Path("{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void updateTask(@HeaderParam("Authorization") final String token,@PathParam("id") final int id ,@FormParam("title")final String title,@FormParam("description") final String description ,@FormParam("venue")final String venue,@FormParam("start")final long start ,@FormParam("end")final long end,@FormParam("allDay") boolean allDay) throws SQLException {
		Claims claims = Auth.verifyToken(token);
		TaskDB.updateTask(new Task(id,claims.get(Auth.KEY_ID, Integer.class), title, description, venue, new Date(start), new Date(end), allDay,null));;
	}
	@Path("{id}")
	@DELETE
	public void deleteTask(@HeaderParam("Authorization") final String token ,@PathParam("id") final int id) throws SQLException {
		Claims claims = Auth.verifyToken(token);
		TaskDB.deleteTask(id, claims.get(Auth.KEY_ID, Integer.class));
	}
}
