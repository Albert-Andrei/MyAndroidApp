package com.example.myandroidapplication.ui.movies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.myandroidapplication.R;
import com.example.myandroidapplication.ui.adapter.SectionPagerAdapter;
import com.example.myandroidapplication.ui.home.MovieFragment;
import com.example.myandroidapplication.ui.home.TvShowsFragment;
import com.google.android.material.tabs.TabLayout;

public class MoviesFragment extends Fragment {

    View myFragment;
    ViewPager viewPager;
    TabLayout tabLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_movies, container, false);

        viewPager = myFragment.findViewById(R.id.viewPagerDashboard);
        tabLayout = myFragment.findViewById(R.id.tabLayoutDashboard);

        return myFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getWindow().setStatusBarColor(getActivity().getColor(R.color.main));

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());

        adapter.addFragmnet(new MyMovies(), "Movies");
        adapter.addFragmnet(new MyTvShows(), "Tv Shows");

        viewPager.setAdapter(adapter);
    }
}
