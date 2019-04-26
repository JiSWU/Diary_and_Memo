package com.example.jh.memoproject;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jh.memoproject.fragment.MainDaily_Fragment;
import com.example.jh.memoproject.fragment.Memo_Fragment;

public class MainActivity extends AppCompatActivity {

    public static String newToMaindaily = "daily";
    public static String newToMainmemo = "memo";
    public DBHelper dbHelper; //new

    protected boolean daily = true;
    private LinearLayout daily_ll, memo_ll;
    private ImageView daily_iv,memo_iv;
    private TextView daily_tv, memo_tv;
    private MainDaily_Fragment mainDaily_fragment;
    private Memo_Fragment memo_fragment;

    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //define
        daily_ll = (LinearLayout)findViewById(R.id.daily_daily);
        daily_iv = (ImageView)findViewById(R.id.daily_daily_iv);
        daily_tv = (TextView)findViewById(R.id.daily_daily_tv);

        memo_ll = (LinearLayout)findViewById(R.id.daily_memo);
        memo_iv = (ImageView) findViewById(R.id.daily_memo_iv);
        memo_tv = (TextView)findViewById(R.id.daily_memo_tv);

        mainDaily_fragment = new MainDaily_Fragment();
        memo_fragment = new Memo_Fragment();
        dbHelper = new DBHelper(this);

        //first Fragment setting
        Daily_Clicked(daily);
        replaceFragment(ft, mainDaily_fragment, R.id.fragment_main);

        daily_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daily = true;
                Daily_Clicked(daily);
                replaceFragment(ft, mainDaily_fragment, R.id.fragment_main);
                Log.d("fragment_change", "daily Fragment in MainActivity");
            }
        }); // end clicklistener()

        memo_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daily = false;
                Daily_Clicked(daily);
                replaceFragment(ft, memo_fragment, R.id.fragment_main);
                Log.d("fragment_change", "memo Fragment in MainActivity");
            }
        }); // end clicklistener()

    }

    public void Daily_Clicked(boolean flag){
        if(flag){
            daily_iv.setSelected(true);
            daily_tv.setSelected(true);
            memo_iv.setSelected(false);
            memo_tv.setSelected(false);
        }else {
            daily_iv.setSelected(false);
            daily_tv.setSelected(false);
            memo_iv.setSelected(true);
            memo_tv.setSelected(true);
        }
    } //end Daily_Clicked()

    public void replaceFragment(FragmentTransaction fragmentTransaction, Fragment fragment, int R_id){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R_id, fragment);
        fragmentTransaction.commit();
    } //end ReplaceFragment()


    public void addFragment(FragmentTransaction fragmentTransaction, Fragment fragment, int R_id, String fragmentTag){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R_id, fragment);
        fragmentTransaction.addToBackStack(fragmentTag);
        fragmentTransaction.commit();
    } //end ReplaceFragment()

    public void backFragment(String fragmentTag){
        getSupportFragmentManager().popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    } //end ReplaceFragment()

}
