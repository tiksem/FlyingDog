package com.example.FlyingDog.ui.fragments;

import com.example.FlyingDog.network.RequestManager;
import com.example.FlyingDog.ui.adapters.PlayListsAdapter;
import com.tiksem.media.data.Audio;
import com.tiksem.media.data.PlayList;
import com.utilsframework.android.adapters.ViewArrayAdapter;

import java.util.List;

/**
 * Created by stykhonenko on 29.10.15.
 */
public abstract class AbstractPlayListsFragment extends AbstractAudioDataFragment<PlayList> {
    @Override
    protected ViewArrayAdapter<PlayList, ?> createAdapter(RequestManager requestManager) {
        return new PlayListsAdapter(getActivity(), getItemBackground(), new PlayListsAdapter.AudiosProvider() {
            @Override
            public List<Audio> getAudiosOfPlayList(PlayList playList) {
                return audioDataBase.getSongsOfPlayList(playList);
            }
        });
    }

    protected int getItemBackground() {
        return 0;
    }
}
