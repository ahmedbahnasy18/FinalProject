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

import com.squareup.picasso.Picasso;

public class ChiefInfoFragment extends Fragment {

    ImageView cheifImage;
    TextView cheifName;
    RatingBar ratingBar;
    TextView openingHours;
    TextView pickUpAvail;
    TextView deliveryAvail;
    TextView deliversIn;

    private static final String ARG_PARAMOBJ = "paramObj";

    private Chief mChief;

    public ChiefInfoFragment() {
    }

    public static ChiefInfoFragment objInstance(Chief cheif ) {
        ChiefInfoFragment fragment = new ChiefInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAMOBJ,cheif);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mChief = getArguments().getParcelable(ARG_PARAMOBJ);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chief_info, container, false);
        cheifImage = (ImageView) v.findViewById(R.id.cheifimage);
        cheifName = (TextView) v.findViewById(R.id.cheifname);
        ratingBar = (RatingBar) v.findViewById(R.id.rating_bar);


        openingHours = (TextView) v.findViewById(R.id.openinghours);
        pickUpAvail = (TextView) v.findViewById(R.id.pickupavailability);
        deliveryAvail = (TextView) v.findViewById(R.id.deliveryavailability);
        deliversIn = (TextView) v.findViewById(R.id.deliversin);

        cheifName.setText(mChief.getName());
        if(!TextUtils.isEmpty(mChief.getImage()))
            Picasso.with(getActivity())
                    .load(mChief.getImage())
                    .resize(200,200)
                    .placeholder(R.drawable.animation_loading)
                    .error(R.drawable.blank_chief)
                    .into(cheifImage);
        else
            Picasso.with(getActivity())
                    .load(R.drawable.blank_chief)
                    .into(cheifImage);
        ratingBar.setRating(mChief.getRating());
        openingHours.setText(mChief.getOpenHours());
        if(mChief.isPickUp()){
            pickUpAvail.setText("Available");
        }else{pickUpAvail.setText("Not Available");}
        if(mChief.isDelivery()){
            deliveryAvail.setText("Available");
        }else{deliveryAvail.setText("Not Available");}
        deliversIn.setText(mChief.getDeliverIn());
        getActivity().setTitle(mChief.getName() + " Info");
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(ContextCompat.getColor(getActivity(), android.R.color.darker_gray), PorterDuff.Mode.SRC_ATOP);
        return v;
    }

}
