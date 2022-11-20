package com.example.mymlapp.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mymlapp.MessagingClasses.CustomMessageEvent;
import com.example.mymlapp.R;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.speech.tts.TextToSpeech;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymlapp.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import java.util.List;
import java.util.Locale;

import com.example.mymlapp.ml.LoadDatasetClient;
import com.example.mymlapp.ml.QaAnswer;
import com.example.mymlapp.ml.QaClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String DATASET_POSITION_KEY = "DATASET_POSITION";
    private static final String TAG = "QaActivity";
    private static final boolean DISPLAY_RUNNING_TIME = false;

    private TextInputEditText questionEditText;
    private TextView contentTextView;
    private TextToSpeech textToSpeech;

    private boolean questionAnswered = false;
    private String content;
    private Handler handler;
    private QaClient qaClient;

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;

    public QaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment QaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QaFragment newInstance(Context context) {
        QaFragment fragment = new QaFragment();
        Bundle args = new Bundle();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.tfe_qa_activity_qa, container, false);

        contentTextView = v.findViewById(R.id.content_text);
        //content="\"Super Bowl 50 was an American football game to determine the champion of the National Football League (NFL) for the 2015 season. The American Football Conference (AFC) champion Denver Broncos defeated the National Football Conference (NFC) champion Carolina Panthers 24\\u201310 to earn their third Super Bowl title. The game was played on February 7, 2016, at Levi's Stadium in the San Francisco Bay Area at Santa Clara, California. As this was the 50th Super Bowl, the league emphasized the \\\"golden anniversary\\\" with various gold-themed initiatives, as well as temporarily suspending the tradition of naming each Super Bowl game with Roman numerals (under which the game would have been known as \\\"Super Bowl L\\\"), so that the logo could prominently feature the Arabic numerals 50.";

        contentTextView.setText(content);
        contentTextView.setMovementMethod(new ScrollingMovementMethod());
        ImageButton askButton = v.findViewById(R.id.ask_button);
        askButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content=contentTextView.getText().toString();
                if(content!=""){
                    Toast.makeText(getActivity(), "content not null", Toast.LENGTH_SHORT).show();
                    answerQuestion(questionEditText.getText().toString());
                }
                else{
                    Toast.makeText(getActivity(), "Extract Text to Query and get answers", Toast.LENGTH_SHORT).show();
                }
            }
        });

        questionEditText = v.findViewById(R.id.question_edit_text);
        questionEditText.setOnFocusChangeListener(
                (view, hasFocus) -> {
                    // If we already answer current question, clear the question so that user can input a new
                    // one.
                    if (hasFocus && questionAnswered) {
                        questionEditText.setText(null);
                    }
                });
        questionEditText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        // Only allow clicking Ask button if there is a question.
                        boolean shouldAskButtonActive = !charSequence.toString().isEmpty();
                        askButton.setClickable(shouldAskButtonActive);
                        askButton.setImageResource(
                                shouldAskButtonActive ? R.drawable.ic_ask_active : R.drawable.ic_ask_inactive);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {}
                });
                questionEditText.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                            if(!contentTextView.getText().toString().equals(null)){


                                Log.v("Problem", questionEditText.getText().toString());
                                answerQuestion(questionEditText.getText().toString());
                              }
                            else{
                                Toast.makeText(getActivity(), "Extract Text to Query and get answers", Toast.LENGTH_SHORT).show();
                            }
                        }
                        return false;
                    }
                });

        // Setup QA client to and background thread to run inference.
        HandlerThread handlerThread = new HandlerThread("QAClient");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        qaClient = new QaClient(getActivity());
        return  v;
    }
    @Override
    public void onStart() {
        Log.v(TAG, "onStart");
        super.onStart();
        EventBus.getDefault().register(this);//register to receive notifictions from event buss

        handler.post(
                () -> {
                    qaClient.loadModel();
                    qaClient.loadDictionary();
                });

        textToSpeech =
                new TextToSpeech(
                        getActivity(),
                        status -> {
                            if (status == TextToSpeech.SUCCESS) {
                                textToSpeech.setLanguage(Locale.US);
                            } else {
                                textToSpeech = null;
                            }
                        });
        Log.v(TAG, "ending");
    }
    @Override
    public void onStop() {
        Log.v(TAG, "onStop");
        super.onStop();
        EventBus.getDefault().unregister(this);
        handler.post(() -> qaClient.unload());

        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
    @Subscribe
    //after declaring that this fragment shall listen to Event Bus(in the onCreate we registered)
    // step 2 in the listening fragment is to ask : what kind of events we want to receive(subscribe to )
    public void onEvent(CustomMessageEvent event){
        //Toast.makeText(getActivity(),"Event fired " +   event.getCustomMessage(), Toast.LENGTH_SHORT).show();

        // Log.d("ElectronicArmory", "Event fired " + event.getCustomMessage());
  //      content=event.getCustomMessage();
        contentTextView.setText(event.getCustomMessage());
    }

    private void answerQuestion(String question) {
        content=contentTextView.getText().toString();
        question = question.trim();
        if (question.isEmpty()) {
            questionEditText.setText(question);
            return;
        }
        Log.v("eeeeeeeeee",question);
        // Append question mark '?' if not ended with '?'.
        // This aligns with question format that trains the model.
        if (!question.endsWith("?")) {
            question += '?';
        }
        final String questionToAsk = question;
        questionEditText.setText(questionToAsk);
        Log.v("eeeeeeeeee",questionToAsk);
        // Delete all pending tasks.
        handler.removeCallbacksAndMessages(null);

        // Hide keyboard and dismiss focus on text edit.
        InputMethodManager imm =
                (InputMethodManager) getActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
        View focusView = getActivity().getCurrentFocus();
        if (focusView != null) {
            focusView.clearFocus();
        }

        // Reset content text view
//        Log.v("cccccccccccc",content);
        contentTextView.setText(content);

        questionAnswered = false;

        Snackbar runningSnackbar =
                Snackbar.make(contentTextView, "Looking up answer...", Integer.MAX_VALUE);
        runningSnackbar.show();

        // Run TF Lite model to get the answer.
        handler.post(
                () -> {
                    long beforeTime = System.currentTimeMillis();
                    Log.v(TAG,questionToAsk.toString());
//                    Log.v(TAG,content.toString());
                    final List<QaAnswer> answers = qaClient.predict(questionToAsk, content);
                    long afterTime = System.currentTimeMillis();
                    double totalSeconds = (afterTime - beforeTime) / 1000.0;

                    if (!answers.isEmpty()) {
                        // Get the top answer
                        QaAnswer topAnswer = answers.get(0);
                        // Show the answer.
                        getActivity().runOnUiThread(
                                () -> {
                                    runningSnackbar.dismiss();
                                    presentAnswer(topAnswer);

                                    String displayMessage = "Top answer was successfully highlighted.";
                                    if (DISPLAY_RUNNING_TIME) {
                                        displayMessage = String.format("%s %.3fs.", displayMessage, totalSeconds);
                                    }
                                    Snackbar.make(contentTextView, displayMessage, Snackbar.LENGTH_LONG).show();
                                    questionAnswered = true;
                                });
                    }
                });
    }
    private void presentAnswer(QaAnswer answer) {
        // Highlight answer.
        Spannable spanText = new SpannableString(content);
        int offset = content.indexOf(answer.text, 0);
        if (offset >= 0) {
            spanText.setSpan(
                    new BackgroundColorSpan(getActivity().getColor(R.color.tfe_qa_color_highlight)),
                    offset,
                    offset + answer.text.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        contentTextView.setText(spanText);

        // Use TTS to speak out the answer.
        if (textToSpeech != null) {
            textToSpeech.speak(answer.text, TextToSpeech.QUEUE_FLUSH, null, answer.text);
        }
    }
}