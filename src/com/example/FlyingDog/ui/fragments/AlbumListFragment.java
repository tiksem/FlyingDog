package com.example.FlyingDog.ui.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import com.example.FlyingDog.R;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.data.Album;
import com.tiksem.media.data.Audio;

import java.util.List;

/**
 * Created by CM on 8/29/2014.
 */
public class AlbumListFragment extends ArtCollectionListFragment<Album> {
    public AlbumListFragment(List<Album> albums, AudioDataManager audioDataManager) {
        super(albums, Album.class, audioDataManager);
    }

    @Override
    protected Fragment createFragment(AudioDataManager audioDataManager, Album album) {
        return new PlayListFragment(album);
    }
}
