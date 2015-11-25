package com.tiksem.FlyingDog.ui.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.tiksem.SongsYouMayLike.R;
import com.tiksem.FlyingDog.ui.adapters.holders.SuggestionHolder;
import com.utilsframework.android.adapters.ViewArrayAdapter;

/**
 * Created by stykhonenko on 20.11.15.
 */
class SuggestionsViewArrayAdapter extends ViewArrayAdapter<String, SuggestionHolder> {
    public SuggestionsViewArrayAdapter(Context context) {
        super(context);
    }

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
}
