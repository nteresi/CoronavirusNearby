package com.example.coronavirusnearby;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.IMarker;

public class LineChartPage extends AppCompatActivity {

    public static LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);

        // Display the selected state from the Main Activity spinner
        TextView state = findViewById(R.id.state_display);
        state.setText(stateMapping.getFullState(MainActivity.select_state));

        // Default data fetch is the number of cases per day for the selected state
        fetchLineChartData fetch = new fetchLineChartData();
        fetch.execute(MainActivity.select_state, "new_case");

        lineChart = findViewById(R.id.line_chart);
        lineChart.setScaleEnabled(false);
        Description desc = new Description();
        desc.setText("Cases Per Day");
        desc.setTextSize(28);
        lineChart.setDescription(desc);

        IMarker marker = new DataMarkerView(this, R.layout.activity_marker);
        lineChart.setMarker(marker);

        // Buttons to toggle cases or deaths line graph, each button switches off the other
        Button new_cases = findViewById(R.id.new_cases);
        Button new_deaths = findViewById(R.id.new_deaths);

        new_cases.setEnabled(false);
        new_cases.setOnClickListener(v -> {
            desc.setText("Cases Per Day");

            new_cases.setEnabled(false);
            new_deaths.setEnabled(true);

            fetchLineChartData fetch_cases = new fetchLineChartData();
            fetch_cases.execute(MainActivity.select_state, "new_case");
        });

        new_deaths.setOnClickListener(v -> {
            desc.setText("Deaths Per Day");

            new_cases.setEnabled(true);
            new_deaths.setEnabled(false);

            fetchLineChartData fetch_deaths = new fetchLineChartData();
            fetch_deaths.execute(MainActivity.select_state, "new_death");
        });

    }

    // On back press, return to Main Activity
    @Override
    public void onBackPressed() {
        finish();
    }
}