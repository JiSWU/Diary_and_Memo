package com.example.jh.memoproject.fragment;

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

import com.example.jh.memoproject.List_Grid_view.DailyListViewAdapter;
import com.example.jh.memoproject.List_Grid_view.DailyListViewItem;
import com.example.jh.memoproject.MainActivity;
import com.example.jh.memoproject.R;


public class MainDaily_Fragment extends ListFragment {

    private static String Tag_Name = "getNewDaily";
    private ListView listview;
    private DailyListViewAdapter adapter;
    private ImageButton newBtn, delBtn;
    protected NewDaily_Fragment newDaily_fragment;
    private String TagName;

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

        newDaily_fragment = new NewDaily_Fragment();
        TagName = ((MainActivity)getActivity()).newToMaindaily;

        // Adapter 생성 및 Adapter 지정.
        adapter = new DailyListViewAdapter() ;
        //setListAdapter(adapter) ;
        listview.setAdapter(adapter);

        // 첫 번째 아이템 추가.
        adapter.addItem("A", "MON", "Circlevvvvv", "time") ;
        // 두 번째 아이템 추가.
        adapter.addItem("AA", "THU","Circle", "Account Circle Black 36dp") ;
        // 세 번째 아이템 추가.
        adapter.addItem("$$", "THUR","Inaaaaaaaaaaaad", "Assignment Ind Black 36dp") ;




        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        String day = item.getDay() ;
        String month = item.getMonth() ;
        String memo = item.getMemo() ;
        String time = item.getTime();
    }

    public void addItem(String day, String month, String memo, String time) {
        adapter.addItem(day, month, memo, time) ;
    }


}
