package com.example.jh.memoproject.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
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

import com.example.jh.memoproject.CustomTypefaceSpan;
import com.example.jh.memoproject.DBHelper;
import com.example.jh.memoproject.MainActivity;
import com.example.jh.memoproject.OnSwipeTouchListener;
import com.example.jh.memoproject.R;

import java.util.Objects;

import static android.graphics.Typeface.BOLD;
import static java.sql.Types.NULL;

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

        View rootView = inflater.inflate(R.layout.memo_new, container, false);

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
                Newmemo_Memo.setText(fromHtml(argc.getString("memo")));
                Newmemo_DateTime.setText(argc.getString("date") + "\n" + argc.getString("time"));
                getsetSpans(Newmemo_Memo);
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

                if(status.equals("new")){
                    title = Newmemo_Title.getText().toString();
                    memo = Newmemo_Memo.getText().toString();
                    if(title.equals(NULL)|| memo.equals(NULL)){
                        Toast.makeText(getContext(),"write title or content", Toast.LENGTH_SHORT).show();
                    }else {
                        SpannableString sb = getsetSpans(Newmemo_Memo);
                        String htmlString = Html.toHtml(sb);
                        dbHelper.memo_insert(Newmemo_Title.getText().toString(), htmlString);
                        ((MainActivity)getActivity()).backFragment(TagName);
                    }
                } else if(status.equals("edit")) {
                    title = Newmemo_Title.getText().toString();
                    SpannableString sb = getsetSpans(Newmemo_Memo);
                    String htmlString = Html.toHtml(sb);
                    ((MainActivity)getActivity()).dbHelper.memo_update(title, htmlString, seq);
                    ((MainActivity)getActivity()).backFragment(TagName);
                } //end else if (edit)
            }
        }); //save data

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
            }
        });

        row_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total_style_ll.setVisibility(View.GONE);
                row_rl.setVisibility(View.VISIBLE);
            }
        });

        bold_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int start = Newmemo_Memo.getSelectionStart();
                int end = Newmemo_Memo.getSelectionEnd();

                SpannableString sb = getsetSpans(Newmemo_Memo);

                SpannableString substring = (SpannableString) sb.subSequence(start, end);
                StyleSpan[] spans = substring.getSpans(0, substring.length(), StyleSpan.class);
                StyleSpan[] removerspans = sb.getSpans(0, sb.length(), StyleSpan.class);

                if(spans.length>0){
                    //it has bold style
                    for(StyleSpan span : spans){
                        if(span.getStyle() == Typeface.BOLD){
                            for(StyleSpan remover : removerspans){
                                if(start <= sb.getSpanStart(remover) && sb.getSpanEnd(remover)<=end){
                                    // selection >= bold
                                    sb.removeSpan(remover);
                                    Newmemo_Memo.setText(sb);
                                } else if(sb.getSpanStart(remover) <= start && end <=sb.getSpanEnd(remover)){
                                    // selection <= bold
                                    if(start > sb.getSpanStart(remover)){ //set first text
                                        sb.setSpan(new StyleSpan(BOLD),  sb.getSpanStart(remover), start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    }
                                    if(end < sb.getSpanEnd(remover)){ //set last text
                                        sb.setSpan(new StyleSpan(BOLD), end,  sb.getSpanEnd(remover), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    }
                                    sb.removeSpan(remover);
                                    Newmemo_Memo.setText(sb);
                                }
                            }// for about remover
                        } //if type == bold
                    }// for about span
                }else{
                    //if no bold style
                    //plain text -> bold
                    sb.setSpan(new StyleSpan(BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Log.i("Font Style", "Bold applied: Success");
                    Newmemo_Memo.setText(sb);
                }
            }
        });

        highlight_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start = Newmemo_Memo.getSelectionStart();
                int end = Newmemo_Memo.getSelectionEnd();

                SpannableString sb = getsetSpans(Newmemo_Memo);
                SpannableString substring = (SpannableString) sb.subSequence(start, end);
                BackgroundColorSpan[] spans = sb.getSpans(start, end, BackgroundColorSpan.class);
                BackgroundColorSpan[] removerspans = sb.getSpans(0, sb.length(), BackgroundColorSpan.class);

                if(spans.length>0){
                    //it has hightlight effect
                    for(BackgroundColorSpan span : spans){
                        for(BackgroundColorSpan remover : removerspans){
                           if(start <= sb.getSpanStart(remover) && sb.getSpanEnd(remover)<=end){
                               // selection >= highlight
                               sb.removeSpan(remover);
                               Newmemo_Memo.setText(sb);
                           } else if(sb.getSpanStart(remover) <= start && end <=sb.getSpanEnd(remover)){
                               // selection <= highlight
                               if(start > sb.getSpanStart(remover)){ //set first text
                                   sb.setSpan(new BackgroundColorSpan(Color.rgb(255, 186, 0)),  sb.getSpanStart(remover), start, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                               }
                               if(end < sb.getSpanEnd(remover)){ //set last text
                                   sb.setSpan(new BackgroundColorSpan(Color.rgb(255, 186, 0)), end,  sb.getSpanEnd(remover), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                               }
                               sb.removeSpan(remover);
                               Newmemo_Memo.setText(sb);
                           }
                        }//for about remover
                    } // for about span
                }else {
                    //if no highlight effect
                    //need to apply highlight
                    sb.setSpan(new BackgroundColorSpan(Color.rgb(255, 186, 0)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Log.i("Font Style", "HighLight applied: Success");
                    Newmemo_Memo.setText(sb);
                }
            }
        });

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

    public SpannableString getsetSpans(EditText editor) {
        if (editor.getText().toString().equals("")) {
            return null;
        } else {

            StyleSpan[] boldspans = editor.getText().getSpans(0, editor.length(), StyleSpan.class);
            BackgroundColorSpan[] backgroundspans = editor.getText().getSpans(0, editor.length(), BackgroundColorSpan.class);
            //CustomTypefaceSpan[] typefaces = editor.getText().getSpans(0, editor.length(), CustomTypefaceSpan.class);
            TypefaceSpan[] typefaces = editor.getText().getSpans(0, editor.length(), TypefaceSpan.class);
            SpannableString sb = new SpannableString(editor.getText());

            if (boldspans.length>0){
                for (StyleSpan span : boldspans) {
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
                        Log.i("SpannableString Spans", "Found StyleSpan at: " +
                                ", Start: " + start +
                                ", End: " + end +
                                ", Flag(s): " + flag);
                        sb.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                       // sb = CustomTypefaceSpan.setCustomFontTypeSpan(getContext(), editor.getText().toString(), start, end, )
                    }
                }
            }

            if(typefaces.length>0){
                for (TypefaceSpan typeface : typefaces) {
                    if (typeface instanceof TypefaceSpan) {
                        int start = editor.getText().getSpanStart(typeface);
                        int end = editor.getText().getSpanEnd(typeface);
                        int flag = editor.getText().getSpanFlags(typeface);

                        Log.i("SpannableString Spans", "Found typeface at: " +
                                ", Start: " + start +
                                ", End: " + end +
                                ", Flag(s): " + flag);

                        sb.setSpan(typeface, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
            editor.setText(sb);
            return sb;
        } //end (Editor not null)
    }



    //event for font and color change
    @Override
    public void onClick(View view) {
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

        else if(view.getId() == R.id.font_1){
            int start = Newmemo_Memo.getSelectionStart();
            int end = Newmemo_Memo.getSelectionEnd();
            Typeface font = Typeface.createFromAsset(getContext().getAssets(), "gulim.ttf");
            TypefaceSpan typefaceSpan = new CustomTypefaceSpan("", font);
            SpannableString sb = getsetSpans(Newmemo_Memo);
            sb.setSpan(typefaceSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Newmemo_Memo.setText(sb);
        }else if(view.getId() == R.id. font_2){
            int start = Newmemo_Memo.getSelectionStart();
            int end = Newmemo_Memo.getSelectionEnd();
            Typeface font = ResourcesCompat.getFont(Objects.requireNonNull(getContext()), R.font.gungsuh);
            SpannableString sb = getsetSpans(Newmemo_Memo);

            sb.setSpan (new CustomTypefaceSpan("",font), start, end,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Newmemo_Memo.setText(sb);
        }else if(view.getId() == R.id. font_3){
            int start = Newmemo_Memo.getSelectionStart();
            int end = Newmemo_Memo.getSelectionEnd();

            Typeface font = ResourcesCompat.getFont(Objects.requireNonNull(getContext()), R.font.dotum);
            SpannableString sb = getsetSpans(Newmemo_Memo);

            sb.setSpan (new CustomTypefaceSpan("", font), start, end,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Newmemo_Memo.setText(sb);
        }else if(view.getId() == R.id. font_4){
            int start = Newmemo_Memo.getSelectionStart();
            int end = Newmemo_Memo.getSelectionEnd();

            Typeface font = ResourcesCompat.getFont(Objects.requireNonNull(getContext()), R.font.batang);
            SpannableString sb = getsetSpans(Newmemo_Memo);

            sb.setSpan (new CustomTypefaceSpan("", font), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Newmemo_Memo.setText(sb);
        }else if(view.getId() == R.id. font_5){
            int start = Newmemo_Memo.getSelectionStart();
            int end = Newmemo_Memo.getSelectionEnd();

            Typeface font = ResourcesCompat.getFont(Objects.requireNonNull(getContext()), R.font.malgun);
            SpannableString sb = getsetSpans(Newmemo_Memo);

            sb.setSpan(new CustomTypefaceSpan("", font), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Newmemo_Memo.setText(sb);
        }
    }
}
