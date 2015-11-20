package com.tiksem.FlyingDog.ui.adapters;

import android.content.Context;
import com.tiksem.FlyingDog.ui.adapters.holders.ArtCollectionHolder;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.data.Audio;
import com.tiksem.media.search.suggestions.AudioSuggestionsProvider;
import com.utils.framework.suggestions.SuggestionsProvider;
import com.utilsframework.android.adapters.SuggestionsAdapter;

/**
 * Created by CM on 10/5/2014.
 */

public class AudiosSuggestionsAdapter
        extends SuggestionsAdapter<Audio, ArtCollectionHolder> {
    public AudiosSuggestionsAdapter(Context context, SuggestionsProvider<Audio> suggestionsProvider) {
        setViewArrayAdapter(new ArtCollectionSpinnerAdapter<Audio>(context));
        setSuggestionsProvider(suggestionsProvider);
    }
}
