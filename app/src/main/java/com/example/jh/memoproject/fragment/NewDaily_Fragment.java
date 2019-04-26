package com.example.jh.memoproject.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.jh.memoproject.LinedEditText;
import com.example.jh.memoproject.MainActivity;
import com.example.jh.memoproject.R;


public class NewDaily_Fragment extends Fragment {

    private LinedEditText newdaily_memo;
    private TextView newdaily_year, newdaily_day, newdaily_save;
    private ImageButton newdaily_back;
    private String TagName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.daily_new, container, false);

        newdaily_memo = (LinedEditText) rootView.findViewById(R.id.newdaily_editmemo);
        newdaily_year = (TextView) rootView.findViewById(R.id.newdaily_year);
        newdaily_day = (TextView) rootView.findViewById(R.id.newdaily_day);
        newdaily_back = (ImageButton) rootView.findViewById(R.id.daily_back);
        newdaily_save = (TextView) rootView.findViewById(R.id.daily_save);

        TagName = ((MainActivity)getActivity()).newToMaindaily;

        newdaily_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).backFragment(TagName);
                Log.d("fragment_change", "new daily -> main(daily)");
            }
        });

        newdaily_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).backFragment("newdaily");
            }
        });

        return rootView;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

}
