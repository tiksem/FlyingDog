package com.example.FlyingDog.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.FlyingDog.FlyingDog;
import com.example.FlyingDog.R;
import com.example.FlyingDog.ui.adapters.SongsAdapter;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.data.Audio;
import com.tiksem.media.local.AndroidAudioDataBase;
import com.tiksem.media.local.LocalAudioDataBase;
import com.tiksem.media.playback.AudioPlayerService;
import com.utils.framework.collections.DifferentlySortedListWithSelectedItem;
import com.utils.framework.collections.ListWithSelectedItem;

import java.util.List;

/**
 * User: Tikhonenko.S
 * Date: 26.08.14
 * Time: 18:32
 */
public class PlayListFragment extends MediaListFragment {
    private ListView listView;
    private AudioDataManager audioDataManager;
    private AudioPlayerService.PlayerBinder playerBinder;
    private AudioPlayerService.PlayBackListener playBackListener;
    private SongsAdapter adapter;
    private Object tag;

    private void onServiceReady() {
        adapter = new SongsAdapter(getActivity());
        listView.setAdapter(adapter);

        final List<Audio> audios;
        if(tag == null){
            audios = playerBinder.getPlayList();
            tag = playerBinder.getPlayListTag();
        } else {
            audios = audioDataManager.getSongsByTag(tag);
        }

        adapter.setElements(audios);
        notifyMediaDataChanged(Audio.class, audios);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(tag != null && !tag.equals(playerBinder.getPlayListTag())){
                    DifferentlySortedListWithSelectedItem<Audio> audiosForPlayBinder =
                            new DifferentlySortedListWithSelectedItem<Audio>(audios);
                    playerBinder.setAudios(audiosForPlayBinder);
                }

                playerBinder.playAudio(position);
            }
        });

        audioDataManager.startAlbumArtsUpdating(new LocalAudioDataBase.OnArtsUpdatingFinished() {
            @Override
            public void onFinished() {
                adapter.notifyDataSetChanged();
            }
        });

        playBackListener = new AudioPlayerService.PlayBackListener() {
            @Override
            public void onAudioPlayingStarted() {
                int position = playerBinder.getPlayingAudioPosition();
                listView.setItemChecked(position, true);
            }

            @Override
            public void onAudioPlayingComplete() {
            }

            @Override
            public void onAudioPlayingPaused() {
            }

            @Override
            public void onAudioPlayingResumed() {
            }

            @Override
            public void onProgressChanged(long progress, long max) {
            }
        };
        playerBinder.addPlayBackListener(playBackListener);
    }

    public PlayListFragment(Object tag) {
        this.tag = tag;
    }

    public PlayListFragment() {
        this(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.play_list_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.play_list_view);

        final FlyingDog flyingDog = FlyingDog.getInstance();
        audioDataManager = flyingDog.getAudioDataManager();

        flyingDog.executeWhenPlayerServiceIsReady(new Runnable() {
            @Override
            public void run() {
                playerBinder = flyingDog.getPlayerBinder();
                onServiceReady();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (playerBinder != null) {
            playerBinder.removePlayBackListener(playBackListener);
        }
    }

    @Override
    protected void onSortingModeChanged() {
        adapter.notifyDataSetChanged();
    }
}
