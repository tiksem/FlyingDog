package com.example.FlyingDog.ui.fragments;

import android.app.Activity;
import com.tiksem.media.data.Album;
import com.tiksem.media.data.Audio;
import com.utilsframework.android.fragments.Fragments;

import java.util.List;

/**
 * Created by stykhonenko on 21.10.15.
 */
public class ArtistAlbumsFragment extends AlbumsFragment implements ArtistIdProvider {
    private static final String ARTIST_ID = "ARTIST_ID";
    private long artistId;

    public static ArtistAlbumsFragment create(long artistId) {
        return Fragments.createFragmentWith1Arg(new ArtistAlbumsFragment(), ARTIST_ID, artistId);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        artistId = getArguments().getLong(ARTIST_ID);
    }

    @Override
    public long getArtistsId() {
        return artistId;
    }

    @Override
    protected List<Album> createList() {
        return audioDataBase.getAlbumsOfArtist(audioDataBase.getArtistById(artistId));
    }
}
