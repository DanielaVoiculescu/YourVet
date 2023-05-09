package com.example.yourvet.doctor;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PetsPagerAdapter extends FragmentStateAdapter {
    private static final int NUM_TABS = 3;
    public PetsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new DogsFragment();
            case 1:
                return new CatsFragment();
            case 2:
                return new OthersFragment();
            default:
                throw new IllegalArgumentException("Invalid tab position");
        }
    }

    @Override
    public int getItemCount() {
        return NUM_TABS;
    }
}
