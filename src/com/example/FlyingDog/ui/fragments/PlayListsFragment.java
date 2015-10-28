package com.example.FlyingDog.ui.fragments;

import com.example.FlyingDog.network.RequestManager;
import com.example.FlyingDog.ui.Level;
import com.example.FlyingDog.ui.PlayListsActivity;
import com.example.FlyingDog.ui.adapters.PlayListsAdapter;
import com.tiksem.media.data.Audio;
import com.tiksem.media.data.PlayList;
import com.tiksem.media.data.PlayLists;
import com.utilsframework.android.adapters.ViewArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stykhonenko on 27.10.15.
 */
public class PlayListsFragment extends AbstractAudioDataFragment<PlayList> {
    @Override
    protected List<PlayList> createList() {
        List<PlayList> localPlayLists = audioDataBase.getPlayLists();
        List<PlayList> result = new ArrayList<>(PlayLists.getSpecialPlayLists());
        result.addAll(localPlayLists);
        return result;
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
        PlayListsActivity activity = getPlayListsActivity();
        if (item.isLocal()) {
            activity.replaceFragment(PlayListSongsFragment.create(item), Level.PLAYLIST_SONGS);
        } else {
            if (item.getId() == PlayLists.SONGS_YOU_MAY_LIKE_PLAYLIST_ID) {
                activity.replaceFragment(new SongsYouMayLikeFragment(), Level.PLAYLIST_SONGS);
            } else {
                throw new UnsupportedOperationException("Unsupported internet playlist");
            }
        }
    }
}
