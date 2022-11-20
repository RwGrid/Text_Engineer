package com.example.mymlapp.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mymlapp.CallManager;
import com.example.mymlapp.Controller.RestManager;
import com.example.mymlapp.MessagingClasses.CustomMessageEvent;
import com.example.mymlapp.Models.Item;
import com.example.mymlapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EntitiesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntitiesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public EditText RichText;
    public Button btnSubmitText;
    public FloatingActionButton fabClearRichtxt;
    public EntitiesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EntitiesFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static EntitiesFragment newInstance(String param1, String param2) {
        EntitiesFragment fragment = new EntitiesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_entities, container, false);

        RichText=v.findViewById(R.id.editTextEntities);
        btnSubmitText=v.findViewById(R.id.btnSubmit);


        btnSubmitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData(v);
            }
        });
        fabClearRichtxt=v.findViewById(R.id.fabClearRichTextEntities);

        fabClearRichtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearRichText(v);
            }
        });
        RichText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fabClearRichtxt.setVisibility(s.length() > 0 ? View.VISIBLE : View.INVISIBLE);
            }
        });
        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                v.getWindowVisibleDisplayFrame(r);
                if (v.getRootView().getHeight() - (r.bottom - r.top) > 500) {
                    //Log.d("keyboardStatus","opened");
                    btnSubmitText.setVisibility(View.GONE);


                } else {
                    // Log.d("keyboardStatus","closed");
                    btnSubmitText.setVisibility(View.VISIBLE);

                }
            }
        });
        return  v;

    }
    public   void submitData(View v){

        String submitted_Rich_Text=RichText.getText().toString();
        //Toast.makeText(this, submitted_Rich_Text, Toast.LENGTH_SHORT).show();
        RestManager restManager=new RestManager();
        CallManager callManager=restManager.getAllCallsService();

        Call<List<Item>> call=callManager.send_data_retrieve_enities(submitted_Rich_Text);

        // Call<List<Item>> call=callManager.get_All_Items();
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getActivity(), "successful", Toast.LENGTH_SHORT).show();
                    List<Item> itemList=response.body();

                    StringBuilder x=new StringBuilder();
                    for (int i=0;i<itemList.size();i++){
                        x.append(itemList.get(i).getLabel());
                        x.append(" :   ");
                        x.append(itemList.get(i).getText());
                        x.append('\n');


                    }
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, x.toString());
                    sendIntent.setType("text/plain");

                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    startActivity(shareIntent);
                   // FormExcel(x);
                    //Toast.makeText(getActivity(),x.toString() , Toast.LENGTH_SHORT).show();
                }
                else{
                    int statusCode=response.code();
                    switch (statusCode){
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            break;
                        default:
                            Log.e("Error", "Generic Error");

                    }
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Toast.makeText(getActivity(), "fail" + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });



    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);//register to receive notifictions from event buss
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe
    //after declaring that this fragment shall listen to Event Bus(in the onCreate we registered)
    // step 2 in the listening fragment is to ask : what kind of events we want to receive(subscribe to )
    public void onEvent(CustomMessageEvent event){
        //Toast.makeText(getActivity(),"Event fired " +   event.getCustomMessage(), Toast.LENGTH_SHORT).show();

        // Log.d("ElectronicArmory", "Event fired " + event.getCustomMessage());
        RichText.setText(event.getCustomMessage());
    }


    public void FormExcel(StringBuilder Concatenated_String){
        String columnString =   "\"Text\",\"Entity Type\"";
        String dataString   =   "\"" + Concatenated_String +"\",\"" ;
        String combinedString = columnString + "\n" + dataString;

        File file   = null;
        File root   = Environment.getExternalStorageDirectory();
        if (root.canWrite()){
            File dir    =   new File (root.getAbsolutePath() + "/PersonData");
            dir.mkdirs();
            file   =   new File(dir, "Data.csv");
            FileOutputStream out   =   null;
            try {
                out = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                out.write(combinedString.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Uri u1  =   null;
        //u1  =   Uri.fromFile(file);

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Person Details");
        if (u1 != null) {
       // sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
        }

        sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, combinedString);
        sendIntent.setType("text/html");
        startActivity(sendIntent);
    }
    public  void clearRichText(View v){
        RichText.setText("");
        EditText edtTxtWebsite=getActivity().findViewById(R.id.editTextWebsite);
        edtTxtWebsite.setText("");


    }
    public void GoToQA_Activity(View v){
        //Intent intent = new Intent(getActivity(), DatasetListActivity.class);

        //startActivity(intent);
    }


}
