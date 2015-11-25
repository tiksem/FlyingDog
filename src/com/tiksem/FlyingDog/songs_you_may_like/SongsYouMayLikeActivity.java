package com.tiksem.FlyingDog.songs_you_may_like;

import com.tiksem.FlyingDog.ui.AbstractPlayListsActivity;
import com.utilsframework.android.navdrawer.FragmentFactory;

/**
 * Created by stykhonenko on 25.11.15.
 */
public class SongsYouMayLikeActivity extends AbstractPlayListsActivity {
    @Override
    protected FragmentFactory createFragmentFactory() {
        return new SongsYouMayLikeFragmentsFactory();
    }
}
