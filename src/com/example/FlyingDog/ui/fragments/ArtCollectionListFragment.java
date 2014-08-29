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
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.data.ArtCollection;
import com.tiksem.media.data.Audio;

import java.util.List;

/**
 * Created by CM on 8/29/2014.
 */
public abstract class ArtCollectionListFragment<T extends ArtCollection> extends MediaListFragment {
    private ArtCollectionAdapter adapter;
    private GridView gridView;
    private List<T> artCollectionList;
    private Class<T> dataType;
    private AudioDataManager audioDataManager;

    protected ArtCollectionListFragment(List<T> artCollectionList, Class<T> dataType,
                                        AudioDataManager audioDataManager) {
        this.artCollectionList = artCollectionList;
        this.dataType = dataType;
        this.audioDataManager = audioDataManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.art_list_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        gridView = (GridView) view.findViewById(R.id.grid);
        adapter = new ArtCollectionAdapter(getActivity());
        adapter.setElements((List<ArtCollection>) artCollectionList);
        gridView.setAdapter(adapter);
        notifyMediaDataChanged(dataType, artCollectionList);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                T selected = artCollectionList.get(position);
                onItemSelected(selected, position);
            }
        });
    }

    @Override
    protected void onSortingModeChanged() {
        adapter.notifyDataSetChanged();
    }

    protected abstract Fragment createFragment(AudioDataManager audioDataManager, T artCollection);

    protected void onItemSelected(T artCollection, int position) {
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment fragment = createFragment(audioDataManager, artCollection);
        transaction.replace(R.id.play_list_fragment_container, fragment);
        transaction.addToBackStack(getClass().getCanonicalName());
        transaction.commit();
    }
}
