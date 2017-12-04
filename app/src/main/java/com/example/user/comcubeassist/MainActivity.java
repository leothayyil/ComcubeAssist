package com.example.user.comcubeassist;

import android.*;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
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

    EditText feedback,shopName;
    String shopStr,feedbackStr,userIds;
    ImageView check;
    String lat="50.7889";
    String longi="68.99";
    int user_id=2;
    LocationManager locationManager;
    String provider;
    public Bundle getBundle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        feedback=(EditText)findViewById(R.id.EdtFeedback);
        feedback.setMaxLines(5);
        feedback.setSelected(true);


        getBundle = this.getIntent().getExtras();
         userIds = getBundle.getString("user_id");
        

        shopName=(EditText)findViewById(R.id.EdtShopNmae);
        feedback=(EditText)findViewById(R.id.EdtFeedback);

        shopStr=shopName.getText().toString();
        feedbackStr=feedback.getText().toString();

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


//        updateFb(shopStr,feedbackStr);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);





    }

    private void updateFb(String shopStr, String feedbackStr) {
        new RetrofitHelper(MainActivity.this).getApIs().feedback(shopStr,user_id,lat,longi,feedbackStr)
                .enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                        try {
                            JSONObject jsonObject=new JSONObject(response.body().toString());
                            String status=jsonObject.getString("status");
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

        Toast.makeText(this, lat, Toast.LENGTH_SHORT).show();
        Log.i("loggg", lat);
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
