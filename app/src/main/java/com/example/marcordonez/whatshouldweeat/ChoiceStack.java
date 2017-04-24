package com.example.marcordonez.whatshouldweeat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import com.google.android.gms.common.ConnectionResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by marcordonez on 4/21/17.
 */
public class ChoiceStack {
    private int maxSize;
    private Choice[] stackArray;
    private int top;

    public ChoiceStack(int s) {
        stackArray = new Choice[s*10];
        top = -1;
    }
    public ChoiceStack(int s,double prevLat, double lat,double prevLg, double longi) {
        stackArray = new Choice[s*10];
        top = -1;
        refil(s,prevLat,lat,prevLg,longi);

    }
    public void refil(int s,double prevLat, double lat,double prevLg, double longi){
        //these are temporary variables whos values will come from the database
        int totalSelections=684;
        //number being multiplied by 100 is number of time the place was "choosen"
        int mex=(42*100)/totalSelections;
        int ital=mex+(46*100)/totalSelections;
        int pizza=ital+(46*100)/totalSelections;
        int chinese=pizza+(36*100)/totalSelections;
        int sushi=chinese+(20*100)/totalSelections;
        int bfast=sushi+(46*100)/totalSelections;
        int thai=bfast+(36*100)/totalSelections;
        int indian=thai+(36*100)/totalSelections;
        int burger=indian+(42*100)/totalSelections;
        int hdog=burger+(36*100)/totalSelections;
        int noodles=hdog+(36*100)/totalSelections;
        int bbq=noodles+(57*100)/totalSelections;;
        int seafood=bbq+(18*100)/totalSelections;;
        int steak=seafood+(54*100)/totalSelections;;
        int wings=steak+(36*100)/totalSelections;
        int vegan=wings+(10*100)/totalSelections;;
        int sandwitch=vegan+(36*100)/totalSelections;
        int cajun=sandwitch+(36*100)/totalSelections;
        int fish=cajun+(15*100)/totalSelections;;


        if (Math.abs(Math.abs(prevLat) - Math.abs(lat)) >= 0.225 && Math.abs(Math.abs(prevLg) - Math.abs(longi)) >= 0.225) {
            for (int i = 0; i < s; i++) {

                Random randomGenerator = new Random();
                int rndm = randomGenerator.nextInt(100);
                String ftype = "";
                if (rndm <=0 && rndm >=mex) {
                    ftype = "mexican";
                } else if (rndm <=mex && rndm >=ital) {
                    ftype = "italian";
                } else if (rndm <=ital && rndm >=pizza) {
                    ftype = "pizza";
                } else if (rndm <=pizza && rndm >=chinese) {
                    ftype = "chinese";
                } else if (rndm <=chinese && rndm >=sushi) {
                    ftype = "sushi";
                } else if (rndm <=sushi && rndm >=bfast) {
                    ftype = "breakfast";
                } else if (rndm <=bfast && rndm >=thai) {
                    ftype = "thai";
                } else if (rndm <=thai && rndm >=indian) {
                    ftype = "indian";
                } else if (rndm <=indian && rndm >=burger) {
                    ftype = "hamburger";
                } else if (rndm <=burger && rndm >=hdog) {
                    ftype = "hotdog";
                } else if (rndm <=hdog && rndm >=noodles) {
                    ftype = "noodles";
                } else if (rndm <=noodles && rndm >=bbq) {
                    ftype = "bbq";
                } else if (rndm <=bbq && rndm >=seafood) {
                    ftype = "seafood";
                } else if (rndm <=seafood && rndm >=steak) {
                    ftype = "steak";
                } else if (rndm <=steak && rndm >=wings) {
                    ftype = "wings";
                } else if (rndm <=wings && rndm >=vegan) {
                    ftype = "vegan";
                } else if (rndm <=vegan && rndm >=sandwitch) {
                    ftype = "sandwitch";
                } else if (rndm <=sandwitch && rndm >=cajun) {
                    ftype = "cajun";
                }
                else {
                    ftype = "fish";
                }
                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
                url = url.concat(String.valueOf(Double.toString(lat)));
                url = url.concat(",");
                url = url.concat(String.valueOf(Double.toString(longi)));
                url = url.concat("&rankby=distance&type=restaurant&key=AIzaSyBDf3cLEXwV77wvfihpvNbsnqDOixWD4Kc&opennow&keyword=");
                url = url.concat(ftype);
                //url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=33.58576431,-101.87939933&radius=500&type=restaurant&key=AIzaSyBDf3cLEXwV77wvfihpvNbsnqDOixWD4Kc";

                new Dtask().execute(url);
            }
        } else {

            //access data from the database

        }
        //Push a random list from database
    }
    public void push(Choice j) {
        stackArray[++top] = j;
    }
    public Choice pop(int s,double prevLat, double lat,double prevLg, double longi) {
        if (top<=1){
            //temporary variable till database is linked in
            boolean dataIsEmpty=true;
            if(dataIsEmpty){
                refil(s,prevLat,lat,prevLg,longi);
            }else{
                //refill with entrys in database
            }

        }
        return stackArray[top--];
    }
    public Choice peek() {
        return stackArray[top];
    }
    public boolean isEmpty() {
        return (top == -1);
    }
    public boolean isFull() {
        return (top == maxSize - 1);
    }

    public class Dtask extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... urls) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
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
                int ran=randomGenerator.nextInt(n);
                final JSONObject choice = place.getJSONObject(ran);
                for(int i=1; i<n; i++){
                    if(i != ran){
                        //insert into database

                    }
                }


                    Choice tmp= new Choice();


                    tmp.name=choice.getString("name");
                    tmp.adress=choice.getString("vicinity");
                    tmp.lat=choice.getJSONObject("geometry").getJSONObject("location").getString("lat");
                    tmp.lng=choice.getJSONObject("geometry").getJSONObject("location").getString("lng");
                    tmp.rateing=choice.getString("rating");
                    String im=choice.getJSONArray("photos").getJSONObject(randomGenerator.nextInt(choice.getJSONArray("photos").length())).getString("photo_reference");
                    tmp.imgurl="https://maps.googleapis.com/maps/api/place/photo?maxheight=250&photoreference=" +
                            im +
                            "&key=AIzaSyBDf3cLEXwV77wvfihpvNbsnqDOixWD4Kc";
                    //tmp.ftype = url.substring(url.lastIndexOf("=")+1);

                        push(tmp);




            } catch (JSONException e) {
                e.printStackTrace();
            }

            //weblist.setText(top.name);


        }
    }


}