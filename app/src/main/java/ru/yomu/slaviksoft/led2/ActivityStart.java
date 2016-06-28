package ru.yomu.slaviksoft.led2;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class ActivityStart extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_start);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
        .addTestDevice("5D2F4312E4D347F2F49F189107789A35")
        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
        .build();
        mAdView.loadAd(adRequest);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new initTask().execute();
            }
        }, 10000);

    }

    private void initApp() {

        DBCatalogs dbCatalogs = new DBCatalogs(this);
        SQLiteDatabase db = dbCatalogs.getWritableDatabase();
        if (dbCatalogs.getCount(db) == 0) dbCatalogs.initCatalogs(db);
        db.close();

    }


    private class initTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            initApp();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

}
