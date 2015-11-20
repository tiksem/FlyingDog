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
    public ArtCollectionSpinnerAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getRootLayoutId(int viewType) {
        return R.layout.art_collection_spinner;
    }
}
