package com.example.FlyingDog.ui.fragments;

import android.app.Fragment;
import android.widget.BaseAdapter;
import com.example.FlyingDog.FlyingDog;
import com.example.FlyingDog.ui.adapters.ArtistsAdapter;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.MediaUpdatingService;
import com.tiksem.media.data.Artist;
import com.utilsframework.android.adapters.ViewArrayAdapter;

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

    @Override
    public void onStart() {
        super.onStart();
        FlyingDog.getInstance().setOnArtistArtUpdated(new MediaUpdatingService.OnArtistArtUpdated() {
            @Override
            public void onArtistArtUpdated(Artist artist) {
                notifyAdapterDataSetChanged();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        FlyingDog.getInstance().setOnArtistArtUpdated(null);
    }

    @Override
    protected ViewArrayAdapter createAdapter(AudioDataManager audioDataManager) {
        return new ArtistsAdapter(getActivity());
    }
}
