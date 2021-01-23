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

public class YearPickerDialog extends DialogFragment {

    public static final int MAX_YEAR;
    private final DatePickerDialog.OnDateSetListener listener;
    private final FragmentManager FRAGMENT_MANAGER;

    static{
        GregorianCalendar gregorianCalendar= new GregorianCalendar();
        MAX_YEAR = gregorianCalendar.getMaximum(Calendar.YEAR);
    }

    public  YearPickerDialog( final FragmentManager fragmentManager,DatePickerDialog.OnDateSetListener listener){
        this.FRAGMENT_MANAGER =fragmentManager;
        this.listener = listener;
    }
    private int year;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();


        View dialog = inflater.inflate(R.layout.dialog_year_picker, null);
        final NumberPicker yearPicker = (NumberPicker) dialog.findViewById(R.id.picker_year);
        yearPicker.setMinValue(0);
        yearPicker.setMaxValue(MAX_YEAR);
        yearPicker.setValue(year);
        builder.setView(dialog)
                // Add action buttons
                .setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDateSet(null, yearPicker.getValue(),0, 0);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        YearPickerDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
    private final static String s ="";
    public void show(int year){
        super.show(FRAGMENT_MANAGER, s);
        this.year=year;
    }
}
