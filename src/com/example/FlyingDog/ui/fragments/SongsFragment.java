package com.example.FlyingDog.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.example.FlyingDog.R;
import com.example.FlyingDog.sort.SortMenuUtils;
import com.example.FlyingDog.ui.PlayListsActivity;
import com.example.FlyingDog.ui.adapters.SongsAdapter;
import com.tiksem.media.data.Audio;
import com.tiksem.media.playback.AudioPlayerService;
import com.tiksem.media.playback.PositionChangedListener;
import com.utils.framework.CollectionUtils;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.adapters.ViewArrayAdapter;
import com.utilsframework.android.adapters.navigation.NavigationListAdapter;
import com.utilsframework.android.network.AsyncRequestExecutorManager;

import java.util.List;

/**
 * Created by stykhonenko on 19.10.15.
 */
public class SongsFragment extends AbstractPlayListFragment<Audio> {
    private List<Audio> songs;
    private List<String> urls;

    @Override
    public void onStart() {
        super.onStart();

        final PlayListsActivity activity = getPlayListsActivity();
        activity.executeWhenPlayBackServiceReady(new Runnable() {
            @Override
            public void run() {
                updateListViewCheckedItemIfNeed(activity.getPlayBackService());
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final PlayListsActivity activity = getPlayListsActivity();
        activity.executeWhenPlayBackServiceReady(new Runnable() {
            @Override
            public void run() {
                onPlayBackServiceReady(activity.getPlayBackService());
            }
        });
    }

    private void onPlayBackServiceReady(final AudioPlayerService.Binder playBackService) {
        playBackService.addPositionChangedListener(new PositionChangedListener() {
            @Override
            public void onPositionChanged() {
                updateListViewCheckedItemIfNeed(playBackService);
            }
        });
    }

    private void updateListViewCheckedItemIfNeed(AudioPlayerService.Binder playBackService) {
        if (!CollectionUtils.contentEquals(playBackService.getPlayList(), urls)) {
            return;
        }

        int position = playBackService.getPosition();
        getListView().setItemChecked(position, true);
    }

    @Override
    protected ViewArrayAdapter<Audio, ?> createAdapter(AsyncRequestExecutorManager requestManager) {
        return new SongsAdapter(getActivity());
    }

    @Override
    protected final List<Audio> createList() {
        songs = getSongs();

        int sortOrder = getSortOrder();
        if (sortOrder != 0) {
            SortMenuUtils.sortAudios(songs, sortOrder);
        }

        urls = CollectionUtils.transformNonCopy(songs, new CollectionUtils.Transformer<Audio, String>() {
            @Override
            public String get(Audio audio) {
                return audio.getUrl();
            }
        });

        return songs;
    }

    protected List<Audio> getSongs() {
        return audioDataBase.getSongs();
    }

    @Override
    protected void onListItemClicked(Audio item, final int position) {
        final PlayListsActivity activity = getPlayListsActivity();
        activity.executeWhenPlayBackServiceReady(new Runnable() {
            @Override
            public void run() {
                activity.getPlayBackService().play(urls, position);
            }
        });
    }

    @Override
    public void onSortOrderChanged(int newSortOrder) {
        super.onSortOrderChanged(newSortOrder);

        AudioPlayerService.Binder playBackService = getPlayBackService();
        if (playBackService != null && playBackService.getPlayList() != null) {
            playBackService.changePlayList(urls);
        }
    }

    @Override
    protected int getSortMenuId() {
        return R.menu.sort_songs;
    }

    @Override
    protected int getSortMenuGroupId() {
        return R.id.action_sort;
    }
}
