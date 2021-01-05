package com.example.coronavirusnearby;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

public class DataMarkerView extends MarkerView {

    final private TextView marker;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public DataMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        marker = findViewById(R.id.marker);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        marker.setText("" + e.getY());

        // this will perform necessary layouting
        super.refreshContent(e, highlight);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }

        return mOffset;
    }
}
