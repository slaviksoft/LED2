package ru.yomu.slaviksoft.led2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class LeadsActivity extends ActionBarActivity implements LeadsListFragment.LeadsListListener{

    private boolean doubleBackToExitPressedOnce;
    private boolean mDualView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leads);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDualView = findViewById(R.id.details) != null;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_leads, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {return true;}
        if(id == android.R.id.home){finish();}
        switch (id){
            case R.id.action_add:{addNewLead(); break;}
            case R.id.action_del:{deleteLead(); break;}
            case R.id.action_sync:{Toast.makeText(getApplicationContext(), "sync pressed", Toast.LENGTH_SHORT).show(); break;}
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }
        doubleBackToExitPressedOnce = true;
        Toast.makeText(getApplicationContext(), "press back to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

    private void deleteLead() {

        LeadsListFragment fragment = (LeadsListFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        fragment.deleteLead();
    }

    private void addNewLead() {
        Intent i = new Intent(getApplicationContext(), NewLeadActivity.class);
        startActivity(i);
    }

    @Override
    public void onItemClick(LeadItem item) {

        getSupportActionBar().setTitle(item.description);

        if (mDualView){
            LeadDetailsFragment detailsFragment = LeadDetailsFragment.newInstance(item);
            getSupportFragmentManager().beginTransaction().replace(R.id.details, detailsFragment).commit();
        }else{
            Intent i = new Intent(getApplicationContext(), LeadDetailsActivity.class);
            i.putExtra("lead", item);
            startActivity(i);
        }


    }
}
