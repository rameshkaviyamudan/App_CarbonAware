package com.sp.loginregisterfirebases;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AboutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean isAboutPageOpen = false; // Add a boolean flag to track the page state
    public AboutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutFragment newInstance(String param1, String param2) {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        // Find the VideoView in the inflated layout
        VideoView videoView = view.findViewById(R.id.videoView);

        // Set up the video path (assuming you have "about.mp4" in res/raw folder)
        String videoPath = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.about;

        // Set the video URI and start playing it
        videoView.setVideoURI(Uri.parse(videoPath));
        videoView.start();
        // Disable clicks on the VideoView
        videoView.setClickable(false);
        videoView.setFocusable(false);
        videoView.setFocusableInTouchMode(false);
        return view;
    }


    // Method to update the about page state
    public void setAboutPageState(boolean isOpen) {
        isAboutPageOpen = isOpen;
    }

    // Method to check if the about page is open
    public boolean isAboutPageOpen() {
        return isAboutPageOpen;
    }
}