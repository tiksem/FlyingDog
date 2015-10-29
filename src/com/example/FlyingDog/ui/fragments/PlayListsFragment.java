package com.example.FlyingDog.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.example.FlyingDog.R;
import com.example.FlyingDog.network.RequestManager;
import com.example.FlyingDog.ui.Level;
import com.example.FlyingDog.ui.PlayListsActivity;
import com.example.FlyingDog.ui.adapters.PlayListsAdapter;
import com.tiksem.media.data.Audio;
import com.tiksem.media.data.PlayList;
import com.tiksem.media.data.PlayLists;
import com.utilsframework.android.adapters.ViewArrayAdapter;
import com.utilsframework.android.threading.OnFinish;
import com.utilsframework.android.threading.ThrowingRunnable;
import com.utilsframework.android.view.Alerts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stykhonenko on 27.10.15.
 */
public class PlayListsFragment extends AbstractAudioDataFragment<PlayList> {
    private void createPlayList(final String playListName) {
        getRequestManager().execute(new ThrowingRunnable<IOException>() {
            @Override
            public void run() throws IOException {
                audioDataBase.addPlayList(playListName);
            }
        }, new OnFinish<IOException>() {
            @Override
            public void onFinish(IOException e) {
                updateNavigationListWithLastFilter();
            }
        });
    }

    private void showAddPlayListDialog() {
        Alerts.showAlertWithInput(getActivity(), R.string.add_playlist_message, new Alerts.OnInputOk() {
            @Override
            public void onOk(String playListName) {
                createPlayList(playListName);
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPlayListDialog();
            }
        });
    }

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

    @Override
    protected int getRootLayout() {
        return R.layout.playlists_fragment;
    }
}
