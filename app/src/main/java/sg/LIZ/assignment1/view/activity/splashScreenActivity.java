package sg.LIZ.assignment1.view.activity;
/**
 * @Author Ang Yun Zane ,Lucas Tan , Lim I Kin
 * @class DIT/FT/2A/21
 */
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import sg.LIZ.assignment1.R;

public class splashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_splash_screen);
        new android.os.Handler().postDelayed(() ->{
            startActivity(new Intent(this, MainActivity.class));
            Log.i("hello", "hello");
            finish();
        }, 1000);
    }
}