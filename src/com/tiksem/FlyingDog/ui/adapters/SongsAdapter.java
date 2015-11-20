package com.tiksem.FlyingDog.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.tiksem.FlyingDog.R;
import com.tiksem.FlyingDog.ui.ImageLoaderUtils;
import com.tiksem.FlyingDog.ui.adapters.holders.SongViewHolder;
import com.tiksem.media.data.ArtSize;
import com.tiksem.media.data.Audio;

/**
 * User: Tikhonenko.S
 * Date: 06.08.14
 * Time: 20:14
 */
public class SongsAdapter extends FlyingDogAdapter<Audio, SongViewHolder> {
    public SongsAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getRootLayoutId(int viewType) {
        return R.layout.song_item;
    }

    @Override
    protected SongViewHolder createViewHolder(View view) {
        SongViewHolder songViewHolder = new SongViewHolder();
        songViewHolder.art = (ImageView) view.findViewById(R.id.art);
        songViewHolder.artistName = (TextView) view.findViewById(R.id.artist_name);
        songViewHolder.songName = (TextView) view.findViewById(R.id.song_name);
        songViewHolder.options = view.findViewById(R.id.options);
        return songViewHolder;
    }

    @Override
    protected void reuseView(final Audio audio, SongViewHolder songViewHolder, int position, final View view) {
        songViewHolder.songName.setText(audio.getName());
        songViewHolder.artistName.setText(audio.getArtistName());
        ImageLoaderUtils.loadImageIfNeed(songViewHolder.art, audio.getArtUrl(ArtSize.SMALL));

        songViewHolder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.showContextMenu();
            }
        });
    }

    @Override
    protected void onViewCreated(int position, View convertView, Audio audio, SongViewHolder holder) {
        holder.options.setFocusable(false);
    }

    protected void reuseNullView(int position, View convertView) {
        TextView textView = (TextView) convertView;
        textView.setTextColor(Color.rgb(255, 0, 0));
        textView.setText(textView.getContext().getString(R.string.please_wait));
    }
}
