package com.example.jh.memoproject.List_Grid_Adapter;

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
    int pos;

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
        pos = position;
        Context context = parent.getContext();
        final DBHelper dbHelper = new DBHelper(context);
        int isholdiay = 0;

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.daily_listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView dayTextView = convertView.findViewById(R.id.daily_memo_day);
        TextView weekTextView = convertView.findViewById(R.id.daily_memo_week);
        TextView memoTextView = convertView.findViewById(R.id.daily_memo_content);
        TextView timeTextView = convertView.findViewById(R.id.daily_memo_time);
        ImageButton deleteBtn = convertView.findViewById(R.id.item_delete);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final DailyListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        dayTextView.setText(listViewItem.getDay());
        weekTextView.setText(listViewItem.getWeek());
        memoTextView.setText(listViewItem.getMemo());
        timeTextView.setText(listViewItem.getTime());
        isholdiay = listViewItem.getHoliday(); //if the day is holiday: true

        changeColor(context, isholdiay, dayTextView, weekTextView);

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
    public void addItem(int seq, String memo, String year_month, String day, String week, String time, int isholiday) {
        DailyListViewItem item = new DailyListViewItem();

        item.setSeq(seq);
        item.setMemo(memo);
        item.setYear_month(year_month);
        item.setDay(day);
        item.setWeek(week);
        item.setTime(time);
        item.setHoliday(isholiday);

        listViewItemList.add(item);
        notifyDataSetChanged();
    }

    public void showDeleteButton() {

        mClick = !mClick;
        notifyDataSetChanged();
    } //end showDeleteButton()

    public void clearItem(){
        listViewItemList.clear();
    }

    public void changeColor(Context context,int isholiday, TextView day, TextView week){
        if(isholiday == 1){ //holiday true
            day.setTextColor(context.getResources().getColor(R.color.memo_weekend, null));
            week.setTextColor(context.getResources().getColor(R.color.memo_weekend, null));
        }else{
            day.setTextColor(context.getResources().getColor(R.color.memo_week_date, null));
            week.setTextColor(context.getResources().getColor(R.color.memo_week, null));
        }
    } //change textColor

}
