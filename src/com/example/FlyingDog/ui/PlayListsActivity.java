package com.example.FlyingDog.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import com.example.FlyingDog.FlyingDog;
import com.example.FlyingDog.ui.fragments.AbstractPlayListFragment;
import com.tiksem.media.local.AudioDataBase;
import com.tiksem.media.playback.AudioPlayerService;
import com.utilsframework.android.Services;
import com.utilsframework.android.fragments.ListViewFragment;
import com.utilsframework.android.navdrawer.FragmentFactory;
import com.utilsframework.android.navdrawer.NavigationActivityWithoutDrawerLayout;
import com.utilsframework.android.threading.Tasks;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by stykhonenko on 19.10.15.
 */
public class PlayListsActivity extends NavigationActivityWithoutDrawerLayout {
    private AsyncTask artsUpdating;
    private Services.UnBinder audioPlayBackUnBinder;
    private AudioPlayerService.Binder playBackService;
    private Queue<Runnable> whenPlayBackServiceReadyQueue = new ArrayDeque<>();

    private AbstractPlayListFragment getPlayListFragment() {
        return (AbstractPlayListFragment) getCurrentFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        artsUpdating = FlyingDog.getInstance().getAudioDataBase().startAlbumArtsUpdating(
                new AudioDataBase.OnArtsUpdatingFinished() {
            @Override
            public void onFinished() {
                AbstractPlayListFragment fragment = getPlayListFragment();
                if (fragment != null) {
                    fragment.notifyDataSetChanged();
                }
            }
        });

        AudioPlayerService.bind(this, new Services.OnBind<AudioPlayerService.Binder>() {
            @Override
            public void onBind(Services.Connection<AudioPlayerService.Binder> connection) {
                onPlayBackServiceConnected(connection);
            }
        });
    }

    private void onPlayBackServiceConnected(Services.Connection<AudioPlayerService.Binder> connection) {
        audioPlayBackUnBinder = connection;
        playBackService = connection.getBinder();

        Tasks.executeAndClearQueue(whenPlayBackServiceReadyQueue);
        whenPlayBackServiceReadyQueue = null;
    }

    public void executeWhenPlayBackServiceReady(Runnable runnable) {
        if (playBackService != null) {
            runnable.run();
        } else {
            whenPlayBackServiceReadyQueue.add(runnable);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        artsUpdating.cancel(false);
        audioPlayBackUnBinder.unbind();
    }

    @Override
    protected FragmentFactory createFragmentFactory() {
        return new FlyingDogFragmentsFactory(this);
    }

    public AudioPlayerService.Binder getPlayBackService() {
        return playBackService;
    }
}
