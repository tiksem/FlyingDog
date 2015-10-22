package com.example.FlyingDog.ui.fragments;

import com.example.FlyingDog.R;
import com.example.FlyingDog.ui.adapters.ArtistsAdapter;
import com.tiksem.media.data.ArtCollection;
import com.tiksem.media.data.Artist;
import com.utilsframework.android.adapters.ViewArrayAdapter;

import java.util.List;

/**
 * Created by stykhonenko on 21.10.15.
 */
public abstract class ArtCollectionFragment<T extends ArtCollection> extends AbstractPlayListFragment<T> {
    @Override
    protected int getRootLayout() {
        return R.layout.play_grid_fragment;
    }
}
