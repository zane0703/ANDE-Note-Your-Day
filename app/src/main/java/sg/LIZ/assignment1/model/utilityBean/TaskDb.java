package sg.LIZ.assignment1.model.utilityBean;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import sg.LIZ.assignment1.model.valueBean.Task;

public class TaskDb extends SQLiteOpenHelper {

    private final String TABLE = "task";
    private final String KEY_ID = "id";
    private final String KEY_DAY = "day";
    private final String KEY_MONTH= "month";
    private final String KEY_YEAR = "year";
    private final String KEY_START_HOURS ="startHours";
    private final String KEY_START_MINUTES="startMinutes";
    private final String KEY_END_HOURS = "endHours";
    private final String KEY_END_MINUTES = "endMinutes";
    private final String KEY_ALL_DAY ="allDay";
    private final String KEY_TITLE = "title";
    private final String KEY_DESORIPTION = "description";
    private final String KEY_VENUE="venue";

    public TaskDb(Context context) {
        super(context, "assignment1", null, 1);
        //3rd argument to be passed is CursorFactory instance
    }
    /*
    * private int id;
    private byte day;
    private byte month;
    private int year;
    private byte startHours;
    private byte startMinutes;
    private byte endHours;
    private byte endMinutes;
    private boolean allDay;
    private String title;
    private String description;
    private String venue;
    * */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE task ( id INTEGER PRIMARY KEY,day INTEGER NOT NULL,month INTEGER NOT NULL ,year INTEGER NOT NULL ,startHours INTEGER NOT NULL,startMinutes INTEGER NOT NULL ,endHours INTEGER NOT NULL ,endMinutes INTEGER NOT NULL,allDay INTEGER NOT NULL,title TEXT NOT NULL,description TEXT NOT NULL,venue TEXT NOT NULL )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS task");
        onCreate(db);
    }
    public void addTask(Task mTask){
        ContentValues values = new ContentValues();
        values.put(KEY_DAY,mTask.DAY);
        values.put(KEY_MONTH,mTask.MONTH);
        values.put(KEY_YEAR, mTask.YEAR);
        values.put(KEY_START_HOURS,mTask.START_HOURS);
        values.put(KEY_START_MINUTES, mTask.START_MINUTES);
        values.put(KEY_END_HOURS, mTask.END_HOURS);
        values.put(KEY_END_MINUTES,mTask.END_MINUTES);
        values.put(KEY_ALL_DAY,mTask.ALL_DAY);
        values.put(KEY_TITLE,mTask.TITLE);
        values.put(KEY_DESORIPTION,mTask.DESCRIPTION);
        values.put(KEY_VENUE,mTask.VENUE);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE, null,values);
    }
    public int[] getTaskByMonthYear(int month ,int year){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor=  db.query(true, TABLE,new String[]{KEY_DAY}, "month=? AND year=?",new String[]{Integer.toString(month),Integer.toString(year)},null,null,KEY_DAY,null);
        Log.d("sql select",cursor.toString());
        int[] days = new int[0];
        int index=0;
        while (cursor.moveToNext()){
            System.arraycopy(days,0,(days=new int[index+1]), 0,index);
            days[index] = cursor.getInt(0);
            ++index;
        }
        db.close();
        return days;
    }
    public Task[] getTaskByDate(int day,int month ,int year){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query( TABLE,new String[]{KEY_ID,KEY_TITLE,KEY_START_HOURS,KEY_START_MINUTES,KEY_END_HOURS,KEY_END_MINUTES,KEY_ALL_DAY}, "month=? AND year=? AND day=?",new String[]{Integer.toString(month),Integer.toString(year),Integer.toString(day)},null,null,null,null);
        Task[] tasks = new Task[0];
        int index=0;
        while(cursor.moveToNext()){
            System.arraycopy(tasks,0,(tasks = new Task[index+1]), 0,index);
            tasks[index]= new Task(cursor.getInt(0), cursor.getString(1),(byte) cursor.getInt(2),(byte) cursor.getInt(3),(byte) cursor.getInt(4),(byte) cursor.getInt(5),cursor.getInt(6)==1);
            ++index;
        }
        db.close();
        return tasks;
    }
    public Task getTaskById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Task task =null;
        Cursor cursor = db.query(TABLE, new String[]{KEY_DAY, KEY_MONTH, KEY_YEAR, KEY_START_HOURS, KEY_START_MINUTES, KEY_END_HOURS, KEY_END_MINUTES, KEY_ALL_DAY, KEY_TITLE, KEY_DESORIPTION, KEY_VENUE},
                "id=?", new String[]{Integer.toString(id)}, null, null, null, null);
        if (cursor.moveToFirst()) {
            task = new Task(id, (byte) cursor.getShort(0), (byte) cursor.getShort(1), cursor.getInt(2), (byte) cursor.getShort(3), (byte) cursor.getShort(4), (byte) cursor.getShort(5), (byte) cursor.getShort(6), cursor.getInt(7) == 1, cursor.getString(8), cursor.getString(9), cursor.getString(10));
            Log.d("alld",cursor.getString(6));
        }
        db.close();
        return task;
    }
    public boolean deleteTask(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        int affectedRows = db.delete(TABLE, "id=?", new String[]{Integer.toString(id)});
        db.close();
        return affectedRows>0;

    }

}
