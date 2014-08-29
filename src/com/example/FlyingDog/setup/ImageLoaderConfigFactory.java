package com.example.FlyingDog.setup;

import android.content.Context;
import android.graphics.Bitmap;
import com.example.FlyingDog.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created with IntelliJ IDEA.
 * User: CM
 * Date: 15.01.13
 * Time: 22:40
 * To change this template use File | Settings | File Templates.
 */
public class ImageLoaderConfigFactory {
    public static ImageLoaderConfiguration getCommonImageLoaderConfig(Context context){
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);

        config.diskCacheSize(1024 * 1024 * 50);
        config.denyCacheImageMultipleSizesInMemory();
        config.memoryCacheSize(1024*1024*2);
        config.tasksProcessingOrder(QueueProcessingType.LIFO/*stack*/);
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.threadPoolSize(1);

        DisplayImageOptions.Builder displayImageOptions = new DisplayImageOptions.Builder();
        displayImageOptions.bitmapConfig(Bitmap.Config.RGB_565);
        displayImageOptions.cacheInMemory(true);
        displayImageOptions.cacheOnDisk(true);
        displayImageOptions.showImageForEmptyUri(R.drawable.ic_launcher);
        displayImageOptions.resetViewBeforeLoading(true);
        displayImageOptions.showImageOnLoading(R.drawable.ic_launcher);
        config.defaultDisplayImageOptions(displayImageOptions.build());

        return config.build();
    }
}