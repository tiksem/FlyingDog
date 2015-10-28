package com.example.FlyingDog.network;

import android.util.Log;
import com.utils.framework.io.Network;
import com.utils.framework.network.GetRequestExecutor;
import com.utilsframework.android.ExecuteTimeLogger;

import java.io.IOException;
import java.util.Map;

/**
 * Created by stykhonenko on 28.10.15.
 */
public class FlyingDogRequestExecutor extends GetRequestExecutor {
    @Override
    public String executeRequest(String url, Map<String, Object> args) throws IOException {
        Log.i(RequestManager.TAG, "url = " + Network.getUrl(url, args));
        ExecuteTimeLogger.timeStart("request");
        String result = super.executeRequest(url, args);
        ExecuteTimeLogger.timeEnd("request", RequestManager.TAG);
        return result;
    }
}
