package com.example.jh.memoproject.fragment;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jh.memoproject.CustomObject.CustomLineHeightSpan;
import com.example.jh.memoproject.CustomObject.CustomTypefaceSpan;
import com.example.jh.memoproject.DBHelper;
import com.example.jh.memoproject.MainActivity;
import com.example.jh.memoproject.OnSwipeTouchListener;
import com.example.jh.memoproject.R;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.Objects;

import static android.graphics.Typeface.BOLD;

public class NewMemo_Fragment extends Fragment implements View.OnClickListener {

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

    //font change widget
    protected ImageButton font_check;
    protected Button font_gulim, font_gungsuh, font_dotum, font_batang, font_malgun;

    //font size widget
    DiscreteSeekBar size_seekbar;
    ImageButton size_check;
    Button size_minus, size_plus;

    //fon row widget
    DiscreteSeekBar row_seekbar;
    ImageButton row_check;
    Button row_minus, row_plus;

    //font color widget
    protected ImageButton font_color_check;
    protected ImageView color1, color2, color3, color4, color5, color6, color7, color8, color9, color10, color11, color12, color13, color14;

    public static Spanned fromHtml(String source){
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            // noinspection deprecation
            return Html.fromHtml(source);
        }
        return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        /*
        TODO: edittext cursor 유지(layout에 touchevent 넣기)
        TODO: 행간격 설정 후 텍스트 라인 바뀌는 문제 발생
        TODO: setProgressbar need to edit
         */
        final View rootView = inflater.inflate(R.layout.memo_new, container, false);

        Newmemo_Back = rootView.findViewById(R.id.newmemo_back);
        Newmemo_Save = rootView.findViewById(R.id.newmemo_save);
        Newmemo_Title = rootView.findViewById(R.id.newmemo_title);
        Newmemo_Memo = rootView.findViewById(R.id.newmemo_memo);
        Newmemo_DateTime = rootView.findViewById(R.id.newmemo_datetime);

        //font(text) editor imagebutton(ib), linearlayout(ll), relativelayout(rl)
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

        //font widget id
        font_check = rootView.findViewById(R.id.font_check);
        font_gulim = rootView.findViewById(R.id.font_1);
        font_gungsuh = rootView.findViewById(R.id.font_2);
        font_dotum = rootView.findViewById(R.id.font_3);
        font_batang = rootView.findViewById(R.id.font_4);
        font_malgun = rootView.findViewById(R.id.font_5);

        //font size widget id
        size_seekbar = rootView.findViewById(R.id.font_size_seekbar);
        size_check = rootView.findViewById(R.id.font_size_check);
        size_minus = rootView.findViewById(R.id.font_size_minus);
        size_plus = rootView.findViewById(R.id.font_size_plus);

        //font row widget id
        row_seekbar = rootView.findViewById(R.id.font_row_seekbar);
        row_check = rootView.findViewById(R.id.font_row_check);
        row_minus = rootView.findViewById(R.id.font_row_minus);
        row_plus = rootView.findViewById(R.id.font_row_plus);

        //font_color widget id
        font_color_check = rootView.findViewById(R.id.font_color_check);
        color1 = rootView.findViewById(R.id.color1);
        color2 = rootView.findViewById(R.id.color2);
        color3 = rootView.findViewById(R.id.color3);
        color4 = rootView.findViewById(R.id.color4);
        color5 = rootView.findViewById(R.id.color5);
        color6 = rootView.findViewById(R.id.color6);
        color7 = rootView.findViewById(R.id.color7);
        color8 = rootView.findViewById(R.id.color8);
        color9 = rootView.findViewById(R.id.color9);
        color10 = rootView.findViewById(R.id.color10);
        color11 = rootView.findViewById(R.id.color11);
        color12 = rootView.findViewById(R.id.color12);
        color13 = rootView.findViewById(R.id.color13);
        color14 = rootView.findViewById(R.id.color14);

        TagName = ((MainActivity)getActivity()).newToMainmemo;
        dbHelper = ((MainActivity)getActivity()).dbHelper;

        Bundle argc = getArguments(); //get data from previous fragment
        status = argc.getString("status");

        if(argc!=null){
            if(status.equals("edit")) { // if edit mode, load data
                seq = argc.getInt("seq");
                Newmemo_Title.setText(argc.getString("title"));
                Newmemo_Memo.setText(argc.getString("memo"));
                Newmemo_Memo.setText(fromHtml(argc.getString("memo")),TextView.BufferType.SPANNABLE);
                Newmemo_DateTime.setText(argc.getString("date") + "\n" + argc.getString("time"));
                getsetSpans(Newmemo_Memo);
                getCustomFontStyle(seq, Newmemo_Memo);
            }else { //if new memo mode, just show layout
                Newmemo_DateTime.setVisibility(View.GONE);
            }
        }


