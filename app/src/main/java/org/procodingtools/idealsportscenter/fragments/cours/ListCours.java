package org.procodingtools.idealsportscenter.fragments.cours;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.gmail.samehadar.iosdialog.IOSDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.procodingtools.idealsportscenter.R;
import org.procodingtools.idealsportscenter.adapters.recyclerviews.CoursRecyclerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.procodingtools.idealsportscenter.commons.WebService.COURS;


public class ListCours extends Fragment {

    private RecyclerView recyclerView;
    private CoursRecyclerAdapter adapter;
    private List<Map<String,String>> data;
    private RequestQueue requestQueue;
    private View v;
    private IOSDialog progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_list_cours,null);
        //init
        //init Volley RQ
        requestQueue= Volley.newRequestQueue(getContext());
        //init Views
        recyclerView= (RecyclerView) v.findViewById(R.id.cours_rv);
        progress=new IOSDialog.Builder(getContext())
                .setTitle(getString(R.string.wait_progress))
                .setTitleColorRes(R.color.gray)
                .setCancelable(false)
                .build();

        progress.show();

        getActivity().setTitle(getString(R.string.courses_list));

        //init RecyclerView adapter
        //init data
        data=new ArrayList<>();
        //init  adapter
        adapter=new CoursRecyclerAdapter(data,getFragmentManager());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));

        //fetching data
        fetchData();

        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i==KeyEvent.KEYCODE_BACK)
                    getActivity().finish();
                return false;
            }
        });

        return v;
    }

    //start fetchng data
    private void fetchData() {
        JsonArrayRequest jar=new JsonArrayRequest(Request.Method.GET, COURS, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0;i<response.length();i++){
                    try {
                        JSONObject obj=response.getJSONObject(i);
                        Map<String,String> map=new HashMap<>();
                        map.put("nom",obj.getString("Libelle"));
                        map.put("id",obj.getString("Id"));
                        data.add(map);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("JSON error",e.getMessage());
                    }
                    finally {
                        progress.dismiss();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                final AwesomeErrorDialog errorDialog=new AwesomeErrorDialog(v.getContext()).setTitle(getString(R.string.error));
                        errorDialog.setMessage(getString(R.string.verify_network))
                        .setButtonText(getString(R.string.dialog_ok_button))
                        .setCancelable(false)
                        .show();
                errorDialog.setErrorButtonClick(new Closure() {
                    @Override
                    public void exec() {
                        errorDialog.hide();
                        getActivity().finish();
                    }
                });

            }
        });
        requestQueue.add(jar);

    }

}
