package com.example.hw9;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.example.hw9.Fragments.FavoriteFragmentInterface;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.Viewholder> {

    private final Context context;
    private final ArrayList<FavoriteModel> FavoriteModelArrayList;

    private final FavoriteFragmentInterface favoriteFragmentInterface;

    // Constructor
    public FavoriteAdapter(Context context, ArrayList<FavoriteModel> FavoriteModelArrayList, FavoriteFragmentInterface FavoriteFragmentInterface) {
        this.context = context;
        this.FavoriteModelArrayList = FavoriteModelArrayList;
        this.favoriteFragmentInterface = FavoriteFragmentInterface;
    }

    @NonNull
    @Override
    public FavoriteAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_card_layout, parent, false);
        return new Viewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.Viewholder holder, @SuppressLint("RecyclerView") int position) {
        // to set data to textview and imageview of each card layout
        FavoriteModel model = FavoriteModelArrayList.get(position);
        Glide.with(holder.eventIconIV.getContext())
                .load(model.getEvent_imagef()).transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(20)))
                .into(holder.eventIconIV);
        holder.eventNameTV.setText(model.getEvent_namef());
        holder.eventVenueTV.setText("" + model.getEvent_venuef());
        holder.eventGenreTV.setText("" + model.getEvent_genref());
        holder.eventIdTV.setText(""+model.getEvent_idf());
        holder.eventDateTV.setText("" + model.getEvent_datef());
        holder.eventTimeTV.setText("" + model.getEvent_timef());


        holder.heartIcon_filled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = context.getSharedPreferences("MyUserPrefs",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.remove(model.getEvent_idf());
                editor.commit();
                FavoriteModelArrayList.remove(position);
                favoriteFragmentInterface.OnItemRemoved(FavoriteModelArrayList.size());
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,FavoriteModelArrayList.size());
                Snackbar snackbar = Snackbar
                        .make(view, model.getEvent_namef() + " removed from Favorites", Snackbar.LENGTH_LONG);
                snackbar.setBackgroundTint(Color.parseColor("#9b9b9b"));
                snackbar.setTextColor(Color.parseColor("#000000"));
                snackbar.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return FavoriteModelArrayList.size();
    }



    public static class Viewholder extends RecyclerView.ViewHolder {
        private final ImageView eventIconIV;
        private final TextView eventNameTV;
        private final TextView eventVenueTV;
        private final TextView eventGenreTV;
        private final TextView eventDateTV;
        private final TextView eventTimeTV;
        private final TextView eventIdTV;
        private final ImageView heartIcon_filled;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            eventIconIV = itemView.findViewById(R.id.idIVeventImagefav);
            eventNameTV = itemView.findViewById(R.id.idTVeventNamefav);
            eventNameTV.setSelected(true);
            eventVenueTV = itemView.findViewById(R.id.idTVeventVenuefav);
            eventVenueTV.setSelected(true);
            eventGenreTV = itemView.findViewById(R.id.idTVeventGenrefav);
            eventDateTV = itemView.findViewById(R.id.idTVeventDatefav);
            eventTimeTV = itemView.findViewById(R.id.idTVeventTimefav);
            eventIdTV = itemView.findViewById(R.id.idTVeventIdfav);
            //Heart Icons
            heartIcon_filled = itemView.findViewById(R.id.HeartIcon_filledfav);
        }
    }
}