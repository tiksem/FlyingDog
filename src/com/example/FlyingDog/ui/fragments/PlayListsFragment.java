package com.example.FlyingDog.ui.fragments;

import com.example.FlyingDog.network.RequestManager;
import com.example.FlyingDog.ui.Level;
import com.example.FlyingDog.ui.adapters.PlayListsAdapter;
import com.tiksem.media.data.Audio;
import com.tiksem.media.data.PlayList;
import com.utilsframework.android.adapters.ViewArrayAdapter;

import java.util.List;

/**
 * Created by stykhonenko on 27.10.15.
 */
public class PlayListsFragment extends AbstractAudioDataFragment<PlayList> {
    @Override
    protected List<PlayList> createList() {
        return audioDataBase.getPlayLists();
    }

    @Override
    protected ViewArrayAdapter<PlayList, ?> createAdapter(RequestManager requestManager) {
        return new PlayListsAdapter(getActivity(), new PlayListsAdapter.AudiosProvider() {
            @Override
            public List<Audio> getAudiosOfPlayList(PlayList playList) {
                return audioDataBase.getSongsOfPlayList(playList);
            }
        });
    }

    @Override
    protected void onListItemClicked(PlayList item, int position) {
        getPlayListsActivity().replaceFragment(PlayListSongsFragment.create(item), Level.PLAYLIST_SONGS);
    }
}
