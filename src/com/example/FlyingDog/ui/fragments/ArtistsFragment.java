package com.example.FlyingDog.ui.fragments;

import com.example.FlyingDog.network.RequestManager;
import com.example.FlyingDog.ui.Level;
import com.example.FlyingDog.ui.adapters.ArtistsAdapter;
import com.tiksem.media.data.Artist;
import com.utilsframework.android.adapters.ViewArrayAdapter;
import com.utilsframework.android.network.AsyncRequestExecutorManager;

import java.util.List;

/**
 * Created by stykhonenko on 19.10.15.
 */
public class ArtistsFragment extends ArtCollectionFragment<Artist> {
    @Override
    protected ViewArrayAdapter<Artist, ?> createAdapter(RequestManager executorManager) {
        return new ArtistsAdapter(getActivity());
    }

    @Override
    protected List<Artist> createList() {
        return audioDataBase.getArtists();
    }

    @Override
    protected void onListItemClicked(Artist item, int position) {
        ArtistSongsFragment fragment = ArtistSongsFragment.create(item.getId());
        getPlayListsActivity().replaceFragment(fragment, Level.ARTIST_SONGS_AND_ALBUMS);
    }
}
