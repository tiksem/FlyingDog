package com.tiksem.FlyingDog.ui.adapters;

import android.content.Context;
import com.tiksem.FlyingDog.ui.adapters.holders.ArtCollectionHolder;
import com.tiksem.media.data.Audio;
import com.utils.framework.suggestions.SuggestionsProvider;
import com.utilsframework.android.adapters.SuggestionsAdapter;

/**
 * Created by CM on 10/5/2014.
 */

public class AudiosSuggestionsAdapter
        extends EditAudioStringSuggestionsAdapter {
    public AudiosSuggestionsAdapter(Context context, SuggestionsProvider<String> suggestionsProvider) {
        super(context, suggestionsProvider);
    }
}
