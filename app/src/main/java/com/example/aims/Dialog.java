package com.example.aims;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Dialog extends AppCompatDialogFragment {
    EditText pass;
    String Pass;
    dialogListener listener;
    @Override
    public android.app.Dialog onCreateDialog( Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog,null);

        builder.setView(view)
                .setTitle("Admin Login")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String password = pass.getText().toString();
                        listener.applyTexts(password);
                    }
                });
        pass = view.findViewById(R.id.editText5);
        Pass = pass.getText().toString();

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (dialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must implement dialogListener");
        }
    }

    public  interface  dialogListener{
        void applyTexts(String pass);
    }
}
