package sg.LIZ.assignment1.model.net;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import sg.LIZ.assignment1.R;
import sg.LIZ.assignment1.model.valueBean.Task;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

class UserAPI {
    private static String userUrl = null;
    public static String KEY_TOKEN ="token";
    public static String HTTP_AUTHORIZATION="Authorization";
    private final SharedPreferences sharedPreferences;
    UserAPI(Context context) {
        if(userUrl==null){
            userUrl =new StringBuilder(context.getResources().getString(R.string.backend_urL)).append(new char[]{'/','a','p','i'}).toString();
        }
        sharedPreferences = context.getSharedPreferences("assignment",0);

    }
    public boolean loginUser(CharSequence userName,CharSequence password) throws IOException {
        HttpURLConnection conn = (HttpURLConnection)new URL(new StringBuilder(userUrl).append(new char[]{'/','l','o','g','i','n','/','u','s','e','r'},0,11).toString()).openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod(TaskAPI.HTTP_METHOD_POST);
        conn.setRequestProperty(TaskAPI.HTTP_CONTENT_TYPE, TaskAPI.FORM_URL_ENCODED);
        Writer out= new OutputStreamWriter(conn.getOutputStream());
        out.write(new char[]{'u', 's', 'e', 'r', 'n', 'a', 'm', 'e','='},0,9);
        out.append(URLEncoder.encode(userName.toString(), StandardCharsets.UTF_8.toString()));
        out.write(new char[]{'&','p', 'a', 's', 's', 'w', 'o', 'r', 'd','='},0,10);
        out.append(URLEncoder.encode(password.toString(), StandardCharsets.UTF_8.toString()));
        out.close();
        Reader in = new InputStreamReader(conn.getInputStream());
        if(conn.getResponseCode()==200){
            StringBuilder stringBuilder = new StringBuilder();
            char[] chunk = new char[1024];
            int len;
            while((len= in.read(chunk,0,1024))!=-1){
                stringBuilder.append(chunk,0,len);
            }
            in.close();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_TOKEN, stringBuilder.toString());
            editor.apply();
            return true;
        }else{
            in.close();
            return false;
        }
    }
    public boolean registerUser(CharSequence userName,CharSequence password) throws IOException {
     HttpURLConnection conn = (HttpURLConnection) new URL(userUrl).openConnection();
     conn.setDoOutput(true);
     conn.setDoInput(false);
     conn.setRequestMethod(TaskAPI.HTTP_METHOD_POST);
        Writer out= new OutputStreamWriter(conn.getOutputStream());
        out.write(new char[]{'u', 's', 'e', 'r', 'n', 'a', 'm', 'e','='},0,9);
        out.append(URLEncoder.encode(userName.toString(), StandardCharsets.UTF_8.toString()));
        out.write(new char[]{'&','p', 'a', 's', 's', 'w', 'o', 'r', 'd','='},0,10);
        out.append(URLEncoder.encode(password.toString(), StandardCharsets.UTF_8.toString()));
        out.close();
    return conn.getResponseCode()==201;
    }
    public boolean isUserExist(@NonNull final String name) throws IOException{
        HttpURLConnection conn = (HttpURLConnection)new URL(new StringBuilder(userUrl).append(new char[]{'/', 'n', 'a', 'm', 'e', '/'},0,6).append(name).toString()).openConnection();
        conn.setRequestMethod(TaskAPI.HTTP_METHOD_GET);
        Reader in = new InputStreamReader(conn.getInputStream());
        if(conn.getResponseCode()==200){
            StringBuilder stringBuilder = new StringBuilder();
            char[] chunk = new char[1024];
            int len;
            while((len= in.read(chunk,0,1024))!=-1){
                stringBuilder.append(chunk,0,len);
            }
            in.close();
            return Boolean.parseBoolean(stringBuilder.toString());
        }else{
            return false;
        }
    }

}
