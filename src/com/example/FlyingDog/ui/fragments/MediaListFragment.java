package com.example.FlyingDog.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.view.Menu;
import com.example.FlyingDog.FlyingDog;
import com.example.FlyingDog.OnMediaDataSetChanged;
import com.example.FlyingDog.ui.menu.FlyingDogMenuUtils;
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setHasOptionsMenu(true);

        FlyingDog.getInstance().setOnMediaDataSetChanged(new OnMediaDataSetChanged() {
            @Override
            public void onDataSetChanged() {
                onMediaListDataSetChanged();
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        DifferentlySortable sortable = null;
        if(mediaData instanceof DifferentlySortable){
            sortable = (DifferentlySortable)mediaData;
        }

        FlyingDogMenuUtils.setupMenuForDataList(sortable, getActivity(), menu, dataClass, new OnMediaDataSetChanged() {
            @Override
            public void onDataSetChanged() {
                onMediaListDataSetChanged();
            }
        });

        super.onPrepareOptionsMenu(menu);
    }

    protected void notifyMediaDataChanged(Class aClass, List mediaData) {
        dataClass = aClass;
        this.mediaData = mediaData;
        getActivity().invalidateOptionsMenu();
    }

    protected abstract void onMediaListDataSetChanged();
}
