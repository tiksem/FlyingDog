package com.example.FlyingDog.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;
import com.example.FlyingDog.FlyingDog;
import com.example.FlyingDog.R;
import com.example.FlyingDog.ui.fragments.AbstractPlayListFragment;
import com.tiksem.media.local.AudioDataBase;
import com.tiksem.media.playback.AudioPlayerService;
import com.tiksem.media.playback.StateChangedListener;
import com.tiksem.media.playback.Status;
import com.tiksem.media.ui.AudioPlaybackSeekBar;
import com.utilsframework.android.Services;
import com.utilsframework.android.fragments.ListViewFragment;
import com.utilsframework.android.navdrawer.FragmentFactory;
import com.utilsframework.android.navdrawer.NavigationActivityWithoutDrawerLayout;
import com.utilsframework.android.threading.Tasks;
import com.utilsframework.android.view.GuiUtilities;

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
        setupPlayingControls();

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

    private void setupPlayingControls() {
        AudioPlaybackSeekBar seekBar = (AudioPlaybackSeekBar) findViewById(R.id.play_seek_bar);
        seekBar.setPlayerBinder(playBackService);

        final ViewGroup playControls = (ViewGroup) findViewById(R.id.play_controls);
        final ToggleButton playButton = (ToggleButton) findViewById(R.id.play);
        View next = findViewById(R.id.next);
        View prev = findViewById(R.id.prev);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playBackService.togglePauseState();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playBackService.playNext();
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playBackService.playPrev();
            }
        });

        playBackService.addStateChangedListener(new StateChangedListener() {
            @Override
            public void onStateChanged(Status status) {
                if (status == Status.PLAYING || status == Status.PAUSED) {
                    playControls.setVisibility(View.VISIBLE);
                    playButton.setChecked(status == Status.PLAYING);
                    GuiUtilities.setEnabledForChildren(playControls, true);
                } else {
                    GuiUtilities.setEnabledForChildren(playControls, false);
                }
            }
        });
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        artsUpdating.cancel(false);
        if (audioPlayBackUnBinder != null) {
            audioPlayBackUnBinder.unbind();
        }
    }

    @Override
    protected FragmentFactory createFragmentFactory() {
        return new FlyingDogFragmentsFactory(this);
    }

    public AudioPlayerService.Binder getPlayBackService() {
        return playBackService;
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.main;
    }
}
