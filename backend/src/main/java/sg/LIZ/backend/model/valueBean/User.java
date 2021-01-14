package sg.LIZ.backend.model.valueBean;

import java.sql.Date;

public class User {
	public final int id;
	public final String username;
	public final String password;
	public final Date createdAt;
	public User (int id ,String username,String password,Date createdAt) {
		this.id =id;
		this.username=username;
		this.password=password;
		this.createdAt= createdAt;
	}
	public User(String username,String password) {
		this(-1, username, password, null);
	}
}
