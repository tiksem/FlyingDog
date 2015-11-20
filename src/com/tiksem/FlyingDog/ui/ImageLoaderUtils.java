package com.tiksem.FlyingDog.ui;

import android.widget.ImageView;
import com.tiksem.FlyingDog.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by stykhonenko on 05.11.15.
 */
public class ImageLoaderUtils {
    private static final ImageLoader IMAGE_LOADER = ImageLoader.getInstance();

    public static void loadImageIfNeed(ImageView imageView, String uri) {
        if (uri != null && uri.equals(imageView.getTag(R.id.art))) {
            return;
        }

        IMAGE_LOADER.displayImage(uri, imageView);
        imageView.setTag(R.id.art, uri);
    }
}
