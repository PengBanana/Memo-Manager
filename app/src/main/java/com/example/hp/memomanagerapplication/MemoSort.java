package com.example.hp.memomanagerapplication;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
        mainActivityMethod();
    }

    public void mainActivityMethod(){
        dbManipulation();
        recyclerAndAdapter();
        //getMemoList("Onprogress");
        toolbarAndFloatingActionButton();
        //notificationSetter();
        alarmReciever();
        //getMemoList("OG");
        //recyclerView.getAdapter().notifyDataSetChanged();
}

    private void dbManipulation() {
        //db.onUpgrade(db.getReadableDatabase(),1,2);
        Log.d("memoList", memoList.toString());
        //insertSampleData();
        db.updateData();
        memoList.clear();
        memoList.addAll(db.getMemoWhere("OG"));
        Log.d("memoList",memoList.size()+"");
        //getMemoList("Onprogress");
    }

    private void alarmReciever() {
        Log.d("alarmRecievermain", "ran");
        AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(System.currentTimeMillis());
        time.add(Calendar.MINUTE, 1);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);

    }

    private void setAlarm(String alarmDate, String alarmTime, String alarmIntervals){
        //TODO: if null checking
        String[] dateSplitter=alarmDate.split("/");
        String[] timeSplitter=alarmTime.split(":");
        AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        Calendar alarmSetTime = Calendar.getInstance();
        if(alarmDate.isEmpty()){
            alarmSetTime.setTimeInMillis(System.currentTimeMillis());
            alarmSetTime.add(Calendar.MINUTE, 1);
            Log.d("AlarmSetTo2:", alarmTime+"");
        }
        else{
            int alarmHour=Integer.parseInt(timeSplitter[0]);
            int alarmMinute=Integer.parseInt(timeSplitter[1]);
            int alarmSecond=0;
            int alarmMonth=Integer.parseInt(dateSplitter[0]);
            int alarmDay=Integer.parseInt(dateSplitter[1]);
            int alarmYear=Integer.parseInt(dateSplitter[2]);
            alarmSetTime.set(Calendar.DAY_OF_MONTH, alarmDay);
            alarmSetTime.set(Calendar.MONTH, alarmMonth);
            alarmSetTime.set(Calendar.YEAR, alarmYear);
            alarmSetTime.set(Calendar.HOUR_OF_DAY, alarmHour);
            alarmSetTime.set(Calendar.MINUTE, alarmMinute);
            alarmSetTime.set(Calendar.SECOND, alarmSecond);
            Log.d("AlarmSetTo:", alarmMonth+"/"+alarmDay+"/"+alarmYear+" "+alarmHour+":"+alarmMinute);

        }
        alarmMgr.set(AlarmManager.RTC_WAKEUP, alarmSetTime.getTimeInMillis(), pendingIntent);
    }

    private void toolbarAndFloatingActionButton() {

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

    private void recyclerAndAdapter() {
        recyclerView = (RecyclerView) findViewById(R.id.rv_itemList);
        mAdapter = new MemoAdapter(memoList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
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
        String where="All";
        if (id == R.id.nav_active) {
            // Handle the camera action
            where="ACTIVE";
        } else if (id == R.id.nav_complete) {
            where="COMPLETE";
        } else if (id == R.id.nav_onprogress) {
            where="OG";
        } else if (id == R.id.nav_overdue) {
            where="OVERDUE";
        }
        else if (id == R.id.nav_all) {
            getMemoListAll();
        }
        getMemoList(where);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getMemoList(String where) {
        memoList.clear();
        memoList.addAll(db.getMemoWhere(where));
        Log.d("getMemoList by "+where+":", memoList.size()+"");
        //orderBy(Memo.DEADLINE_CODE);
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
                    String deadline=data.getStringExtra(Memo.DEADLINE_CODE);
                    String time=data.getStringExtra(Memo.NOTIFICATIONTIME_CODE);
                    String notificationIntervals=data.getStringExtra(Memo.NOTIFICATIONINTERVALS_CODE);
                    Memo newItem = new Memo(
                            data.getStringExtra(Memo.TITLE_CODE), data.getStringExtra(Memo.CATEGORY_CODE),
                            deadline ,data.getStringExtra(Memo.PRIORITYLEVEL_CODE),
                            notificationIntervals, time,
                            data.getStringExtra(Memo.STATUS_CODE), data.getStringExtra(Memo.NOTE_CODE));
                    db.addMemo(newItem);
                    //get time of notification
                    setAlarm(deadline, time, notificationIntervals);
                    getMemoList("OG");
                    Log.d("memoListCount:", memoList.size()+"");

                    break;
            }
        }
    }

    public void getMemoListAll(){
        db.updateData();
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

        recyclerView.getAdapter().notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

    private void orderBy(String Code) {
        Log.d("orderBy:", Code);
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
        Log.d("insertSampleData", "default");
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
}
