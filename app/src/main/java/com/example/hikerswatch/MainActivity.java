package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {
    LocationManager locationManager;
    LocationListener locationListener;
    Location lastKnownLocation;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            {
                lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                setTextInfos(lastKnownLocation);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener= new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                setTextInfos(location);
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
        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            setTextInfos(lastKnownLocation);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }

    }
    public void setTextInfos(Location loc)
    {
        TextView alt=(TextView)findViewById(R.id.alt);
        TextView lat=(TextView)findViewById(R.id.lat);
        TextView lng=(TextView)findViewById(R.id.lng);
        TextView acc=(TextView)findViewById(R.id.acc);
        TextView add=(TextView)findViewById(R.id.add);
        lat.setText(Double.toString(loc.getLatitude()));
        lng.setText(Double.toString(loc.getLongitude()));
        alt.setText(Double.toString(loc.getAltitude()));
        acc.setText(Double.toString(loc.getAccuracy()));
        Geocoder geocoder= new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addressList= geocoder.getFromLocation(loc.getLatitude(),loc.getLongitude(),1);
            if (addressList!=null && addressList.size()>0)
            {
                String address="";
                if (addressList.get(0).getFeatureName()!=null)
                {
                    address += addressList.get(0).getFeatureName() + ", ";
                }
                if (addressList.get(0).getLocality()!=null)
                {
                    address += addressList.get(0).getLocality() + ", ";
                }
                if (addressList.get(0).getThoroughfare()!=null)
                {
                    address += addressList.get(0).getLocality() + ", ";
                }
                if (addressList.get(0).getAdminArea()!=null)
                {
                    address += addressList.get(0).getAdminArea() + ", ";
                }
                if (addressList.get(0).getPostalCode()!=null)
                {
                    address += addressList.get(0).getPostalCode() + " ";
                }
                add.setText(address);
            }
            else{
                add.setText("Couldn't find address");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
