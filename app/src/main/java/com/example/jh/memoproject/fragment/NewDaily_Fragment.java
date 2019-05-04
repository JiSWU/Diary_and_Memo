package com.example.jh.memoproject.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jh.memoproject.DBHelper;
import com.example.jh.memoproject.LinedEditText;
import com.example.jh.memoproject.MainActivity;
import com.example.jh.memoproject.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class NewDaily_Fragment extends Fragment {

    private LinedEditText newdaily_memo;
    private TextView newdaily_year, newdaily_day, newdaily_save, newdaily_week;
    private ImageButton newdaily_back;
    private String TagName, status;

    //for database
    private DBHelper dbHelper;
    protected int seq, holiday;
    private String memo, year_month, day, week, time;
    //for date picker
    private int mYear, mMonth, mDay;
    private String mweek;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            UpdateNow(); //update textview value
        }
    };

    public void mOnClick(View v){
        if(v.getId() == R.id.newdaily_year || v.getId() == R.id.newdaily_day || v.getId() == R.id.newdaily_week) {
            new DatePickerDialog(getActivity(), mDateSetListener, mYear,mMonth, mDay).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.daily_new, container, false);

        newdaily_memo = rootView.findViewById(R.id.newdaily_editmemo);
        newdaily_year = rootView.findViewById(R.id.newdaily_year);
        newdaily_day = rootView.findViewById(R.id.newdaily_day);
        newdaily_week = rootView.findViewById(R.id.newdaily_week);
        newdaily_back = rootView.findViewById(R.id.daily_back);
        newdaily_save = rootView.findViewById(R.id.daily_save);

        TagName = ((MainActivity)getActivity()).newToMaindaily;
        dbHelper = ((MainActivity)getActivity()).dbHelper;

        Bundle recvmsg = getArguments();

        status = recvmsg.getString("status"); //edit or new

        if(recvmsg!=null){
            int state;
            if(status.matches("edit")) {
                seq = recvmsg.getInt("seq");
                newdaily_year.setText(recvmsg.getString("year_month"));
                newdaily_day.setText(recvmsg.getString("day"));
                newdaily_week.setText(recvmsg.getString("week"));
                newdaily_memo.setText(recvmsg.getString("memo"));
                state = recvmsg.getInt("holiday");
                if(state==1){
                    newdaily_day.setTextColor(getContext().getResources().getColorStateList(R.color.memo_weekend, null));
                    newdaily_week.setTextColor(getContext().getResources().getColorStateList(R.color.memo_weekend, null));
                }else {
                    newdaily_day.setTextColor(getContext().getResources().getColor(R.color.memo_week, null));
                    newdaily_week.setTextColor(getContext().getResources().getColor(R.color.memo_week, null));
                }

            }else if(status.equals("new")){
                mYear = recvmsg.getInt("picker_mYear");
                mMonth = recvmsg.getInt("picker_mMonth");
                mDay = recvmsg.getInt("picker_mDay");
                UpdateNow();
            }
        } //첫 화면 초기 출력




        newdaily_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClick(v);
            }
        });

        newdaily_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClick(v);
            }
        });

        newdaily_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClick(v);
            }
        });

        newdaily_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memo = newdaily_memo.getText().toString();
                year_month = newdaily_year.getText().toString();
                day = newdaily_day.getText().toString();
                week = newdaily_week.getText().toString();
                time = new SimpleDateFormat("a HH:mm").format(new Date());

                final StringBuilder date = new StringBuilder();
                date.append(year_month.replaceAll("[^0-9]", ""));

                if(day.length()==1){
                    date.append("0");
                    date.append(day);
                }else
                    date.append(day);

                if(isNetworkAvailable(getContext())){
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            holiday = getHolidayAPI(date.substring(0,4), date.substring(4,6),date.toString());
                            Log.d("holiday", "this value is holiday: " + date.substring(0,4) + ", " + date.substring(4,6) +", " + date.toString());

                            if(status.equals("new")){
                                if(memo.equals("")){
                                    getActivity().runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(getContext(),"write content", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else {
                                    dbHelper.daily_insert(memo, year_month, day, week, time, holiday);
                                    ((MainActivity)getActivity()).backFragment(TagName);
                                    Log.d("fragment_change", "new daily -> main(daily)");
                                }
                            } else if(status.equals("edit")) {
                                dbHelper.daily_update(memo, year_month, day, week, time, seq, holiday);
                                ((MainActivity)getActivity()).backFragment(TagName);
                                Log.d("fragment_change", "new daily -> main(daily)");

                                Log.d("holiday", "this value is holiday: "+holiday);
                            }
                        }
                    }.start();
                }else{
                    if(status.equals("new")){
                        if(memo.equals("")){
                            Toast.makeText(getContext(),"write content", Toast.LENGTH_SHORT).show();
                        }else {
                            dbHelper.daily_insert(memo, year_month, day, week, time, holiday);
                            ((MainActivity)getActivity()).backFragment(TagName);
                            Log.d("fragment_change", "new daily -> main(daily)");
                        }
                    } else if(status.equals("edit")) {
                        dbHelper.daily_update(memo, year_month, day, week, time, seq, holiday);
                        ((MainActivity)getActivity()).backFragment(TagName);
                        Log.d("fragment_change", "new daily -> main(daily)");

                        Log.d("holiday", "this value is holiday: "+holiday);
                    }
                }

           }
        });

        newdaily_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).backFragment(TagName);
                Log.d("fragment_change", "new daily -> main(daily)");
            }
        });
        
        return rootView;
    }

    public void UpdateNow(){
        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        Date d = new Date(mYear, mMonth, mDay);
        mweek = sdf.format(d);

        newdaily_year.setText(String.format("%d년 %02d월",mYear, mMonth+1));
        newdaily_day.setText(String.format("%d",mDay));
        newdaily_week.setText(mweek);
    }

    public boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    } //check network connect state

    public int getHolidayAPI(String year, String month, String date){
        try{
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=gajrpchqjDXZyXfBUDFZFSsC7l8o6ycd0SEKim98RHYFvK3J%2BiMu5YNNzDS7qaHHRhj8JGtsZb4JUgEzc8tk2g%3D%3D"); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("solYear","UTF-8") + "=" + URLEncoder.encode(year, "UTF-8")); /*연*/
            urlBuilder.append("&" + URLEncoder.encode("solMonth","UTF-8") + "=" + URLEncoder.encode(month, "UTF-8")); /*월*/

            URL url = new URL(urlBuilder.toString());
            InputStream APIis= url.openStream(); //url위치로 입력스트림 연결

            BufferedReader br = new BufferedReader(new InputStreamReader(APIis, "UTF-8") ); //inputstream 으로부터 xml 입력받기
            StringBuilder sb = new StringBuilder();
            //read data
            while (true) {
                String line = br.readLine();
                if (line == null)
                    break;
                sb.append(line);
            } //end reading data
            br.close();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentbuilder = factory.newDocumentBuilder();

            InputStream returnis = new ByteArrayInputStream(sb.toString().getBytes()); // 문자열을 InputStream으로 변환
            Document doc = documentbuilder.parse(returnis); //start parsing

            Element element = doc.getDocumentElement(); // get root element

            // 파싱할 태그의 리스트를 찾아온다
            NodeList item = element.getElementsByTagName("item");
            String locdate, holiday;

            // 리스트를 순회
            for (int i = 0; i < item.getLength(); i++) {

                Node node = item.item(i); //item의 요소
                Element fstElmnt = (Element) node;
                NodeList locdateNode = fstElmnt.getElementsByTagName("locdate");
                Element nameElement = (Element) locdateNode.item(0);
                locdateNode = nameElement.getChildNodes();
                locdate = locdateNode.item(0).getNodeValue();
                Log.i("holiday_paringlocdate: ", locdate);
                Log.i("holiday_paringdate: ", date);

                if (locdate.equals(date)) {
                    NodeList holidayNode = fstElmnt.getElementsByTagName("isHoliday");
                    holiday = holidayNode.item(0).getChildNodes().item(0).getNodeValue();
                    if (holiday.equals("Y")) {
                        Log.i("APIParsing holiday", holiday);
                        return 1;
                    }
                }
            }

        }catch(Exception e){
            Log.i("system err", e.getMessage());
        }//outer try_catch()

        return 0;
    }
}
