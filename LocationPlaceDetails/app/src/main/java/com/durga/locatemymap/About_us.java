package com.durga.locatemymap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by admin on 09-04-2016.
 */
public class About_us extends AppCompatActivity{
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("About Us");
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;

            case R.id.near_by:
                startActivity(new Intent(About_us.this, MainActivity.class));
                break;
            case R.id.my_loc:
                startActivity(new Intent(About_us.this, My_Loc.class));
                break;
            case R.id.about:
                startActivity(new Intent(About_us.this, About_us.class));
                break;
            case R.id.share:

                break;
            case R.id.rate:

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
