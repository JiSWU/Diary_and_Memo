package com.example.jh.memoproject.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jh.memoproject.DBHelper;
import com.example.jh.memoproject.MainActivity;
import com.example.jh.memoproject.OnSwipeTouchListener;
import com.example.jh.memoproject.R;

public class NewMemo_Fragment extends Fragment {

    private ImageButton Newmemo_Back;
    private TextView Newmemo_Save, Newmemo_DateTime;
    private EditText Newmemo_Title, Newmemo_Memo;
    private String TagName, status, title, memo;
    private int seq;
    private DBHelper dbHelper;

    //string modifier
    protected LinearLayout total_style_ll;
    protected ImageButton font_ib, size_ib, bold_ib, highlight_ib, row_ib, color_ib;
    protected RelativeLayout font_rl, size_rl, row_rl, color_rl;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.memo_new, container, false);

        Newmemo_Back = rootView.findViewById(R.id.newmemo_back);
        Newmemo_Save = rootView.findViewById(R.id.newmemo_save);
        Newmemo_Title = rootView.findViewById(R.id.newmemo_title);
        Newmemo_Memo = rootView.findViewById(R.id.newmemo_memo);
        Newmemo_DateTime = rootView.findViewById(R.id.newmemo_datetime);

        //font(text) editor
        total_style_ll = rootView.findViewById(R.id.total_style);
        font_ib = rootView.findViewById(R.id.font_style);
        size_ib = rootView.findViewById(R.id.font_size);
        bold_ib = rootView.findViewById(R.id.font_bold);
        highlight_ib = rootView.findViewById(R.id.font_highlight);
        row_ib = rootView.findViewById(R.id.font_row);
        color_ib = rootView.findViewById(R.id.font_color);
        font_rl = rootView.findViewById(R.id.style_font);
        size_rl = rootView.findViewById(R.id.style_font_size);
        row_rl = rootView.findViewById(R.id.style_font_row);
        color_rl = rootView.findViewById(R.id.style_font_color);


        TagName = ((MainActivity)getActivity()).newToMainmemo;
        dbHelper = ((MainActivity)getActivity()).dbHelper;

        Bundle argc = getArguments();
        status = argc.getString("status");

        if(argc!=null){
            if(status.equals("edit")) {
                seq = argc.getInt("seq");
                Newmemo_Title.setText(argc.getString("title"));
                Newmemo_Memo.setText(argc.getString("memo"));
                Newmemo_DateTime.setText(argc.getString("date") + "\n" + argc.getString("time"));
            }else {
                Newmemo_DateTime.setVisibility(View.GONE);
            }
        }

        Newmemo_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).backFragment(TagName);
            }
        });

        Newmemo_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(status.equals("new")){
                    title = Newmemo_Title.getText().toString();
                    memo = Newmemo_Memo.getText().toString();
                    if(title.equals("")|| memo.equals("")){
                        Toast.makeText(getContext(),"write title or content", Toast.LENGTH_SHORT).show();
                    }else {
                        dbHelper.memo_insert(Newmemo_Title.getText().toString(), Newmemo_Memo.getText().toString());
                        ((MainActivity)getActivity()).backFragment(TagName);
                    }
                } else if(status.equals("edit")) {
                    title = Newmemo_Title.getText().toString();
                    memo = Newmemo_Memo.getText().toString();
                    ((MainActivity)getActivity()).dbHelper.memo_update(title, memo, seq);
                    ((MainActivity)getActivity()).backFragment(TagName);
                }
            }
        });

        //swipe to show text editor
        Newmemo_Memo.setOnTouchListener(new OnSwipeTouchListener(getContext()){
            @Override
            public void onSwipeTop() {
                total_style_ll.setVisibility(View.VISIBLE);
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                super.onSwipeTop();
            }

            @Override
            public void onSwipeBottom() {
                super.onSwipeBottom();
                total_style_ll.setVisibility(View.GONE);
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            }

        });


        font_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total_style_ll.setVisibility(View.GONE);
                font_rl.setVisibility(View.VISIBLE);
            }
        });

        size_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total_style_ll.setVisibility(View.GONE);
                size_rl.setVisibility(View.VISIBLE);
            }
        });

        row_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total_style_ll.setVisibility(View.GONE);
                row_rl.setVisibility(View.VISIBLE);
            }
        });

        color_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total_style_ll.setVisibility(View.GONE);
                color_rl.setVisibility(View.VISIBLE);
            }
        });

        return rootView;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }
}
