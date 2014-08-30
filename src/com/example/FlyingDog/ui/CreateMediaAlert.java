package com.example.FlyingDog.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import com.example.FlyingDog.R;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.data.NamedData;

import java.util.List;

/**
 * Created by CM on 8/30/2014.
 */
public abstract class CreateMediaAlert {
    private final AlertDialog dialog;
    private List<String> names;
    private Listener listener;

    protected abstract List<? extends NamedData> getNamedDataList(AudioDataManager audioDataManager);
    protected abstract int getMediaNameId();
    protected abstract void addMediaToDataBase(AudioDataManager audioDataManager, String mediaName);

    public CreateMediaAlert(final AudioDataManager audioDataManager, final Context context) {
        names = NamedData.namedDataListToNameList(getNamedDataList(audioDataManager));
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final EditText name = new EditText(context);
        builder.setView(name);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (text.isEmpty() || names.contains(text)) {
                    name.setTextColor(Color.rgb(255, 0, 0));
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    name.setTextColor(Color.rgb(255, 255, 255));
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        builder.setPositiveButton(R.string.create_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String text = name.getText().toString();

                addMediaToDataBase(audioDataManager, text);
                dialog.setOnDismissListener(null);
                dialogInterface.dismiss();
                if (listener != null) {
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

        String title = context.getString(R.string.create_media_title, context.getString(getMediaNameId()));
        String message = context.getString(R.string.create_media_message, context.getString(getMediaNameId()));
        builder.setTitle(title);
        builder.setMessage(message);

        dialog = builder.create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (listener != null) {
                    listener.onDialogClosed(false);
                }
            }
        });
    }

    public interface Listener {
        public void onDialogClosed(boolean newMediaCreated);
    }

    public void show(Listener listener) {
        this.listener = listener;
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
