package layout;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.example.marcordonez.whatshouldweeat.Choice;
import com.example.marcordonez.whatshouldweeat.ChoiceStack;
import com.example.marcordonez.whatshouldweeat.MainActivity;
import com.example.marcordonez.whatshouldweeat.R;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodDisp extends Fragment {


    public FoodDisp() {
        // Required empty public constructor
    }

    Button food;
    Button mapit;
    TextView whatYouWant;
    TextView info;
    TextView weblist;
    WebView webView;
    ChoiceStack picker;


    double longi;
    double lat;
    String maplat;
    String maplon;
    double prevLg=-0.1;
    double prevLat=-0.1;
    int countdown;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
View view = inflater.inflate(R.layout.fragment_food_disp, container, false);

        food = (Button) view.findViewById(R.id.Food);
        mapit = (Button) view.findViewById(R.id.Mapit);
        whatYouWant = (TextView) view.findViewById(R.id.WhatYouWant);
        weblist = (TextView) view.findViewById(R.id.weblist);
        info = (TextView) view.findViewById(R.id.info);
        webView=(WebView) view.findViewById(R.id.webView);
        //MainActivity.GPSTracker gps = new MainActivity.GPSTracker(this);
       // picker.refil(5,prevLat,lat,prevLg,longi);

food.setOnClickListener(
        new View.OnClickListener(){
            public void onClick(View v){
                onClickFood(v);
            }
        }
);
        mapit.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        onClickMapit(v);
                    }
                }
        );

        return view;
    }



    public void onClickFood(View view) {
        if(picker.isEmpty()){
            //ProgressDialog dialog=new ProgressDialog(this);
         //   dialog.setMessage("Loading");
           // dialog.setCancelable(true);
            //dialog.setInverseBackgroundForced(false);
            //dialog.show();

            picker.refil(5,prevLat,lat,prevLg,longi);
            // while (picker.isEmpty()){

            // }

           // dialog.hide();
        }
        Choice next = picker.pop(5,prevLat,lat,prevLg,longi);

        String pick="\nRating: ";
        pick=pick.concat(next.getRating());
        pick=pick.concat("\nLocation: ");
        pick=pick.concat(next.getAddress());

        whatYouWant.setText(next.getName());
        info.setText(pick);
        mapit.setVisibility(View.VISIBLE);
        maplat= next.getLat();
        maplon= next.getLng();
        webView.setVisibility(View.VISIBLE);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(next.getImgurl());
        weblist.setText(next.getFtype().getDisplayName());



    }

    public void onClickMapit(View view) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+maplat+","+maplon);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);

    }
    public void passStack(ChoiceStack x,double pl, double l,double pl2, double l2) {
        picker=x;
        prevLat=pl;
        lat=l;
        prevLg=pl2;
        longi=l2;
    }

}
