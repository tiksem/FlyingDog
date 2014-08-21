package com.example.FlyingDog;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import com.example.FlyingDog.ui.menu.SortingMenuUtils;
import com.utils.framework.collections.DifferentlySortable;

import java.util.Collections;
import java.util.List;

/**
 * User: Tikhonenko.S
 * Date: 21.08.14
 * Time: 20:16
 */
public abstract class MediaListActivity extends Activity {
    private Class dataClass = Object.class;
    private List mediaData = Collections.singletonList(null);

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        DifferentlySortable sortable = null;
        if(mediaData instanceof DifferentlySortable){
            sortable = (DifferentlySortable)mediaData;
        }

        SortingMenuUtils.setupMenuForDataList(sortable, menu, dataClass, new SortingMenuUtils.SortingChanged() {
            @Override
            public void onSortingChanged() {
                onSortingModeChanged();
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    protected void notifyMediaDataChanged(Class aClass, List mediaData) {
        dataClass = aClass;
        this.mediaData = mediaData;
        invalidateOptionsMenu();
    }

    protected abstract void onSortingModeChanged();
}
