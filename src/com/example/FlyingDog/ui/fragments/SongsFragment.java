package com.example.FlyingDog.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import com.example.FlyingDog.R;
import com.example.FlyingDog.network.RequestManager;
import com.example.FlyingDog.network.UrlReport;
import com.example.FlyingDog.sort.SortMenuUtils;
import com.example.FlyingDog.ui.Level;
import com.example.FlyingDog.ui.PlayListsActivity;
import com.example.FlyingDog.ui.adapters.SongsAdapter;
import com.tiksem.media.data.Audio;
import com.tiksem.media.playback.AudioPlayerService;
import com.tiksem.media.playback.PositionChangedListener;
import com.tiksem.media.playback.Status;
import com.tiksem.media.playback.UrlsProvider;
import com.tiksem.media.search.InternetSearchEngine;
import com.tiksem.media.search.parsers.UrlQueryData;
import com.utils.framework.CollectionUtils;
import com.utils.framework.Transformer;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.adapters.ViewArrayAdapter;
import com.utilsframework.android.view.Alerts;

import java.util.List;

/**
 * Created by stykhonenko on 19.10.15.
 */
public abstract class SongsFragment extends AbstractAudioDataFragment<Audio> {
    private static NavigationList<Audio> currentPlayList;

    private List<String> urls;
    private List<UrlsProvider> urlsProviders;
    private View toPlayingNowButton;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onStart() {
        super.onStart();

        final PlayListsActivity activity = getPlayListsActivity();
        activity.executeWhenPlayBackServiceReady(new Runnable() {
            @Override
            public void run() {
                onListViewStateUpdate(activity.getPlayBackService());
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toPlayingNowButton = getActivity().findViewById(R.id.to_playing_now);

        final PlayListsActivity activity = getPlayListsActivity();
        activity.executeWhenPlayBackServiceReady(new Runnable() {
            @Override
            public void run() {
                onPlayBackServiceReady(activity.getPlayBackService());
            }
        });

        registerForContextMenu(getListView());
    }

    private void onPlayBackServiceReady(final AudioPlayerService.Binder playBackService) {
        playBackService.addPositionChangedListener(new PositionChangedListener() {
            @Override
            public void onPositionChanged() {
                onListViewStateUpdate(playBackService);
            }
        });
    }

    protected void onListViewStateUpdate(AudioPlayerService.Binder playBackService) {
        if (urls == null || !CollectionUtils.contentEquals(playBackService.getPlayList(), urls)) {
            if (urlsProviders == null || playBackService.getUrlsProviders() != urlsProviders) {
                getListView().clearChoices();
                return;
            }
        }

        updateListViewCheckedItem(playBackService);
    }

    protected final void updateListViewCheckedItem(AudioPlayerService.Binder playBackService) {
        int position = playBackService.getPosition();
        getListView().setItemChecked(position, true);
    }

    @Override
    protected ViewArrayAdapter<Audio, ?> createAdapter(RequestManager requestManager) {
        return new SongsAdapter(getActivity());
    }

    @Override
    protected final List<Audio> createList() {
        List<Audio> songs = getLocalSongs();
        urlsProviders = null;

        int sortOrder = getSortOrder();
        if (sortOrder != 0) {
            SortMenuUtils.sortAudios(songs, sortOrder);
        }

        urls = CollectionUtils.transformNonCopy(songs, new Transformer<Audio, String>() {
            @Override
            public String get(Audio audio) {
                return audio.getUrl();
            }
        });

        return songs;
    }

    protected abstract List<Audio> getLocalSongs();

    @Override
    protected void onListItemClicked(Audio item, final int position) {
        final PlayListsActivity activity = getPlayListsActivity();
        activity.executeWhenPlayBackServiceReady(new Runnable() {
            @Override
            public void run() {
                AudioPlayerService.Binder playBackService = activity.getPlayBackService();
                if (urls != null) {
                    playBackService.play(urls, position);
                    currentPlayList = getElements();
                } else {
                    playBackService.playUrlsProviders(urlsProviders, position);
                    currentPlayList = getElements();
                }
                toPlayingNowButton.setVisibility(View.GONE);
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
    protected boolean hasSearchMenu() {
        return true;
    }

    @Override
    protected NavigationList<Audio> createNavigationList(String filter) {
        urls = null;
        RequestManager requestManager = getRequestManager();
        NavigationList<Audio> audios = getAudiosFromInternet(filter, requestManager);
        urlsProviders = requestManager.getUrlsData(audios);
        return audios;
    }

    protected abstract NavigationList<Audio> getAudiosFromInternet(String filter, RequestManager requestManager);

    @Override
    protected int getSortMenuId() {
        return R.menu.sort_songs;
    }

    @Override
    protected int getSortMenuGroupId() {
        return R.id.action_sort;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.song_contextual_menu, menu);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Audio audio = getAdapter().getElementOfView(info.targetView);
        boolean isLocal = audio.isLocal();
        menu.findItem(R.id.edit).setVisible(isLocal);
        menu.findItem(R.id.add_to_playlist).setVisible(isLocal);

        MenuItem report = menu.findItem(R.id.report);
        if (!isLocal) {
            AudioPlayerService.Binder playBackService = getPlayBackService();
            if (playBackService == null) {
                report.setVisible(false);
            } else {
                Status status = playBackService.getStatus();
                report.setVisible(status == Status.PAUSED || status == Status.PLAYING);
            }
        } else {
            report.setVisible(false);
        }
    }

    private void reportWrongUrl(int position, String message) {
        Audio audio = getAdapter().getElementByViewPosition(position);
        AudioPlayerService.Binder playBackService = getPlayBackService();
        InternetSearchEngine.VkUrlsProvider urlsProvider =
                (InternetSearchEngine.VkUrlsProvider) playBackService.getUrlsProviders().get(position);
        UrlQueryData data = urlsProvider.getQueryDataList().get(playBackService.getProviderUrlPosition());

        UrlReport report = new UrlReport();
        report.message = message;
        report.queryArtistName = audio.getArtistName();
        report.queryDuration = audio.getDuration();
        report.queryName = audio.getName();
        report.url = data.getUrl();
        report.vkArtistName = data.getArtistName();
        report.vkName = data.getName();
        report.vkDuration = data.getDuration();
        getRequestManager().reportWrongUrl(report);
    }

    private void showReportWrongUrlAlert(final int position) {
        Alerts.InputAlertSettings settings = new Alerts.InputAlertSettings();
        settings.message = R.string.report_wrong_url_dialog;
        settings.cancel = 0;
        settings.onInputOk = new Alerts.OnInputOk() {
            @Override
            public void onOk(String message) {
                reportWrongUrl(position, message);
            }
        };
        Alerts.showAlertWithInput(getActivity(), settings);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.edit:
                return true;
            case R.id.report:
                showReportWrongUrlAlert(info.position);
                return true;
            case R.id.add_to_playlist:
                showAddToPlayListDialog();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void showAddToPlayListDialog() {
        getPlayListsActivity().replaceFragment(new AddToPlayListsFragment(), Level.ADD_TO_PLAYLIST);
    }

    @Override
    protected void onNavigationListChanged(NavigationList<Audio> navigationList) {
        super.onNavigationListChanged(navigationList);

        getPlayListsActivity().executeWhenPlayBackServiceReady(new Runnable() {
            @Override
            public void run() {
                onListViewStateUpdate(getPlayListsActivity().getPlayBackService());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        toPlayingNowButton.setVisibility(View.VISIBLE);
    }

    protected static NavigationList<Audio> getCurrentPlayList() {
        return currentPlayList;
    }

    protected final View getToPlayingNowButton() {
        return toPlayingNowButton;
    }
}
