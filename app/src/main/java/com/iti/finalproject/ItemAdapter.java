package com.iti.finalproject;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ahmed on 21/03/2017.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.CustomViewHolder>{

    private List<Item> itemsList;
    private Activity activity;
    CustomItemClickListener listener;

    public ItemAdapter(Activity activity, List<Item> itemsList, CustomItemClickListener listener) {
        this.itemsList = itemsList;
        this.activity = activity;
        this.listener = listener;
    }
    @Override
    public ItemAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row,parent,false);
        final ItemAdapter.CustomViewHolder viewHolder = new ItemAdapter.CustomViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, viewHolder.getPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemAdapter.CustomViewHolder holder, int position) {
        Item item = itemsList.get(position);
        holder.itemName.setText(item.getName());
        holder.price.setText(String.valueOf(item.getPrice()));
    }


    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        protected TextView itemName;
        protected TextView price;
        protected ImageView itemImage;

        public CustomViewHolder(View itemView) {
            super(itemView);
            this.itemName = (TextView) itemView.findViewById(R.id.itemname);
            this.price = (TextView) itemView.findViewById(R.id.price);
            this.itemImage = (ImageView) itemView.findViewById(R.id.itemimage);

        }
    }
}
