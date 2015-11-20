package com.tiksem.FlyingDog.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import com.tiksem.FlyingDog.R;
import com.tiksem.media.data.NamedData;
import com.utilsframework.android.adapters.SingleViewArrayAdapter;

/**
 * Created by CM on 8/30/2014.
 */
public class NamedDataSpinnerAdapter<T extends NamedData> extends SingleViewArrayAdapter<T> {
    private String nullItemText;

    public NamedDataSpinnerAdapter(Context context, String nullItemText) {
        super(context);
        this.nullItemText = nullItemText;
    }

    @Override
    protected int getRootLayoutId(int viewType) {
        return R.layout.spinner_text_view;
    }

    @Override
    protected void reuseView(T namedData, Void aVoid, int position, View view) {
        TextView textView = (TextView) view;
        textView.setText(namedData.getName());
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
