package org.procodingtools.idealsportscenter.fragments.reserved;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.procodingtools.idealsportscenter.R;
import org.procodingtools.idealsportscenter.adapters.recyclerviews.ReservedRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;

import static android.view.View.GONE;
import static org.procodingtools.idealsportscenter.commons.WebService.RESERVED;
import static org.procodingtools.idealsportscenter.commons.entity.Users.USER_ENTITY;

public class ReservedFragment extends Fragment {
    private RecyclerView recyclerView;
    private ReservedRecyclerAdapter adapter;
    private List<Map<String,String>> fullData,data;
    private int day,month,year;
    private TextView naTv;
    private View v;
    private HorizontalCalendar horizontalPicker;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_reserved,container,false);

        //init
        fullData=new ArrayList<>();
        data=new ArrayList<>();

        //init views
        initViews();

        //init horizentalDatePicker
        horizontalPicker = new HorizontalCalendar.Builder(v,R.id.calendarView).build();

        //init adapter
        day= Calendar.getInstance().DAY_OF_MONTH;
        month= Calendar.getInstance().MONTH;
        year= Calendar.getInstance().YEAR;
        adapter=new ReservedRecyclerAdapter(data);
        adapter.setDate(day,month,year);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));

        //fetching data
        fetchData();

        return v;
    }


    //starting init views
    private void initViews() {
        recyclerView= (RecyclerView) v.findViewById(R.id.rv_calendar);
        naTv= (TextView) v.findViewById(R.id.tv_na);
    }


    //starting fetch data
    private void fetchData() {
        JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, RESERVED+"/"+USER_ENTITY.getID(), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj=response.getJSONObject(i);
                        Map<String, String> map = new HashMap<>();
                        map.put("cours", obj.getString("NomCours"));
                        map.put("id", obj.getString("IdSeance"));
                        String date=obj.getString("DateSeance"),time,commutationDate=obj.getString("DateReservation");
                        date.replace("\\","");

                        //setting commutation ic_date
                        map.put("commutation day",commutationDate.split("/")[0]);
                        map.put("commutation month",commutationDate.split("/")[1]);
                        map.put("commutation year", commutationDate.split("/")[2].substring(0,commutationDate.split("/")[2].indexOf(' ')));

                        //setting session ic_date
                        map.put("day",date.split("/")[0]);
                        map.put("month",date.split("/")[1]);
                        map.put("year", date.split("/")[2].substring(0,date.split("/")[2].indexOf(' ')));
                        time=date.split("\\s")[1];
                        map.put("hour",time.split(":")[0]);
                        map.put("minute",time.split(":")[1]);
                        Calendar systemDate=Calendar.getInstance(),sessionDate=Calendar.getInstance();
                        sessionDate.set(Calendar.DAY_OF_MONTH,Integer.valueOf(date.split("/")[0]));
                        sessionDate.set(Calendar.MONTH,Integer.valueOf(date.split("/")[1])-1);
                        sessionDate.set(Calendar.YEAR,Integer.valueOf(date.split("/")[2].substring(0,date.split("/")[2].indexOf(' '))));
                        sessionDate.set(Calendar.HOUR_OF_DAY,Integer.valueOf(time.split(":")[0]));
                        sessionDate.set(Calendar.MINUTE,Integer.valueOf(time.split(":")[1]));
                        if (systemDate.compareTo(sessionDate)>0)
                            map.put("changeable","0");
                        else
                            map.put("changeable","1");
                        fullData.add(map);
                    } catch (JSONException e) {
                        Log.e("JsonError",e.getMessage());
                    }
                }
                if (fullData.size()==0)
                    showNA(true);
                else
                    settingFirstApparence();
                initDatePickerListener();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /*MainShow.showNetError();*/
            }
        });
        Volley.newRequestQueue(getContext()).add(request);

    }

    //showing/hidding N/A
    private void showNA(boolean b){
        if(b) {
            recyclerView.setVisibility(GONE);
            naTv.setVisibility(View.VISIBLE);
        }
        else {
            naTv.setVisibility(GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    }

    //starting set datePicker listener
    private void initDatePickerListener() {

        horizontalPicker.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                data.clear();
                int i;
                for(i=0;i<fullData.size();i++){
                    Map<String,String> map=fullData.get(i);
                    Calendar calendar= Calendar.getInstance();
                    calendar.setTime(date);
                    int day=calendar.get(Calendar.DAY_OF_MONTH)
                            ,month=calendar.get(Calendar.MONTH)
                            ,year=calendar.get(Calendar.YEAR);
                    month++;
                    String dayStr,monthStr;

                    if(map.get("day").startsWith("0"))
                        dayStr=map.get("day").substring(1,2);
                    else
                        dayStr=map.get("day");

                    if(map.get("month").startsWith("0"))
                        monthStr=map.get("month").substring(1,2);
                    else
                        monthStr=map.get("month");


                    if(Integer.valueOf(dayStr)==day&& Integer.valueOf(monthStr)==month&& Integer.valueOf(map.get("year"))==year){
                        data.add(map);
                        showNA(false);
                    }

                }
                if (data.size()==0)
                    showNA(true);
                Calendar calendar= Calendar.getInstance();
                calendar.setTime(date);
                adapter.setDate(calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.MONTH),calendar.get(Calendar.YEAR));
                adapter.notifyDataSetChanged();
            }
        });
    }

    //setting first apparence
    private void settingFirstApparence(){
        data.clear();
        int i;
        for(i=0;i<fullData.size();i++){
            Map<String,String> map=fullData.get(i);
            Calendar calendar= Calendar.getInstance();
            int day=calendar.get(Calendar.DAY_OF_MONTH)
                    ,month=calendar.get(Calendar.MONTH)
                    ,year=calendar.get(Calendar.YEAR);
            month++;
            String dayStr,monthStr;

            if(map.get("day").startsWith("0"))
                dayStr=map.get("day").substring(1,2);
            else
                dayStr=map.get("day");

            if(map.get("month").startsWith("0"))
                monthStr=map.get("month").substring(1,2);
            else
                monthStr=map.get("month");


            if(Integer.valueOf(dayStr)==day&& Integer.valueOf(monthStr)==month&& Integer.valueOf(map.get("year"))==year){
                data.add(map);
                showNA(false);
            }

        }
        if (data.size()==0)
            showNA(true);
        Calendar calendar= Calendar.getInstance();
        adapter.setDate(calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.MONTH),calendar.get(Calendar.YEAR));
        adapter.notifyDataSetChanged();
    }
}
