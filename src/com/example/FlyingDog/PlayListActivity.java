package com.example.FlyingDog;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import com.example.FlyingDog.ui.fragments.*;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.data.Album;
import com.tiksem.media.data.AllSongsTag;
import com.tiksem.media.data.Artist;
import com.tiksem.media.data.PlayList;
import com.utilsframework.android.view.GuiUtilities;
import com.utilsframework.android.view.LayoutRadioButtonGroup;

import java.util.List;

public class PlayListActivity extends Activity {
    private AudioDataManager audioDataManager;
    private LayoutRadioButtonGroup artistNavigationTabs;

    private Fragment getCurrentModeFragment() {
        return getFragmentManager().findFragmentById(R.id.play_list_fragment_container);
    }

    private Fragment getFragmentByModeId(int id) {
        switch (id) {
            case R.id.allSongsPlayListButton:
                return new AudioListFragment(new AllSongsTag());
            case R.id.albumsPlayListButton:
                List<Album> albums = audioDataManager.getAlbums();
                return new AlbumListFragment(albums, audioDataManager);
            case R.id.artistsPlayListButton:
                List<Artist> artists = audioDataManager.getArtists();
                return new ArtistListFragment(artists, audioDataManager, new ArtistNavigationModeProvider() {
                    @Override
                    public ArtistNavigationMode getNavigationMode() {
                        return getArtistNavigationModeBySelectedTab(artistNavigationTabs.getSelectedItem());
                    }
                });
            case R.id.playlistsPlayListButton:
                audioDataManager = FlyingDog.getInstance().getAudioDataManager();
                List<PlayList> playLists = audioDataManager.getPlayLists();
                return new PlayListsFragment(playLists, audioDataManager);
        }

        return new AudioListFragment();
    }

    private void onPlayListModeChanged(int newPlayListModeId) {
        if(newPlayListModeId == R.id.artistsPlayListButton){
            artistNavigationTabs.setVisibility(View.VISIBLE);
        } else {
            artistNavigationTabs.setVisibility(View.GONE);
        }

        Fragment newFragment = getFragmentByModeId(newPlayListModeId);
        FragmentManager fragmentManager = getFragmentManager();
        while (fragmentManager.popBackStackImmediate());
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.play_list_fragment_container, newFragment);
        transaction.commit();
    }

    private void onArtistNavigationModeChanged(ArtistNavigationMode mode) {
        Fragment current = getCurrentModeFragment();
        if(current instanceof ArtistListFragment){
            return;
        }

        if(current instanceof AlbumListFragment){
            AlbumListFragment albumListFragment = (AlbumListFragment) current;
            AudioListFragment audioListFragment = new AudioListFragment(albumListFragment.getArtist());
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.popBackStackImmediate();
            fragmentManager.beginTransaction().replace(
                    R.id.play_list_fragment_container, audioListFragment).addToBackStack(null).commit();
            return;
        }

        if(mode == ArtistNavigationMode.SONGS){
            getFragmentManager().popBackStackImmediate();
            onArtistNavigationModeChanged(mode);
        } else {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.popBackStackImmediate();

            AudioListFragment audioListFragment = (AudioListFragment) current;
            AlbumListFragment albumListFragment = new AlbumListFragment
                    (audioListFragment.getArtist(), audioDataManager);

            fragmentManager.beginTransaction().replace(
                    R.id.play_list_fragment_container, albumListFragment).addToBackStack(null).commit();
        }
    }

    private ArtistNavigationMode getArtistNavigationModeBySelectedTab(
            LayoutRadioButtonGroup.LayoutRadioButton selectedTab) {
        if(selectedTab.getId() == R.id.songsOfArtistTab){
            return ArtistNavigationMode.SONGS;
        } else {
            return ArtistNavigationMode.ALBUMS;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        audioDataManager = FlyingDog.getInstance().getAudioDataManager();

        LayoutRadioButtonGroup playModeSwitcher = (LayoutRadioButtonGroup)
                findViewById(R.id.playlistSwitcherContent);

        playModeSwitcher.setOnSelectedChangedListener(new LayoutRadioButtonGroup.OnSelectedChanged() {
            @Override
            public void onSelectedChanged(boolean fromUser,
                                          LayoutRadioButtonGroup.LayoutRadioButton item,
                                          LayoutRadioButtonGroup.LayoutRadioButton old) {
                onPlayListModeChanged(item.getId());
            }
        });

        artistNavigationTabs = (LayoutRadioButtonGroup)
                findViewById(R.id.artistTabs);

        artistNavigationTabs.setOnSelectedChangedListener(new LayoutRadioButtonGroup.OnSelectedChanged() {
            @Override
            public void onSelectedChanged(boolean fromUser, LayoutRadioButtonGroup.LayoutRadioButton item,
                                          LayoutRadioButtonGroup.LayoutRadioButton old) {
                onArtistNavigationModeChanged(getArtistNavigationModeBySelectedTab(item));
            }
        });

        getFragmentManager().
                beginTransaction().
                add(R.id.play_list_fragment_container, new AudioListFragment(new AllSongsTag())).
                commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
