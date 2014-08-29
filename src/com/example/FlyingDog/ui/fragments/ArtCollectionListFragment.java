package com.example.FlyingDog.ui.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.example.FlyingDog.R;
import com.example.FlyingDog.ui.adapters.ArtCollectionAdapter;
import com.example.FlyingDog.ui.adapters.holders.ArtCollectionHolder;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.data.ArtCollection;
import com.tiksem.media.data.Audio;
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
    protected ViewArrayAdapter createAdapter() {
        return new ArtCollectionAdapter(getActivity());
    }
}
