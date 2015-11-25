package com.tiksem.FlyingDog.ui.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.tiksem.SongsYouMayLike.R;
import com.tiksem.FlyingDog.ui.ImageLoaderUtils;
import com.tiksem.FlyingDog.ui.adapters.holders.ArtCollectionHolder;
import com.tiksem.media.data.ArtCollection;
import com.tiksem.media.data.ArtSize;

/**
 * Created by CM on 8/29/2014.
 */
public abstract class ArtCollectionAdapter<T extends ArtCollection> extends
        FlyingDogAdapter<T, ArtCollectionHolder>{

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
        ImageLoaderUtils.loadImageIfNeed(holder.art, artCollection.getArtUrl(ArtSize.MEDIUM));
    }

    @Override
    protected int getNullLayoutId() {
        return R.layout.art_collection_loading;
    }

    @Override
    protected int getRootLayoutId(int viewType) {
        return R.layout.art_collection_item;
    }
}
