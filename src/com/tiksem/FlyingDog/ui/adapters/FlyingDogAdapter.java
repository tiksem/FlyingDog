package com.tiksem.FlyingDog.ui.adapters;

import android.content.Context;
import com.tiksem.SongsYouMayLike.R;
import com.utilsframework.android.adapters.ViewArrayAdapter;

/**
 * Created by stykhonenko on 23.10.15.
 */
public abstract class FlyingDogAdapter<T, Holder> extends ViewArrayAdapter<T, Holder> {
    public FlyingDogAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getNullLayoutId() {
        return R.layout.spinner_text_view;
    }
}
