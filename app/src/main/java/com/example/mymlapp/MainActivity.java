package com.example.mymlapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.example.mymlapp.Controller.RestManager;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.mymlapp.MessagingClasses.CustomMessageEvent;
import com.example.mymlapp.Models.ArticleItem;
import com.example.mymlapp.Models.SummaryItem;
import com.example.mymlapp.fragment.CenteredTextFragment;
import com.example.mymlapp.fragment.EntitiesFragment;
import com.example.mymlapp.fragment.KeywordsFragment;
import com.example.mymlapp.fragment.SummarizeFragment;
import com.example.mymlapp.menu.DrawerAdapter;
import com.example.mymlapp.menu.DrawerItem;
import com.example.mymlapp.menu.SimpleItem;
import com.example.mymlapp.menu.SpaceItem;
import com.example.mymlapp.ui.QaFragment;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.callback.DragStateListener;

import org.greenrobot.eventbus.EventBus;


public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener{
    /* Drawer menu code variables*/
    private static final int POS_Entities = 0;
    private static final int POS_Summarize = 1;
    private static final int POS_Keywords = 2;
    private static final int POS_Classify = 3;
    private static final int POS_LOGOUT = 5;

    private String[] screenTitles;
    private Drawable[] screenIcons;
    public Button btnFetchData;
    private SlidingRootNav slidingRootNav;
    //************************************************************
    public  Toolbar toolbar;
    public  EditText edtTxtWebsite;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       //RichText=  findViewById(R.id.editText); ????????????????????????

        btnFetchData=findViewById(R.id.btnFetchWebsite);
        edtTxtWebsite=findViewById(R.id.editTextWebsite);
       /* Drawer Menu Section in On Create*/

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

       /* edtTxtWebsite.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v == edtTxtWebsite) {
                    if (hasFocus) {
                        // Open keyboard
                        ((InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(edtTxtWebsite, InputMethodManager.SHOW_FORCED);
                    } else {

                        // Close keyboard
                        ((InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edtTxtWebsite.getWindowToken(), 0);

                    }
                }
            }
        });*/
        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .addDragStateListener(new DragStateListener() {
                    @Override
                    public void onDragStart() {
                        // if dragging the drawer menu happens , this call back is invoked
                       // InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        //imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        // Close keyboard
                        ((InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edtTxtWebsite.getWindowToken(), 0);

                        //setEditTextFocus(false);
                    }

                    @Override
                    public void onDragEnd(boolean isMenuOpened) {
                        if(isMenuOpened==false){

                           // setEditTextFocus(true);
                        }
                    }
                })
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_Entities).setChecked(true),
                createItemFor(POS_Summarize),
                createItemFor(POS_Keywords),
                createItemFor(POS_Classify),
                new SpaceItem(48),
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_Entities);
        //************************************************ End of Drawer Section


        greetings();

    }
    // ********************************************DRAWER SECTION
    @Override
    public void onItemSelected(int position) {
        if (position == POS_LOGOUT) {
            finish();
        }
        else if(position== POS_Entities){

            Fragment selectedScreen = EntitiesFragment.newInstance("parameter1","parameter2");

            showFragment(selectedScreen);
        }
        else if(position== POS_Summarize){

            Fragment selectedScreen = SummarizeFragment.newInstance("parameter1","parameter2");
            showFragment(selectedScreen);
        }
        else if(position== POS_Keywords){
            Fragment selectedScreen = KeywordsFragment.newInstance("parameter1","parameter2");
            showFragment(selectedScreen);
        }
        else if(position==POS_Classify){
            Fragment selectedScreen = QaFragment.newInstance(this);
            showFragment(selectedScreen);
        }
        else{
            Fragment selectedScreen = CenteredTextFragment.createFor(screenTitles[position]);
            showFragment(selectedScreen);
        }
        edtTxtWebsite.setText("");
        slidingRootNav.closeMenu();

    }

    private void showFragment(androidx.fragment.app.Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.textColorSecondary))
                .withTextTint(color(R.color.textColorPrimary))
                .withSelectedIconTint(color(R.color.colorAccent))
                .withSelectedTextTint(color(R.color.colorAccent));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {

        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }
    // **************************************** ********************* END OF DRAWER SECTION


    public  void Fetch_URLData(View v){
       // edtTxtWebsite.setText("https://www.sciencealert.com/new-mars-footage-let-s-you-take-a-flight-over-the-korolev-crater-on-mars");
        if(isValid(edtTxtWebsite.getText().toString())){

            String userText = "this shit fucking works";
            RestManager restManager=new RestManager();
            CallManager callManager=restManager.getAllCallsService();

            Call<List<ArticleItem>> call=callManager.send_data_retrieve_article(edtTxtWebsite.getText().toString());

            call.enqueue(new Callback<List<ArticleItem>>() {
                @Override
                public void onResponse(Call<List<ArticleItem>> call, Response<List<ArticleItem>> response) {
                    List<ArticleItem> itemList=response.body();
                    CustomMessageEvent event = new CustomMessageEvent();
                    event.setCustomMessage(itemList.get(0).getArticle_text());
                    EventBus.getDefault().post(event);
                }

                @Override
                public void onFailure(Call<List<ArticleItem>> call, Throwable t) {

                }
            });


        }
        else{
            Toast.makeText(this, "Bad Link", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isValid(String urlString) {
        try {
            URL url = new URL(urlString);
            return URLUtil.isValidUrl(String.valueOf(url)) && Patterns.WEB_URL.matcher(String.valueOf(url)).matches();
        } catch (MalformedURLException e) {


        }

        return false;
    }
    public void setEditTextFocus(boolean isFocused) {
        edtTxtWebsite.setCursorVisible(isFocused);
        edtTxtWebsite.setFocusable(isFocused);
        edtTxtWebsite.setFocusableInTouchMode(isFocused);

        if (isFocused) {
            edtTxtWebsite.requestFocus();
        }
    }
    public void greetings(){
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if (timeOfDay >= 4 && timeOfDay < 12) {
            Toast.makeText(this, " Hello,Good Morning", Toast.LENGTH_SHORT).show();
        } else if (timeOfDay >= 13 && timeOfDay < 16) {
            Toast.makeText(this, "Hello,Good Afternoon", Toast.LENGTH_SHORT).show();
        } else if (timeOfDay >= 17 && timeOfDay < 21) {
            Toast.makeText(this, "Hello,Good Evening", Toast.LENGTH_SHORT).show();
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            Toast.makeText(this, "Hello,Good Night", Toast.LENGTH_SHORT).show();
        }
    }
}
