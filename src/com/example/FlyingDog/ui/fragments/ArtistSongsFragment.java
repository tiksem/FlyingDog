package com.example.FlyingDog.ui.fragments;

import android.app.Activity;
import com.tiksem.media.data.Audio;
import com.utilsframework.android.fragments.Fragments;

import java.util.List;

/**
 * Created by stykhonenko on 21.10.15.
 */
public class ArtistSongsFragment extends SongsFragment implements ArtistIdProvider {
    private static final String ARTIST_ID = "ARTIST_ID";
    private long artistId;

    public static ArtistSongsFragment create(long artistId) {
        return Fragments.createFragmentWith1Arg(new ArtistSongsFragment(), ARTIST_ID, artistId);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        artistId = getArguments().getLong(ARTIST_ID);
    }

    @Override
    protected List<Audio> getSongs() {
        return audioDataBase.getSongsOfArtist(audioDataBase.getArtistById(artistId));
    }

    @Override
    public long getArtistsId() {
        return artistId;
    }
}
