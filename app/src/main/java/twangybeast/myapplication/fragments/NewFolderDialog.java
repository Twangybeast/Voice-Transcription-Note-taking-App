package twangybeast.myapplication.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;

import twangybeast.myapplication.R;

/**
 * Created by cHeNdAn19 on 3/6/2018.
 */

public class NewFolderDialog extends DialogFragment {
    public static final String TAG = "NewFolderDialog";
    public interface NewFolderListener
    {
        public void onNewFolderDialogPositiveClick(DialogFragment dialog);
        public void onNewFolderDialogNegativeClick(DialogFragment dialog);
    }
    private NewFolderListener mListener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setTitle(R.string.title_new_folder_dialog);
        builder.setView(inflater.inflate(R.layout.dialog_new_folder, null))
                .setPositiveButton(R.string.button_create_folder, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onNewFolderDialogPositiveClick(NewFolderDialog.this);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onNewFolderDialogNegativeClick(NewFolderDialog.this);
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            mListener = (NewFolderListener) activity;
        } catch (ClassCastException e)
        {
            Log.e(TAG, "Could not cast activity to listener");
            e.printStackTrace();
        }
    }
}
