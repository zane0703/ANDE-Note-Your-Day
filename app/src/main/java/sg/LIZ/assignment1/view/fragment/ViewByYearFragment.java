package sg.LIZ.assignment1.view.fragment;

import android.annotation.SuppressLint;
import androidx.fragment.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.Year;
import java.util.Calendar;
import java.util.GregorianCalendar;

import sg.LIZ.assignment1.Key;
import sg.LIZ.assignment1.R;
import sg.LIZ.assignment1.view.activity.MainActivity;
import sg.LIZ.assignment1.view.layout.YearPickerDialog;
import sg.LIZ.assignment1.view.layout.onSetMonth;

public class ViewByYearFragment extends Fragment implements onSetMonth {
    private final GregorianCalendar gregorianCalendar = new GregorianCalendar();
    private int selectedYear = 0;
    private YearPickerDialog mDatePickerDialog = null;
    private TextView[] dayOfMonth;
    private MainActivity activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity=(MainActivity) getActivity();
        View view =inflater.inflate(R.layout.fragment_view_by_year, container, false);
        dayOfMonth=new TextView[] {
                view.findViewById(R.id.view_by_year_day_Jan),
                view.findViewById(R.id.view_by_year_day_Fed),
                view.findViewById(R.id.view_by_year_day_Mar),
                view.findViewById(R.id.view_by_year_day_Apr),
                view.findViewById(R.id.view_by_year_day_May),
                view.findViewById(R.id.view_by_year_day_Jun),
                view.findViewById(R.id.view_by_year_day_Jul),
                view.findViewById(R.id.view_by_year_day_Aug),
                view.findViewById(R.id.view_by_year_day_Sep),
                view.findViewById(R.id.view_by_year_day_Oct),
                view.findViewById(R.id.view_by_year_day_Nov),
                view.findViewById(R.id.view_by_year_day_Dec)
        };
        selectedYear=activity.getYear();
        onSetMaxDay();
        return view;
    }
    private void onSetMaxDay(){
        for(int i = 0 ;i<12;++i){
            gregorianCalendar.set(Calendar.MONTH, i);
            dayOfMonth[i].setText(Integer.toString(gregorianCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)));
        }
    }
    @Override
    public void onSetYear(final String[] months){
        if(mDatePickerDialog==null){
            mDatePickerDialog = new YearPickerDialog(getParentFragmentManager(),((view2, year, month, dayOfMonth) -> {
                if(selectedYear!=year){
                    selectedYear = year;
                    activity.setYear(year);
                    gregorianCalendar.set(Calendar.YEAR, year);
                    onSetMaxDay();
                }
            }));
        }
        mDatePickerDialog.show(selectedYear);
    }
    @Override
    public void toNext(int day){
        activity.setYear(++selectedYear);
        gregorianCalendar.set(Calendar.YEAR, selectedYear);
        onSetMaxDay();
    }
    @Override
    public void toLast(int day){
        activity.setYear(--selectedYear);
        gregorianCalendar.set(Calendar.YEAR, selectedYear);
        onSetMaxDay();
    }


}
