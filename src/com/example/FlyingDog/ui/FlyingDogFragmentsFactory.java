package com.example.FlyingDog.ui;

import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.TextView;
import com.example.FlyingDog.R;
import com.example.FlyingDog.ui.fragments.*;
import com.tiksem.media.data.Artist;
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

    private PlayListsActivity activity;

    public FlyingDogFragmentsFactory(PlayListsActivity activity) {
        this.activity = activity;
    }

    @Override
    public AbstractAudioDataFragment createFragmentBySelectedItem(int selectedItemId,
                                                                 int tabIndex, int navigationLevel) {
        if (navigationLevel == 0) {
            PlayListMode mode = PlayListMode.values()[tabIndex];
            switch (mode) {
                case ALL_SONGS:
                    return new AllSongsFragment();
                case ARTISTS:
                    return new ArtistsFragment();
                case ALBUMS:
                    return new AlbumsFragment();
                case PLAYLISTS:
                    return new PlayListsFragment();
            }
        } else if (navigationLevel == Level.ARTIST_SONGS_AND_ALBUMS) {
            ArtistProvider artistProvider = (ArtistProvider) activity.getCurrentFragment();
            Artist artist = artistProvider.getArtist();

            if (tabIndex == ARTIST_SONGS_TAB) {
                return ArtistSongsFragment.create(artist);
            } else if(tabIndex == ARTIST_ALBUMS_TAB) {
                return ArtistAlbumsFragment.create(artist);
            }
        }

        throw new RuntimeException("Invalid fragment request");
    }

    @Override
    public void initTab(int currentSelectedItem, int tabIndex, int navigationLevel, TabsAdapter.Tab tab) {
        if (navigationLevel == 0) {
            TabLayout.Tab tabView = (TabLayout.Tab) tab.getTabHandler();
            View view = View.inflate(activity, R.layout.tab, null);
            TextView title = (TextView) view.findViewById(R.id.title);
            tabView.setCustomView(view);

            PlayListMode mode = PlayListMode.values()[tabIndex];
            switch (mode) {
                case ALL_SONGS:
                    title.setText(R.string.all_songs);
                    break;
                case ARTISTS:
                    title.setText(R.string.artists);
                    break;
                case ALBUMS:
                    title.setText(R.string.albums);
                    break;
                case PLAYLISTS:
                    title.setText(R.string.play_lists);
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