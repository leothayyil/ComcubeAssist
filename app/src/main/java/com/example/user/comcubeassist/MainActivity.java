package com.example.user.comcubeassist;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.ColorInt;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.comcubeassist.retrofit.RetrofitHelper;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LocationListener {

    EditText feedback,shopName,phoneEdt;
    String shopStr,feedbackStr,user_ids,phoneS;
    String shopf="LOG OUT";
    String phonee="000";
    String feedbf="finished";
    String lat;
    String longi;

    LocationManager locationManager;
    String provider;
    CardView update,finish;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        feedback=(EditText)findViewById(R.id.EdtFeedback);
        feedback.setMaxLines(5);
        feedback.setSelected(true);

        preferences = getApplicationContext().getSharedPreferences("user_id_shared", MODE_PRIVATE);
        editor = preferences.edit();

         user_ids=preferences.getString("user_id_preff", "0");

        if (user_ids =="0"){
            Intent logIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(logIntent);

        }

        shopName=findViewById(R.id.EdtShopNmae);
        phoneEdt=findViewById(R.id.EdtPhone);
        update=findViewById(R.id.cardUpdate);
        finish=findViewById(R.id.cardFinish);

        Toasty.Config.getInstance()
                .setErrorColor(ContextCompat.getColor(getApplicationContext(),R.color.colour3))
    .setInfoColor(ContextCompat.getColor(getApplicationContext(),R.color.colour1))
    .setSuccessColor(ContextCompat.getColor(getApplicationContext(),R.color.colour2))
    .setWarningColor(ContextCompat.getColor(getApplicationContext(),R.color.colour4))
    .setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colour5))
    .tintIcon(true)
    .apply();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                shopStr=shopName.getText().toString();
                feedbackStr=feedback.getText().toString();
                phoneS=phoneEdt.getText().toString();

                if (shopName.getText().toString().isEmpty()){
                    Toasty.warning(MainActivity.this, "Please enter the shop name", Toast.LENGTH_SHORT, true).show();
                }else if (phoneEdt.getText().toString().isEmpty()){
                    Toasty.warning(MainActivity.this, "Field phone number is empty", Toast.LENGTH_SHORT, true).show();
                }else if (feedback.getText().toString().isEmpty()){
                    Toasty.warning(MainActivity.this, "What about feedback?", Toast.LENGTH_SHORT, true).show();

                }else if (user_ids=="") {
                    Toasty.warning(MainActivity.this, "You need to login First", Toast.LENGTH_SHORT, true).show();
                    finish();
                }
                else   updateFb(shopStr,phoneS,user_ids,lat,longi,feedbackStr);
            }
        });

            finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    updateFb(shopf,phonee,user_ids,lat,longi,feedbf);
                    editor.remove("user_id_preff");
                    editor.clear();
                    Intent logIntent=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(logIntent);
                    finish();
                }
            });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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
                Toasty.error(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();
        }else{
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
    }
    private void updateFb(final String shop, final String phone,final String user_ids,final String lat, String lon,final String feedb) {

        new RetrofitHelper(MainActivity.this).getApIs().feedback(shop,phone,user_ids,lat,lon,feedb)
                .enqueue(new Callback<JsonElement>()
                {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response.body().toString());
                            String status=jsonObject.getString("status");
                            String date_time=jsonObject.getString("dt_time");
                            if (status=="Failed"){
                                Toasty.error(MainActivity.this, "Updating Failed", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                shopName.setText("");
                                feedback.setText("");
                                phoneEdt.setText("");
                                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                Toasty.success(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
