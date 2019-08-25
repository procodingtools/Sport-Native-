package org.procodingtools.idealsportscenter.adapters.recyclerviews;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;

import org.procodingtools.idealsportscenter.R;
import org.procodingtools.idealsportscenter.fragments.dialogs.ReserverFragmentDialog;

import java.util.List;
import java.util.Map;

/**
 * Created by djamiirr on 06/09/17.
 */

public class ReservedRecyclerAdapter extends RecyclerView.Adapter<ReservedRecyclerAdapter.ViewHolder> {

    private List<Map<String,String>> data;
    private Context cnx;
    private int day,month,year;
    private String hour,minute;

    public ReservedRecyclerAdapter(List<Map<String, String>> data){
        this.data=data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_reserved_cours,parent,false);
        cnx=parent.getContext();
        return new ViewHolder(v);
    }

    public void setDate(int day,int month,int year){
        this.day=day;
        this.month=month;
        this.year=year;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String,String> map=data.get(position);
        holder.cours.setText(map.get("cours"));
        holder.date.setText(cnx.getString(R.string.reserved_on)+map.get("commutation day")+"/"+map.get("commutation month")+"/"+map.get("commutation year"));
        day= Integer.valueOf(map.get("day"));
        month= Integer.valueOf(map.get("month"));
        year= Integer.valueOf(map.get("year"));
        hour=map.get("hour");
        minute=map.get("minute");
        holder.id=map.get("id");
        switch (map.get("changeable")){
            case "1":{holder.changeable=true;break;}
            case "0":{holder.changeable=false;break;}
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView cours,date;
        private String id;
        private boolean changeable;
        ReserverFragmentDialog dialog;
        AwesomeErrorDialog timeOver;

        public ViewHolder(final View itemView) {
            super(itemView);
            //init views
            initViews(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (changeable)
                        try{
                            dialog.dismiss();
                        }catch(Exception e){}
                    finally {
                            showDialog();
                        }
                    else
                        try{
                            timeOver.hide();
                        }catch (Exception e){}
                    finally {
                            timeOver=new AwesomeErrorDialog(itemView.getContext())
                                    .setMessage(itemView.getContext().getString(R.string.seance_time_over));
                            timeOver.show();
                        }

                }
                public  void showDialog(){
                    FragmentManager fragmentManager =((FragmentActivity)cnx).getSupportFragmentManager();
                    dialog = new ReserverFragmentDialog(ReserverFragmentDialog.DERESERVER,id,"reserved");
                    dialog.setCancelable(false);
                    dialog.setDialogTitle(cnx.getResources().getString(R.string.reserver_title));
                    dialog.setDialogCourseName(cours.getText().toString());
                    dialog.show(fragmentManager, null);
                }
            });
        }

        private void initViews(View v) {
            cours= (TextView) v.findViewById(R.id.tvcours);
            date= (TextView) v.findViewById(R.id.date);
        }

    }
}
