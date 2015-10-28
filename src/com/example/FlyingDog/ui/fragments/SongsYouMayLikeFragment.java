package com.example.FlyingDog.ui.fragments;

import android.app.Activity;
import com.example.FlyingDog.network.RequestManager;
import com.example.FlyingDog.network.SongsYouMayLikeService;
import com.tiksem.media.data.Audio;
import com.utils.framework.collections.InfiniteLoadingNavigationList;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.Services;

import java.util.List;

/**
 * Created by stykhonenko on 28.10.15.
 */
public class SongsYouMayLikeFragment extends SongsOfFragment {
    private static final int DISTANCE_TO_LOAD_NEXT_PAGE = 50;
    private SongsYouMayLikeService.Binder binder;
    private Services.UnBinder unBinder;

    @Override
    public void onAttach(Activity activity) {
        SongsYouMayLikeService.bindAndStart(activity, new Services.OnBind<SongsYouMayLikeService.Binder>() {
            @Override
            public void onBind(Services.Connection<SongsYouMayLikeService.Binder> connection) {
                unBinder = connection;
                binder = connection.getBinder();
                updateNavigationListWithLastFilter();
            }
        });
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unBinder.unbind();
    }

    @Override
    protected List<Audio> getLocalSongs() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected NavigationList<Audio> getAudiosFromInternet(String filter, RequestManager requestManager) {
        if (binder == null) {
            // show loading, when binder is not ready
            return InfiniteLoadingNavigationList.get();
        } else {
            NavigationList<Audio> songsYouMayLike = binder.getSongsYouMayLike();
            songsYouMayLike.setDistanceToLoadNextPage(DISTANCE_TO_LOAD_NEXT_PAGE);
            return songsYouMayLike;
        }
    }

    @Override
    protected boolean alwaysUseNavigationList() {
        return true;
    }
}
