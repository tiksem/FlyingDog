package com.example.FlyingDog.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.*;
import android.widget.TextView;
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
    private TextView emptyViewText;

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

    private void initEmptyTextViewIfNeed() {
        if (emptyViewText == null) {
            emptyViewText = (TextView) ((ViewGroup) getEmptyView()).getChildAt(0);
        }
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
        if (!alwaysGetListFromInternet() && filter == null) {
            List<T> localList = createLocalList();
            return NavigationList.decorate(localList);
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

    protected boolean alwaysGetListFromInternet() {
        return false;
    }

    @Override
    protected void onSearchViewExpandCollapse(Menu menu, boolean expanded) {
        super.onSearchViewExpandCollapse(menu, expanded);
        if (sortMenuItem != null) {
            sortMenuItem.setVisible(!expanded && getElements().isDecorated());
        }

        getPlayListsActivity().dismissHelpToast();
    }

    @Override
    protected int getSortMenuGroupId() {
        return R.id.action_sort;
    }

    @Override
    protected final void onListItemClicked(T item, int position) {
        getPlayListsActivity().dismissHelpToast();
        onItemSelected(item, position);
    }

    protected void onItemSelected(T item, int position) {

    }

    @Override
    protected void onEmptyViewIsShown() {
        initEmptyTextViewIfNeed();
        emptyViewText.setText(getEmptyListText());
    }

    @Override
    protected int getEmptyListResourceId() {
        return R.id.empty_list;
    }

    protected CharSequence getEmptyListText() {
        return getString(R.string.no_music_found);
    }

    @Override
    protected int getRetryLoadingButtonId() {
        return R.id.retry;
    }
}
