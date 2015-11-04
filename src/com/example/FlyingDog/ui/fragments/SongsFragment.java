package com.example.FlyingDog.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import com.example.FlyingDog.R;
import com.example.FlyingDog.network.RequestManager;
import com.example.FlyingDog.network.UrlReport;
import com.example.FlyingDog.sort.SortMenuUtils;
import com.example.FlyingDog.ui.Level;
import com.example.FlyingDog.ui.PlayListsActivity;
import com.example.FlyingDog.ui.adapters.SongsAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tiksem.media.data.ArtSize;
import com.tiksem.media.data.Audio;
import com.tiksem.media.playback.AudioPlayerService;
import com.tiksem.media.playback.PositionChangedListener;
import com.tiksem.media.playback.Status;
import com.tiksem.media.playback.UrlsProvider;
import com.tiksem.media.search.InternetSearchEngine;
import com.tiksem.media.search.parsers.UrlQueryData;
import com.utils.framework.CollectionUtils;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.adapters.ViewArrayAdapter;
import com.utilsframework.android.view.Alerts;

import java.util.List;

/**
 * Created by stykhonenko on 19.10.15.
 */
public abstract class SongsFragment extends AbstractAudioDataFragment<Audio> {
    private static NavigationList<Audio> currentPlayList;

    private View toPlayingNowButton;
    private List<UrlsProvider> urlsProviders;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
        if (currentPlayList == null) {
            return;
        }

        NavigationList<Audio> elements = getElements();
        if (elements == null) {
            return;
        }

        if (elements != currentPlayList) {
            if (!elements.isDecorated()) {
                return;
            }

            if (!currentPlayList.isDecorated()) {
                return;
            }

            if (!CollectionUtils.contentEquals(currentPlayList, elements)) {
                return;
            }
        }

