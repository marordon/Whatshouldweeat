package com.example.marcordonez.whatshouldweeat;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.Manifest;
import android.location.LocationManager;
import android.location.Location;
import android.content.Context;
import android.location.Criteria;
import android.os.IBinder;
import android.app.Service;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import android.util.Log;

import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.common.api.ResultCallback;

import android.text.TextUtils;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.AutocompletePrediction;
import java.util.List;
import java.util.ArrayList;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import java.util.concurrent.TimeUnit;
import com.google.android.gms.common.api.Status;
import java.util.Iterator;
import com.google.android.gms.location.places.PlaceFilter;
import android.text.Html;
import android.net.Uri;
import android.text.Spanned;
import android.content.res.Resources;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    Button food;
    TextView whatYouWant;
    TextView weblist;
    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 1;
    String url="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        food = (Button) findViewById(R.id.Food);
        whatYouWant = (TextView) findViewById(R.id.WhatYouWant);
        weblist = (TextView) findViewById(R.id.weblist);


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

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            //whatYouWant.setText( "nope");

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }
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
        private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

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
    public void onClickFood(View view) {
        Random randomGenerator = new Random();
        int rndm=randomGenerator.nextInt(17);
        String ftype = "";
        if (rndm==1) {
            ftype = "mexican";
        }
        else if (rndm==2) {
            ftype = "italian";
        }
        else if (rndm==3) {
            ftype = "pizza";
        }
        else if (rndm==4) {
            ftype = "chinese";
        }
        else if (rndm==5) {
            ftype = "sushi";
        }
        else if (rndm==6) {
            ftype = "breakfast";
        }
        else if (rndm==7) {
            ftype = "thai";
        }
        else if (rndm==8) {
            ftype = "indian";
        }
        else if (rndm==9) {
            ftype = "hamburger";
        }
        else if (rndm==10) {
            ftype = "hotdog";
        }
        else if (rndm==11) {
            ftype = "noodles";
        }
        else if (rndm==12) {
            ftype = "bbq";
        }
        else if (rndm==13) {
            ftype = "seafood";
        }
        else if (rndm==14) {
            ftype = "steak";
        }
        else if (rndm==15) {
            ftype = "wings";
        }
        else if (rndm==16) {
            ftype = "vegan";
        }
        else if (rndm==17) {
            ftype = "sandwitch";
        }
        else if (rndm==0) {
            ftype = "cajun";
        }

        weblist.setText(ftype);















        //LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

       // Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //double longitude = location.getLongitude();
        //double latitude = location.getLatitude();
        GPSTracker gps = new GPSTracker(this);
        if(gps.canGetLocation()){
            double latitude= gps.getLatitude(); // returns latitude
            double longitude = gps.getLongitude(); // returns longitude
            String longi =Double.toString(longitude);
            String lat= Double.toString(latitude);

            url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
            url=url.concat(String.valueOf(lat));
            url=url.concat(",");
            url=url.concat(String.valueOf(longi));
            url=url.concat("&rankby=distance&type=restaurant&key=AIzaSyBDf3cLEXwV77wvfihpvNbsnqDOixWD4Kc&opennow&keyword=");
            url=url.concat(ftype);
            //url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=33.58576431,-101.87939933&radius=500&type=restaurant&key=AIzaSyBDf3cLEXwV77wvfihpvNbsnqDOixWD4Kc";
            new Dtask().execute(url);
            //whatYouWant.setText(url);
        }
        else{
            whatYouWant.setText("Could not access location");
        }



        //guessCurrentPlace();

    }

    private void guessCurrentPlace() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            //whatYouWant.setText( "nope");
            return;
        }
        /*PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient, null);
        result.setResultCallback( new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult( PlaceLikelihoodBuffer likelyPlaces ) {
                whatYouWant.setText( "" );
                int c=likelyPlaces.getCount();
                PlaceLikelihood placeLikelihood = likelyPlaces.get( 0 );
                String content[] =  new String[c];

                ArrayList<String> filter = new ArrayList<>();
                filter.add(""+Place.TYPE_FOOD);

               PlaceFilter placeFilter = new PlaceFilter(true,filter);
                StringBuilder builder = new StringBuilder();
                for (String str : placeFilter.getPlaceIds () ) {
                    builder.append(str).append(" ");
                }

                String txt = builder.toString();


                whatYouWant.setText(txt  );

                for(int i=0; i<c; i++){
                    content[i]=""+placeLikelihood.getPlace().getName();
                    //whatYouWant.append( content[i]+""+(int) ( placeLikelihood.getLikelihood() * 100 )+"\n" );
                }

/*
                PlaceLikelihood placeLikelihood = likelyPlaces.get( 0 );
                String content = "";
                if( placeLikelihood != null && placeLikelihood.getPlace() != null && !TextUtils.isEmpty( placeLikelihood.getPlace().getName() ) )
                    content = "Most likely place: " + placeLikelihood.getPlace().getName() + "\n";
                if( placeLikelihood != null )
                    content += "Percent change of being there: " + (int) ( placeLikelihood.getLikelihood() * 100 ) + "%";
                whatYouWant.setText( content );
*/
        //    likelyPlaces.release();
        //}
        //});

        ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(PlaceBuffer places) {
                if (!places.getStatus().isSuccess()) {
                    // Request did not complete successfully
                    //Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                    places.release();
                    return;
                }
                // Get the Place object from the buffer.
                final Place place = places.get(0);

                // Format details of the place for display and show it in a TextView.
                whatYouWant.setText("wertyuio" + place.getName());

                // Display the third party attributions if set.
                /*final CharSequence thirdPartyAttribution = places.getAttributions();
                if (thirdPartyAttribution == null) {
                    mPlaceDetailsAttribution.setVisibility(View.GONE);
                } else {
                    mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
                    mPlaceDetailsAttribution.setText(Html.fromHtml(thirdPartyAttribution.toString()));
                }

                Log.i(TAG, "Place details received: " + place.getName());
                */

                places.release();


            }


        };
    }
    public class Dtask extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... urls) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                System.out.println("in");
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                //whatYouWant.setText(buffer.toString());
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;

        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            final JSONObject obj;
            try {
                obj = new JSONObject(result);
                final JSONArray place = obj.getJSONArray("results");
                final int n = place.length();
                Random randomGenerator = new Random();
                    final JSONObject choice = place.getJSONObject(randomGenerator.nextInt(n));
                    whatYouWant.setText(choice.getString("name"));
                    //System.out.println(choice.getString("opening_hours"));


            } catch (JSONException e) {
                e.printStackTrace();
            }

               // weblist.setText(result);


        }
    }
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
