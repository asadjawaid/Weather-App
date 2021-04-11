/*
 * Author: Asad Jawaid
 * Description: Simple Weather application that fetches data about a location based off of the name of the city, geographical coordinates, zip codes, and cities in circle.
 *              The Data was used from openweathermap.org.
 * */
package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {
    private Button city;
    private Button geoCoor;
    private Button zip;
    private Button cityCircle;
    public static final String EXTRA_MESSAGE = "com.example.WeatherApp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide(); // hide action bar

        city = (Button) findViewById(R.id.search_city);
        geoCoor = (Button) findViewById(R.id.search_geo);
        zip = (Button) findViewById(R.id.search_zip);
        cityCircle = (Button) findViewById(R.id.search_circle);

        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStarted(v.getId());
            }
        });

        geoCoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStarted(v.getId());
            }
        });

        zip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStarted(v.getId());
            }
        });

        cityCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStarted(v.getId());
            }
        });
    }

    private void getStarted(int id) {
        if(id == R.id.search_city) {
            String option = "first";
            startIntent(option);
        }
        else if(id == R.id.search_geo) {
            String option = "second";
            startIntent(option);
        }
        else if(id == R.id.search_zip) {
            String option = "third";
            startIntent(option);
        }
        else if(id == R.id.search_circle) {
            String option = "forth";
            startIntent(option);
        }
    }

    private void startIntent(String option) {
        Intent intent = new Intent(MainActivity.this, WeatherSearch.class);
        intent.putExtra(EXTRA_MESSAGE, option);
        startActivity(intent);
    }
}