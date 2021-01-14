package sg.LIZ.backend.model.valueBean;

import java.sql.Date;

public class Task {
	public final int id;
	public final int fkUserId;
	public final String title;
	public final String description ;
	public final String venue;
	public final Date start;
	public final Date end;
	public final Date createdAt;
	public Task (int id,int fkUserId,String title,String description,String venue,Date start,Date end,Date createdAt) {
		this.id = id;
		this.fkUserId =fkUserId;
		this.title =title;
		this.description=description;
		this.venue =venue;
		this.start = start;
		this.end=end;
		this.createdAt=createdAt;
	}
	public Task (int fkUserId,String title,String description,String venue,Date start,Date end) {
		this(1,fkUserId, title, description, venue, start, end, null);
	}
	
	
}
