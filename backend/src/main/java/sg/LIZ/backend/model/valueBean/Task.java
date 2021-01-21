package sg.LIZ.backend.model.valueBean;

import java.sql.Date;

import jakarta.json.bind.annotation.JsonbTypeAdapter;
import sg.LIZ.backend.DateAdapter;

public class Task {
	public final int id;
	public final int fkUserId;
	public final String title;
	public final String description ;
	public final String venue;
	public final boolean allDay;
	@JsonbTypeAdapter(DateAdapter.class)
	public final Date start;
	@JsonbTypeAdapter(DateAdapter.class)
	public final Date end;
	@JsonbTypeAdapter(DateAdapter.class)
	public final Date createdAt;
	public Task (int id,int fkUserId,String title,String description,String venue,Date start,Date end,boolean allDay,Date createdAt) {
		this.id = id;
		this.fkUserId =fkUserId;
		this.title =title;
		this.description=description;
		this.venue =venue;
		this.start = start;
		this.end=end;
		this.allDay= allDay;
		this.createdAt=createdAt;
	}
	public Task (int fkUserId,String title,String description,String venue,Date start,Date end,boolean allDay) {
		this(1,fkUserId, title, description, venue, start, end,allDay, null);
	}
	
	
}
