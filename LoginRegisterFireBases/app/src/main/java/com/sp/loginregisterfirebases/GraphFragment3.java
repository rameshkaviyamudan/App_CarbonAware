package com.sp.loginregisterfirebases;

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

public class GraphFragment3 extends Fragment {
    private BarChart barChart;

    public GraphFragment3() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_graph3, container, false);
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
            float userHomeCarbonFootprint = statData.getHomeCarbonFootprint();
            float userFoodCarbonFootprint = statData.getFoodCarbonFootprint();
            float userTravelCarbonFootprint = statData.getTravelCarbonFootprint();
            float userTotalCarbonFootprint = statData.getTotalCarbonFootprint();
            List<BarEntry> entries = new ArrayList<>();
            entries.add(new BarEntry(0, userHomeCarbonFootprint)); // User
            entries.add(new BarEntry(1, 300f)); // World
            entries.add(new BarEntry(2, 260f)); // SEA
            entries.add(new BarEntry(3, 380f)); // Singapore

            BarDataSet dataSet = new BarDataSet(entries, "Carbon Footprint");
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            // Add title to the BarDataSet
            dataSet.setLabel("Home Carbon Footprint"); // Set the title for the BarDataSet

            dataSet.setValueTextColor(Color.BLACK); // Set the text color for the title
            dataSet.setValueTextSize(12f); // Set the text size for the title
            dataSet.setValueTypeface(Typeface.DEFAULT_BOLD); // Set the text style for the title

            BarData barData = new BarData(dataSet);
            barData.setBarWidth(0.9f); // Set custom bar width (0.9f for spacing)

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
        return new String[]{"User", "World", "SEA", "Singapore"};
    }
    public BarChart getBarChart() {
        return barChart;
    }
}

