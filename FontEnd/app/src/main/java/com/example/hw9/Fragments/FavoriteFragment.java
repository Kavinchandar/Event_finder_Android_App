package com.example.hw9.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.hw9.ArtistAdapter;
import com.example.hw9.ArtistModel;
import com.example.hw9.EventAdapter;
import com.example.hw9.EventModel;
import com.example.hw9.FavoriteAdapter;
import com.example.hw9.FavoriteModel;
import com.example.hw9.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;


public class FavoriteFragment extends Fragment implements FavoriteFragmentInterface {
    private RecyclerView recyclerView;
    private FavoriteAdapter favoriteAdapter;
    ArrayList<FavoriteModel> FavoriteModelArrayList = new ArrayList<FavoriteModel>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView = getView().findViewById(R.id.FavoriteRV);
        favoriteAdapter = new FavoriteAdapter(getView().getContext(), FavoriteModelArrayList,this);
        FavoriteModelArrayList.clear();
        SharedPreferences sp = getActivity().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        Map<String,String> Data = (Map<String, String>) sp.getAll();
        LinearLayout NoFavorites = getView().findViewById(R.id.NoFavorites);
        if(Data.isEmpty()){
            NoFavorites.setVisibility(View.VISIBLE);
            getView().findViewById(R.id.FavoriteRV).setVisibility(View.GONE);
        }else{
            NoFavorites.setVisibility(View.GONE);
            getView().findViewById(R.id.FavoriteRV).setVisibility(View.VISIBLE);
        }
        for(String key: Data.keySet()){
            try {
                JSONObject favorite = new JSONObject(Data.get(key));
                FavoriteModelArrayList.add(new FavoriteModel(favorite.getString("ID"),favorite.getString("Name"),favorite.getString("Image"),favorite.getString("Date"),favorite.getString("Time"),favorite.getString("Genre"),favorite.getString("Venue")));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        recyclerView.setAdapter(favoriteAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    @Override
    public void OnItemRemoved(int length) {
        if (length == 0) {
            getView().findViewById(R.id.NoFavorites).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.FavoriteRV).setVisibility(View.GONE);
        }
    }
}