package com.tiksem.FlyingDog.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.tiksem.FlyingDog.R;
import com.tiksem.FlyingDog.ui.FlyingDogPlayListsActivity;
import com.tiksem.FlyingDog.ui.fragments.SongsFragment;
import com.tiksem.media.data.Audio;
import com.tiksem.media.playback.AudioPlayerService;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.Services;

/**
 * Created by stykhonenko on 07.11.15.
 */
public class FlyingDogPlaybackService extends AudioPlayerService {
    private Bitmap icon;

    public static void bindAndStart(Context context, Services.OnBind<Binder> onBind) {
        bindAndStart(context, FlyingDogPlaybackService.class, onBind);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        icon = BitmapFactory.decodeResource(getResources(), R.drawable.notification_icon);
    }

    @Override
    protected void startForeground() {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setLargeIcon(icon);
        builder.setSmallIcon(R.drawable.small_icon);

        NavigationList<Audio> currentPlayList = SongsFragment.getCurrentPlayList();
        int position = getPosition();
        Audio currentAudio = currentPlayList.get(position);

        builder.setContentTitle(currentAudio.getName());
        builder.setContentText(currentAudio.getArtistName());

        Intent intent = new Intent(this, FlyingDogPlayListsActivity.class);
        builder.setContentIntent(PendingIntent.getActivity(this, 0, intent, 0));

        startForeground(R.id.player_service_notification, builder.getNotification());
    }
}
