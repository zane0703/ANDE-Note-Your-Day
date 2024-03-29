/**
 * @Author Ang Yun Zane
 * @Author Lucas Tan
 * @Author Lim I Kin
 * class DIT/FT/2A/21
 */
package sg.LIZ.assignment1.view.layout;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;
import java.util.GregorianCalendar;

import sg.LIZ.assignment1.R;
public class MonthYearPickerDialog extends DialogFragment {

    public static final int MAX_YEAR;
    private final DatePickerDialog.OnDateSetListener listener;
    private final CharSequence[] MONTHS;
    private final FragmentManager FRAGMENT_MANAGER;

    static{
        GregorianCalendar gregorianCalendar= new GregorianCalendar();
        MAX_YEAR = gregorianCalendar.getMaximum(Calendar.YEAR);
    }

    public  MonthYearPickerDialog(@NonNull final FragmentManager fragmentManager, @Nullable final CharSequence[] monthList, @NonNull DatePickerDialog.OnDateSetListener listener){
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
        /*check if there array of month*/
       if(MONTHS==null){
           /*if not the there will be no option for the user to set month. this is in the fragment_view_by_year.xml where*/
           monthPicker.setVisibility(View.GONE);
       }else{
           /*convent  CharSequence to  String*/
           String[] months= new String[12];
           for(int i =0;i<12;++i){
               months[i]=MONTHS[i].toString();
           }
           monthPicker.setDisplayedValues(months);
           monthPicker.setMinValue(0);
           monthPicker.setMaxValue(11);
           monthPicker.setValue(month);
       }
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