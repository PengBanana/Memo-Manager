package com.example.hp.memomanagerapplication;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ViewMemoDetails extends AppCompatActivity {
    public static final int EDITITEM_CODE=2;
    public static final int DELETEITEM_CODE=3;
    public int itemId;
    public TextView title,category,deadline,priorityLevel,notificationIntervals,notificationTime,status,note;
    public MySQLiteHelper db = new MySQLiteHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_memo_details);
        Button btn_edit = (Button) findViewById(R.id.btn_editItem);
        Button btn_cancel = (Button) findViewById(R.id.btn_back);
        Button btn_delete = (Button) findViewById(R.id.btn_delete);
        title = (TextView) findViewById(R.id.tv_title);
        category = (TextView) findViewById(R.id.tv_category);
        deadline = (TextView) findViewById(R.id.tv_deadline);
        priorityLevel = (TextView) findViewById(R.id.tv_prioritylevel);
        notificationIntervals = (TextView) findViewById(R.id.tv_notificationsintervals);
        notificationTime = (TextView) findViewById(R.id.tv_notificationtime);
        status = (TextView) findViewById(R.id.tv_status);
        note = (TextView) findViewById(R.id.tv_note);


        super.onStart();
        Bundle extras = getIntent().getExtras();
        /*int itemClicked=extras.getInt("itemClicked");
        //ArrayList<Memo> memoList =(ArrayList<Memo>) extras.getParcelable("memoList");
        //Memo item = memoList.get(itemClicked);
        Memo item = new Memo(extras.getString("title"),extras.getString("category"),extras.getString("deadline"),extras.getString("level"),extras.getString("intervals"),extras.getString("time"),extras.getString("status"),extras.getString("note"));
        item.setId(extras.getInt("id"));*/
        itemId = extras.getInt("itemId");
        setUI(itemId);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteMemo(db.getMemo(itemId));
                finish();
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editItemIntent = new Intent(ViewMemoDetails.this, newMemoActivity.class);
                editItemIntent.putExtra(Memo.TITLE_CODE, title.getText().toString());
                editItemIntent.putExtra(Memo.CATEGORY_CODE, category.getText().toString());
                editItemIntent.putExtra(Memo.DEADLINE_CODE, deadline.getText().toString());
                editItemIntent.putExtra(Memo.PRIORITYLEVEL_CODE, priorityLevel.getText().toString());
                editItemIntent.putExtra(Memo.NOTIFICATIONINTERVALS_CODE, notificationIntervals.getText().toString());
                editItemIntent.putExtra(Memo.NOTIFICATIONTIME_CODE, notificationTime.getText().toString());
                editItemIntent.putExtra(Memo.STATUS_CODE, status.getText().toString());
                editItemIntent.putExtra(Memo.NOTE_CODE, note.getText().toString());
                editItemIntent.putExtra("action", EDITITEM_CODE);
                editItemIntent.putExtra(Memo.ID_CODE, itemId);
                ViewMemoDetails.this.startActivityForResult(editItemIntent, EDITITEM_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case EDITITEM_CODE:
                Memo newItem = new Memo(
                        data.getStringExtra(Memo.TITLE_CODE), data.getStringExtra(Memo.CATEGORY_CODE),
                        data.getStringExtra(Memo.DEADLINE_CODE),data.getStringExtra(Memo.PRIORITYLEVEL_CODE),
                        data.getStringExtra(Memo.NOTIFICATIONINTERVALS_CODE), data.getStringExtra(Memo.NOTIFICATIONTIME_CODE),
                        data.getStringExtra(Memo.STATUS_CODE), data.getStringExtra(Memo.NOTE_CODE));
                newItem.setId(itemId);
                db.updateMemo(newItem);
                setUI(itemId);
                break;
        }

    }

    public void setUI(int itemId){

        Memo item=db.getMemo(itemId);
        title.setText(item.getTitle());
        category.setText(item.getCategory());
        deadline.setText(item.getDeadline());
        priorityLevel.setText(item.getPriorityLevel());
        notificationIntervals.setText(item.getNotificationIntervals());
        notificationTime.setText(item.getNotificationTime());
        note.setText(item.getNote());
        String statustext = item.getStatus();
        status.setText(statustext);
        if(statustext.equalsIgnoreCase("active")){
            status.setTextColor(Color.parseColor("#e7b416"));
        }
        else if(statustext.equalsIgnoreCase("complete")){
            status.setTextColor(Color.parseColor("#99c140"));
        }
        else if(statustext.equalsIgnoreCase("overdue")){
            status.setTextColor(Color.parseColor("#cc3232"));
        }
        else{
            status.setTextColor(Color.parseColor("#e7b416"));
        }
    }
}
