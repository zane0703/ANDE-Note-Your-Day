package sg.LIZ.backend.model.utilityBean;
import sg.LIZ.backend.Config;
import sg.LIZ.backend.model.valueBean.User;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

import org.glassfish.hk2.api.DynamicConfiguration;

import jakarta.ws.rs.NotFoundException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class UserDB {
public static void addUser(User user) throws SQLException {
	Connection conn = DriverManager.getConnection(Config.DB_HOSTNAME, Config.DB_PROPERTIES);
	PreparedStatement stmt=conn.prepareStatement("INSERT INTO USER(username,password) VALUES(?,?) ;");
	stmt.setString(1, user.username);
	stmt.setString(2, user.password);
	stmt.execute();
	conn.close();
	
}
public static User getUserByName(String username ) throws SQLException {
	Connection conn = DriverManager.getConnection(Config.DB_HOSTNAME, Config.DB_PROPERTIES);
	PreparedStatement stmt=conn.prepareStatement("SELECT id,password,created_at FROM user WHERE username=? ;");
	stmt.setString(1, username);
	ResultSet rs = stmt.executeQuery();
	if(rs.next()) {
		User user = new User(rs.getInt(1), username,rs.getString(2),rs.getDate(3));
		conn.close();
		return user;
	}else {
		conn.close();
		throw new NotFoundException();
	}
}

}
