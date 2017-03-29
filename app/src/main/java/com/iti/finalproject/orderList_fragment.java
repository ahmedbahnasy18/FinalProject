package com.iti.finalproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class orderList_fragment extends Fragment {

    private List<Order> order;
    private RecyclerView mRecyclerView;
    public MyrecyclerviewAdapter adapter;

    public orderList_fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.home, menu);
        ((SearchView) menu.findItem(R.id.action_search).getActionView())
                .setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        adapter.filter(s);
                        return false;
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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
                Collections.sort(order, new Comparator<Order>() {
                    @Override
                    public int compare(Order item1, Order item2)
                    {
                        return  item1.getStatus() - item2.getStatus();
                    }
                });
                adapter = new MyrecyclerviewAdapter(getActivity(), order, new CustomItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
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
}
