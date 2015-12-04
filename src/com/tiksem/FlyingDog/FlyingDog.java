package com.tiksem.FlyingDog;

import android.app.Application;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import com.tiksem.FlyingDog.services.FlyingDogArtsUpdatingService;
import com.tiksem.FlyingDog.setup.ImageLoaderConfigFactory;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tiksem.media.local.FlyingDogAudioDatabase;

import java.util.HashSet;
import java.util.Set;

/**
 * User: Tikhonenko.S
 * Date: 07.08.14
 * Time: 19:15
 */
public class FlyingDog extends Application {
    private static FlyingDog instance;
    private FlyingDogAudioDatabase audioDataBase;
    private Set<OnDataBaseChanged> onDataBaseChangedListeners = new HashSet<>();

    public interface OnDataBaseChanged {
        void onChanged();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        ImageLoader.getInstance().init(ImageLoaderConfigFactory.getCommonImageLoaderConfig(this));
        audioDataBase = new FlyingDogAudioDatabase(this);
        FlyingDogArtsUpdatingService.updateAudioArts(this);

        final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        getContentResolver().registerContentObserver(uri, true, new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                if (!selfChange) {
                    onDataBaseChangesDetected();
                }
            }
        });
    }

    private void onDataBaseChangesDetected() {
        new AsyncTask<Void, Void, FlyingDogAudioDatabase>() {
            @Override
            protected FlyingDogAudioDatabase doInBackground(Void... params) {
                return new FlyingDogAudioDatabase(instance);
            }

            @Override
            protected void onPostExecute(FlyingDogAudioDatabase audioDatabase) {
                instance.audioDataBase = audioDatabase;

                for (OnDataBaseChanged onDataBaseChanged : onDataBaseChangedListeners) {
                    onDataBaseChanged.onChanged();
                }
            }
        }.execute();
    }

    public static FlyingDog getInstance() {
        return instance;
    }

    public FlyingDogAudioDatabase getAudioDataBase() {
        return audioDataBase;
    }

    public void addDataBaseChangedListener(OnDataBaseChanged onDataBaseChanged) {
        onDataBaseChangedListeners.add(onDataBaseChanged);
    }

    public void removeDataBaseChangedListener(OnDataBaseChanged onDataBaseChanged) {
        onDataBaseChangedListeners.remove(onDataBaseChanged);
    }
}
