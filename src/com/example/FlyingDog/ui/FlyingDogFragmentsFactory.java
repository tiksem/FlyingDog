package com.example.FlyingDog.ui;

import android.support.v4.app.Fragment;
import com.example.FlyingDog.R;
import com.example.FlyingDog.ui.fragments.*;
import com.utilsframework.android.navdrawer.FragmentFactory;
import com.utilsframework.android.navdrawer.NavigationActivityInterface;
import com.utilsframework.android.navdrawer.TabsAdapter;

/**
 * Created by stykhonenko on 19.10.15.
 */
public class FlyingDogFragmentsFactory implements FragmentFactory {
    private static final int ARTIST_TABS_COUNT = 2;
    private static final int ARTIST_SONGS_TAB = 0;
    private static final int ARTIST_ALBUMS_TAB = 1;

    private NavigationActivityInterface activityInterface;

    public FlyingDogFragmentsFactory(NavigationActivityInterface activityInterface) {
        this.activityInterface = activityInterface;
    }

    @Override
    public AbstractPlayListFragment createFragmentBySelectedItem(int selectedItemId,
                                                                 int tabIndex, int navigationLevel) {
        if (navigationLevel == 0) {
            PlayListMode mode = PlayListMode.values()[tabIndex];
            switch (mode) {
                case ALL_SONGS:
                    return new SongsFragment();
                case ARTISTS:
                    return new ArtistsFragment();
                case ALBUMS:
                    return new AlbumsFragment();
            }
        } else if (navigationLevel == Level.ARTIST_SONGS_AND_ALBUMS) {
            ArtistIdProvider artistIdProvider = (ArtistIdProvider) activityInterface.getCurrentFragment();
            long artistId = artistIdProvider.getArtistsId();

            if (tabIndex == ARTIST_SONGS_TAB) {
                return ArtistSongsFragment.create(artistId);
            } else if(tabIndex == ARTIST_ALBUMS_TAB) {
                return ArtistAlbumsFragment.create(artistId);
            }
        }

        throw new RuntimeException("Invalid fragment request");
    }

    @Override
    public void initTab(int currentSelectedItem, int tabIndex, int navigationLevel, TabsAdapter.Tab tab) {
        if (navigationLevel == 0) {
            PlayListMode mode = PlayListMode.values()[tabIndex];
            switch (mode) {
                case ALL_SONGS:
                    tab.setText(R.string.all_songs);
                    break;
                case ARTISTS:
                    tab.setText(R.string.artists);
                    break;
                case ALBUMS:
                    tab.setText(R.string.albums);
                    break;
            }
        } else if(navigationLevel == Level.ARTIST_SONGS_AND_ALBUMS) {
            if (tabIndex == ARTIST_SONGS_TAB) {
                tab.setText(R.string.songs);
            } else if(tabIndex == ARTIST_ALBUMS_TAB) {
                tab.setText(R.string.albums);
            }
        }
    }

    @Override
    public int getTabsCount(int selectedItemId, int navigationLevel) {
        if (navigationLevel == 0) {
            return PlayListMode.values().length;
        } else if(navigationLevel == Level.ARTIST_SONGS_AND_ALBUMS) {
            return ARTIST_TABS_COUNT;
        }

        return 1;
    }
}
