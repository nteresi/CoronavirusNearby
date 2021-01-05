package com.example.coronavirusnearby;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView data;
    TextView state_display;
    public static String select_state = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up state spinner with 50 US states + D.C.
        Spinner state_spinner = (Spinner) findViewById(R.id.states_spinner);
        state_spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> state_adapter = ArrayAdapter.createFromResource(this,
                R.array.states_array, android.R.layout.simple_spinner_item);
        state_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state_spinner.setAdapter(state_adapter);

        // This will display the total cases/deaths for the state
        data = findViewById(R.id.display_data);

        // Display the full state name here
        state_display = findViewById(R.id.display_state);

        // Buttons are initially hidden until a state is selected
        Button cases_button = findViewById(R.id.cases_button);
        Button deaths_button = findViewById(R.id.deaths_button);
        Button line_chart_button = findViewById((R.id.line_chart_button));

        // Start the Async task to fetch case data from CDC JSON, then toggle buttons
        cases_button.setVisibility(View.GONE);
        cases_button.setOnClickListener(v -> {
            fetchData fetch = new fetchData(data);
            fetch.execute(select_state, "tot_cases");

            cases_button.setEnabled(false);
            deaths_button.setEnabled(true);
        });

        // Start the Async task to fetch deaths data from CDC JSON, then toggle buttons
        deaths_button.setVisibility(View.GONE);
        deaths_button.setOnClickListener(v -> {
            fetchData fetch = new fetchData(data);
            fetch.execute(select_state, "tot_death");

            cases_button.setEnabled(true);
            deaths_button.setEnabled(false);
        });

        // Button to start line chart activity, showing the progression of cases/deaths in state
        line_chart_button.setVisibility(View.GONE);
        line_chart_button.setOnClickListener(v -> {
            Intent intent = new Intent(this, LineChartPage.class);
            startActivity(intent);
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        Button deaths_button = findViewById(R.id.deaths_button);
        Button cases_button = findViewById(R.id.cases_button);
        Button line_chart_button = findViewById((R.id.line_chart_button));

        // If no state is selected/state is deselected, display_data is cleared and buttons hidden
        if (pos == 0) {
            select_state = "";

            data.setText("");
            state_display.setText("");

            cases_button.setVisibility(View.GONE);
            deaths_button.setVisibility(View.GONE);
            line_chart_button.setVisibility(View.GONE);

            return;
        }

        // Otherwise, buttons are set visible, and data is fetched with cases as the default query
        cases_button.setVisibility(View.VISIBLE);
        deaths_button.setVisibility(View.VISIBLE);
        line_chart_button.setVisibility(View.VISIBLE);

        // Since cases are the default option, cases button is disabled and deaths are enabled
        cases_button.setEnabled(false);
        deaths_button.setEnabled(true);

        Spinner states = findViewById(R.id.states_spinner);
        select_state = states.getSelectedItem().toString();
        state_display.setText(stateMapping.getFullState(select_state));

        fetchData fetch = new fetchData(data);
        fetch.execute(select_state, "tot_cases");

        // Show what was selected from the spinner using a toast
        Toast.makeText(parent.getContext(), "Selected: " + stateMapping.getFullState(select_state), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Nothing to do here
    }

}