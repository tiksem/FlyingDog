package com.tiksem.FlyingDog.ui.fragments;

import com.tiksem.SongsYouMayLike.R;
import com.tiksem.FlyingDog.network.RequestManager;
import com.tiksem.media.data.Audio;
import com.utils.framework.collections.NavigationList;

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
