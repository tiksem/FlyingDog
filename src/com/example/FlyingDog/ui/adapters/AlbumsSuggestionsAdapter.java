package com.example.FlyingDog.ui.adapters;

import android.content.Context;
import com.example.FlyingDog.ui.adapters.holders.ArtCollectionHolder;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.data.Album;
import com.tiksem.media.search.suggestions.AlbumSuggestionsProvider;
import com.utils.framework.suggestions.SuggestionsProvider;
import com.utilsframework.android.adapters.SuggestionsAdapter;
import com.utilsframework.android.adapters.ViewArrayAdapter;

/**
 * Created by CM on 10/5/2014.
 */
public class AlbumsSuggestionsAdapter extends SuggestionsAdapter<Album, ArtCollectionHolder> {
    private static final String NULL_ITEM_TEXT = "Unknown album";
    private static final int MAX_ALBUMS_COUNT = 10;

    public AlbumsSuggestionsAdapter(Context context, AudioDataManager audioDataManager) {
        super(new ArtCollectionSpinnerAdapter<Album>(context, NULL_ITEM_TEXT),
                new AlbumSuggestionsProvider(audioDataManager, MAX_ALBUMS_COUNT));
    }
}
