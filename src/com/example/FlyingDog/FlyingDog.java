package com.example.FlyingDog;

import android.app.Application;
import com.example.FlyingDog.setup.ImageLoaderConfigFactory;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.MediaUpdatingService;
import com.tiksem.media.data.AllSongsTag;
import com.tiksem.media.data.Audio;
import com.tiksem.media.local.AndroidAudioDataBase;
import com.tiksem.media.local.LocalAudioDataBase;
import com.tiksem.media.playback.AudioPlayerService;
import com.tiksem.media.search.InternetSearchEngine;
import com.utils.framework.collections.DifferentlySortedListWithSelectedItem;
import com.utilsframework.android.Services;
import com.utilsframework.android.threading.Tasks;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * User: Tikhonenko.S
 * Date: 07.08.14
 * Time: 19:15
 */
public class FlyingDog extends Application {
    private AudioPlayerService.PlayerBinder playerBinder;
    private AndroidAudioDataBase audioDataBase;
    private InternetSearchEngine internetSearchEngine;
    private AudioDataManager audioDataManager;
    private Queue<Runnable> onPlayerServiceReady = new ArrayDeque<Runnable>();
    private MediaUpdatingService mediaUpdatingService;
    private MediaUpdatingService.OnArtistArtUpdated onArtistArtUpdated;
    private MediaUpdatingService.OnAlbumArtUpdated onAlbumArtUpdated;
    private MediaUpdatingService.OnAlbumOfAudioUpdated onAlbumOfAudioUpdated;

    private static FlyingDog instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        ImageLoader.getInstance().init(ImageLoaderConfigFactory.getCommonImageLoaderConfig(this));

        AudioPlayerService.start(this);
        AudioPlayerService.bind(this, new Services.OnBind<AudioPlayerService.PlayerBinder>() {
            @Override
            public void onBind(Services.Connection<AudioPlayerService.PlayerBinder> connection) {
                playerBinder = connection.getBinder();

                DifferentlySortedListWithSelectedItem<Audio> audios =
                        new DifferentlySortedListWithSelectedItem<Audio>(audioDataBase.getSongs());
                playerBinder.setAudios(audios);
                playerBinder.setPlayListTag(new AllSongsTag());

                Tasks.executeAndClearQueue(onPlayerServiceReady);
                onPlayerServiceReady = null;
            }
        });

        audioDataBase = new AndroidAudioDataBase(getContentResolver());
        internetSearchEngine = new InternetSearchEngine();
        audioDataManager = new AudioDataManager(audioDataBase, internetSearchEngine);
        mediaUpdatingService = new MediaUpdatingService(audioDataBase, internetSearchEngine);

        mediaUpdatingService.updateAllArtistsArt(onArtistArtUpdated);
        audioDataManager.startAlbumArtsUpdating(new LocalAudioDataBase.OnArtsUpdatingFinished() {
            @Override
            public void onFinished() {
                if(onAlbumArtUpdated != null){
                    onAlbumArtUpdated.onAlbumArtUpdated(null);
                }

                mediaUpdatingService.updateAlbumsOfAudios(onAlbumOfAudioUpdated);
                mediaUpdatingService.updateAllAlbumsArt(onAlbumArtUpdated);
            }
        });
    }

    public void executeWhenPlayerServiceIsReady(Runnable runnable) {
        if(playerBinder == null){
            onPlayerServiceReady.add(runnable);
        } else {
            runnable.run();
        }
    }

    public AudioPlayerService.PlayerBinder getPlayerBinder() {
        return playerBinder;
    }

    public AudioDataManager getAudioDataManager() {
        return audioDataManager;
    }

    public MediaUpdatingService getMediaUpdatingService() {
        return mediaUpdatingService;
    }

    public MediaUpdatingService.OnArtistArtUpdated getOnArtistArtUpdated() {
        return onArtistArtUpdated;
    }

    public void setOnArtistArtUpdated(MediaUpdatingService.OnArtistArtUpdated onArtistArtUpdated) {
        this.onArtistArtUpdated = onArtistArtUpdated;
    }

    public MediaUpdatingService.OnAlbumArtUpdated getOnAlbumArtUpdated() {
        return onAlbumArtUpdated;
    }

    public void setOnAlbumArtUpdated(MediaUpdatingService.OnAlbumArtUpdated onAlbumArtUpdated) {
        this.onAlbumArtUpdated = onAlbumArtUpdated;
    }

    public MediaUpdatingService.OnAlbumOfAudioUpdated getOnAlbumOfAudioUpdated() {
        return onAlbumOfAudioUpdated;
    }

    public void setOnAlbumOfAudioUpdated(MediaUpdatingService.OnAlbumOfAudioUpdated onAlbumOfAudioUpdated) {
        this.onAlbumOfAudioUpdated = onAlbumOfAudioUpdated;
    }

    public static FlyingDog getInstance() {
        return instance;
    }
}
