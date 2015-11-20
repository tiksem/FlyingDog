package com.tiksem.FlyingDog.network;

import android.util.Log;
import com.utils.framework.io.Network;
import com.utils.framework.network.GetRequestExecutor;
import com.utilsframework.android.ExecuteTimeLogger;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by stykhonenko on 28.10.15.
 */
public class FlyingDogRequestExecutor extends GetRequestExecutor {
    private static AtomicInteger requestsCount = new AtomicInteger(0);

    @Override
    public String executeRequest(String url, Map<String, Object> args) throws IOException {
        Log.i(RequestManager.TAG, "requests count = " + requestsCount.incrementAndGet());
        Log.i(RequestManager.TAG, "url = " + Network.getUrl(url, args));
        ExecuteTimeLogger.timeStart("request");
        String result;
        try {
            result = super.executeRequest(url, args);
        } finally {
            ExecuteTimeLogger.timeEnd("request", RequestManager.TAG);
            Log.i(RequestManager.TAG, "requests count = " + requestsCount.decrementAndGet());
        }
        return result;
    }
}
