package com.example.FlyingDog.services;

import android.content.Context;
import com.example.FlyingDog.FlyingDog;
import com.example.FlyingDog.network.FlyingDogRequestExecutor;
import com.tiksem.media.local.FlyingDogAudioDatabase;
import com.tiksem.media.search.updating.ArtUpdatingService;
import com.utils.framework.network.RequestExecutor;

/**
 * Created by stykhonenko on 30.10.15.
 */
public class FlyingDogArtsUpdatingService extends ArtUpdatingService {
    @Override
    protected RequestExecutor getRequestExecutor() {
        return new FlyingDogRequestExecutor();
    }

    @Override
    protected FlyingDogAudioDatabase getAudioDatabase() {
        return FlyingDog.getInstance().getAudioDataBase();
    }

    public static void updateAudioArts(Context context) {
        updateAudioArts(context, FlyingDogArtsUpdatingService.class);
    }
}
