package com.example.FlyingDog.ui.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import com.example.FlyingDog.R;
import com.tiksem.media.AudioDataManager;
import com.utilsframework.android.adapters.ViewArrayAdapter;

import java.util.List;

/**
 * Created by CM on 8/29/2014.
 */
public abstract class MediaContainerListFragment<T> extends MediaListFragment {
    private ViewArrayAdapter<T, ? extends Object> adapter;
    private AbsListView listView;
    private List<T> mediaList;
    private Class<T> dataType;
    private AudioDataManager audioDataManager;

    protected MediaContainerListFragment(List<T> artCollectionList, Class<T> dataType,
                                        AudioDataManager audioDataManager) {
        this.mediaList = artCollectionList;
        this.dataType = dataType;
        this.audioDataManager = audioDataManager;
    }

    protected abstract int getLayoutId();
    protected abstract int getListViewId();
    protected abstract ViewArrayAdapter<T, ? extends Object> createAdapter(AudioDataManager audioDataManager);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        listView = (AbsListView) view.findViewById(getListViewId());
        adapter = createAdapter(audioDataManager);
        adapter.setElements(mediaList);
        listView.setAdapter(adapter);
        notifyMediaDataChanged(dataType, mediaList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                T selected = mediaList.get(position);
                onItemSelected(selected, position);
            }
        });
    }

    @Override
    protected void onSortingModeChanged() {
        adapter.notifyDataSetChanged();
    }

    protected abstract Fragment createChildFragment(AudioDataManager audioDataManager, T media);

    protected void onItemSelected(T media, int position) {
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment fragment = createChildFragment(audioDataManager, media);
        transaction.replace(R.id.play_list_fragment_container, fragment);
        transaction.addToBackStack(getClass().getCanonicalName());
        transaction.commit();
    }
}
