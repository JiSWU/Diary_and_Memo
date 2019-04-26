package com.example.jh.memoproject.fragment;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jh.memoproject.DBHelper;
import com.example.jh.memoproject.MainActivity;
import com.example.jh.memoproject.R;

import org.w3c.dom.Text;

public class NewMemo_Fragment extends Fragment {

    private ImageButton Newmemo_Back;
    private TextView Newmemo_Save, Newmemo_DateTime;
    private EditText Newmemo_Title, Newmemo_Memo;
    private String TagName, status, title, memo;
    private int seq;
    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.memo_new, container, false);

        Newmemo_Back = (ImageButton) rootView.findViewById(R.id.newmemo_back);
        Newmemo_Save = (TextView) rootView.findViewById(R.id.newmemo_save);
        Newmemo_Title = (EditText) rootView.findViewById(R.id.newmemo_title);
        Newmemo_Memo = (EditText) rootView.findViewById(R.id.newmemo_memo);
        Newmemo_DateTime = (TextView) rootView.findViewById(R.id.newmemo_datetime);

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


        return rootView;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }
}
