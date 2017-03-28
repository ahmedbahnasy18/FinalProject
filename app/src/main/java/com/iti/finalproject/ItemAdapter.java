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
import java.util.Locale;

/**
 * Created by Ahmed on 21/03/2017.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.CustomViewHolder>{

    private List<Item> itemsList, fullList;
    private Activity activity;
    CustomItemClickListener listener;

    public ItemAdapter(Activity activity, List<Item> itemsList, CustomItemClickListener listener) {
        this.itemsList = itemsList;
        this.fullList = new ArrayList<>(itemsList);
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
                listener.onItemClick(v, viewHolder.getAdapterPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemAdapter.CustomViewHolder holder, int position) {
        Item item = itemsList.get(position);
        holder.itemName.setText(item.getName());
        holder.price.setText(String.format(Locale.getDefault(), "%.2f LE", item.getPrice()));
    }

    void filter(final String filter) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                itemsList.clear();
                if (TextUtils.isEmpty(filter))
                    itemsList.addAll(fullList);
                else
                    for (Item item : fullList)
                        if (item.getName().toLowerCase().contains(filter.toLowerCase()))
                            itemsList.add(item);

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
