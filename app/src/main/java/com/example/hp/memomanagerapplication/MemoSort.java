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
import android.os.SystemClock;
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
import java.text.Format;
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
    public static final String sdFormat="yyyy-MM-dd";
    private ArrayList<Memo> memoList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MemoAdapter mAdapter;
    public MySQLiteHelper db = new MySQLiteHelper(this);

    SimpleDateFormat mdyFormat = new SimpleDateFormat(sdFormat, Locale.ENGLISH);
    SimpleDateFormat hmFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    SimpleDateFormat hourFormat = new SimpleDateFormat("HH", Locale.ENGLISH);
    SimpleDateFormat minuteFormat = new SimpleDateFormat("mm", Locale.ENGLISH);
    SimpleDateFormat secondFormat = new SimpleDateFormat("ss", Locale.ENGLISH);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_sort);
        Log.d("MemoSort:","onCreate-START");
        mainActivityMethod();
        Log.d("MemoSort:","onCreate-END");
    }

    public void mainActivityMethod(){
        Log.d("MemoSort:","mainActivityMethod-START");
        dbManipulation();
        recyclerAndAdapter();
        //getMemoList("Onprogress");
        toolbarAndFloatingActionButton();
        //notificationSetter();
        alarmReciever();
        //getMemoList("OG");
        //recyclerView.getAdapter().notifyDataSetChanged();
        Log.d("MemoSort:","mainActivityMethod-END");
}

    private void dbManipulation() {
        Log.d("MemoSort:","dbManipulation-START");
        db.onUpgrade(db.getReadableDatabase(),1,2);
        insertSampleData();
        db.updateData();
        memoList.clear();
        memoList.addAll(db.getMemoWhere("OG"));
        //getMemoList("Onprogress");
        Log.d("MemoSort:","dbManipulation-END");
    }

    private void alarmReciever() {
        Log.d("MemoSort:","alarmReciever-START");

        //dd/MM/YYYY
        long timeNow = System.currentTimeMillis();
        String alarmDate = mdyFormat.format(timeNow);
        String alarmTime = hmFormat.format(timeNow);
        setAlarm(alarmDate, alarmTime);
        //sample notification
        AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(System.currentTimeMillis());
        time.add(Calendar.SECOND, 10);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
        Log.d("MemoSort:","alarmReciever-END");
    }

    public void setAlarm(String alarmDate, String alarmTime){
        Log.d("MemoSort:","setAlarm-START");
        //TODO: if null checking
        String[] dateSplitter=alarmDate.split("/");
        String[] timeSplitter=alarmTime.split(":");
        Intent intent = new Intent(this, AlarmReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        Calendar alarmSetTime = Calendar.getInstance();
        AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        if(alarmDate.isEmpty()){
            alarmSetTime.setTimeInMillis(System.currentTimeMillis());
            alarmSetTime.add(Calendar.SECOND, 10);
        }
        else{
            //alarm once attempt
            //TODO: COMPARE CURRENT DATE TO ALARM DATE
            long timeNow=System.currentTimeMillis();
            int monthNow = Integer.parseInt(monthFormat.format(timeNow));
            int dayNow = Integer.parseInt(dayFormat.format(timeNow));
            int yearNow = Integer.parseInt(yearFormat.format(timeNow));
            int hourNow = Integer.parseInt(hourFormat.format(timeNow));
            int minuteNow = Integer.parseInt(minuteFormat.format(timeNow));
            int secondNow = Integer.parseInt(secondFormat.format(timeNow));
            //int alarmHour=9;
            //int alarmMinute=0;
            //instantiating stuff
            /*
            if(timeSplitter.length>0){
                alarmHour=9;
                alarmMinute=0;
            }
            else{
                alarmHour=9;
                alarmMinute=0;
            }
            int alarmMonth=Integer.parseInt(dateSplitter[1]);
            int alarmDay=Integer.parseInt(dateSplitter[0]);
            int alarmYear=Integer.parseInt(dateSplitter[2]);
            if(alarmHour<hourNow){
                alarmDay++;
            }
            */
            //attempt to alarm at 9am everyday
            alarmSetTime.set(yearNow, monthNow, dayNow,hourNow, minuteNow+1, 0);
            alarmMgr.set(AlarmManager.RTC_WAKEUP, alarmSetTime.getTimeInMillis(), pendingIntent);
            alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    alarmSetTime.getTimeInMillis(),
                    AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
            /*
            //computing how many to add to curent time
            int addMonth=alarmMonth-monthNow;
            int addDay=alarmDay-dayNow;
            int addYear=alarmYear-yearNow;
            int addHour=alarmHour-hourNow;
            int addMinute=alarmMinute-minuteNow;
            //alternative
            alarmSetTime.add(Calendar.MONTH, addMonth);
            alarmSetTime.add(Calendar.DATE, addDay);
            alarmSetTime.add(Calendar.YEAR, addYear);
            alarmSetTime.add(Calendar.HOUR, addHour);
            alarmSetTime.add(Calendar.MINUTE, addMinute);

            //set
            /*
            alarmSetTime.set(Calendar.DAY_OF_MONTH, alarmDay);
            alarmSetTime.set(Calendar.MONTH, alarmMonth);
            alarmSetTime.set(Calendar.YEAR, alarmYear);
            alarmSetTime.set(Calendar.HOUR_OF_DAY, alarmHour);
            alarmSetTime.set(Calendar.MINUTE, alarmMinute);
            alarmSetTime.set(Calendar.SECOND, alarmSecond);
            */
        }
        /*alarmMgr.set(AlarmManager.RTC_WAKEUP, alarmSetTime.getTimeInMillis(), pendingIntent);
        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HALF_HOUR,
                AlarmManager.INTERVAL_HALF_HOUR, pendingIntent);
        Log.d("MemoSort:","setAlarm-END");
        */
    }



    private void toolbarAndFloatingActionButton() {
        Log.d("MemoSort:","toolbarAndFloatingActionButton-START");

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
        Log.d("MemoSort:","toolbarAndFloatingActionButton-END");
    }

    private void recyclerAndAdapter() {
        Log.d("MemoSort:","recyclerAndAdapter-START");
        recyclerView = (RecyclerView) findViewById(R.id.rv_itemList);
        mAdapter = new MemoAdapter(memoList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        Log.d("MemoSort:","recyclerAndAdapter-END");
    }

    @Override
    public void onBackPressed() {
        Log.d("MemoSort:","onBackPressed-START");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        Log.d("MemoSort:","onBackPressed-END");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("MemoSort:","onCreateOptionsMenu-START");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.memo_sort, menu);
        Log.d("MemoSort:","onCreateOptionsMenu-END");
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.d("MemoSort:","onNavigationItemSelected-START");
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
        Log.d("MemoSort:","onNavigationItemSelected-END");
        return true;
    }

    private void getMemoList(String where) {
        Log.d("MemoSort:","getMemoList-START");
        memoList.clear();
        memoList.addAll(db.getMemoWhere(where));
        //orderBy(Memo.DEADLINE_CODE);
        recyclerView.getAdapter().notifyDataSetChanged();
        Log.d("MemoSort:","getMemoList-END");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("MemoSort:","onActivityResult-START");
        if(resultCode == RESULT_OK){
            switch (requestCode) {
                case NEWMEMOACTIVITY_CODE:
                    /*
                    String title, String category, String deadline,
                String priorityLevel, String notificationIntervals,
                String notificationTime, String status, String note
                     */
                    String deadline=data.getStringExtra(Memo.DEADLINE_CODE);
                    String time=data.getStringExtra(Memo.NOTIFICATIONTIME_CODE);
                    String notificationIntervals="Daily";
                    Memo newItem = new Memo(
                            data.getStringExtra(Memo.TITLE_CODE), data.getStringExtra(Memo.CATEGORY_CODE),
                            deadline ,data.getStringExtra(Memo.PRIORITYLEVEL_CODE),
                            notificationIntervals, time,
                            data.getStringExtra(Memo.STATUS_CODE), data.getStringExtra(Memo.NOTE_CODE));
                    db.addMemo(newItem);
                    //get time of notification
                    //setAlarm(deadline, time, notificationIntervals);
                    getMemoList("OG");

                    break;
            }
        }
        Log.d("MemoSort:","onActivityResult-END");
    }

    public void getMemoListAll(){
        Log.d("MemoSort:","getMemoListAll-START");
        db.updateData();
        memoList.clear();
        memoList.addAll(db.getAllMemos());
        recyclerView.getAdapter().notifyDataSetChanged();
        Log.d("MemoSort:","getMemoListAll-END");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("MemoSort:","getMemoListAll-START");
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
        Log.d("MemoSort:","getMemoListAll-END");
        return super.onOptionsItemSelected(item);
    }

    private void orderBy(String Code) {
        Log.d("MemoSort:","orderBy-START");
        db.useTemporaryTable();
        for(int i=0; i<memoList.size(); i++){
            db.addTempMemo(memoList.get(i));
        }
        memoList.clear();
        memoList.addAll(db.getTempMemoBy(Code));
        recyclerView.getAdapter().notifyDataSetChanged();
        Log.d("MemoSort:","orderBy-END");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MemoSort:","onResume-START");
        db.updateData();
        getMemoList("OG");
        Log.d("MemoSort:","onResume-END");
    }

    private void insertSampleData() {
        Log.d("MemoSort:","insertSampleData-START");
        //String title, String category, String deadline, String priorityLevel, String notificationIntervals, String notificationTime, String status
        Memo sampleMemo = new
                Memo("WIR-TEC Beta Demo",
                "Academic",
                "2018-03-14",
                "Highest",
                "Weekly",
                "9:00PM",
                "ACTIVE",
                "User interface Only");
        db.addMemo(sampleMemo);
        sampleMemo = new
                Memo("ITMETHDS Paper",
                "Academic",
                "2018-03-16",
                "High",
                "Daily",
                "8:00AM",
                "ACTIVE",
                "Thesis paper");
        db.addMemo(sampleMemo);
        sampleMemo = new
                Memo("LSCS Internal Meeting",
                "Organization",
                "2018-03-04",
                "Medium",
                "Daily",
                "10:00AM",
                "COMPLETE",
                "Updates, Problems and Solutions");
        db.addMemo(sampleMemo);
        sampleMemo = new
                Memo("BMS Unplugged Linkages",
                "Organization",
                "2018-03-10",
                "Low",
                "Daily",
                "9:00am",
                "OVERDUE",
                "Remind External EVP and Corporel VP");
        db.addMemo(sampleMemo);

        sampleMemo = new
                Memo("WIR-TEC Beta Demo Again",
                "Academic",
                "2018-04-14",
                "Highest",
                "Weekly",
                "9:00PM",
                "ACTIVE",
                "User interface Only");
        db.addMemo(sampleMemo);
        sampleMemo = new
                Memo("ITMETHDS Paper Again",
                "Academic",
                "2018-04-16",
                "High",
                "Daily",
                "8:00AM",
                "ACTIVE",
                "Thesis paper");
        db.addMemo(sampleMemo);
        sampleMemo = new
                Memo("LSCS Internal Meeting Again",
                "Organization",
                "2018-09-04",
                "Medium",
                "Daily",
                "10:00AM",
                "COMPLETE",
                "Updates, Problems and Solutions");
        db.addMemo(sampleMemo);
        sampleMemo = new
                Memo("BMS Unplugged Linkages Again",
                "Organization",
                "2018-04-10",
                "Low",
                "Daily",
                "9:00AM",
                "OVERDUE",
                "Remind External EVP and Corporel VP");
        db.addMemo(sampleMemo);

        sampleMemo = new
                Memo("WIR-TEC Beta Demo Part 3",
                "Academic",
                "2018-05-11",
                "Highest",
                "Weekly",
                "9:00PM",
                "ACTIVE",
                "User interface Only");
        db.addMemo(sampleMemo);
        sampleMemo = new
                Memo("ITMETHDS Paper Part 3",
                "Academic",
                "2018-05-16",
                "High",
                "Daily",
                "8:00AM",
                "ACTIVE",
                "Thesis paper");
        db.addMemo(sampleMemo);
        sampleMemo = new
                Memo("LSCS Internal Meeting Part 3",
                "Organization",
                "2018-05-09",
                "Medium",
                "Daily",
                "10:00AM",
                "COMPLETE",
                "Updates, Problems and Solutions");
        db.addMemo(sampleMemo);
        sampleMemo = new
                Memo("BMS Unplugged Linkages Part 3",
                "Organization",
                "2018-04-10",
                "Low",
                "Daily",
                "9:00AM",
                "OVERDUE",
                "Remind External EVP and Corporel VP");
        db.addMemo(sampleMemo);
        Log.d("MemoSort:","insertSampleData-END");
    }
}
