package com.example.testpathlib.view.fragment;

import com.example.testpathlib.R;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Julien Quiévreux on 23/12/13.
 */
public class ErrorDialogFragment extends DialogFragment
{

    private TextView tv;
    private String text;
    public ErrorDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.error_fragment_dialog, container);
        tv = (TextView) view.findViewById(R.id.tvErrorMessage);
        tv.setText(text);
        getDialog().setTitle("Error");

        return view;
    }


    public void setText(String text)
    {
        this.text = text;
    }
}
