package com.example.FlyingDog.ui.fragments;

import android.app.Fragment;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.data.Album;

import java.util.List;

/**
 * Created by CM on 8/29/2014.
 */
public class AlbumListFragment extends ArtCollectionListFragment<Album> {
    public AlbumListFragment(List<Album> albums, AudioDataManager audioDataManager) {
        super(albums, Album.class, audioDataManager);
    }

    @Override
    protected Fragment createChildFragment(AudioDataManager audioDataManager, Album album) {
        return new AudioListFragment(album);
    }
}
