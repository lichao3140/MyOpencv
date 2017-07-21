package com.lqh.lichao.myopencv.com.lqh.lichao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.lqh.lichao.myopencv.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-07-20.
 */

public class MyListViewAdapter extends BaseAdapter{
    private List<CommandData> commandDataList;
    private Context context;

    public MyListViewAdapter(Context appcontext) {
        this.context = appcontext;
        this.commandDataList = new ArrayList<>();
    }

    public List<CommandData> getModel() {
        return this.commandDataList;
    }

    @Override
    public int getCount() {
        return commandDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return commandDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return commandDataList.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowitem, viewGroup, false);
        TextView txtItem = (TextView) rowView.findViewById(R.id.row_textView);
        rowView.setTag(commandDataList.get(i));
        txtItem.setText(commandDataList.get(i).getCommand());
        return rowView;
    }

}
