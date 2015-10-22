package com.example.FlyingDog.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;
import com.example.FlyingDog.FlyingDog;
import com.example.FlyingDog.R;
import com.example.FlyingDog.ui.PlayListsActivity;
import com.tiksem.media.local.AudioDataBase;
import com.tiksem.media.playback.AudioPlayerService;
import com.tiksem.media.playback.StateChangedListener;
import com.tiksem.media.playback.Status;
import com.tiksem.media.ui.AudioPlaybackSeekBar;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.menu.SortListener;
import com.utilsframework.android.navigation.NavigationListFragment;
import com.utilsframework.android.network.AsyncRequestExecutorManager;
import com.utilsframework.android.view.GuiUtilities;

import java.util.List;

/**
 * Created by stykhonenko on 19.10.15.
 */
public abstract class AbstractPlayListFragment<T> extends NavigationListFragment<T, AsyncRequestExecutorManager>
        implements SortListener {
    protected AudioDataBase audioDataBase;
    private StateChangedListener stateChangedListener;
    private AudioPlayerService.Binder playBackService;

    protected final PlayListsActivity getPlayListsActivity() {
        return (PlayListsActivity) getActivity();
    }

    @Override
    protected int getRootLayout() {
        return R.layout.play_list_fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setHasOptionsMenu(true);
        audioDataBase = FlyingDog.getInstance().getAudioDataBase();
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final PlayListsActivity activity = getPlayListsActivity();
        activity.executeWhenPlayBackServiceReady(new Runnable() {
            @Override
            public void run() {
                onViewCratedAndPlayBackServiceReady(view, activity.getPlayBackService());
            }
        });
    }

    private void onViewCratedAndPlayBackServiceReady(View view, final AudioPlayerService.Binder playBackService) {
        this.playBackService = playBackService;

        AudioPlaybackSeekBar seekBar = (AudioPlaybackSeekBar) view.findViewById(R.id.play_seek_bar);
        seekBar.setPlayerBinder(playBackService);

        final ViewGroup playControls = (ViewGroup) view.findViewById(R.id.play_controls);
        final ToggleButton playButton = (ToggleButton) view.findViewById(R.id.play);
        View next = view.findViewById(R.id.next);
        View prev = view.findViewById(R.id.prev);

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

        stateChangedListener = new StateChangedListener() {
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
        };
        playBackService.addStateChangedListener(stateChangedListener);
        stateChangedListener.onStateChanged(playBackService.getStatus());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (playBackService != null) {
            playBackService.removeStateChangedListener(stateChangedListener);
        }
    }

    public AudioPlayerService.Binder getPlayBackService() {
        return playBackService;
    }

    @Override
    protected AsyncRequestExecutorManager obtainRequestManager() {
        return new AsyncRequestExecutorManager();
    }

    @Override
    protected int getLoadingResourceId() {
        return R.id.loading;
    }

    @Override
    protected int getNoInternetConnectionViewId() {
        return R.id.no_connection;
    }

    protected abstract List<T> createList();

    protected NavigationList<T> createNavigationList(String filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected final NavigationList<T> getNavigationList(AsyncRequestExecutorManager requestManager, String filter) {
        if (filter == null) {
            return NavigationList.decorate(createList());
        } else {
            return createNavigationList(filter);
        }
    }

    @Override
    protected boolean useSwipeRefresh() {
        return false;
    }

    @Override
    protected int getListResourceId() {
        return R.id.list;
    }
}
