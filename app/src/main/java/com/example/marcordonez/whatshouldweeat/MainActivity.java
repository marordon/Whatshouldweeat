package com.example.marcordonez.whatshouldweeat;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;
import android.location.LocationManager;
import android.location.Location;
import android.content.Context;
import android.os.IBinder;
import android.app.Service;
import android.util.Log;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import android.net.Uri;
import android.webkit.WebViewClient;

import layout.FoodDisp;

//extends FragmentActivity

public class MainActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    Button food;
    Button mapit;
    TextView whatYouWant;
    TextView info;
    TextView weblist;
    WebView webView;
    ImageView imview;
    ChoiceStack picker;

    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 1;
    double longi;
    double lat;
    String maplat;
    String maplon;
    double prevLg=-0.1;
    double prevLat=-0.1;
    int countdown;
    //This is used to get the location
    public class GPSTracker extends Service implements LocationListener {

        private final Context mContext;

        // flag for GPS status
        boolean isGPSEnabled = false;

        // flag for network status
        boolean isNetworkEnabled = false;

        // flag for GPS status
        boolean canGetLocation = false;

        Location location; // location
        double latitude; // latitude
        double longitude; // longitude

        // The minimum distance to change Updates in meters
        private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

        // The minimum time between updates in milliseconds
        private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 5; // 5 minute

        // Declaring a Location Manager
        protected LocationManager locationManager;

        public GPSTracker(Context context) {
            this.mContext = context;
            getLocation();
        }

        public Location getLocation() {
            try {
                locationManager = (LocationManager) mContext
                        .getSystemService(LOCATION_SERVICE);

                // getting GPS status
                isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled) {
                    // no network provider is enabled
                } else {
                    this.canGetLocation = true;
                    // First get location from Network Provider
                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("Network", "Network");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                    // if GPS Enabled get lat/long using GPS Services
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return location;
        }

        /**
         * Stop using GPS listener
         * Calling this function will stop using GPS in your app
         * */
        public void stopUsingGPS(){
            if(locationManager != null){
                locationManager.removeUpdates(GPSTracker.this);
            }
        }

        /**
         * Function to get latitude
         * */
        public double getLatitude(){
            if(location != null){
                latitude = location.getLatitude();
            }

            // return latitude
            return latitude;
        }

        /**
         * Function to get longitude
         * */
        public double getLongitude(){
            if(location != null){
                longitude = location.getLongitude();
            }

            // return longitude
            return longitude;
        }

        /**
         * Function to check GPS/wifi enabled
         * @return boolean
         * */
        public boolean canGetLocation() {
            return this.canGetLocation;
        }



        @Override
        public void onLocationChanged(Location location) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public IBinder onBind(Intent arg0) {
            return null;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imview = (ImageView) findViewById(R.id.imageView);

        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());

        Choice test1 = new Choice("Test1", "100.0", "100.0", "5.0", "Test Address", "http://www.test.com", DatabaseHelper.FType.MEXICAN);
        Choice test2 = new Choice("Test2", "100.0", "100.0", "4.0", "Test Address", "http://www.test.com", DatabaseHelper.FType.SUSHI);
        Choice test3 = new Choice("Test3", "100.0", "100.0", "3.0", "Test Address", "http://www.test.com", DatabaseHelper.FType.CHINESE);
        Choice test4 = new Choice("Test4", "100.0", "100.0", "2.0", "Test Address", "http://www.test.com", DatabaseHelper.FType.SANDWICH);
        Choice test5 = new Choice("Test5", "100.0", "100.0", "1.0", "Test Address", "http://www.test.com", DatabaseHelper.FType.STEAK);



        setContentView(R.layout.activity_main);
        MainActivity.GPSTracker gps = new MainActivity.GPSTracker(this);
        FoodDisp fooddisp = (FoodDisp) getSupportFragmentManager().findFragmentById(R.id.FoodDisp_fragment);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


        //get prevLat and PrevLg from database

        if(gps.canGetLocation()) {
            lat = gps.getLatitude(); // returns latitude
            longi = gps.getLongitude(); // returns longitude
            if (picker==null) {
                picker = new ChoiceStack(getApplicationContext(), 5, prevLat, lat, prevLg, longi);
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //food.setEnabled(true);
            gps.stopUsingGPS();
        }
        else {
            //whatYouWant.setText("Location Unavailable");
           // food.setEnabled(false);
            while (!gps.canGetLocation()){

            }
           // whatYouWant.setText("Location Detected");

            lat = gps.getLatitude(); // returns latitude
            longi = gps.getLongitude(); // returns longitude

            picker =new ChoiceStack(getApplicationContext(), 5,prevLat,lat,prevLg,longi);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

           // food.setEnabled(true);
            gps.stopUsingGPS();
        }

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .enableAutoManage(this, 0, this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.



            //whatYouWant.setText( "nope");

        }

        fooddisp.passStack(picker,prevLat,lat,prevLg,longi);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();

        GPSTracker gps = new GPSTracker(this);
        if(gps.canGetLocation()) {
            lat = gps.getLatitude(); // returns latitude
            longi = gps.getLongitude(); // returns longitude

            picker =new ChoiceStack(getApplicationContext(), 5,prevLat,lat,prevLg,longi);


            gps.stopUsingGPS();
        }

    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }
    //selects category





//used to process the results from api
@Override
public void onConnected(@Nullable Bundle bundle) {

}

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
