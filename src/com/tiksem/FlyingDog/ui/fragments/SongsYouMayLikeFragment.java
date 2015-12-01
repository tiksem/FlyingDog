package com.tiksem.FlyingDog.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import com.tiksem.FlyingDog.network.RequestManager;
import com.tiksem.FlyingDog.services.FlyingDogSongsYouMayLikeService;
import com.tiksem.SongsYouMayLike.R;
import com.tiksem.media.data.Audio;
import com.tiksem.media.search.syouml.SongsYouMayLikeService;
import com.utils.framework.collections.InfiniteLoadingNavigationList;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.Services;

import java.util.List;

/**
 * Created by stykhonenko on 28.10.15.
 */
public class SongsYouMayLikeFragment extends SongsOfFragment {
    private static final int DISTANCE_TO_LOAD_NEXT_PAGE = 50;
    private FlyingDogSongsYouMayLikeService.Binder binder;
    private Services.UnBinder unBinder;
    private MenuItem updateItem;

    @Override
    public void onAttach(Activity activity) {
        FlyingDogSongsYouMayLikeService.bindAndStart(activity,
                new Services.OnBind<FlyingDogSongsYouMayLikeService.Binder>() {
            @Override
            public void onBind(Services.Connection<FlyingDogSongsYouMayLikeService.Binder> connection) {
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
        if (unBinder != null) {
            unBinder.unbind();
        }
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
            NavigationList<Audio> songsYouMayLike = binder.getSuggestedSongs();
            songsYouMayLike.setDistanceToLoadNextPage(DISTANCE_TO_LOAD_NEXT_PAGE);
            return songsYouMayLike;
        }
    }

    @Override
    protected boolean alwaysGetListFromInternet() {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.update, menu);
        super.onCreateOptionsMenu(menu, inflater);

        updateItem = menu.findItem(R.id.action_update);
        updateItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                binder.reload();
                updateNavigationListWithLastFilter();
                return true;
            }
        });

        refreshUpdateItemVisibility();
    }

    private void refreshUpdateItemVisibility() {
        if (updateItem == null) {
            return;
        }

        AbsListView listView = getListView();
        if (listView != null) {
            updateItem.setVisible(listView.getVisibility() == View.VISIBLE);
        } else {
            updateItem.setVisible(false);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshUpdateItemVisibility();
    }

    @Override
    protected void onNavigationListChanged(NavigationList<Audio> navigationList) {
        super.onNavigationListChanged(navigationList);
        refreshUpdateItemVisibility();
    }

    @Override
    protected void onListViewIsShown() {
        super.onListViewIsShown();
        refreshUpdateItemVisibility();
    }

    @Override
    protected void onEmptyViewIsShown() {
        super.onEmptyViewIsShown();
        refreshUpdateItemVisibility();
    }

    public FlyingDogSongsYouMayLikeService.Binder getBinder() {
        return binder;
    }
}
