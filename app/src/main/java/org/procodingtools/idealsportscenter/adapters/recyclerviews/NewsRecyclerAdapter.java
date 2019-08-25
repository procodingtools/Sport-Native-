package org.procodingtools.idealsportscenter.adapters.recyclerviews;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.procodingtools.idealsportscenter.R;
import org.procodingtools.idealsportscenter.fragments.news.NewsDetailsDialog;

import java.util.List;
import java.util.Map;

/**
 * Created by djamiirr on 03/11/17.
 */

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder> {

    private List<Map<String,String>> data;
    private FragmentManager fm;

    public NewsRecyclerAdapter(List data, FragmentManager fm){
        this.data=data;
        this.fm=fm;
    }

    @Override
    public NewsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_news,parent,false));
    }

    @Override
    public void onBindViewHolder(NewsRecyclerAdapter.ViewHolder holder, int position) {
        Map<String,String> map=data.get(position);
        holder.eventTitle.setText(map.get("title"));
        Log.e("title adapter",map.get("title"));
        holder.id=map.get("id");
        holder.eventText.setText(map.get("text"));
        holder.title=new String(map.get("title"));
        holder.text=new String(map.get("text"));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        String id,title,text;
        NewsDetailsDialog dialog;
        //ImageView eventImage;
        TextView eventTitle,eventText;
        public ViewHolder(View itemView) {
            super(itemView);
            //initViews
            initViews(itemView);

        }

        private void initViews(View itemView) {
            //eventImage= (ImageView) itemView.findViewById(R.id.event_image);
            eventTitle= (TextView) itemView.findViewById(R.id.event_title);
            eventText= (TextView) itemView.findViewById(R.id.event_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        dialog.dismiss();
                    }catch (Exception e) {}
                    finally {
                        dialog = new NewsDetailsDialog(title, text);
                        dialog.show(fm, null);
                    }
                }
            });

        }
    }
}
