package com.tiksem.FlyingDog.ui.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.tiksem.FlyingDog.R;
import com.tiksem.FlyingDog.ui.adapters.holders.SuggestionHolder;
import com.utils.framework.suggestions.SuggestionsProvider;
import com.utilsframework.android.adapters.SuggestionsAdapter;
import com.utilsframework.android.adapters.ViewArrayAdapter;

/**
 * Created by stykhonenko on 20.11.15.
 */
public class EditAudioStringSuggestionsAdapter extends SuggestionsAdapter<String, SuggestionHolder> {
    public EditAudioStringSuggestionsAdapter(Context context, SuggestionsProvider<String> suggestionsProvider) {
        setSuggestionsProvider(suggestionsProvider);
        setViewArrayAdapter(new ViewArrayAdapter<String, SuggestionHolder>(context) {
            @Override
            protected int getRootLayoutId(int viewType) {
                return R.layout.suggestion;
            }

            @Override
            protected SuggestionHolder createViewHolder(View view) {
                SuggestionHolder suggestionHolder = new SuggestionHolder();
                suggestionHolder.text = (TextView) view.findViewById(R.id.suggestion);
                return suggestionHolder;
            }

            @Override
            protected void reuseView(String suggestion, SuggestionHolder suggestionHolder, int position, View view) {
                suggestionHolder.text.setText(suggestion);
            }
        });
    }
}
