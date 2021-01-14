package sg.LIZ.backend.controller;

import java.sql.SQLException;

import io.jsonwebtoken.Claims;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import sg.LIZ.backend.Auth;
import sg.LIZ.backend.model.utilityBean.TaskDB;
import sg.LIZ.backend.model.valueBean.Task;

@Path("task")
public class TaskAPI {

	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON_PATCH_JSON)
	public Task getTaskById(@HeaderParam("authorization")String token,@PathParam("id")int id) throws SQLException {
		Claims claims = Auth.verifyToken(token);
		return TaskDB.getTask(id,claims.get("id",Integer.class));
	}
	
	
}
