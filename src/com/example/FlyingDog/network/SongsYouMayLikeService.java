package com.example.FlyingDog.network;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import com.example.FlyingDog.FlyingDog;
import com.tiksem.media.data.Audio;
import com.tiksem.media.search.InternetSearchEngine;
import com.tiksem.media.search.navigation.SongsYouMayLikeNavigationList;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.Services;
import com.utilsframework.android.network.*;
import com.utilsframework.android.network.RequestManager;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by stykhonenko on 28.10.15.
 */
public class SongsYouMayLikeService extends Service {
    private static final int MAX_SONGS_YOU_MAY_LIKE_COUNT = 500;
    private static final int SIMILAR_TRACKS_PER_PAGE_COUNT = 30;
    private SongsYouMayLikeNavigationList navigationList;
    private ExecutorService executor;
    private RequestManager requestManager;

    public class Binder extends android.os.Binder implements Services.OnUnbind {
        public NavigationList<Audio> getSongsYouMayLike() {
            if (navigationList == null) {
                SongsYouMayLikeNavigationList.Params params = new SongsYouMayLikeNavigationList.Params();
                params.internetSearchEngine = new InternetSearchEngine(new FlyingDogRequestExecutor());
                params.maxCount = MAX_SONGS_YOU_MAY_LIKE_COUNT;
                params.songsCountPerPage = SIMILAR_TRACKS_PER_PAGE_COUNT;
                params.userPlaylist = FlyingDog.getInstance().getAudioDataBase().getSongs();

                if (executor == null) {
                    executor = Executors.newSingleThreadExecutor();
                }
                requestManager = params.requestManager = new AsyncRequestExecutorManager(executor);

                navigationList = new SongsYouMayLikeNavigationList(params);
            }

            return navigationList;
        }

        @Override
        public void onUnbind() {
            if (navigationList != null) {
                continueLoadingOnBackground();
            }
        }

        public void continueLoadingOnBackground() {
            navigationList.loadNextPage(new NavigationList.OnPageLoadingFinished<Audio>() {
                @Override
                public void onLoadingFinished(List<Audio> elements) {
                    if (!navigationList.isAllDataLoaded()) {
                        navigationList.loadNextPage();
                    }
                }
            });
        }
    }

    public static void bindAndStart(Context context, Services.OnBind<Binder> onBind) {
        Services.start(context, SongsYouMayLikeService.class);
        Services.bind(context, SongsYouMayLikeService.class, onBind);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requestManager.cancelAll();
        executor.shutdown();
    }
}
