package com.cloupix.groupmail.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cloupix.groupmail.R;


/**
 * Created by alonsoapp on 28/04/16.
 *
 */
public class AddEmailDialog extends DialogFragment {

    private static final String TAG = "dialog_add_email";

    private EditText editText;

    private EditTextDialogCallbacks mListener;


    public void setEditTextDialogCallbacks(EditTextDialogCallbacks mCallbacks){
        this.mListener = mCallbacks;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        View rootView = inflater.inflate(R.layout.dialog_add_email, null);
        loadViewElements(rootView);



        builder.setView(rootView);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogSet(editText.getText().toString());
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogCancel();
                dismiss();
            }
        });
        return builder.create();
    }

    private void loadViewElements(View rootView){

        editText = (EditText) rootView.findViewById(R.id.editText);

        /*
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        */
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the BoletusLoginDialogListener in case the host needs to query it. */
    public interface EditTextDialogCallbacks {

        //public void onDialogLoginClick(String userEmail, String userPassword);

        void onDialogSet(String text);
        void onDialogCancel();

    }

}
