package com.example.hw9.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hw9.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

public class VenueFragment extends Fragment {

    JSONObject Venue;
    private GoogleMap mMap;

    public VenueFragment(JSONObject Venue){
        this.Venue = Venue;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_venue, container, false);
        String VenueName = Venue.optString("Name");
        String VenueAddress = Venue.optString("Address");
        String VenueCity = Venue.optString("City");
        String VenuePhoneNumber = Venue.optString("PhoneNumber");
        String OpenHours = Venue.optString("OpenHours");
        String GeneralRule = Venue.optString("GeneralRule");
        String ChildRule = Venue.optString("ChildRule");
        RequestQueue requestQueue = Volley.newRequestQueue(rootView.getContext());
        CardView VenueCard = rootView.findViewById(R.id.VenueCard);
        CardView DescriptionCard = rootView.findViewById(R.id.DescriptionCard);
        if(Venue.length() == 0){
            VenueCard.setVisibility(rootView.GONE);
            DescriptionCard.setVisibility(rootView.GONE);
        }

        if(VenueName.equals("") && VenueAddress.equals("") && VenueCity.equals("") && VenuePhoneNumber.equals("")){
            VenueCard.setVisibility(rootView.GONE);
        }

        if(OpenHours.equals("") && GeneralRule.equals("") && ChildRule.equals("")){
            DescriptionCard.setVisibility(rootView.GONE);
        }

        TextView VenueNameBox = rootView.findViewById(R.id.venuename_data);
        if(!VenueName.equals("")) {
            VenueNameBox.setText(VenueName);
            VenueNameBox.setSelected(true);
        }else{
            TextView VenueNameLabel = rootView.findViewById(R.id.venuename);
            VenueNameLabel.setVisibility(rootView.GONE);
            VenueNameBox.setVisibility(rootView.GONE);
        }

        TextView VenueAddressBox = rootView.findViewById(R.id.venueaddress_data);
        if(!VenueAddress.equals("")) {
            VenueAddressBox.setText(VenueAddress);
            VenueAddressBox.setSelected(true);
        }else{
            TextView VenueAddressLabel = rootView.findViewById(R.id.venueaddress);
            VenueAddressLabel.setVisibility(rootView.GONE);
            VenueAddressBox.setVisibility(rootView.GONE);
        }

        TextView VenueCityBox = rootView.findViewById(R.id.venuecity_data);
        if(!VenueCity.equals("")) {
            VenueCityBox.setText(VenueCity);
            VenueCityBox.setSelected(true);
        }else{
            TextView VenueCityLabel = rootView.findViewById(R.id.venuecity);
            VenueCityLabel .setVisibility(rootView.GONE);
            VenueCityBox.setVisibility(rootView.GONE);
        }

        TextView VenueContactBox = rootView.findViewById(R.id.venuecontact_data);
        if(!VenuePhoneNumber.equals("")) {
            VenueContactBox.setText(VenuePhoneNumber);
            VenueContactBox.setSelected(true);
        }else{
            TextView VenueContactLabel = rootView.findViewById(R.id.venuecontact);
            VenueContactLabel.setVisibility(rootView.GONE);
            VenueContactBox.setVisibility(rootView.GONE);
        }

        TextView OpenHoursBox = rootView.findViewById(R.id.openhours_data);
        if(!OpenHours.equals("")) {
            OpenHoursBox.setText(OpenHours);
        }else{
            TextView OpenHoursLabel = rootView.findViewById(R.id.openhours);
            OpenHoursLabel.setVisibility(rootView.GONE);
            OpenHoursBox.setVisibility(rootView.GONE);
        }

        TextView GeneralRuleBox = rootView.findViewById(R.id.generalrule_data);
        if(!GeneralRule.equals("")) {
            GeneralRuleBox.setText(GeneralRule);
        }else{
            TextView GeneralRuleLabel = rootView.findViewById(R.id.generalrule);
            GeneralRuleLabel.setVisibility(rootView.GONE);
            GeneralRuleBox.setVisibility(rootView.GONE);
        }

        TextView ChildRuleBox = rootView.findViewById(R.id.childrule_data);
        if(!ChildRule.equals("")) {
            ChildRuleBox.setText(ChildRule);
        }else{
            TextView ChildRuleLabel = rootView.findViewById(R.id.childrule);
            ChildRuleLabel.setVisibility(rootView.GONE);
            ChildRuleBox.setVisibility(rootView.GONE);
        }


        String url = "https://hw8-event-app.wl.r.appspot.com/geocoding?location="+VenueName;
        Log.d("url",url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("cat",response.toString());
                        Double latitude = Double.parseDouble(response.optString("latitude"));
                        Double longitude = Double.parseDouble(response.optString("longitude"));
                        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.Map);
                        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(@NonNull GoogleMap googleMap) {
                                mMap = googleMap;
                                // Add a marker at Sydney and move the camera
                                LatLng sydney = new LatLng(latitude, longitude);
                                mMap.addMarker(new MarkerOptions().position(sydney).title(VenueName));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Booba", "Error: " + error.getMessage());
            }
        });

        requestQueue.add(request);



        return rootView;
    }
}