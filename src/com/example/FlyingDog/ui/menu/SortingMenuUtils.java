package com.example.FlyingDog.ui.menu;

import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import com.example.FlyingDog.R;
import com.utils.framework.collections.DifferentlySortable;
import com.utilsframework.android.view.GuiUtilities;

import java.util.Comparator;
import java.util.List;

/**
 * User: Tikhonenko.S
 * Date: 21.08.14
 * Time: 16:42
 */
public class SortingMenuUtils {
    public static interface SortingChanged {
        void onSortingChanged();
    }

    public static void setupMenuForDataList(final DifferentlySortable sortable, Menu menu, Class aClass,
                                            final SortingChanged sortingChangedListener) {
        MenuItem sortItem = menu.findItem(R.id.action_sort);
        SubMenu subMenu = sortItem.getSubMenu();
        GuiUtilities.setChildrenVisibility(subMenu, false);
        sortItem.setVisible(false);

        if(sortable == null){
            return;
        }

        boolean oneItemVisible = false;
        for(SortingMode sortingMode : SortingMode.getSortingModesByDataType(aClass)){
            final Comparator comparator = sortingMode.comparator;
            int itemId = sortingMode.menuItemId;

            MenuItem menuItem = subMenu.findItem(itemId);
            if(menuItem == null){
                throw new RuntimeException("Could not find menu item");
            }
            menuItem.setVisible(true);
            oneItemVisible = true;

            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    sortable.setCurrentSortComparator(comparator);
                    sortingChangedListener.onSortingChanged();
                    return true;
                }
            });
        }

        if(oneItemVisible){
            sortItem.setVisible(true);
        }
    }
}
