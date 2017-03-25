package com.iti.finalproject;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Soft on 3/21/2017.
 */

public class MyrecyclerviewAdapter extends RecyclerView.Adapter<MyrecyclerviewAdapter.ViewHolder>  {

    private Context mContext;
    private List<Order> orderlist;
    private CustomItemClickListener listener;

    public MyrecyclerviewAdapter(Context Context,List<Order> orderlist,CustomItemClickListener onItemClickListener){
        this.mContext=Context;
        this.orderlist=orderlist;
        this.listener=onItemClickListener;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Order list=orderlist.get(holder.getAdapterPosition());
        holder.ordernum.setText(list.getId());
        holder.orderdate.setText(list.getDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v,holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView ordernum;
        protected TextView orderdate;
        public ViewHolder(View itemView) {
            super(itemView);
            ordernum=(TextView)itemView.findViewById(R.id.ordernum_text);
            orderdate=(TextView)itemView.findViewById(R.id.order_date_text);



        }
    }

}
