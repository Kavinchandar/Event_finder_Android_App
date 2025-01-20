package com.example.hw9;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.hw9.Fragments.FavoriteFragment;
import com.example.hw9.Fragments.SearchFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position){
        switch(position){
            case 0:
                return new SearchFragment();
            case 1:
                return new FavoriteFragment();
            default:
                return new SearchFragment();
        }
    }

    @Override
    public int getItemCount(){
        return 2;
    }
}
