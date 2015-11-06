package com.example.FlyingDog.network;

import com.utilsframework.android.time.TimeUtils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by stykhonenko on 27.10.15.
 */
public class UrlReport {
    public String queryName;
    public String queryArtistName;
    public int queryDuration;
    public String vkName;
    public String vkArtistName;
    public int vkDuration;
    public String url;
    public String message;
    public long time;

    public UrlReport() {
        time = TimeUtils.getCurrentHumanReadableDateAndTimeAsLong();
    }

    public Map<String, Object> toQueryArgs() {
        Map<String, Object> args = new HashMap<>();
        args.put("query_name", queryName);
        args.put("query_artistname", queryArtistName);
        args.put("time", time);
        args.put("message", message);
        args.put("vk_name", vkName);
        args.put("vk_artistname", vkArtistName);
        args.put("query_duration", queryDuration);
        args.put("vk_duration", vkDuration);
        args.put("url", url);
        return args;
    }

    @Override
    public String toString() {
        return "UrlReport{" +
                "queryName='" + queryName + '\'' +
                ", queryArtistName='" + queryArtistName + '\'' +
                ", queryDuration=" + queryDuration +
                ", vkName='" + vkName + '\'' +
                ", vkArtistName='" + vkArtistName + '\'' +
                ", vkDuration=" + vkDuration +
                ", url='" + url + '\'' +
                ", message='" + message + '\'' +
                ", time=" + time +
                '}';
    }
}
