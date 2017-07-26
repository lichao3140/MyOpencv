package com.lqh.lichao.myopencv;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lqh.lichao.myopencv.com.lqh.lichao.adapter.CommandConstants;
import com.lqh.lichao.myopencv.com.lqh.lichao.adapter.CommandData;
import com.lqh.lichao.myopencv.com.lqh.lichao.adapter.MyListViewAdapter;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private MyListViewAdapter myViewAdapter;
    private String command;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadViewData();
    }

    private void loadViewData() {
        myViewAdapter = new MyListViewAdapter(this.getApplicationContext());
        ListView listView = (ListView)this.findViewById(R.id.command_ListView);
        listView.setAdapter(myViewAdapter);
        listView.setOnItemClickListener(this);
        myViewAdapter.getModel().addAll(CommandData.getCommandList());
        myViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Object obj = view.getTag();
        if(obj instanceof CommandData) {
            CommandData cmddata = (CommandData)obj;
            command = cmddata.getCommand();
        }
        processCommand();
    }

    private void processCommand() {
        if(CommandConstants.THRESHOLD_BINARY_COMMAND.equals(command) ||
                CommandConstants.ADAPTIVE_THRESHOLD_COMMAND.equals(command) ||
                CommandConstants.ADAPTIVE_GAUSSIAN_COMMAND.equals(command) ||
                CommandConstants.HOUGH_LINES_COMMAND.equals(command) ||
                CommandConstants.HOUGH_CIRCLE_COMMAND.equals(command) ||
                CommandConstants.FIND_CONTOURS_COMMAND.equals(command) ||
                CommandConstants.MEASURE_OBJECT_COMMAND.equals(command) ||
                CommandConstants.CANNY_EDGE_COMMAND.equals(command)) {
            Intent intent = new Intent(this.getApplicationContext(), ThresholdProcessActivity.class);
            intent.putExtra("command", command);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this.getApplicationContext(), ProcessImageActivity.class);
            intent.putExtra("command", command);
            startActivity(intent);
        }
    }
}
