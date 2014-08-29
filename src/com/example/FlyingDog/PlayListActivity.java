package com.example.FlyingDog;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import com.example.FlyingDog.ui.fragments.AlbumListFragment;
import com.example.FlyingDog.ui.fragments.PlayListFragment;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.data.Album;
import com.tiksem.media.data.AllSongsTag;
import com.utilsframework.android.view.LayoutRadioButtonGroup;

import java.util.List;

public class PlayListActivity extends Activity {
    private Fragment getFragmentByModeId(int id) {
        switch (id) {
            case R.id.allSongsPlayListButton:
                return new PlayListFragment(new AllSongsTag());
            case R.id.albumsPlayListButton:
                AudioDataManager audioDataManager = FlyingDog.getInstance().getAudioDataManager();
                List<Album> albums = audioDataManager.getAlbums();
                return new AlbumListFragment(albums, audioDataManager);
        }

        return new PlayListFragment();
    }

    private void onPlayListModeChanged(int newPlayListModeId) {
        Fragment newFragment = getFragmentByModeId(newPlayListModeId);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStackImmediate();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.play_list_fragment_container, newFragment);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        LayoutRadioButtonGroup layoutRadioButtonGroup = (LayoutRadioButtonGroup)
                findViewById(R.id.playlistSwitcherContent);

        layoutRadioButtonGroup.setOnSelectedChangedListener(new LayoutRadioButtonGroup.OnSelectedChanged() {
            @Override
            public void onSelectedChanged(boolean fromUser,
                                          LayoutRadioButtonGroup.LayoutRadioButton item,
                                          LayoutRadioButtonGroup.LayoutRadioButton old) {
                onPlayListModeChanged(item.getId());
            }
        });

        getFragmentManager().
                beginTransaction().
                add(R.id.play_list_fragment_container, new PlayListFragment(new AllSongsTag())).
                commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
