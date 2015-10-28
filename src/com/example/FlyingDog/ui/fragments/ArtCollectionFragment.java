package com.example.FlyingDog.ui.fragments;

import com.example.FlyingDog.R;
import com.tiksem.media.data.ArtCollection;

/**
 * Created by stykhonenko on 21.10.15.
 */
public abstract class ArtCollectionFragment<T extends ArtCollection> extends AbstractAudioDataFragment<T> {
    @Override
    protected int getRootLayout() {
        return R.layout.play_grid_fragment;
    }
}
