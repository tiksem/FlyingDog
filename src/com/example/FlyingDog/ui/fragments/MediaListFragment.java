package com.example.FlyingDog.ui.fragments;

import android.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import com.example.FlyingDog.R;
import com.example.FlyingDog.ui.menu.SortingMenuUtils;
import com.utils.framework.collections.DifferentlySortable;

import java.util.Collections;
import java.util.List;

/**
 * User: Tikhonenko.S
 * Date: 26.08.14
 * Time: 18:28
 */
public abstract class MediaListFragment extends Fragment {
    private Class dataClass = Object.class;
    private List mediaData = Collections.singletonList(null);

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        DifferentlySortable sortable = null;
        if(mediaData instanceof DifferentlySortable){
            sortable = (DifferentlySortable)mediaData;
        }

        SortingMenuUtils.setupMenuForDataList(sortable, menu, dataClass, new SortingMenuUtils.SortingChanged() {
            @Override
            public void onSortingChanged() {
                onSortingModeChanged();
            }
        });

        super.onPrepareOptionsMenu(menu);
    }

    protected void notifyMediaDataChanged(Class aClass, List mediaData) {
        dataClass = aClass;
        this.mediaData = mediaData;
        getActivity().invalidateOptionsMenu();
    }

    protected abstract void onSortingModeChanged();
}
