package com.cloupix.groupmail.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.cloupix.groupmail.R;


/**
 * Created by alonsoapp on 28/04/16.
 *
 */
public class EditTextDialog extends DialogFragment {

    private static final String TAG = "dialog_edit_text";

    private TextView textViewTitle;
    private EditText editText;

    private EditTextDialogCallbacks mListener;

    private String title, hint;
    private int inputType;
    private Context context;


    public void setEditTextDialogCallbacks(EditTextDialogCallbacks mCallbacks){
        this.mListener = mCallbacks;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        View rootView = inflater.inflate(R.layout.dialog_edit_text, null);
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

        textViewTitle = (TextView) rootView.findViewById(R.id.textViewTitle);
        editText = (EditText) rootView.findViewById(R.id.editText);
        editText.setInputType(inputType);

        textViewTitle.setText(title);
        editText.setHint(hint);
        /*
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        */
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setHint(String hint) {
        this.hint = hint;
    }
    public void setInputType(int inputType) {
        this.inputType = inputType;
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
