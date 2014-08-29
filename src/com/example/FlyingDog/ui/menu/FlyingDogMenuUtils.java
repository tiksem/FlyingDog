package com.example.FlyingDog.ui.menu;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import com.example.FlyingDog.FlyingDog;
import com.example.FlyingDog.R;
import com.example.FlyingDog.ui.CreatePlayListAlert;
import com.tiksem.media.data.PlayList;
import com.utils.framework.collections.DifferentlySortable;
import com.utilsframework.android.view.GuiUtilities;

import java.util.Comparator;

/**
 * User: Tikhonenko.S
 * Date: 21.08.14
 * Time: 16:42
 */
public class FlyingDogMenuUtils {
    public static interface MediaDataSetChanged {
        void onDataSetChanged();
    }

    private static void initSortingItem(final DifferentlySortable sortable, Menu menu, Class aClass,
                                        final MediaDataSetChanged mediaDataSetChanged) {
        MenuItem sortItem = menu.findItem(R.id.action_sort);
        SubMenu subMenu = sortItem.getSubMenu();
        GuiUtilities.setChildrenVisibility(subMenu, false);
        sortItem.setVisible(false);

        if (sortable != null) {
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
                        mediaDataSetChanged.onDataSetChanged();
                        return true;
                    }
                });
            }

            if(oneItemVisible){
                sortItem.setVisible(true);
            }
        }
    }

    private static void initAddPlaylistItem(Menu menu, Class aClass, final Context context,
                                           final MediaDataSetChanged mediaDataSetChanged) {
        MenuItem addPlayListItem = menu.findItem(R.id.action_add_playlist);
        addPlayListItem.setVisible(aClass == PlayList.class);

        addPlayListItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FlyingDog flyingDog = FlyingDog.getInstance();
                CreatePlayListAlert alert = new CreatePlayListAlert(flyingDog.getAudioDataManager(),
                        context);
                alert.show(new CreatePlayListAlert.Listener() {
                    @Override
                    public void onDialogClosed(boolean playListCreated) {
                        if(playListCreated){
                            mediaDataSetChanged.onDataSetChanged();
                        }
                    }
                });

                return true;
            }
        });
    }

    public static void setupMenuForDataList(final DifferentlySortable sortable, Context context,
                                            Menu menu, Class aClass,
                                            final MediaDataSetChanged mediaDataSetChanged) {
        initSortingItem(sortable, menu, aClass, mediaDataSetChanged);
        initAddPlaylistItem(menu, aClass, context, mediaDataSetChanged);
    }
}
