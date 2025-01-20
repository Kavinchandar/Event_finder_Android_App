package com.example.hw9.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.hw9.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventFragment extends Fragment {

    private String ArtistString;
    private String Venue;
    private String Date;
    private String Time;
    private String Genres;
    private String Price_Range;
    private String Ticket_status;
    private String Buy_Link;

    private String SeatMap;

    public EventFragment(String ArtistString,String Venue,String Date,String Time,String Genres,String Price_Range,String Ticket_status,String Buy_Link,String SeatMap){
        this.ArtistString = ArtistString;
        this.Venue = Venue;
        this.Date = Date;
        this.Time = Time;
        this.Genres = Genres;
        this.Price_Range = Price_Range;
        this.Ticket_status = Ticket_status;
        this.Buy_Link = Buy_Link;
        this.SeatMap = SeatMap;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event, container, false);


        TextView ArtistBox = rootView.findViewById(R.id.Artist_Teams_data);
        if(!ArtistString.equals("")) {
            ArtistBox.setText(ArtistString);
            ArtistBox.setSelected(true);
        }else{
            TextView ArtistLabel = rootView.findViewById(R.id.Artist_Teams);
            ArtistLabel.setVisibility(rootView.GONE);
            ArtistBox.setVisibility(rootView.GONE);
        }

        TextView VenueBox = rootView.findViewById(R.id.Venue_data);
        if(!Venue.equals("")){
            VenueBox.setText(Venue);
            VenueBox.setSelected(true);
        }else{
            TextView VenueLabel = rootView.findViewById(R.id.Venue);
            VenueLabel.setVisibility(rootView.GONE);
            VenueBox.setVisibility(rootView.GONE);
        }

        TextView DateBox = rootView.findViewById(R.id.Date_data);
        if(!Date.equals("")){
            SimpleDateFormat dateFormatIn = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dateFormatOut = new SimpleDateFormat("MMM dd, yyyy");
            Date date = null;
            try {
                date = dateFormatIn.parse(Date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date = dateFormatOut.format(date);
            DateBox.setText(Date);
            DateBox.setSelected(true);
        }else{
            TextView DateLabel = rootView.findViewById(R.id.Date);
            DateLabel.setVisibility(rootView.GONE);
            DateBox.setVisibility(rootView.GONE);
        }

        TextView TimeBox = rootView.findViewById(R.id.Time_data);
        if(!Time.equals("")){
            SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
            java.util.Date time = null;
            try {
                time = inputFormat.parse(Time);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            SimpleDateFormat outputFormat = new SimpleDateFormat("h:mm a", Locale.US);
            Time = outputFormat.format(time);
            TimeBox.setText(Time);
            TimeBox.setSelected(true);
        }else{
            TextView TimeLabel = rootView.findViewById(R.id.Time);
            TimeLabel.setVisibility(rootView.GONE);
            TimeBox.setVisibility(rootView.GONE);
        }


        TextView GenresBox = rootView.findViewById(R.id.Genres_data);
        if(!Genres.equals("")){
            GenresBox.setText(Genres);
            GenresBox.setSelected(true);
        }else{
            TextView GenresLabel = rootView.findViewById(R.id.Genres);
            GenresLabel.setVisibility(rootView.GONE);
            GenresBox.setVisibility(rootView.GONE);
        }

        TextView PriceRangeBox = rootView.findViewById(R.id.PriceRange_data);
        if(!Price_Range.equals("")){
            int index = Price_Range.lastIndexOf(" ");
            Price_Range = Price_Range.substring(0, index) + " (" + Price_Range.substring(index + 1) + ")";
            PriceRangeBox.setText(Price_Range);
            PriceRangeBox.setSelected(true);
        }else{
            TextView PriceRangeLabel = rootView.findViewById(R.id.PriceRange);
            PriceRangeLabel.setVisibility(rootView.GONE);
            PriceRangeBox.setVisibility(rootView.GONE);
        }

        CardView TicketBox = rootView.findViewById(R.id.TicketStatus_data_container);
        TextView TicketStatusBox = rootView.findViewById(R.id.TicketStatus_data);
        if(!Ticket_status.equals("")){
            if(Ticket_status.equals("onsale")){
                int color = getResources().getColor(R.color.green);
                TicketBox.setCardBackgroundColor(color);
                TicketStatusBox.setText("On Sale");
            }
            if(Ticket_status.equals("offsale")){
                int color = getResources().getColor(R.color.red);
                TicketBox.setCardBackgroundColor(color);
                TicketStatusBox.setText("Off Sale");
            }
            if(Ticket_status.equals("cancelled")){
                int color = getResources().getColor(R.color.black);
                TicketBox.setCardBackgroundColor(color);
                TicketStatusBox.setText("Cancelled");
            }
            if(Ticket_status.equals("postponed")){
                int color = getResources().getColor(R.color.orange);
                TicketBox.setCardBackgroundColor(color);
                TicketStatusBox.setText("Postponed");
            }
            if(Ticket_status.equals("rescheduled")){
                int color = getResources().getColor(R.color.orange);
                TicketBox.setCardBackgroundColor(color);
                TicketStatusBox.setText("Rescheduled");
            }
            TicketStatusBox.setSelected(true);
        }else{
            TextView TicketStatusLabel = rootView.findViewById(R.id.TicketStatus);
            TicketStatusLabel.setVisibility(rootView.GONE);
            TicketStatusBox.setVisibility(rootView.GONE);
        }


        TextView BuyTicketsBox = rootView.findViewById(R.id.BuyLink_data);
        if(!Buy_Link.equals("")){
            BuyTicketsBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse(Buy_Link);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
            BuyTicketsBox.setText(Buy_Link);
            BuyTicketsBox.setSelected(true);
        }else{
            TextView BuyTicketLabel = rootView.findViewById(R.id.BuyLink);
            BuyTicketLabel.setVisibility(rootView.GONE);
            BuyTicketsBox.setVisibility(rootView.GONE);
        }

        ImageView seatMap = rootView.findViewById(R.id.SeatMap);
        if(!SeatMap.equals("")){
            Glide.with(rootView).load(SeatMap).into(seatMap);
        }

        return rootView;
    }
}