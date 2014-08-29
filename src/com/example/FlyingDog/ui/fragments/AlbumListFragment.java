package com.example.FlyingDog.ui.fragments;

import android.app.Fragment;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.data.Album;
import com.tiksem.media.data.Artist;

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
    protected Fragment createChildFragment(AudioDataManager audioDataManager, Album album) {
        return new AudioListFragment(album);
    }

    public Artist getArtist() {
        return artist;
    }
}
