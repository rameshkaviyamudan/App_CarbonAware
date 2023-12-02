package com.sp.loginregisterfirebases;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class BarChartClass {

    private BarChart barChart;
    private List<BarEntry> barEntries;

    public BarChartClass(BarChart barChart) {
        this.barChart = barChart;
        this.barEntries = new ArrayList<>();
    }

    public void addData(int[] values) {
        for (int i = 0; i < values.length; i++) {
            barEntries.add(new BarEntry(i, values[i]));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Stats");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.invalidate();
    }

    public List<BarEntry> getBarEntries() {
        return barEntries;
    }
}