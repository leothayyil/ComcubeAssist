package com.example.user.comcubeassist;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.comcubeassist.retrofit.RetrofitHelper;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LocationListener {

    EditText feedback,shopName,phoneEdt;
    String shopStr,feedbackStr,userIds,phoneS;
    String lat;
    String longi;

    LocationManager locationManager;
    String provider;
    Button updateBtn;
    CardView update,finish;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        feedback=(EditText)findViewById(R.id.EdtFeedback);
        feedback.setMaxLines(5);
        feedback.setSelected(true);

         bundle = getIntent().getExtras();
        if (bundle != null)
        {
            userIds=" "+bundle.getString("user_id");

        }else {
            userIds="";

            Intent logIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(logIntent);
            finish();

        }


        shopName=(EditText)findViewById(R.id.EdtShopNmae);
        feedback=(EditText)findViewById(R.id.EdtFeedback);
        phoneEdt=(EditText)findViewById(R.id.EdtPhone);

//        updateBtn=(Button)findViewById(R.id.submitBtnId);

        update=(CardView)findViewById(R.id.cardUpdate);
        finish=(CardView)findViewById(R.id.cardFinish);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                shopStr=shopName.getText().toString();
                feedbackStr=feedback.getText().toString();
                phoneS=phoneEdt.getText().toString();

                if (shopName.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter the shop name", Toast.LENGTH_SHORT).show();

                }else if (feedback.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Field phone number is empty", Toast.LENGTH_SHORT).show();
                }else if (feedback.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "What about feedback?", Toast.LENGTH_SHORT).show();
                }else if (userIds=="") {
                    Toast.makeText(MainActivity.this, "You need to login First", Toast.LENGTH_SHORT).show();
//                    Intent logIntent = new Intent(MainActivity.this, LoginActivity.class);
//                    startActivity(logIntent);
                    finish();
                }
                else   updateFb(shopStr,phoneS,lat,longi,feedbackStr);


            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String shop="LOGOUT";
                String phonee="000";
                String feedb="finished";

                if (bundle!=null) {
                    bundle.remove("user_id");
                    bundle.clear();
                }
                updateFb(shop,phonee,lat,longi,feedb);
                Toast.makeText(MainActivity.this, "You are Logged Out", Toast.LENGTH_SHORT).show();
                Intent logIntent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(logIntent);
                finish();



            }
        });




        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Creating an empty criteria object
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        if (provider != null && !provider.equals("")) {

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1);
        }



    }

    private void updateFb(String shop, final String phone, String lat, String lon, String feedb) {
        new RetrofitHelper(MainActivity.this).getApIs().feedback(shopStr,phone,userIds,lat,longi,feedbackStr)
                .enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response.body().toString());
                            String status=jsonObject.getString("status");
                            String date_time=jsonObject.getString("dt_time");

                            
                            if (status=="Failed"){

                                Toast.makeText(MainActivity.this, "Updating Failed", Toast.LENGTH_SHORT).show();

                            }
                            else {
                                shopName.setText("");
                                feedback.setText("");
                                phoneEdt.setText("");


                                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {

                    }
                });
    }

    @Override
    public void onLocationChanged(Location location) {
        lat= String.valueOf(location.getLatitude());
        longi=String.valueOf(location.getLongitude());

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
