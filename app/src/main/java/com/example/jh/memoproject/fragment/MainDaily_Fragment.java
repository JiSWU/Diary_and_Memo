package com.example.jh.memoproject.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jh.memoproject.List_Grid_view.DailyListViewAdapter;
import com.example.jh.memoproject.List_Grid_view.DailyListViewItem;
import com.example.jh.memoproject.MainActivity;
import com.example.jh.memoproject.R;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainDaily_Fragment extends ListFragment {

    private static String Tag_Name = "getNewDaily";
    private ListView listview;
    private DailyListViewAdapter adapter;
    private ImageButton newBtn, delBtn;
    private TextView year_month;
    protected NewDaily_Fragment newDaily_fragment;
    private String TagName;
    private Cursor mCursor;
    Bundle args;

    FragmentTransaction ft;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.daily_main, container, false);

        //define
        listview = (ListView) rootView.findViewById(android.R.id.list);
        newBtn = (ImageButton) rootView.findViewById(R.id.daily_new);
        delBtn = (ImageButton) rootView.findViewById(R.id.daily_delete);
        year_month = (TextView) rootView.findViewById(R.id.daily_year);

        newDaily_fragment = new NewDaily_Fragment();
        TagName = ((MainActivity)getActivity()).newToMaindaily;
        args = new Bundle();

        // Adapter 생성 및 Adapter 지정.
        adapter = new DailyListViewAdapter() ;
        //setListAdapter(adapter) ;
        listview.setAdapter(adapter);

        // database to listview -> show items
        doWhileCursorToArray();

        if(adapter.getCount() == 0){
            year_month.setVisibility(View.GONE);
        }else{
            year_month.setText("not null");
        }


        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("status", "new");//new
                newDaily_fragment.setArguments(args);
                ((MainActivity)getActivity()).addFragment(ft,newDaily_fragment, R.id.fragment_main, TagName);
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

        DailyListViewItem item = (DailyListViewItem) l.getItemAtPosition(position) ;
        int seq = item.getSeq();
        String day = item.getDay() ;
        String week = item.getWeek() ;
        String memo = item.getMemo() ;
        String year_month = item.getYear_month();

        args.putString("status", "edit");
        args.putInt("seq", seq);
        args.putString("year_month", year_month);
        args.putString("day", day);
        args.putString("week", week);
        args.putString("memo", memo);
        newDaily_fragment.setArguments(args);
        ((MainActivity)getActivity()).addFragment(ft,newDaily_fragment, R.id.fragment_main, TagName);
    }

    private void doWhileCursorToArray(){

        String memo, year_month, day, week, time;
        int seq;

        mCursor = ((MainActivity)getActivity()).dbHelper.getReadableDatabase().rawQuery(
                "SELECT *" +
                        " FROM DIARY ORDER BY year, day, week, time", null);

        Log.e("MemoDatabase Get", "COUNT = " + mCursor.getCount());

        while (mCursor.moveToNext()) {
            seq = mCursor.getInt(0);
            memo = mCursor.getString(1);
            year_month = mCursor.getString(2);
            day = mCursor.getString(3);
            week = mCursor.getString(4);
            time = mCursor.getString(5);
            adapter.addItem(seq, memo, year_month, day, week, time);
        }

        mCursor.close();
    }


}
