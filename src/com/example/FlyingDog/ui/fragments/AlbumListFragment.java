package com.example.FlyingDog.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import com.example.FlyingDog.FlyingDog;
import com.example.FlyingDog.ui.adapters.AlbumsAdapter;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.MediaArtUpdatingService;
import com.tiksem.media.data.Album;
import com.tiksem.media.data.Artist;
import com.utilsframework.android.adapters.ViewArrayAdapter;

import java.util.List;

/**
 * Created by CM on 8/29/2014.
 */
public class AlbumListFragment extends ArtCollectionListFragment<Album> {
    private Artist artist;

    public AlbumListFragment(List<Album> albums, AudioDataManager audioDataManager) {
        super(albums, Album.class, audioDataManager);
    }

    public AlbumListFragment(Artist artist, AudioDataManager audioDataManager) {
        super(audioDataManager.getAlbumsOfArtist(artist), Album.class, audioDataManager);
        this.artist = artist;
    }

    @Override
    public void onStart() {
        super.onStart();
        FlyingDog.getInstance().setOnAlbumArtUpdated(new MediaArtUpdatingService.OnAlbumArtUpdated() {
            @Override
            public void onAlbumArtUpdated(Album album) {
                onMediaListDataSetChanged();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        FlyingDog.getInstance().setOnAlbumArtUpdated(null);
    }

    @Override
    protected Fragment createChildFragment(AudioDataManager audioDataManager, Album album) {
        return new AudioListFragment(album);
    }

    public Artist getArtist() {
        return artist;
    }

    @Override
    protected ViewArrayAdapter createAdapter(AudioDataManager audioDataManager) {
        return new AlbumsAdapter(getActivity());
    }
}
