package org.procodingtools.idealsportscenter.fragments.cours;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.procodingtools.idealsportscenter.R;
import org.json.JSONObject;

import static org.procodingtools.idealsportscenter.commons.WebService.COURS;


/**
 * Created by djamiirr on 17/09/17.
 */

@SuppressLint("ValidFragment")
public class CoursDetailsDialog extends DialogFragment {

    private TextView price,libell,periode,dure;
    private ProgressBar pb;
    private String id;
    private RelativeLayout rl;
    private Dialog dialog;


   @SuppressLint("ValidFragment")
   public CoursDetailsDialog(String id){
       this.id=id;
   }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //init dialog
        dialog=new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_cours_details);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        rl= (RelativeLayout) dialog.findViewById(R.id.main_layout);
        pb= (ProgressBar) dialog.findViewById(R.id.progres);

        //Show progress
        progress(true);

        //init TextViews
        initTexts();

        //fetching data
        fetchData();

        return dialog;
    }

    private void fetchData() {


        StringRequest request=new StringRequest( COURS + "/" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject obj=new JSONObject(response);

                    //adapting
                    libell.setText(obj.getString("Libelle"));
                    periode.setText(obj.getString("Periode"));
                    price.setText(obj.getDouble("Prix")+" Dt");
                    dure.setText(obj.getDouble("Duree")+" h");
                }catch(Exception e){
                    Log.e("JSON error",e.getMessage());
                }finally {
                    progress(false);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("VOLLEY error",error.getMessage());
            }
        });
        //execute query
        RequestQueue rq= Volley.newRequestQueue(getContext());
        rq.add(request);
    }

    private void initTexts() {
        //init
        libell= (TextView) dialog.findViewById(R.id.title);
        periode= (TextView) dialog.findViewById(R.id.periode_tv);
        price= (TextView) dialog.findViewById(R.id.price_tv);
        dure= (TextView) dialog.findViewById(R.id.dure_tv);
    }


    private void progress(boolean f){
        if(f){
            rl.setVisibility(View.GONE);
            pb.setVisibility(View.VISIBLE);
        }else{
            pb.setVisibility(View.GONE);
            rl.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                try{
                    rl.setBackground(getActivity().getDrawable(R.drawable.fd_background));
                }catch (Exception e){};
            }
        }

    }


}
