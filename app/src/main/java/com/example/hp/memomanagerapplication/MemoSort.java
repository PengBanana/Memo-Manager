package com.example.hp.memomanagerapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MemoSort extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final int NEWMEMOACTIVITY_CODE = 1;
    public static final int EDITITEM_CODE = 2;
    public static final int DELETEITEM_CODE = 3;
    public static final int CANCELNEWMEMO = 4;
    private ArrayList<Memo> memoList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MemoAdapter mAdapter;
    public MySQLiteHelper db = new MySQLiteHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_sort);
        memoList = db.getAllMemos();
        db.onUpgrade(db.getReadableDatabase(),1,2);
        Log.d("memoList", memoList.toString());
        insertSampleData();
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
                Intent newMemoActivityIntent = new Intent(MemoSort.this, newMemoActivity.class);
                MemoSort.this.startActivityForResult(newMemoActivityIntent, NEWMEMOACTIVITY_CODE);
                /*Snackbar.make(view, "Create New Memo", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.memo_sort, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_active) {
            // Handle the camera action
            getMemoList("'ACTIVE'");
        } else if (id == R.id.nav_complete) {
            getMemoList("'COMPLETE'");
        } else if (id == R.id.nav_onprogress) {
            getMemoList("Onprogress");
        } else if (id == R.id.nav_overdue) {
            getMemoList("'OVERDUE'");
        }
        else if (id == R.id.nav_all) {
            getMemoListAll();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getMemoList(String where) {
        memoList.clear();
        memoList.addAll(db.getMemoWhere(where));
        Log.d("memoListuponAdd:", memoList.size()+"");
        recyclerView.getAdapter().notifyDataSetChanged();
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
                    getMemoList("ONPROGRESS");
                    break;
            }
        }
    }

    public void getMemoListAll(){
        memoList.clear();
        memoList.addAll(db.getAllMemos());
        Log.d("memoListuponAdd:", memoList.size()+"");
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_refresh) {
            updateMemoList();
            return true;
        }*/
        if (id == R.id.action_sortbyTitle) {
            orderBy(Memo.TITLE_CODE);
            return true;
        }
        else if (id == R.id.action_sortbyDate) {
            orderBy(Memo.DEADLINE_CODE);
            return true;
        }
        else if (id == R.id.action_sortbyPriority) {
            orderBy(Memo.PRIORITYLEVEL_CODE);
            return true;
        }
        else if (id == R.id.action_sortbyStatus) {
            orderBy(Memo.STATUS_CODE);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void orderBy(String Code) {
            db.useTemporaryTable();
            for(int i=0; i<memoList.size(); i++){
                db.addTempMemo(memoList.get(i));
            }
            memoList.clear();
            memoList.addAll(db.getTempMemoBy(Code));
            recyclerView.getAdapter().notifyDataSetChanged();
    }


    @Override
    protected void onResume() {
        super.onResume();
        getMemoListAll();
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
                "ACTIVE",
                "User interface Only");
        db.addMemo(sampleMemo);
        sampleMemo = new
                Memo("ITMETHDS Paper",
                "Academic",
                "March 16,2018",
                "High",
                "Daily",
                "8:00AM",
                "ACTIVE",
                "Thesis paper");
        db.addMemo(sampleMemo);
        sampleMemo = new
                Memo("LSCS Internal Meeting",
                "Organization",
                "March 09,2018",
                "Medium",
                "Daily",
                "10:00AM",
                "COMPLETE",
                "Updates, Problems and Solutions");
        db.addMemo(sampleMemo);
        sampleMemo = new
                Memo("BMS Unplugged Linkages",
                "Organization",
                "March 10,2018",
                "Low",
                "Daily",
                "9:00am",
                "OVERDUE",
                "Remind External EVP and Corporel VP");
        db.addMemo(sampleMemo);

        sampleMemo = new
                Memo("WIR-TEC Beta Demo Again",
                "Academic",
                "April 14,2018",
                "Highest",
                "Weekly",
                "9:00PM",
                "ACTIVE",
                "User interface Only");
        db.addMemo(sampleMemo);
        sampleMemo = new
                Memo("ITMETHDS Paper Again",
                "Academic",
                "April 16,2018",
                "High",
                "Daily",
                "8:00AM",
                "ACTIVE",
                "Thesis paper");
        db.addMemo(sampleMemo);
        sampleMemo = new
                Memo("LSCS Internal Meeting Again",
                "Organization",
                "April 09,2018",
                "Medium",
                "Daily",
                "10:00AM",
                "COMPLETE",
                "Updates, Problems and Solutions");
        db.addMemo(sampleMemo);
        sampleMemo = new
                Memo("BMS Unplugged Linkages Again",
                "Organization",
                "April 10,2018",
                "Low",
                "Daily",
                "9:00AM",
                "OVERDUE",
                "Remind External EVP and Corporel VP");
        db.addMemo(sampleMemo);

        sampleMemo = new
                Memo("WIR-TEC Beta Demo Part 3",
                "Academic",
                "May 14,2018",
                "Highest",
                "Weekly",
                "9:00PM",
                "ACTIVE",
                "User interface Only");
        db.addMemo(sampleMemo);
        sampleMemo = new
                Memo("ITMETHDS Paper Part 3",
                "Academic",
                "May 16,2018",
                "High",
                "Daily",
                "8:00AM",
                "ACTIVE",
                "Thesis paper");
        db.addMemo(sampleMemo);
        sampleMemo = new
                Memo("LSCS Internal Meeting Part 3",
                "Organization",
                "May 09,2018",
                "Medium",
                "Daily",
                "10:00AM",
                "COMPLETE",
                "Updates, Problems and Solutions");
        db.addMemo(sampleMemo);
        sampleMemo = new
                Memo("BMS Unplugged Linkages Part 3",
                "Organization",
                "May 10,2018",
                "Low",
                "Daily",
                "9:00AM",
                "OVERDUE",
                "Remind External EVP and Corporel VP");
        db.addMemo(sampleMemo);
    }

    private void checkData(){

    }
}
