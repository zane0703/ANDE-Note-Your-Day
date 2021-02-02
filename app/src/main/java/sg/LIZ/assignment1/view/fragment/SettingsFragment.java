package sg.LIZ.assignment1.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import sg.LIZ.assignment1.Key;
import sg.LIZ.assignment1.R;


public class SettingsFragment extends Fragment {

    @SuppressLint("NonConstantResourceId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences appSettingPrefs;

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        appSettingPrefs = getActivity().getSharedPreferences(Key.DATABASE_NAME, 0);
        //sharedPrefsEdit = SharedPreferences.Editor = appSettingPrefs.edit();
        int theme_id = appSettingPrefs.getInt("theme", AppCompatDelegate.MODE_NIGHT_UNSPECIFIED);
        switch (theme_id){
            case AppCompatDelegate.MODE_NIGHT_UNSPECIFIED:
            default:
                theme_id = R.id.setting_theme_default;
                break;
            case AppCompatDelegate.MODE_NIGHT_NO:
                theme_id = R.id.setting_theme_light;
                break;
            case AppCompatDelegate.MODE_NIGHT_YES:
                theme_id = R.id.setting_theme_dark;
        }
        ((RadioButton) view.findViewById(theme_id)).setChecked(true);
        ((RadioGroup) view.findViewById(R.id.setting_theme)).setOnCheckedChangeListener((group, checkedId) -> {
            int mode;
            switch (checkedId) {
                case R.id.setting_theme_default:
                    mode = AppCompatDelegate.MODE_NIGHT_UNSPECIFIED;
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
            AppCompatDelegate.setDefaultNightMode(mode);
            SharedPreferences.Editor editor = appSettingPrefs.edit();
            editor.putInt("theme", mode);
            editor.apply();
        });

      /* view.findViewById(R.id.switchButton).setOnClickListener(new View.OnClickListener()  {
           @Override
           public void onClick(View view) {
               if (isNightModeOn) { //if not night mode, set night mode, vice versa
                   AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                   //sharedPrefsEdit("Night Mode", false);
               } else {
                   AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                   //setDefaultNightMode() automatically recreates any started activities.
               }
           }

       });*/

        /*view.findViewById(R.id.followSystemMode).setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            }

        });*/


        return view;
    } // end of onCreate
} //end of SettingsFragment
