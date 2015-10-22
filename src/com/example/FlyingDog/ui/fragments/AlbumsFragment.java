package com.example.FlyingDog.ui.fragments;

import com.example.FlyingDog.network.RequestManager;
import com.example.FlyingDog.ui.Level;
import com.example.FlyingDog.ui.adapters.AlbumsAdapter;
import com.tiksem.media.data.Album;
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
    protected List<Album> createList() {
        return audioDataBase.getAlbums();
    }

    @Override
    protected void onListItemClicked(Album item, int position) {
        AlbumSongsFragment fragment = AlbumSongsFragment.create(item.getId());
        getPlayListsActivity().replaceFragment(fragment, Level.ALBUM_SONGS);
    }
}
