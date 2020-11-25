package sg.LIZ.assignment1.view.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import sg.LIZ.assignment1.R;
import sg.LIZ.assignment1.model.utilityBean.TaskDb;
import sg.LIZ.assignment1.model.valueBean.Task;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TaskDetailActivity extends AppCompatActivity {
    public static final String TASK_ID ="id";
    private final TaskDb taskDb = new TaskDb(this);
    private int id ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_task_detail);
        Intent i = getIntent();
        id = i.getExtras().getInt(TASK_ID);
        Task mTask = taskDb.getTaskById(id);
        ((TextView)findViewById(R.id.detail_title)).setText(mTask.TITLE);
        ((TextView)findViewById(R.id.detail_description)).setText(mTask.DESCRIPTION);
        ((TextView)findViewById(R.id.detail_venue)).setText(mTask.VENUE);
       if(mTask.ALL_DAY){
           findViewById(R.id.detail_show_task_time).setVisibility(View.INVISIBLE);
       }else{
           findViewById(R.id.detail_is_all_day).setVisibility(View.INVISIBLE);
           int startHours = mTask.START_HOURS;
           int startMinutes=mTask.START_MINUTES;
           int endHours = mTask.END_HOURS;
           int endMinutes = mTask.END_MINUTES;
           ((TextView)findViewById(R.id.detail_show_start_time)).setText(new StringBuilder(8)
                   .append(startHours >12 ? startHours - 12 : startHours)
                   .append(':').append(startMinutes>9?Integer.toString(startMinutes):"0"+startMinutes)
                   .append(' ')
                   .append(startHours >11 ? new char[]{'P', 'M'} : new char[]{'A', 'M'}));
           ((TextView)findViewById(R.id.detail_show_end_time)).setText(new StringBuilder(8)
                   .append(endHours >12 ? endHours - 12 : endHours)
                   .append(':').append(endMinutes>9?Integer.toString(endMinutes):"0"+endMinutes)
                   .append(' ')
                   .append(endHours >11 ? new char[]{'P', 'M'} : new char[]{'A', 'M'}));
       }
        ((TextView) findViewById(R.id.detail_task_date)).setText(mTask.DAY + " " + getResources().getStringArray(R.array.month)[mTask.MONTH]);
    }
    public void onDelete(View v){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.delete_task)
                .setMessage(R.string.delete_task2)
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    taskDb.deleteTask(id);
                    setResult(Activity.RESULT_OK);
                    finish();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    public void onBackPressed(View v){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}