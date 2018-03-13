package com.example.hp.memomanagerapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.security.Key;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
        Log.d("Create Database Code:", CREATE_MEMO_TABLE);
        // create books table
        db.execSQL(CREATE_MEMO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS memos");

        // create fresh books table
        this.onCreate(db);
    }

    public void addMemo(Memo item){
        //for logging
        Log.d("addMemo", item.toString());

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
    }
    public Memo getMemo(int id){

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
        Log.d("getBook("+id+")", item.toString());

        // 5. return book
        return item;
    }
    public ArrayList<Memo> getAllMemos() {
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

        Log.d("getAllBooks()", memoList.toString());

        // return books
        return memoList;
    }
    public int updateMemo(Memo item) {

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

        return i;

    }

    public void deleteMemo(Memo item) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_MEMO, //table name
                KEY_ID+" = ?",  // selections
                new String[] { String.valueOf(item.getId()) }); //selections args

        // 3. close
        db.close();

        //log
        Log.d("deleteBook", item.toString());
    }
}