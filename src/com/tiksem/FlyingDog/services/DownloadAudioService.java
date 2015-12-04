package com.tiksem.FlyingDog.services;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import com.tiksem.FlyingDog.FlyingDog;
import com.tiksem.SongsYouMayLike.R;
import com.tiksem.media.data.ArtSize;
import com.tiksem.media.data.Audio;
import com.tiksem.media.local.FlyingDogAudioDatabase;
import com.utils.framework.CollectionUtils;
import com.utils.framework.Predicate;
import com.utilsframework.android.bitmap.BitmapUtilities;
import com.utilsframework.android.network.DownloadFileService;
import com.utilsframework.android.view.Toasts;

import java.io.File;
import java.io.IOException;

/**
 * Created by stykhonenko on 03.12.15.
 */
public class DownloadAudioService extends DownloadFileService {
    private static final String AUDIO = "AUDIO";
    private static final int DEFAULT_MAX = 10;
    private Notification.Builder builder;

    public static void downloadAudio(Context context, Audio audio, String url) {
        String music = Environment.getExternalStorageDirectory() + "/Music";
        String saveFileName = music + "/" + audio.getName() +
                "_" + audio.getArtistName();
        while (new File(saveFileName + ".mp3").exists()) {
            saveFileName += "_copy";
        }
        saveFileName += ".mp3";

        Intent intent = createStartIntent(url, saveFileName, context, DownloadAudioService.class);
        intent.putExtra(AUDIO, audio);
        context.startService(intent);
    }

    @Override
    protected void startForeground(Intent intent, String url, final String saveFileName, int fileIndex) {
        final Audio audio = intent.getParcelableExtra(AUDIO);

        builder = new Notification.Builder(this);
        builder.setContentTitle(audio.getName());
        builder.setContentText(audio.getArtistName());
        builder.setLargeIcon(BitmapUtilities.getBitmapFromURL(audio.getArtUrl(ArtSize.SMALL)));
        builder.setProgress(DEFAULT_MAX, 0, false);
        builder.setSmallIcon(R.drawable.notification_icon);

        updateNotification(fileIndex);
    }

    private void updateDatabaseWhenDownloaded(final Audio internetAudio, final String saveFileName) {
        final FlyingDog flyingDog = FlyingDog.getInstance();
        flyingDog.addDataBaseChangedListener(new FlyingDog.OnDataBaseChanged() {
            @Override
            public void onChanged() {
                FlyingDogAudioDatabase audioDataBase = flyingDog.getAudioDataBase();
                Audio downloadedAudio = CollectionUtils.find(audioDataBase.getSongs(), new Predicate<Audio>() {
                    @Override
                    public boolean check(Audio item) {
                        return item.getUrl().endsWith(saveFileName);
                    }
                });
                flyingDog.removeDataBaseChangedListener(this);

                if (downloadedAudio != null) {
                    long id = downloadedAudio.getId();
                    audioDataBase.setAudioName(id, internetAudio.getName());
                    audioDataBase.setArtistName(id, internetAudio.getArtistName());
                    try {
                        String artUrl = internetAudio.getArtUrl(ArtSize.SMALL);
                        if (artUrl != null) {
                            audioDataBase.downloadAndSaveAudioArt(downloadedAudio,
                                    artUrl,
                                    ArtSize.SMALL);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void updateNotification(int fileIndex) {
        startForeground(fileIndex, builder.getNotification());
    }

    @Override
    public void onProgressChanged(int fileIndex, int progress, int max) {
        builder.setProgress(max, progress, false);
        updateNotification(fileIndex);
    }

    @Override
    protected void onFileDownloaded(String fileName, Intent intent) {
        Audio audio = intent.getParcelableExtra(AUDIO);
        updateDatabaseWhenDownloaded(audio, fileName);
    }

    @Override
    protected void onHandleError(IOException error) {
        Toasts.message(this, R.string.download_failed);
    }
}
