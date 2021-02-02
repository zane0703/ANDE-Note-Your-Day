package sg.LIZ.assignment1.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;
import sg.LIZ.assignment1.R;


public class SettingsFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences appSettingPrefs;

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        appSettingPrefs = getActivity().getSharedPreferences("AppSettingPrefs", 0);
        //sharedPrefsEdit = SharedPreferences.Editor = appSettingPrefs.edit();
        boolean isNightModeOn = appSettingPrefs.getBoolean("Night Mode", false );

//        if(isNightModeOn) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        } else {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        }

       view.findViewById(R.id.switchButton).setOnClickListener(new View.OnClickListener()  {
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

       });

        view.findViewById(R.id.followSystemMode).setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            }

        });


        return view;
    } // end of onCreate
} //end of SettingsFragment
