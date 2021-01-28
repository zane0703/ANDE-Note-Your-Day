package sg.LIZ.assignment1.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;
import androidx.constraintlayout.widget.ConstraintLayout;

import sg.LIZ.assignment1.Key;
import sg.LIZ.assignment1.R;
import sg.LIZ.assignment1.view.adapter.SlidingMenuAdapter;
import sg.LIZ.assignment1.view.fragment.ViewByMonthFragment;
import sg.LIZ.assignment1.view.fragment.ViewByYearFragment;
import sg.LIZ.assignment1.view.layout.onSetMonth;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private ListView listViewSliding;
    private int selectYear = Key.currentYear;
    private int selectMonth = Key.currentMonth;
    private ImageButton hamburgerMenu;
    private TextView textViewMonthView;
    private TextView textViewYearView;
    private DrawerLayout drawerLayout;
    private ConstraintLayout constraintLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private CharSequence[] months;
    private boolean isDrawerOpened =false;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try
        {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        }
        catch (NullPointerException ignored){}
       listViewSliding = findViewById(R.id.sliding_menu);
        drawerLayout=findViewById(R.id.drawer_layout);
        constraintLayout =findViewById(R.id.sliding_menu2);
        textViewMonthView = findViewById(R.id.month);
        textViewYearView = findViewById(R.id.year);
        hamburgerMenu = findViewById(R.id.hamburger_menu);
        Resources resources = getResources();
        months = resources.getTextArray(R.array.month);
        CharSequence[] listSliding = new CharSequence[]{
                resources.getText(R.string.month),
                resources.getText(R.string.year)
        };
        textViewYearView.setText(Integer.toString(selectYear));
        SlidingMenuAdapter adapter = new SlidingMenuAdapter(this, listSliding);
        listViewSliding.setAdapter(adapter);
        listViewSliding.setItemChecked(0,true);
        drawerLayout.closeDrawer(constraintLayout);
        listViewSliding.setOnItemClickListener((parent,view,position,id)->{
            listViewSliding.setItemChecked(position,true);
            replaceFragment(position);
            drawerLayout.closeDrawer(constraintLayout);
        });
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.drawer_open,R.string.drawer_close ){
                @Override
                public  void onDrawerOpened(View view){
                    super.onDrawerOpened(view);
                    invalidateOptionsMenu();
                    isDrawerOpened =true;
                    hamburgerMenu.setSelected(true);
                }
            @Override
                public  void onDrawerClosed(View view){
                    super.onDrawerClosed(view);
                    invalidateOptionsMenu();
                    isDrawerOpened =false;
                hamburgerMenu.setSelected(false);
                }

        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        replaceFragment(0);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        actionBarDrawerToggle.syncState();
    }
    @SuppressLint("NonConstantResourceId")
    public void onClick(@NonNull View view){
        switch (view.getId()){
            case R.id.view_by_year_btn_Jan:
                selectMonth =0;
                break;
            case R.id.view_by_year_btn_Feb:
                selectMonth=1;
                break;
            case R.id.view_by_year_btn_Mar :
                selectMonth =2;
                break;
            case R.id.view_by_year_btn_Apr:
                selectMonth= 3;
                break;
            case  R.id.view_by_year_btn_May:
                selectMonth = 4;
                break;
            case R.id.view_by_year_btn_Jun :
                selectMonth = 5;
                break;
            case R.id.view_by_year_btn_Jul :
                selectMonth = 6;
            case R.id.view_by_year_btn_Aug :
                selectMonth = 7;
                break;
            case R.id.view_by_year_btn_Sep:
                selectMonth = 8;
                break;
            case R.id.view_by_year_btn_Oct :
                selectMonth = 9;
                break;
            case R.id.view_by_year_btn_Nov :
                selectMonth = 10;
                break;
            case R.id.view_by_year_btn_Dec :
                selectMonth = 11;
                break;
            default:
                return;
        }

        replaceFragment(0);
    }
    public void onSetYear(View view){
        ((onSetMonth) getSupportFragmentManager().findFragmentById(R.id.main_content)).onSetYear(months);
    }
    public MainActivity setMonth(int month){
        selectMonth = month;
        textViewMonthView.setText(months[month]);
        return this;

    }
    public MainActivity setYear(int year){
        selectYear =year;
        textViewYearView.setText(Integer.toString(year));
        return this;
    }
    public int getYear(){
        return selectYear;
    }
    public int getMonth(){
        return selectMonth;
    }
    private void replaceFragment(int pos){
        Fragment fragment=null;
        switch (pos){
            case 0:
                fragment = new ViewByMonthFragment();
                textViewMonthView.setText(months[selectMonth]);
                break;
            case 1:
                fragment = new ViewByYearFragment();
                textViewMonthView.setText(new char[0],0,0);
                break;
            default:
                return;
        }
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_content, fragment);
        transaction.commit();
    }
    public  void onOpenDrawer(View v){
        if(isDrawerOpened){
            hamburgerMenu.setSelected(false);
            drawerLayout.closeDrawer(constraintLayout);
        }else{
            hamburgerMenu.setSelected(true);
            drawerLayout.openDrawer(constraintLayout);
        }
    }


}