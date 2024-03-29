/**
 * @Author Ang Yun Zane
 * @Author Lucas Tan
 * @Author Lim I Kin
 * class DIT/FT/2A/21
 */
package sg.LIZ.assignment1.view.activity;

import sg.LIZ.assignment1.Key;
import sg.LIZ.assignment1.R;
import sg.LIZ.assignment1.model.utilityBean.TaskDb;
import sg.LIZ.assignment1.model.valueBean.Task;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskDetailActivity extends AppCompatActivity {
    private final TaskDb taskDb = new TaskDb(this);
    private int id;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Intent i = getIntent();
        id = i.getExtras().getInt(Key.KEY_ID);
        Task mTask = taskDb.getTaskById(id);
        ((TextView) findViewById(R.id.detail_title)).setText(mTask.TITLE);
        ((TextView) findViewById(R.id.detail_description)).setText(mTask.DESCRIPTION);
        ((TextView) findViewById(R.id.detail_venue)).setText(mTask.VENUE);
        /*check if task have image*/
        if (mTask.image != null) {
            /*if have show the image to image view */
            ImageView imageView = findViewById(R.id.detail_show_task_img);
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(mTask.image, 0, mTask.image.length));
            imageView.setVisibility(View.VISIBLE);
        }/*End of if statement*/
        /*check if is all day */
        if (mTask.ALL_DAY) {
            /*if true show the "all day" text*/
            findViewById(R.id.detail_show_task_time).setVisibility(View.INVISIBLE);
        } else {
            /*else show the start and end time */
            findViewById(R.id.detail_is_all_day).setVisibility(View.INVISIBLE);
            final int startHours = mTask.START_HOURS;
            final int endHours = mTask.END_HOURS;
            final String FORMAT = "%02d";/*format to add zero in front if the minutes is less then 10 */
            ((TextView) findViewById(R.id.detail_show_start_time)).setText(new StringBuilder(8)
                    .append(startHours > 12 ? startHours - 12 : startHours)
                    .append(':').append(String.format(FORMAT, mTask.START_MINUTES))
                    .append(startHours > 11 ? new char[]{' ','P', 'M'} : new char[]{' ','A', 'M'}));
            ((TextView) findViewById(R.id.detail_show_end_time)).setText(new StringBuilder(8)
                    .append(endHours > 12 ? endHours - 12 : endHours)
                    .append(':').append(String.format(FORMAT, mTask.END_MINUTES))
                    .append(endHours > 11 ? new char[]{' ','P', 'M'} : new char[]{' ','A', 'M'}));
        }/*End of if else*/
        ((TextView) findViewById(R.id.detail_task_date)).setText(new StringBuilder().append(mTask.DAY).append(' ').append(getResources().getStringArray(R.array.month)[mTask.MONTH]));
    }/*End of onCreate*/
    /*run this to delete task*/
    public void onDelete(final View v) {
        /*confirm with the user if there relay want to delete task*/
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
    }/*End of onDelete*/
    /*navigate to google map to show the venue*/
    public void onClickVenue(final View v) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + Uri.encode(((TextView) v).getText().toString()))));
    }
    /*set result to canceled when the user go back*/
    public void onBackPressed(final View v) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }/*End of onBackPressed*/
}