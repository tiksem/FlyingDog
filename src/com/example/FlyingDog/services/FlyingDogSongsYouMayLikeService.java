package com.example.FlyingDog.services;

import android.content.Context;
import com.example.FlyingDog.FlyingDog;
import com.example.FlyingDog.network.FlyingDogRequestExecutor;
import com.tiksem.media.data.Audio;
import com.tiksem.media.local.FlyingDogAudioDatabase;
import com.tiksem.media.search.syouml.SongsYouMayLikeService;
import com.utils.framework.Lists;
import com.utils.framework.network.RequestExecutor;
import com.utilsframework.android.Services;

import java.util.List;

/**
 * Created by stykhonenko on 28.10.15.
 */
public class FlyingDogSongsYouMayLikeService extends SongsYouMayLikeService {
    @Override
    protected RequestExecutor createRequestExecutor() {
        return new FlyingDogRequestExecutor();
    }

    @Override
    protected List<Audio> getUserPlayList() {
        FlyingDogAudioDatabase audioDataBase = FlyingDog.getInstance().getAudioDataBase();
        return Lists.concat(audioDataBase.getSongs(),
                audioDataBase.getInternetAudios());
    }

    public static void bindAndStart(Context context,
                                    Services.OnBind<Binder> onBind) {
        Services.start(context, FlyingDogSongsYouMayLikeService.class);
        Services.bind(context, FlyingDogSongsYouMayLikeService.class, onBind);
    }
}
