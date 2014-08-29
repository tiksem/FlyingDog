package com.example.FlyingDog.ui.fragments;

import android.app.Fragment;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.data.Artist;

import java.util.List;

/**
 * Created by CM on 8/29/2014.
 */
public class ArtistListFragment extends ArtCollectionListFragment<Artist> {

    private ArtistNavigationModeProvider navigationModeProvider;

    public ArtistListFragment(List<Artist> artCollectionList,
                              AudioDataManager audioDataManager, ArtistNavigationModeProvider navigationModeProvider) {
        super(artCollectionList, Artist.class, audioDataManager);
        this.navigationModeProvider = navigationModeProvider;
    }

    @Override
    protected Fragment createChildFragment(AudioDataManager audioDataManager, Artist artist) {
        if(navigationModeProvider.getNavigationMode() == ArtistNavigationMode.SONGS){
            return new AudioListFragment(artist);
        } else {
            return new AlbumListFragment(artist, audioDataManager);
        }
    }
}
