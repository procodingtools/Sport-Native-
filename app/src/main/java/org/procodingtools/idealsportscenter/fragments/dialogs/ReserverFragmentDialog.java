package org.procodingtools.idealsportscenter.fragments.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;

import static org.procodingtools.idealsportscenter.commons.entity.Users.USER_ENTITY;
import org.procodingtools.idealsportscenter.R;
import org.procodingtools.idealsportscenter.commons.WebService;
import org.procodingtools.idealsportscenter.fragments.reserved.ReservedFragment;
import org.procodingtools.idealsportscenter.fragments.resume.ResumeFragment;


@SuppressLint("ValidFragment")
public class ReserverFragmentDialog extends DialogFragment {

    private FragmentActivity fragmentActivity;
    private Button btnYes,btnNo;
    private TextView dialogText;
    private String dialogBoxTitle, courseName;
    public static int RESERVER=0,DERESERVER=1;
    private int dialogType;
    private StringRequest reservationRequest;
    private RequestQueue queue;
    private String idSeance,idAdheran,fromFrag, eventName;
    private View view;


    @SuppressLint("ValidFragment")
    public ReserverFragmentDialog(int dialogType, String idSeance, String fromFrag){
        this.dialogType=dialogType;
        this.idSeance = idSeance;
        this.fromFrag=fromFrag;
        this.idAdheran=USER_ENTITY.getID();
        this.eventName="";
    }

    @SuppressLint("ValidFragment")
    public ReserverFragmentDialog(int dialogType, String idSeance, String fromFrag,String eventName){
        this.dialogType=dialogType;
        this.idSeance = idSeance;
        this.fromFrag=fromFrag;
        this.idAdheran=USER_ENTITY.getID();
        this.eventName =eventName;
    }

    //---set the title of the dialog window---
    public void setDialogTitle(String title) {
        dialogBoxTitle = title;
    }

    public void setDialogCourseName(String couseName){
        this.courseName =couseName;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {

        view= inflater.inflate(R.layout.dialog_fragment_reserver, container);
        //init Views
        btnYes = (Button) view.findViewById(R.id.btnYes);
        btnNo = (Button) view.findViewById(R.id.btnNo);
        dialogText= (TextView) view.findViewById(R.id.dialog_text);

        //init requestQueue
        queue= Volley.newRequestQueue(view.getContext());

        //setting text concerning dialog event
        if(dialogType==0){
            dialogText.setText(getResources().getString(R.string.reserver)+eventName);
        }else {
            dialogText.setText(getResources().getString(R.string.dereserver)+eventName);
        }

        // Button listener
        btnYes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //refreshing view
                //---dismiss the alert---
                manageReseravtion();
            }
        });
        btnNo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //---set the title for the dialog
        getDialog().setTitle(dialogBoxTitle);

        //add course name
        dialogText.append(courseName+" ?");

        return view;
    }

    //start put/delete reservation
    private void manageReseravtion() {

        //Log.e("link",WebService.RESERVE+"/"+idAdheran+"/"+ idSeance +"/"+dialogType);

        final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.wait_progress));
        progressDialog.setCancelable(false);
        progressDialog.show();

        reservationRequest=new StringRequest(Request.Method.GET, WebService.RESERVE+"/"+idAdheran+"/"+ idSeance +"/"+dialogType, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (dialogType==0) {
                    if (response.contains("1")) {
                        Toast.makeText(fragmentActivity, getString(R.string.reservation_success), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(fragmentActivity, getString(R.string.unfortunatally), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new ResumeFragment(), null).commit();
                    Toast.makeText(fragmentActivity, getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                }
                if (fromFrag.equals("resume")) {
                    fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new ResumeFragment(), null).commit();
                }else {
                    fragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new ReservedFragment(), null).commit();
                }
                progressDialog.dismiss();
                dismiss();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new AwesomeErrorDialog(getContext()).setMessage(getString(R.string.verify_network))
                        .setCancelable(false)
                        .setButtonText(getString(R.string.dialog_ok_button))
                        .setButtonBackgroundColor(ContextCompat.getColor(getContext(),R.color.material_red))
                        .setErrorButtonClick(new Closure() {
                            @Override
                            public void exec() {
                                getDialog().dismiss();
                            }
                        });
                progressDialog.dismiss();
            }
        });
        queue.add(reservationRequest);

    }


    @Override
    public void onAttach(Context context) {
        Activity a;
        a=(Activity) context;
        fragmentActivity=(FragmentActivity) a;
        super.onAttach(context);
    }


}