package com.example.FlyingDog.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import com.example.FlyingDog.R;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.data.NamedData;
import com.tiksem.media.data.PlayList;
import com.utilsframework.android.view.UiMessages;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CM on 8/29/2014.
 */
public class CreatePlayListAlert {
    private final AlertDialog dialog;
    private List<String> playLists;
    private Listener listener;

    public CreatePlayListAlert(final AudioDataManager audioDataManager, final Context context) {
        playLists = NamedData.namedDataListToNameList(audioDataManager.getPlayLists());
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final EditText playListName = new EditText(context);
        builder.setView(playListName);
        playListName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if(playLists.contains(text)){
                    playListName.setTextColor(Color.rgb(255, 0, 0));
                } else {
                    playListName.setTextColor(Color.rgb(255, 255, 255));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        builder.setPositiveButton(R.string.create_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String text = playListName.getText().toString();

                if(text.isEmpty()){
                    UiMessages.error(context, R.string.play_list_name_empty);
                    return;
                }

                if(playLists.contains(text)){
                    UiMessages.error(context, R.string.play_list_exists, text);
                    return;
                }

                audioDataManager.addPlayList(text);
                dialog.setOnDismissListener(null);
                dialogInterface.dismiss();
                if(listener != null){
                    listener.onDialogClosed(true);
                }
            }
        });

        builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setTitle(R.string.create_play_list_title);
        builder.setMessage(R.string.create_play_list_message);

        dialog = builder.create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(listener != null){
                    listener.onDialogClosed(false);
                }
            }
        });
    }

    public interface Listener {
        public void onDialogClosed(boolean playListCreated);
    }

    public void show(Listener listener) {
        this.listener = listener;
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
