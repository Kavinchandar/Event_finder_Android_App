package com.example.hw9;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.hw9.ArtistModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.Viewholder> {

    private final Context context;
    private final ArrayList<ArtistModel> artistModelArrayList;

    // Constructor
    public ArtistAdapter(Context context, ArrayList<ArtistModel> artistModelArrayList) {
        this.context = context;
        this.artistModelArrayList = artistModelArrayList;
    }

    @NonNull
    @Override
    public ArtistAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_card_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        ArtistModel model = artistModelArrayList.get(position);
        holder.ArtistName.setText(model.getArtist_name());

        String newNumber = model.getArtist_Followers().replace(",","");
        long number = Integer.parseInt(newNumber);
        if (number < 1000) {
            newNumber = String.valueOf(number);
        }else{
            final String[] units = new String[]{"K", "M", "B"};
            int digitGroups = (int) (Math.log10(number) / Math.log10(1000));

            newNumber = new DecimalFormat("#,##0.#")
                    .format(number / Math.pow(1000, digitGroups))
                    + units[digitGroups-1];
        }

        holder.ArtistFollowers.setText(""+ newNumber + " Followers");
        holder.ArtistPopularity.setProgress(Integer.parseInt(model.getArtist_popularity()));
        holder.ArtistPopularityNumber.setText(""+model.getArtist_popularity());
        Glide.with(holder.ArtistImage.getContext())
                .load(model.getArtist_Image()).transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(20)))
                .into(holder.ArtistImage);
        Glide.with(holder.AlbumImage1.getContext())
                .load(model.getArtist_AlbumImage1()).transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(20)))
                .into(holder.AlbumImage1);
        Glide.with(holder.AlbumImage2.getContext())
                .load(model.getArtist_AlbumImage2()).transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(20)))
                .into(holder.AlbumImage2);
        Glide.with(holder.AlbumImage3.getContext())
                .load(model.getArtist_AlbumImage3()).transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(20)))
                .into(holder.AlbumImage3);
        holder.ArtistSpotify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(""+model.getArtist_Spotify());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);
                    }
                });
    }



    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return artistModelArrayList.size();
    }


    public static class Viewholder extends RecyclerView.ViewHolder {
        private final ImageView ArtistImage;
        private final TextView ArtistName;
        private final TextView ArtistFollowers;
        private final TextView ArtistSpotify;
        private final TextView ArtistPopularityNumber;
        private final ProgressBar ArtistPopularity;

        private final ImageView AlbumImage1;

        private final ImageView AlbumImage2;

        private final ImageView AlbumImage3;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            ArtistImage = itemView.findViewById(R.id.ArtistImage);
            ArtistName = itemView.findViewById(R.id.ArtistName);
            ArtistFollowers = itemView.findViewById(R.id.ArtistRating);
            ArtistSpotify = itemView.findViewById(R.id.ArtistSpotify);
            ArtistSpotify.setPaintFlags(ArtistSpotify.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
            ArtistPopularity = itemView.findViewById(R.id.popularityBar);
            ArtistPopularityNumber = itemView.findViewById(R.id.popularityNumber);
            AlbumImage1 = itemView.findViewById(R.id.AlbumImage1);
            AlbumImage2 = itemView.findViewById(R.id.AlbumImage2);
            AlbumImage3 = itemView.findViewById(R.id.AlbumImage3);

        }
    }
}