        Newmemo_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).backFragment(TagName);
            }
        }); //go back

        Newmemo_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = Newmemo_Title.getText().toString();
                memo = Newmemo_Memo.getText().toString();

                if(status.equals("new")){
                    if(title.equals("")|| memo.equals("")){
                        Toast.makeText(getContext(),"write title or content", Toast.LENGTH_SHORT).show();
                    }else {
                        SpannableString sb = getsetSpans(Newmemo_Memo);
                        String htmlString = Html.toHtml(sb);
                        Log.i("SpannableString Spans", "htmlStirng: " + htmlString);
                        dbHelper.memo_insert(Newmemo_Title.getText().toString(), htmlString);
                        saveCustomFontStyle(Newmemo_Memo, "new");
                        ((MainActivity)getActivity()).backFragment(TagName);
                    }
                } else if(status.equals("edit")) {
                    if(title.equals("")|| memo.equals("")){
                        Toast.makeText(getContext(),"write title or content", Toast.LENGTH_SHORT).show();
                    }else{
                        title = Newmemo_Title.getText().toString();
                        SpannableString sb = getsetSpans(Newmemo_Memo);
                        String htmlString = Html.toHtml(sb);
                        Log.i("SpannableString Spans", "htmlStirng: " + htmlString);
                        ((MainActivity)getActivity()).dbHelper.memo_update(title, htmlString, seq);
                        saveCustomFontStyle(Newmemo_Memo, "edit");
                        ((MainActivity)getActivity()).backFragment(TagName);
                    }

                } //end else if (edit)
            }
        }); //save data

        //swipe to show text style editor
        Newmemo_Memo.setOnTouchListener(new OnSwipeTouchListener(getContext(), Newmemo_Memo, size_seekbar){

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

        //if click font style, show the font editor layout and hide total_style_ll
        font_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total_style_ll.setVisibility(View.GONE);
                font_rl.setVisibility(View.VISIBLE);
            }
        });

        //change font
        font_gulim.setOnClickListener(this);
        font_gungsuh.setOnClickListener(this);
        font_dotum.setOnClickListener(this);
        font_batang.setOnClickListener(this);
        font_malgun.setOnClickListener(this);

        //if click font_check button, show total_style_ll, hide font_rl
        font_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total_style_ll.setVisibility(View.VISIBLE);
                font_rl.setVisibility(View.GONE);
            }
        });



        size_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total_style_ll.setVisibility(View.GONE);
                size_rl.setVisibility(View.VISIBLE);
                //TODO: setProgressbar need to edit
                size_seekbar.setProgress(14);
            }
        });


        size_seekbar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            int start, end;
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
                start = Newmemo_Memo.getSelectionStart();
                end = Newmemo_Memo.getSelectionEnd();

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                getsetFontSize(Newmemo_Memo, seekBar);
                Newmemo_Memo.setSelection(start, end);
            }

        });

        //if click minus button, progress--
        size_minus.setOnClickListener(this);
        //if click plus button, progress++
        size_plus.setOnClickListener(this);

        //if check button click, show total_style_ll and hide size_rl
        size_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total_style_ll.setVisibility(View.VISIBLE);
                size_rl.setVisibility(View.GONE);

            }
        });

        row_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total_style_ll.setVisibility(View.GONE);
                row_rl.setVisibility(View.VISIBLE);
                //TODO: setProgressbar need to edit
                row_seekbar.setProgress(10);
            }
        });

        row_seekbar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            int start, end;
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
                start = Newmemo_Memo.getSelectionStart();
                end = Newmemo_Memo.getSelectionEnd();

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                getsetRowSize(Newmemo_Memo, seekBar);
                Newmemo_Memo.setSelection(start, end);
            }

        });

        //if click minus button, progress--
        row_minus.setOnClickListener(this);
        //if click plus button, progress++
        row_plus.setOnClickListener(this);

        //if check button click, show total_style_ll and hide row_rl
        row_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total_style_ll.setVisibility(View.VISIBLE);
                row_rl.setVisibility(View.GONE);
            }
        });

        bold_ib.setOnClickListener(this);

        highlight_ib.setOnClickListener(this);

        color_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total_style_ll.setVisibility(View.GONE);
                color_rl.setVisibility(View.VISIBLE);
            }
        });

        //color button click event listner
        color1.setOnClickListener(this);
        color2.setOnClickListener(this);
        color3.setOnClickListener(this);
        color4.setOnClickListener(this);
        color5.setOnClickListener(this);
        color6.setOnClickListener(this);
        color7.setOnClickListener(this);
        color8.setOnClickListener(this);
        color9.setOnClickListener(this);
        color10.setOnClickListener(this);
        color11.setOnClickListener(this);
        color12.setOnClickListener(this);
        color13.setOnClickListener(this);
        color14.setOnClickListener(this);

        //if check button click, show total_style_ll and hide color_rl
        font_color_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total_style_ll.setVisibility(View.VISIBLE);
                color_rl.setVisibility(View.GONE);
            }
        });

        return rootView;
    }

    public void pressedSeekbar(final DiscreteSeekBar seekBar){
        seekBar.setPressed(true);
        seekBar.refreshDrawableState();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                seekBar.setPressed(false);
                seekBar.refreshDrawableState();
            }
        },1000);
    }

    public SpannableString getsetSpans(EditText editor) {
        if (editor.getText().toString().equals("")) {
            return null;
        } else {

            CustomTypefaceSpan[] fonts = editor.getText().getSpans(0, editor.length(), CustomTypefaceSpan.class);
            AbsoluteSizeSpan[] font_sizes = editor.getText().getSpans(0, editor.length(), AbsoluteSizeSpan.class);
            StyleSpan[] stylespans = editor.getText().getSpans(0, editor.length(), StyleSpan.class);
            BackgroundColorSpan[] backgroundspans = editor.getText().getSpans(0, editor.length(), BackgroundColorSpan.class);
            CustomLineHeightSpan[] row_sizes = editor.getText().getSpans(0, editor.length(), CustomLineHeightSpan.class);

            SpannableString sb = new SpannableString(editor.getText());

            if(fonts.length>0){
                for (CustomTypefaceSpan font : fonts) {
                    if (font instanceof CustomTypefaceSpan) {
                        int start = editor.getText().getSpanStart(font);
                        int end = editor.getText().getSpanEnd(font);
                        int flag = editor.getText().getSpanFlags(font);
                        Log.i("SpannableString Spans", "Found Font at: " +
                                ", Start: " + start +
                                ", End: " + end +
                                ", Font: " + font.getFamily() +
                                ", Flag(s): " + flag);

                        sb.setSpan(font, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }

            if (font_sizes.length>0){
                for (AbsoluteSizeSpan span : font_sizes) {
                    if (span instanceof AbsoluteSizeSpan) {
                        int start = editor.getText().getSpanStart(span);
                        int end = editor.getText().getSpanEnd(span);
                        int flag = editor.getText().getSpanFlags(span);
                        Log.i("SpannableString Spans", "Found Font Size at: " +
                                ", Start: " + start +
                                ", End: " + end +
                                ", Size: " + span.getSize() +
                                ", Flag(s): " + flag);
                        sb.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }

            if (stylespans.length>0){
                for (StyleSpan span : stylespans) {
                    if (span instanceof StyleSpan) {
                        int start = editor.getText().getSpanStart(span);
                        int end = editor.getText().getSpanEnd(span);
                        int flag = editor.getText().getSpanFlags(span);
                        Log.i("SpannableString Spans", "Found StyleSpan at: " +
                                ", Start: " + start +
                                ", End: " + end +
                                ", Flag(s): " + flag);
                        sb.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }

            if (backgroundspans.length>0){
                for (BackgroundColorSpan span : backgroundspans) {
                    if (span instanceof BackgroundColorSpan) {
                        int start = editor.getText().getSpanStart(span);
                        int end = editor.getText().getSpanEnd(span);
                        int flag = editor.getText().getSpanFlags(span);
                        Log.i("SpannableString Spans", "Found Highlight at: " +
                                ", Start: " + start +
                                ", End: " + end +
                                ", Flag(s): " + flag);
                        sb.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }

            if (row_sizes.length>0){
                for (CustomLineHeightSpan span : row_sizes) {
                    if (span instanceof CustomLineHeightSpan) {
                        int start = editor.getText().getSpanStart(span);
                        int end = editor.getText().getSpanEnd(span);
                        int flag = editor.getText().getSpanFlags(span);
                        Log.i("SpannableString Spans", "Found Row Size at: " +
                                ", Start: " + start +
                                ", End: " + end +
                                ", Flag(s): " + flag);
                        sb.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }

            editor.setText(sb);
            return sb;
        } //end (Editor not null)
    }

    public void getsetFontSize(EditText editor, DiscreteSeekBar seekBar){
        int start = editor.getSelectionStart();
        int end = editor.getSelectionEnd();

        SpannableString sb = getsetSpans(editor);
        AbsoluteSizeSpan[] spans = sb.getSpans(start, end, AbsoluteSizeSpan.class);
        AbsoluteSizeSpan[] removerspans = sb.getSpans(0, sb.length(), AbsoluteSizeSpan.class);

        if (spans.length > 0) {
            //it has font-size
            for (AbsoluteSizeSpan span : spans) {
                for (AbsoluteSizeSpan remover : removerspans) {
                    if(start!=end){
                        if (start <= sb.getSpanStart(remover) && sb.getSpanEnd(remover) <= end) {
                            // selection > text(applied font-size)
                            sb.removeSpan(remover);
                            sb.setSpan(new AbsoluteSizeSpan(seekBar.getProgress(), true), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            editor.setText(sb);
                        } else if (sb.getSpanStart(remover) <= start && end <= sb.getSpanEnd(remover)) {
                            // selection <= text(applied font-size)
                            if (start > sb.getSpanStart(remover)) { //set first text
                                sb.setSpan(new AbsoluteSizeSpan(seekBar.getProgress(), true), sb.getSpanStart(remover), start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                            if (end < sb.getSpanEnd(remover)) { //set last text
                                sb.setSpan(new AbsoluteSizeSpan(seekBar.getProgress(), true), end, sb.getSpanEnd(remover), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                            sb.removeSpan(remover);
                            editor.setText(sb);
                        }
                    }
                }//for about remover
            } // for about span
        }else {
            //if no highlight effect
            //need to apply highlight
            sb.setSpan(new AbsoluteSizeSpan(seekBar.getProgress(), true), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Log.i("Font Style", "Font-Size applied: Success");
            editor.setText(sb);
        }
    }

    public void getsetRowSize(EditText editor, DiscreteSeekBar seekBar){
        int start = editor.getSelectionStart();
        int end = editor.getSelectionEnd();

        SpannableString sb = getsetSpans(editor);
        CustomLineHeightSpan[] spans = sb.getSpans(start, end, CustomLineHeightSpan.class);
        CustomLineHeightSpan[] removerspans = sb.getSpans(0, sb.length(), CustomLineHeightSpan.class);

        if (spans.length > 0) {
            //it has font-size
            for (CustomLineHeightSpan span : spans) {
                for (CustomLineHeightSpan remover : removerspans) {
                    if(start!=end){
                        if (start <= sb.getSpanStart(remover) && sb.getSpanEnd(remover) <= end) {
                            // selection > text(applied font-size)
                            sb.removeSpan(remover);
                            sb.setSpan(new CustomLineHeightSpan(seekBar.getProgress()), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            editor.setText(sb);
                        } else if (sb.getSpanStart(remover) <= start && end <= sb.getSpanEnd(remover)) {
                            // selection <= text(applied font-size)
                            if (start > sb.getSpanStart(remover)) { //set first text
                                sb.setSpan(new CustomLineHeightSpan(seekBar.getProgress()), sb.getSpanStart(remover), start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                            if (end < sb.getSpanEnd(remover)) { //set last text
                                sb.setSpan(new CustomLineHeightSpan(seekBar.getProgress()), end, sb.getSpanEnd(remover), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                            sb.removeSpan(remover);
                            editor.setText(sb);
                        }
                    }
                }//for about remover
            } // for about span
        }else {
            //if no highlight effect
            //need to apply highlight
            sb.setSpan(new CustomLineHeightSpan(seekBar.getProgress()), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Log.i("Font Style", "Font-Row applied: Success");
            editor.setText(sb);
        }
    }

    public void getCustomFontStyle(int id, EditText editor){

        Cursor mCursor;
        int start, end; // common parameter;
        String font; // only font
        Typeface getfont;
        int size; //font size or row

        SpannableString sb =getsetSpans(editor);

        //get font table values
        mCursor = ((MainActivity)getActivity()).dbHelper.getReadableDatabase().rawQuery(
                "SELECT FONT, START_INDEX , END_INDEX " +
                        " FROM FONT" +
                        " WHERE SEQ = " + id , null);

        Log.e("FONTDATABSE Get", "COUNT = " + mCursor.getCount());

        if (mCursor.getCount() > 0){
            while (mCursor.moveToNext()) {
                font = mCursor.getString(0);
                start = mCursor.getInt(1);
                end = mCursor.getInt(2);

                Log.e("FONTDATABSE Get", "Font = " + font + "start = " +  start + "start = " + end);

                switch (font){
                    case "gulim":
                        getfont = ResourcesCompat.getFont(Objects.requireNonNull(getContext()), R.font.gulim);
                        sb.setSpan(new CustomTypefaceSpan("gulim", getfont),start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        editor.setText(sb);
                        break;
                    case "gungsuh":
                        getfont = ResourcesCompat.getFont(Objects.requireNonNull(getContext()), R.font.gungsuh);
                        sb.setSpan(new CustomTypefaceSpan("gungsuh", getfont),start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        editor.setText(sb);
                        break;
                    case "dotum":
                        getfont = ResourcesCompat.getFont(Objects.requireNonNull(getContext()), R.font.dotum);
                        sb.setSpan(new CustomTypefaceSpan("dotum", getfont),start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        editor.setText(sb);
                        break;
                    case "batang":
                        getfont = ResourcesCompat.getFont(Objects.requireNonNull(getContext()), R.font.batang);
                        sb.setSpan(new CustomTypefaceSpan("batang", getfont),start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        editor.setText(sb);
                        break;
                    case "malgun":
                        getfont = ResourcesCompat.getFont(Objects.requireNonNull(getContext()), R.font.malgun);
                        sb.setSpan(new CustomTypefaceSpan("malgun", getfont),start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        editor.setText(sb);
                        break;
                    default:
                        break;
                } //get font
            }// finish get and set font style
        }


        //get font table values
        mCursor = ((MainActivity)getActivity()).dbHelper.getReadableDatabase().rawQuery(
                "SELECT SIZE, START_INDEX , END_INDEX " +
                        " FROM FONT_SIZE" +
                        " WHERE SEQ = " + id , null);

        Log.e("FONTSIZEDATABSE Get", "COUNT = " + mCursor.getCount());

        if (mCursor.getCount() > 0){
            while (mCursor.moveToNext()) {
                size = mCursor.getInt(0);
                start = mCursor.getInt(1);
                end = mCursor.getInt(2);

                sb.setSpan(new AbsoluteSizeSpan(size, true), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                Log.e("FONTSIZEDATABSE Get", "Size = " + size + ", start = " +  start + ", end = " + end);

                editor.setText(sb);
            } // end get and set font_size
        }

        //get row table values
        mCursor = ((MainActivity)getActivity()).dbHelper.getReadableDatabase().rawQuery(
                "SELECT SIZE, START_INDEX , END_INDEX " +
                        " FROM FONT_ROW" +
                        " WHERE SEQ = " + id , null);


        Log.e("FONTROWDATABSE Get", "COUNT = " + mCursor.getCount());

        if (mCursor.getCount() > 0){
            while (mCursor.moveToNext()) {
                size = mCursor.getInt(0);
                start = mCursor.getInt(1);
                end = mCursor.getInt(2);

                sb.setSpan(new CustomLineHeightSpan(size), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                editor.setText(sb);
            } // end get and set font_row
        }
    }

    public void saveCustomFontStyle(EditText editor, String mode){

        CustomTypefaceSpan[] fonts = editor.getText().getSpans(0, editor.length(), CustomTypefaceSpan.class);
        AbsoluteSizeSpan[] font_sizes = editor.getText().getSpans(0, editor.length(), AbsoluteSizeSpan.class);
        CustomLineHeightSpan[] row_sizes = editor.getText().getSpans(0, editor.length(), CustomLineHeightSpan.class);

        if(fonts.length>0){
            ((MainActivity)getActivity()).dbHelper.delete("FONT", seq);
            for (CustomTypefaceSpan font : fonts) {
                if (font instanceof CustomTypefaceSpan) {

                    String fontname = font.getFamily();
                    int start = editor.getText().getSpanStart(font);
                    int end = editor.getText().getSpanEnd(font);
                    Log.i("SpannableString Spans", "Found Font at: " +
                            ", Start: " + start +
                            ", End: " + end +
                            ", Font: " + font.getFamily());

                    if(mode.equals("edit")){
                        ((MainActivity)getActivity()).dbHelper.font_insert(seq, fontname, start, end);
                    }else {
                        int newseq = MakeNewSeq("FONT");
                        if(newseq>=0)
                            ((MainActivity)getActivity()).dbHelper.font_insert(newseq, fontname, start, end);
                        else
                            Log.e("Font Style", "Fail get font");
                    }
                }
            }
        }

        if (font_sizes.length>0){
            ((MainActivity)getActivity()).dbHelper.delete("FONT_SIZE", seq);
            for (AbsoluteSizeSpan span : font_sizes) {
                if (span instanceof AbsoluteSizeSpan) {
                    int size = span.getSize();
                    int start = editor.getText().getSpanStart(span);
                    int end = editor.getText().getSpanEnd(span);

                    Log.i("SpannableString Spans", "Found Font Size at: " +
                            ", Start: " + start +
                            ", End: " + end +
                            ", Size: " + span.getSize());

                    if(mode.equals("edit")){
                        ((MainActivity)getActivity()).dbHelper.font_size_insert(seq, size, start, end);
                    }else {
                        int newseq = MakeNewSeq("SIZE");
                        if(newseq>=0)
                            ((MainActivity)getActivity()).dbHelper.font_size_insert(newseq, size, start, end);
                        else
                            Log.e("Font Style", "Fail get font size");
                    }
                }
            }
        }

        if (row_sizes.length>0){
            ((MainActivity)getActivity()).dbHelper.delete("FONT_ROW", seq);
            for (CustomLineHeightSpan span : row_sizes) {
                if (span instanceof CustomLineHeightSpan) {
                    int size = span.getmSize();
                    int start = editor.getText().getSpanStart(span);
                    int end = editor.getText().getSpanEnd(span);
                    Log.i("SpannableString Spans", "Found Row Size at: " +
                            ", Start: " + start +
                            ", End: " + end +
                            ", Size: " + span.getmSize());

                    if(mode.equals("edit")){
                        ((MainActivity)getActivity()).dbHelper.font_row_insert(seq, size, start, end);
                    }else {
                        int newseq = MakeNewSeq("LINE_ROW");
                        if(newseq>=0)
                            ((MainActivity)getActivity()).dbHelper.font_row_insert(newseq, size, start, end);
                        else
                            Log.e("Font Style", "Fail get font row");
                    }
                }
            }
        }
    }

    public int MakeNewSeq(String table_name){

        int newseq = -1;

        Cursor mCursor = ((MainActivity)getActivity()).dbHelper.getReadableDatabase().rawQuery(
                "SELECT _ID AS _id" +
                        " FROM table_name", null);

        Log.e("MemoDatabase Get", "COUNT = " + mCursor.getCount());

        while (mCursor.moveToNext()) {
            newseq = mCursor.getInt(0);
        }

        return ++newseq;
    }

    //event for font and color change
    @Override
    public void onClick(View view) {

        //font change event
        if (view.getId() == R.id.font_1) {
            int start = Newmemo_Memo.getSelectionStart();
            int end = Newmemo_Memo.getSelectionEnd();
            Typeface font = ResourcesCompat.getFont(Objects.requireNonNull(getContext()), R.font.gulim);
            SpannableString sb = getsetSpans(Newmemo_Memo);

            sb.setSpan(new CustomTypefaceSpan("gulim", font), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Newmemo_Memo.setText(sb);
        } else if (view.getId() == R.id.font_2) {
            int start = Newmemo_Memo.getSelectionStart();
            int end = Newmemo_Memo.getSelectionEnd();
            Typeface font = ResourcesCompat.getFont(Objects.requireNonNull(getContext()), R.font.gungsuh);
            SpannableString sb = getsetSpans(Newmemo_Memo);

            sb.setSpan(new CustomTypefaceSpan("gungsuh", font), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Newmemo_Memo.setText(sb);
        } else if (view.getId() == R.id.font_3) {
            int start = Newmemo_Memo.getSelectionStart();
            int end = Newmemo_Memo.getSelectionEnd();

            Typeface font = ResourcesCompat.getFont(Objects.requireNonNull(getContext()), R.font.dotum);
            SpannableString sb = getsetSpans(Newmemo_Memo);

            sb.setSpan(new CustomTypefaceSpan("dotum", font), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Newmemo_Memo.setText(sb);
        } else if (view.getId() == R.id.font_4) {
            int start = Newmemo_Memo.getSelectionStart();
            int end = Newmemo_Memo.getSelectionEnd();

            Typeface font = ResourcesCompat.getFont(Objects.requireNonNull(getContext()), R.font.batang);
            SpannableString sb = getsetSpans(Newmemo_Memo);

            sb.setSpan(new CustomTypefaceSpan("batang", font), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Newmemo_Memo.setText(sb);
        } else if (view.getId() == R.id.font_5) {
            int start = Newmemo_Memo.getSelectionStart();
            int end = Newmemo_Memo.getSelectionEnd();

            Typeface font = ResourcesCompat.getFont(Objects.requireNonNull(getContext()), R.font.malgun);
            SpannableString sb = getsetSpans(Newmemo_Memo);

            sb.setSpan(new CustomTypefaceSpan("malgun", font), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Newmemo_Memo.setText(sb);
        }


        //change font size
        else if(view.getId() == R.id.font_size_minus){
            int progress = size_seekbar.getProgress();
            if(progress>=1){
                // set progress -1
                pressedSeekbar(size_seekbar);
                size_seekbar.setProgress(progress-1);
                Log.i("Font Style size Seekbar", "progress value changed:" +size_seekbar.getProgress());

                getsetFontSize(Newmemo_Memo, size_seekbar);

            }if(progress==0){
                //progress value can't be smaller than 0, because of minvalue is 0
                pressedSeekbar(size_seekbar);
            } //if progress==0
        }else if (view.getId() == R.id.font_size_plus){
            int progress = size_seekbar.getProgress();
            if(progress<=99){
                // set progress -1
                pressedSeekbar(size_seekbar);
                size_seekbar.setProgress(progress+1);
                Log.i("Font Style size Seekbar", "progress value changed:" +size_seekbar.getProgress());

                getsetFontSize(Newmemo_Memo, size_seekbar);

            }if(progress==100){
                //progress value can't be smaller than 0, because of minvalue is 0
                pressedSeekbar(size_seekbar);
            } //if progress==100
        }


        //change font row
        else if(view.getId() == R.id.font_row_minus){
            int progress = row_seekbar.getProgress();
            if(progress>=1){
                // set progress -1
                pressedSeekbar(row_seekbar);
                row_seekbar.setProgress(progress-1);
                Log.i("Font Style size Seekbar", "progress value changed:" +row_seekbar.getProgress());

                getsetRowSize(Newmemo_Memo, row_seekbar);

            }if(progress==0){
                //progress value can't be smaller than 0, because of minvalue is 0
                pressedSeekbar(row_seekbar);
            } //if progress==0
        }else if (view.getId() == R.id.font_row_plus){
            int progress = row_seekbar.getProgress();
            if(progress<=99){
                // set progress -1
                pressedSeekbar(row_seekbar);
                row_seekbar.setProgress(progress+1);
                Log.i("Font Style size Seekbar", "progress value changed:" +row_seekbar.getProgress());

                getsetRowSize(Newmemo_Memo, row_seekbar);

            }if(progress==100){
                //progress value can't be smaller than 0, because of minvalue is 0
                pressedSeekbar(size_seekbar);
            } //if progress==100
        }


        //change font bold
        else if (view.getId() == R.id.font_bold) {

            int start = Newmemo_Memo.getSelectionStart();
            int end = Newmemo_Memo.getSelectionEnd();

            SpannableString sb = getsetSpans(Newmemo_Memo);

            SpannableString substring = (SpannableString) sb.subSequence(start, end);
            StyleSpan[] spans = substring.getSpans(0, substring.length(), StyleSpan.class);
            StyleSpan[] removerspans = sb.getSpans(0, sb.length(), StyleSpan.class);

            if (spans.length > 0) {
                //it has bold style
                for (StyleSpan span : spans) {
                    if (span.getStyle() == Typeface.BOLD) {
                        for (StyleSpan remover : removerspans) {
                            if (start <= sb.getSpanStart(remover) && sb.getSpanEnd(remover) <= end) {
                                // selection >= bold
                                sb.removeSpan(remover);
                                Newmemo_Memo.setText(sb);
                            } else if (sb.getSpanStart(remover) <= start && end <= sb.getSpanEnd(remover)) {
                                // selection <= bold
                                if (start > sb.getSpanStart(remover)) { //set first text
                                    sb.setSpan(new StyleSpan(BOLD), sb.getSpanStart(remover), start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                }
                                if (end < sb.getSpanEnd(remover)) { //set last text
                                    sb.setSpan(new StyleSpan(BOLD), end, sb.getSpanEnd(remover), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                }
                                sb.removeSpan(remover);
                                Newmemo_Memo.setText(sb);
                            }
                        }// for about remover
                    } //if type == bold
                }// for about span
            } else {
                //if no bold style
                //plain text -> bold
                sb.setSpan(new StyleSpan(BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                Log.i("Font Style", "Bold applied: Success");
                Newmemo_Memo.setText(sb);
            }
        }


        //change highlight
        else if (view.getId() == R.id.font_highlight) {
            int start = Newmemo_Memo.getSelectionStart();
            int end = Newmemo_Memo.getSelectionEnd();

            SpannableString sb = getsetSpans(Newmemo_Memo);
            BackgroundColorSpan[] spans = sb.getSpans(start, end, BackgroundColorSpan.class);
            BackgroundColorSpan[] removerspans = sb.getSpans(0, sb.length(), BackgroundColorSpan.class);

            if (spans.length > 0) {
                //it has hightlight effect
                for (BackgroundColorSpan span : spans) {
                    for (BackgroundColorSpan remover : removerspans) {
                        if (start <= sb.getSpanStart(remover) && sb.getSpanEnd(remover) <= end) {
                            // selection >= highlight
                            sb.removeSpan(remover);
                            Newmemo_Memo.setText(sb);
                        } else if (sb.getSpanStart(remover) <= start && end <= sb.getSpanEnd(remover)) {
                            // selection <= highlight
                            if (start > sb.getSpanStart(remover)) { //set first text
                                sb.setSpan(new BackgroundColorSpan(Color.rgb(255, 186, 0)), sb.getSpanStart(remover), start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                            if (end < sb.getSpanEnd(remover)) { //set last text
                                sb.setSpan(new BackgroundColorSpan(Color.rgb(255, 186, 0)), end, sb.getSpanEnd(remover), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                            sb.removeSpan(remover);
                            Newmemo_Memo.setText(sb);
                        }
                    }//for about remover
                } // for about span
            } else {
                //if no highlight effect
                //need to apply highlight
                sb.setSpan(new BackgroundColorSpan(Color.rgb(255, 186, 0)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                Log.i("Font Style", "HighLight applied: Success");
                Newmemo_Memo.setText(sb);
            }
        }


        //change font color
        if (view.getId() == R.id.color1) {
            int start = Newmemo_Memo.getSelectionStart();
            int end = Newmemo_Memo.getSelectionEnd();
            SpannableString sb = getsetSpans(Newmemo_Memo);
            int color = getResources().getColor(R.color.color1);
            sb.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Log.i("Font Style", "HighLight applied: Success");
            Newmemo_Memo.setText(sb);
        } else if (view.getId() == R.id.color2) {
            int start = Newmemo_Memo.getSelectionStart();
            int end = Newmemo_Memo.getSelectionEnd();
            SpannableString sb = getsetSpans(Newmemo_Memo);
            int color = getResources().getColor(R.color.color2);
            sb.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Log.i("Font Style", "HighLight applied: Success");
            Newmemo_Memo.setText(sb);
        } else if (view.getId() == R.id.color3) {
            int start = Newmemo_Memo.getSelectionStart();
            int end = Newmemo_Memo.getSelectionEnd();
            SpannableString sb = getsetSpans(Newmemo_Memo);
            int color = getResources().getColor(R.color.color3);
            sb.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Log.i("Font Style", "HighLight applied: Success");
            Newmemo_Memo.setText(sb);
        } else if (view.getId() == R.id.color4) {
            int start = Newmemo_Memo.getSelectionStart();
            int end = Newmemo_Memo.getSelectionEnd();
            SpannableString sb = getsetSpans(Newmemo_Memo);
            int color = getResources().getColor(R.color.color4);
            sb.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Log.i("Font Style", "HighLight applied: Success");
            Newmemo_Memo.setText(sb);
        } else if (view.getId() == R.id.color5) {
            int start = Newmemo_Memo.getSelectionStart();
            int end = Newmemo_Memo.getSelectionEnd();
            SpannableString sb = getsetSpans(Newmemo_Memo);
            int color = getResources().getColor(R.color.color5);
            sb.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Log.i("Font Style", "HighLight applied: Success");
            Newmemo_Memo.setText(sb);
        } else if (view.getId() == R.id.color6) {
            int start = Newmemo_Memo.getSelectionStart();
            int end = Newmemo_Memo.getSelectionEnd();
            SpannableString sb = getsetSpans(Newmemo_Memo);
            int color = getResources().getColor(R.color.color6);
            sb.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Log.i("Font Style", "HighLight applied: Success");
            Newmemo_Memo.setText(sb);
        } else if (view.getId() == R.id.color7) {
            int start = Newmemo_Memo.getSelectionStart();
            int end = Newmemo_Memo.getSelectionEnd();
            SpannableString sb = getsetSpans(Newmemo_Memo);
            int color = getResources().getColor(R.color.color7);
            sb.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Log.i("Font Style", "HighLight applied: Success");
            Newmemo_Memo.setText(sb);
        } else if (view.getId() == R.id.color8) {
            int start = Newmemo_Memo.getSelectionStart();
            int end = Newmemo_Memo.getSelectionEnd();
            SpannableString sb = getsetSpans(Newmemo_Memo);
            int color = getResources().getColor(R.color.color8);
            sb.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Log.i("Font Style", "HighLight applied: Success");
            Newmemo_Memo.setText(sb);
        } else if (view.getId() == R.id.color9) {
            int start = Newmemo_Memo.getSelectionStart();
            int end = Newmemo_Memo.getSelectionEnd();
            SpannableString sb = getsetSpans(Newmemo_Memo);
            int color = getResources().getColor(R.color.color9);
            sb.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Log.i("Font Style", "HighLight applied: Success");
            Newmemo_Memo.setText(sb);
        } else if (view.getId() == R.id.color10) {
            int start = Newmemo_Memo.getSelectionStart();
            int end = Newmemo_Memo.getSelectionEnd();
            SpannableString sb = getsetSpans(Newmemo_Memo);
            int color = getResources().getColor(R.color.color10);
            sb.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Log.i("Font Style", "HighLight applied: Success");
            Newmemo_Memo.setText(sb);
        } else if (view.getId() == R.id.color11) {
            int start = Newmemo_Memo.getSelectionStart();
            int end = Newmemo_Memo.getSelectionEnd();
            SpannableString sb = getsetSpans(Newmemo_Memo);
            int color = getResources().getColor(R.color.color11);
            sb.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Log.i("Font Style", "HighLight applied: Success");
            Newmemo_Memo.setText(sb);
        } else if (view.getId() == R.id.color12) {
            int start = Newmemo_Memo.getSelectionStart();
            int end = Newmemo_Memo.getSelectionEnd();
            SpannableString sb = getsetSpans(Newmemo_Memo);
            int color = getResources().getColor(R.color.color12);
            sb.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Log.i("Font Style", "HighLight applied: Success");
            Newmemo_Memo.setText(sb);
        } else if (view.getId() == R.id.color13) {
            int start = Newmemo_Memo.getSelectionStart();
            int end = Newmemo_Memo.getSelectionEnd();
            SpannableString sb = getsetSpans(Newmemo_Memo);
            int color = getResources().getColor(R.color.color13);
            sb.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Log.i("Font Style", "HighLight applied: Success");
            Newmemo_Memo.setText(sb);
        } else if (view.getId() == R.id.color14) {
            int start = Newmemo_Memo.getSelectionStart();
            int end = Newmemo_Memo.getSelectionEnd();
            SpannableString sb = getsetSpans(Newmemo_Memo);
            int color = getResources().getColor(R.color.color14);
            sb.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Log.i("Font Style", "HighLight applied: Success");
            Newmemo_Memo.setText(sb);
        }


    }

}
