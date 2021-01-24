package sg.LIZ.assignment1.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.view.menu.ListMenuItemView;

import sg.LIZ.assignment1.R;
import sg.LIZ.assignment1.model.valueBean.ItemSliderMenu;

public class SlidingMenuAdapter extends BaseAdapter {
    private Context context;
    private String[] listItem;
    public SlidingMenuAdapter(Context context,String[] listItem ){
        this.context =context;
        this.listItem=listItem;
    }
    @Override
    public int getCount() {
        return listItem.length;
    }

    @Override
    public Object getItem(int position) {
        return listItem[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") View view =View.inflate(context, R.layout.item_sliding_menu, null);
        ((TextView) view.findViewById(R.id.sliding_item_title)).setText(listItem[position]);
        return view;
    }
}
