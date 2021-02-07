/**
 * @Author Ang Yun Zane
 * @Author Lucas Tan
 * @Author Lim I Kin
 * class DIT/FT/2A/21
 */
package sg.LIZ.assignment1.view.activity;

import sg.LIZ.assignment1.Key;
import sg.LIZ.assignment1.R;
import sg.LIZ.assignment1.view.adapter.SlidingMenuAdapter;
import sg.LIZ.assignment1.view.fragment.SettingsFragment;
import sg.LIZ.assignment1.view.fragment.ViewByMonthFragment;
import sg.LIZ.assignment1.view.fragment.ViewByYearFragment;
import sg.LIZ.assignment1.view.layout.onSetMonth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity {
    private ListView listViewSliding;
    private int selectYear;
    private int selectMonth;
    private ImageButton hamburgerMenu;
    private TextView textViewMonthView;
    private TextView textViewYearView;
    private DrawerLayout drawerLayout;
    private ConstraintLayout constraintLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private CharSequence[] months;
    private PendingIntent pendingIntent;
    private AlarmManager mAlarmManager;
    private int currentFragment;
    private Fragment fragment;
    private boolean isDrawerOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*pre define view object */
        listViewSliding = findViewById(R.id.sliding_menu);
        drawerLayout = findViewById(R.id.drawer_layout);
        constraintLayout = findViewById(R.id.sliding_menu2);
        textViewMonthView = findViewById(R.id.month);
        textViewYearView = findViewById(R.id.year);
        hamburgerMenu = findViewById(R.id.hamburger_menu);
        Resources resources = getResources();
        months = resources.getTextArray(R.array.month);
        /*set the list of item in the sliding bar*/
        CharSequence[] listSliding = new CharSequence[]{
                resources.getText(R.string.month),
                resources.getText(R.string.year),
                resources.getString(R.string.settings)
        };
        /*check if activity is recreated*/
        if (savedInstanceState == null) {
            /*if not set default*/
            selectYear = Key.currentYear;
            selectMonth = Key.currentMonth;
            currentFragment = 0;
            textViewYearView.setText(Integer.toString(selectYear));
        } else {
            /*else get the previous data*/
            currentFragment = savedInstanceState.getInt(Key.KEY_ID);
            selectMonth = savedInstanceState.getInt(Key.KEY_MONTH);
            selectYear = savedInstanceState.getInt(Key.KEY_YEAR);
            if (currentFragment != 2) {
                textViewYearView.setText(Integer.toString(selectYear));
            }
        }
        /*create adapter for the sliding bar */
        SlidingMenuAdapter adapter = new SlidingMenuAdapter(this, listSliding);
        listViewSliding.setAdapter(adapter);
        listViewSliding.setItemChecked(0, true);
        drawerLayout.closeDrawer(constraintLayout);
        /* on item click in the sliding bar move to another fragment page*/
        listViewSliding.setOnItemClickListener((parent, view, position, id) -> {
            listViewSliding.setItemChecked(position, true);
            replaceFragment(position);
            drawerLayout.closeDrawer(constraintLayout);
        });
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
                invalidateOptionsMenu();
                isDrawerOpened = true;
                hamburgerMenu.setSelected(true);
            }

            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                isDrawerOpened = false;
                hamburgerMenu.setSelected(false);
            }

        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        /*set up to update the Date*/
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        pendingIntent = PendingIntent.getBroadcast(this, 123, new Intent(this, ChangeDate.class), PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        replaceFragment(currentFragment);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        actionBarDrawerToggle.syncState();
    }

    /*run this to change month*/
    @SuppressLint("NonConstantResourceId")
    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.view_by_year_btn_Jan:
                selectMonth = 0;
                break;
            case R.id.view_by_year_btn_Feb:
                selectMonth = 1;
                break;
            case R.id.view_by_year_btn_Mar:
                selectMonth = 2;
                break;
            case R.id.view_by_year_btn_Apr:
                selectMonth = 3;
                break;
            case R.id.view_by_year_btn_May:
                selectMonth = 4;
                break;
            case R.id.view_by_year_btn_Jun:
                selectMonth = 5;
                break;
            case R.id.view_by_year_btn_Jul:
                selectMonth = 6;
            case R.id.view_by_year_btn_Aug:
                selectMonth = 7;
                break;
            case R.id.view_by_year_btn_Sep:
                selectMonth = 8;
                break;
            case R.id.view_by_year_btn_Oct:
                selectMonth = 9;
                break;
            case R.id.view_by_year_btn_Nov:
                selectMonth = 10;
                break;
            case R.id.view_by_year_btn_Dec:
                selectMonth = 11;
                break;
            default:
                return;
        }
        currentFragment = 0;
        replaceFragment(0);
    }

    /*show the dialog for the user to jump to a set year or month */
    public void onSetYear(View view) {
        /*check if is a view by year/month to prevent ClassCastException*/
        if (fragment instanceof onSetMonth) {
            ((onSetMonth) fragment).onSetYear(months);
        }

    }

    /*for the fragment to set the month*/
    public MainActivity setMonth(int month) {
        selectMonth = month;
        textViewMonthView.setText(months[month]);
        return this;

    }
    /*for the fragment to set the year*/

    public MainActivity setYear(int year) {
        selectYear = year;
        textViewYearView.setText(Integer.toString(year));
        return this;
    }

    /* for the fragment to get the year from main activity*/
    public int getYear() {
        return selectYear;
    }

    /* for the fragment to get the month from main activity*/
    public int getMonth() {
        return selectMonth;
    }

    /*navigate to anther fragment */
    private void replaceFragment(int pos) {
        switch (pos) {
            case 0:
                fragment = new ViewByMonthFragment();
                if (currentFragment == 2) {
                    textViewYearView.setText(Integer.toString(selectYear));
                }
                textViewMonthView.setText(months[selectMonth]);
                break;
            case 1:
                fragment = new ViewByYearFragment();
                textViewMonthView.setText(new char[0], 0, 0);
                if (currentFragment == 2) {
                    textViewYearView.setText(Integer.toString(selectYear));
                }
                break;
            case 2:
                fragment = new SettingsFragment();
                textViewMonthView.setText(new char[0], 0, 0);
                textViewYearView.setText(R.string.settings);
                break;
            default:
                return;
        }
        currentFragment = pos;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_content, fragment);
        transaction.commit();
    }
    /*when the hamburger menu click open or closer  sliding bar */

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Key.KEY_MONTH, selectMonth);
        outState.putInt(Key.KEY_YEAR, selectYear);
        outState.putInt(Key.KEY_ID, currentFragment);
    }

    public void onOpenDrawer(View v) {
        if (isDrawerOpened) {
            hamburgerMenu.setSelected(false);
            drawerLayout.closeDrawer(constraintLayout);
        } else {
            hamburgerMenu.setSelected(true);
            drawerLayout.openDrawer(constraintLayout);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAlarmManager.cancel(pendingIntent);
    }

    private class ChangeDate extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            int newCurrentYear = gregorianCalendar.get(GregorianCalendar.YEAR);
            int newCurrentMonth = gregorianCalendar.get(GregorianCalendar.MONTH);
            int newCurrentDay = gregorianCalendar.get(GregorianCalendar.DAY_OF_MONTH);
            if (MainActivity.this.fragment instanceof ViewByMonthFragment) {
                ViewByMonthFragment viewByMonthFragment = (ViewByMonthFragment) fragment;
                if (Key.currentMonth != newCurrentMonth) {
                    if (Key.currentMonth == selectMonth && Key.currentYear == selectYear) {
                        viewByMonthFragment.toChangeCurrentDay(Key.currentDay, -1);
                    } else if (newCurrentMonth == selectMonth && newCurrentYear == selectYear) {
                        viewByMonthFragment.toChangeCurrentDay(-1, newCurrentDay);
                    }
                } else {
                    if (Key.currentMonth == selectMonth && Key.currentYear == selectYear) {
                        viewByMonthFragment.toChangeCurrentDay(Key.currentDay, newCurrentDay);
                    }
                }
            }
            Key.currentDay = (byte) newCurrentDay;
            Key.currentYear = newCurrentYear;
            Key.currentMonth = (byte) newCurrentMonth;
        }
    }
}