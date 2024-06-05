package com.example.tracker.Utilities;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tracker.Fragments.expense;
import com.example.tracker.Fragments.income;

public class MyViewPager extends FragmentStateAdapter {


    public MyViewPager(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new expense();
            case 1:
                return new income();
            default:
                return new expense();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
