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
import com.tiksem.media.ui.AudioPlaybackSeekBar;
import com.utils.framework.collections.DifferentlySortedListWithSelectedItem;
import com.utilsframework.android.Services;

import java.util.ArrayList;
import java.util.List;

public class PlayListActivity extends Activity {
    private ListView listView;
    private AndroidAudioDataBase audioDataBase;
    private AudioPlayerService.PlayerBinder playerBinder;

    /**
     * Called when the activity is first created.
     */

    private void onServiceReady() {
        final SongsAdapter adapter = new SongsAdapter(this);
        listView.setAdapter(adapter);

        adapter.setElements(playerBinder.getPlayList());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playerBinder.playAudio(position);
            }
        });

        audioDataBase.startAlbumArtsUpdating(new LocalAudioDataBase.OnArtsUpdatingFinished() {
            @Override
            public void onFinished() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        AudioPlayerService.start(this);

        listView = (ListView) findViewById(R.id.play_list_view);

        final FlyingDog flyingDog = FlyingDog.getInstance();
        audioDataBase = flyingDog.getAudioDataBase();

        flyingDog.executeWhenPlayerServiceIsReady(new Runnable() {
            @Override
            public void run() {
                playerBinder = flyingDog.getPlayerBinder();
                onServiceReady();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
