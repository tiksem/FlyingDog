package com.example.FlyingDog;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import com.example.FlyingDog.ui.fragments.PlayListFragment;
import com.utilsframework.android.view.LayoutRadioButtonGroup;

import java.util.List;

public class PlayListActivity extends Activity {
    private Fragment getFragmentByModeId(int id) {
        switch (id) {
            case R.id.allSongsPlayListButton:
                return new PlayListFragment();
        }

        return null;
    }

    private void onPlayListModeChanged(int newPlayListModeId) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        LayoutRadioButtonGroup layoutRadioButtonGroup = (LayoutRadioButtonGroup)
                findViewById(R.id.playlistSwitcherContent);

        layoutRadioButtonGroup.setOnSelectedChangedListener(new LayoutRadioButtonGroup.OnSelectedChanged() {
            @Override
            public void onSelectedChanged(boolean fromUser,
                                          LayoutRadioButtonGroup.LayoutRadioButton item,
                                          LayoutRadioButtonGroup.LayoutRadioButton old) {
                onPlayListModeChanged(item.getId());
            }
        });
    }
}
