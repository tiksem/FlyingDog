package com.tiksem.FlyingDog.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.Toast;
import com.tiksem.FlyingDog.R;
import com.tiksem.FlyingDog.ui.fragments.AbstractAudioDataFragment;
import com.utilsframework.android.navdrawer.*;
import com.utilsframework.android.view.LayoutRadioButtonGroup;
import com.utilsframework.android.view.Toasts;

/**
 * Created by stykhonenko on 19.10.15.
 */
public class FlyingDogPlayListsActivity extends AbstractPlayListsActivity {
    private TabLayoutAdapter tabLayoutAdapter;
    private LayoutRadioButtonGroupTabsAdapter layoutRadioButtonGroupTabsAdapter;
    private TabsAdapterSwitcher tabsAdapterSwitcher;
    private Toast helpToast;

    private AbstractAudioDataFragment getPlayListFragment() {
        return (AbstractAudioDataFragment) getCurrentFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        helpToast = Toasts.customViewAtCenter(this, R.layout.search_internet_help_toast, Toast.LENGTH_LONG);
    }

    @Override
    protected FragmentFactory createFragmentFactory() {
        return new FlyingDogFragmentsFactory(this);
    }

    @Override
    protected TabsAdapter createTabsAdapter() {
        tabsAdapterSwitcher = new TabsAdapterSwitcher(this, getTabsStub());
        layoutRadioButtonGroupTabsAdapter = LayoutRadioButtonGroupTabsAdapter.fromLayoutId(this,
                R.layout.main_tabs, R.id.playlistSwitcherContent);
        tabLayoutAdapter = TabLayoutAdapter.fromLayoutId(this, getTabLayoutId());

        final HorizontalScrollView scrollView = (HorizontalScrollView) layoutRadioButtonGroupTabsAdapter.getView();
        LayoutRadioButtonGroup radioButtonGroup = (LayoutRadioButtonGroup) scrollView.getChildAt(0);

        setupMainTabsScrolling(scrollView, radioButtonGroup);

        return tabsAdapterSwitcher;
    }

    private void setupMainTabsScrolling(final HorizontalScrollView scrollView,
                                        LayoutRadioButtonGroup radioButtonGroup) {

        LayoutRadioButtonGroup.OnSelectedChanged leftScrollListener = new LayoutRadioButtonGroup.OnSelectedChanged() {
            @Override
            public void onSelectedChanged(boolean fromUser, LayoutRadioButtonGroup.LayoutRadioButton item,
                                          LayoutRadioButtonGroup.LayoutRadioButton old) {
                scrollView.fullScroll(View.FOCUS_LEFT);
            }
        };

        int genres = PlayListMode.GENRES.ordinal();
        for (int i = 0; i < genres; i++) {
            radioButtonGroup.getItemByIndex(i).setOnSelectedChangedListener(leftScrollListener);
        }

        LayoutRadioButtonGroup.OnSelectedChanged rightScrollListener = new LayoutRadioButtonGroup.OnSelectedChanged() {
            @Override
            public void onSelectedChanged(boolean fromUser, LayoutRadioButtonGroup.LayoutRadioButton item,
                                          LayoutRadioButtonGroup.LayoutRadioButton old) {
                scrollView.fullScroll(View.FOCUS_RIGHT);
            }
        };

        int childCount = radioButtonGroup.getChildCount();
        for (int i = genres; i < childCount; i++) {
            radioButtonGroup.getItemByIndex(i).setOnSelectedChangedListener(rightScrollListener);
        }
    }

    @Override
    protected void onTabsInit(int tabsCount, int navigationLevel) {
        super.onTabsInit(tabsCount, navigationLevel);
        if (navigationLevel == 0) {
            tabsAdapterSwitcher.setTabsAdapter(layoutRadioButtonGroupTabsAdapter);
        } else {
            tabsAdapterSwitcher.setTabsAdapter(tabLayoutAdapter);
        }
    }

    @Override
    public void dismissHelpToast() {
        if (helpToast != null) {
            helpToast.cancel();
            helpToast = null;
        }
    }
}
