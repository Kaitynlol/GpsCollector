package avnatarkin.hse.ru.gpscollector.main.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import avnatarkin.hse.ru.gpscollector.R;

/**
 * Created by sanjar on 11.12.16.
 */

public class ChangeSyncTimeFragment extends DialogFragment {
    public interface SyncDialogListener {
        public void onSyncDialogPositiveClick(DialogFragment dialog, int which);
    }

    public ChangeSyncTimeFragment.SyncDialogListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.dialogSyncFragment_title));
        builder.setItems(R.array.dialogFragment_syncTimes, new ChangeSyncTimeFragment.YesOptionClickListener());

        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (ChangeSyncTimeFragment.SyncDialogListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    private class YesOptionClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mListener.onSyncDialogPositiveClick(ChangeSyncTimeFragment.this, which);
        }
    }
}
