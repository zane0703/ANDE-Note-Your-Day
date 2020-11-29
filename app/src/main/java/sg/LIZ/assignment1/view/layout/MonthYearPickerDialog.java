package sg.LIZ.assignment1.view.layout;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;
import java.util.GregorianCalendar;

import sg.LIZ.assignment1.R;
public class MonthYearPickerDialog extends DialogFragment {

    public static final int MAX_YEAR;
    private final DatePickerDialog.OnDateSetListener listener;
    private final String[] MONTHS;
    private final FragmentManager FRAGMENT_MANAGER;

    static{
        GregorianCalendar gregorianCalendar= new GregorianCalendar();
        MAX_YEAR = gregorianCalendar.getMaximum(Calendar.YEAR);
    }

    public  MonthYearPickerDialog( final FragmentManager fragmentManager,final  String[] monthList,DatePickerDialog.OnDateSetListener listener){
        this.FRAGMENT_MANAGER =fragmentManager;
        this.MONTHS =monthList;
        this.listener = listener;
    }
    private int month;
    private int year;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();


        View dialog = inflater.inflate(R.layout.dialog_date_picker, null);
        final NumberPicker monthPicker = (NumberPicker) dialog.findViewById(R.id.picker_month);
        final NumberPicker yearPicker = (NumberPicker) dialog.findViewById(R.id.picker_year);
        monthPicker.setDisplayedValues(MONTHS);
        monthPicker.setMinValue(0);
        monthPicker.setMaxValue(11);
        monthPicker.setValue(month);
        yearPicker.setMinValue(0);
        yearPicker.setMaxValue(MAX_YEAR);
        yearPicker.setValue(year);
        builder.setView(dialog)
                // Add action buttons
                .setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDateSet(null, yearPicker.getValue(), monthPicker.getValue(), 0);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MonthYearPickerDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
    private  static String s ="hello";
    public void show(int year,int month){
    super.show(FRAGMENT_MANAGER, s);
    this.year=year;
    this.month=month;
    }
}