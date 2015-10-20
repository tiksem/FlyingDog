package com.example.FlyingDog.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.example.FlyingDog.FlyingDog;
import com.example.FlyingDog.R;
import com.example.FlyingDog.ui.PlayListsActivity;
import com.tiksem.media.local.AudioDataBase;
import com.tiksem.media.playback.AudioPlayerService;
import com.tiksem.media.ui.AudioPlaybackSeekBar;
import com.utilsframework.android.fragments.Fragments;
import com.utilsframework.android.fragments.ListViewFragment;
import com.utilsframework.android.view.GuiUtilities;

/**
 * Created by stykhonenko on 19.10.15.
 */
public abstract class AbstractPlayListFragment<T> extends ListViewFragment<T> {
    protected AudioDataBase audioDataBase;

    protected final PlayListsActivity getPlayListsActivity() {
        return (PlayListsActivity) getActivity();
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.play_list_fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        audioDataBase = FlyingDog.getInstance().getAudioDataBase();
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final PlayListsActivity activity = getPlayListsActivity();
        activity.executeWhenPlayBackServiceReady(new Runnable() {
            @Override
            public void run() {
                AudioPlaybackSeekBar seekBar = (AudioPlaybackSeekBar) view.findViewById(R.id.play_seek_bar);
                seekBar.setPlayerBinder(activity.getPlayBackService());
            }
        });
    }
}
