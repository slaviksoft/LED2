package ru.yomu.slaviksoft.led2;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class LeadDetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_details);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle args = getIntent().getExtras();
        if (args != null){
            LeadDetailsFragment detailsFragment = LeadDetailsFragment.newInstance((LeadItem) args.getParcelable("lead"));
            getSupportFragmentManager().beginTransaction().replace(R.id.container, detailsFragment).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lead_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {return true;}

        if(id == android.R.id.home){finish();}

        if(id ==  R.id.action_del){deleteLead();}

        return super.onOptionsItemSelected(item);
    }

    private void deleteLead() {

        // TODO: Implement delete

        LeadDetailsFragment fragment = (LeadDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        DBLeads dbLeads = new DBLeads(this);
        dbLeads.deleteLead( fragment.getLead() );

        finish();

    }
}
