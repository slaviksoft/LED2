package ru.yomu.slaviksoft.led2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBLeads extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 8;

    static final public String DB_NAME = "LED2";
    static final public String TABLE_LEADS = "LEADS";
    static final public String LEADS_ID = "_id";
    static final public String LEADS_TITLE = "title";
    static final public String LEADS_DESC = "description";
    static final public String LEADS_STREET = "street";

    private void log(String text) {
        Log.d("DB", text);
    }

    public DBLeads(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_LEADS + " ("
                + LEADS_ID + " integer primary key autoincrement,"
                + LEADS_TITLE + " text,"
                + LEADS_DESC + " text,"
                + LEADS_STREET + " text"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEADS);

        onCreate(db);
    }

    /////////////////////////////////////////////////////////////////////////////////////

    public void deleteLead(LeadItem lead){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LEADS, LEADS_ID+"=?", new String[]{String.valueOf(lead.id)});
        db.close();

    }

    public long addLead(LeadItem lead) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LEADS_TITLE, lead.title);
        values.put(LEADS_DESC, lead.description);
        values.put(LEADS_STREET, lead.street);

        long id = db.insert(TABLE_LEADS, null, values);
        db.close();

        return id;
    }


    public long getLeadsCount() {
        String countQuery = "select * from " + TABLE_LEADS;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public ArrayList<LeadItem> getAllLeads() {

        ArrayList<LeadItem> leadsList = new ArrayList();
        String selectQuery = "select * from " + TABLE_LEADS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                LeadItem lead = new LeadItem();
                lead.id = cursor.getLong(0);
                lead.title = cursor.getString(1);
                lead.description = cursor.getString(2);
                lead.street = cursor.getString(3);
                leadsList.add(lead);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return leadsList;
    }



}