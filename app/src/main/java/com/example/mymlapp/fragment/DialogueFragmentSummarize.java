package com.example.mymlapp.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mymlapp.R;

public class DialogueFragmentSummarize extends DialogFragment {

    public  EditText edtSummarizedText;
    private String mParamSummaryText;
    public Button btnShareSummary;
    public DialogueFragmentSummarize() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static DialogueFragmentSummarize newInstance(String title) {
        DialogueFragmentSummarize frag = new DialogueFragmentSummarize();
        Bundle args = new Bundle();
        args.putString("sumarizedText", title);
        frag.setArguments(args);
        return frag;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamSummaryText = getArguments().getString("sumarizedText");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.dialog_fragment_summarize, container);

        // Set transparent background and no title
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        return  v;
    }
    public  void shareData(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, edtSummarizedText.getText().toString());
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }
    @Override
    public void onStart()
    {//https://stackoverflow.com/questions/12478520/how-to-set-dialogfragments-width-and-height
        super.onStart();

        // safety check
        if (getDialog() == null)
            return;

        int dialogWidth = 1000; // specify a value here
        int dialogHeight = 1500; // specify a value here

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

        // ... other stuff you want to do in your onStart() method
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        btnShareSummary=view.findViewById(R.id.btnShareSummarizedText);

        // Get field from view
        edtSummarizedText = (EditText) view.findViewById(R.id.edtSummarizedText);
        edtSummarizedText.setText(mParamSummaryText);
        // Fetch arguments from bundle and set title
       // String title = getArguments().getString("title", "Enter Name");
        //getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
      /*  mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);*/
        btnShareSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareData();
            }
        });
    }
}
