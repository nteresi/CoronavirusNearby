package com.example.coronavirusnearby;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataFormatter extends ValueFormatter {

    /* Value formatter for the X Axis of the line chart.
     * Converts the datetime of the CDC JSON to a nicer format.
     * Dates are fetched and stored in the fetchLineChartData class, and accessed by index.
     *
     * Parameters:
     * float value: the index of the date array to access
     * AxisBase axis: the axis to apply the formatter to
     */
    @Override
    public String getAxisLabel(float value, AxisBase axis) {

        Date date = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            date = sdf.parse(fetchLineChartData.dates.get((int) value));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.applyPattern("MM/dd/yy");
        return sdf.format(date);
    }

}
