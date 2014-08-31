package com.example.FlyingDog.ui.adapters;

import android.content.Context;
import com.example.FlyingDog.R;

/**
 * Created by CM on 8/31/2014.
 */
public class ArtistsAdapter extends ArtCollectionAdapter {
    public ArtistsAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.artist_item;
    }
}
