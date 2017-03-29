package com.iti.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ChiefItemsFragment extends Fragment {

    private static final int ITEM_REQCODE = 1;

    private List<Item> itemsList;
    private RecyclerView recyclerView;
    private ItemAdapter iAdapter;
    FloatingActionButton floatingActionButton;

    TextView basketNo;

    private static final String ARG_PARAMOBJ = "paramObj";

    private Chief mChief;

    public ChiefItemsFragment() {
    }


    public static ChiefItemsFragment objInstance(Chief cheif ) {
        ChiefItemsFragment fragment = new ChiefItemsFragment();
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
        inflater.inflate(R.menu.mainmenu, menu);
        ((SearchView) menu.findItem(R.id.action_search).getActionView())
                .setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        iAdapter.filter(s);
                        return false;
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ITEM_REQCODE) {
            if(resultCode == RESULT_OK){
                getView().findViewById(R.id.basket_button_layout).setVisibility(View.VISIBLE);
                basketNo.setText(String.valueOf(Integer.parseInt(basketNo.getText().toString()) + data.getIntExtra("QUANTITY",0)));
                for(int i = 0; i < data.getIntExtra("QUANTITY",0); i++){
                    ((HomeActivity) getActivity()).currentBasket.add((Item) data.getParcelableExtra("RETURN_ITEM"));
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(mChief.getName());
        View v = inflater.inflate(R.layout.fragment_chief_items, container, false);
        floatingActionButton = (FloatingActionButton) v.findViewById(R.id.fab);
        basketNo = (TextView) v.findViewById(R.id.basket_no);
        basketNo.setText(String.valueOf(((HomeActivity) getActivity()).currentBasket.size()));
        if(((HomeActivity) getActivity()).currentBasket.size() > 0){
            v.findViewById(R.id.basket_button_layout).setVisibility(View.VISIBLE);
        }
        ((HomeActivity) getActivity()).isItemsFragmentVisible = true;
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Basket_fragment basket_fragment = Basket_fragment.objInstance(((ArrayList<Item>) ((HomeActivity) getActivity()).currentBasket), mChief);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_home,basket_fragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        itemsList = new ArrayList<>(mChief.getMenuItems());
        iAdapter = new ItemAdapter(getActivity(), itemsList, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {


                Intent i = new Intent(getActivity(),BasketDialogActivity.class);
                i.putExtra("ITEM_INTENT",itemsList.get(position));
                startActivityForResult(i,ITEM_REQCODE);
            }
        });
        recyclerView.setAdapter(iAdapter);

        return v;
    }

    @Override
    public void onStop() {
        ((HomeActivity) getActivity()).isItemsFragmentVisible = false;
        super.onStop();
    }

}
