package com.example.FlyingDog.ui.fragments;

import com.example.FlyingDog.R;
import com.example.FlyingDog.network.RequestManager;
import com.example.FlyingDog.ui.Level;
import com.example.FlyingDog.ui.adapters.ArtistsAdapter;
import com.tiksem.media.data.Artist;
import com.utils.framework.collections.NavigationList;
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
    protected List<Artist> createLocalList() {
        return audioDataBase.getArtists();
    }

    @Override
    protected NavigationList<Artist> createInternetList(String filter) {
        return getRequestManager().searchArtists(filter);
    }

    @Override
    protected void onItemSelected(Artist item, int position) {
        ArtistSongsFragment fragment = ArtistSongsFragment.create(item);
        getPlayListsActivity().replaceFragment(fragment, Level.ARTIST_SONGS_AND_ALBUMS);
    }

    @Override
    protected boolean hasSearchMenu() {
        return true;
    }

    @Override
    protected CharSequence getEmptyListText() {
        return getString(R.string.no_artists_found);
    }
}
