package com.example.FlyingDog.network;

import android.util.Log;
import com.tiksem.media.data.*;
import com.tiksem.media.local.FlyingDogAudioDatabase;
import com.tiksem.media.playback.UrlsProvider;
import com.tiksem.media.search.InternetSearchEngine;
import com.tiksem.media.search.navigation.*;
import com.tiksem.media.search.navigation.albums.AlbumsNavigationList;
import com.tiksem.media.search.navigation.albums.ArtistAlbumsFilterNavigationList;
import com.tiksem.media.search.navigation.albums.ArtistAlbumsNavigationList;
import com.tiksem.media.search.navigation.artists.*;
import com.tiksem.media.search.navigation.songs.*;
import com.tiksem.media.search.network.UrlReport;
import com.tiksem.media.search.updating.UpdateAudioArtTask;
import com.utils.framework.collections.NavigationList;
import com.utils.framework.network.RequestExecutor;
import com.utilsframework.android.network.AsyncRequestExecutorManager;
import com.utilsframework.android.network.OnePageNavigationList;
import com.utilsframework.android.threading.OnFinish;
import com.utilsframework.android.threading.ThrowingRunnable;

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

    private <T> PageNavListParams getPageNavigationListInitialParams(String query) {
        PageNavListParams initParams = new PageNavListParams();
        initParams.query = query;
        initParams.internetSearchEngine = internetSearchEngine;
        initParams.requestManager = this;

        return initParams;
    }

    private <T> MultiTagNavigationList.Params getMultiTagNavigationListParams(String[] tags) {
        MultiTagNavigationList.Params params = new MultiTagNavigationList.Params();
        params.internetSearchEngine = internetSearchEngine;
        params.requestManager = this;
        params.elementsPerPage = 50;
        params.tags = tags;

        return params;
    }

    public RequestManager() {
        internetSearchEngine = new InternetSearchEngine(networkRequestExecutor);
        internetSearchEngine.setLoggingTag(TAG);
    }

    public NavigationList<Audio> searchSongs(String query) {
        return new SongsNavigationList(getPageNavigationListInitialParams(query));
    }

    public NavigationList<Artist> searchArtists(String query) {
        return new ArtistsNavigationList(getPageNavigationListInitialParams(query));
    }

    public NavigationList<Audio> getAudiosOfArtist(Artist artist, String filter) {
        String artistName = artist.getName();
        if (filter == null) {
            return new ArtistSongsNavigationList(getPageNavigationListInitialParams(artistName));
        } else {
            return new ArtistSongsFilterNavigationList(getPageNavigationListInitialParams(filter), artistName);
        }
    }

    public NavigationList<Audio> getAudiosOfAlbum(final Album album) {
        return new OnePageNavigationList<Audio>(this) {
            @Override
            protected List<Audio> load() throws IOException {
                return internetSearchEngine.getSongsOfAlbum(album);
            }
        };
    }

    public List<UrlsProvider> getUrlsData(NavigationList<Audio> audios) {
        return internetSearchEngine.getUrlsProviders(audios);
    }

    public NavigationList<Album> searchAlbums(String query) {
        return new AlbumsNavigationList(getPageNavigationListInitialParams(query));
    }

    public NavigationList<Album> getAlbumsOfArtist(Artist artist, String filter) {
        String artistName = artist.getName();
        if (filter == null) {
            return new ArtistAlbumsNavigationList(getPageNavigationListInitialParams(artistName));
        } else {
            return new ArtistAlbumsFilterNavigationList(getPageNavigationListInitialParams(filter), artistName);
        }
    }

    public void reportWrongUrl(final UrlReport report) {
        execute(new ThrowingRunnable<IOException>() {
            @Override
            public void run() throws IOException {
                networkRequestExecutor.executeRequest(REPORT_URL, report.toQueryArgs());
            }
        }, new OnFinish<IOException>() {
            @Override
            public void onFinish(IOException e) {
                if (e == null) {
                    Log.i(TAG, "reportWrongUrl success = " + report);
                } else {
                    Log.e(TAG, "reportWrongUrl failed");
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

    public NavigationList<Audio> getSongsByTag(String tag, String filter) {
        if (filter == null) {
            return new TagSongsNavigationList(getPageNavigationListInitialParams(tag));
        } else {
            return new SongsFilterTagNavigationList(getPageNavigationListInitialParams(filter), tag);
        }
    }

    public NavigationList<Artist> getArtistsByTag(String tag) {
        return new TagArtistsNavigationList(getPageNavigationListInitialParams(tag));
    }

    public NavigationList<Audio> getSongsByMood(String mood) {
        String[] tags = Mood.getMoodTags(mood);
        return new MultiTagsSongsNavigationList(getMultiTagNavigationListParams(tags));
    }

    public NavigationList<Audio> getSongsByCountry(String country, String filter) {
        String[] tags = Countries.getCountryTags(country);
        if (filter == null) {
            return new MultiTagsSongsNavigationList(getMultiTagNavigationListParams(tags));
        } else {
            return new SongsFilterMultiTagNavigationList(getPageNavigationListInitialParams(filter), tags);
        }
    }

    public NavigationList<Artist> getArtistsByTag(String tag, String filter) {
        if (filter == null) {
            return getArtistsByTag(tag);
        } else {
            return new ArtistsFilterTagNavigationList(getPageNavigationListInitialParams(filter), tag);
        }
    }

    public NavigationList<Artist> getArtistsByCountry(String country, String filter) {
        String[] tags = Countries.getCountryTags(country);
        if (filter == null) {
            return new MultiTagsArtistNavigationList(getMultiTagNavigationListParams(tags));
        } else {
            return new ArtistsFilterMultiTagsNavigationList(getPageNavigationListInitialParams(filter), tags);
        }
    }
}
