package com.example.marcordonez.whatshouldweeat;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.marcordonez.whatshouldweeat.DatabaseHelper.FType.*;

/**
 * Created by marcordonez on 4/21/17.
 */
public class ChoiceStack {
    private int maxSize;
    private Choice[] stackArray;
    private int top;
    private DatabaseHelper dbHelper;
    public ChoiceStack(Context c, int s) {
        stackArray = new Choice[s*10];
        top = -1;
        dbHelper = new DatabaseHelper(c);
    }
    public ChoiceStack(Context c, int s,double prevLat, double lat,double prevLg, double longi) {
        stackArray = new Choice[s*10];
        top = -1;
        dbHelper = new DatabaseHelper(c);
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
                DatabaseHelper.FType ftype;
                if (rndm >=0 && rndm <=mex) {
                    ftype = MEXICAN;
                } else if (rndm <=ital) {
                    ftype = ITALIAN;
                } else if (rndm <=pizza) {
                    ftype = PIZZA;
                } else if (rndm <=chinese) {
                    ftype = CHINESE;
                } else if (rndm <=sushi) {
                    ftype = SUSHI;
                } else if (rndm <=bfast) {
                    ftype = BREAKFAST;
                } else if (rndm <=thai) {
                    ftype = THAI;
                } else if (rndm <=indian) {
                    ftype = INDIAN;
                } else if (rndm <=burger) {
                    ftype = HAMBURGER;
                } else if (rndm <=hdog) {
                    ftype = HOTDOG;
                } else if (rndm <=noodles) {
                    ftype = NOODLES;
                } else if (rndm <=bbq) {
                    ftype = BBQ;
                } else if (rndm <=seafood) {
                    ftype = SEAFOOD;
                } else if (rndm <=steak) {
                    ftype = STEAK;
                } else if (rndm <=wings) {
                    ftype = WINGS;
                } else if (rndm <=vegan) {
                    ftype = VEGAN;
                } else if (rndm <=sandwitch) {
                    ftype = SANDWICH;
                } else if (rndm <=cajun) {
                    ftype = CAJUN;
                }
                else {
                    ftype = FISH;
                }
                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
                url = url.concat(String.valueOf(Double.toString(lat)));
                url = url.concat(",");
                url = url.concat(String.valueOf(Double.toString(longi)));
                url = url.concat("&rankby=distance&type=restaurant&key=AIzaSyBDf3cLEXwV77wvfihpvNbsnqDOixWD4Kc&opennow&keyword=");
                url = url.concat(ftype.getDisplayName());
                //url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=33.58576431,-101.87939933&radius=500&type=restaurant&key=AIzaSyBDf3cLEXwV77wvfihpvNbsnqDOixWD4Kc";
                //

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
        if (top<=3){
            //temporary variable till database is linked in
            boolean dataIsEmpty=true;
            if(dataIsEmpty){
                refil(s,prevLat,lat,prevLg,longi);
            }else{
                return dbHelper.popChoice();
            }
            Choice temp=new Choice();
        return temp;
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
                String temp = urls[0]+" "+buffer.toString();
                return temp;

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
            String[] parts = result.split(" ", 2);
            String ftype = parts[0];
            String urlRes = parts[1];
            ftype=ftype.substring(ftype.lastIndexOf("=")+1);
            try {
                obj = new JSONObject(urlRes);
                String test=obj.getString("status");
                if(test.equals("OVER_QUERY_LIMIT")){
                    Choice tmp = new Choice();
                    tmp.name = "OVER QUERY LIMIT";
                    push(tmp);
                }else {

                    final JSONArray place = obj.getJSONArray("results");
                    final int n = place.length();
                    Log.e("DB TESTING", "Value of n: " + n);
                    Random randomGenerator = new Random();
                    int ran = randomGenerator.nextInt(n<=0?1:n);
                    final JSONObject choice = place.getJSONObject(ran);
                    for (int i = 1; i < n; i++) {
                        if (i != ran && dbHelper.getNumElements() <= 20) {
                            final JSONObject Jobj = place.getJSONObject(i);
                            Choice c = new Choice();
                            c.setName(Jobj.getString("name"));
                            c.setAddress(Jobj.getString("vicinity"));
                            c.setLat(Jobj.getJSONObject("geometry").getJSONObject("location").getString("lat"));
                            c.setLng(Jobj.getJSONObject("geometry").getJSONObject("location").getString("lng"));
                            c.setRating(Jobj.getString("rating"));
                            String image = choice.getJSONArray("photos").getJSONObject(randomGenerator.nextInt(choice.getJSONArray("photos").length())).getString("photo_reference");
                            c.setImgurl("https://maps.googleapis.com/maps/api/place/photo?maxheight=250&photoreference=" +
                                    image +
                                    "&key=AIzaSyBDf3cLEXwV77wvfihpvNbsnqDOixWD4Kc");
                            dbHelper.addChoice(c);
                        }
                    }


                    Choice tmp = new Choice();


                    tmp.name = choice.getString("name");
                    tmp.address = choice.getString("vicinity");
                    tmp.lat = choice.getJSONObject("geometry").getJSONObject("location").getString("lat");
                    tmp.lng = choice.getJSONObject("geometry").getJSONObject("location").getString("lng");
                    tmp.rating = choice.getString("rating");
                    String im = choice.getJSONArray("photos").getJSONObject(randomGenerator.nextInt(choice.getJSONArray("photos").length())).getString("photo_reference");
                    tmp.imgurl = "https://maps.googleapis.com/maps/api/place/photo?maxheight=250&photoreference=" +
                            im +
                            "&key=AIzaSyBDf3cLEXwV77wvfihpvNbsnqDOixWD4Kc";

                    tmp.ftype = DatabaseHelper.FType.findType(ftype);
                    //tmp.ftype=part1;
                    push(tmp);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            //weblist.setText(top.name);


        }
    }


}