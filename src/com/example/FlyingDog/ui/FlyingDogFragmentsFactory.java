package com.example.FlyingDog.ui;

import android.support.v4.app.Fragment;
import com.example.FlyingDog.R;
import com.example.FlyingDog.ui.fragments.ArtistsFragment;
import com.example.FlyingDog.ui.fragments.SongsFragment;
import com.utilsframework.android.navdrawer.FragmentFactory;
import com.utilsframework.android.navdrawer.TabsAdapter;

/**
 * Created by stykhonenko on 19.10.15.
 */
public class FlyingDogFragmentsFactory implements FragmentFactory {
    @Override
    public Fragment createFragmentBySelectedItem(int selectedItemId, int tabIndex, int navigationLevel) {
        PlayListMode mode = PlayListMode.values()[tabIndex];
        switch (mode) {
            case ALL_SONGS:
                return new SongsFragment();
            case ARTISTS:
                return new ArtistsFragment();
        }

        throw new RuntimeException("Invalid fragment request");
    }

    @Override
    public void initTab(int currentSelectedItem, int tabIndex, int navigationLevel, TabsAdapter.Tab tab) {
        PlayListMode mode = PlayListMode.values()[tabIndex];
        switch (mode) {
            case ALL_SONGS:
                tab.setText(R.string.all_songs);
                break;
            case ARTISTS:
                tab.setText(R.string.artists);
                break;
        }
    }

    @Override
    public int getTabsCount(int selectedItemId, int navigationLevel) {
        return PlayListMode.values().length;
    }
}
