package sg.LIZ.assignment1.model.net;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.GregorianCalendar;

import sg.LIZ.assignment1.R;
import sg.LIZ.assignment1.model.utilityBean.TaskDb;
import sg.LIZ.assignment1.model.valueBean.Task;

class TaskAPI {
    public static String HTTP_METHOD_POST="POST";
    public static String HTTP_METHOD_GET="GET";
    public static String HTTP_METHOD_PUT="PUT";
    public static String HTTP_METHOD_DELETE="DELETE";
    public static String HTTP_CONTENT_TYPE="Content-Type";
    public static String FORM_URL_ENCODED="application/x-www-form-urlencoded";
    private static final String KEY_FK_USER_ID="fkUserId";
    private static final String KEY_START="start";
    private static final String KEY_END="END";
    private static String taskUrl = null;
    private final SharedPreferences sharedPreferences;
    TaskAPI(@NonNull Context context) {
        if(taskUrl==null){
            taskUrl = new StringBuilder(context.getResources().getString(R.string.backend_urL)).append(new char[]{'/','a','p','i','/','t','a','s','k'}).toString();
        }
        sharedPreferences = context.getSharedPreferences("assignment",0);
    }
    public Task getTask(final int id) throws IOException, JSONException {
        HttpURLConnection conn = (HttpURLConnection) new URL(new StringBuilder(taskUrl).append('/').append(id).toString()).openConnection();
        conn.setRequestMethod(HTTP_METHOD_GET);
        conn.setRequestProperty(UserAPI.HTTP_AUTHORIZATION, "Bearer "+sharedPreferences.getString(UserAPI.KEY_TOKEN, ""));
        Reader in = new InputStreamReader(conn.getInputStream());
        StringBuilder stringBuilder = new StringBuilder();
        int len=0;
        char[] chunk = new char[1024];
        while ((len=in.read(chunk, 0, 1024))!=-1){
            stringBuilder.append(chunk,0,len);
        }
        JSONObject jsonObject = new JSONObject(stringBuilder.toString());
        GregorianCalendar start = new GregorianCalendar();
        start.setTimeInMillis(jsonObject.getLong(KEY_START));
        GregorianCalendar end = new GregorianCalendar();
        end.setTimeInMillis(jsonObject.getLong(KEY_END));
        return new Task(jsonObject.getInt(TaskDb.KEY_ID),(byte)start.get(GregorianCalendar.DAY_OF_MONTH) ,(byte) start.get(GregorianCalendar.MONTH), start.get(GregorianCalendar.YEAR), (byte)start.get(GregorianCalendar.HOUR), (byte)start.get(GregorianCalendar.MINUTE),(byte) end.get(GregorianCalendar.HOUR), (byte) end.get(GregorianCalendar.MINUTE), jsonObject.getBoolean(TaskDb.KEY_ALL_DAY),jsonObject.getString(TaskDb.KEY_TITLE),jsonObject.getString(TaskDb.KEY_DESCRIPTION),jsonObject.getString(TaskDb.KEY_VENUE));

    }
    public Task[] getTask() throws IOException, JSONException {
        HttpURLConnection conn = (HttpURLConnection) new URL(taskUrl).openConnection();
        conn.setRequestMethod(HTTP_METHOD_GET);
        conn.setRequestProperty(UserAPI.HTTP_AUTHORIZATION, "Bearer "+sharedPreferences.getString(UserAPI.KEY_TOKEN, ""));
        Reader in = new InputStreamReader(conn.getInputStream());
        StringBuilder stringBuilder = new StringBuilder();
        int len=0;
        char[] chunk = new char[1024];
        while ((len=in.read(chunk, 0, 1024))!=-1){
            stringBuilder.append(chunk,0,len);
        }
        JSONArray jsonArray = new JSONArray(stringBuilder.toString());
        len =jsonArray.length();
        Task[] tasks = new Task[len];
        GregorianCalendar start = new GregorianCalendar();
        GregorianCalendar end = new GregorianCalendar();
        for(int i = 0;i<len;++i){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            start.setTimeInMillis(jsonObject.getLong(KEY_START));
            end.setTimeInMillis(jsonObject.getLong(KEY_END));
            tasks[i] = new Task(jsonObject.getInt(TaskDb.KEY_ID),(byte)start.get(GregorianCalendar.DAY_OF_MONTH) ,(byte) start.get(GregorianCalendar.MONTH), start.get(GregorianCalendar.YEAR), (byte)start.get(GregorianCalendar.HOUR), (byte)start.get(GregorianCalendar.MINUTE),(byte) end.get(GregorianCalendar.HOUR), (byte) end.get(GregorianCalendar.MINUTE), jsonObject.getBoolean(TaskDb.KEY_ALL_DAY),jsonObject.getString(TaskDb.KEY_TITLE),jsonObject.getString(TaskDb.KEY_DESCRIPTION),jsonObject.getString(TaskDb.KEY_VENUE));
        }
        return tasks;
    }
}
