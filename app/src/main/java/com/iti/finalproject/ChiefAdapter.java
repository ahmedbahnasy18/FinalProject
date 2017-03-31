package com.iti.finalproject;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
                listener.onItemClick(v, viewHolder.getAdapterPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Chief cheif = chiefList.get(position);
        holder.cheifName.setText(cheif.getName());
        holder.deliverIn.setText(cheif.getDeliverIn());
        holder.ratingBar.setRating(cheif.getRating());
        if(!TextUtils.isEmpty(cheif.getImage()))
            Picasso
                    .with(activity)
                    .load(cheif.getImage())
                    .resize(200,200)
                    .placeholder(R.drawable.animation_loading)
                    .error(R.drawable.blank_chief)
                    .into(holder.chiefImage);

        LayerDrawable stars = (LayerDrawable) holder.ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(ContextCompat.getColor(activity, android.R.color.darker_gray), PorterDuff.Mode.SRC_ATOP);
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
        TextView cheifName;
        TextView deliverIn;
        RatingBar ratingBar;
        ImageView chiefImage;

        CustomViewHolder(View itemView) {
            super(itemView);
            this.cheifName = (TextView) itemView.findViewById(R.id.cheifname);
            this.deliverIn = (TextView) itemView.findViewById(R.id.deliverin);
            this.ratingBar = (RatingBar) itemView.findViewById(R.id.rating_bar);
            this.chiefImage = (ImageView) itemView.findViewById(R.id.img_chief);
        }
    }
}
