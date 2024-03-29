package sg.LIZ.backend.model.utilityBean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.ws.rs.NotFoundException;
import sg.LIZ.backend.Config;
import sg.LIZ.backend.model.valueBean.Task;

public class TaskDB {
	public static void addTask(Task task) throws SQLException {
		Connection conn = DriverManager.getConnection(Config.DB_HOSTNAME, Config.DB_PROPERTIES);
		PreparedStatement stmt = conn.prepareStatement("INSERT INTO task(fk_user_id,title,description,venue,start,end,all_day) VALUES(?,?,?,?,?,?,?) ;");
		stmt.setInt(1, task.fkUserId);
		stmt.setString(2, task.title);
		stmt.setString(3, task.description);
		stmt.setString(4,task.venue);
		stmt.setDate(5, task.start);
		stmt.setDate(6,task.end);
		stmt.setBoolean(7, task.allDay);
		stmt.execute();
		conn.close();
		
	}
	public static Task getTask(int id ,int userId) throws SQLException {
		Connection conn = DriverManager.getConnection(Config.DB_HOSTNAME, Config.DB_PROPERTIES);
		PreparedStatement stmt = conn.prepareStatement("SELECT fk_user_id,title,description,venue,start,end,all_day,created_at FROM task WHERE id=? AND fk_user_id=? ;");
		stmt.setInt(1, id);
		stmt.setInt(2, userId);
		ResultSet rs =stmt.executeQuery();
		if(rs.next()) {
			Task task = new Task(id, rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getDate(5),rs.getDate(6),rs.getBoolean(7),rs.getDate(8));
			conn.close();
			return task;
		}else {
			conn.close();
			throw new NotFoundException();
		}
	}
	public static Task[] getTaskByUser(int userId) throws SQLException {
		Connection conn = DriverManager.getConnection(Config.DB_HOSTNAME, Config.DB_PROPERTIES);
		PreparedStatement stmt = conn.prepareStatement("SELECT id,title,description,venue,start,end,all_day,created_at FROM task WHERE fk_user_id=? ;");
		stmt.setInt(1, userId);
		Task[] tasks = new Task[0];
		ResultSet rs =stmt.executeQuery();
		for(int i=0;rs.next();++i) {
			System.arraycopy(tasks, 0, (tasks = new Task[i+1]), 0, i);
			tasks[i]=new Task( rs.getInt(1),userId, rs.getString(2), rs.getString(3), rs.getString(4), rs.getDate(5),rs.getDate(6),rs.getBoolean(7),rs.getDate(8));
		}
		conn.close();
		return tasks;
	}
	public static void updateTask(Task task) throws SQLException {
		Connection conn = DriverManager.getConnection(Config.DB_HOSTNAME, Config.DB_PROPERTIES);
		PreparedStatement stmt = conn.prepareStatement("UPDATE task SET title=? ,description=?,venue=?,start=?,end=? ,all_day=?WHERE id=? AND fk_user_id=? ;");
		stmt.setString(1, task.title);
		stmt.setString(2, task.description);
		stmt.setString(3, task.venue);
		stmt.setDate(4, task.start);
		stmt.setDate(5, task.end);
		stmt.setBoolean(6, task.allDay);
		stmt.setInt(7, task.id);
		stmt.setInt(8, task.fkUserId);
		stmt.execute();
		conn.close();
	}
	public static void deleteTask(int id,int userId) throws SQLException {
		Connection conn = DriverManager.getConnection(Config.DB_HOSTNAME, Config.DB_PROPERTIES);
		PreparedStatement stmt = conn.prepareStatement("DELETE FROM task WHERE id=? AND fk_user_id=? ;");
		stmt.setInt(1, id);
		stmt.setInt(2, userId);
		stmt.execute();
		conn.close();
	}
	public static int[] getFirendSchedule(final int userId,final int year,final int month) throws SQLException{
		Connection conn = DriverManager.getConnection(Config.DB_HOSTNAME, Config.DB_PROPERTIES);
		PreparedStatement stmt = conn.prepareStatement("SELECT DAYOFMONTH(t.start) FROM task t INNER JOIN friend f ON f.fk_user_id_2 = T.fk_user_id  WHERE F.fk_user_id_1=? AND YEAR=(t.start) AND  MONTH(t.start)=? ;");
		stmt.setInt(1, userId);
		stmt.setInt(2, year);
		stmt.setInt(3, month);
		ResultSet rs = stmt.executeQuery();
		int[] firends = new int[0];
		int i=0;
		while(rs.next()) {
			System.arraycopy(firends, 0, (firends=new int[i+1]), 0, i);
			firends[i]=rs.getInt(1);
			++i;
		}
		stmt = conn.prepareStatement("SELECT DAYOFMONTH(t.start) FROM task t INNER JOIN friend f ON f.fk_user_id_1 = T.fk_user_id  WHERE F.fk_user_id_2=? AND YEAR=(t.start) AND  MONTH(t.start)=? ;");
		stmt.setInt(1, userId);
		stmt.setInt(2, year);
		stmt.setInt(3, month);
		rs = stmt.executeQuery();
		while(rs.next()) {
			System.arraycopy(firends, 0, (firends=new int[i+1]), 0, i);
			firends[i]=rs.getInt(1);
			++i;
		}
		return firends;
	}
	
}
