package com.example.FlyingDog.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AbsListView;
import com.example.FlyingDog.R;
import com.example.FlyingDog.network.RequestManager;
import com.example.FlyingDog.ui.adapters.PlayListsAdapter;
import com.tiksem.media.data.PlayList;
import com.utilsframework.android.adapters.ViewArrayAdapter;

import java.util.List;

/**
 * Created by stykhonenko on 29.10.15.
 */
public class AddToPlayListsFragment extends AbstractPlayListsFragment {
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
    }

    @Override
    protected List<PlayList> createList() {
        return audioDataBase.getPlayLists();
    }

    @Override
    protected void onListItemClicked(PlayList item, int position) {

    }

    @Override
    protected int getRootLayout() {
        return R.layout.add_to_playlists_fragment;
    }

    @Override
    protected int getItemBackground() {
        return R.drawable.selected_item;
    }
}
