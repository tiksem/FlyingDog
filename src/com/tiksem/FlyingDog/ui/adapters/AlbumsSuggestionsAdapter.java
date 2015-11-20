package com.tiksem.FlyingDog.ui.adapters;

import android.content.Context;
import com.tiksem.FlyingDog.ui.adapters.holders.ArtCollectionHolder;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.data.Album;
import com.tiksem.media.search.suggestions.AlbumSuggestionsProvider;
import com.utils.framework.suggestions.SuggestionsProvider;
import com.utilsframework.android.adapters.SuggestionsAdapter;

/**
 * Created by CM on 10/5/2014.
 */
public class AlbumsSuggestionsAdapter extends SuggestionsAdapter<Album, ArtCollectionHolder> {
    public AlbumsSuggestionsAdapter(Context context, SuggestionsProvider<Album> suggestionsProvider) {
        super(new ArtCollectionSpinnerAdapter<Album>(context),
                suggestionsProvider);
    }
}
