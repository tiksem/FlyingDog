package com.example.FlyingDog.ui;

import com.example.FlyingDog.R;
import com.example.FlyingDog.ui.fragments.*;
import com.tiksem.media.data.Artist;
import com.utilsframework.android.navdrawer.FragmentFactory;
import com.utilsframework.android.navdrawer.TabsAdapter;

/**
 * Created by stykhonenko on 19.10.15.
 */
public class FlyingDogFragmentsFactory implements FragmentFactory {
    private static final int ARTIST_TABS_COUNT = 2;
    private static final int ARTIST_SONGS_TAB = 0;
    private static final int ARTIST_ALBUMS_TAB = 1;
    private static final int TAG_TABS_COUNT = 2;
    private static final int TAG_SONGS_TAB = 0;
    private static final int TAG_ARTISTS_TAB = 1;

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
                case GENRES:
                    return new GenresFragment();
                case MOOD:
                    return new MoodFragment();
                case COUNTRY:
                    return new CountriesFragment();
            }
        } else if (navigationLevel == Level.ARTIST_SONGS_AND_ALBUMS) {
            ArtistProvider artistProvider = (ArtistProvider) activity.getCurrentFragment();
            Artist artist = artistProvider.getArtist();

            if (tabIndex == ARTIST_SONGS_TAB) {
                return ArtistSongsFragment.create(artist);
            } else if(tabIndex == ARTIST_ALBUMS_TAB) {
                return ArtistAlbumsFragment.create(artist);
            }
        } else if(navigationLevel == Level.TAG_SONGS_AND_ARTISTS) {
            TagProvider tagProvider = (TagProvider) activity.getCurrentFragment();
            String tag = tagProvider.getTagName();

            if (tabIndex == TAG_SONGS_TAB) {
                return SongsOfTagFragment.create(tag);
            } else if(tabIndex == TAG_ARTISTS_TAB) {
                return ArtistsOfTagFragment.create(tag);
            }
        }

        throw new RuntimeException("Invalid fragment request");
    }

    @Override
    public void initTab(int currentSelectedItem, int tabIndex, int navigationLevel, TabsAdapter.Tab tab) {
        if (navigationLevel == 0) {
//            TabLayout.Tab tabView = (TabLayout.Tab) tab.getTabHandler();
//            View view = View.inflate(activity, R.layout.tab, null);
//            TextView title = (TextView) view.findViewById(R.id.title);
//            tabView.setCustomView(view);

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
                case PLAYLISTS:
                    tab.setText(R.string.play_lists);
                    break;
                case GENRES:
                    tab.setText(R.string.genres);
                    break;
                case MOOD:
                    tab.setText(R.string.mood);
                    break;
                case COUNTRY:
                    tab.setText(R.string.countries);
                    break;
            }
        } else if(navigationLevel == Level.ARTIST_SONGS_AND_ALBUMS) {
            if (tabIndex == ARTIST_SONGS_TAB) {
                tab.setText(R.string.songs);
            } else if(tabIndex == ARTIST_ALBUMS_TAB) {
                tab.setText(R.string.albums);
            }
        } else if(navigationLevel == Level.TAG_SONGS_AND_ARTISTS) {
            if (tabIndex == TAG_SONGS_TAB) {
                tab.setText(R.string.songs);
            } else if(tabIndex == TAG_ARTISTS_TAB) {
                tab.setText(R.string.artists);
            }
        }
    }

    @Override
    public int getTabsCount(int selectedItemId, int navigationLevel) {
        if (navigationLevel == 0) {
            return PlayListMode.values().length;
        } else if(navigationLevel == Level.ARTIST_SONGS_AND_ALBUMS) {
            return ARTIST_TABS_COUNT;
        } else if(navigationLevel == Level.TAG_SONGS_AND_ARTISTS) {
            return TAG_TABS_COUNT;
        }

        return 1;
    }
}
