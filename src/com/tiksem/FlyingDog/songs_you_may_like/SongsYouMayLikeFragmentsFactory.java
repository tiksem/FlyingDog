package com.tiksem.FlyingDog.songs_you_may_like;

import android.support.v4.app.Fragment;
import com.tiksem.FlyingDog.ui.fragments.SongsYouMayLikeFragment;
import com.tiksem.SongsYouMayLike.R;
import com.utilsframework.android.navdrawer.FragmentFactory;
import com.utilsframework.android.navdrawer.TabsAdapter;

/**
 * Created by stykhonenko on 25.11.15.
 */
public class SongsYouMayLikeFragmentsFactory implements FragmentFactory {
    private static final int TABS_COUNT = 2;
    private static final int SUGGESTED_TAB = 0;
    private static final int LOCAL_SONGS_TAB = 1;

    @Override
    public Fragment createFragmentBySelectedItem(int selectedItemId, int tabIndex, int navigationLevel) {
        if (tabIndex == SUGGESTED_TAB) {
            return new SongsYouMayLikeFragment();
        } else if(tabIndex == LOCAL_SONGS_TAB) {
            return new YourSongsFragment();
        }

        throw new RuntimeException("Invalid tab request");
    }

    @Override
    public void initTab(int currentSelectedItem, int tabIndex, int navigationLevel, TabsAdapter.Tab tab) {
        if (tabIndex == SUGGESTED_TAB) {
            tab.setText(R.string.suggested);
        } else if(tabIndex == LOCAL_SONGS_TAB) {
            tab.setText(R.string.your_songs);
        } else {
            throw new RuntimeException("Invalid tab request");
        }
    }

    @Override
    public int getTabsCount(int selectedItemId, int navigationLevel) {
        return TABS_COUNT;
    }
}
