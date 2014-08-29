package com.example.FlyingDog.ui.fragments;

import com.example.FlyingDog.R;
import com.example.FlyingDog.ui.adapters.ArtCollectionAdapter;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.data.ArtCollection;
import com.utilsframework.android.adapters.ViewArrayAdapter;

import java.util.List;

/**
 * Created by CM on 8/29/2014.
 */
public abstract class ArtCollectionListFragment<T extends ArtCollection> extends MediaContainerListFragment<T> {
    protected ArtCollectionListFragment(List<T> artCollectionList, Class<T> dataType,
                                        AudioDataManager audioDataManager) {
        super(artCollectionList, dataType, audioDataManager);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.art_list_fragment;
    }

    @Override
    protected int getListViewId() {
        return R.id.grid;
    }

    @Override
    protected ViewArrayAdapter createAdapter(AudioDataManager audioDataManager) {
        return new ArtCollectionAdapter(getActivity());
    }
}
