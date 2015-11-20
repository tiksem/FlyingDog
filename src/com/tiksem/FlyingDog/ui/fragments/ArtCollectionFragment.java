package com.tiksem.FlyingDog.ui.fragments;

import com.tiksem.FlyingDog.R;
import com.tiksem.FlyingDog.sort.SortMenuUtils;
import com.tiksem.media.data.ArtCollection;

import java.util.List;

/**
 * Created by stykhonenko on 21.10.15.
 */
public abstract class ArtCollectionFragment<T extends ArtCollection> extends AbstractAudioDataFragment<T> {
    @Override
    protected int getRootLayout() {
        return R.layout.play_grid_fragment;
    }

    @Override
    protected int getSortMenuId() {
        return R.menu.sort_art_collections;
    }

    @Override
    protected void sort(List<T> items, int sortingOrder) {
        SortMenuUtils.sortArtCollections(items, sortingOrder);
    }
}
