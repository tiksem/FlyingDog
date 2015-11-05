package com.example.FlyingDog.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.example.FlyingDog.FlyingDog;
import com.example.FlyingDog.R;
import com.example.FlyingDog.ui.fragments.AbstractAudioDataFragment;
import com.example.FlyingDog.ui.fragments.PlayingNowFragment;
import com.tiksem.media.local.AudioDataBase;
import com.tiksem.media.playback.AudioPlayerService;
import com.tiksem.media.playback.StateChangedListener;
import com.tiksem.media.playback.Status;
import com.tiksem.media.ui.AudioPlaybackSeekBar;
import com.utils.framework.Cancelable;
import com.utilsframework.android.Services;
import com.utilsframework.android.navdrawer.*;
import com.utilsframework.android.threading.Tasks;
import com.utilsframework.android.view.GuiUtilities;
import com.utilsframework.android.view.LayoutRadioButtonGroup;
import com.utilsframework.android.view.Toasts;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by stykhonenko on 19.10.15.
 */
public class PlayListsActivity extends NavigationActivityWithoutDrawerLayout {
    private static final int RIGHT_SCROLL_TAB_INDEX = 4;
    private AsyncTask artsUpdating;
    private Services.UnBinder audioPlayBackUnBinder;
    private AudioPlayerService.Binder playBackService;
    private Queue<Runnable> whenPlayBackServiceReadyQueue = new ArrayDeque<>();
    private TabLayoutAdapter tabLayoutAdapter;
    private LayoutRadioButtonGroupTabsAdapter layoutRadioButtonGroupTabsAdapter;
    private TabsAdapterSwitcher tabsAdapterSwitcher;
    private Toasts.Controller helpToast;

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

        AudioPlayerService.bindAndStart(this, new Services.OnBind<AudioPlayerService.Binder>() {
            @Override
            public void onBind(Services.Connection<AudioPlayerService.Binder> connection) {
                onPlayBackServiceConnected(connection);
            }
        });

        helpToast = Toasts.infiniteCustomViewAtCenter(this, R.layout.search_internet_help_toast);
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

        StateChangedListener stateChangedListener = new StateChangedListener() {
            @Override
            public void onStateChanged(Status status) {
                if (status == Status.PLAYING || status == Status.PAUSED) {
                    playControls.setVisibility(View.VISIBLE);
                    playButton.setChecked(status == Status.PLAYING);
                    playButton.setEnabled(true);
                } else {
                    playButton.setEnabled(false);
                }
            }
        };
        playBackService.addStateChangedListener(stateChangedListener);
        stateChangedListener.onStateChanged(playBackService.getStatus());

        findViewById(R.id.to_playing_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new PlayingNowFragment(), Level.PLAYING_NOW);
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

        dismissHelpToast();
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

    @Override
    protected TabsAdapter createTabsAdapter() {
        tabsAdapterSwitcher = new TabsAdapterSwitcher(this, getTabsStub());
        layoutRadioButtonGroupTabsAdapter = LayoutRadioButtonGroupTabsAdapter.fromLayoutId(this,
                R.layout.main_tabs, R.id.playlistSwitcherContent);
        tabLayoutAdapter = TabLayoutAdapter.fromLayoutId(this, getTabLayoutId());

        final HorizontalScrollView scrollView = (HorizontalScrollView) layoutRadioButtonGroupTabsAdapter.getView();
        LayoutRadioButtonGroup radioButtonGroup = (LayoutRadioButtonGroup) scrollView.getChildAt(0);

        radioButtonGroup.getItemByIndex(RIGHT_SCROLL_TAB_INDEX).setOnSelectedChangedListener(
                new LayoutRadioButtonGroup.OnSelectedChanged() {
            @Override
            public void onSelectedChanged(boolean fromUser, LayoutRadioButtonGroup.LayoutRadioButton item,
                                          LayoutRadioButtonGroup.LayoutRadioButton old) {
                scrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });

        return tabsAdapterSwitcher;
    }

    @Override
    protected void onTabsInit(int tabsCount, int navigationLevel) {
        super.onTabsInit(tabsCount, navigationLevel);
        if (navigationLevel == 0) {
            tabsAdapterSwitcher.setTabsAdapter(layoutRadioButtonGroupTabsAdapter);
        } else {
            tabsAdapterSwitcher.setTabsAdapter(tabLayoutAdapter);
        }
    }

    public void dismissHelpToast() {
        if (helpToast != null) {
            helpToast.dismiss();
            helpToast = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (helpToast != null) {
            helpToast.resumeShowing();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (helpToast != null) {
            helpToast.pauseShowing();
        }
    }
}
