package com.example.FlyingDog.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.FlyingDog.FlyingDog;
import com.example.FlyingDog.R;
import com.tiksem.media.playback.AudioPlayerService;
import com.tiksem.media.ui.AudioPlaybackSeekBar;
import com.utilsframework.android.view.GuiUtilities;
import com.utilsframework.android.view.PausedStateToggleButton;

/**
 * User: Tikhonenko.S
 * Date: 07.08.14
 * Time: 15:37
 */
public class PlayerControlsFragment extends Fragment {
    private AudioPlaybackSeekBar audioPlaybackSeekBar;
    private View prevButton;
    private View nextButton;
    private PausedStateToggleButton playButton;
    private AudioPlayerService.PlayerBinder playerBinder;
    private AudioPlayerService.PlayBackListener playBackListener;

    private void onServiceReady() {
        audioPlaybackSeekBar.setPlayerBinder(playerBinder);
        playButton.setPauseable(playerBinder);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerBinder.playNext();
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerBinder.playPrev();
            }
        });

        playBackListener = new AudioPlayerService.PlayBackListener() {
            @Override
            public void onAudioPlayingStarted() {
                getView().setVisibility(View.VISIBLE);
            }

            @Override
            public void onAudioPlayingComplete() {
                getView().setVisibility(View.GONE);
            }

            @Override
            public void onAudioPlayingPaused() {
            }

            @Override
            public void onAudioPlayingResumed() {
            }

            @Override
            public void onProgressChanged(long progress, long max) {
            }
        };

        playerBinder.addPlayBackListener(playBackListener);
        if(playerBinder.isPlaying()){
            getView().setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.player_controls, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        audioPlaybackSeekBar = (AudioPlaybackSeekBar) view.findViewById(R.id.play_seek_bar);
        nextButton = view.findViewById(R.id.next);
        prevButton = view.findViewById(R.id.prev);
        playButton = (PausedStateToggleButton) view.findViewById(R.id.play);

        view.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        final FlyingDog flyingDog = FlyingDog.getInstance();
        flyingDog.executeWhenPlayerServiceIsReady(new Runnable() {
            @Override
            public void run() {
                playerBinder = flyingDog.getPlayerBinder();
                GuiUtilities.executeWhenViewCreated(PlayerControlsFragment.this, new GuiUtilities.OnViewCreated() {
                    @Override
                    public void onViewCreated(View view) {
                        onServiceReady();
                    }
                });
            }
        });

    }

    @Override
    public void onDetach() {
        super.onDetach();
        playerBinder.removePlayBackListener(playBackListener);
    }
}
