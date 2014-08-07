package com.example.FlyingDog;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.FlyingDog.setup.ImageLoaderConfigFactory;
import com.example.FlyingDog.ui.adapters.SongsAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tiksem.media.data.Audio;
import com.tiksem.media.local.AndroidAudioDataBase;
import com.tiksem.media.local.LocalAudioDataBase;
import com.tiksem.media.playback.AudioPlayerService;
import com.utils.framework.collections.DifferentlySortedListWithSelectedItem;
import com.utilsframework.android.Services;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity {
    private ListView listView;
    private AndroidAudioDataBase audioDataBase;
    private AudioPlayerService.PlayerBinder playerBinder;

    /**
     * Called when the activity is first created.
     */

    private void onServiceReady() {
        SongsAdapter adapter = new SongsAdapter(this);
        listView.setAdapter(adapter);
        DifferentlySortedListWithSelectedItem<Audio> audios =
                new DifferentlySortedListWithSelectedItem<Audio>(audioDataBase.getSongs());
        playerBinder.setAudios(audios);

        adapter.setElements(audios);

        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        //listView.setSelector(R.drawable.song_item);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playerBinder.playAudio(position);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AudioPlayerService.start(this);

        listView = new ListView(this);
        setContentView(listView);

        ImageLoader.getInstance().init(ImageLoaderConfigFactory.getCommonImageLoaderConfig(this));
        audioDataBase = new AndroidAudioDataBase(getContentResolver());

        AudioPlayerService.bind(this, new Services.OnBind<AudioPlayerService.PlayerBinder>() {
            @Override
            public void onBind(Services.Connection<AudioPlayerService.PlayerBinder> connection) {
                playerBinder = connection.getBinder();
                onServiceReady();
            }
        });
    }
}
