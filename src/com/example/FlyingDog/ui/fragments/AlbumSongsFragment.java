package com.example.FlyingDog.ui.fragments;

import android.app.Activity;
import com.example.FlyingDog.network.RequestManager;
import com.tiksem.media.data.Album;
import com.tiksem.media.data.Audio;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.fragments.Fragments;

import java.util.List;

/**
 * Created by stykhonenko on 21.10.15.
 */
public class AlbumSongsFragment extends SongsOfFragment {
    private static final String ALBUM = "ALBUM";
    private Album album;

    public static AlbumSongsFragment create(Album album) {
        return Fragments.createFragmentWith1Arg(new AlbumSongsFragment(), ALBUM, album);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        album = getArguments().getParcelable(ALBUM);
    }

    @Override
    protected List<Audio> getLocalSongs() {
        return audioDataBase.getSongsOfAlbum(album);
    }

    @Override
    protected NavigationList<Audio> getAudiosFromInternet(String filter, RequestManager requestManager) {
        return requestManager.getAudiosOfAlbum(album);
    }

    @Override
    protected boolean alwaysGetListFromInternet() {
        return !album.isLocal();
    }
}
