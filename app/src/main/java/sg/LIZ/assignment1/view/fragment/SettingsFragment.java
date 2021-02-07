package sg.LIZ.assignment1.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import java.util.Locale;

import sg.LIZ.assignment1.Key;
import sg.LIZ.assignment1.R;


public class SettingsFragment extends Fragment {
    @SuppressLint("NonConstantResourceId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        final SharedPreferences appSettingPrefs = getActivity().getSharedPreferences(Key.DATABASE_NAME, 0);
        //sharedPrefsEdit = SharedPreferences.Editor = appSettingPrefs.edit();
        int theme_id = appSettingPrefs.getInt("theme", AppCompatDelegate.MODE_NIGHT_UNSPECIFIED);
        switch (theme_id){
            case AppCompatDelegate.MODE_NIGHT_NO:
                theme_id = R.id.setting_theme_light;
                break;
            case AppCompatDelegate.MODE_NIGHT_YES:
                theme_id = R.id.setting_theme_dark;
                break;
            default:
                theme_id = R.id.setting_theme_default;
                break;
        }
        switch (appSettingPrefs.getInt("language", 0)){
            case 1 :
                ((RadioButton)view.findViewById(R.id.setting_language_english)).setChecked(true);
                break;
            case 2:
                ((RadioButton)view.findViewById(R.id.setting_language_chinese)).setChecked(true);
                break;
            case 3:
                ((RadioButton)view.findViewById(R.id.setting_language_traditional_chinese)).setChecked(true);
                break;
            default:
                ((RadioButton)view.findViewById(R.id.setting_language_default)).setChecked(true);
                break;
        }

        ((RadioButton) view.findViewById(theme_id)).setChecked(true);
        ((RadioGroup) view.findViewById(R.id.setting_theme)).setOnCheckedChangeListener((group, checkedId) -> {
            try {
                int mode;
                switch (checkedId) {
                    case R.id.setting_theme_default:
                        mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                        break;
                    case R.id.setting_theme_light:
                        mode = AppCompatDelegate.MODE_NIGHT_NO;
                        break;
                    case R.id.setting_theme_dark:
                        mode = AppCompatDelegate.MODE_NIGHT_YES;
                        break;
                    default:
                        return;
                }
                SharedPreferences.Editor editor = appSettingPrefs.edit();
                editor.putInt("theme", mode);
                editor.apply();
                AppCompatDelegate.setDefaultNightMode(mode);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        ((RadioGroup) view.findViewById(R.id.setting_language)).setOnCheckedChangeListener((group, checkedId) -> {
            Locale mLocale;
            int languageId;
            switch (checkedId) {
                case R.id.setting_language_default:
                    mLocale = Locale.getDefault();
                    languageId=0;
                    break;
                case R.id.setting_language_english:
                    mLocale=Locale.ENGLISH;
                    languageId=1;
                    break;
                case R.id.setting_language_chinese:
                    mLocale=Locale.SIMPLIFIED_CHINESE;
                    languageId=2;
                    break;
                case R.id.setting_language_traditional_chinese:
                    mLocale = Locale.TRADITIONAL_CHINESE;
                    languageId=3;
                    break;
                default:
                    return;
            }
            SharedPreferences.Editor editor = appSettingPrefs.edit();
            editor.putInt("language", languageId);
            editor.apply();
            Resources res = getResources();
            Configuration conf = res.getConfiguration();
            conf.locale = mLocale;
            res.updateConfiguration(conf, res.getDisplayMetrics());
            getActivity().recreate();
        });
        return view;
    } // end of onCreate
} //end of SettingsFragment
