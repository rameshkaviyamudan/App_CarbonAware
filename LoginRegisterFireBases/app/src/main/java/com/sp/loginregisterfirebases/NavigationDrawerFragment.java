package com.sp.loginregisterfirebases;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;

public class NavigationDrawerFragment extends Fragment {
    private CardView cardAbout;
    private CardView cardWebsite;
    private CardView cardLogout;
    private CardView cardExit;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);        // Find the CardViews
        cardAbout = view.findViewById(R.id.frabout);
        cardWebsite = view.findViewById(R.id.frwebs);
        cardLogout = view.findViewById(R.id.frlogout);
        cardExit = view.findViewById(R.id.frexit);

        // Set click listeners
        cardAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle "About" card click
                // Implement your logic here
            }
        });

        cardWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle "Go to the Website" card click
                // Create an instance of WebViewFragment and navigate to it
                WebViewFragment webViewFragment = new WebViewFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container2, webViewFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        cardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle "Logout" card click
                // Implement your logic here
                // Navigate to the login activity
                Intent intent = new Intent(getActivity(), Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });

        cardExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle "Exit" card click
                // Implement your logic here
                requireActivity().finish();
            }
        });

        return view;
    }
}