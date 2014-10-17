package com.example.FlyingDog.ui.menu;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.CursorAdapter;
import com.example.FlyingDog.FlyingDog;
import com.example.FlyingDog.OnMediaDataSetChanged;
import com.example.FlyingDog.R;
import com.example.FlyingDog.ui.CreatePlayListAlert;
import com.example.FlyingDog.ui.adapters.InternetSearch;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.data.Identified;
import com.tiksem.media.data.PlayList;
import com.tiksem.media.playback.AudioPlayerService;
import com.utils.framework.collections.DifferentlySortable;
import com.utilsframework.android.adapters.CursorSuggestionsAdapterWrapper;
import com.utilsframework.android.threading.AsyncOperationCallback;
import com.utilsframework.android.view.Alerts;
import com.utilsframework.android.view.GuiUtilities;
import android.widget.SearchView;

import java.util.Comparator;
import java.util.List;

/**
 * User: Tikhonenko.S
 * Date: 21.08.14
 * Time: 16:42
 */
public class FlyingDogMenuUtils {

    private static void initSortingItem(final DifferentlySortable sortable, Menu menu, Class aClass,
                                        final OnMediaDataSetChanged onMediaDataSetChanged) {
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
                        onMediaDataSetChanged.onDataSetChanged((List) sortable);
                        return true;
                    }
                });
            }

            if(oneItemVisible){
                sortItem.setVisible(true);
            }
        }
    }

    private static void initAddPlaylistItem(Menu menu, final List mediaData, Class aClass, final Context context,
                                           final OnMediaDataSetChanged onMediaDataSetChanged) {
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
                            onMediaDataSetChanged.onDataSetChanged(mediaData);
                        }
                    }
                });

                return true;
            }
        });
    }

    public static void initSearchItem(Menu menu, List mediaData, final OnMediaDataSetChanged  onMediaDataSetChanged,
                                      final Context context, final Class aClass) {
        MenuItem searchItem = menu.findItem(R.id.action_search);

        if(Identified.class.isAssignableFrom(aClass)){
            if(mediaData == null){
                throw new RuntimeException();
            }

            searchItem.setVisible(true);
            SearchView searchView = (SearchView) searchItem.getActionView();

            FlyingDog flyingDog = FlyingDog.getInstance();
            final AudioDataManager audioDataManager = flyingDog.getAudioDataManager();

            CursorAdapter suggestionsAdapter =
                    new CursorSuggestionsAdapterWrapper(context,
                            InternetSearch.createSuggestionsAdapterForClass(audioDataManager, context, aClass));
            searchView.setSuggestionsAdapter(suggestionsAdapter);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(final String query) {
                    Alerts.runAsyncOperationWithCircleLoading(context, R.string.please_wait,
                            new AsyncOperationCallback<List>() {
                        @Override
                        public List runOnBackground() {
                            return InternetSearch.search(query, audioDataManager, aClass);
                        }

                        @Override
                        public void onFinish(List result) {
                            onMediaDataSetChanged.onDataSetChanged(result);
                        }
                    });
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        } else {
            searchItem.setVisible(false);
        }
    }

    public static void setupMenuForDataList(final List mediaData, Context context,
                                            Menu menu, Class aClass,
                                            final OnMediaDataSetChanged onMediaDataSetChanged) {
        DifferentlySortable sortable = null;
        if(mediaData != null && mediaData instanceof DifferentlySortable){
            sortable = (DifferentlySortable) mediaData;
        }

        initSortingItem(sortable, menu, aClass, onMediaDataSetChanged);
        initAddPlaylistItem(menu, mediaData, aClass, context, onMediaDataSetChanged);
        initSearchItem(menu, mediaData, onMediaDataSetChanged, context, aClass);
    }
}
