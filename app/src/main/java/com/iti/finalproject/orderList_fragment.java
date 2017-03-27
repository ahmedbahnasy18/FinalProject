package com.iti.finalproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link orderList_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link orderList_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class orderList_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<Order> order;
    Order list1;
    String order_id="1";
    String ordre_name="bataato";
    private RecyclerView mRecyclerView;
    public MyrecyclerviewAdapter adapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public orderList_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment orderList_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static orderList_fragment newInstance(String param1, String param2) {
        orderList_fragment fragment = new orderList_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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

        getActivity().setTitle("Orders");
        View v=inflater.inflate(R.layout.fragment_order_list_fragment, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.Recview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        order = new ArrayList<>();

        DatabaseAdapter.getInstance().getDatabase().getReference("Orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                order.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Order dbOrder = postSnapshot.getValue(Order.class);
                    if (dbOrder.getUserID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                        order.add(dbOrder);
                }
                adapter = new MyrecyclerviewAdapter(getActivity(), order, new CustomItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        //TODO: add orders
                        orderdeatial_frag orderdeatialFrag = orderdeatial_frag.objInstance(order.get(position));
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_home, orderdeatialFrag)
                                .addToBackStack(null)
                                .commit();
                    }
                });
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return v;
    }

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
