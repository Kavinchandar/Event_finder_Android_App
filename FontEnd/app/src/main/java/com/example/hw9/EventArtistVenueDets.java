package com.example.hw9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hw9.Fragments.ArtistFragment;
import com.example.hw9.Fragments.EventFragment;
import com.example.hw9.Fragments.VenueFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EventArtistVenueDets extends AppCompatActivity {

    TabLayout tabLayout_new;
    ViewPager2 viewPager2_new;
    MyViewPagerAdapter2 myViewPagerAdapter_new;

    //Tab icons
    private int[] tabIcons = {
            R.drawable.info_icon,
            R.drawable.artist_icon,
            R.drawable.venue_icon
    };


    @SuppressLint({"RestrictedApi", "MissingInflatedId", "ResourceType"})

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_artist_venue_dets);


        viewPager2_new = findViewById(R.id.view_pager1);
        tabLayout_new = findViewById(R.id.tab_layout1);
        TabLayout.Tab tab0 = tabLayout_new.getTabAt(0);
        tab0.setIcon(R.drawable.info_icon);
        int tabIconColor = ContextCompat.getColor(getBaseContext(), R.color.selected);
        tab0.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        TabLayout.Tab tab1 = tabLayout_new.getTabAt(1);
        Drawable icon = ContextCompat.getDrawable(this, R.drawable.artist_icon);
        tab1.setIcon(icon);
        TabLayout.Tab tab2 = tabLayout_new.getTabAt(2);
        tab2.setIcon(R.drawable.venue_icon);
        myViewPagerAdapter_new = new MyViewPagerAdapter2(this);


        String ID = getIntent().getStringExtra("ID");
        String Name = getIntent().getStringExtra("Name");
        String Image = getIntent().getStringExtra("Image");

        //variables for making api calls
        ArrayList<JSONObject> musicartists = new ArrayList<>();
        final String[] venue_key = new String[1];



        //Back arrow in Action Bar
        ImageView backArrow = findViewById(R.id.backarrow);
        backArrow.setOnClickListener(v -> {finish();});

        //Event Name Header in Action Bar
        TextView header = findViewById(R.id.eventHeader);
        header.setText(Name);
        header.setSelected(true);

        //Heart Icons
        ImageView heartIcon_outline = findViewById(R.id.heartIconOutline);
        ImageView heartIcon_filled = findViewById(R.id.heartIconFilled);

        SharedPreferences sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        if (sp.contains(ID)) {
            heartIcon_filled.setVisibility(View.VISIBLE);
            heartIcon_outline.setVisibility(View.GONE);
        }
        else{
            heartIcon_filled.setVisibility(View.GONE);
            heartIcon_outline.setVisibility(View.VISIBLE);
        }




        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://hw8-event-app.wl.r.appspot.com/eventDetails?event_id="+ID;
        Log.d("url",url);
        //Getting Event Detail Info
        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                            String ID = response.optString("Id");
                            String EventName = response.optString("Name");
                            String Date = response.optString("Date");
                            String Time = response.optString("Time");
                            String ArtistString = response.optString("ArtistString");
                            String Venue = response.optString("Venue");
                            String Genre = response.optString("Genre");
                            String Prices = response.optString("Prices");
                            String TicketStatus = response.optString("Ticket");
                            String BuyLink = response.optString("Buy");
                            String SeatMap = response.optString("SeatMap");
                            JSONArray Artists = response.optJSONArray("Artist");

                        heartIcon_outline.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                heartIcon_outline.setVisibility(view.GONE);
                                heartIcon_filled.setVisibility(view.VISIBLE);

                                JSONObject favorite = new JSONObject();
                                try {
                                    favorite.put("ID",ID);
                                    favorite.put("Name",EventName);
                                    favorite.put("Venue",Venue);
                                    favorite.put("Genre",Genre);
                                    favorite.put("Date",Date);
                                    favorite.put("Time",Time);
                                    favorite.put("Image",Image);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString(ID,favorite.toString());
                                editor.commit();

                                Snackbar snackbar = Snackbar
                                        .make(view, "Event Added to Favorites", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        });
                        heartIcon_filled.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                heartIcon_outline.setVisibility(view.VISIBLE);
                                heartIcon_filled.setVisibility(view.GONE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.remove(ID);
                                editor.commit();
                            }
                        });

                        ImageView twitterIcon = findViewById(R.id.twitter_icon);
                        twitterIcon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String url = "http://twitter.com/share?text=Check "+ EventName +" on Ticketmaster. &url="+BuyLink+"&hashtags=hashtag1,hashtag2,hashtag3"; // Replace with your desired link
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(url));
                                startActivity(intent);
                            }
                        });

                        ImageView FacebookIcon = findViewById(R.id.facebook_icon);
                        FacebookIcon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String url = "https://www.facebook.com/sharer/sharer.php?u="+BuyLink+"&amp;src=sdkpreparse"; // Replace with your desired link
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(url));
                                startActivity(intent);
                            }
                        });

                            //Artist Details API Call
                            for (int i=0; i< Artists.length(); i++){
                                JSONArray innerArray = Artists.optJSONArray(i);
                                String MusicArtistName = innerArray.optString(0);
                                String isMusicArtist = innerArray.optString(1);
                                if(isMusicArtist.equals("1")){
                                    String url = "https://hw8-event-app.wl.r.appspot.com/artistDetails?name="+MusicArtistName;
                                    Log.d("url",url);
                                    int finalI = i;
                                    JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET, url, null,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    musicartists.add(response);
                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("Booba", "Error: " + error.getMessage());
                                        }
                                    });
                                    requestQueue.add(request2);
                                }
                            }

                            //Venue Details Api Call
                            venue_key[0] = Venue;
                            String url = "https://hw8-event-app.wl.r.appspot.com/venueDetails?keyword="+Venue;
                            Log.d("url",url);

                            JsonObjectRequest request3 = new JsonObjectRequest(Request.Method.GET, url, null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.d("new",response.toString());
                                            EventFragment eventFragment = new EventFragment(ArtistString,Venue,Date,Time,Genre,Prices,TicketStatus,BuyLink,SeatMap);
                                            ArtistFragment artistFragment = new ArtistFragment(musicartists);
                                            VenueFragment venueFragment = new VenueFragment(response);
                                            myViewPagerAdapter_new.addData(eventFragment);
                                            myViewPagerAdapter_new.addData(artistFragment);
                                            myViewPagerAdapter_new.addData(venueFragment);
                                            viewPager2_new.setAdapter(myViewPagerAdapter_new);
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Booba", "Error: " + error.getMessage());
                                }
                            });
                            requestQueue.add(request3);



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Booba", "Error: " + error.getMessage());
            }
        });

        requestQueue.add(request1);
        Log.d("boba",musicartists.toString());

        tabLayout_new.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2_new.setCurrentItem(tab.getPosition());
                int tabIconColor = ContextCompat.getColor(getBaseContext(), R.color.selected);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(getBaseContext(), R.color.unselected);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2_new.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position){
                super.onPageSelected(position);
                tabLayout_new.getTabAt(position).select();
            }
        });
    }


}