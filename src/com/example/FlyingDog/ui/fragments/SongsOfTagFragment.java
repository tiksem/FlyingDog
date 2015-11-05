package com.example.FlyingDog.ui.fragments;

import android.app.Activity;
import com.example.FlyingDog.network.RequestManager;
import com.tiksem.media.data.Audio;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.fragments.Fragments;

import java.util.List;

/**
 * Created by stykhonenko on 03.11.15.
 */
public class SongsOfTagFragment extends SongsOfFragment implements TagProvider {
    private static final String TAG = "TAG";

    private String tag;

    public static SongsOfTagFragment create(String tag) {
        return create(tag, new SongsOfTagFragment());
    }

    protected static <T extends SongsOfTagFragment> T create(String tag, T fragment) {
        return Fragments.createFragmentWith1Arg(fragment, TAG, tag);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        tag = getArguments().getString(TAG);
    }

    @Override
    protected List<Audio> getLocalSongs() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected NavigationList<Audio> getAudiosFromInternet(String filter, RequestManager requestManager) {
        return requestManager.getSongsByTag(tag, filter);
    }

    @Override
    protected boolean alwaysUseNavigationList() {
        return true;
    }

    @Override
    public String getTagName() {
        return tag;
    }

    @Override
    protected boolean hasSearchMenu() {
        return true;
    }
}
