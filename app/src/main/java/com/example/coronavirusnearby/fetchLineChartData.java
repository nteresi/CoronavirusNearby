package com.example.coronavirusnearby;

import android.graphics.Color;
import android.os.AsyncTask;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class fetchLineChartData extends AsyncTask<String, Void, Void> {

    LineData lineData;

    // Store the dates for each object in the JSON, for use with X Axis formatter
    public static ArrayList<String> dates = new ArrayList<>();

    /* Fetches new cases or new death data from CDC JSON. Also stores datetime values for use with
     * DataFormatter. Upon fetching the data, sets up the data to be used with the linechart.
     *
     * Parameters:
     * String... arg: the selected state (State Code) from spinner of Main Act, accessed at index 0
     *                the query option (new cases or new deaths) for the CDC JSON, index 1
     */
    @Override
    protected Void doInBackground(String... arg) {
        try {
            String qURL = "https://data.cdc.gov/resource/9mfq-cb36.json?$where=state=" + "%27" + arg[0] + "%27&$order=submission_date";
            URL covidDB = new URL(qURL);
            HttpURLConnection urlCon = (HttpURLConnection) covidDB.openConnection();
            InputStream inputStream = urlCon.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder data = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                data.append(line);
            }

            ArrayList<Entry> values = new ArrayList<>();

            JSONArray jsonArray = new JSONArray(data.toString());
            JSONObject jsonObject;
            for (int x = 0; x < jsonArray.length(); x++) {
                jsonObject = (JSONObject) jsonArray.get(x);
                dates.add(jsonObject.getString("submission_date"));
                values.add(new Entry(x, (float) jsonObject.getDouble(arg[1])));
            }

            String label;
            int color;
            if (arg[1].equals("new_case")) {
                label = "New Cases";
                color = Color.BLUE;
            } else {
                label = "New Deaths";
                color = Color.RED;
            }

            LineDataSet lineDataSet = new LineDataSet(values, label);
            lineDataSet.setDrawCircles(false);
            lineDataSet.setColor(color);
            List<ILineDataSet> dataSet = new ArrayList<>();
            dataSet.add(lineDataSet);
            lineData = new LineData(dataSet);

            XAxis xAxis = LineChartPage.lineChart.getXAxis();
            xAxis.setValueFormatter(new DataFormatter());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Set the linechart data and then refresh it.
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        LineChartPage.lineChart.setData(lineData);
        LineChartPage.lineChart.invalidate();
    }


}
