package com.example.coronavirusnearby;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class fetchData extends AsyncTask<String, Void, Void> {

    // The number of Covid cases/deaths for the selected state
    String num;

    // A weak reference to the display_data TextView in Main Activity
    private final WeakReference<TextView> data;

    public fetchData(TextView data) {
        this.data = new WeakReference<>(data);
    }

    /* Called upon selecting a state from the spinner.
     * Accesses data from CDC website and queries the JSON
     * for the state's total number of cases or deaths.
     *
     * Parameters:
     * String... arg: the selected state (State Code) from spinner of Main Act, accessed at index 0
     *                the query option (total cases or total deaths) for the CDC JSON, index 1
     *
     */
    @Override
    protected Void doInBackground(String... arg) {
        try {
            String qURL = "https://data.cdc.gov/resource/9mfq-cb36.json?$where=state=" + "%27" + arg[0] + "%27" + "&$order=submission_date DESC&$limit=1";
            URL covidDB = new URL(qURL);
            HttpURLConnection urlCon = (HttpURLConnection) covidDB.openConnection();
            InputStream inputStream = urlCon.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder data = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                data.append(line);
            }
            JSONArray jsonArray = new JSONArray(data.toString());
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            num = jsonObject.getString(arg[1]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    // Upon completing the Async task, display the number of cases/deaths in the layout.
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        TextView textView = data.get();
        textView.setText(num);
    }
}
