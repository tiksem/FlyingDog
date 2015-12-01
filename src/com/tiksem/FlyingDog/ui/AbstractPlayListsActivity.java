package com.tiksem.FlyingDog.ui;

import android.graphics.LightingColorFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ToggleButton;
import com.tiksem.FlyingDog.FlyingDog;
import com.tiksem.SongsYouMayLike.R;
import com.tiksem.FlyingDog.services.FlyingDogPlaybackService;
import com.tiksem.FlyingDog.ui.fragments.AbstractAudioDataFragment;
import com.tiksem.FlyingDog.ui.fragments.PlayingNowFragment;
import com.tiksem.media.local.AudioDataBase;
import com.tiksem.media.playback.AudioPlayerService;
import com.tiksem.media.playback.StateChangedListener;
import com.tiksem.media.playback.Status;
import com.tiksem.media.ui.AudioPlaybackSeekBar;
import com.utilsframework.android.Services;
import com.utilsframework.android.navdrawer.LayoutRadioButtonGroupTabsAdapter;
import com.utilsframework.android.navdrawer.NavigationActivityWithoutDrawerLayout;
import com.utilsframework.android.threading.Tasks;
import com.utilsframework.android.view.GuiUtilities;
import com.utilsframework.android.view.KeyboardIsShownListener;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by stykhonenko on 20.11.15.
 */
public abstract class AbstractPlayListsActivity extends NavigationActivityWithoutDrawerLayout {
    private AsyncTask artsUpdating;
    private Services.UnBinder audioPlayBackUnBinder;
    private AudioPlayerService.Binder playBackService;
    private Queue<Runnable> whenPlayBackServiceReadyQueue = new ArrayDeque<>();
    private LayoutRadioButtonGroupTabsAdapter layoutRadioButtonGroupTabsAdapter;
    private View bottomBar;
    private KeyboardIsShownListener keyboardIsShownListener;

    private AbstractAudioDataFragment getPlayListFragment() {
        return (AbstractAudioDataFragment) getCurrentFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        artsUpdating = FlyingDog.getInstance().getAudioDataBase().startAlbumArtsUpdating(
                new AudioDataBase.OnArtsUpdatingFinished() {
                    @Override
                    public void onFinished() {
                        AbstractAudioDataFragment fragment = getPlayListFragment();
                        if (fragment != null) {
                            fragment.notifyDataSetChanged();
                        }
                    }
                });

        FlyingDogPlaybackService.bindAndStart(this, new Services.OnBind<AudioPlayerService.Binder>() {
            @Override
            public void onBind(Services.Connection<AudioPlayerService.Binder> connection) {
                onPlayBackServiceConnected(connection);
            }
        });

        bottomBar = findViewById(R.id.bottom_bar);

        keyboardIsShownListener = new KeyboardIsShownListener(this) {
            @Override
            protected void onKeyboardStateChanged(boolean isShown) {
                bottomBar.setVisibility(isShown ? View.GONE : View.VISIBLE);
            }
        };
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
        final AudioPlaybackSeekBar seekBar = (AudioPlaybackSeekBar) findViewById(R.id.play_seek_bar);
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

        final View urlLoading = findViewById(R.id.url_loading);

        StateChangedListener stateChangedListener = new StateChangedListener() {
            @Override
            public void onStateChanged(Status status, Status lastStatus) {
                if (status == Status.PLAYING || status == Status.PAUSED) {
                    playControls.setVisibility(View.VISIBLE);
                    playButton.setChecked(status == Status.PLAYING);
                    playButton.setEnabled(true);
                } else {
                    playButton.setEnabled(false);
                }

                urlLoading.setVisibility(status == Status.PREPARING ? View.VISIBLE : View.GONE);
            }
        };
        playBackService.addStateChangedListener(stateChangedListener);
        stateChangedListener.onStateChanged(playBackService.getStatus(), null);

        findViewById(R.id.to_playing_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new PlayingNowFragment(), Level.PLAYING_NOW);
            }
        });

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            GuiUtilities.setSeekBarTintColor(seekBar, getResources().getColor(R.color.colorAccent));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        artsUpdating.cancel(false);
        if (audioPlayBackUnBinder != null) {
            audioPlayBackUnBinder.unbind();
        }

        keyboardIsShownListener.destroy();

        dismissHelpToast();
    }

    public AudioPlayerService.Binder getPlayBackService() {
        return playBackService;
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.main;
    }

    public void dismissHelpToast() {

    }

    @Override
    protected int getToolbarLayoutId() {
        return R.layout.toolbar;
    }
}
