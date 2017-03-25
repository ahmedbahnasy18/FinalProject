package com.iti.finalproject;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 20/03/2017.
 */

public class ChiefAdapter extends RecyclerView.Adapter<ChiefAdapter.CustomViewHolder> {
    private List<Chief> chiefList, fullList;
    private Activity activity;
    CustomItemClickListener listener;

    public ChiefAdapter(Activity activity, List<Chief> chiefList, CustomItemClickListener listener) {
        this.chiefList = chiefList;
        this.fullList = new ArrayList<>(chiefList);
        this.activity = activity;
        this.listener = listener;
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cheif_row,parent,false);
        final CustomViewHolder viewHolder = new CustomViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, viewHolder.getPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Chief cheif = chiefList.get(position);
        holder.cheifName.setText(cheif.getName());
        holder.deliverIn.setText(cheif.getDeliverIn());
        switch (cheif.getRating()){
            case 1:
                holder.star1.setImageResource(android.R.drawable.star_big_on);
                break;
            case 2:
                holder.star1.setImageResource(android.R.drawable.star_big_on);
                holder.star2.setImageResource(android.R.drawable.star_big_on);
                break;
            case 3:
                holder.star1.setImageResource(android.R.drawable.star_big_on);
                holder.star2.setImageResource(android.R.drawable.star_big_on);
                holder.star3.setImageResource(android.R.drawable.star_big_on);
                break;
            case 4:
                holder.star1.setImageResource(android.R.drawable.star_big_on);
                holder.star2.setImageResource(android.R.drawable.star_big_on);
                holder.star3.setImageResource(android.R.drawable.star_big_on);
                holder.star4.setImageResource(android.R.drawable.star_big_on);
                break;
            case 5:
                holder.star1.setImageResource(android.R.drawable.star_big_on);
                holder.star2.setImageResource(android.R.drawable.star_big_on);
                holder.star3.setImageResource(android.R.drawable.star_big_on);
                holder.star4.setImageResource(android.R.drawable.star_big_on);
                holder.star5.setImageResource(android.R.drawable.star_big_on);
                break;
        }
    }

    void filter(final String filter) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                chiefList.clear();
                if (TextUtils.isEmpty(filter))
                    chiefList.addAll(fullList);
                else
                    for (Chief chief : fullList)
                        if (chief.getName().toLowerCase().contains(filter.toLowerCase()))
                            chiefList.add(chief);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        }).start();

    }

    @Override
    public int getItemCount() {
        return chiefList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        protected TextView cheifName;
        protected TextView deliverIn;
        protected ImageView star1;
        protected ImageView star2;
        protected ImageView star3;
        protected ImageView star4;
        protected ImageView star5;

        public CustomViewHolder(View itemView) {
            super(itemView);
            this.cheifName = (TextView) itemView.findViewById(R.id.cheifname);
            this.deliverIn = (TextView) itemView.findViewById(R.id.deliverin);
            this.star1 = (ImageView) itemView.findViewById(R.id.star1);
            this.star2 = (ImageView) itemView.findViewById(R.id.star2);
            this.star3 = (ImageView) itemView.findViewById(R.id.star3);
            this.star4 = (ImageView) itemView.findViewById(R.id.star4);
            this.star5 = (ImageView) itemView.findViewById(R.id.star5);
        }
    }
}
