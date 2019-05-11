package com.example.jh.memoproject.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jh.memoproject.List_Grid_view.DailyListViewAdapter;
import com.example.jh.memoproject.List_Grid_view.DailyListViewItem;
import com.example.jh.memoproject.MainActivity;
import com.example.jh.memoproject.OnDatePickerStateSetListener;
import com.example.jh.memoproject.R;

public class MainDaily_Fragment extends ListFragment {

    private static String Tag_Name = "getNewDaily";
    private ListView listview;
    private DailyListViewAdapter adapter;
    private ImageButton newBtn, delBtn;
    private TextView year_month;
    protected NewDaily_Fragment newDaily_fragment;
    private String TagName;
    private Cursor mCursor;
    private Context context;
    Bundle args;

    FragmentTransaction ft;

    StringBuilder date;
    String memo, yearmonth, day, week, time;
    int seq, isholiday;


    //for date picker
    private int mYear, mMonth, mDay;
    private OnDatePickerStateSetListener onDatePickerStateSetListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.daily_main, container, false);

        //define
        listview = rootView.findViewById(android.R.id.list);
        newBtn = rootView.findViewById(R.id.daily_new);
        delBtn = rootView.findViewById(R.id.daily_delete);
        year_month = rootView.findViewById(R.id.daily_year);

        newDaily_fragment = new NewDaily_Fragment();
        TagName = ((MainActivity) getActivity()).newToMaindaily;
        args = new Bundle();

        // Adapter 생성 및 Adapter 지정.
        adapter = new DailyListViewAdapter();
        //setListAdapter(adapter) ;
        listview.setAdapter(adapter);

        //init now year-month.
        mYear = ((MainActivity) getActivity()).mYear;
        mMonth = ((MainActivity) getActivity()).mMonth;
        mDay = ((MainActivity) getActivity()).mDay;
        //year_mont.setText and show listview
        UpdateNow();

        year_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), mDateSetListener, mYear, mMonth, mDay).show();
            }
        });

        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("status", "new");//new
                args.putInt("picker_mYear", mYear);
                args.putInt("picker_mMonth", mMonth);
                args.putInt("picker_mDay", mDay);
                newDaily_fragment.setArguments(args);
                ((MainActivity) getActivity()).addFragment(ft, newDaily_fragment, R.id.fragment_main, TagName);
                Log.d("fragment_change", "main(daily) -> new daily");
            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.showDeleteButton();
            }
        });
        //return super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        DailyListViewItem item = (DailyListViewItem) l.getItemAtPosition(position);
        int seq = item.getSeq();
        String day = item.getDay();
        String week = item.getWeek();
        String memo = item.getMemo();
        String year_month = item.getYear_month();
        int holiday = item.getHoliday();

        args.putString("status", "edit");
        args.putInt("seq", seq);
        args.putString("year_month", year_month);
        args.putString("day", day);
        args.putString("week", week);
        args.putString("memo", memo);
        args.putInt("holiday", holiday);
        newDaily_fragment.setArguments(args);
        ((MainActivity) getActivity()).addFragment(ft, newDaily_fragment, R.id.fragment_main, TagName);
    }

    private void doWhileCursorToArray() {

        mCursor = ((MainActivity) getActivity()).dbHelper.getReadableDatabase().rawQuery(
                "SELECT *" +
                        " FROM DIARY" +
                        " WHERE YEAR = " +
                        "'" +
                        this.year_month.getText().toString() +
                        "' ORDER BY year desc, day desc, week desc, time desc", null);

        Log.e("DiaryDatabase Get", "COUNT = " + mCursor.getCount());

        while (mCursor.moveToNext()) {
            seq = mCursor.getInt(0);
            memo = mCursor.getString(1);
            yearmonth = mCursor.getString(2);
            day = mCursor.getString(3);
            week = mCursor.getString(4);
            time = mCursor.getString(5);
            isholiday = mCursor.getInt(6);

            if (isholiday == 2 && ((MainActivity)getActivity()).isNetworkAvailable(getContext())) {
                date = new StringBuilder();
                date.append(yearmonth.replaceAll("[^0-9]", ""));
                if (day.length() == 1) {
                    date.append("0");
                    date.append(day);
                } else {
                    date.append(day);
                }
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        isholiday = ((MainActivity)getActivity()).getHolidayAPI(date.substring(0, 4), date.substring(4, 6), date.toString());
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                adapter.addItem(seq, memo, yearmonth, day, week, time, isholiday);
                            }
                        });
                    }
                }.start();
            } else {
                adapter.addItem(seq, memo, yearmonth, day, week, time, isholiday);
            }

        }
        mCursor.close();
    }

    DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;

            //update textview value
            UpdateNow();
        }
    };

    void UpdateNow() {
        year_month.setText(String.format("%d년 %02d월", mYear, mMonth + 1));
        // database to listview -> show items
        adapter.clearItem();
        adapter.notifyDataSetChanged();
        doWhileCursorToArray();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mYear = savedInstanceState.getInt("save_mYear");
            mMonth = savedInstanceState.getInt("save_mMonth");
            mDay = savedInstanceState.getInt("save_mDay");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof OnDatePickerStateSetListener) {
            onDatePickerStateSetListener = (OnDatePickerStateSetListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implements onDatePickerStateSetListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        onDatePickerStateSetListener.onDatePickerStateSetListener(mYear, mMonth, mDay);
    }

}
