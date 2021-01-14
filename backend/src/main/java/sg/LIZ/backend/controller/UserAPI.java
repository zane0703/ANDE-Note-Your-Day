package sg.LIZ.backend.controller;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import sg.LIZ.backend.Config;
import sg.LIZ.backend.model.utilityBean.UserDB;
import sg.LIZ.backend.model.valueBean.User;

import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
@Path("user")
public class UserAPI {
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void addUser( @FormParam("username") String username,@FormParam("Password")String password) throws SQLException {
		UserDB.addUser(new User(username,BCrypt.hashpw(password, BCrypt.gensalt())));
	}
	@Path("login")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String loginUser(@FormParam("username") String username,@FormParam("Password")String password) throws SQLException {
		User user = UserDB.getUserByName(username);
		if(BCrypt.checkpw(password, user.password)) {
			return Jwts.builder().claim("id", user.id)
			.setIssuedAt(new java.util.Date())
			.setExpiration(new java.util.Date(System.currentTimeMillis()+86400))
			.signWith(SignatureAlgorithm.HS256,Config.JWT_SECRET).compact();
			}else {
				throw new WebApplicationException(401);
		
	}
	}
}
