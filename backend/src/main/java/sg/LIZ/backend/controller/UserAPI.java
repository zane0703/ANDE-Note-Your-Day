package sg.LIZ.backend.controller;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sg.LIZ.backend.Auth;
import sg.LIZ.backend.Config;
import sg.LIZ.backend.model.utilityBean.UserDB;
import sg.LIZ.backend.model.valueBean.User;

import java.sql.SQLException;

import javax.net.ssl.HttpsURLConnection;

import org.mindrot.jbcrypt.BCrypt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
@Path("user")
public class UserAPI {
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addUser( @FormParam("username") String username,@FormParam("password")String password) throws SQLException {
		UserDB.addUser(new User(username,BCrypt.hashpw(password, BCrypt.gensalt())));
		return Response.status(201).build();
	}
	@Path("login")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String loginUser(@FormParam("username") String username,@FormParam("Password")String password) throws SQLException {
		User user = UserDB.getUserByName(username);
		if(BCrypt.checkpw(password, user.password)) {
			return Jwts.builder().claim(Auth.KEY_ID, user.id)
			.setIssuedAt(new java.util.Date())
			.setExpiration(new java.util.Date(System.currentTimeMillis()+86400))
			.signWith(SignatureAlgorithm.HS256,Config.JWT_SECRET).compact();
			}else {
				throw new WebApplicationException(401);
		
	}
	}
	@Path("name/{name}")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public boolean isUserExist(@PathParam("name")String name) throws SQLException {
		return UserDB.isUserExist(name);
	}
}
