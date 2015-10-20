package com.example.FlyingDog.ui.fragments;

import android.app.Activity;
import com.example.FlyingDog.FlyingDog;
import com.example.FlyingDog.ui.PlayListsActivity;
import com.example.FlyingDog.ui.adapters.SongsAdapter;
import com.tiksem.media.data.Audio;
import com.tiksem.media.local.AudioDataBase;
import com.utils.framework.CollectionUtils;
import com.utilsframework.android.adapters.ViewArrayAdapter;
import com.utilsframework.android.fragments.ListViewFragment;

import java.util.List;

/**
 * Created by stykhonenko on 19.10.15.
 */
public class SongsFragment extends AbstractPlayListFragment<Audio> {
    private List<Audio> songs;
    private List<String> urls;

    @Override
    protected ViewArrayAdapter<Audio, ?> createAdapter() {
        return new SongsAdapter(getActivity());
    }

    @Override
    protected List<Audio> createList() {
        songs = audioDataBase.getSongs();
        urls = CollectionUtils.transformNonCopy(songs, new CollectionUtils.Transformer<Audio, String>() {
            @Override
            public String get(Audio audio) {
                return audio.getUrl();
            }
        });
        return songs;
    }

    @Override
    protected void onListItemClicked(Audio item, final int position) {
        final PlayListsActivity activity = getPlayListsActivity();
        activity.executeWhenPlayBackServiceReady(new Runnable() {
            @Override
            public void run() {
                activity.getPlayBackService().play(urls, position);
            }
        });
    }
}
