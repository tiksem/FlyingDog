package com.example.FlyingDog.ui.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import com.example.FlyingDog.R;
import com.example.FlyingDog.network.RequestManager;
import com.tiksem.media.data.Audio;
import com.tiksem.media.data.PlayList;
import com.tiksem.media.playback.AudioPlayerService;
import com.tiksem.media.playback.Status;
import com.tiksem.media.playback.UrlsProvider;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.fragments.Fragments;
import com.utilsframework.android.threading.OnFinish;
import com.utilsframework.android.threading.Threading;
import com.utilsframework.android.threading.ThrowingRunnable;
import com.utilsframework.android.view.Alerts;

import java.io.IOException;
import java.util.List;

/**
 * Created by stykhonenko on 28.10.15.
 */
public class PlayListSongsFragment extends SongsOfFragment {
    private static final String PLAYLIST = "PLAYLIST";

    private PlayList playList;

    public static PlayListSongsFragment create(PlayList playList) {
        return Fragments.createFragmentWith1Arg(new PlayListSongsFragment(), PLAYLIST, playList);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        playList = getArguments().getParcelable(PLAYLIST);
    }

    @Override
    protected List<Audio> getLocalSongs() {
        return audioDataBase.getSongsOfPlayList(playList);
    }

    @Override
    protected NavigationList<Audio> getAudiosFromInternet(String filter, RequestManager requestManager) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void onCreateContextMenu(ContextMenu menu, Audio audio, int position) {
        super.onCreateContextMenu(menu, audio, position);

        MenuItem removeSongItem = menu.findItem(R.id.remove_from_playlist);
        boolean visible = true;
        AudioPlayerService.Binder playBackService = getPlayBackService();
        if (playBackService != null) {
            if (getCurrentPlayList() == getElements()) {
                visible = playBackService.getPosition() != position;
            }
        }

        removeSongItem.setVisible(visible);
    }

    @Override
    protected boolean onContextItemSelected(MenuItem item, View targetView, int position, final Audio audio) {
        if (item.getItemId() == R.id.remove_from_playlist) {
            removeSongFromPlayList(audio, position);
            return true;
        }

        return super.onContextItemSelected(item, targetView, position, audio);
    }

    private void removeSongFromPlayList(final Audio audio, final int position) {
        final ProgressDialog progressDialog = Alerts.showCircleProgressDialog(getActivity(), R.string.please_wait);
        getRequestManager().execute(new Threading.Task<IOException, Object>() {
            @Override
            public Object runOnBackground() throws IOException {
                audioDataBase.removeSongFromPlayList(audio, playList);
                return null;
            }

            @Override
            public void onComplete(Object o, IOException error) {
                updateNavigationListWithLastFilter();
                changePlayListIfPlayingCurrent(0);
            }

            @Override
            public void onAfterCompleteOrCancelled() {
                progressDialog.dismiss();
            }
        });
    }
}
