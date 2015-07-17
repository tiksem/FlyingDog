package com.example.FlyingDog.ui.fragments;

import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.FlyingDog.FlyingDog;
import com.example.FlyingDog.R;
import com.example.FlyingDog.ui.adapters.SongsAdapter;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.MediaUpdatingService;
import com.tiksem.media.data.Album;
import com.tiksem.media.data.Artist;
import com.tiksem.media.data.Audio;
import com.tiksem.media.data.AudioInArtist;
import com.tiksem.media.playback.AudioPlayerService;
import com.utils.framework.collections.DifferentlySortedListWithSelectedItem;
import com.utilsframework.android.view.GuiUtilities;

import java.util.List;

/**
 * User: Tikhonenko.S
 * Date: 26.08.14
 * Time: 18:32
 */
public class AudioListFragment extends MediaListFragment {
    private ListView listView;
    private AudioDataManager audioDataManager;
    private AudioPlayerService.PlayerBinder playerBinder;
    private AudioPlayerService.PlayBackListener playBackListener;
    private SongsAdapter adapter;
    private Object tag;

    private int updateSelectedItem() {
        int position = playerBinder.getPlayingAudioPosition();
        listView.setItemChecked(position, true);
        return position;
    }

    private Class getDataType() {
        if(tag instanceof Artist || tag instanceof Album){
            return AudioInArtist.class;
        }

        return Audio.class;
    }

    private void onServiceReady() {
        adapter = new SongsAdapter(getActivity());
        listView.setAdapter(adapter);

        final List<Audio> audios;
        if(tag == null || tag.equals(playerBinder.getPlayListTag())){
            audios = playerBinder.getPlayList();
            tag = playerBinder.getPlayListTag();
            if(playerBinder.isPlaying()){
                int selectedPosition = updateSelectedItem();
                GuiUtilities.scrollListViewToPosition(listView, selectedPosition);
            }

        } else {
            audios = new DifferentlySortedListWithSelectedItem<Audio>(audioDataManager.getSongsByTag(tag));
        }

        adapter.setElements(audios);
        notifyMediaDataChanged(getDataType(), audios);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(tag != null && !tag.equals(playerBinder.getPlayListTag())){
                    DifferentlySortedListWithSelectedItem<Audio> audiosForPlayBinder =
                            new DifferentlySortedListWithSelectedItem<Audio>(audios);
                    playerBinder.setAudios(audiosForPlayBinder);
                    playerBinder.setPlayListTag(tag);
                }

                playerBinder.playAudio(position);
            }
        });

        playBackListener = new AudioPlayerService.PlayBackListener() {
            @Override
            public void onAudioPlayingStarted() {
                updateSelectedItem();
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

    public AudioListFragment(Object tag) {
        this.tag = tag;
    }

    public AudioListFragment() {
        this(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.play_list_fragment, null);
    }

    private void updatePlayBinder() {
        final FlyingDog flyingDog = FlyingDog.getInstance();
        flyingDog.executeWhenPlayerServiceIsReady(new Runnable() {
            @Override
            public void run() {
                playerBinder = flyingDog.getPlayerBinder();
                onServiceReady();
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.play_list_view);

        registerForContextMenu(listView);

        final FlyingDog flyingDog = FlyingDog.getInstance();
        audioDataManager = flyingDog.getAudioDataManager();
        updatePlayBinder();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.song_contextual_menu, menu);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Audio audio = adapter.getElementOfView(info.targetView);
        GuiUtilities.setClickListenerToMenuItems(menu, new AudioContextMenuListener(audio, getActivity()));

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public void onStart() {
        super.onStart();

        FlyingDog flyingDog = FlyingDog.getInstance();
        flyingDog.setOnAlbumArtUpdated(new MediaUpdatingService.OnAlbumArtUpdated() {
            @Override
            public void onAlbumArtUpdated(Album album) {
                if(adapter != null){
                    adapter.notifyDataSetChanged();
                }
            }
        });
        flyingDog.setOnAlbumOfAudioUpdated(new MediaUpdatingService.OnAlbumOfAudioUpdated() {
            @Override
            public void onUpdateFinished(Album album, Audio audio) {
                if(adapter != null){
                    adapter.notifyDataSetChanged();
                }
            }
        });

        if (playerBinder != null) {
            playerBinder.addPlayBackListener(playBackListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (playerBinder != null) {
            playerBinder.removePlayBackListener(playBackListener);
        }
        FlyingDog flyingDog = FlyingDog.getInstance();
        flyingDog.setOnAlbumArtUpdated(null);
        flyingDog.setOnAlbumOfAudioUpdated(null);
    }

    public Object getPlayListTag() {
        return tag;
    }

    public Artist getArtist() {
        if(tag instanceof Artist){
            return (Artist) tag;
        }

        return null;
    }

    @Override
    protected void onMediaListDataSetChanged(List newData) {
        if (adapter != null) {
            adapter.setElements(newData);
        }
    }
}