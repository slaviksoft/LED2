package ru.yomu.slaviksoft.led2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Slavik on 09.04.2015.
 */
public class DBCatalogs extends SQLiteOpenHelper {

    Context mContext;

    private static final int DATABASE_VERSION = 9;

    static final public String DB_NAME = "LED2_CATALOGS";

    static final public String TABLE_STREETS = "STREETS";
    static final public String STREETS_ID    = "_id";
    static final public String STREETS_NAME  = "name";

    public DBCatalogs(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    private void log(String text) {
        Log.d("DB", text);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_STREETS + " ("
                + STREETS_ID + " integer primary key autoincrement,"
                + STREETS_NAME + " text"+");");

        log("start init streets "+ Calendar.getInstance().getTime());
        initCatalogs(db);
        log("end init streets "+ Calendar.getInstance().getTime());



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STREETS);

        onCreate(db);
    }


    public Cursor getStreetsCursor(CharSequence constraint) {

        SQLiteDatabase db = getWritableDatabase();

        String select = "SELECT * FROM "+TABLE_STREETS+" WHERE "+STREETS_NAME+" LIKE ? LIMIT 10";
        String[]  selectArgs = { constraint.toString().toUpperCase() + "%"};
        Cursor cursor = db.rawQuery(select, selectArgs);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.close();

        return cursor;
    }

    public static CharSequence getStreet(Cursor cursor) {
        int index = cursor.getColumnIndexOrThrow(STREETS_NAME);
        return cursor.getString(index);
    }

    public int getCount(SQLiteDatabase db){

        Cursor cursor = db.rawQuery("select count(*) from "+TABLE_STREETS, null);
        if (cursor.moveToFirst()){
            return cursor.getInt(0);
        }else return 0;
    }

    public void initCatalogs(SQLiteDatabase db){

        try {
            String tempDir = mContext.getFilesDir().getPath()+"/cities/";
            InputStream is = mContext.getResources().openRawResource(R.raw.cities);

            List<String> files = DBCatalogs.unzip(is, tempDir);

            loadData(db, tempDir);

            for(String item: files){
                File f = new File(item);
                f.delete();
            }
            File f = new File(tempDir);
            f.delete();

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

    }

    private void loadData(SQLiteDatabase db, String tempDir) {

        String filename = tempDir+"cities.csv";

        try {
            FileInputStream csvStream = new FileInputStream(filename);
            InputStreamReader csvStreamReader = new InputStreamReader(csvStream);
            BufferedReader csvReader = new BufferedReader(csvStreamReader);

            db.beginTransaction();

            db.delete(TABLE_STREETS, null, null);

            String line;
            try {

                line = csvReader.readLine(); //headers
                if (line == null) return;
                while( (line = csvReader.readLine()) != null ){
                    String[] cols = line.toUpperCase().split(";");

                    String queryRows = "insert into "+TABLE_STREETS;
                    ContentValues values = new ContentValues();
                    values.put(STREETS_NAME, cols[1]+" "+cols[2]+" "+cols[3]);
                    db.insert(TABLE_STREETS, null, values);

                }



                db.setTransactionSuccessful();

            } catch (IOException e) {
                e.printStackTrace();

            } finally {
                db.endTransaction();
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    private static List<String> unzip(InputStream inputStream, String location) throws IOException {

        List<String> files = new ArrayList<String>();

        try {
            File f = new File(location);
            if(!f.isDirectory()) {
                f.mkdirs();
            }
            ZipInputStream zin = new ZipInputStream(inputStream);
            try {
                ZipEntry ze = null;
                while ((ze = zin.getNextEntry()) != null) {
                    String path = location + ze.getName();

                    if (!ze.isDirectory()) {
                        files.add(path);
                        FileOutputStream fOut = new FileOutputStream(path, false);
                        try {
                            for (int c = zin.read(); c != -1; c = zin.read()) {
                                fOut.write(c);
                            }
                            zin.closeEntry();
                        }
                        finally {
                            fOut.close();
                        }
                    }
                }
            }
            finally {
                zin.close();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return files;
    }

}
