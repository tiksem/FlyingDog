package com.example.FlyingDog.ui.fragments;

import android.app.Activity;
import com.tiksem.media.data.Album;
import com.tiksem.media.data.Artist;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.fragments.Fragments;

import java.util.List;

/**
 * Created by stykhonenko on 21.10.15.
 */
public class ArtistAlbumsFragment extends AlbumsFragment implements ArtistProvider {
    private static final String ARTIST = "ARTIST";
    private Artist artist;

    public static ArtistAlbumsFragment create(Artist artist) {
        return Fragments.createFragmentWith1Arg(new ArtistAlbumsFragment(), ARTIST, artist);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        artist = getArguments().getParcelable(ARTIST);
    }

    @Override
    public Artist getArtist() {
        return artist;
    }

    @Override
    protected List<Album> createLocalList() {
        return audioDataBase.getAlbumsOfArtist(artist);
    }

    @Override
    protected NavigationList<Album> createInternetList(String filter) {
        return getRequestManager().getAlbumsOfArtist(artist);
    }

    @Override
    protected boolean alwaysUseNavigationList() {
        return !artist.isLocal();
    }
}
