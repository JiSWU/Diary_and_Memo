package com.example.jh.memoproject.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jh.memoproject.DBHelper;
import com.example.jh.memoproject.LinedEditText;
import com.example.jh.memoproject.MainActivity;
import com.example.jh.memoproject.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class NewDaily_Fragment extends Fragment {

    private LinedEditText newdaily_memo;
    private TextView newdaily_year, newdaily_day, newdaily_save, newdaily_week;
    private ImageButton newdaily_back;
    private String TagName, status;

    //for database
    private DBHelper dbHelper;
    protected int seq;
    private String memo, year_month, day, week, time;
    //for date picker
    private int mYear, mMonth, mDay;
    private String mweek;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.daily_new, container, false);

        newdaily_memo = (LinedEditText) rootView.findViewById(R.id.newdaily_editmemo);
        newdaily_year = (TextView) rootView.findViewById(R.id.newdaily_year);
        newdaily_day = (TextView) rootView.findViewById(R.id.newdaily_day);
        newdaily_week = (TextView) rootView.findViewById(R.id.newdaily_week);
        newdaily_back = (ImageButton) rootView.findViewById(R.id.daily_back);
        newdaily_save = (TextView) rootView.findViewById(R.id.daily_save);

        TagName = ((MainActivity)getActivity()).newToMaindaily;
        dbHelper = ((MainActivity)getActivity()).dbHelper;

        Bundle recvmsg = getArguments();

        status = recvmsg.getString("status"); //edit or new

        if(recvmsg!=null){
            if(status.matches("edit")) {
                seq = recvmsg.getInt("seq");
                newdaily_year.setText(recvmsg.getString("year_month"));
                newdaily_day.setText(recvmsg.getString("day"));
                newdaily_week.setText(recvmsg.getString("week"));
                newdaily_memo.setText(recvmsg.getString("memo"));
            }else if(status.equals("new")){
                mYear = recvmsg.getInt("picker_mYear");
                mMonth = recvmsg.getInt("picker_mMonth");
                mDay = recvmsg.getInt("picker_mDay");
                UpdateNow();
            }
        } //첫 화면 초기 출력

        newdaily_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClick(v);
            }
        });

        newdaily_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClick(v);
            }
        });

        newdaily_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClick(v);
            }
        });

        newdaily_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memo = newdaily_memo.getText().toString();
                year_month = newdaily_year.getText().toString();
                day = newdaily_day.getText().toString();
                week = newdaily_week.getText().toString();
                time = new SimpleDateFormat("a HH:mm").format(new Date());

                if(status.equals("new")){
                    if(memo.equals("")){
                        Toast.makeText(getContext(),"write content", Toast.LENGTH_SHORT).show();
                    }else {
                        dbHelper.daily_insert(memo, year_month, day, week, time);
                        ((MainActivity)getActivity()).backFragment(TagName);
                        Log.d("fragment_change", "new daily -> main(daily)");
                    }
                } else if(status.equals("edit")) {
                    dbHelper.daily_update(memo, year_month, day, week, time, seq);
                    ((MainActivity)getActivity()).backFragment(TagName);
                    Log.d("fragment_change", "new daily -> main(daily)");
                }

            }
        });

        newdaily_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).backFragment(TagName);
                Log.d("fragment_change", "new daily -> main(daily)");
            }
        });

        return rootView;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void mOnClick(View v){
        if(v.getId() == R.id.newdaily_year || v.getId() == R.id.newdaily_day || v.getId() == R.id.newdaily_week) {
            new DatePickerDialog(getActivity(), mDateSetListener, mYear,mMonth, mDay).show();
        }
    }

    DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;

            //update textview value
            UpdateNow();
        }
    };

    public void UpdateNow(){
        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        Date d = new Date(mYear, mMonth, mDay);
        mweek = sdf.format(d);

        newdaily_year.setText(String.format("%d년 %d월",mYear, mMonth+1));
        newdaily_day.setText(String.format("%2d",mDay));
        newdaily_week.setText(mweek);
    }

}
