package com.example.hp.memomanagerapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ViewMemoDetails extends AppCompatActivity {
    public TextView title,category,deadline,priorityLevel,notificationIntervals,notificationTime,status,note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //set view
        //end set view
        setContentView(R.layout.activity_view_memo_details);
        title = (TextView) findViewById(R.id.tv_title);
        category = (TextView) findViewById(R.id.tv_category);
        deadline = (TextView) findViewById(R.id.tv_deadline);
        priorityLevel = (TextView) findViewById(R.id.tv_prioritylevel);
        notificationIntervals = (TextView) findViewById(R.id.tv_notificationsintervals);
        notificationTime = (TextView) findViewById(R.id.tv_notificationtime);
        status = (TextView) findViewById(R.id.tv_status);
        note = (TextView) findViewById(R.id.tv_note);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle extras = getIntent().getExtras();
        int itemClicked=extras.getInt("itemClicked");
        //ArrayList<Memo> memoList =(ArrayList<Memo>) extras.getParcelable("memoList");
        //Memo item = memoList.get(itemClicked);
        Memo item = new Memo(extras.getString("title"),extras.getString("category"),extras.getString("deadline"),extras.getString("level"),extras.getString("intervals"),extras.getString("time"),extras.getString("status"),extras.getString("note"));
        title.setText(item.getTitle());
        category.setText(item.getCategory());
        deadline.setText(item.getDeadline());
        priorityLevel.setText(item.getPriorityLevel());
        notificationIntervals.setText(item.getNotificationIntervals());
        notificationTime.setText(item.getNotificationTime());
        note.setText(item.getNote());
    }
}
