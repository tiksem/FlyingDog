package com.example.FlyingDog.ui.fragments;

import android.app.Activity;
import com.example.FlyingDog.network.RequestManager;
import com.tiksem.media.data.Audio;
import com.tiksem.media.data.PlayList;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.fragments.Fragments;

import java.util.List;

/**
 * Created by stykhonenko on 28.10.15.
 */
public class PlayListSongsFragment extends SongsOfFragment {
    private static final String PLAYLIST = "PLAYLIST";

    private PlayList playList;

    public static PlayListSongsFragment create(PlayList playList) {
        return Fragments.createFragmentWith1Arg(new PlayListSongsFragment(), PLAYLIST, playList);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        playList = getArguments().getParcelable(PLAYLIST);
    }

    @Override
    protected List<Audio> getLocalSongs() {
        return audioDataBase.getSongsOfPlayList(playList);
    }

    @Override
    protected NavigationList<Audio> getAudiosFromInternet(String filter, RequestManager requestManager) {
        throw new UnsupportedOperationException();
    }
}
