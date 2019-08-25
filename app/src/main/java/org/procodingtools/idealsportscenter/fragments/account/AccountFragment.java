package org.procodingtools.idealsportscenter.fragments.account;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gmail.samehadar.iosdialog.IOSDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.procodingtools.idealsportscenter.R;
import org.procodingtools.idealsportscenter.adapters.recyclerviews.AccountRVAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.procodingtools.idealsportscenter.commons.WebService.PAYEMENT;
import static org.procodingtools.idealsportscenter.commons.entity.Users.USER_ENTITY;

/**
 * Created by djamiirr on 02/01/18.
 */

public class AccountFragment extends Fragment {

    private View v;
    private RecyclerView recyclerView;
    private AccountRVAdapter adapter;
    private List<HashMap<String,String>> data;
    private FloatingActionButton refresh;
    private TextView name;
    private CollapsingToolbarLayout toolbar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //init
        v=inflater.inflate(R.layout.fragment_account,container,false);

        //init views
        recyclerView=v.findViewById(R.id.account_rv);
        refresh=v.findViewById(R.id.fabrefresh);
        name=v.findViewById(R.id.name);
        toolbar=v.findViewById(R.id.toolbar_layout);

        toolbar.setTitle(USER_ENTITY.getNom()+" "+USER_ENTITY.getPrenom());
        toolbar.setCollapsedTitleTextColor(ContextCompat.getColor(getContext(),R.color.white));

        name.setText(USER_ENTITY.getNom()+" "+USER_ENTITY.getPrenom());

        //init adapter
        //init data
        data=new ArrayList<>();

        //adapter instace
        adapter=new AccountRVAdapter(data);

        //init recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        //setting adapter to rcycler view
        recyclerView.setAdapter(adapter);

        //end init

        //start fetching data
        fetchData();


        //seting refresh fab click listener
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //refetch data
                fetchData();
            }
        });

        return v;
    }

    //starting fetch data
    private void fetchData() {
        final IOSDialog progress=new IOSDialog.Builder(getContext())
                .setTitle(getString(R.string.wait_progress))
                .setTitleColorRes(R.color.gray)
                .setCancelable(false)
                .build();

        progress.show();
        data.clear();
        JsonArrayRequest jar=new JsonArrayRequest(PAYEMENT+USER_ENTITY.getID(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0;i<response.length();i++){
                    try {
                        JSONObject obj=response.getJSONObject(i);
                        HashMap<String,String>map=new HashMap<>();
                        String dateDeb=obj.getString("DateDebut").split("T")[0],dateFin=obj.getString("DateFin").split("T")[0];
                        map.put("date",dateDeb+" à "+dateFin);
                        map.put("type",obj.getString("Type"));
                        map.put("price",obj.getString("Montant"));
                        map.put("is paid",obj.getString("Payer"));
                        data.add(map);
                    } catch (JSONException e) {
                        Log.e("json error",e.getMessage());
                    }
                }
                adapter.notifyDataSetChanged();
                progress.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        Volley.newRequestQueue(getContext()).add(jar);
    }
}
