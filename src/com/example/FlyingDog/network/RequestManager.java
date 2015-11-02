package com.example.FlyingDog.network;

import com.tiksem.media.data.Album;
import com.tiksem.media.data.Artist;
import com.tiksem.media.data.Audio;
import com.tiksem.media.local.FlyingDogAudioDatabase;
import com.tiksem.media.playback.UrlsProvider;
import com.tiksem.media.search.InternetSearchEngine;
import com.tiksem.media.search.navigation.*;
import com.tiksem.media.search.updating.UpdateAudioArtTask;
import com.utils.framework.collections.NavigationList;
import com.utils.framework.network.RequestExecutor;
import com.utilsframework.android.network.AsyncRequestExecutorManager;
import com.utilsframework.android.network.OnePageNavigationList;

import java.io.IOException;
import java.util.List;

/**
 * Created by stykhonenko on 22.10.15.
 */
public class RequestManager extends AsyncRequestExecutorManager {
    public static final String TAG = "RequestManager";
    private static final String REPORT_URL = "http://azazai.com/api/reportWrongUrl";

    private static final RequestExecutor networkRequestExecutor = new FlyingDogRequestExecutor();

    private InternetSearchEngine internetSearchEngine;

    private <T> PageNavigationList.InitParams getPageNavigationListInitialParams(String query) {
        PageNavigationList.InitParams initParams = new PageNavigationList.InitParams();
        initParams.query = query;
        initParams.internetSearchEngine = internetSearchEngine;
        initParams.requestManager = this;

        return initParams;
    }

    public RequestManager() {
        internetSearchEngine = new InternetSearchEngine(networkRequestExecutor);
    }

    public NavigationList<Audio> searchSongs(String query) {
        return new SongsNavigationList(getPageNavigationListInitialParams(query));
    }

    public NavigationList<Artist> searchArtists(String query) {
        return new ArtistsNavigationList(getPageNavigationListInitialParams(query));
    }

    public NavigationList<Audio> getAudiosOfArtist(Artist artist) {
        return new ArtistSongsNavigationList(getPageNavigationListInitialParams(artist.getName()));
    }

    public NavigationList<Audio> getAudiosOfAlbum(final Album album) {
        return new OnePageNavigationList<Audio>(this) {
            @Override
            protected List<Audio> load() throws IOException {
                return internetSearchEngine.getSongsOfAlbum(album);
            }
        };
    }

    public List<UrlsProvider> getUrlsData(List<Audio> audios) {
        return internetSearchEngine.getUrlsProviders(audios);
    }

    public NavigationList<Album> searchAlbums(String query) {
        return new AlbumsNavigationList(getPageNavigationListInitialParams(query));
    }

    public NavigationList<Album> getAlbumsOfArtist(Artist artist) {
        return new ArtistAlbumsNavigationList(getPageNavigationListInitialParams(artist.getName()));
    }

    public void reportWrongUrl(final UrlReport report) {
        execute(new Runnable() {
            @Override
            public void run() {
                try {
                    networkRequestExecutor.executeRequest(REPORT_URL, report.toQueryArgs());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public interface OnUpdated {
        void onUpdated();
    }

    public void updateAudioArt(FlyingDogAudioDatabase audioDatabase,
                               Audio audio, final OnUpdated onUpdated) {
        execute(new UpdateAudioArtTask(internetSearchEngine, audioDatabase, audio) {
            @Override
            protected void onUpdated() {
                onUpdated.onUpdated();
            }
        });
    }
}
