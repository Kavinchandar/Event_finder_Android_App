package com.example.hw9;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.Viewholder> {

    private final RecyclerViewInterface recyclerViewInterface;
    private final Context context;
    private final ArrayList<EventModel> eventModelArrayList;
    SharedPreferences sp;

    // Constructor
    public EventAdapter(Context context, ArrayList<EventModel> eventModelArrayList, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.eventModelArrayList = eventModelArrayList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public EventAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card_layout, parent, false);

        return new Viewholder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        EventModel model = eventModelArrayList.get(position);
        Glide.with(holder.eventIconIV.getContext())
                .load(model.getEvent_image()).transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(20)))
                .into(holder.eventIconIV);
        holder.eventNameTV.setText(model.getEvent_name());
        holder.eventVenueTV.setText("" + model.getEvent_venue());
        holder.eventGenreTV.setText("" + model.getEvent_genre());
        holder.eventIdTV.setText(""+model.getEvent_id());
        holder.eventDateTV.setText("" + model.getEvent_date());
        holder.eventTimeTV.setText("" + model.getEvent_time());


        sp = context.getSharedPreferences("MyUserPrefs",Context.MODE_PRIVATE);
        if (sp.contains(model.getEvent_id())) {
            holder.heartIcon_filled.setVisibility(View.VISIBLE);
            holder.heartIcon_outline.setVisibility(View.GONE);
        }
        else{
            holder.heartIcon_filled.setVisibility(View.GONE);
            holder.heartIcon_outline.setVisibility(View.VISIBLE);
        }

        holder.heartIcon_outline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.heartIcon_filled.setVisibility(View.VISIBLE);
                holder.heartIcon_outline.setVisibility(View.GONE);
                SharedPreferences.Editor editor = sp.edit();
                JSONObject favorite = new JSONObject();

                try {
                    favorite.put("ID",model.getEvent_id());
                    favorite.put("Name",model.getEvent_name());
                    favorite.put("Venue",model.getEvent_venue());
                    favorite.put("Genre",model.getEvent_genre());
                    favorite.put("Date",model.getEvent_date());
                    favorite.put("Time",model.getEvent_time());
                    favorite.put("Image",model.getEvent_image());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                editor.putString(model.getEvent_id(),favorite.toString());
                editor.apply();
                Snackbar snackbar = Snackbar
                        .make(view, model.getEvent_name() + " added to Favorites", Snackbar.LENGTH_LONG);
                snackbar.setBackgroundTint(Color.parseColor("#9b9b9b"));
                snackbar.setTextColor(Color.parseColor("#000000"));
                snackbar.show();

            }
        });
        holder.heartIcon_filled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.heartIcon_filled.setVisibility(View.GONE);
                holder.heartIcon_outline.setVisibility(View.VISIBLE);
                SharedPreferences.Editor editor = sp.edit();
                editor.remove(model.getEvent_id());
                editor.apply();
                Snackbar snackbar = Snackbar
                        .make(view, model.getEvent_name() + " removed from Favorites", Snackbar.LENGTH_LONG);
                snackbar.setBackgroundTint(Color.parseColor("#9b9b9b"));
                snackbar.setTextColor(Color.parseColor("#000000"));
                snackbar.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return eventModelArrayList.size();
    }


    public static class Viewholder extends RecyclerView.ViewHolder {
        private final ImageView eventIconIV;
        private final TextView eventNameTV;
        private final TextView eventVenueTV;
        private final TextView eventGenreTV;
        private final TextView eventDateTV;
        private final TextView eventTimeTV;
        private final TextView eventIdTV;

        private ImageView heartIcon_outline;
        private ImageView heartIcon_filled;
        public Viewholder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            eventIconIV = itemView.findViewById(R.id.idIVeventImage);
            eventNameTV = itemView.findViewById(R.id.idTVeventName);
            eventNameTV.setSelected(true);
            eventVenueTV = itemView.findViewById(R.id.idTVeventVenue);
            eventVenueTV.setSelected(true);
            eventGenreTV = itemView.findViewById(R.id.idTVeventGenre);
            eventDateTV = itemView.findViewById(R.id.idTVeventDate);
            eventTimeTV = itemView.findViewById(R.id.idTVeventTime);
            eventIdTV = itemView.findViewById(R.id.idTVeventId);
            //Heart Icons
            heartIcon_outline = itemView.findViewById(R.id.HeartIcon_outline);
            heartIcon_filled = itemView.findViewById(R.id.HeartIcon_filled);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            recyclerViewInterface.OnItemClick(position);
                        }
                    }
                }
            });
        }



    }
}