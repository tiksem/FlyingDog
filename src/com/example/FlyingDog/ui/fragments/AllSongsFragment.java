package com.example.FlyingDog.ui.fragments;

import com.example.FlyingDog.R;
import com.example.FlyingDog.network.RequestManager;
import com.tiksem.media.data.Audio;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.view.Toasts;

import java.util.List;

/**
 * Created by stykhonenko on 28.10.15.
 */
public class AllSongsFragment extends SongsFragment {
    @Override
    protected List<Audio> getLocalSongs() {
        return audioDataBase.getSongs();
    }

    @Override
    protected NavigationList<Audio> getAudiosFromInternet(String filter, RequestManager requestManager) {
        return requestManager.searchSongs(filter);
    }
}
