package com.tiksem.FlyingDog.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import com.tiksem.FlyingDog.R;
import com.tiksem.media.data.ArtCollection;

/**
 * Created by CM on 8/31/2014.
 */
public class ArtCollectionSpinnerAdapter<T extends ArtCollection> extends ArtCollectionAdapter<T> {
    private String nullItemText;

    public ArtCollectionSpinnerAdapter(Context context, String nullItemText) {
        super(context);
        this.nullItemText = nullItemText;
    }

    @Override
    protected int getRootLayoutId(int viewType) {
        return R.layout.art_collection_spinner;
    }

    @Override
    protected int getNullLayoutId() {
        return R.layout.spinner_text_view;
    }

    @Override
    protected void reuseNullView(int position, View convertView) {
        TextView textView = (TextView) convertView;
        textView.setTextColor(Color.rgb(255, 0, 0));
        textView.setText(nullItemText);
    }
}
