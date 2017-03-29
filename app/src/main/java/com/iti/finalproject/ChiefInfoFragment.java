package com.iti.finalproject;

import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChiefInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChiefInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChiefInfoFragment extends Fragment {

    ImageView cheifImage;
    TextView cheifName;
    RatingBar ratingBar;
    TextView openingHours;
    TextView pickUpAvail;
    TextView deliveryAvail;
    TextView deliversIn;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAMOBJ = "paramObj";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Chief mChief;

    private OnFragmentInteractionListener mListener;

    public ChiefInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChiefInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChiefInfoFragment newInstance(String param1, String param2) {
        ChiefInfoFragment fragment = new ChiefInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mChief = getArguments().getParcelable(ARG_PARAMOBJ);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chief_info, container, false);
        cheifImage = (ImageView) v.findViewById(R.id.cheifimage);
        cheifName = (TextView) v.findViewById(R.id.cheifname);
        ratingBar = (RatingBar) v.findViewById(R.id.rating_bar);


        openingHours = (TextView) v.findViewById(R.id.openinghours);
        pickUpAvail = (TextView) v.findViewById(R.id.pickupavailability);
        deliveryAvail = (TextView) v.findViewById(R.id.deliveryavailability);
        deliversIn = (TextView) v.findViewById(R.id.deliversin);
        //Chief cheif = getIntent().getParcelableExtra("CHEIF_INFO");
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
