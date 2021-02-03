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
import android.util.Log;
import android.widget.RadioButton;

import java.util.Locale;

import sg.LIZ.assignment1.Key;
import sg.LIZ.assignment1.R;

public class SplashScreenActivity extends AppCompatActivity {
    private static boolean isDone = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences appSettingPrefs = getSharedPreferences(Key.DATABASE_NAME, 0);
        if(isDone){
            isDone=false;
            AppCompatDelegate.setDefaultNightMode(appSettingPrefs.getInt("theme", AppCompatDelegate.MODE_NIGHT_UNSPECIFIED));
            recreate();
            return;
        }
        setLanguage(appSettingPrefs.getInt("language", 0));
        setContentView(R.layout.activity_splash_screen);
        /*navigate to main activity after 1 sec*/
        new android.os.Handler().postDelayed(() -> {
                startActivity(new Intent(this, MainActivity.class));
                finish();
        }, 1000);
    }

    private void setLanguage(final int languageId) {
        Locale locale;
        switch (languageId) {
            case 1:
                locale = Locale.ENGLISH;
                break;
            case 2:
                locale = Locale.CHINESE;
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