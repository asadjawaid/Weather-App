/*
* Author: Asad Jawaid
* Description: Simple Weather application that fetches data about a location based off of the name of the city, geographical coordinates, zip codes, and cities in circle.
*              The Data was used from openweathermap.org.
* */
package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class WeatherSearch extends AppCompatActivity {
    private TextView headerTitle;
    private EditText city;
    private EditText latitude;
    private EditText longitude;
    private EditText zipCode;
    private EditText countryCode;
    private EditText cnt;
    private Button submitButton;
    private TextView headerScrollView;
    private TextView mainContent;
    private int searchToStart = 0;
    private DecimalFormat df = new DecimalFormat("0.00");
    private final String URL = "https://api.openweathermap.org/data/2.5/weather?";
    private final String appId = "f04116adda8b034f4c98897fc90f486d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_search);
        getSupportActionBar().hide(); // hide action bar

        headerTitle = (TextView) findViewById(R.id.header_text);
        city = (EditText) findViewById(R.id.input_city);
        latitude = (EditText) findViewById(R.id.input_lat);
        longitude = (EditText) findViewById(R.id.input_long);
        zipCode = (EditText) findViewById(R.id.input_zip);
        countryCode = (EditText) findViewById(R.id.input_country_code);
        cnt = (EditText) findViewById(R.id.input_cnt);
        submitButton = (Button) findViewById(R.id.get_weather_report);
        headerScrollView = (TextView) findViewById(R.id.name_city);
        mainContent = (TextView) findViewById(R.id.main_content);

        getIntentData();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUp();
            }
        });
    }

    private void setUp() {
        // city
        if(searchToStart == 1) {
            String cityEntered = city.getText().toString().replace(" ", "%20"); // if the string contains spaces then replace it with %20 for easier search from api

            if(cityEntered.equals("")) {
                Toast.makeText(getApplicationContext(), "Input a city!", Toast.LENGTH_SHORT).show();
            }
            else {
                String finalUrl = URL + "q=" + cityEntered + "&appid=" + appId;
                //Toast.makeText(WeatherSearch.this, finalUrl, Toast.LENGTH_SHORT).show();
                getWeatherData(finalUrl);
            }
        }
        // coordinates
        else if(searchToStart == 2) {
            String textLatitude = latitude.getText().toString().trim();
            String textLongitude = longitude.getText().toString().trim();

            if((textLatitude.equals("") && textLongitude.equals("")) || (textLatitude.equals("") && !textLongitude.equals("")) || (!textLatitude.equals("") && textLongitude.equals(""))) {
                Toast.makeText(getApplicationContext(), "Must enter both lat and lon to continue!", Toast.LENGTH_LONG).show();
            }
            else {
                String finalUrl = URL + "lat=" + textLatitude + "&lon=" + textLongitude + "&appid=" + appId;
                getWeatherData(finalUrl);
            }
        }
        // zip code
        else if(searchToStart == 3) {
            String textZip = zipCode.getText().toString().trim();
            String textCC = countryCode.getText().toString().trim();

            if(textZip.equals("")) {
                Toast.makeText(getApplicationContext(), "Please enter the zip code!", Toast.LENGTH_SHORT).show();
            }
            else {
                String finalUrl = URL + "zip=" + textZip + "," + textCC + "&appid=" + appId;
                getWeatherData(finalUrl);
            }
        }
        // Cities in circle
        else if(searchToStart == 4) {
            String textLatitude = latitude.getText().toString().trim();
            String textLongitude = longitude.getText().toString().trim();
            String textCnt = cnt.getText().toString().trim();

            if((textLatitude.equals("") && textLongitude.equals("")) || (textLatitude.equals("") && !textLongitude.equals("")) || (!textLatitude.equals("") && textLongitude.equals(""))) {
                Toast.makeText(getApplicationContext(), "Must enter both lat and lon to continue!", Toast.LENGTH_SHORT).show();
            }
            else {
                String finalUrl = URL + "lat=" + textLatitude + "&lon=" + textLongitude + "&cnt=" + textCnt + "&appid=" + appId;
                getWeatherData(finalUrl);
            }
        }
    }

    private void getWeatherData(String finalUrl) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, finalUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                String output = "";

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String nameOfCity = jsonObject.getString("name");

                    // main object in jsonObject
                    JSONObject mainObject = jsonObject.getJSONObject("main");
                    double temp = mainObject.getDouble("temp") - 273.15;
                    double feels_like = mainObject.getDouble("feels_like") - 273.15;
                    double temp_min = mainObject.getDouble("temp_min") - 273.15;
                    double temp_max = mainObject.getDouble("temp_max") - 273.15;
                    float pressure = mainObject.getInt("pressure");
                    int humidity = mainObject.getInt("humidity");

                    double visibility = jsonObject.getDouble("visibility");

                    // wind object in jsonObject
                    JSONObject windObject = jsonObject.getJSONObject("wind");
                    String wind = windObject.getString("speed");
                    String deg = windObject.getString("deg");

                    JSONObject cloudsObject = jsonObject.getJSONObject("clouds");
                    String cloud = cloudsObject.getString("all");

                    String textToSet = "Weather report for " + nameOfCity;

                    output += "Temperature:\n" + df.format(temp) + " °C\n\n" + "Feels Like:\n" + df.format(feels_like) + " °C\n\n" + "Minimum Temperature:\n" + df.format(temp_min) + " °C\n\n"
                            + "Maximum Temperature:\n" + df.format(temp_max) + " °C\n\n" + "Pressure:\n" + pressure + " hPa\n\n" + "Humidity:\n" + humidity + "%\n\n"
                            + "Visibility:\n" + visibility + "\n\nWind Speed:\n" + wind + " m/s\n\n" + "Degree:\n" + deg + "\n\nClouds:\n" + cloud;

                    headerScrollView.setText(textToSet);
                    mainContent.setText(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WeatherSearch.this, "Error! Try Again!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }


    private void getIntentData() {
        Intent intent = getIntent();
        String optionSelected = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        if(optionSelected.equalsIgnoreCase("first")) {
            city.setVisibility(View.VISIBLE);
            latitude.setVisibility(View.INVISIBLE);
            longitude.setVisibility(View.INVISIBLE);
            zipCode.setVisibility(View.INVISIBLE);
            countryCode.setVisibility(View.INVISIBLE);
            cnt.setVisibility(View.INVISIBLE);
            submitButton.setTranslationY(-270);

            String title = "Search By City";
            headerTitle.setText(title);
            searchToStart = 1;
        }
        else if(optionSelected.equalsIgnoreCase("second")) {
            latitude.setVisibility(View.VISIBLE);
            longitude.setVisibility(View.VISIBLE);
            cnt.setVisibility(View.INVISIBLE);
            city.setVisibility(View.INVISIBLE);
            zipCode.setVisibility(View.INVISIBLE);
            countryCode.setVisibility(View.INVISIBLE);
            submitButton.setTranslationY(-140);

            String title = "Search By Coordinates";
            headerTitle.setText(title);
            searchToStart = 2;
        }
        else if(optionSelected.equalsIgnoreCase("third")) {
            zipCode.setVisibility(View.VISIBLE);
            countryCode.setVisibility(View.VISIBLE);
            latitude.setVisibility(View.INVISIBLE);
            longitude.setVisibility(View.INVISIBLE);
            cnt.setVisibility(View.INVISIBLE);
            city.setVisibility(View.INVISIBLE);
            submitButton.setTranslationY(-140);

            String title = "Search By Zip Code";
            headerTitle.setText(title);
            searchToStart = 3;
        }
        else if(optionSelected.equalsIgnoreCase("forth")) {
            latitude.setVisibility(View.VISIBLE);
            longitude.setVisibility(View.VISIBLE);
            cnt.setVisibility(View.VISIBLE);
            city.setVisibility(View.INVISIBLE);
            zipCode.setVisibility(View.INVISIBLE);
            countryCode.setVisibility(View.INVISIBLE);

            String title = "Search By Cities Circle";
            headerTitle.setText(title);
            searchToStart = 4;
        }
    }
}