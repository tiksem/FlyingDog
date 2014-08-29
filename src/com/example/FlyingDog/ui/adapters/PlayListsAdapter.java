package com.example.FlyingDog.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.FlyingDog.R;
import com.example.FlyingDog.ui.adapters.holders.PlayListViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.data.ArtSize;
import com.tiksem.media.data.Audio;
import com.tiksem.media.data.PlayList;
import com.utilsframework.android.adapters.ViewArrayAdapter;
import com.utilsframework.android.view.GuiUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CM on 8/29/2014.
 */
public class PlayListsAdapter extends ViewArrayAdapter<PlayList, PlayListViewHolder>{
    private static final ImageLoader IMAGE_LOADER = ImageLoader.getInstance();

    private AudioDataManager audioDataManager;
    private Map<Integer, List<String>> cachedUrls = new HashMap<Integer, List<String>>();

    private List<String> createArts(PlayList playList, int count){
        List<String> arts = new ArrayList<String>(count);
        if (playList.isLocal()) {
            List<Audio> audios = audioDataManager.getTracksOfPlayList(playList);
            for(Audio audio : audios){
                if(arts.size() >= count){
                    return arts;
                }

                String art = audio.getArtUrl(ArtSize.SMALL);
                if(art != null){
                    arts.add(art);
                }
            }
        }

        while (arts.size() < count) {
            arts.add(null);
        }

        return arts;
    }

    private List<String> getArts(PlayList playList, int count){
        List<String> arts = cachedUrls.get(System.identityHashCode(playList));
        if(arts == null){
            arts = createArts(playList, count);
        }

        return arts;
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.playlist_folder;
    }

    @Override
    protected PlayListViewHolder createViewHolder(View view) {
        PlayListViewHolder holder = new PlayListViewHolder();
        holder.arts = GuiUtilities.getAllChildrenRecursive(view, ImageView.class);
        holder.name = (TextView) view.findViewById(R.id.name);
        return holder;
    }

    @Override
    protected void reuseView(PlayList playList, PlayListViewHolder holder,
                             int position) {
        List<ImageView> arts = holder.arts;
        List<String> artUrls = getArts(playList, arts.size());

        int index = 0;
        for (ImageView art : arts) {
            IMAGE_LOADER.displayImage(artUrls.get(index++), art);
        }

        holder.name.setText(playList.getName());
    }

    public PlayListsAdapter(Context context, AudioDataManager audioDataManager) {
        super(context);
        this.audioDataManager = audioDataManager;
    }
}
