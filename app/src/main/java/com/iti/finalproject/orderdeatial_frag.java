package com.iti.finalproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link orderdeatial_frag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link orderdeatial_frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class orderdeatial_frag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAMOBJ = "paramObj";

    RatingBar qality_rate;
    RatingBar time_rate;
    RatingBar price_rate;
    RatingBar all_experince;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Order myorder;

    private OnFragmentInteractionListener mListener;

    public orderdeatial_frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment orderdeatial_frag.
     */
    // TODO: Rename and change types and number of parameters
    public static orderdeatial_frag newInstance(String param1, String param2) {
        orderdeatial_frag fragment = new orderdeatial_frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            myorder = getArguments().getParcelable(ARG_PARAMOBJ);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Details");
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_orderdeatial_frag, container, false);
        Float price = null;
        String name=null;
        qality_rate=(RatingBar)v.findViewById(R.id.qualiry_rate);
        time_rate=(RatingBar)v.findViewById(R.id.time_rate);
        price_rate=(RatingBar)v.findViewById(R.id.price_rate);
        all_experince=(RatingBar)v.findViewById(R.id.experince_rate);

        List<Item> results = myorder.getItems();

        Collections.sort(results, new Comparator<Item>() {
            @Override
            public int compare(Item item1, Item item2)
            {
                return  item1.getName().compareTo(item2.getName());
            }
        });
//-------------------------------------------------------------------------------
        TextView txtItems = (TextView) v.findViewById(R.id.itemname_text);
        TextView txtPrice = (TextView) v.findViewById(R.id.price_text);
        TextView txtTotal = (TextView) v.findViewById(R.id.total_text);
        TextView txtQuantity = (TextView) v.findViewById(R.id.quantity_text);
        txtItems.setText(results.get(0).getName());
        txtPrice.setText(String.format("EGP%s", String.valueOf(results.get(0).getPrice())));
        txtQuantity.setText("");
        float Total = 0.0f;
        int count = 1;
        for (int i = 1; i < results.size(); i++){
            if (results.get(i).getName().compareTo(results.get(i-1).getName()) != 0) {
                txtItems.setText(txtItems.getText().toString() + "\n" + results.get(i).getName());
                txtPrice.setText(txtPrice.getText().toString() + "\nEGP" + String.valueOf(results.get(i).getPrice()));
                txtQuantity.setText(txtQuantity.getText().toString() + String.valueOf(count) + "x" + "\n");
                count = 1;
            } else {
                count++;
            }
            Total += results.get(i).getPrice();
        }
        txtQuantity.setText(txtQuantity.getText().toString() + String.valueOf(count) + "x");
        txtTotal.setText(String.format("Total: %.2f", Total));


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
