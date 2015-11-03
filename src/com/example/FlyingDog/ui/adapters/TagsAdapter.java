package com.example.FlyingDog.ui.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.example.FlyingDog.R;
import com.example.FlyingDog.ui.adapters.holders.TagHolder;
import com.utils.framework.strings.Strings;

/**
 * Created by stykhonenko on 03.11.15.
 */
public class TagsAdapter extends FlyingDogAdapter<String, TagHolder> {
    public TagsAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getRootLayoutId(int viewType) {
        return R.layout.tag_item;
    }

    @Override
    protected TagHolder createViewHolder(View view) {
        TagHolder tagHolder = new TagHolder();
        tagHolder.tag = (TextView) view.findViewById(R.id.tag);
        return tagHolder;
    }

    @Override
    protected void reuseView(String tag, TagHolder tagHolder, int position, View view) {
        tagHolder.tag.setText(Strings.capitalizeCharSequence(tag));
    }
}
