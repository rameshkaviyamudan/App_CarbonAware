package com.sp.loginregisterfirebases;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class GraphFragment5 extends Fragment {
    private BarChart barChart;
    public GraphFragment5() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_graph1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        barChart = view.findViewById(R.id.barChart);
        // Retrieve the StatData object from the Bundle
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("userStatData")) {
            StatData statData = bundle.getParcelable("userStatData");

            // Now you can use the values from statData as needed
            float userTotalCarbonFootprint = statData.getTotalCarbonFootprint();

            // Retrieve the total carbon footprint values from each SharedPreferences
            SharedPreferences travelPrefs = requireContext().getSharedPreferences("MytravelPrefs", Context.MODE_PRIVATE);
            float travelFootprint = travelPrefs.getFloat("travelfootprint", 0.0f);

            SharedPreferences housePrefs = requireContext().getSharedPreferences("MyhousePrefs", Context.MODE_PRIVATE);
            float houseFootprint = housePrefs.getFloat("housefootprint", 0.0f);

            SharedPreferences foodPrefs = requireContext().getSharedPreferences("MyfoodPrefs", Context.MODE_PRIVATE);
            float foodFootprint = foodPrefs.getFloat("foodfootprint", 0.0f);

// Calculate the combined total carbon footprint
            float storedTotalCarbonFootprint = travelFootprint + houseFootprint + foodFootprint;



            List<BarEntry> entries = new ArrayList<>();
            entries.add(new BarEntry(0, userTotalCarbonFootprint)); // User
            entries.add(new BarEntry(1, storedTotalCarbonFootprint)); // Average

            BarDataSet dataSet = new BarDataSet(entries, "Carbon Footprint");
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            // Add title to the BarDataSet
            dataSet.setLabel("User vs Average Carbon Footprint"); // Set the title for the BarDataSet
            dataSet.setValueTextColor(Color.BLACK); // Set the text color for the title
            dataSet.setValueTextSize(12f); // Set the text size for the title
            dataSet.setValueTypeface(Typeface.DEFAULT_BOLD); // Set the text style for the title

            BarData barData = new BarData(dataSet);
            barData.setBarWidth(0.5f); // Set custom bar width (0.5f for spacing)

            barChart.setData(barData);
            barChart.setFitBars(true); // Make the bars fit the screen width
            barChart.getDescription().setEnabled(false); // Hide the description
            barChart.getLegend().setEnabled(false); // Hide the legend

            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(getLabels()));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1f); // Minimum axis-step (interval) is 1
            xAxis.setDrawGridLines(false); // Hide vertical grid lines

            YAxis leftYAxis = barChart.getAxisLeft();
            YAxis rightYAxis = barChart.getAxisRight();
            leftYAxis.setAxisMinimum(0f); // Minimum value on the Y-axis is 0
            rightYAxis.setAxisMinimum(0f); // Minimum value on the Y-axis is 0

            barChart.invalidate(); // Refresh the chart
        }
    }

    private String[] getLabels() {
        return new String[]{"Your Average", "Your Current"};
    }
    public BarChart getBarChart() {
        return barChart;
    }
}

