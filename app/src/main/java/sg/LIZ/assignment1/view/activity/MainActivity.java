package sg.LIZ.assignment1.view.activity;
/**
 * @Author Ang Yun Zane ,Lucas Tan , Lim I Kin
 * @class DIT/FT/2A/21
 */

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Arrays;
import sg.LIZ.assignment1.R;
import sg.LIZ.assignment1.model.valueBean.Task;
import sg.LIZ.assignment1.model.utilityBean.TaskDb;
import sg.LIZ.assignment1.view.layout.MonthYearPickerDialog;

@SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
public class MainActivity extends AppCompatActivity {
    private GregorianCalendar gregorianCalendar = new GregorianCalendar();
    private Button[] daysBtn;
    private String[] months;
    private TextView yearView;
    private TextView monthView;
    private TextView noTaskMassage;
    private ListView listTaskItem;
    private Drawable selectedStyle;
    private Drawable selectedWithTaskStyle;
    private Drawable dayWithTaskStyle;
    private MonthYearPickerDialog mDatePickerDialog = null;
    private final TaskDb taskDb = new TaskDb(this);
    private int selectedDay = gregorianCalendar.get(Calendar.DAY_OF_MONTH);
    private int selectedMonth = gregorianCalendar.get(Calendar.MONTH);
    private int selectedYear = gregorianCalendar.get(Calendar.YEAR);
    private int selectedDayIndex = 0;
    private byte currentDay = (byte) selectedDay;
    private byte currentMonth = (byte) selectedMonth;
    private int currentYear = selectedYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_main);
        daysBtn = new Button[]{
                findViewById(R.id.day_1),
                findViewById(R.id.day_2),
                findViewById(R.id.day_3),
                findViewById(R.id.day_4),
                findViewById(R.id.day_5),
                findViewById(R.id.day_6),
                findViewById(R.id.day_7),
                findViewById(R.id.day_8),
                findViewById(R.id.day_9),
                findViewById(R.id.day_10),
                findViewById(R.id.day_11),
                findViewById(R.id.day_12),
                findViewById(R.id.day_13),
                findViewById(R.id.day_14),
                findViewById(R.id.day_15),
                findViewById(R.id.day_16),
                findViewById(R.id.day_17),
                findViewById(R.id.day_18),
                findViewById(R.id.day_19),
                findViewById(R.id.day_20),
                findViewById(R.id.day_21),
                findViewById(R.id.day_22),
                findViewById(R.id.day_23),
                findViewById(R.id.day_24),
                findViewById(R.id.day_25),
                findViewById(R.id.day_26),
                findViewById(R.id.day_27),
                findViewById(R.id.day_28),
                findViewById(R.id.day_29),
                findViewById(R.id.day_30),
                findViewById(R.id.day_31),
                findViewById(R.id.day_32),
                findViewById(R.id.day_33),
                findViewById(R.id.day_34),
                findViewById(R.id.day_35),
                findViewById(R.id.day_36),
                findViewById(R.id.day_37),
                findViewById(R.id.day_38),
                findViewById(R.id.day_39),
                findViewById(R.id.day_40),
                findViewById(R.id.day_41),
                findViewById(R.id.day_42)
        };
        selectedStyle = getDrawable(R.drawable.layout_selected_day);
        selectedWithTaskStyle = getDrawable(R.drawable.layout_selected_with_task);
        dayWithTaskStyle = getDrawable(R.drawable.layout_day_with_task);
        monthView = findViewById(R.id.month);
        yearView = findViewById(R.id.year);
        noTaskMassage = findViewById(R.id.no_task_msg);
        listTaskItem = findViewById(R.id.list_task_item);
        months = getResources().getStringArray(R.array.month);
        monthView.setText(months[selectedMonth]);
        yearView.setText(Integer.toString(selectedYear));
        gregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
        onSetMonth();
    }



    private void onSetMonth() {
        int daysInMonth = gregorianCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int[] daysWithTask = taskDb.getTaskByMonthYear(selectedMonth, selectedYear);
        int firstWeekOfMonth = gregorianCalendar.get(Calendar.DAY_OF_WEEK);
        /*Check if is the first week is sunday if yes set it to 7 else minus 1*/
        if (firstWeekOfMonth == 1) {
            firstWeekOfMonth = 6;
        } else {
            firstWeekOfMonth -= 2;
        }
        int i = 0;
        if (firstWeekOfMonth != 0) {
            for (int start = new GregorianCalendar(selectedYear, selectedMonth - 1, 1).getActualMaximum(Calendar.DAY_OF_MONTH) - firstWeekOfMonth; i < firstWeekOfMonth; ++i) {
                Button dayBtn = daysBtn[i];
                dayBtn.setBackgroundColor(0x00000000);
                dayBtn.setTextColor(0xff999797);
                dayBtn.setText(Integer.toString(++start));
                final int day = start;
                dayBtn.setOnClickListener( v-> toLastMonth(day));

            }
        }
        for (int j = 1; j <= daysInMonth; ++i,++j) {
            final Button dayBtn = daysBtn[i];
            dayBtn.setText(Integer.toString(j));
            boolean isSelectedDay =j == selectedDay;
           boolean isDayWithTask =Arrays.binarySearch(daysWithTask, j) >-1;
            if(selectedYear==currentYear&&selectedMonth==currentMonth&&j==currentDay){
                dayBtn.setTextColor(0xffff0000);
            }else{
                dayBtn.setTextColor(0xffffffff);
            }
            if(isSelectedDay&&isDayWithTask){
                dayBtn.setBackground(selectedWithTaskStyle);
                selectedDayIndex = i;
                showTask();
            }else if(isDayWithTask){
                dayBtn.setBackground(dayWithTaskStyle);
            }else if(isSelectedDay){
                selectedDayIndex = i;
                dayBtn.setBackground(selectedStyle);
                listTaskItem.setVisibility(View.INVISIBLE);
                listTaskItem.setAdapter(null);
                noTaskMassage.setVisibility(View.VISIBLE);
            }else {
                dayBtn.setBackgroundColor(0x00000000);
            }
            dayBtn.setOnClickListener( new OnDayClickListener(j, i, isDayWithTask));
        }
        for (int j = 1; i < 42; ++j, ++i) {
            Button dayBtn = daysBtn[i];
            dayBtn.setBackgroundColor(0x00000000);
            dayBtn.setTextColor(0xff999797);
            dayBtn.setText(Integer.toString(j));
            final int day = j;
            dayBtn.setOnClickListener( v ->toNextMonth(day));
        }
    }
    public void onSetMonth(View v){
        if(mDatePickerDialog==null){
            mDatePickerDialog = new MonthYearPickerDialog(getSupportFragmentManager(),months,((view, year, month, dayOfMonth) -> {
                    if(selectedYear!=year||selectedMonth!=month){
                        selectedYear = year;
                        selectedMonth = month;
                        selectedDay=1;
                        monthView.setText(months[month]);
                        yearView.setText(Integer.toString(year));
                        gregorianCalendar.set(year, month, 1);
                        onSetMonth();
                    }
            }));
        }
        mDatePickerDialog.show(selectedYear,selectedMonth);

    }
    public void toNextMonth(int day){
        if (selectedMonth == 11) {
            if(selectedYear==MonthYearPickerDialog.MAX_YEAR){
                Toast.makeText(this, R.string.max_year, Toast.LENGTH_SHORT).show();
                return;
            }
            selectedMonth = 0;
            yearView.setText(Integer.toString(++selectedYear));
        } else {
            ++selectedMonth;
        }
        selectedDay=day;
        daysBtn[selectedDayIndex].setBackgroundResource(0);
        monthView.setText(months[selectedMonth]);
        gregorianCalendar.set(selectedYear, selectedMonth, 1);
        onSetMonth();
    }
    public void toLastMonth(int day){
        if (selectedMonth == 0) {
            if(selectedYear==0){
                Toast.makeText(this, R.string.min_year, Toast.LENGTH_SHORT).show();
                return;
            }
            selectedMonth = 11;
            yearView.setText(Integer.toString(--selectedYear));
        } else {
            --selectedMonth;
        }
        selectedDay=day;
        daysBtn[selectedDayIndex].setBackgroundResource(0);
        monthView.setText(months[selectedMonth]);
        gregorianCalendar.set(selectedYear, selectedMonth, 1);
        onSetMonth();
    }
    private void showTask(){

        final Task[] task = taskDb.getTaskByDate(selectedDay, selectedMonth, selectedYear);
        if(task.length>0){
            noTaskMassage.setVisibility(View.INVISIBLE);
            listTaskItem.setVisibility(View.VISIBLE);
            int taskSize = task.length;
            String[] taskTitle = new String[taskSize];
            for (int i = 0 ;i<taskSize;++i){
                taskTitle[i] = task[i].TITLE;
            }
            listTaskItem.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1,taskTitle));
            listTaskItem.setOnItemClickListener((AdapterView<?> adapterView, View view, int position, long l)->{
               Intent i = new Intent(this,TaskDetailActivity.class);
               i.putExtra(TaskDetailActivity.TASK_ID, task[position].ID);
               startActivityForResult(i, 2);
            });
        }else{
            noTaskMassage.setVisibility(View.VISIBLE);
            listTaskItem.setVisibility(View.INVISIBLE);
            listTaskItem.setAdapter(null);
            daysBtn[selectedDayIndex].setBackground(selectedStyle);
            daysBtn[selectedDayIndex].setOnClickListener(new OnDayClickListener(selectedDay, selectedDayIndex, false));
        }

    }

    public void onAddTask(View v){
        Intent i =  new Intent(this,AddTaskActivity.class);
        i.putExtra(AddTaskActivity.DAY,selectedDay);
        i.putExtra(AddTaskActivity.MONTH, selectedMonth);
        i.putExtra(AddTaskActivity.IS_EDIT, false);
        i.putExtra(AddTaskActivity.YEAR, selectedYear);
        startActivityForResult(i, 1);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("a",Integer.toString((resultCode)));
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case 1:
                    Button dayBtn=daysBtn[selectedDayIndex];
                    if(dayBtn.getBackground()==selectedStyle){
                        dayBtn.setBackground(selectedWithTaskStyle);
                        dayBtn.setOnClickListener(new OnDayClickListener(selectedDay, selectedDayIndex, true));
                    }
                    showTask();
                    break;
                case 2:
                    showTask();
                    break;
            }
        }
    }
    private final class OnDayClickListener implements View.OnClickListener{
        private final int DAY_NUM;
        private final int INDEX;
        private final boolean HAS_TASK;
        public OnDayClickListener(final int dayNum,final int index,final  boolean hasTask){
            this.DAY_NUM =dayNum;
            this.INDEX=index;
            this.HAS_TASK=hasTask;
        }
        @Override
        public void onClick(View v) {
            Button oldSelectedBtn =  daysBtn[selectedDayIndex];
            selectedDay =DAY_NUM;
            if(oldSelectedBtn.getBackground()==selectedWithTaskStyle){
                oldSelectedBtn.setBackground(dayWithTaskStyle);
            }else{
                oldSelectedBtn.setBackgroundResource(0);
            }
            if(HAS_TASK){
                v.setBackground(selectedWithTaskStyle);
                showTask();
            }else{
                v.setBackground(selectedStyle);
                noTaskMassage.setVisibility(View.VISIBLE);
                listTaskItem.setVisibility(View.INVISIBLE);
                listTaskItem.setAdapter(null);
            }
            selectedDayIndex = INDEX;
        }
    }
}