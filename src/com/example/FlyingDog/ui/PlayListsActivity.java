package com.example.FlyingDog.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import com.example.FlyingDog.FlyingDog;
import com.tiksem.media.local.AudioDataBase;
import com.utilsframework.android.fragments.ListViewFragment;
import com.utilsframework.android.navdrawer.FragmentFactory;
import com.utilsframework.android.navdrawer.NavigationActivityWithoutDrawerLayout;
import com.utilsframework.android.navdrawer.NavigationDrawerActivity;
import com.utilsframework.android.navdrawer.NavigationDrawerMenuAdapter;

/**
 * Created by stykhonenko on 19.10.15.
 */
public class PlayListsActivity extends NavigationActivityWithoutDrawerLayout {
    private AsyncTask artsUpdating;

    private ListViewFragment getListViewFragment() {
        return (ListViewFragment) getCurrentFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        artsUpdating = FlyingDog.getInstance().getAudioDataBase().startAlbumArtsUpdating(
                new AudioDataBase.OnArtsUpdatingFinished() {
            @Override
            public void onFinished() {
                getListViewFragment().notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        artsUpdating.cancel(false);
    }

    @Override
    protected FragmentFactory createFragmentFactory() {
        return new FlyingDogFragmentsFactory();
    }
}
