package com.example.FlyingDog.network;

import android.util.Log;
import com.tiksem.media.data.Audio;
import com.tiksem.media.search.InternetSearchEngine;
import com.tiksem.media.search.navigation.PageNavigationList;
import com.tiksem.media.search.navigation.SongsNavigationList;
import com.utils.framework.collections.NavigationList;
import com.utils.framework.io.Network;
import com.utils.framework.network.GetRequestExecutor;
import com.utils.framework.network.RequestExecutor;
import com.utilsframework.android.ExecuteTimeLogger;
import com.utilsframework.android.network.AsyncRequestExecutorManager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by stykhonenko on 22.10.15.
 */
public class RequestManager extends AsyncRequestExecutorManager {
    public static final String TAG = "RequestManager";

    private static final RequestExecutor networkRequestExecutor = new GetRequestExecutor() {
        @Override
        public String executeRequest(String url, Map<String, Object> args) throws IOException {
            Log.i(TAG, "url = " + Network.getUrl(url, args));
            ExecuteTimeLogger.timeStart("request");
            String result = super.executeRequest(url, args);
            ExecuteTimeLogger.timeEnd("request", TAG);
            return result;
        }
    };

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
}