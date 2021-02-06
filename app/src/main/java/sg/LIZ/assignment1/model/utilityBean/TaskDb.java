package sg.LIZ.assignment1.model.utilityBean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import sg.LIZ.assignment1.Key;
import sg.LIZ.assignment1.model.valueBean.Task;

public final class TaskDb extends SQLiteOpenHelper {

    private final static String TABLE = "task";

    public TaskDb(@NonNull Context context) {
        super(context, Key.DATABASE_NAME, null, 2);
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
        db.execSQL("CREATE TABLE task ( id INTEGER PRIMARY KEY,day INTEGER NOT NULL,month INTEGER NOT NULL ,year INTEGER NOT NULL ,startHours INTEGER ,startMinutes INTEGER  ,endHours INTEGER  ,endMinutes INTEGER ,allDay INTEGER NOT NULL,title TEXT NOT NULL,description TEXT NOT NULL,venue TEXT NOT NULL, image BLOB ) ;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS task ;");
        onCreate(db);
    }

    public void addTask(@NonNull Task mTask) {
        ContentValues values = new ContentValues();
        values.put(Key.KEY_DAY, mTask.DAY);
        values.put(Key.KEY_MONTH, mTask.MONTH);
        values.put(Key.KEY_YEAR, mTask.YEAR);
        if (mTask.image == null) {
            values.putNull(Key.KEY_IMAGE);
        } else {
            values.put(Key.KEY_IMAGE, mTask.image);
        }
        if (mTask.ALL_DAY) {
            values.put(Key.KEY_ALL_DAY, true);
        } else {
            values.put(Key.KEY_START_HOURS, mTask.START_HOURS);
            values.put(Key.KEY_START_MINUTES, mTask.START_MINUTES);
            values.put(Key.KEY_END_HOURS, mTask.END_HOURS);
            values.put(Key.KEY_END_MINUTES, mTask.END_MINUTES);
            values.put(Key.KEY_ALL_DAY, false);
        }
        values.put(Key.KEY_TITLE, mTask.TITLE);
        values.put(Key.KEY_DESCRIPTION, mTask.DESCRIPTION);
        values.put(Key.KEY_VENUE, mTask.VENUE);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE, null, values);
    }

    public int[] getTaskByMonthYear(@IntRange(from = 0, to = 11) int month, @IntRange(from = 0) int year) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(true, TABLE, new String[]{Key.KEY_DAY}, "month=? AND year=?", new String[]{Integer.toString(month), Integer.toString(year)}, null, null, Key.KEY_DAY, null);
        Log.d("sql select", cursor.toString());
        int[] days = new int[0];
        for (int index = 0; cursor.moveToNext(); ++index) {
            System.arraycopy(days, 0, (days = new int[index + 1]), 0, index);
            days[index] = cursor.getInt(0);
        }
        db.close();
        return days;
    }

    public Task[] getTaskByDate(@IntRange(from = 0, to = 31) int day, @IntRange(from = 0, to = 11) int month, @IntRange(from = 0) int year) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE, new String[]{Key.KEY_ID, Key.KEY_TITLE, Key.KEY_START_HOURS, Key.KEY_START_MINUTES, Key.KEY_END_HOURS, Key.KEY_END_MINUTES, Key.KEY_ALL_DAY}, "month=? AND year=? AND day=?", new String[]{Integer.toString(month), Integer.toString(year), Integer.toString(day)}, null, null, null, null);
        Task[] tasks = new Task[0];
        int index = 0;
        while (cursor.moveToNext()) {
            System.arraycopy(tasks, 0, (tasks = new Task[index + 1]), 0, index);
            tasks[index] = new Task(cursor.getInt(0), cursor.getString(1), (byte) cursor.getInt(2), (byte) cursor.getInt(3), (byte) cursor.getInt(4), (byte) cursor.getInt(5), cursor.getInt(6) == 1);
            ++index;
        }
        db.close();
        return tasks;
    }

    @Nullable
    public Task getTaskById(@IntRange(from = 1) int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Task task = null;
        Cursor cursor = db.query(false, TABLE, new String[]{Key.KEY_DAY, Key.KEY_MONTH, Key.KEY_YEAR, Key.KEY_START_HOURS, Key.KEY_START_MINUTES, Key.KEY_END_HOURS, Key.KEY_END_MINUTES, Key.KEY_ALL_DAY, Key.KEY_TITLE, Key.KEY_DESCRIPTION, Key.KEY_VENUE, Key.KEY_IMAGE},
                "id=?", new String[]{Integer.toString(id)}, null, null, null, null);
        if (cursor.moveToFirst()) {
            byte[] image = null;
            if (!cursor.isNull(11)) {
                image = cursor.getBlob(11);
            }

            task = new Task(id, (byte) cursor.getShort(0), (byte) cursor.getShort(1), cursor.getInt(2), (byte) cursor.getShort(3), (byte) cursor.getShort(4), (byte) cursor.getShort(5), (byte) cursor.getShort(6), cursor.getInt(7) == 1, cursor.getString(8), cursor.getString(9), cursor.getString(10), image);
        }
        db.close();
        return task;
    }

    public boolean deleteTask(@IntRange(from = 1) int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int affectedRows = db.delete(TABLE, "id=?", new String[]{Integer.toString(id)});
        db.close();
        return affectedRows > 0;

    }

}
