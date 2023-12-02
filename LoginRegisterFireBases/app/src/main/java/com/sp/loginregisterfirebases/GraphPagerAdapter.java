package com.sp.loginregisterfirebases;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class GraphPagerAdapter extends FragmentStateAdapter {
    public GraphPagerAdapter(Stats fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return a NEW fragment instance based on the position
        switch (position) {
            case 0:
                return new GraphFragment1();
            case 1:
                return new GraphFragment2();
            case 2:
                return new GraphFragment3();
            case 3:
                return new GraphFragment4();
            case 4:
                return new GraphFragment5();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 5; // Total number of fragments
    }

}
