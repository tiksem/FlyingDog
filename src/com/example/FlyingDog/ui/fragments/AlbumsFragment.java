package com.example.FlyingDog.ui.fragments;

import com.example.FlyingDog.network.RequestManager;
import com.example.FlyingDog.ui.Level;
import com.example.FlyingDog.ui.adapters.AlbumsAdapter;
import com.tiksem.media.data.Album;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.adapters.ViewArrayAdapter;
import com.utilsframework.android.network.AsyncRequestExecutorManager;

import java.util.List;

/**
 * Created by stykhonenko on 21.10.15.
 */
public class AlbumsFragment extends ArtCollectionFragment<Album> {
    @Override
    protected ViewArrayAdapter<Album, ?> createAdapter(RequestManager executorManager) {
        return new AlbumsAdapter(getActivity());
    }

    @Override
    protected List<Album> createLocalList() {
        return audioDataBase.getAlbums();
    }

    @Override
    protected void onListItemClicked(Album item, int position) {
        super.onListItemClicked(item, position);
        AlbumSongsFragment fragment = AlbumSongsFragment.create(item);
        getPlayListsActivity().replaceFragment(fragment, Level.ALBUM_SONGS);
    }

    @Override
    protected boolean hasSearchMenu() {
        return true;
    }

    @Override
    protected NavigationList<Album> createInternetList(String filter) {
        return getRequestManager().searchAlbums(filter);
    }
}
