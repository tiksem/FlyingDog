package com.example.FlyingDog.ui.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AbsListView;
import com.example.FlyingDog.R;
import com.example.FlyingDog.network.RequestManager;
import com.example.FlyingDog.ui.adapters.PlayListsAdapter;
import com.tiksem.media.data.Audio;
import com.tiksem.media.data.PlayList;
import com.utilsframework.android.adapters.ViewArrayAdapter;
import com.utilsframework.android.fragments.Fragments;
import com.utilsframework.android.threading.OnFinish;
import com.utilsframework.android.threading.ThrowingRunnable;
import com.utilsframework.android.view.Alerts;
import com.utilsframework.android.view.Toasts;

import java.io.IOException;
import java.util.List;

/**
 * Created by stykhonenko on 29.10.15.
 */
public class AddToPlayListsFragment extends AbstractPlayListsFragment {
    private static final String AUDIO = "AUDIO";

    private Audio audio;

    public static AddToPlayListsFragment create(Audio audio) {
        return Fragments.createFragmentWith1Arg(new AddToPlayListsFragment(), AUDIO, audio);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        audio = getArguments().getParcelable(AUDIO);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOk();
            }
        });

        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancel();
            }
        });
    }

    private void addAudioToPlayLists(List<PlayList> playLists) {
        for (PlayList playList : playLists) {
            audioDataBase.addSongToPlayList(audio, playList);
        }
    }

    private void onOk() {
        final List<PlayList> selectedPlayLists = getSelectedItems();

        if (!selectedPlayLists.isEmpty()) {
            final ProgressDialog progressDialog = Alerts.showCircleProgressDialog(getActivity(), R.string.please_wait);
            getRequestManager().execute(new ThrowingRunnable<IOException>() {
                @Override
                public void run() throws IOException {
                    addAudioToPlayLists(selectedPlayLists);
                }
            }, new OnFinish<IOException>() {
                @Override
                public void onFinish(IOException e) {
                    progressDialog.dismiss();
                    onCancel();
                }
            });
        } else {
            Toasts.message(getActivity(), R.string.select_playlist);
        }
    }

    private void onCancel() {
        getActivity().onBackPressed();
    }

    @Override
    protected List<PlayList> createLocalList() {
        return audioDataBase.getPlayListsWhereSongCanBeAdded(audio);
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
