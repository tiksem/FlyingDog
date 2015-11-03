package com.example.FlyingDog.ui.fragments;

import com.example.FlyingDog.network.RequestManager;
import com.tiksem.media.data.Audio;
import com.utils.framework.collections.NavigationList;

/**
 * Created by stykhonenko on 03.11.15.
 */
public class CountrySongsFragment extends SongsOfTagFragment {
    public static CountrySongsFragment create(String tag) {
        return create(tag, new CountrySongsFragment());
    }

    @Override
    protected NavigationList<Audio> getAudiosFromInternet(String filter, RequestManager requestManager) {
        return requestManager.getSongsByCountry(getTagName());
    }
}
