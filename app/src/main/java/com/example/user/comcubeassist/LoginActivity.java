package com.example.user.comcubeassist;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.comcubeassist.retrofit.RetrofitHelper;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    String provider;

    Button login;
    EditText usename, password;
    String currentDateTimeString;
          String   Suser;
          String Spasswor;

    String currentLatitude;
    String currentLongitude;

List<String>logindet=new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Paper.init(this);
        login = findViewById(R.id.btn_login);
        usename = findViewById(R.id.login_input_email);
        password = findViewById(R.id.login_input_password);

        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 Suser=usename.getText().toString();
                 Spasswor=password.getText().toString();

//                Toast.makeText(LoginActivity.this, currentLatitude+currentLatitude, Toast.LENGTH_SHORT).show();
                loginWeb(Suser,Spasswor,currentLatitude,currentLongitude);

            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Creating an empty criteria object
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        if (provider != null && !provider.equals("")) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(provider);

            locationManager.requestLocationUpdates(provider, 20000, 1, this);

            if(location!=null)
                onLocationChanged(location);
            else
                Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();

        }else{
//
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
             }


    }

    private void loginWeb(final String suser, String spasswor, String currentLatitude, String currentLongitude) {

        if (currentLatitude != "" && currentLongitude != ""&&suser !=""&&spasswor !="") {

            new RetrofitHelper(LoginActivity.this).getApIs().
                    login(suser, spasswor, currentLatitude, currentLongitude).enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                    String userId = "";
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String status = jsonObject.getString("status");
                        userId = jsonObject.getString("user_id");
                        String dateTime = jsonObject.getString("dt_time");

                       Paper.book(suser).write(userId,logindet);
                        Paper.book(suser).write(dateTime,logindet);

                        if (userId==""){

                            Toast.makeText(LoginActivity.this, "Please check login Details", Toast.LENGTH_SHORT).show();


                        }
                        else{
                            Toast.makeText(LoginActivity.this, "You are logged in as "+suser, Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {

                }
            });
        }else {
            Toast.makeText(this, "Please Check Login Details", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onLocationChanged(Location location) {

      currentLatitude= String.valueOf(location.getLatitude());
      currentLongitude= String.valueOf(location.getLongitude());

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}

