package com.example.hp.memomanagerapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by hp on 13/03/2018.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "MemoDB";
    private static final String TABLE_MEMO = "memos";
    private static final String KEY_ID = "id";
    SimpleDateFormat sdf = new SimpleDateFormat(MemoSort.sdFormat, Locale.ENGLISH);
    SimpleDateFormat tf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    private static final String[] COLUMNS = {
            Memo.ID_CODE,
            Memo.TITLE_CODE,
            Memo.CATEGORY_CODE,
            Memo.DEADLINE_CODE,
            Memo.NOTE_CODE,
            Memo.NOTIFICATIONINTERVALS_CODE,
            Memo.NOTIFICATIONTIME_CODE,
            Memo.PRIORITYLEVEL_CODE,
            Memo.STATUS_CODE};

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("MySqLiteHelper:","onCreate-START");
        // SQL statement to create book table
        String CREATE_MEMO_TABLE = "CREATE TABLE memos ( " +
                Memo.ID_CODE+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Memo.TITLE_CODE +" TEXT, "+
                Memo.CATEGORY_CODE+" TEXT, "+
                Memo.DEADLINE_CODE+" TEXT, "+
                Memo.NOTE_CODE+" TEXT, "+
                Memo.NOTIFICATIONINTERVALS_CODE+" TEXT, "+
                Memo.NOTIFICATIONTIME_CODE+" TEXT, "+
                Memo.PRIORITYLEVEL_CODE+" TEXT, "+
                Memo.STATUS_CODE+" TEXT"+")";
        // create books table
        db.execSQL(CREATE_MEMO_TABLE);
        Log.d("MySqLiteHelper:","onCreate-END");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("MySqLiteHelper:","onUpgrade-START");
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS memos");

        // create fresh books table
        this.onCreate(db);
        Log.d("MySqLiteHelper:","onUpgrade-END");
    }

    public void addMemo(Memo item){
        Log.d("MySqLiteHelper:","addMemo-START");
        //for logging

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(Memo.TITLE_CODE, item.getTitle()); // get title
        values.put(Memo.DEADLINE_CODE, item.getDeadline()); // get title
        values.put(Memo.NOTE_CODE, item.getNote()); // get title
        values.put(Memo.NOTIFICATIONINTERVALS_CODE, item.getNotificationIntervals()); // get title
        values.put(Memo.NOTIFICATIONTIME_CODE, item.getNotificationTime()); // get title
        values.put(Memo.PRIORITYLEVEL_CODE, item.getPriorityLevel()); // get title
        values.put(Memo.CATEGORY_CODE, item.getCategory()); // get title
        values.put(Memo.STATUS_CODE, item.getStatus()); // get title
        //
        // 3. insert
        db.insert(TABLE_MEMO, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
        Log.d("MySqLiteHelper:","addMemo-END");
    }
    public Memo getMemo(int id){
        Log.d("MySqLiteHelper:","getMemo-START");
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_MEMO, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object
        Memo item = new Memo();
        item.setId(Integer.parseInt(cursor.getString(0)));
        item.setTitle(cursor.getString(1));
        item.setCategory(cursor.getString(2));
        item.setDeadline(cursor.getString(3));
        item.setNote(cursor.getString(4));
        item.setNotificationIntervals(cursor.getString(5));
        item.setNotificationTime(cursor.getString(6));
        item.setPriorityLevel(cursor.getString(7));
        item.setStatus(cursor.getString(8));

        //log

        // 5. return book
        Log.d("MySqLiteHelper:","getMemo-END");
        return item;
    }
    public ArrayList<Memo> getAllMemos() {
        Log.d("MySqLiteHelper:","getAllMemos-START");
        ArrayList<Memo> memoList = new ArrayList<>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_MEMO;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Memo item = null;
        if (cursor.moveToFirst()) {
            do {
                item = new Memo();
                item.setId(Integer.parseInt(cursor.getString(0)));
                item.setTitle(cursor.getString(1));
                item.setCategory(cursor.getString(2));
                item.setDeadline(cursor.getString(3));
                item.setNote(cursor.getString(4));
                item.setNotificationIntervals(cursor.getString(5));
                item.setNotificationTime(cursor.getString(6));
                item.setPriorityLevel(cursor.getString(7));
                item.setStatus(cursor.getString(8));

                // Add book to books
                memoList.add(item);
            } while (cursor.moveToNext());
        }

        Log.d("MySqLiteHelper:","getAllMemos-END");
        // return books
        return memoList;
    }
    public ArrayList<Memo> getMemoWhere(String where) {
        Log.d("MySqLiteHelper:","getMemoWhere-START");
        ArrayList<Memo> memoList = new ArrayList<>();

        if(where.equalsIgnoreCase("OG")){
            where=" WHERE "+ Memo.STATUS_CODE +" = 'ACTIVE' OR "+ Memo.STATUS_CODE + " = 'OVERDUE' OR " + Memo.STATUS_CODE +" IS NULL ORDER BY "+ Memo.DEADLINE_CODE+" DESC";
        }
        else if(where.equalsIgnoreCase("OVERDUE")){
            where=" WHERE "+ Memo.STATUS_CODE +" = 'OVERDUE' ORDER BY "+ Memo.DEADLINE_CODE+" DESC";
        }
        else if(where.equalsIgnoreCase("ACTIVE")){
            where=" WHERE "+ Memo.STATUS_CODE +" = 'ACTIVE' ORDER BY "+ Memo.DEADLINE_CODE+" DESC";
        }
        else if(where.equalsIgnoreCase("Complete")){
            where=" WHERE "+ Memo.STATUS_CODE +" = 'COMPLETE' ORDER BY "+ Memo.DEADLINE_CODE+" DESC";
        }
        else if(where.equalsIgnoreCase("All")){
            where=" ORDER BY "+ Memo.DEADLINE_CODE+" DESC";
        }
        //
        // 1. build the query
        String query = "SELECT * FROM " + TABLE_MEMO + where;
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Memo item = null;
        if (cursor.moveToFirst()) {
            do {
                item = new Memo();
                item.setId(Integer.parseInt(cursor.getString(0)));
                item.setTitle(cursor.getString(1));
                item.setCategory(cursor.getString(2));
                item.setDeadline(cursor.getString(3));
                item.setNote(cursor.getString(4));
                item.setNotificationIntervals(cursor.getString(5));
                item.setNotificationTime(cursor.getString(6));
                item.setPriorityLevel(cursor.getString(7));
                item.setStatus(cursor.getString(8));

                // Add book to books
                memoList.add(item);
            } while (cursor.moveToNext());
        }
        Log.d("MySqLiteHelper:","getMemoWhere-END");
        // return books
        return memoList;
    }

    public int updateMemo(Memo item) {
        Log.d("MySqLiteHelper:","udpateMemo-START");
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(Memo.TITLE_CODE, item.getTitle()); // get title
        values.put(Memo.DEADLINE_CODE, item.getDeadline()); // get title
        values.put(Memo.NOTE_CODE, item.getNote()); // get title
        values.put(Memo.NOTIFICATIONINTERVALS_CODE, item.getNotificationIntervals()); // get title
        values.put(Memo.NOTIFICATIONTIME_CODE, item.getNotificationTime()); // get title
        values.put(Memo.PRIORITYLEVEL_CODE, item.getPriorityLevel()); // get title
        values.put(Memo.CATEGORY_CODE, item.getCategory()); // get title
        values.put(Memo.STATUS_CODE, item.getStatus()); // get title

        // 3. updating row
        int i = db.update(TABLE_MEMO, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(item.getId()) }); //selection args

        // 4. close
        db.close();
        Log.d("MySqLiteHelper:","udpateMemo-END");
        return i;

    }

    public void deleteMemo(Memo item) {
        Log.d("MySqLiteHelper:","deleteMemo-START");

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_MEMO, //table name
                KEY_ID+" = ?",  // selections
                new String[] { String.valueOf(item.getId()) }); //selections args

        // 3. close
        db.close();
        Log.d("MySqLiteHelper:","deleteMemo-END");
        //log
    }

    public void useTemporaryTable(){
        Log.d("MySqLiteHelper:","useTemporaryTable-START");
        //bullshit
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS TemporaryMemos");
        // SQL statement to create book table
        String CREATE_MEMO_TABLE = "CREATE TABLE TemporaryMemos ( " +
                Memo.ID_CODE+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Memo.TITLE_CODE +" TEXT, "+
                Memo.CATEGORY_CODE+" TEXT, "+
                Memo.DEADLINE_CODE+" TEXT, "+
                Memo.NOTE_CODE+" TEXT, "+
                Memo.NOTIFICATIONINTERVALS_CODE+" TEXT, "+
                Memo.NOTIFICATIONTIME_CODE+" TEXT, "+
                Memo.PRIORITYLEVEL_CODE+" TEXT, "+
                Memo.STATUS_CODE+" TEXT"+")";
        // create books table
        db.execSQL(CREATE_MEMO_TABLE);
        db.close();
        Log.d("MySqLiteHelper:","useTemporaryTable-START");
    }
    public void addTempMemo(Memo item){
        Log.d("MySqLiteHelper:","addTempMemo-START");
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(Memo.TITLE_CODE, item.getTitle()); // get title
        values.put(Memo.DEADLINE_CODE, item.getDeadline()); // get title
        values.put(Memo.NOTE_CODE, item.getNote()); // get title
        values.put(Memo.NOTIFICATIONINTERVALS_CODE, item.getNotificationIntervals()); // get title
        values.put(Memo.NOTIFICATIONTIME_CODE, item.getNotificationTime()); // get title
        values.put(Memo.PRIORITYLEVEL_CODE, item.getPriorityLevel()); // get title
        values.put(Memo.CATEGORY_CODE, item.getCategory()); // get title
        values.put(Memo.STATUS_CODE, item.getStatus()); // get title
        //
        // 3. insert
        db.insert("TemporaryMemos", // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
        Log.d("MySqLiteHelper:","addTempMemo-END");
    }
    public ArrayList<Memo> getTempMemoBy(String Code){
        Log.d("MySqLiteHelper:","getTempMemoBy-START");
        ArrayList<Memo> memoList=new ArrayList<>();
        // 1. build the query
        String query = "SELECT  * FROM " + "TemporaryMemos";
        if(Code.equals(Memo.STATUS_CODE)){
            query = "SELECT  * FROM " + "TemporaryMemos" + " ORDER BY " + Memo.STATUS_CODE;
        }
        else if(Code.equals(Memo.PRIORITYLEVEL_CODE)){
            query = "SELECT  * FROM " + "TemporaryMemos" + " ORDER BY " + Memo.PRIORITYLEVEL_CODE+" DESC";
        }
        else if(Code.equals(Memo.DEADLINE_CODE)){
            query = "SELECT  * FROM " + "TemporaryMemos" + " ORDER BY " + Memo.DEADLINE_CODE +" DESC";
        }
        else if(Code.equals(Memo.TITLE_CODE)){
            query = "SELECT  * FROM " + "TemporaryMemos" + " ORDER BY " + Memo.TITLE_CODE;
        }


        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Memo item = null;
        if (cursor.moveToFirst()) {
            do {
                item = new Memo();
                item.setId(Integer.parseInt(cursor.getString(0)));
                item.setTitle(cursor.getString(1));
                item.setCategory(cursor.getString(2));
                item.setDeadline(cursor.getString(3));
                item.setNote(cursor.getString(4));
                item.setNotificationIntervals(cursor.getString(5));
                item.setNotificationTime(cursor.getString(6));
                item.setPriorityLevel(cursor.getString(7));
                item.setStatus(cursor.getString(8));

                // Add book to books
                memoList.add(item);
            } while (cursor.moveToNext());
        }


        // return books
        Log.d("MySqLiteHelper:","getTempMemoBy-END");
        return memoList;
    }
    public void updateData(){
        Log.d("MySqLiteHelper:","updateData-START");
        SQLiteDatabase db = this.getWritableDatabase();
        long currentdate = System.currentTimeMillis();
        String dateString = sdf.format(currentdate);
        String timeString = tf.format(currentdate);
        //UPDATE `acadsosd`.`studentathleteprofile` SET `studentDateOfBirth`='1996-10-18' WHERE `studentIDNumber`='11327219';
        String[] args={};
        Log.d("MySqLiteHelper:","updateData-WHERE "+Memo.DEADLINE_CODE +" > "+dateString+" AND " +
                //Memo.NOTIFICATIONTIME_CODE+" > "+ timeString+" AND "+
                Memo.STATUS_CODE + " NOT 'OVERDUE' AND " +
                Memo.STATUS_CODE + " NOT 'COMPLETE'"+
                "");
        ContentValues values = new ContentValues();
        values.put(Memo.STATUS_CODE, "OVERDUE"); // get title
        db.update(TABLE_MEMO, values, Memo.DEADLINE_CODE +" < '"+dateString+"' AND " +
                //Memo.NOTIFICATIONTIME_CODE+" > "+ timeString+" AND "+
                Memo.STATUS_CODE + " IS NOT 'OVERDUE' AND " +
                Memo.STATUS_CODE + " IS NOT 'COMPLETE'"+
                "", args);
        db.close();
        Log.d("MySqLiteHelper:","updateData-END");
    }

    public int getOngoingCount(){
        Log.d("MySqLiteHelper:","getOngoingCount-START");
        int x=0;
        String query="SELECT COUNT(*) FROM " + TABLE_MEMO +" WHERE "+ Memo.STATUS_CODE +" = 'ACTIVE' OR "+ Memo.STATUS_CODE + " = 'OVERDUE' OR " + Memo.STATUS_CODE +" IS NULL ORDER BY "+ Memo.DEADLINE_CODE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        x=Integer.parseInt(cursor.getString(0));
        db.close();
        cursor.close();
        Log.d("MySqLiteHelper:","getOngoingCount-END");
        return x;
    }

    public int getOverdueCount() {
        Log.d("MySqLiteHelper:","getOverdueCount-START");
        int overdueCount=0;
        String query="SELECT COUNT(*) FROM " + TABLE_MEMO +" WHERE "+ Memo.STATUS_CODE + " = 'OVERDUE'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        overdueCount=Integer.parseInt(cursor.getString(0));
        db.close();
        cursor.close();
        Log.d("MySqLiteHelper:","getOverdueCount-END");
        return overdueCount;
    }
    public int getActiveCount() {
        Log.d("MySqLiteHelper:","getActiveCount-START");
        int activeCount=0;
        String query="SELECT COUNT(*) FROM " + TABLE_MEMO +" WHERE "+ Memo.STATUS_CODE + " = 'ACTIVE'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        activeCount=Integer.parseInt(cursor.getString(0));
        db.close();
        cursor.close();
        Log.d("MySqLiteHelper:","getActiveCount-END");
        return activeCount;
    }

    public void markAsComplete(int itemId) {
            SQLiteDatabase db = this.getWritableDatabase();
            String[] args={};
            ContentValues values = new ContentValues();
            values.put(Memo.STATUS_CODE, "COMPLETE"); // get title
            db.update(TABLE_MEMO, values, Memo.ID_CODE +" IS '"+itemId+"'"+
                    "", args);
            db.close();

    }
}
