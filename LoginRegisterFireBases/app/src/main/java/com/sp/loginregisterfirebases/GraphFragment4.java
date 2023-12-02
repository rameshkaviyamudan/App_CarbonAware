package com.sp.loginregisterfirebases;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class GraphFragment4 extends Fragment {
    private RadarChart radarChart;

    public GraphFragment4() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_graph4, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        radarChart = view.findViewById(R.id.radarChart);
        // Retrieve the StatData object from the Bundle
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("userStatData")) {
            StatData statData = bundle.getParcelable("userStatData");

            // Now you can use the values from statData as needed
            float userHomeCarbonFootprint = statData.getHomeCarbonFootprint();
            float userFoodCarbonFootprint = statData.getFoodCarbonFootprint();
            float userTravelCarbonFootprint = statData.getTravelCarbonFootprint();
            float userTotalCarbonFootprint = statData.getTotalCarbonFootprint();
        List<RadarEntry> entries = new ArrayList<>();
        entries.add(new RadarEntry(userTravelCarbonFootprint)); // User
        entries.add(new RadarEntry(200f)); // World
        entries.add(new RadarEntry(120f)); // SEA
        entries.add(new RadarEntry(160f)); // Singapore

        RadarDataSet dataSet = new RadarDataSet(entries, "Carbon Footprint");
        dataSet.setColor(ColorTemplate.COLORFUL_COLORS[0]);
        dataSet.setFillColor(ColorTemplate.COLORFUL_COLORS[0]);
        dataSet.setDrawFilled(true); // Fill the area under the line
            // Add title to the RadarDataSet
            dataSet.setLabel("Travel Carbon Footprint"); // Set the title for the RadarDataSet
            dataSet.setValueTextColor(Color.BLACK); // Set the text color for the title
            dataSet.setValueTextSize(18f); // Set the text size for the title
            dataSet.setValueTypeface(Typeface.DEFAULT_BOLD); // Set the text style for the title

        RadarData radarData = new RadarData(dataSet);
        radarData.setDrawValues(false); // Hide the values

        radarChart.setData(radarData);
        radarChart.getDescription().setEnabled(false); // Hide the description

        String[] labels = {"User", "World", "SEA", "Singapore"};
        radarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels)); // Set the labels on the X-axis

        radarChart.invalidate(); // Refresh the chart
    }}
    public RadarChart getRadarChart() {
        return radarChart;
    }
}
