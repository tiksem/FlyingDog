package com.example.FlyingDog;

import android.app.Application;
import com.example.FlyingDog.setup.ImageLoaderConfigFactory;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tiksem.media.local.AndroidAudioDataBase;
import com.tiksem.media.local.AudioDataBase;
import com.tiksem.media.playback.AudioPlayerService;
import com.utilsframework.android.Services;

/**
 * User: Tikhonenko.S
 * Date: 07.08.14
 * Time: 19:15
 */
public class FlyingDog extends Application {
    private static FlyingDog instance;
    private AudioDataBase audioDataBase;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        ImageLoader.getInstance().init(ImageLoaderConfigFactory.getCommonImageLoaderConfig(this));
        audioDataBase = new AndroidAudioDataBase(getContentResolver());
    }

    public static FlyingDog getInstance() {
        return instance;
    }

    public AudioDataBase getAudioDataBase() {
        return audioDataBase;
    }
}
