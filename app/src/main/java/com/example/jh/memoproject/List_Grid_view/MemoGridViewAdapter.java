package com.example.jh.memoproject.List_Grid_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jh.memoproject.DBHelper;
import com.example.jh.memoproject.MainActivity;
import com.example.jh.memoproject.R;

import java.util.ArrayList;

public class MemoGridViewAdapter extends BaseAdapter {

    private ArrayList<MemoGridViewItem> gridViewItemList = new ArrayList<MemoGridViewItem>();
    private boolean mClick = false;
    private DBHelper dbHelper;

    public MemoGridViewAdapter(){


    }

    @Override
    public int getCount() {
        return gridViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return gridViewItemList.get(position);
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
            convertView = inflater.inflate(R.layout.memo_gridview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView dateTextView = (TextView) convertView.findViewById(R.id.gridview_date) ;
        TextView titleTextView = (TextView) convertView.findViewById(R.id.gridview_title) ;
        TextView memoTextView = (TextView) convertView.findViewById(R.id.gridview_memo) ;
        TextView timeTextView = (TextView) convertView.findViewById(R.id.gridview_time) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final MemoGridViewItem gridViewItem = gridViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        dateTextView.setText(gridViewItem.getDate());
        titleTextView.setText(gridViewItem.getTitle());
        memoTextView.setText(gridViewItem.getMemo());
        timeTextView.setText(gridViewItem.getTime());

        ImageButton deleteBtn = (ImageButton)convertView.findViewById(R.id.gridview_delete);

        //if delete button click
        if(mClick){
            deleteBtn.setVisibility(View.VISIBLE);

        }else{
            deleteBtn.setVisibility(View.GONE);
        }

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridViewItemList.remove(position);
                dbHelper.delete("MEMO", gridViewItem.getSeq());
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(int seq, String date, String title, String memo, String time) {
        MemoGridViewItem item = new MemoGridViewItem();
        item.setSeq(seq);
        item.setDate(date);
        item.setTitle(title);
        item.setMemo(memo);
        item.setTime(time);

        gridViewItemList.add(item);
    }


    public void showDeleteButton() {

        if (mClick)
            mClick = false;
        else
            mClick = true;

        notifyDataSetChanged();
    } //end showDeleteButton()

}
