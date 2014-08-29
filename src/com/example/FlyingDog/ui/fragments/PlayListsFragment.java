package com.example.FlyingDog.ui.fragments;

import android.app.Fragment;
import com.example.FlyingDog.R;
import com.example.FlyingDog.ui.adapters.PlayListsAdapter;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.data.PlayList;
import com.utilsframework.android.adapters.ViewArrayAdapter;

import java.util.List;

/**
 * Created by CM on 8/29/2014.
 */
public class PlayListsFragment extends MediaContainerListFragment<PlayList>{
    public PlayListsFragment(List<PlayList> playLists,
                             AudioDataManager audioDataManager) {
        super(playLists, PlayList.class, audioDataManager);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.play_lists_fragment;
    }

    @Override
    protected int getListViewId() {
        return R.id.list_view;
    }

    @Override
    protected ViewArrayAdapter<PlayList, ?> createAdapter(AudioDataManager audioDataManager) {
        return new PlayListsAdapter(getActivity(), audioDataManager);
    }

    @Override
    protected Fragment createChildFragment(AudioDataManager audioDataManager, PlayList media) {
        return new AudioListFragment(media);
    }
}
