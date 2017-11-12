package com.zombietechinc.rovingrepairsadmin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by User on 8/29/2017.
 */

public class EnterPriceDialogFragment extends DialogFragment {

    String service;
    double price;

    public interface NoticeDialogListener {
        public void onConfirmButtonClick(String name, double price);

    }

    NoticeDialogListener mListener;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }}



    static EnterPriceDialogFragment newInstance() {
        return new EnterPriceDialogFragment();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        service = getArguments().getString("service");
        View view = inflater.inflate(R.layout.choose_price_dialog, container, false);
        TextView questionTV = view.findViewById(R.id.dialogTV);
        final EditText priceET = view.findViewById(R.id.priceET);
        Button confirmBtn = view.findViewById(R.id.confirmbtn);
        questionTV.setText("Please enter the Price for " + service);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String priceString = String.valueOf(priceET.getText());
                if (priceString == null || priceString == "" || priceString.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter a value", Toast.LENGTH_SHORT).show();
                }else{

                    price = Double.valueOf(String.valueOf(priceET.getText()));
                    mListener.onConfirmButtonClick(service, price);
                    dismiss();
                }
            }
        });



        return view;
    }
}
