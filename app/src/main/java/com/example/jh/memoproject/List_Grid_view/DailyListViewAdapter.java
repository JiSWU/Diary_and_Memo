package com.example.jh.memoproject.List_Grid_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.jh.memoproject.DBHelper;
import com.example.jh.memoproject.R;

import java.util.ArrayList;



public class DailyListViewAdapter extends BaseAdapter {

    private ArrayList<DailyListViewItem> listViewItemList = new ArrayList<DailyListViewItem>() ;
    private boolean mClick = false;
    private DBHelper dbHelper;

    public DailyListViewAdapter(){

    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        dbHelper = new DBHelper(context);

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.daily_listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView dayTextView = (TextView) convertView.findViewById(R.id.daily_memo_day) ;
        TextView weekTextView = (TextView) convertView.findViewById(R.id.daily_memo_week) ;
        TextView memoTextView = (TextView) convertView.findViewById(R.id.daily_memo_content) ;
        TextView timeTextView = (TextView) convertView.findViewById(R.id.daily_memo_time) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final DailyListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        dayTextView.setText(listViewItem.getDay());
        weekTextView.setText(listViewItem.getWeek());
        memoTextView.setText(listViewItem.getMemo());
        timeTextView.setText(listViewItem.getTime());

        ImageButton deleteBtn = (ImageButton)convertView.findViewById(R.id.item_delete);

        //if delete button click
        if(mClick){
            deleteBtn.setVisibility(View.VISIBLE);
        }else{
            deleteBtn.setVisibility(View.GONE);
        }

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listViewItemList.remove(position);
                dbHelper.delete("DIARY", listViewItem.getSeq());
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(int seq, String memo, String year_month, String day, String week, String time) {
        DailyListViewItem item = new DailyListViewItem();

        item.setSeq(seq);
        item.setMemo(memo);
        item.setYear_month(year_month);
        item.setDay(day);
        item.setWeek(week);
        item.setTime(time);

        listViewItemList.add(item);
    }

    public void showDeleteButton() {

        if (mClick)
            mClick = false;
        else
            mClick = true;

        notifyDataSetChanged();
    } //end showDeleteButton()
}
