package org.procodingtools.idealsportscenter.adapters.recyclerviews;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.procodingtools.idealsportscenter.R;
import org.procodingtools.idealsportscenter.fragments.cours.CoursDetailsDialog;

import java.util.List;
import java.util.Map;

/**
 * Created by djamiirr on 08/09/17.
 */

public class CoursRecyclerAdapter extends RecyclerView.Adapter<CoursRecyclerAdapter.ViewHolder>{

    private List<Map<String,String>> data;
    private FragmentManager fm;

    public CoursRecyclerAdapter(List<Map<String,String>> data, FragmentManager fm){
        this.data=data;
        this.fm=fm;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_cours,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String,String> map=data.get(position);
        holder.nomTv.setText(map.get("nom"));
        holder.id=map.get("id");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nomTv;
        private String id;
        CoursDetailsDialog dialog;

        public ViewHolder(final View itemView) {
            super(itemView);
            //init Views
            nomTv= (TextView) itemView.findViewById(R.id.course_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        dialog.dismiss();
                    }catch(Exception e){}
                    finally {
                        dialog= new CoursDetailsDialog(id);
                        dialog.show(fm, null);
                    }
                }
            });
        }
    }
}
