package com.tiksem.FlyingDog.ui.fragments;

import com.tiksem.FlyingDog.R;
import com.tiksem.FlyingDog.network.RequestManager;
import com.tiksem.FlyingDog.ui.Level;
import com.tiksem.FlyingDog.ui.adapters.AlbumsAdapter;
import com.tiksem.media.data.Album;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.adapters.ViewArrayAdapter;

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
    protected void onItemSelected(Album item, int position) {
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

    @Override
    protected CharSequence getEmptyListText() {
        return getString(R.string.no_albums_found);
    }
}
