package csd.jt.capsmobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set Categories Listeners
        ImageButton cat1 = (ImageButton) findViewById(R.id.cat1);
        ImageButton cat2 = (ImageButton) findViewById(R.id.cat2);
        ImageButton cat3 = (ImageButton) findViewById(R.id.cat3);
        ImageButton cat4 = (ImageButton) findViewById(R.id.cat4);
        ImageButton cat5 = (ImageButton) findViewById(R.id.cat5);
        ImageButton cat6 = (ImageButton) findViewById(R.id.cat6);

        cat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);
                intent.putExtra("category", "Περίθαλψη");
                startActivity(intent);
            }
        });

        cat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);
                intent.putExtra("category", "Ζώα");
                startActivity(intent);
            }
        });

        cat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);
                intent.putExtra("category", "Κοινότητες");
                startActivity(intent);
            }
        });

        cat4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);
                intent.putExtra("category", "Εκπαίδευση");
                startActivity(intent);
            }
        });

        cat5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);
                intent.putExtra("category", "Περιβάλλον");
                startActivity(intent);
            }
        });

        cat6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);
                intent.putExtra("category", "Έκτακτη Ανάγκη");
                startActivity(intent);
            }
        });


    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
