package com.example.marcordonez.whatshouldweeat;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
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

    public void onClickFood(View view) {


        String randomWord = "";
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(33);
        if (randomInt == 1) {
            randomWord = "Wendy's";
        }
        if (randomInt == 2) {
            randomWord = "Five Guys";
        }
        if (randomInt == 3) {
            randomWord = "Hiyashi";
        }
        if (randomInt == 4) {
            randomWord = "Bobs Burgers";
        }
        if (randomInt == 5) {
            randomWord = "McAlisters";
        }
        if (randomInt == 6) {
            randomWord = "Mojo Bowl";
        }
        if (randomInt == 7) {
            randomWord = "Indian Place";
        }
        if (randomInt == 8) {
            randomWord = "Rudy's";
        }
        if (randomInt == 9) {
            randomWord = "Long John Silver's";
        }
        if (randomInt == 10) {
            randomWord = "B-Dubbs";
        }
        if (randomInt == 11) {
            randomWord = "Torchies";
        }
        if (randomInt == 12) {
            randomWord = "Fuzzie's";
        }
        if (randomInt == 13) {
            randomWord = "Picantes";
        }
        if (randomInt == 14) {
            randomWord = "Lubbock Pancake House";
        }
        if (randomInt == 15) {
            randomWord = "Jazz";
        }
        if (randomInt == 16) {
            randomWord = "Thai Pepper";
        }
        if (randomInt == 17) {
            randomWord = "I Love Pho";
        }
        if (randomInt == 18) {
            randomWord = "Denny's";
        }
        if (randomInt == 19) {
            randomWord = "Golden Chick";
        }
        if (randomInt == 20) {
            randomWord = "Freebird's";
        }
        if (randomInt == 21) {
            randomWord = "Canes";
        }
        if (randomInt == 22) {
            randomWord = "One Guy's";
        }
        if (randomInt == 23) {
            randomWord = "Steak and Shake";
        }
        if (randomInt == 24) {
            randomWord = "What-a-Burger";
        }
        if (randomInt == 25) {
            randomWord = "Chick fil a";
        }
        if (randomInt == 26) {
            randomWord = "Rosa's";
        }
        if (randomInt == 27) {
            randomWord = "Panda Express";
        }
        if (randomInt == 28) {
            randomWord = "Domino's";
        }
        if (randomInt == 29) {
            randomWord = "Spanky's";
        }
        if (randomInt == 30) {
            randomWord = "Weinerschnitzel";
        }
        if (randomInt == 31) {
            randomWord = "Hub City Grill";
        }
        if (randomInt == 32) {
            randomWord = "Heff's Burgers";
        }
        if (randomInt == 0) {
            randomWord = "Cotton Patch";
        }


        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        String longi =Double.toString(longitude);
        String lat= Double.toString(latitude);

String url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+longi+"&radius=500&type=restaurant&key=AIzaSyBDf3cLEXwV77wvfihpvNbsnqDOixWD4Kc";
        new Dtask().execute(url);
         whatYouWant.setText("You want to go to " + randomWord+ " "+url);

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

            weblist.setText(result);


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