        checkListViewItem(playBackService);
    }

    protected final void checkListViewItem(AudioPlayerService.Binder playBackService) {
        int position = playBackService.getPosition();
        getListView().setItemChecked(position, true);
    }

    @Override
    protected ViewArrayAdapter<Audio, ?> createAdapter(RequestManager requestManager) {
        return new SongsAdapter(getActivity());
    }

    @Override
    protected final List<Audio> createLocalList() {
        return getLocalSongs();
    }

    @Override
    protected void sort(List<Audio> songs, int sortingOrder) {
        SortMenuUtils.sortAudios(songs, sortingOrder);
    }

    protected abstract List<Audio> getLocalSongs();

    @Override
    protected void onListItemClicked(Audio item, final int position) {
        final PlayListsActivity activity = getPlayListsActivity();
        activity.executeWhenPlayBackServiceReady(new Runnable() {
            @Override
            public void run() {
                AudioPlayerService.Binder playBackService = activity.getPlayBackService();
                playBackService.playUrlsProviders(urlsProviders, position);
                currentPlayList = getElements();
                toPlayingNowButton.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onSortOrderChanged(int newSortOrder) {
        super.onSortOrderChanged(newSortOrder);

        AudioPlayerService.Binder playBackService = getPlayBackService();
        if (playBackService != null) {
            if (getListView().getCheckedItemPosition() >= 0) {
                currentPlayList = getElements();
                playBackService.changePlayListProviders(urlsProviders);
                toPlayingNowButton.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected boolean hasSearchMenu() {
        return true;
    }

    @Override
    protected NavigationList<Audio> createInternetList(String filter) {
        return getAudiosFromInternet(filter, getRequestManager());
    }

    @Override
    protected NavigationList<Audio> getNavigationList(String filter) {
        NavigationList<Audio> navigationList = super.getNavigationList(filter);
        urlsProviders = getRequestManager().getUrlsData(navigationList);
        return navigationList;
    }

    protected abstract NavigationList<Audio> getAudiosFromInternet(String filter, RequestManager requestManager);

    @Override
    protected int getSortMenuId() {
        return R.menu.sort_songs;
    }

    @Override
    public final void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.song_contextual_menu, menu);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Audio audio = getAdapter().getElementOfView(info.targetView);
        onCreateContextMenu(menu, audio, info.position);
    }

    protected void onCreateContextMenu(ContextMenu menu, Audio audio, int position) {
        boolean isLocal = audio.isLocal();

        menu.findItem(R.id.edit).setVisible(isLocal);
        boolean updateAlbumArtVisible = isLocal && audio.getArtUrl(ArtSize.SMALL) == null;
        updateAlbumArtVisible = false; // Implement later
        menu.findItem(R.id.update_album_art).setVisible(updateAlbumArtVisible);
        MenuItem addToPlayList = menu.findItem(R.id.add_to_playlist);
        addToPlayList.setVisible(isLocal);

        MenuItem report = menu.findItem(R.id.report);
        if (!isLocal) {
            AudioPlayerService.Binder playBackService = getPlayBackService();
            if (playBackService == null) {
                report.setVisible(false);
            } else {
                Status status = playBackService.getStatus();
                boolean activeStatus = status == Status.PAUSED || status == Status.PLAYING;
                report.setVisible(activeStatus);
                addToPlayList.setVisible(activeStatus);
            }
        } else {
            report.setVisible(false);
        }
    }

    private void reportWrongUrl(Audio audio, int position, String message) {
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

    private void showReportWrongUrlAlert(final Audio audio, final int position) {
        Alerts.InputAlertSettings settings = new Alerts.InputAlertSettings();
        settings.message = R.string.report_wrong_url_dialog;
        settings.cancel = 0;
        settings.onInputOk = new Alerts.OnInputOk() {
            @Override
            public void onOk(String message) {
                reportWrongUrl(audio, position, message);
            }
        };
        Alerts.showAlertWithInput(getActivity(), settings);
    }

    private void updateAlbumArt(final View view, final Audio audio) {
        getRequestManager().updateAudioArt(audioDataBase, audio, new RequestManager.OnUpdated() {
            @Override
            public void onUpdated() {
                ImageView art = (ImageView) view.findViewById(R.id.art);
                String artUrl = audio.getArtUrl(ArtSize.SMALL);
                ImageLoader.getInstance().displayImage(artUrl, art);
            }
        });
    }

    @Override
    public final boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        Audio audio = getAdapter().getElement(position);

        return onContextItemSelected(item, info.targetView, position, audio);
    }

    protected boolean onContextItemSelected(MenuItem item,
                                          View targetView,
                                          int position, Audio audio) {
        switch (item.getItemId()) {
            case R.id.edit:
                getPlayListsActivity().replaceFragment(EditAudioFragment.create(audio), Level.EDIT_AUDIO);
                return true;
            case R.id.report:
                showReportWrongUrlAlert(audio, position);
                return true;
            case R.id.add_to_playlist:
                showAddToPlayListDialog(audio);
                return true;
            case R.id.update_album_art:
                updateAlbumArt(targetView, audio);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void showAddToPlayListDialog(Audio audio) {
        if (!audio.isLocal()) {
            String url = getPlayBackService().getCurrentUrl();
            audio.setUrl(url);
        }

        getPlayListsActivity().replaceFragment(AddToPlayListsFragment.create(audio), Level.ADD_TO_PLAYLIST);
    }

    @Override
    protected void onNavigationListChanged(final NavigationList<Audio> navigationList) {
        super.onNavigationListChanged(navigationList);

        getPlayListsActivity().executeWhenPlayBackServiceReady(new Runnable() {
            @Override
            public void run() {
                onListViewStateUpdate(getPlayListsActivity().getPlayBackService());
                if (toPlayingNowButton != null) {
                    if (currentPlayList != navigationList) {
                        toPlayingNowButton.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        resetPlayerIfPreparing();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        toPlayingNowButton.setVisibility(View.VISIBLE);

        resetPlayerIfPreparing();
    }

    private void resetPlayerIfPreparing() {
        AudioPlayerService.Binder playBackService = getPlayBackService();
        if (playBackService != null && playBackService.getStatus() == Status.PREPARING) {
            playBackService.reset();
        }
    }

    protected static NavigationList<Audio> getCurrentPlayList() {
        return currentPlayList;
    }

    protected final View getToPlayingNowButton() {
        return toPlayingNowButton;
    }
}
