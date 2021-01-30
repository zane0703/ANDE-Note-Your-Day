package sg.LIZ.assignment1.view.fragment;

import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import sg.LIZ.assignment1.R;
import sg.LIZ.assignment1.view.activity.MainActivity;
import sg.LIZ.assignment1.view.layout.MonthYearPickerDialog;
import sg.LIZ.assignment1.view.layout.onSetMonth;
@SuppressLint("SetTextI18n")
public class ViewByYearFragment extends Fragment implements onSetMonth {
    private GregorianCalendar gregorianCalendar = null;
    private int selectedYear = 0;
    private MonthYearPickerDialog mDatePickerDialog = null;
    private TextView fedText;
    private MainActivity activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity=(MainActivity) getActivity();
        View view =inflater.inflate(R.layout.fragment_view_by_year, container, false);
        fedText= view.findViewById(R.id.view_by_year_day_Fed);
        selectedYear = activity.getYear();
        gregorianCalendar =new GregorianCalendar(selectedYear,1,1);
        fedText.setText(Integer.toString(gregorianCalendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH)));
        return view;
    }
    @Override
    public void onSetYear(final CharSequence[] months){
        if(mDatePickerDialog==null){
            mDatePickerDialog = new MonthYearPickerDialog(getParentFragmentManager(),null,((view2, year, month, dayOfMonth) -> {
                if(selectedYear!=year){
                    selectedYear = year;
                    activity.setYear(year);
                    gregorianCalendar.set(GregorianCalendar.YEAR, year);
                    fedText.setText(Integer.toString(gregorianCalendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH)));
                }
            }));
        }
        mDatePickerDialog.show(selectedYear,0);
    }
    @Override
    public void toNext(int day){
        activity.setYear(++selectedYear);
        gregorianCalendar.set(GregorianCalendar.YEAR, selectedYear);
        fedText.setText(Integer.toString(gregorianCalendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH)));
    }
    @Override
    public void toLast(int day){
        activity.setYear(--selectedYear);
        gregorianCalendar.set(Calendar.YEAR, selectedYear);
        fedText.setText(Integer.toString(gregorianCalendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH)));
    }


}
