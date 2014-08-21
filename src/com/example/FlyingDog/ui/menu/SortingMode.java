package com.example.FlyingDog.ui.menu;

import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import com.example.FlyingDog.R;
import com.tiksem.media.data.Audio;
import com.tiksem.media.data.AudioComparators;
import com.utils.framework.collections.DifferentlySortedList;
import com.utils.framework.collections.map.AbstractMultiMap;
import com.utils.framework.collections.map.MultiMap;
import com.utils.framework.collections.map.UniqueValuesHashMultiMap;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * User: Tikhonenko.S
 * Date: 21.08.14
 * Time: 16:43
 */
public final class SortingMode<T> {
    private static MultiMap<Class, SortingMode> sortingModes = new UniqueValuesHashMultiMap<Class, SortingMode>();
    static {
        sortingModes.put(Audio.class, new SortingMode<Audio>(AudioComparators.audioByName(), R.id.sort_by_name));
        sortingModes.put(Audio.class, new SortingMode<Audio>(DifferentlySortedList.DEFAULT_COMPARATOR,
                R.id.sort_adding_date));
    }

    public SortingMode(Comparator<T> comparator, int menuItemId) {
        this.comparator = comparator;
        this.menuItemId = menuItemId;
    }

    public Comparator<T> comparator;
    public int menuItemId;

    public static Collection<SortingMode> getSortingModesByDataType(Class aClass) {
        return sortingModes.getValues(aClass);
    }
}
