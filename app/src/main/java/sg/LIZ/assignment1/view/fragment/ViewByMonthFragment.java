package sg.LIZ.assignment1.view.fragment;
/**
 * @Author Ang Yun Zane ,Lucas Tan , Lim I Kin
 * @class DIT/FT/2A/21
 */

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Arrays;

import sg.LIZ.assignment1.Key;
import sg.LIZ.assignment1.R;
import sg.LIZ.assignment1.model.valueBean.Task;
import sg.LIZ.assignment1.model.utilityBean.TaskDb;
import sg.LIZ.assignment1.view.activity.AddTaskActivity;
import sg.LIZ.assignment1.view.activity.MainActivity;
import sg.LIZ.assignment1.view.layout.MonthYearPickerDialog;
import sg.LIZ.assignment1.view.adapter.TaskArrayAdapter;
import sg.LIZ.assignment1.view.layout.onSetMonth;

@SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
public class ViewByMonthFragment extends Fragment implements  onSetMonth {
    private GregorianCalendar gregorianCalendar = null;
    private Button[] daysBtn;
    private TextView textViewNoTaskMassage;
    private RecyclerView listTaskItem;
    private Drawable selectedStyle;
    private Drawable selectedWithTaskStyle;
    private Drawable dayWithTaskStyle;
    private MonthYearPickerDialog mDatePickerDialog = null;
    private  TaskDb taskDb = null;
    private int selectedDay = 1;
    private int selectedMonth = 0;
    private int selectedYear = 0;
    private int selectedDayIndex = 0;
    private MainActivity activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity =(MainActivity) getActivity();
        taskDb = new TaskDb(activity);
        View view = inflater.inflate(R.layout.fragment_view_by_month, container, false);
        daysBtn = new Button[]{
                //42 squares in each page to represent 42 days
                view.findViewById(R.id.day_1),
                view.findViewById(R.id.day_2),
                view.findViewById(R.id.day_3),
                view.findViewById(R.id.day_4),
                view.findViewById(R.id.day_5),
                view.findViewById(R.id.day_6),
                view.findViewById(R.id.day_7),
                view.findViewById(R.id.day_8),
                view.findViewById(R.id.day_9),
                view.findViewById(R.id.day_10),
                view.findViewById(R.id.day_11),
                view.findViewById(R.id.day_12),
                view.findViewById(R.id.day_13),
                view.findViewById(R.id.day_14),
                view.findViewById(R.id.day_15),
                view.findViewById(R.id.day_16),
                view.findViewById(R.id.day_17),
                view.findViewById(R.id.day_18),
                view.findViewById(R.id.day_19),
                view.findViewById(R.id.day_20),
                view.findViewById(R.id.day_21),
                view.findViewById(R.id.day_22),
                view.findViewById(R.id.day_23),
                view.findViewById(R.id.day_24),
                view.findViewById(R.id.day_25),
                view.findViewById(R.id.day_26),
                view.findViewById(R.id.day_27),
                view.findViewById(R.id.day_28),
                view.findViewById(R.id.day_29),
                view.findViewById(R.id.day_30),
                view.findViewById(R.id.day_31),
                view.findViewById(R.id.day_32),
                view.findViewById(R.id.day_33),
                view.findViewById(R.id.day_34),
                view.findViewById(R.id.day_35),
                view.findViewById(R.id.day_36),
                view.findViewById(R.id.day_37),
                view.findViewById(R.id.day_38),
                view.findViewById(R.id.day_39),
                view.findViewById(R.id.day_40),
                view.findViewById(R.id.day_41),
                view.findViewById(R.id.day_42)
        };
        selectedYear = activity.getYear();
        selectedMonth=activity.getMonth();
        if(selectedYear==Key.currentYear&&selectedMonth==Key.currentMonth){
            selectedDay=Key.currentDay;
        }
        gregorianCalendar = new GregorianCalendar(selectedYear, selectedMonth, 1);
        listTaskItem = view.findViewById(R.id.list_task_item);
        view.findViewById(R.id.add_task_btn).setOnClickListener((v)->{
            Intent i =  new Intent(activity, AddTaskActivity.class);
            i.putExtra(Key.KEY_DAY,selectedDay);
            i.putExtra(Key.KEY_MONTH, selectedMonth);
            i.putExtra(Key.KEY_YEAR, selectedYear);
            startActivityForResult(i, 1);

        });
        selectedStyle = activity.getDrawable(R.drawable.layout_selected_day);
        selectedWithTaskStyle = activity.getDrawable(R.drawable.layout_selected_with_task);
        dayWithTaskStyle = activity.getDrawable(R.drawable.layout_day_with_task);
        textViewNoTaskMassage = view.findViewById(R.id.no_task_msg);
        listTaskItem = view.findViewById(R.id.list_task_item);
        listTaskItem.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        onSetMonth();
        return view;
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
                dayBtn.setOnClickListener( v-> toLast(day));

            }
        }
        for (int j = 1; j <= daysInMonth; ++i,++j) {
            final Button dayBtn = daysBtn[i];
            dayBtn.setText(Integer.toString(j));
            boolean isSelectedDay =j == selectedDay;
           boolean isDayWithTask =Arrays.binarySearch(daysWithTask, j) >-1;
            if(selectedYear==Key.currentYear&&selectedMonth==Key.currentMonth&&j==Key.currentDay){
                dayBtn.setTextColor(0xff76A5E3);
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
                textViewNoTaskMassage.setVisibility(View.VISIBLE);
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
            dayBtn.setOnClickListener( v ->toNext(day));
        }
    }
    @Override
    public void onSetYear(final String[] months){
        if(mDatePickerDialog==null){
            mDatePickerDialog = new MonthYearPickerDialog(getParentFragmentManager(),months,((view, year, month, dayOfMonth) -> {
                    if(selectedYear!=year||selectedMonth!=month){
                        selectedYear = year;
                        selectedMonth = month;
                        selectedDay=1;
                        activity.setMonth(month)
                                .setYear(year);
                        gregorianCalendar.set(year, month, 1);
                        onSetMonth();
                    }
            }));
        }
        mDatePickerDialog.show(selectedYear,selectedMonth);

    }
    @Override
    public void toNext(int day){
        if (selectedMonth == 11) {
            if(selectedYear==MonthYearPickerDialog.MAX_YEAR){
                Toast.makeText(activity, R.string.max_year, Toast.LENGTH_SHORT).show();
                return;
            }
            selectedMonth = 0;
           activity.setYear(++selectedYear);
        } else {
            ++selectedMonth;
        }
        selectedDay=day;
        daysBtn[selectedDayIndex].setBackgroundResource(0);
        activity.setMonth(selectedMonth);
        gregorianCalendar.set(selectedYear, selectedMonth, 1);
        onSetMonth();
    }
    @Override
    public void toLast(int day){
        if (selectedMonth == 0) {
            if(selectedYear==0){
                Toast.makeText(activity, R.string.min_year, Toast.LENGTH_SHORT).show();
                return;
            }
            selectedMonth = 11;

        } else {
            --selectedMonth;
        }
        selectedDay=day;
        daysBtn[selectedDayIndex].setBackgroundResource(0);
        activity.setMonth(selectedMonth);
        gregorianCalendar.set(selectedYear, selectedMonth, 1);
        onSetMonth();
    }
    private void showTask(){

        final Task[] task = taskDb.getTaskByDate(selectedDay, selectedMonth, selectedYear);
        if(task.length>0){
            textViewNoTaskMassage.setVisibility(View.INVISIBLE);
            listTaskItem.setVisibility(View.VISIBLE);
            TaskArrayAdapter taskArrayAdapter = new TaskArrayAdapter(activity,this, task);
            listTaskItem.setAdapter(taskArrayAdapter);
            int taskSize = task.length;

            String[] taskTitle = new String[taskSize];
            for (int i = 0 ;i<taskSize;++i){
                taskTitle[i] = task[i].TITLE;
            }/*
            listTaskItem.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1,taskTitle));
            listTaskItem.setOnItemClickListener((AdapterView<?> adapterView, View view, int position, long l)->{
               Intent i = new Intent(this,TaskDetailActivity.class);
               i.putExtra(TaskDetailActivity.TASK_ID, task[position].ID);
               startActivityForResult(i, 2);
            });*/
        }else{
            textViewNoTaskMassage.setVisibility(View.VISIBLE);
            listTaskItem.setVisibility(View.INVISIBLE);
            listTaskItem.setAdapter(null);
            daysBtn[selectedDayIndex].setBackground(selectedStyle);
            daysBtn[selectedDayIndex].setOnClickListener(new OnDayClickListener(selectedDay, selectedDayIndex, false));
        }

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
                textViewNoTaskMassage.setVisibility(View.VISIBLE);
                listTaskItem.setVisibility(View.INVISIBLE);
                listTaskItem.setAdapter(null);
            }
            selectedDayIndex = INDEX;
        }
    }
}