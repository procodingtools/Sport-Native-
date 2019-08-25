package org.procodingtools.idealsportscenter.fragments.resume;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.gmail.samehadar.iosdialog.IOSDialog;

import static android.media.MediaCodec.MetricsConstants.MODE;
import static org.procodingtools.idealsportscenter.commons.WebService.COURS;
import static org.procodingtools.idealsportscenter.commons.entity.Users.USER_ENTITY;
import org.procodingtools.idealsportscenter.R;
import org.procodingtools.idealsportscenter.fragments.dialogs.LoginFragmentDialog;
import org.procodingtools.idealsportscenter.fragments.dialogs.ReserverFragmentDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static org.procodingtools.idealsportscenter.commons.WebService.RESUME;


/**
 * Created by djamiirr on 09/09/17.
 */

public class ResumeFragment extends Fragment {

    private View v;
    private WeekView weekView;
    private ArrayList<WeekViewEvent> events,displayedEvents;
    private LinearLayout spinner;
    private SpinnerDialog spinnerDialog;
    private TextView spinnerTv,toDayTv;
    private ArrayList<String>coursesNameList;
    private RelativeLayout goToDay;
    private LoginFragmentDialog dialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_resume,null);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();


        //init

        //init dialog
        if (USER_ENTITY==null)
            dialog=new LoginFragmentDialog("resume");


        //initViews
        initViews();

        //setting current day icon
        toDayTv.setText(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+"");

        //setting go to day listener
        goToDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weekView.goToDate(Calendar.getInstance());
            }
        });

        //init coursesNmaeList
        coursesNameList=new ArrayList<>();

        //tessssst
        coursesNameList.add(getString(R.string.all_courses));




        spinnerTv.setText(coursesNameList.get(0));


        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialog.showSpinerDialog();
            }
        });


        //setting visible days on screen
        weekView.setNumberOfVisibleDays(2);

        //init events list
        events=new ArrayList<>();
        displayedEvents=new ArrayList<>();

        //making Agenda
        agendaWeek();

        //init weekview
        initWeekView();

        //fetching courses names
        fetchCoursesName();

        //fetch data
        fetchData();

        return v;
    }

    //starting init weekview
    private void initWeekView() {
        Calendar min=Calendar.getInstance();

        //min.set(Calendar.DAY_OF_WEEK,Calendar.getInstance().getFirstDayOfWeek()+1);
        min.set(Calendar.DAY_OF_WEEK,Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        min.set(Calendar.MONTH,Calendar.getInstance().get(Calendar.MONTH));
        min.set(Calendar.YEAR,Calendar.getInstance().get(Calendar.YEAR));
        weekView.goToDate(min);
        weekView.goToHour(8);
    }

    private void weekOverScroll(){
        Calendar min=Calendar.getInstance();
        min.set(Calendar.DAY_OF_MONTH,Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        min.set(Calendar.MONTH,Calendar.getInstance().get(Calendar.MONTH));
        min.set(Calendar.YEAR,Calendar.getInstance().get(Calendar.YEAR));
        weekView.goToDate(min);
    }


    //starting init views
    private void initViews() {
        weekView= (WeekView) v.findViewById(R.id.weekview);
        spinner=v.findViewById(R.id.spinner_layout);
        spinnerTv=v.findViewById(R.id.spinner_tv);
        toDayTv=v.findViewById(R.id.tv_today);
        goToDay=v.findViewById(R.id.go_today);
    }

    //weekview constraints
    private void agendaWeek(){

        //weekView event click listener
        weekView.setOnEventClickListener(new WeekView.EventClickListener() {
            @Override
            public void onEventClick(WeekViewEvent event, RectF eventRect) {

                   if (USER_ENTITY != null) {
                        if (event.getStartTime().compareTo(Calendar.getInstance())>0) {
                            //making processes depending color
                            if (event.getColor() == Color.GREEN/*create reservation*/) {
                                ReserverFragmentDialog dialog = new ReserverFragmentDialog(ReserverFragmentDialog.RESERVER, event.getId() + "", "resume");
                                dialog.setCancelable(false);
                                dialog.setDialogTitle(getContext().getResources().getString(R.string.reserver_title));
                                dialog.setDialogCourseName(event.getName());
                                dialog.show(getChildFragmentManager(), null);
                                //event.setColor(Color.BLUE);
                            } else if (event.getColor() == Color.RED/*full room*/) {
                                Toast.makeText(getContext(), getResources().getString(R.string.salle_sature), Toast.LENGTH_SHORT).show();
                            } else {/*delete reservation*/
                                ReserverFragmentDialog dialog = new ReserverFragmentDialog(ReserverFragmentDialog.DERESERVER, event.getId() + "", "resume");
                                dialog.setCancelable(false);
                                dialog.setDialogTitle(getContext().getResources().getString(R.string.reserver_title));
                                dialog.setDialogCourseName(event.getName());
                                dialog.show(getChildFragmentManager(), null);
                                //event.setColor(Color.GREEN);
                            }
                        } else if (event.getStartTime().compareTo(Calendar.getInstance()) < 0)
                            new AwesomeErrorDialog(getContext())
                                    .setMessage(getString(R.string.seance_time_over)).show();
                    }else{

                       if (dialog.getDialog()==null)
                        dialog.show(getChildFragmentManager(),null);

                    }
            }
        });
        //setting some visible effects
        weekView.setHourSeparatorColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
        weekView.setHourSeparatorHeight(1);

        //weekview month change listener
        weekView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
            @Override
            public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                List<WeekViewEvent> weekviewEvents = new ArrayList<WeekViewEvent>();
                for (WeekViewEvent event : displayedEvents) {
                    if(event.getStartTime().get(Calendar.MONTH)+1 == newMonth && event.getStartTime().get(Calendar.YEAR) == newYear){
                        weekviewEvents.add(event);
                    }
                }

                return weekviewEvents;
            }
        });

        weekView.setScrollListener(new WeekView.ScrollListener() {
            @Override
            public void onFirstVisibleDayChanged(Calendar newFirstVisibleDay, Calendar oldFirstVisibleDay) {
                /*if (Calendar.getInstance().get(Calendar.WEEK_OF_MONTH)!=newFirstVisibleDay.get(Calendar.WEEK_OF_MONTH)){
                    if (newFirstVisibleDay.get(Calendar.WEEK_OF_MONTH)==Calendar.getInstance().get(Calendar.WEEK_OF_MONTH)+1)
                        weekOverScroll();
                    else if (newFirstVisibleDay.get(Calendar.WEEK_OF_MONTH)==Calendar.getInstance().get(Calendar.WEEK_OF_MONTH)-1)
                        weekOverScroll(true);
                }*/
                if (newFirstVisibleDay.compareTo(Calendar.getInstance())<0)
                    weekOverScroll();
                if (newFirstVisibleDay.get(Calendar.HOUR_OF_DAY)>22)
                    weekView.goToHour(22);
                if (newFirstVisibleDay.get(Calendar.HOUR)<8)
                    weekView.goToHour(8);
            }
        });

        //changeing dimensions to best fit the view.
        weekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
        weekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
        weekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
    }

    //start fetching data
    private void fetchData() {
        final IOSDialog progress=new IOSDialog.Builder(getContext())
                .setTitle(getString(R.string.wait_progress))
                .setTitleColorRes(R.color.gray)
                .setCancelable(false)
                .build();
        progress.show();
        String id="";
        if (USER_ENTITY!=null)
            id="/"+USER_ENTITY.getID();
        JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, RESUME + id, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int i;
                List<String>coursesName=new ArrayList<>();
                for (i=response.length();i>=0;i--){
                    try {
                        JSONObject obj=response.getJSONObject(i);
                            //creatin event
                            WeekViewEvent event=new WeekViewEvent();
                            Calendar preStart=Calendar.getInstance();
                            Calendar start= Calendar.getInstance();
                            String dateTime=obj.getString("Date");
                            String endTime= String.valueOf(obj.getDouble("Duree"));
                            String date=dateTime.split("T")[0];
                            String time=dateTime.split("T")[1];

                            //parsing ic_date time
                            //parsin ic_date
                            preStart.set(Calendar.DAY_OF_MONTH, Integer.valueOf(date.split("-")[2]));
                            preStart.set(Calendar.MONTH, Integer.valueOf(date.split("-")[1])-1);
                            preStart.set(Calendar.YEAR, Integer.valueOf(date.split("-")[0]));


                            start.set(Calendar.DAY_OF_MONTH,preStart.get(Calendar.DAY_OF_MONTH));
                            start.set(Calendar.MONTH,Calendar.getInstance().get(Calendar.MONTH));
                            start.set(Calendar.YEAR,Calendar.getInstance().get(Calendar.YEAR));

                            //parsing time
                            start.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time.split(":")[0]));
                            start.set(Calendar.MINUTE, Integer.valueOf(time.split(":")[1]));
                            Calendar end= (Calendar) start.clone();
                            end.add(Calendar.HOUR_OF_DAY, Integer.valueOf(endTime.split("\\.")[0]));
                            end.add(Calendar.MINUTE, Integer.valueOf(endTime.split("\\.")[1].substring(0,1))*60);

                            //init event
                            event.setName(obj.getString("Cours"));
                            event.setStartTime(start);
                            event.setEndTime(end);
                            if (USER_ENTITY!=null){
                                int freePlaces=obj.getInt("Capacite")-obj.getInt("Reserver");
                                if (Integer.valueOf(obj.getString("Reserved"))>=1)
                                    event.setColor(Color.BLUE);
                                else if (freePlaces==0)
                                    event.setColor(Color.RED);
                                else if (freePlaces>0)
                                    event.setColor(Color.GREEN);
                            }else
                                event.setColor(Color.CYAN);

                            event.setId(Long.valueOf(obj.getInt("IdSeance")));
                            //end creating event

                            //adding event to list
                            events.add(event);
                        displayedEvents.add(event);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //Log.e("json resume error",e.getMessage());
                    }

                }

                //dismissing progress
                progress.dismiss();

                if(i==0) {
                    Toast.makeText(getContext(), getString(R.string.no_courses), Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
                //nitifying weekView
                weekView.notifyDatasetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new AwesomeErrorDialog(getContext())
                        .setMessage(getString(R.string.verify_network))
                        .setButtonText(getString(R.string.dialog_ok_button))
                        .setCancelable(false)
                        .setErrorButtonClick(new Closure() {
                            @Override
                            public void exec() {
                                getActivity().finish();
                            }
                        })
                        .show();

            }
        });
        //adding request to queue
        Volley.newRequestQueue(getContext()).add(request);
    }

    //fetching courses names
    private void fetchCoursesName(){
        Volley.newRequestQueue(v.getContext()).add(new JsonArrayRequest(COURS, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0;i<response.length();i++){
                    try {
                        JSONObject obj=response.getJSONObject(i);
                        coursesNameList.add(obj.getString("Libelle"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                spinnerDialog=new SpinnerDialog(getActivity(),coursesNameList,"Select course",R.style.DialogAnimations_SmileWindow);

                //calling spinner listener
                setSpinnerListener();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }));
    }

    private void setSpinnerListener() {
        //spinner listener
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                spinnerTv.setText(s);
                displayedEvents.clear();
                if (!s.equals(getString(R.string.all_courses))){
                    for(int j=0;j<events.size();j++){
                        WeekViewEvent event=events.get(j);
                        if(event.getName().equals(s))
                            displayedEvents.add(event);
                    }
                }else
                    displayedEvents= (ArrayList<WeekViewEvent>) events.clone();
                weekView.notifyDatasetChanged();
            }
        });
    }

}
