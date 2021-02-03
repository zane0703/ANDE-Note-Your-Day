/**
 * @Author Ang Yun Zane
 * @Author Lucas Tan
 * @Author Lim I Kin
 * class DIT/FT/2A/21
 */
package sg.LIZ.assignment1.view.fragment;

import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    /*show the dialog to allow the user to jump the a set year*/
    @Override
    public void onSetYear(final CharSequence[] months){
        /*check if the dialog created*/
        if(mDatePickerDialog==null){
            /*if not crate the dialog*/
            mDatePickerDialog = new MonthYearPickerDialog(getParentFragmentManager(),null,((view2, year, month, dayOfMonth) -> {
                /*check if the user had selected the same year*/
                if(selectedYear!=year){
                    selectedYear = year;
                    activity.setYear(year);
                    gregorianCalendar.set(GregorianCalendar.YEAR, year);
                    /*get the last day of fad*/
                    fedText.setText(Integer.toString(gregorianCalendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH)));
                }
            }));
        }
        mDatePickerDialog.show(selectedYear,0);
    }
    /*go to next year*/
    @Override
    public void toNext(int day){
        /*check if the the user already at the max year the device can show */
        if (selectedYear == MonthYearPickerDialog.MAX_YEAR) {
            /*if is at max year show a message to tell the user that he/she is already at the max*/
            Toast.makeText(activity, R.string.max_year, Toast.LENGTH_SHORT).show();
            return;
        }
        activity.setYear(++selectedYear);
        gregorianCalendar.set(GregorianCalendar.YEAR, selectedYear);
        fedText.setText(Integer.toString(gregorianCalendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH)));
    }
    /*go to last year*/
    @Override
    public void toLast(int day){
        /*check if is already at year 0*/
        if (selectedYear == 0) {
            /*tell the user that he/she have already at the min*/
            Toast.makeText(activity, R.string.min_year, Toast.LENGTH_SHORT).show();
            return;
        }
        activity.setYear(--selectedYear);
        gregorianCalendar.set(Calendar.YEAR, selectedYear);
        fedText.setText(Integer.toString(gregorianCalendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH)));
    }


}
