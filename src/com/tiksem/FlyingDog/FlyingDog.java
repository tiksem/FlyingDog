package com.tiksem.FlyingDog;

import android.app.Application;
import android.net.Uri;
import android.provider.Settings;
import com.tiksem.FlyingDog.services.FlyingDogArtsUpdatingService;
import com.tiksem.FlyingDog.setup.ImageLoaderConfigFactory;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tiksem.media.local.FlyingDogAudioDatabase;

/**
 * User: Tikhonenko.S
 * Date: 07.08.14
 * Time: 19:15
 */
public class FlyingDog extends Application {
    private static FlyingDog instance;
    private FlyingDogAudioDatabase audioDataBase;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Uri smsDefaultApplication = Settings.Secure.getUriFor("sms_default_application");

        ImageLoader.getInstance().init(ImageLoaderConfigFactory.getCommonImageLoaderConfig(this));
        audioDataBase = new FlyingDogAudioDatabase(this);
        FlyingDogArtsUpdatingService.updateAudioArts(this);
    }

    public static FlyingDog getInstance() {
        return instance;
    }

    public FlyingDogAudioDatabase getAudioDataBase() {
        return audioDataBase;
    }
}
