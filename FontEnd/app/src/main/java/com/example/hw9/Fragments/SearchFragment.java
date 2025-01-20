package com.example.hw9.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hw9.EventAdapter;
import com.example.hw9.EventArtistVenueDets;
import com.example.hw9.EventModel;
import com.example.hw9.R;
import com.example.hw9.RecyclerViewInterface;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;


public class SearchFragment extends Fragment implements AdapterView.OnItemSelectedListener, RecyclerViewInterface {
    RecyclerView eventRV;
    EventAdapter eventAdapter;
    ArrayList<EventModel> eventModelArrayList = new ArrayList<EventModel>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //rootview
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        //Dropdown Menu
        Spinner spinner = rootView.findViewById(R.id.spinner_1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.categories, R.layout.my_selected_item);
        adapter.setDropDownViewResource(R.layout.my_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        //Getting Form Input
        AutoCompleteTextView Keyword = rootView.findViewById(R.id.keywordInput);
        EditText Distance = rootView.findViewById(R.id.distanceInput);
        Spinner Category = rootView.findViewById(R.id.spinner_1);
        EditText Location = rootView.findViewById(R.id.locationInput);
        Switch AutoDetect = (Switch) rootView.findViewById(R.id.autodetect);

        //Buttons
        Button SearchButton = rootView.findViewById(R.id.searchButton);
        Button ClearButton = rootView.findViewById(R.id.clearButton);

        //getting the entire Form
        CardView searchform = rootView.findViewById(R.id.searchForm);

        //getting the entire results table
        LinearLayout searchResults = rootView.findViewById(R.id.searchResults);

        RequestQueue requestQueue = Volley.newRequestQueue(rootView.getContext());

        //Recycler View Stuff
        eventRV = rootView.findViewById(R.id.idRVEvent);
        eventAdapter = new EventAdapter(rootView.getContext(), eventModelArrayList, this);

        //ProgressBar for loading screen
        LinearLayout progressBarContainer = rootView.findViewById(R.id.progressBarContainer);

        //No results layout if no results available
        LinearLayout noResults = rootView.findViewById(R.id.NoResults);

        //ProgressBar for Autocomplete
        ProgressBar loadingIcon = rootView.findViewById(R.id.autoCompleteLoading);


        final String[] NewLocation = new String[1];

        AutoDetect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (AutoDetect.isChecked()) {
                    Location.setVisibility(View.GONE);

                    String url = "https://ipinfo.io/?token=08d62f3d270fd3";
                    Log.d("url",url);
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    NewLocation[0] = response.optString("loc");
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Booba", "Error: " + error.getMessage());
                        }
                    });

                    requestQueue.add(request);
                } else {
                    Location.setVisibility(View.VISIBLE);
                }
            }
        });

        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword = Keyword.getText().toString();
                String distance = Distance.getText().toString();
                final String[] category = {Category.getSelectedItem().toString()};
                final String[] location = {Location.getText().toString()};


                if(AutoDetect.isChecked()){
                    location[0] = NewLocation[0];
                }

                //Form Validation
                if (TextUtils.isEmpty(keyword) || TextUtils.isEmpty(location[0])) {
                    Snackbar snackbar = Snackbar
                            .make(view, "Please fill all fields", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    searchform.animate().alpha(0.0f).translationX(100).setDuration(500).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            searchform.setVisibility(View.GONE);
                            searchResults.setVisibility(View.VISIBLE);
                            progressBarContainer.setVisibility(View.VISIBLE);
                            eventModelArrayList.clear();
                            eventAdapter.notifyDataSetChanged();

                            if(category[0].equals("All")){
                                category[0] = "Default";
                            }
                            String url = "https://hw8-event-app.wl.r.appspot.com/allEvents?keyword=" + keyword + "&radius=" + distance + "&category="+ category[0] +"&location=" + location[0];
                            Log.d("url",url);
                            url = url.replace(" ","%20");
                            Log.d("info", url);
                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.d("Boba", "Response: " + response.toString());
                                            progressBarContainer.setVisibility(View.GONE);
                                            Iterator<String> keys = response.keys();
                                            if(!keys.hasNext()){
                                                eventRV.setVisibility(View.GONE);
                                                noResults.setVisibility(View.VISIBLE);
                                            }else{
                                                eventRV.setVisibility(View.VISIBLE);
                                                noResults.setVisibility(View.GONE);
                                                while(keys.hasNext()){
                                                    String key = keys.next();
                                                        JSONObject Event = response.optJSONObject(key);
                                                        String ID = Event.optString("id");
                                                        String Date = Event.optString("Date");
                                                        String Time = Event.optString("Time");
                                                        String Icon = Event.optString("Icon");
                                                        String Name = Event.optString("Name");
                                                        String Genre = Event.optString("Genre");
                                                        String Venue = Event.optString("Venue");
                                                        try {
                                                            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                                            java.util.Date date = inputFormat.parse(Date);

                                                            SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                                                            Date = outputFormat.format(date);

                                                            // Now the outputDateString variable contains the date string in MM-DD-YYYY format
                                                        } catch (ParseException e) {
                                                            e.printStackTrace();
                                                        }
                                                        try {
                                                            SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
                                                            Date time = inputFormat.parse(Time);

                                                            SimpleDateFormat outputFormat = new SimpleDateFormat("h:mm a", Locale.US);
                                                            Time = outputFormat.format(time);
                                                        } catch (ParseException e) {
                                                            e.printStackTrace();
                                                        }
                                                        eventModelArrayList.add(new EventModel(ID,Name,Icon,Date,Time,Genre,Venue));


                                                }
                                                eventRV.setAdapter(eventAdapter);
                                                eventRV.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
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
                    });

                }
            }
        });

        ImageView BackButton = rootView.findViewById(R.id.backButton);
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchResults.setVisibility(View.GONE);
                searchform.setVisibility(View.VISIBLE);
                searchform.animate().alpha(1.0f).translationX(0).setDuration(500);
                noResults.setVisibility(View.GONE);
            }
        });

        ClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Keyword.setText("");
                Distance.setText("10");
                Category.setSelection(0);
                Location.setText("");
                AutoDetect.setChecked(false);
            }
        });

        final boolean[] itemSelected = {false};

        Keyword.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // An item has been selected from the dropdown list
                itemSelected[0] = true;
            }
        });


        Keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loadingIcon.setVisibility(View.VISIBLE);
                String url = "https://hw8-event-app.wl.r.appspot.com/suggest?keyword=" + charSequence.toString();
                Log.d("url",url);
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loadingIcon.setVisibility(View.GONE);
                        ArrayList<String> answers = new ArrayList<>();
                        try {
                            JSONArray suggestions = response.getJSONArray("suggestions");
                            for (int i = 0; i < suggestions.length(); i++) {
                                String item = suggestions.optString(i);
                                answers.add(item);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(rootView.getContext(), R.layout.autocomplete_dropdown_item, answers);
                        Keyword.setAdapter(adapter);
                        Keyword.setThreshold(1);
                        if (charSequence.toString().length() > 0) {
                            Keyword.showDropDown();
                        }
                        if(itemSelected[0]){
                            Keyword.dismissDropDown();
                            itemSelected[0] = false;
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


            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public void OnItemClick(int position) {
        Intent intent = new Intent(getActivity(), EventArtistVenueDets.class);
        intent.putExtra("ID",eventModelArrayList.get(position).getEvent_id());
        intent.putExtra("Name",eventModelArrayList.get(position).getEvent_name());
        intent.putExtra("Image",eventModelArrayList.get(position).getEvent_image());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        eventAdapter.notifyDataSetChanged();
    }
}

