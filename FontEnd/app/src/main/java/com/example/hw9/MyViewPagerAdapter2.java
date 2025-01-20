package com.example.hw9;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.hw9.Fragments.ArtistFragment;
import com.example.hw9.Fragments.EventFragment;
import com.example.hw9.Fragments.VenueFragment;

import java.util.ArrayList;
import java.util.List;

public class MyViewPagerAdapter2 extends FragmentStateAdapter {

    ArrayList<Fragment> data = new ArrayList<>();
    public MyViewPagerAdapter2(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
            return data.get(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addData(Fragment fragment){
        data.add(fragment);
    }
}
