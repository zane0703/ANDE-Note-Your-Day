package sg.LIZ.assignment1.model.net;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

class UserAPI {
    private final static URL BASE_URL;
    private final static URL LOGIN_URL;
    static {
        String baseURLString= "http://localhost:8080/api/user";
        try {
            BASE_URL = new URL(baseURLString);
            LOGIN_URL = new URL(baseURLString+"/login");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new Error(e);
        }
    }
    private static String loginUser(CharSequence userName,CharSequence password) throws IOException {
        HttpURLConnection conn = (HttpURLConnection)LOGIN_URL.openConnection();
        Writer out= new OutputStreamWriter( conn.getOutputStream());
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        out.write(new char[]{'u', 's', 'e', 'r', 'n', 'a', 'm', 'e','='},0,9);
        out.append(URLEncoder.encode(userName.toString(), StandardCharsets.UTF_8.toString()));
        out.write(new char[]{'&','p', 'a', 's', 's', 'w', 'o', 'r', 'd','='},0,10);
        out.append(URLEncoder.encode(password.toString(), StandardCharsets.UTF_8.toString()));
        out.close();
    }


}
