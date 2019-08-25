package org.procodingtools.idealsportscenter.adapters.recyclerviews;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.procodingtools.idealsportscenter.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by djamiirr on 02/01/18.
 */

public class AccountRVAdapter extends RecyclerView.Adapter<AccountRVAdapter.ViewHolder> {


    private List<HashMap<String,String>> data;
    private Context context;

    public AccountRVAdapter(List<HashMap<String,String>>data){
        this.data=data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_account_payment,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HashMap<String,String>map=data.get(position);
        holder.type.setText(map.get("type"));
        holder.date.setText(map.get("ic_date"));
        holder.price.setText(map.get("price")+" DT");
        if (map.get("is paid").equals("0"))
            holder.background.setBackground(ContextCompat.getDrawable(context,R.drawable.red_gradient));
        else
            holder.background.setBackground(ContextCompat.getDrawable(context,R.drawable.green_gradien));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView type,price,date;
        RelativeLayout background;
        public ViewHolder(View itemView) {
            super(itemView);
             //start init views
            initViews(itemView);
        }

        private void initViews(View v) {
            type=v.findViewById(R.id.type);
            price=v.findViewById(R.id.price);
            date=v.findViewById(R.id.date);
            background=v.findViewById(R.id.background_layout);
        }
    }


}
