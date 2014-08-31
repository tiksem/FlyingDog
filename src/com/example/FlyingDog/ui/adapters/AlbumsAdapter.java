package com.example.FlyingDog.ui.adapters;

import android.content.Context;
import com.example.FlyingDog.R;

/**
 * Created by CM on 8/31/2014.
 */
public class AlbumsAdapter extends ArtCollectionAdapter {
    public AlbumsAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.album_item;
    }
}
