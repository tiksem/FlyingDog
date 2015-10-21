package com.example.FlyingDog.ui.adapters;

import android.content.Context;
import com.example.FlyingDog.R;
import com.tiksem.media.data.Artist;

/**
 * Created by CM on 8/31/2014.
 */
public class ArtistsAdapter extends ArtCollectionAdapter<Artist> {
    public ArtistsAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getRootLayoutId(int viewType) {
        return R.layout.artist_item;
    }
}
