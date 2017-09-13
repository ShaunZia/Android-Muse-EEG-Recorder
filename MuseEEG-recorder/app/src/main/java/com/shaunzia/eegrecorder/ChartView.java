package com.shaunzia.eegrecorder;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import android.content.Context;
import android.util.AttributeSet;

/**
 * ChartView.java is based on Phil Jay's MPAndroidChart line-chart library
 *
 * @author Shaun Zia
 * @version 2.0 27/11/2015
 */
public class ChartView extends LineChart {

    // Declare sampling rate variables
    public final static int MAX_VISIBLE_RANGE = Constants.getDefaultSamplingRate() * 2;
    public final static int MAX_FLUSH_COUNT = Constants.getDefaultSamplingRate() / 5;

    // Declare line data variables
    private LineData data;
    private LineDataSet set;
    private int flushCount = 0;

    // Constructor
    public ChartView(Context context) {
        super(context);
    }

    // Constructor
    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // Constructor
    public ChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // Define chart display
    public void setChart(String chartName, int color) {
        // Binding data
        data = new LineData();
        set = initSet(chartName, color);
        data.addDataSet(set);

        // Define axes
        setData(data);
        setDescription(chartName);
        getXAxis().setDrawLabels(false);
        getAxisRight().setEnabled(false);
        getLegend().setEnabled(false);
        setPinchZoom(false);
        setDragEnabled(false);
        setTouchEnabled(false);
    }

    // Display discrete data values
    public void addEntry(float value) {
        data.addXValue(set.getEntryCount() + "");
        data.addEntry(new Entry(value, set.getEntryCount()), 0);
        if (flushCount > MAX_FLUSH_COUNT) {
            notifyDataSetChanged();
            setVisibleXRangeMaximum(MAX_VISIBLE_RANGE);
            moveViewToX(data.getXValCount() - MAX_VISIBLE_RANGE - 1);
            flushCount = 0;
        }
        ++flushCount;

    }

    // Define line draw settings
    private LineDataSet initSet(String name, int color) {
        LineDataSet set = new LineDataSet(null, name);
        set.setDrawValues(false);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(color);
        set.setDrawCircles(false);
        set.setLineWidth(2f);
        set.setDrawValues(false);
        return set;
    }
}
