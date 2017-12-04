package com.example.user.comcubeassist;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    TextView locationTv;
    EditText usename, password;
    String currentDateTimeString;
          String   Suser;
          String Spasswor;

    String currentLatitude;
    String currentLongitude;
    Bundle bundle;
    ProgressDialog progress;

public  static List<String>logindet=new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        login = (Button) findViewById(R.id.btn_login);
        locationTv = (TextView) findViewById(R.id.noLocationTV);
        usename = (EditText) findViewById(R.id.login_input_email);
        password = (EditText) findViewById(R.id.login_input_password);

        progress = new ProgressDialog(this);
        bundle=new Bundle();

        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {


            }
        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress.setTitle("Loading");
                progress.setMessage("Wait while loading...");
                progress.setCancelable(false);

                 Suser=usename.getText().toString();
                 Spasswor=password.getText().toString();

                Toast.makeText(LoginActivity.this, "You are logged in ", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);


                if (currentLatitude==null
                         ){
                     Toast.makeText(LoginActivity.this, "Unable to fetch location", Toast.LENGTH_SHORT).show();
                 }else {
                     loginWeb(Suser,Spasswor,currentLatitude,currentLongitude);
                     progress.show();
                 }


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

      if (suser.equals("")){
            Toast.makeText(this, "Enter Your Name", Toast.LENGTH_SHORT).show();
        }
        else if (password.equals("")){
            Toast.makeText(this, "Enter Your Password", Toast.LENGTH_SHORT).show();
        }

       else {

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

                        bundle.putString("user_id",userId);



                        progress.dismiss();


                         if (userId.isEmpty()){

                             Toast.makeText(LoginActivity.this, "failed", Toast.LENGTH_SHORT).show();
                         }else {

                             Toast.makeText(LoginActivity.this, "You are logged in ", Toast.LENGTH_SHORT).show();
                             Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                             intent.putExtras(bundle);
                             startActivity(intent);

                         }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Please Check Login Details", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {

                }
            });
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

