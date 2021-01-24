package sg.LIZ.assignment1.view.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import sg.LIZ.assignment1.model.valueBean.Task;
import sg.LIZ.assignment1.R;
import sg.LIZ.assignment1.view.activity.TaskDetailActivity;

public  class TaskArrayAdapter extends RecyclerView.Adapter<TaskArrayAdapter.MyViewHolder>  {
    private final Task[] mTasks;
    private final Activity activity;
    private final Fragment fragment;
    public  TaskArrayAdapter(@NonNull final Activity activity, @Nullable Fragment fragment, @NonNull final Task[] mTasks) {
       this.mTasks =mTasks;
        this.activity=activity;
        this.fragment =fragment;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Create View Holder
        final MyViewHolder myViewHolder = new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recycler_item, parent, false));

        //Item Clicks
        myViewHolder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(activity, TaskDetailActivity.class);
            i.putExtra(TaskDetailActivity.TASK_ID,mTasks[myViewHolder.getLayoutPosition()].ID);
            if(fragment==null){
                activity.startActivityForResult(i,2 );
            }else{
                fragment.startActivityForResult(i, 2);
            }
        });
        return myViewHolder;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Task mTask = mTasks[position];
        holder.textViewTitle.setText(mTask.TITLE);
        if(mTask.ALL_DAY){
            holder.textViewTime.setVisibility(View.GONE);
        }else{
            holder.textViewAllDay.setVisibility(View.GONE);
            final int startHours = mTask.START_HOURS;
            final int endHours = mTask.END_HOURS;
            final String FORMAT = "%02d";
            holder.textViewTime.setText(new StringBuilder(19)
                    .append(startHours >12 ? startHours - 12 : startHours)
                    .append(':').append(String.format(FORMAT, mTask.START_MINUTES))
                    .append(' ')
                    .append(startHours >11 ? new char[]{'P', 'M',' ','-',' '} : new char[]{'A', 'M',' ','-',' '})
                    .append(endHours >12 ? endHours - 12 : endHours)
                    .append(':').append(String.format(FORMAT, mTask.END_MINUTES))
                    .append(' ')
                    .append(endHours >11 ? new char[]{'P', 'M'} : new char[]{'A', 'M'}));
        }
    }

    @Override
    public int getItemCount() {
        return mTasks.length;
    }

    final class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle;
        private final TextView textViewTime;
        private final TextView textViewAllDay;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.recycler_task_title);
            textViewTime= itemView.findViewById(R.id.recycler_task_time);
            textViewAllDay = itemView.findViewById(R.id.recycler_all_day);

        }
    }


}
