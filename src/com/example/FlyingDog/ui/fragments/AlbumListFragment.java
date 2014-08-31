package com.example.FlyingDog.ui.fragments;

import android.app.Fragment;
import com.example.FlyingDog.FlyingDog;
import com.example.FlyingDog.ui.adapters.AlbumsAdapter;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.MediaUpdatingService;
import com.tiksem.media.data.Album;
import com.tiksem.media.data.Artist;
import com.tiksem.media.data.Audio;
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
        FlyingDog flyingDog = FlyingDog.getInstance();
        flyingDog.setOnAlbumArtUpdated(new MediaUpdatingService.OnAlbumArtUpdated() {
            @Override
            public void onAlbumArtUpdated(Album album) {
                onMediaListDataSetChanged();
            }
        });
        flyingDog.setOnAlbumOfAudioUpdated(new MediaUpdatingService.OnAlbumOfAudioUpdated() {
            @Override
            public void onUpdateFinished(Album album, Audio audio) {
                onMediaListDataSetChanged();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        FlyingDog flyingDog = FlyingDog.getInstance();
        flyingDog.setOnAlbumArtUpdated(null);
        flyingDog.setOnAlbumOfAudioUpdated(null);
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
