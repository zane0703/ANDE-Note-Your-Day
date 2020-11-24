package sg.LIZ.assignment1.view.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import sg.LIZ.assignment1.R;
import sg.LIZ.assignment1.model.utilityBean.TaskDb;
import sg.LIZ.assignment1.model.valueBean.Task;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.GridLayout;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AddTaskActivity extends AppCompatActivity {
    public static final String DAY = "day";
    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String IS_EDIT = "isEdit";
    private GridLayout timeSelect;
    private TimePickerDialog startTimePicker =null;
    private TimePickerDialog endTimePicker=null;
    private Button startTimeBtn;
    private Button endTimeBtn;
    private EditText titleInput;
    private EditText descriptionInput;
    private EditText venueInput;
    private int selectedDay;
    private int selectedMonth;
    private int selectedYear;
    private boolean allDay;
    private int startHours;
    private int startMinutes;
    private int endHours;
    private int endMinutes;

    private TaskDb taskDb = new TaskDb(this);

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_add_task);
        Intent i = getIntent();
        selectedDay = i.getIntExtra(DAY, -1);
        selectedMonth = i.getIntExtra(MONTH, -1);
        selectedYear = i.getIntExtra(YEAR, -1);
        timeSelect = findViewById(R.id.add_task_time);
        startTimeBtn = findViewById(R.id.add_start_time);
        endTimeBtn = findViewById(R.id.add_end_time);
        titleInput = findViewById(R.id.add_title);
        descriptionInput = findViewById(R.id.add_description);
        venueInput = findViewById(R.id.add_value);
        GregorianCalendar currentDate = new GregorianCalendar();
        startHours = currentDate.get(Calendar.HOUR_OF_DAY);
        startMinutes = currentDate.get(Calendar.MINUTE);
        startTimeBtn.setText(new StringBuilder(8)
                .append(currentDate.get(Calendar.HOUR))
                .append(':').append(startMinutes>9?Integer.toString(startMinutes):"0"+startMinutes)
                .append(' ')
                .append(currentDate.get(Calendar.AM_PM) == 1 ? new char[]{'P', 'M'} : new char[]{'A', 'M'}));
        currentDate.set(Calendar.HOUR_OF_DAY, currentDate.get(Calendar.HOUR_OF_DAY) + 1);
        endHours = currentDate.get(Calendar.HOUR_OF_DAY);
        endMinutes = currentDate.get(Calendar.MINUTE);
        endTimeBtn.setText(new StringBuilder(8)
                .append(currentDate.get(Calendar.HOUR))
                .append(':')
                .append(endMinutes>9?Integer.toString(endMinutes):"0"+endMinutes)
                .append(' ')
                .append(currentDate.get(Calendar.AM_PM) == 1 ? new char[]{'P', 'M'} : new char[]{'A', 'M'}));
        ((TextView) findViewById(R.id.task_date)).setText(selectedDay + " " + getResources().getStringArray(R.array.month)[selectedMonth]);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.discard_task)
                .setMessage(R.string.discard_task2)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    public void onBackPressed(View v) {
        onBackPressed();
    }

    public void onAllDayToggled(View v) {
        if ((allDay = ((SwitchCompat) v).isChecked())) {
            timeSelect.setVisibility(View.INVISIBLE);
        } else {
            timeSelect.setVisibility(View.VISIBLE);
        }
    }

    public void onSetStartTime(View v) {
        if(startTimePicker==null){
            startTimePicker=  new TimePickerDialog(this, (v2, hours, minute) -> {
                if (hours < endHours || minute < endMinutes) {
                    startHours = hours;
                    startMinutes = minute;
                    startTimeBtn.setText(new StringBuilder(8)
                            .append( hours > 12 ? startHours - 12 : startHours)
                            .append(':').append(startMinutes>9?Integer.toString(startMinutes):"0"+startMinutes)
                            .append(' ')
                            .append( hours > 11 ? new char[]{'P', 'M'} : new char[]{'A', 'M'}));
                } else {
                    Toast.makeText(this, R.string.set_start_time_err, Toast.LENGTH_LONG).show();
                }
            }, startHours, startMinutes, false);
        }
        startTimePicker.show();

    }

    public void onSetEndTime(View v) {
        if(endTimePicker==null){
            endTimePicker=  new TimePickerDialog(this, (v2, hours, minute) -> {
                if (hours > startHours || minute > startMinutes) {
                    endHours = hours;
                    endMinutes = minute;
                    endTimeBtn.setText(new StringBuilder(8)
                            .append(hours > 12? endHours - 12 : endHours)
                            .append(':').append(endMinutes>9?Integer.toString(endMinutes):"0"+endMinutes)
                            .append(' ')
                            .append(hours > 11? new char[]{'P', 'M'} : new char[]{'A', 'M'}));
                } else {
                    Toast.makeText(this, R.string.set_end_time_err, Toast.LENGTH_LONG).show();
                }
            }, endHours, endMinutes, false);
        }
        endTimePicker.show();
    }

    public void onSubmit(View v) {
        String title = titleInput.getText().toString().trim();
        String description = descriptionInput.getText().toString().trim();
        String venue = venueInput.getText().toString().trim();
        if (title.equals("") || description.equals("") || venue.equals("")) {
            Toast.makeText(this,R.string.no_input_error, Toast.LENGTH_LONG).show();
        } else {
            if (allDay) {
                startMinutes = -1;
                startHours = -1;
                endMinutes = -1;
                endHours = -1;
            }
            Task mTask = new Task((byte) selectedDay, (byte) selectedMonth, selectedYear, (byte) startHours, (byte) startMinutes, (byte) endHours, (byte) endMinutes, allDay, title, description, venue);
            taskDb.addTask(mTask);
            setResult(Activity.RESULT_OK);
            Toast.makeText(this, R.string.add_successfully, Toast.LENGTH_LONG).show();
            finish();
        }

    }

}