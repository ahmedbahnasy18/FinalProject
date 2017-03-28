package com.iti.finalproject;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class Basket_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAMOBJ = "paramObj";
    private static final String ARG_PARAMCHIEF = "paramChief";

    private ArrayList<Item> mItems;
    private Chief mChief;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public Basket_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Basket_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Basket_fragment newInstance(String param1, String param2) {
        Basket_fragment fragment = new Basket_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Basket_fragment objInstance(ArrayList<Item> items, Chief chief) {
        Basket_fragment fragment = new Basket_fragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAMOBJ,items);
        args.putParcelable(ARG_PARAMCHIEF,chief);
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
            mItems = getArguments().getParcelableArrayList(ARG_PARAMOBJ);
            mChief = getArguments().getParcelable(ARG_PARAMCHIEF);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Basket");
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_basket_fragment, container, false);
        final Spinner dropdown = (Spinner)v.findViewById(R.id.spinner1);
        final ArrayList<String> items = new ArrayList<>();
        if(mChief.isDelivery())
            items.add("Delivery");
        if(mChief.isPickUp())
            items.add("PickUp");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        final Spinner dropdown1 = (Spinner)v.findViewById(R.id.spinner2);
        dropdown1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (dropdown1.getItemAtPosition(position).equals("Add New..")) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_home, new UserProfileFragment(), "profile")
                            .addToBackStack(null)
                            .commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        DatabaseAdapter.getInstance().getDatabase()
                .getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Addresses")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> item = new ArrayList<>();
                item.add("Select Address");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    item.add(postSnapshot.getValue(String.class));
                }
                item.add("Add New..");
                ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, item);
                dropdown1.setAdapter(adapter1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        v.findViewById(R.id.checkout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dropdown1.getSelectedItem().toString().equals("Select Address")){
                    new AlertDialog.Builder(getActivity())
                            .setMessage("You need to select an address before placing an order.")
                            .setPositiveButton("Ok", null)
                            .show();
                } else {
                    android.app.AlertDialog.Builder alertdialog = new android.app.AlertDialog.Builder(getActivity());

                    alertdialog.setMessage("You're about to place your order, Do you want to continue");
                    alertdialog.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseAdapter.getInstance().submitOrder(
                                    new Order(
                                            String.format("%9s", String.valueOf((int) Math.round(Math.random() * 999999999))).replace(' ', '0'),
                                            mItems,
                                            items.get(dropdown.getSelectedItemPosition()).equals("Delivery") ? Order.DELIVERY_METHOD_DELIVERY : Order.DELIVERY_METHOD_PICKUP,
                                            Order.STATUS_PENDING,
                                            new SimpleDateFormat("EEEE, MMM dd, yyyy 'At' hh':'mm a", Locale.getDefault()).format(Calendar.getInstance().getTime()),
                                            dropdown1.getSelectedItem().toString(),
                                            false,
                                            mChief.getId(),
                                            FirebaseAuth.getInstance().getCurrentUser().getUid()
                                    )
                            );
                            ((HomeActivity) getActivity()).currentBasket.clear();
                            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }

                    });
                    alertdialog.setNegativeButton("No", null);
                    alertdialog.create();
                    alertdialog.show();
                }
            }
        });

//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        Collections.sort(mItems, new Comparator<Item>() {
            @Override
            public int compare(Item item1, Item item2)
            {
                return  item1.getName().compareTo(item2.getName());
            }
        });

        TextView txtItems = (TextView) v.findViewById(R.id.itemname_text);
        TextView txtPrice = (TextView) v.findViewById(R.id.price_text);
        TextView txtTotal = (TextView) v.findViewById(R.id.total_text);
        TextView txtQuantity = (TextView) v.findViewById(R.id.quantity_text);
        txtItems.setText(mItems.get(0).getName());
        txtPrice.setText(String.format(Locale.getDefault(), "%s LE", String.valueOf(mItems.get(0).getPrice())));
        txtQuantity.setText("");
        float Total = mItems.get(0).getPrice();;
        int count = 1;
        for (int i = 1; i < mItems.size(); i++){
            if (mItems.get(i).getName().compareTo(mItems.get(i-1).getName()) != 0) {
                txtItems.setText(String.format(Locale.getDefault(), "%s\n%s",txtItems.getText().toString(),mItems.get(i).getName()));
                txtPrice.setText(String.format(Locale.getDefault(), "%s\n%.2f LE", txtPrice.getText().toString(), mItems.get(i).getPrice()));
                txtQuantity.setText(String.format(Locale.getDefault(), "%s%dx\n", txtQuantity.getText().toString(), count));
                count = 1;
            } else {
                count++;
            }
            Total += mItems.get(i).getPrice();
        }
        txtQuantity.setText(String.format(Locale.getDefault(), "%s%dx", txtQuantity.getText().toString(), count));
        txtTotal.setText(String.format(Locale.getDefault(),"%.2f LE", Total));
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}
