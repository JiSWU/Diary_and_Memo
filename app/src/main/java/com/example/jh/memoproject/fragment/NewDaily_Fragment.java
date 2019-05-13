package com.example.jh.memoproject.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
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
import java.util.Date;


public class NewDaily_Fragment extends Fragment {

    private LinedEditText newdaily_memo;
    private TextView newdaily_year, newdaily_day, newdaily_save, newdaily_week;
    private ImageButton newdaily_back;
    private String TagName, status;

    //for database
    private DBHelper dbHelper;
    protected int seq, holiday;
    private String memo, year_month, day, week, time;
    //for date picker
    private int mYear, mMonth, mDay;
    private String mweek;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            UpdateNow(); //update textview value
        }
    };

    public void mOnClick(View v){
        if(v.getId() == R.id.newdaily_year || v.getId() == R.id.newdaily_day || v.getId() == R.id.newdaily_week) {
            new DatePickerDialog(getActivity(), mDateSetListener, mYear,mMonth, mDay).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.daily_new, container, false);

        newdaily_memo = rootView.findViewById(R.id.newdaily_editmemo);
        newdaily_year = rootView.findViewById(R.id.newdaily_year);
        newdaily_day = rootView.findViewById(R.id.newdaily_day);
        newdaily_week = rootView.findViewById(R.id.newdaily_week);
        newdaily_back = rootView.findViewById(R.id.daily_back);
        newdaily_save = rootView.findViewById(R.id.daily_save);

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
                holiday = recvmsg.getInt("holiday");
                if(holiday==1){
                    newdaily_day.setTextColor(getContext().getResources().getColorStateList(R.color.memo_weekend, null));
                    newdaily_week.setTextColor(getContext().getResources().getColorStateList(R.color.memo_weekend, null));
                }else {
                    newdaily_day.setTextColor(getContext().getResources().getColor(R.color.memo_week, null));
                    newdaily_week.setTextColor(getContext().getResources().getColor(R.color.memo_week, null));
                }

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

                final StringBuilder date = new StringBuilder();
                date.append(year_month.replaceAll("[^0-9]", ""));

                if(day.length()==1){
                    date.append("0");
                    date.append(day);
                }else
                    date.append(day);

                if(((MainActivity)getActivity()).isNetworkAvailable(getContext())){
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            holiday = ((MainActivity)getActivity()).getHolidayAPI(date.substring(0,4), date.substring(4,6),date.toString());
                            Log.d("holiday", "this value is holiday: " + date.substring(0,4) + ", " + date.substring(4,6) +", " + date.toString());

                            if(status.equals("new")){
                                if(memo.equals("")){
                                    getActivity().runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(getContext(),"write content", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else {
                                    dbHelper.daily_insert(memo, year_month, day, week, time, holiday);
                                    ((MainActivity)getActivity()).backFragment(TagName);
                                    Log.d("fragment_change", "new daily -> main(daily)");
                                }
                            } else if(status.equals("edit")) {
                                dbHelper.daily_update(memo, year_month, day, week, time, seq, holiday);
                                ((MainActivity)getActivity()).backFragment(TagName);
                                Log.d("fragment_change", "new daily -> main(daily)");

                                Log.d("holiday", "this value is holiday: "+holiday);
                            }
                        }
                    }.start();
                }else{
                    if(status.equals("new")){
                        if(memo.equals("")){
                            Toast.makeText(getContext(),"write content", Toast.LENGTH_SHORT).show();
                        }else {
                            dbHelper.daily_insert(memo, year_month, day, week, time, 2);
                            ((MainActivity)getActivity()).backFragment(TagName);
                            Log.d("fragment_change", "new daily -> main(daily)");
                        }
                    } else if(status.equals("edit")) {
                        dbHelper.daily_update(memo, year_month, day, week, time, seq, holiday);
                        ((MainActivity)getActivity()).backFragment(TagName);
                        Log.d("fragment_change", "new daily -> main(daily)");
                        Log.d("holiday", "this value is holiday: " + holiday);
                    }
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
    }

    public void UpdateNow(){
        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        Date d = new Date(mYear, mMonth, mDay);
        mweek = sdf.format(d);

        newdaily_year.setText(String.format("%d년 %02d월",mYear, mMonth+1));
        newdaily_day.setText(String.format("%d",mDay));
        newdaily_week.setText(mweek);
    }

}
