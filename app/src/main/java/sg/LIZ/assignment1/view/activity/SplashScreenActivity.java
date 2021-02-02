/**
 * @Author Ang Yun Zane
 * @Author Lucas Tan
 * @Author Lim I Kin
 * class DIT/FT/2A/21
 */
package sg.LIZ.assignment1.view.activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.RadioButton;

import java.util.Locale;

import sg.LIZ.assignment1.Key;
import sg.LIZ.assignment1.R;

public class SplashScreenActivity extends AppCompatActivity {
    private boolean isDone =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        /*navigate to main activity after 1 sec*/
        new android.os.Handler().postDelayed(() ->{
            if(isDone){
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }else{
                isDone=true;
            }
        }, 1000);
        final SharedPreferences appSettingPrefs = getSharedPreferences(Key.DATABASE_NAME, 0);
        AppCompatDelegate.setDefaultNightMode(appSettingPrefs.getInt("theme", AppCompatDelegate.MODE_NIGHT_UNSPECIFIED));
        Locale locale=null;
       setLanguage(appSettingPrefs.getInt("language", 0));
        if(isDone){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }else{
            isDone=true;
        }
    }
    private void setLanguage(final int languageId){
        Locale locale;
        switch (languageId){
            case 1:
                locale = Locale.ENGLISH;
                break;
            case 2:
                locale= Locale.CHINESE;
                break;
            default:
                return;
        }
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        res.updateConfiguration(conf, dm);

    }
}