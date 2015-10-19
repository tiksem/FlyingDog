package com.example.FlyingDog.ui.fragments;

import android.app.Activity;
import android.app.ListFragment;
import com.example.FlyingDog.FlyingDog;
import com.tiksem.media.local.AudioDataBase;
import com.utilsframework.android.adapters.ViewArrayAdapter;
import com.utilsframework.android.fragments.ListViewFragment;

import java.util.List;

/**
 * Created by stykhonenko on 19.10.15.
 */
public abstract class AbstractPlayListFragment<T> extends ListViewFragment<T> {
    protected AudioDataBase audioDataBase;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        audioDataBase = FlyingDog.getInstance().getAudioDataBase();
    }
}
