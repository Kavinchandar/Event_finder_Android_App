package com.example.hw9.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hw9.ArtistAdapter;
import com.example.hw9.ArtistModel;
import com.example.hw9.EventAdapter;
import com.example.hw9.EventModel;
import com.example.hw9.FavoriteModel;
import com.example.hw9.R;
import com.example.hw9.RecyclerViewInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class ArtistFragment extends Fragment {

    ArrayList<JSONObject> MusicArtists = new ArrayList<>();
    ArrayList<ArtistModel> artistModelArrayList = new ArrayList<ArtistModel>();
    public ArtistFragment (ArrayList MusicArtists){
        this.MusicArtists = MusicArtists;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_artist, container, false);
        RequestQueue requestQueue = Volley.newRequestQueue(rootView.getContext());
        RecyclerView recyclerView = rootView.findViewById(R.id.ArtistList);
        ArtistAdapter artistAdapter = new ArtistAdapter(rootView.getContext(), artistModelArrayList);
        if(MusicArtists.size() == 0){
            LinearLayout NoResults = rootView.findViewById(R.id.NoMusicResults);
            NoResults.setVisibility(rootView.VISIBLE);
        }
        for(int i = 0; i < MusicArtists.size(); i++){
            JSONObject artist = MusicArtists.get(i);
            String Name = artist.optString("Name");
            String ID = artist.optString("ID");
            String Followers = artist.optString("Followers");
            String Popularity = artist.optString("Popularity");
            String Spotify = artist.optString("spotify");
            String Image = artist.optString("Image");
            final String[] Album1 = new String[1];
            final String[] Album2 = new String[1];;
            final String[] Album3 = new String[1];;
            String url = "https://hw8-event-app.wl.r.appspot.com/albumDetails?id="+ID;
            Log.d("url",url);
            int finalI = i;
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONArray Albums = response.optJSONArray("Album");
                            Album1[0] = Albums.optString(0);
                            Album2[0] = Albums.optString(1);
                            Album3[0] = Albums.optString(2);
                            artistModelArrayList.add(new ArtistModel(Name,Followers, Image, Album1[0], Album2[0],Album3[0], Spotify, Popularity));
                            if(finalI == MusicArtists.size()-1) {
                                recyclerView.setAdapter(artistAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Booba", "Error: " + error.getMessage());
                }
            });
            requestQueue.add(request);
        }
        return rootView;
    }
}