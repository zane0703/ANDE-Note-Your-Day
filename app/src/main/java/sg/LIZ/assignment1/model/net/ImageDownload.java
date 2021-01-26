package sg.LIZ.assignment1.model.net;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import sg.LIZ.assignment1.Key;
import sg.LIZ.assignment1.R;

public final class ImageDownload {

    final private static Pattern HTTP_PATTERN = Pattern.compile("^https?://.*?");
    final private static Pattern IMAGE_TYPE_PATTERN = Pattern.compile("^image/.*?");
    @Nullable
    public static Bitmap getImage(@NonNull Activity context, @NonNull CharSequence url){
        String urlString;
        Log.i("hello", Boolean.toString(HTTP_PATTERN.matcher(url).matches()));
        if(!HTTP_PATTERN.matcher(url).matches()){
            urlString = "http://"+url.toString();
        }else{
            urlString=url.toString();
        }
        try{
            Log.i("helo", urlString);
            HttpURLConnection conn = (HttpURLConnection)new URL(urlString).openConnection();
            conn.setRequestMethod(Key.HTTP_METHOD_GET);
            conn.setRequestProperty("Accept", "image/jpeg, image/png, image/webp");
            if(conn.getResponseCode()==200){
                if(IMAGE_TYPE_PATTERN.matcher(conn.getHeaderField("Content-Type")).matches()){
                    return BitmapFactory.decodeStream(conn.getInputStream());
                }else{
                    context.runOnUiThread(()-> Toast.makeText(context, R.string.not_image, Toast.LENGTH_SHORT).show());
                    return null;
                }
            }else{
                final String resMessage=conn.getResponseMessage();
                context.runOnUiThread(()->Toast.makeText(context,resMessage , Toast.LENGTH_SHORT).show());

                return null;
            }
        }catch (MalformedURLException e){
            Log.e("image error",e.getMessage(),e);
            context.runOnUiThread(()->Toast.makeText(context, R.string.wrong_url, Toast.LENGTH_SHORT).show());
            return null;
        }catch (IOException e){
            Log.e("image error",e.getMessage(),e);
            context.runOnUiThread(()->Toast.makeText(context, R.string.not_conn, Toast.LENGTH_SHORT).show());
            return null;
        }

    }


}
