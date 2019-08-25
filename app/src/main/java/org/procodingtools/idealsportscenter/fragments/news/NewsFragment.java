package org.procodingtools.idealsportscenter.fragments.news;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.gmail.samehadar.iosdialog.IOSDialog;

import org.procodingtools.idealsportscenter.R;
import org.procodingtools.idealsportscenter.adapters.recyclerviews.NewsRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.procodingtools.idealsportscenter.commons.WebService.NEWS;

/**
 * Created by djamiirr on 03/11/17.
 */

public class NewsFragment extends Fragment {
    private View v;
    private RecyclerView recyclerView;
    private NewsRecyclerAdapter adapter;
    private List<Map<String,String>> data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_news,null);

        //init
        //init views
        initViews();

        //init lists
        data=new ArrayList<>();

        //init RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new NewsRecyclerAdapter(data,getFragmentManager());
        recyclerView.setAdapter(adapter);

        //fetch data
        fetchData();
        return v;
    }

    private void fetchData() {
        final IOSDialog progress=new IOSDialog.Builder(getContext())
                .setTitle(getString(R.string.wait_progress))
                .setTitleColorRes(R.color.gray)
                .setCancelable(false)
                .build();

        progress.show();

        data.clear();

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(NEWS, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0;i<response.length();i++){
                    try {
                        JSONObject obj=response.getJSONObject(i);
                        Map<String,String> map=new HashMap<>();
                        map.put("title",obj.getString("Titre"));
                        map.put("text",obj.getString("Description"));
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
                new AwesomeErrorDialog(getContext()).setMessage(getString(R.string.verify_network))
                        .setCancelable(false)
                        .setErrorButtonClick(new Closure() {
                            @Override
                            public void exec() {
                                getActivity().finish();
                            }
                        })
                        .setButtonText(getString(R.string.dialog_ok_button))
                        .show();
            }
        });
        Volley.newRequestQueue(getContext()).add(jsonArrayRequest);
    }

    private void initViews() {
        recyclerView= v.findViewById(R.id.news_rv);
    }
}
