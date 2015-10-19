package com.example.FlyingDog.ui.fragments;

import android.app.Activity;
import com.example.FlyingDog.FlyingDog;
import com.example.FlyingDog.ui.adapters.SongsAdapter;
import com.tiksem.media.data.Audio;
import com.tiksem.media.local.AudioDataBase;
import com.utilsframework.android.adapters.ViewArrayAdapter;
import com.utilsframework.android.fragments.ListViewFragment;

import java.util.List;

/**
 * Created by stykhonenko on 19.10.15.
 */
public class SongsFragment extends AbstractPlayListFragment<Audio> {
    @Override
    protected ViewArrayAdapter<Audio, ?> createAdapter() {
        return new SongsAdapter(getActivity());
    }

    @Override
    protected List<Audio> createList() {
        return audioDataBase.getSongs();
    }
}
