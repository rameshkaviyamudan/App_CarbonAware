package com.sp.loginregisterfirebases;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class GraphFragment2 extends Fragment {
    private PieChart pieChart;

    public GraphFragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_graph2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pieChart = view.findViewById(R.id.pieChart);

        BarChart barChart = view.findViewById(R.id.barChart);
        // Retrieve the StatData object from the Bundle
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("userStatData")) {
            StatData statData = bundle.getParcelable("userStatData");

            // Now you can use the values from statData as needed
            float userHomeCarbonFootprint = statData.getHomeCarbonFootprint();
            float userFoodCarbonFootprint = statData.getFoodCarbonFootprint();
            float userTravelCarbonFootprint = statData.getTravelCarbonFootprint();
            float userTotalCarbonFootprint = statData.getTotalCarbonFootprint();
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(userFoodCarbonFootprint, "User")); // User
        entries.add(new PieEntry(150f, "World")); // World
        entries.add(new PieEntry(90f, "SEA")); // SEA
        entries.add(new PieEntry(110f, "Singapore")); // Singapore

        PieDataSet dataSet = new PieDataSet(entries, "Carbon Footprint");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(12f);

        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter(pieChart)); // Format the values as percentages
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false); // Hide the description
        pieChart.getLegend().setEnabled(false); // Hide the legend
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setDrawEntryLabels(true); // Hide labels on pie slices
        // Display the title inside the PieChart
            pieChart.setCenterText("Food Carbon Footprint");
            pieChart.setCenterTextSize(18f);
            pieChart.setCenterTextColor(Color.BLACK);

        pieChart.invalidate(); // Refresh the chart
    }
}
    public PieChart getPieChart() {
        return pieChart;
    }
}
