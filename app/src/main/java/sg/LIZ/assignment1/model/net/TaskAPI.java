package sg.LIZ.assignment1.model.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.GregorianCalendar;

import sg.LIZ.assignment1.R;
import sg.LIZ.assignment1.model.valueBean.Task;
import sg.LIZ.assignment1.Key;
class TaskAPI {

    private static String taskUrl = null;
    private final SharedPreferences sharedPreferences;
    private final Context context;
    TaskAPI(@NonNull Context context) {
        this.context =context;
        if(taskUrl==null){
            taskUrl = new StringBuilder(context.getResources().getString(R.string.backend_urL)).append(new char[]{'/','a','p','i','/','t','a','s','k'}).toString();
        }
        sharedPreferences = context.getSharedPreferences("assignment",0);
    }
    public Task getTask(final int id) throws IOException, JSONException, SessionTimeoutException {
        HttpURLConnection conn = (HttpURLConnection) new URL(new StringBuilder(taskUrl).append('/').append(id).toString()).openConnection();
        conn.setRequestMethod(Key.HTTP_METHOD_GET);
        conn.setRequestProperty(Key.HTTP_AUTHORIZATION, "Bearer "+sharedPreferences.getString(Key.KEY_TOKEN, ""));
        Reader in = new InputStreamReader(conn.getInputStream());
        switch (conn.getResponseCode()){
            case 200:
                StringBuilder stringBuilder = new StringBuilder();
                int len=0;
                char[] chunk = new char[1024];
                while ((len=in.read(chunk, 0, 1024))!=-1){
                    stringBuilder.append(chunk,0,len);
                }
                in.close();
                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                GregorianCalendar start = new GregorianCalendar();
                start.setTimeInMillis(jsonObject.getLong(Key.KEY_START));
                GregorianCalendar end = new GregorianCalendar();
                end.setTimeInMillis(jsonObject.getLong(Key.KEY_END));
                return new Task(jsonObject.getInt(Key.KEY_ID),(byte)start.get(GregorianCalendar.DAY_OF_MONTH) ,(byte) start.get(GregorianCalendar.MONTH), start.get(GregorianCalendar.YEAR), (byte)start.get(GregorianCalendar.HOUR), (byte)start.get(GregorianCalendar.MINUTE),(byte) end.get(GregorianCalendar.HOUR), (byte) end.get(GregorianCalendar.MINUTE), jsonObject.getBoolean(Key.KEY_ALL_DAY),jsonObject.getString(Key.KEY_TITLE),jsonObject.getString(Key.KEY_DESCRIPTION),jsonObject.getString(Key.KEY_VENUE));
            case 401:
                in.close();
                throw new SessionTimeoutException();
            default:
                in.close();
                Toast.makeText(this.context, R.string.err_500, Toast.LENGTH_SHORT);
                return null;
        }

    }
    public Task[] getTask() throws IOException, JSONException, SessionTimeoutException {
        HttpURLConnection conn = (HttpURLConnection) new URL(taskUrl).openConnection();
        conn.setRequestMethod(Key.HTTP_METHOD_GET);
        conn.setRequestProperty(Key.HTTP_AUTHORIZATION, "Bearer "+sharedPreferences.getString(Key.KEY_TOKEN, ""));
        Reader in = new InputStreamReader(conn.getInputStream());
        switch (conn.getResponseCode()) {
            case 200:
                StringBuilder stringBuilder = new StringBuilder();
                int len = 0;
                char[] chunk = new char[1024];
                while ((len = in.read(chunk, 0, 1024)) != -1) {
                    stringBuilder.append(chunk, 0, len);
                }
                in.close();
                JSONArray jsonArray = new JSONArray(stringBuilder.toString());
                len = jsonArray.length();
                Task[] tasks = new Task[len];
                GregorianCalendar start = new GregorianCalendar();
                GregorianCalendar end = new GregorianCalendar();
                for (int i = 0; i < len; ++i) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    start.setTimeInMillis(jsonObject.getLong(Key.KEY_START));
                    end.setTimeInMillis(jsonObject.getLong(Key.KEY_END));
                    tasks[i] = new Task(jsonObject.getInt(Key.KEY_ID), (byte) start.get(GregorianCalendar.DAY_OF_MONTH), (byte) start.get(GregorianCalendar.MONTH), start.get(GregorianCalendar.YEAR), (byte) start.get(GregorianCalendar.HOUR), (byte) start.get(GregorianCalendar.MINUTE), (byte) end.get(GregorianCalendar.HOUR), (byte) end.get(GregorianCalendar.MINUTE), jsonObject.getBoolean(Key.KEY_ALL_DAY), jsonObject.getString(Key.KEY_TITLE), jsonObject.getString(Key.KEY_DESCRIPTION), jsonObject.getString(Key.KEY_VENUE));
                }
                return tasks;
            case 401:
                throw new SessionTimeoutException();
            default:
                in.close();
                return null;
        }
    }
    public boolean addTask(@NonNull Task task) throws IOException, SessionTimeoutException {
        HttpURLConnection conn = (HttpURLConnection) new URL(taskUrl).openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(false);
        conn.setRequestMethod(Key.HTTP_METHOD_POST);
        conn.setRequestProperty(Key.HTTP_AUTHORIZATION, "Bearer "+sharedPreferences.getString(Key.KEY_TOKEN, ""));
        conn.setRequestProperty(Key.HTTP_CONTENT_TYPE, Key.FORM_URL_ENCODED);
        Writer out = new OutputStreamWriter(conn.getOutputStream());
        out.write(new char[]{'t', 'i', 't', 'l', 'e','='},0,6);
        String enc = StandardCharsets.UTF_8.toString();
        out.write(URLEncoder.encode(task.TITLE, enc));
        out.write(new char[]{'&', 'd', 'e', 's', 'c', 'r', 'i', 'p', 't', 'i', 'o', 'n', '='},0,13);
        out.write(URLEncoder.encode(task.DESCRIPTION, enc));
        out.write(new char[]{ '&', 'v', 'e', 'n', 'u', 'e', '='},0,7);
        out.write(URLEncoder.encode(task.VENUE, enc));
        GregorianCalendar date = new GregorianCalendar(task.YEAR, task.MONTH, task.DAY, task.START_HOURS, task.START_MINUTES);
        out.write(new char[]{'&', 's', 't', 'a', 'r', 't', '='},0,7);
        out.write(Long.toString(date.getTimeInMillis()));
        date.set(GregorianCalendar.HOUR, task.END_HOURS);
        date.set(GregorianCalendar.MINUTE, task.END_MINUTES);
        out.write(new char[]{'&', 'e', 'n', 'd', '='},0,5);
        out.write(Long.toString(date.getTimeInMillis()));
        out.write(new char[]{'&', 'a', 'l', 'l', 'D', 'a', 'y', '='},0,8);
        out.write(task.ALL_DAY?new char[]{'t','r','u','e'}:new char[]{'f','a','l','s','e'});
        out.close();
        switch (conn.getResponseCode()){
            case 201:
            case 204:
                return true;
            case 401:
                throw  new SessionTimeoutException();
            default:
                return false;
        }
    }
    public boolean updateTask(int id,@NonNull Task task) throws IOException, SessionTimeoutException {
        HttpURLConnection conn = (HttpURLConnection) new URL(new StringBuilder(taskUrl).append('/').append(id).toString()).openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(false);
        conn.setRequestMethod(Key.HTTP_METHOD_PUT);
        conn.setRequestProperty(Key.HTTP_AUTHORIZATION, "Bearer "+sharedPreferences.getString(Key.KEY_TOKEN, ""));
        conn.setRequestProperty(Key.HTTP_CONTENT_TYPE, Key.FORM_URL_ENCODED);
        Writer out = new OutputStreamWriter(conn.getOutputStream());
        out.write(new char[]{'t', 'i', 't', 'l', 'e','='},0,6);
        String enc = StandardCharsets.UTF_8.toString();
        out.write(URLEncoder.encode(task.TITLE, enc));
        out.write(new char[]{'&', 'd', 'e', 's', 'c', 'r', 'i', 'p', 't', 'i', 'o', 'n', '='},0,13);
        out.write(URLEncoder.encode(task.DESCRIPTION, enc));
        out.write(new char[]{ '&', 'v', 'e', 'n', 'u', 'e', '='},0,7);
        out.write(URLEncoder.encode(task.VENUE, enc));
        GregorianCalendar date = new GregorianCalendar(task.YEAR, task.MONTH, task.DAY, task.START_HOURS, task.START_MINUTES);
        out.write(new char[]{'&', 's', 't', 'a', 'r', 't', '='},0,7);
        out.write(Long.toString(date.getTimeInMillis()));
        date.set(GregorianCalendar.HOUR, task.END_HOURS);
        date.set(GregorianCalendar.MINUTE, task.END_MINUTES);
        out.write(new char[]{'&', 'e', 'n', 'd', '='},0,5);
        out.write(Long.toString(date.getTimeInMillis()));
        out.write(new char[]{'&', 'a', 'l', 'l', 'D', 'a', 'y', '='},0,8);
        out.write(task.ALL_DAY?new char[]{'t','r','u','e'}:new char[]{'f','a','l','s','e'});
        out.close();
        switch (conn.getResponseCode()){
            case 204:
                return true;
            case 401:
                throw  new SessionTimeoutException();
            default:
                return false;
        }
    }
    public boolean deleteTask(int id) throws IOException, SessionTimeoutException {
        HttpURLConnection conn = (HttpURLConnection) new URL(new StringBuilder(taskUrl).append('/').append(id).toString()).openConnection();
        conn.setDoInput(false);
        conn.setRequestMethod(Key.HTTP_METHOD_DELETE);
        conn.setRequestProperty(Key.HTTP_AUTHORIZATION, "Bearer "+sharedPreferences.getString(Key.KEY_TOKEN, ""));
        switch (conn.getResponseCode()){
            case 204:
                return true;
            case 401:
                throw  new SessionTimeoutException();
            default:
                return false;
        }
    }
    public int[] getFriendSchedule(int year,int month) throws IOException, JSONException, SessionTimeoutException {
        HttpURLConnection conn = (HttpURLConnection) new URL(new StringBuilder(taskUrl)
                .append(new char[]{'/', 'f', 'r', 'i', 'e', 'n', 'd', '?', 'y', 'e', 'a', 'r', '='},0,13)
                .append(year)
                .append(new char[]{ '&', 'm', 'o', 'n', 't', 'h', '='},0,7)
                .append(month)
                .toString()).openConnection();
        conn.setRequestMethod(Key.HTTP_METHOD_GET);
        conn.setRequestProperty(Key.HTTP_AUTHORIZATION, "Bearer "+sharedPreferences.getString(Key.KEY_TOKEN, ""));
        Reader in = new InputStreamReader(conn.getInputStream());
      switch (conn.getResponseCode()){
          case 200 :
              StringBuilder stringBuilder = new StringBuilder();
              int len=0;
              char[] chunk = new char[1024];
              while ((len=in.read(chunk, 0, 1024))!=-1){
                  stringBuilder.append(chunk,0,len);
              }
              in.close();
              JSONArray jsonArray = new JSONArray(stringBuilder.toString());
              len = jsonArray.length();
              int[] firend = new int[len];
              for(int i = 0 ;i<len;++i){
                  firend[i] =jsonArray.getInt(i);
              }
              return  firend;
          case 401:
              in.close();
              throw new SessionTimeoutException();
          default:
              in.close();
              return null;

      }
    }
}
