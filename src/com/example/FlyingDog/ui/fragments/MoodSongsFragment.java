package com.example.FlyingDog.ui.fragments;

import com.example.FlyingDog.network.RequestManager;
import com.tiksem.media.data.Audio;
import com.tiksem.media.data.Mood;
import com.utils.framework.collections.NavigationList;

/**
 * Created by stykhonenko on 03.11.15.
 */
public class MoodSongsFragment extends SongsOfTagFragment {
    public static MoodSongsFragment create(String tag) {
        return create(tag, new MoodSongsFragment());
    }

    @Override
    protected NavigationList<Audio> getAudiosFromInternet(String filter, RequestManager requestManager) {
        return requestManager.getSongsByMood(getTagName());
    }
}
