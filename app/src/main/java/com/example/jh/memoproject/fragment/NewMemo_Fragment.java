package com.example.jh.memoproject.fragment;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

        // TODO: this is important thisng
        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                           float velocityY) {
                        final int SWIPE_MIN_DISTANCE = 120;
                        final int SWIPE_MAX_OFF_PATH = 250;
                        final int SWIPE_THRESHOLD_VELOCITY = 200;
                        try {
                            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                                return false;
                            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                Log.i("gesture", "Right to Left");
                            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                Log.i("gesture", "Left to Right");
                            }
                        } catch (Exception e) {
                            // nothing
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gesture.onTouchEvent(event);
                return true; // <-- this line made the difference
            }
        });
        return rootView;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }
}
