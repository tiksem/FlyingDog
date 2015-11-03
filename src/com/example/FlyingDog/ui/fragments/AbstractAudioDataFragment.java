package com.example.FlyingDog.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.example.FlyingDog.FlyingDog;
import com.example.FlyingDog.R;
import com.example.FlyingDog.network.RequestManager;
import com.example.FlyingDog.ui.PlayListsActivity;
import com.tiksem.media.local.FlyingDogAudioDatabase;
import com.tiksem.media.playback.AudioPlayerService;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.menu.SortListener;
import com.utilsframework.android.navigation.NavigationListFragment;

import java.util.List;

/**
 * Created by stykhonenko on 19.10.15.
 */
public abstract class AbstractAudioDataFragment<T> extends NavigationListFragment<T, RequestManager>
        implements SortListener {
    protected FlyingDogAudioDatabase audioDataBase;
    private AudioPlayerService.Binder playBackService;
    private MenuItem sortMenuItem;

    protected final PlayListsActivity getPlayListsActivity() {
        return (PlayListsActivity) getActivity();
    }

    @Override
    protected int getRootLayout() {
        return R.layout.play_list_fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setHasOptionsMenu(true);
        audioDataBase = FlyingDog.getInstance().getAudioDataBase();
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final PlayListsActivity activity = getPlayListsActivity();
        activity.executeWhenPlayBackServiceReady(new Runnable() {
            @Override
            public void run() {
                onViewCreatedAndPlayBackServiceReady(view, activity.getPlayBackService());
            }
        });
    }

    private void onViewCreatedAndPlayBackServiceReady(View view, final AudioPlayerService.Binder playBackService) {
        this.playBackService = playBackService;
    }

    public AudioPlayerService.Binder getPlayBackService() {
        return playBackService;
    }

    @Override
    protected RequestManager obtainRequestManager() {
        return new RequestManager();
    }

    @Override
    protected int getLoadingResourceId() {
        return R.id.loading;
    }

    @Override
    protected int getNoInternetConnectionViewId() {
        return R.id.no_connection;
    }

    protected abstract List<T> createLocalList();

    protected NavigationList<T> createInternetList(String filter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        sortMenuItem = menu.findItem(R.id.sort);
        setSortMenuItemVisibility(getElements().isDecorated());
    }

    protected NavigationList<T> getNavigationList(String filter) {
        if (!alwaysUseNavigationList() && filter == null) {
            return NavigationList.decorate(createLocalList());
        } else {
            return createInternetList(filter);
        }
    }

    @Override
    protected final NavigationList<T> getNavigationList(RequestManager requestManager, String filter) {
        return getNavigationList(filter);
    }

    @Override
    protected void onNavigationListChanged(NavigationList<T> navigationList) {
        setSortMenuItemVisibility(navigationList.isDecorated());
    }

    protected final void setSortMenuItemVisibility(boolean value) {
        if (sortMenuItem != null) {
            sortMenuItem.setVisible(value);
        }
    }

    @Override
    protected boolean useSwipeRefresh() {
        return false;
    }

    @Override
    protected int getListResourceId() {
        return R.id.list;
    }

    protected boolean alwaysUseNavigationList() {
        return false;
    }

    @Override
    protected void onSearchViewExpandCollapse(Menu menu, boolean expanded) {
        super.onSearchViewExpandCollapse(menu, expanded);
        if (sortMenuItem != null) {
            sortMenuItem.setVisible(!expanded && getElements().isDecorated());
        }
    }
}
