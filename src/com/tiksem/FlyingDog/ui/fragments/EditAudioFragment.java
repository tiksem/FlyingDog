package com.tiksem.FlyingDog.ui.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tiksem.FlyingDog.FlyingDog;
import com.tiksem.FlyingDog.R;
import com.tiksem.FlyingDog.network.RequestManager;
import com.tiksem.FlyingDog.ui.PlayListsActivity;
import com.tiksem.media.data.Audio;
import com.tiksem.media.local.FlyingDogAudioDatabase;
import com.utilsframework.android.fragments.Fragments;
import com.utilsframework.android.fragments.RequestManagerFragment;
import com.utilsframework.android.navdrawer.ActionBarTitleProvider;
import com.utilsframework.android.threading.Threading;
import com.utilsframework.android.view.Alerts;
import com.utilsframework.android.view.EditTextWithSuggestions;
import com.utilsframework.android.view.Toasts;

import java.io.IOException;

/**
 * Created by stykhonenko on 29.10.15.
 */
public class EditAudioFragment extends RequestManagerFragment<RequestManager> implements ActionBarTitleProvider {
    private static final String AUDIO = "AUDIO";

    private PlayListsActivity activity;
    private FlyingDogAudioDatabase audioDataBase;
    private EditTextWithSuggestions artistNameView;
    private EditTextWithSuggestions nameView;
    private Audio audio;

    public static EditAudioFragment create(Audio audio) {
        return Fragments.createFragmentWith1Arg(new EditAudioFragment(), AUDIO, audio);
    }

    @Override
    protected RequestManager obtainRequestManager() {
        return new RequestManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getActivity(), R.layout.edit_audio, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        artistNameView = (EditTextWithSuggestions) view.findViewById(R.id.artist_name);
        nameView = (EditTextWithSuggestions) view.findViewById(R.id.name);

        artistNameView.setText(audio.getArtistName());
        nameView.setText(audio.getName());

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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (PlayListsActivity) activity;
        audioDataBase = FlyingDog.getInstance().getAudioDataBase();
        audio = getArguments().getParcelable(AUDIO);
    }

    private void onCancel() {
        activity.onBackPressed();
    }

    private void onOk() {
        final String name = nameView.getText().toString();
        if (name.isEmpty()) {
            Toasts.message(activity, R.string.enter_audio_name);
            return;
        }

        final String artistName = artistNameView.getText().toString();
        if (artistName.isEmpty()) {
            Toasts.message(activity, R.string.enter_artist_name);
            return;
        }

        final ProgressDialog progressDialog = Alerts.showCircleProgressDialog(activity, R.string.please_wait);
        getRequestManager().execute(new Threading.Task<IOException, Object>() {
            @Override
            public Object runOnBackground() throws IOException {
                audioDataBase.setArtistName(audio, artistName);
                audioDataBase.setAudioName(audio, name);
                return null;
            }

            @Override
            public void onAfterCompleteOrCancelled() {
                progressDialog.dismiss();
                onCancel();
            }
        });
    }

    @Override
    public String getActionBarTitle() {
        return getString(R.string.edit_audio);
    }
}
