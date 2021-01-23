package sg.LIZ.assignment1.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;

import sg.LIZ.assignment1.Key;
import sg.LIZ.assignment1.R;
import sg.LIZ.assignment1.view.layout.YearPickerDialog;
import sg.LIZ.assignment1.view.layout.onSetMonth;

public class ViewByYearActivity extends AppCompatActivity implements onSetMonth{
    private final GregorianCalendar gregorianCalendar = new GregorianCalendar();
    private int selectedYear = gregorianCalendar.get(Calendar.YEAR);
    private YearPickerDialog mDatePickerDialog = null;
    private TextView textViewYearView;
    private TextView[] dayOfMonth;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_view_by_year);
        textViewYearView = findViewById(R.id.year);
        dayOfMonth=new TextView[] {
                findViewById(R.id.view_by_year_day_Jan),
                findViewById(R.id.view_by_year_day_Fed),
                findViewById(R.id.view_by_year_day_Mar),
                findViewById(R.id.view_by_year_day_Apr),
                findViewById(R.id.view_by_year_day_May),
                findViewById(R.id.view_by_year_day_Jun),
                findViewById(R.id.view_by_year_day_Jul),
                findViewById(R.id.view_by_year_day_Aug),
                findViewById(R.id.view_by_year_day_Sep),
                findViewById(R.id.view_by_year_day_Oct),
                findViewById(R.id.view_by_year_day_Nov),
                findViewById(R.id.view_by_year_day_Dec)
        };
        onSetMaxDay();
    }
    @SuppressLint("SetTextI18n")
    private void onSetMaxDay(){
        textViewYearView.setText(Integer.toString(selectedYear));
        for(int i = 0 ;i<12;++i){
            gregorianCalendar.set(Calendar.MONTH, i);
            dayOfMonth[i].setText(Integer.toString(gregorianCalendar.getMaximum(Calendar.DAY_OF_MONTH)));
        }
    }
    @SuppressLint("NonConstantResourceId")
    public void onClick(View view){
        Intent i = new Intent(getBaseContext(), MainActivity.class);
        switch (view.getId()){
            case R.id.view_by_year_btn_Jan:
                i.putExtra(Key.KEY_MONTH, 0);
                break;
            case R.id.view_by_year_btn_Feb:
                i.putExtra(Key.KEY_MONTH, 1);
                break;
            case R.id.view_by_year_btn_Mar :
                i.putExtra(Key.KEY_MONTH, 2);
                break;
            case R.id.view_by_year_btn_Apr:
                i.putExtra(Key.KEY_MONTH, 3);
                break;
            case  R.id.view_by_year_btn_May:
                i.putExtra(Key.KEY_MONTH, 4);
                break;
            case R.id.view_by_year_btn_Jun :
                i.putExtra(Key.KEY_MONTH, 5);
                break;
            case R.id.view_by_year_btn_Jul :
                i.putExtra(Key.KEY_MONTH, 6);
            case R.id.view_by_year_btn_Aug :
                i.putExtra(Key.KEY_MONTH, 7);
                break;
            case R.id.view_by_year_btn_Sep:
                i.putExtra(Key.KEY_MONTH, 8);
                break;
            case R.id.view_by_year_btn_Oct :
                i.putExtra(Key.KEY_MONTH, 9);
                break;
            case R.id.view_by_year_btn_Nov :
                i.putExtra(Key.KEY_MONTH, 10);
                break;
            case R.id.view_by_year_btn_Dec :
                i.putExtra(Key.KEY_MONTH, 11);
                break;
            default:
                return;
        }
        i.putExtra(Key.KEY_YEAR, selectedYear);
        startActivity(i);
        finish();

    }
    public void onSetYear(View view){
        if(mDatePickerDialog==null){
            mDatePickerDialog = new YearPickerDialog(getSupportFragmentManager(),((view2, year, month, dayOfMonth) -> {
                if(selectedYear!=year){
                    selectedYear = year;
                    textViewYearView.setText(Integer.toString(year));
                    gregorianCalendar.set(Calendar.YEAR, year);
                    onSetMaxDay();
                }
            }));
        }
        mDatePickerDialog.show(selectedYear);
    }
    @Override
    public void toNext(int day){
        ++selectedYear;
        gregorianCalendar.set(Calendar.YEAR, selectedYear);
        onSetMaxDay();
    }
    @Override
    public void toLast(int day){
        --selectedYear;
        gregorianCalendar.set(Calendar.YEAR, selectedYear);
        onSetMaxDay();
    }


}
