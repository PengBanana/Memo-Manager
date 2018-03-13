package com.example.hp.memomanagerapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ViewMemoActivity extends AppCompatActivity {
    public static final int NEWMEMOACTIVITY_CODE = 1;
    private ArrayList<Memo> memoList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MemoAdapter mAdapter;
    public MySQLiteHelper db = new MySQLiteHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_memo);
        //upgrade database TODO: Ask sir about this upgrade
        //db.onUpgrade(db.getReadableDatabase(),0,0);


        //Insert Sample Data
        //insertSampleData();
        memoList = db.getAllMemos();
        Log.d("memoList", memoList.toString());

        //RecyclerView and Adapter Call
        recyclerView = (RecyclerView) findViewById(R.id.rv_itemList);
        mAdapter = new MemoAdapter(memoList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        //Toolbar and FAB
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_addItem);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Launch activity create New Memo
                Intent newMemoActivityIntent = new Intent(ViewMemoActivity.this, newMemoActivity.class);
                ViewMemoActivity.this.startActivityForResult(newMemoActivityIntent, NEWMEMOACTIVITY_CODE);
                /*Snackbar.make(view, "Create New Memo", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        //swipe refresh attempt

    }

    private void insertSampleData() {
        //String title, String category, String deadline, String priorityLevel, String notificationIntervals, String notificationTime, String status
        Memo sampleMemo = new
                Memo("WIR-TEC Beta Demo",
                "Academic",
                "March 14,2018",
                "Highest",
                "Weekly",
                "9:00PM",
                "Active",
                "User interface Only");
        db.addMemo(sampleMemo);
        sampleMemo = new
                Memo("ITMETHDS Paper",
                "Academic",
                "March 16,2018",
                "High",
                "Daily",
                "8:00AM",
                "Active",
                "Thesis paper");
        db.addMemo(sampleMemo);
        sampleMemo = new
                Memo("LSCS Internal Meeting",
                "Organization",
                "March 09,2018",
                "Medium",
                "Daily",
                "10:00AM",
                "Complete",
                "Updates, Problems and Solutions");
        db.addMemo(sampleMemo);
        sampleMemo = new
                Memo("BMS Unplugged Linkages",
                "Organization",
                "March 10,2018",
                "Low",
                "Daily",
                "9:00AM",
                "Overdue",
                "Remind External EVP and Corporel VP");
        db.addMemo(sampleMemo);
        
        sampleMemo = new
                Memo("WIR-TEC Beta Demo Again",
                "Academic",
                "April 14,2018",
                "Highest",
                "Weekly",
                "9:00PM",
                "Active",
                "User interface Only");
        db.addMemo(sampleMemo);
        sampleMemo = new
                Memo("ITMETHDS Paper Again",
                "Academic",
                "April 16,2018",
                "High",
                "Daily",
                "8:00AM",
                "Active",
                "Thesis paper");
        db.addMemo(sampleMemo);
        sampleMemo = new
                Memo("LSCS Internal Meeting Again",
                "Organization",
                "April 09,2018",
                "Medium",
                "Daily",
                "10:00AM",
                "Complete",
                "Updates, Problems and Solutions");
        db.addMemo(sampleMemo);
        sampleMemo = new
                Memo("BMS Unplugged Linkages Again",
                "Organization",
                "April 10,2018",
                "Low",
                "Daily",
                "9:00AM",
                "Overdue",
                "Remind External EVP and Corporel VP");
        db.addMemo(sampleMemo);
        
        sampleMemo = new
                Memo("WIR-TEC Beta Demo Part 3",
                "Academic",
                "May 14,2018",
                "Highest",
                "Weekly",
                "9:00PM",
                "Active",
                "User interface Only");
        db.addMemo(sampleMemo);
        sampleMemo = new
                Memo("ITMETHDS Paper Part 3",
                "Academic",
                "May 16,2018",
                "High",
                "Daily",
                "8:00AM",
                "Active",
                "Thesis paper");
        db.addMemo(sampleMemo);
        sampleMemo = new
                Memo("LSCS Internal Meeting Part 3",
                "Organization",
                "May 09,2018",
                "Medium",
                "Daily",
                "10:00AM",
                "Complete",
                "Updates, Problems and Solutions");
        db.addMemo(sampleMemo);
        sampleMemo = new
                Memo("BMS Unplugged Linkages Part 3",
                "Organization",
                "May 10,2018",
                "Low",
                "Daily",
                "9:00AM",
                "Overdue",
                "Remind External EVP and Corporel VP");
        db.addMemo(sampleMemo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode) {
                case NEWMEMOACTIVITY_CODE:
                    Log.d("new memo",requestCode+"");
                    /*
                    String title, String category, String deadline,
                String priorityLevel, String notificationIntervals,
                String notificationTime, String status, String note
                     */
                    Memo newItem = new Memo(
                            data.getStringExtra(Memo.TITLE_CODE), data.getStringExtra(Memo.CATEGORY_CODE),
                            data.getStringExtra(Memo.DEADLINE_CODE),data.getStringExtra(Memo.PRIORITYLEVEL_CODE),
                            data.getStringExtra(Memo.NOTIFICATIONINTERVALS_CODE), data.getStringExtra(Memo.NOTIFICATIONTIME_CODE),
                            data.getStringExtra(Memo.STATUS_CODE), data.getStringExtra(Memo.NOTE_CODE));
                    db.addMemo(newItem);
                    Log.d("memoListbeforeAdd:", memoList.size()+"");
                    memoList.clear();
                    memoList.addAll(db.getAllMemos());

                    Log.d("memoListuponAdd:", memoList.size()+"");
                    recyclerView.getAdapter().notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_memo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
