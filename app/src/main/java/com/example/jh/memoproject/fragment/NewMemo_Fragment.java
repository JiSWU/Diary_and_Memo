package com.example.jh.memoproject.fragment;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class NewMemo_Fragment extends Fragment {

    private ImageButton Newmemo_Back;
    private TextView Newmemo_Save, Newmemo_DateTime;
    private EditText Newmemo_Title, Newmemo_Memo;
    private String TagName, status, title, memo;
    private int seq;
    private DBHelper dbHelper;

    //string modifier
    protected LinearLayout total_style_ll;
    protected ImageButton font_ib, size_ib, bold_ib, highlight_ib, row_ib, color_ib; //6 selection button in total_style_ll
    protected RelativeLayout font_rl, size_rl, row_rl, color_rl; //font bold and highlight don't need another layout

    StyleSpan[] mSpans;

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
            if(status.equals("edit")) { // if edit mode
                seq = argc.getInt("seq");
                Newmemo_Title.setText(argc.getString("title"));
                Newmemo_Memo.setText(argc.getString("memo"));
                Newmemo_DateTime.setText(argc.getString("date") + "\n" + argc.getString("time"));
                getsetSpans(mSpans, Newmemo_Memo,"GetDB");
            }else { //if new memo mode
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
                        saveSpan("new");
                        ((MainActivity)getActivity()).backFragment(TagName);
                    }
                } else if(status.equals("edit")) {
                    title = Newmemo_Title.getText().toString();
                    memo = Newmemo_Memo.getText().toString();
                    ((MainActivity)getActivity()).dbHelper.memo_update(title, memo, seq);
                    saveSpan("edit");
                    ((MainActivity)getActivity()).backFragment(TagName);



                }
            }
        });



        //swipe to show text style editor
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

        //TODO; retry.it doesn't work
        /*
        //if click back button, show 'the total_style_ll' again.
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK) {
                    if (size_rl.getVisibility() == View.VISIBLE || row_rl.getVisibility() == View.VISIBLE || color_rl.getVisibility() == View.VISIBLE) {
                        size_rl.setVisibility(View.GONE);
                        row_rl.setVisibility(View.GONE);
                        color_rl.setVisibility(View.GONE);
                        total_style_ll.setVisibility(View.VISIBLE);
                        return true;
                    }
                    return false;
                } else {
                    return false;
                }
            }
        });*/

        //if click font style, show the font editor layout and hide total_style_ll
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

        bold_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int start = Newmemo_Memo.getSelectionStart();
                int end = Newmemo_Memo.getSelectionEnd();
                int i=0;

                if(mSpans!=null){
                    for(i=0; i<mSpans.length; i++){
                        mSpans[i] = null;
                    }
                }

                SpannableString sb = getsetSpans(mSpans, Newmemo_Memo, "NotDB");

                Log.i("Font Style", "Bold sb string: " + sb);

                int span = sb.getSpanFlags(StyleSpan.class);
                Log.i("Font Style", "span: " + span);
                if(span!=0){

                }else{
                    //need to apply bold
                    sb.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Log.i("Font Style", "Bold applied: Success");
                    Newmemo_Memo.setText(sb);
                }
            }
        });

        highlight_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String original = Newmemo_Memo.getText().toString();
                int start = Newmemo_Memo.getSelectionStart();
                int end = Newmemo_Memo.getSelectionEnd();

                SpannableStringBuilder sb = new SpannableStringBuilder();
                sb.append(original);
                Log.i("Font Style", "HighLight sb string: " + sb);

                //need to apply highlight
                sb.setSpan(new BackgroundColorSpan(Color.RED), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                Log.i("Font Style", "HighLight applied: Success");
                Newmemo_Memo.setText(sb);
            }
        });

        return rootView;
    }


//    public static Spanned fromHtml(String source){
//        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
//            // noinspection deprecation
//            return Html.fromHtml(source);
//        }
//        return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
//    }

    public SpannableString getsetSpans(StyleSpan[] spans, EditText editor, String mode) {
        if (editor.getText().toString().equals("")) {
            return null;
        } else {

            if(mode.equals("NotDB")){
                spans = editor.getText().getSpans(0, editor.length(), StyleSpan.class);
            }else if(mode.equals("GetDB")){
                Cursor mCursor = ((MainActivity)getActivity()).dbHelper.getReadableDatabase().rawQuery(
                        "SELECT STYLE " +
                                " FROM STYLE " +
                                "WHERE SEQ = " + seq, null);
                if(mCursor.getCount()==0){
                    spans = editor.getText().getSpans(0, editor.length(), StyleSpan.class);
                }else{
                    //get spans values from db
                    byte[] rawFontStyle;
                    int index = 0;
                    while(mCursor.moveToNext()){
                        rawFontStyle = mCursor.getBlob(1);
                        spans[index] = read(rawFontStyle);
                        index++;
                    }
                }
            }

            SpannableString sb = new SpannableString(editor.getText());

            if (spans.length>0){
                for (StyleSpan span : spans) {
                    if (span instanceof StyleSpan) {
                        int start = editor.getText().getSpanStart(span);
                        int end = editor.getText().getSpanEnd(span);
                        int flag = editor.getText().getSpanFlags(span);
                        Log.i("SpannableString Spans", "Found StyleSpan at:\n" +
                                "Start: " + start +
                                "\n End: " + end +
                                "\n Flag(s): " + flag);
                        sb.setSpan(span, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        editor.setText(sb);
                    }
                }
            }
            return sb;
        } //end (Editor not null)
    }

    public void saveSpan(String mode) {
        StyleSpan[] spans = Newmemo_Memo.getText().getSpans(0, Newmemo_Memo.length(), StyleSpan.class); //getspans

        if (spans.length>0){ //if there are some Spaanable
            //String result = ("" + Arrays.asList(spans)).
                    //replaceAll("(^.|.$)", "  ").replace(", ", "  , " );
            for(StyleSpan span : spans){
                byte [] rawspans = makebyte(span);

                if(mode.equals("new")){
                    int id = 0;
                    Cursor mCursor = ((MainActivity)getActivity()).dbHelper.getReadableDatabase().rawQuery(
                            "SELECT _ID AS _id" +
                                    " FROM MEMO", null);
                    Log.i("Select_Style", "count: " + mCursor.getCount());
                    while(mCursor.moveToNext()){
                        id = mCursor.getInt(0);
                    }
                    ((MainActivity)getActivity()).dbHelper.style_insert(id, rawspans);
                }else if(mode.equals("edit")){

                    Cursor mCursor = ((MainActivity)getActivity()).dbHelper.getReadableDatabase().rawQuery(
                            "SELECT SEQ " +
                                    " FROM STYLE " +
                                    "WHERE SEQ = " + seq, null);
                    Log.i("Select_Style", "count: " + mCursor.getCount());
                    if (mCursor.getCount() == 0) {
                        ((MainActivity)getActivity()).dbHelper.style_update(seq, rawspans);
                    }else {
                        ((MainActivity) getActivity()).dbHelper.style_insert(seq, rawspans);
                    }
                }
            }
        }
    }
    public byte[] makebyte(StyleSpan modeldata) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(modeldata);
            byte[] rawspanvalue = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(rawspanvalue);
            return rawspanvalue;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public StyleSpan read(byte[] data) {
        try {
            ByteArrayInputStream baip = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(baip);
            StyleSpan dataobj = (StyleSpan) ois.readObject();
            return dataobj;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}
