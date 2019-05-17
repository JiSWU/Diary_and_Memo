package com.example.jh.memoproject.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import com.example.jh.memoproject.List_Grid_Adapter.MemoGridViewAdapter;
import com.example.jh.memoproject.List_Grid_Adapter.MemoGridViewItem;
import com.example.jh.memoproject.MainActivity;
import com.example.jh.memoproject.R;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Memo_Fragment extends Fragment {


    private GridView gridView;
    private MemoGridViewAdapter adapter;
    private ImageButton newBtn, delBtn, backBtn;
    private NewMemo_Fragment newMemo_fragment;
    private MainDaily_Fragment mainDaily_fragment;
    private FragmentTransaction ft;
    private String TagName;
    private Cursor mCursor;
    Bundle args;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.memo, container, false);

        gridView = rootView.findViewById(R.id.memo_gridview);
        newBtn = rootView.findViewById(R.id.memo_new);
        delBtn = rootView.findViewById(R.id.memo_delete);
        backBtn = rootView.findViewById(R.id.memo_back);
        newMemo_fragment = new NewMemo_Fragment();
        mainDaily_fragment = new MainDaily_Fragment();
        TagName = ((MainActivity)getActivity()).newToMainmemo;
        args = new Bundle();

        // Adapter 생성 및 Adapter 지정.
        adapter = new MemoGridViewAdapter() ;
        //setAdapter(adapter) ;
        gridView.setAdapter(adapter);

        // database to listview -> show items
        doWhileCursorToArray();

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.showDeleteButton();
            }
        });

        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("status", "new");//new
                newMemo_fragment.setArguments(args);
                    ((MainActivity)getActivity()).addFragment(ft,newMemo_fragment, R.id.fragment_main, TagName);

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragment(ft, mainDaily_fragment,R.id.fragment_main);
                ((MainActivity)getActivity()).Daily_Clicked(true);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title, memo, date, time;
                int seq;

                seq = ((MemoGridViewItem)adapter.getItem(position)).getSeq();
                title = ((MemoGridViewItem)adapter.getItem(position)).getTitle();
                memo = ((MemoGridViewItem)adapter.getItem(position)).getMemo();
                date = ((MemoGridViewItem)adapter.getItem(position)).getDate();
                time = ((MemoGridViewItem)adapter.getItem(position)).getTime();

                args.putString("status", "edit");
                args.putInt("seq", seq);
                args.putString("title",title);
                args.putString("memo", memo);
                args.putString("date", date);
                args.putString("time", time);
                newMemo_fragment.setArguments(args);

                ((MainActivity)getActivity()).addFragment(ft,newMemo_fragment, R.id.fragment_main, TagName);


            }
        });
        return rootView;
    }


    /**
     * Get db data -> add to arraylist
     */
    private void doWhileCursorToArray(){

        String title, memo, date, time;
        long datetime;
        Date TransToDateTime;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("a HH:mm");

        int seq;
        mCursor = ((MainActivity)getActivity()).dbHelper.getReadableDatabase().rawQuery(
                "SELECT _ID AS _id," +
                " title, memo," +
                " strftime('%s', DATE) *1000 AS date" +
                " FROM MEMO", null);

        Log.e("MemoDatabase Get", "COUNT = " + mCursor.getCount());

        while (mCursor.moveToNext()) {
            seq = mCursor.getInt(0);
            title = mCursor.getString(1);
            memo = mCursor.getString(2);
            datetime = mCursor.getLong(3);
            TransToDateTime=new Date(datetime);
            date = dateFormat.format(TransToDateTime);
            time = timeFormat.format(TransToDateTime);

            adapter.addItem(seq, date,title, memo, time);
        }

        mCursor.close();
    }


}
