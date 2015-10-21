package com.example.FlyingDog.ui.fragments;

import android.app.Activity;
import com.tiksem.media.data.Audio;
import com.utilsframework.android.fragments.Fragments;

import java.util.List;

/**
 * Created by stykhonenko on 21.10.15.
 */
public class AlbumSongsFragment extends SongsFragment {
    private static final String ALBUM_ID = "ALBUM_ID";
    private long albumId;

    public static AlbumSongsFragment create(long albumId) {
        return Fragments.createFragmentWith1Arg(new AlbumSongsFragment(), ALBUM_ID, albumId);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        albumId = getArguments().getLong(ALBUM_ID);
    }

    @Override
    protected List<Audio> getSongs() {
        return audioDataBase.getSongsOfAlbum(audioDataBase.getAlbumById(albumId));
    }
}
