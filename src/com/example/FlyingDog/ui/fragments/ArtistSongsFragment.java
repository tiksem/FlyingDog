package com.example.FlyingDog.ui.fragments;

import android.app.Activity;
import com.example.FlyingDog.network.RequestManager;
import com.tiksem.media.data.Artist;
import com.tiksem.media.data.Audio;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.fragments.Fragments;

import java.util.List;

/**
 * Created by stykhonenko on 21.10.15.
 */
public class ArtistSongsFragment extends SongsOfFragment implements ArtistProvider {
    private static final String ARTIST = "ARTIST";
    private Artist artist;

    public static ArtistSongsFragment create(Artist artist) {
        return Fragments.createFragmentWith1Arg(new ArtistSongsFragment(), ARTIST, artist);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        artist = getArguments().getParcelable(ARTIST);
    }

    @Override
    protected List<Audio> getLocalSongs() {
        return audioDataBase.getSongsOfArtist(artist);
    }

    @Override
    protected NavigationList<Audio> getAudiosFromInternet(String filter, RequestManager requestManager) {
        return requestManager.getAudiosOfArtist(artist, filter);
    }

    @Override
    public Artist getArtist() {
        return artist;
    }

    @Override
    protected boolean alwaysUseNavigationList() {
        return !artist.isLocal();
    }

    @Override
    protected boolean hasSearchMenu() {
        return true;
    }
}
