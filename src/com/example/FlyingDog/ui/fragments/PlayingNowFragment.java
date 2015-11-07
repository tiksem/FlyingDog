package com.example.FlyingDog.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AbsListView;
import com.example.FlyingDog.R;
import com.example.FlyingDog.network.RequestManager;
import com.tiksem.media.data.Audio;
import com.tiksem.media.playback.AudioPlayerService;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.navdrawer.ActionBarTitleProvider;
import com.utilsframework.android.view.listview.ListViews;

import java.util.List;

/**
 * Created by stykhonenko on 27.10.15.
 */
public class PlayingNowFragment extends SongsFragment implements ActionBarTitleProvider {
    private boolean firstListViewUpdate = true;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getToPlayingNowButton().setVisibility(View.GONE);
    }

    @Override
    protected NavigationList<Audio> getNavigationList(String filter) {
        return getCurrentPlayList();
    }

    @Override
    protected boolean hasSearchMenu() {
        return false;
    }

    @Override
    public String getActionBarTitle() {
        return getString(R.string.playing_now_title);
    }

    @Override
    protected List<Audio> getLocalSongs() {
        throw new RuntimeException("WTF?");
    }

    @Override
    protected NavigationList<Audio> getAudiosFromInternet(String filter, RequestManager requestManager) {
        throw new RuntimeException("WTF?");
    }

    @Override
    protected void onUpdateSelectedItem(AudioPlayerService.Binder playBackService) {
        super.onUpdateSelectedItem(playBackService);

        if (firstListViewUpdate) {
            AbsListView listView = getListView();
            ListViews.scrollListViewToPosition(listView, listView.getCheckedItemPosition());
        } else {
            firstListViewUpdate = false;
        }
    }
}
