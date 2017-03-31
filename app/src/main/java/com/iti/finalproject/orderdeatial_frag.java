package com.iti.finalproject;

import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class orderdeatial_frag extends Fragment {
    private static final String ARG_PARAMOBJ = "paramObj";

    RatingBar qality_rate;
    RatingBar time_rate;
    RatingBar price_rate;
    RatingBar all_experince;

    Order myorder;

    public orderdeatial_frag() {
    }

    public static orderdeatial_frag objInstance( Order ORDER) {
        orderdeatial_frag fragment = new orderdeatial_frag();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAMOBJ,  ORDER);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            myorder = getArguments().getParcelable(ARG_PARAMOBJ);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Details");
        View v=inflater.inflate(R.layout.fragment_orderdeatial_frag, container, false);
        final TextView txtChiefName = (TextView) v.findViewById(R.id.cheifname);
        final RatingBar ratingBar = (RatingBar) v.findViewById(R.id.rating_bar);
        final ImageView imgChiefImage = (ImageView) v.findViewById(R.id.cheifimage);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(ContextCompat.getColor(getActivity(), android.R.color.darker_gray), PorterDuff.Mode.SRC_ATOP);
        DatabaseAdapter
                .getInstance()
                .getDatabase()
                .getReference("Chiefs")
                .child(myorder.getChiefID())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("name").getValue(String.class) != null)
                            txtChiefName.setText(dataSnapshot.child("name").getValue(String.class));
                        if (dataSnapshot.child("rating").getValue(Float.class) != null)
                            ratingBar.setRating(dataSnapshot.child("rating").getValue(Float.class));

                        String imageURL = dataSnapshot.child("image").getValue(String.class);
                        if(imageURL != null && !TextUtils.isEmpty(imageURL))
                            Picasso
                                    .with(getActivity())
                                    .load(imageURL)
                                    .resize(200,200)
                                    .placeholder(R.drawable.animation_loading)
                                    .error(R.drawable.blank_chief)
                                    .into(imgChiefImage);
                        else
                            Picasso
                                    .with(getActivity())
                                    .load(R.drawable.blank_chief)
                                    .into(imgChiefImage);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        final TextView txtRatingMessage = (TextView) v.findViewById(R.id.rating_message);

        if(myorder.getStatus() == Order.STATUS_DELIVERED && !myorder.isRated()){
            v.findViewById(R.id.rating_layout).setVisibility(View.VISIBLE);
        } else if (myorder.getStatus() != Order.STATUS_DELIVERED){
            txtRatingMessage.setVisibility(View.VISIBLE);
            txtRatingMessage.setText("Sorry, but you can only rate the order once it has been delivered!");
        } else if (myorder.isRated()){
            txtRatingMessage.setVisibility(View.VISIBLE);
            txtRatingMessage.setText("You've already rated this order, Thank you!");
        }

        qality_rate=(RatingBar)v.findViewById(R.id.qualiry_rate);
        time_rate=(RatingBar)v.findViewById(R.id.time_rate);
        price_rate=(RatingBar)v.findViewById(R.id.price_rate);
        all_experince=(RatingBar)v.findViewById(R.id.experince_rate);

        List<Item> results = myorder.getItems();


        v.findViewById(R.id.btn_submit_rating).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = (qality_rate.getRating() * 0.25f);
                rating += (time_rate.getRating() * 0.25);
                rating += (price_rate.getRating() * 0.25);
                rating += (all_experince.getRating() * 0.25);
                DatabaseAdapter.getInstance().addChiefRating(myorder.getChiefID(),myorder.getId(), rating);
                getView().findViewById(R.id.rating_layout).setVisibility(View.GONE);
                txtRatingMessage.setVisibility(View.VISIBLE);
                txtRatingMessage.setText("Thank you!");
            }
        });

        Collections.sort(results, new Comparator<Item>() {
            @Override
            public int compare(Item item1, Item item2)
            {
                return  item1.getName().compareTo(item2.getName());
            }
        });
        TextView txtOrderNumber = (TextView) v.findViewById(R.id.txt_order_number);
        TextView txtItems = (TextView) v.findViewById(R.id.itemname_text);
        TextView txtPrice = (TextView) v.findViewById(R.id.price_text);
        TextView txtTotal = (TextView) v.findViewById(R.id.total_text);
        TextView txtQuantity = (TextView) v.findViewById(R.id.quantity_text);
        txtOrderNumber.setText(String.format(Locale.getDefault(), "#%s", myorder.getId()));
        txtItems.setText(results.get(0).getName());
        txtPrice.setText(String.format(Locale.getDefault(), "%s LE", String.valueOf(results.get(0).getPrice())));
        txtQuantity.setText("");
        float Total = results.get(0).getPrice();
        int count = 1;
        for (int i = 1; i < results.size(); i++){
            if (results.get(i).getName().compareTo(results.get(i-1).getName()) != 0) {
                txtItems.setText(String.format(Locale.getDefault(), "%s\n%s",txtItems.getText().toString(),results.get(i).getName()));
                txtPrice.setText(String.format(Locale.getDefault(), "%s\n%.2f LE", txtPrice.getText().toString(), results.get(i).getPrice()));
                txtQuantity.setText(String.format(Locale.getDefault(), "%s%dx\n", txtQuantity.getText().toString(), count));
                count = 1;
            } else {
                count++;
            }
            Total += results.get(i).getPrice();
        }
        txtQuantity.setText(String.format(Locale.getDefault(), "%s%dx", txtQuantity.getText().toString(), count));
        txtTotal.setText(String.format(Locale.getDefault(),"%.2f LE", Total));
        return v;
    }
}
