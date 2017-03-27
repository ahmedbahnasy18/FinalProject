package com.iti.finalproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        holder.ordernum.setText(String.format(Locale.getDefault(), "#%s",list.getId()));
        holder.orderdate.setText(list.getDate());
        switch (list.getStatus()){
            case Order.STATUS_PENDING:
                holder.orderstatus.setText("Pending");
                holder.imgOrderStatus.setColorFilter(Color.GRAY);
                break;
            case Order.STATUS_CANCELED:
                holder.orderstatus.setText("Canceled");
                holder.imgOrderStatus.setColorFilter(Color.RED);
                break;
            case Order.STATUS_CONFIRMED:
                holder.orderstatus.setText("Confirmed");
                holder.imgOrderStatus.setColorFilter(Color.GREEN);
                break;
            case Order.STATUS_DELIVERED:
                holder.orderstatus.setText("Delivered");
                holder.imgOrderStatus.setColorFilter(Color.BLUE);
                break;
            case Order.STATUS_NOT_DELIVERED:
                holder.orderstatus.setText("Not Delivered");
                holder.imgOrderStatus.setColorFilter(Color.RED);
                break;
        }
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
        protected TextView orderstatus;
        protected ImageView imgOrderStatus;
        public ViewHolder(View itemView) {
            super(itemView);
            ordernum=(TextView)itemView.findViewById(R.id.ordernum_text);
            orderdate=(TextView)itemView.findViewById(R.id.order_date_text);
            orderstatus = (TextView) itemView.findViewById(R.id.order_status_text);
            imgOrderStatus = (ImageView) itemView.findViewById(R.id.img_order_status);


        }
    }

}
