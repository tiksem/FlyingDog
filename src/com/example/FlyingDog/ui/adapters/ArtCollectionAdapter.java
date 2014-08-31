package com.example.FlyingDog.ui.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.FlyingDog.R;
import com.example.FlyingDog.ui.adapters.holders.ArtCollectionHolder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tiksem.media.data.ArtCollection;
import com.tiksem.media.data.ArtSize;
import com.utilsframework.android.adapters.ViewArrayAdapter;

/**
 * Created by CM on 8/29/2014.
 */
public abstract class ArtCollectionAdapter extends ViewArrayAdapter<ArtCollection, ArtCollectionHolder>{
    private static final ImageLoader IMAGE_LOADER = ImageLoader.getInstance();

    public ArtCollectionAdapter(Context context) {
        super(context);
    }

    @Override
    protected ArtCollectionHolder createViewHolder(View view) {
        ArtCollectionHolder holder = new ArtCollectionHolder();
        holder.art = (ImageView) view.findViewById(R.id.art);
        holder.name = (TextView) view.findViewById(R.id.name);
        return holder;
    }

    @Override
    protected void reuseView(ArtCollection artCollection, ArtCollectionHolder holder, int position, View view) {
        holder.name.setText(artCollection.getName());
        IMAGE_LOADER.displayImage(artCollection.getArtUrl(ArtSize.MEDIUM), holder.art);
    }
}
