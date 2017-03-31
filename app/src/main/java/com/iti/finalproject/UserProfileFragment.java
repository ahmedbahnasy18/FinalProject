package com.iti.finalproject;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class UserProfileFragment extends Fragment {

    private final static int RQST_PLACES_AUTOCOMPLETE = 555;

    private final int MAX_ADDRESSES = 5;

    private LinearLayout mAddressesLayout;
    private ArrayList<EditText> mAddressEditTexts;

    private EditText txtName;
    private EditText txtPhone;

    private FirebaseUser mUser;

    public UserProfileFragment() {
        mAddressEditTexts = new ArrayList<>();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.save, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_save){
            ArrayList<String> addresses = new ArrayList<>();

            txtName.setError(null);
            txtPhone.setError(null);

            boolean cancel = false;
            View focusView = null;

            if (TextUtils.isEmpty(txtPhone.getText().toString())){
                cancel = true;
                txtPhone.setError("This field is required!");
                focusView = txtPhone;
            } else if (!txtPhone.getText().toString().matches("01[120][0-9]{8}")){
                txtPhone.setError("This Phone Number is Invalid");
                focusView = txtPhone;
                cancel = true;
            }

            if (TextUtils.isEmpty(txtName.getText().toString())){
                cancel = true;
                txtName.setError("This field is required!");
                focusView = txtName;
            }

            if (mAddressEditTexts.size() == 0){
                cancel = true;
                focusView = txtPhone;
                new AlertDialog.Builder(getActivity())
                        .setMessage("You need to have at least one address," +
                                " Please add a new address to complete your profile.")
                        .setPositiveButton("Ok", null)
                        .show();
            }

            if (cancel){

                focusView.requestFocus();

            } else {

                for (EditText address : mAddressEditTexts) {
                    if (!TextUtils.isEmpty(address.getText().toString()))
                        addresses.add(address.getText().toString());
                }

                mUser.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(txtName.getText().toString()).build());

                DatabaseAdapter.getInstance().addUserInfo(mUser.getUid(), txtPhone.getText().toString(), addresses);

                Toast.makeText(getActivity(), "Saved...", Toast.LENGTH_SHORT).show();

                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_profile, container, false);

        getActivity().setTitle("Profile");

        mAddressesLayout = (LinearLayout) v.findViewById(R.id.addresses_layout);

        txtName = (EditText) v.findViewById(R.id.txt_name);
        txtPhone = (EditText) v.findViewById(R.id.txt_phone);

        txtName.setText(mUser.getDisplayName());

        v.findViewById(R.id.btn_add_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAddressEditTexts.size() == MAX_ADDRESSES) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage("Sorry but you can't have more than " + MAX_ADDRESSES + " addresses at the same time," +
                                    " Please delete one of your addresses before adding a new one.")
                            .setPositiveButton("Ok", null)
                            .show();
                } else {
                    try {
                        Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(getActivity());
                        startActivityForResult(intent, RQST_PLACES_AUTOCOMPLETE);
                    } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                        new AlertDialog.Builder(getActivity())
                                .setMessage(e.getLocalizedMessage())
                                .setPositiveButton("Ok", null)
                                .show();
                    }
                }
            }
        });

        DatabaseAdapter
                .getInstance()
                .getDatabase()
                .getReference("Users")
                .child(mUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (getActivity() != null) {
                            mAddressEditTexts.clear();
                            mAddressesLayout.removeAllViews();
                            txtPhone.setText(dataSnapshot.child("Phone").getValue(String.class));
                            for (DataSnapshot dataSnapshotAddress : dataSnapshot.child("Addresses").getChildren()) {
                                addNewAddress(dataSnapshotAddress.getValue(String.class));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RQST_PLACES_AUTOCOMPLETE) {
            Place place = PlaceAutocomplete.getPlace(getContext(), data);
            addNewAddress(place.getAddress().toString());
        }
    }

    private void addNewAddress(String address) {
        if (mAddressEditTexts.size() < MAX_ADDRESSES) {
            final LinearLayout linearLayout = new LinearLayout(getApplicationContext());

            final EditText txtAddress = new EditText(getApplicationContext());
            txtAddress.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.85f));
            txtAddress.setMaxLines(1);
            txtAddress.setTextColor(Color.BLACK);
            txtAddress.setInputType(InputType.TYPE_NULL);
            txtAddress.setText(address);

            Button button = new Button(new ContextThemeWrapper(getApplicationContext(), R.style.Button));
            button.setText("-");
            button.setTextColor(Color.WHITE);
            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.15f);
            buttonParams.bottomMargin = 5;
            //noinspection ResourceType
            button.setPadding(0,0,0,-20);
            button.setLayoutParams(buttonParams);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAddressEditTexts.size() > 1) {
                        new AlertDialog.Builder(getActivity())
                                .setMessage("Are you sure?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mAddressesLayout.removeView(linearLayout);
                                        mAddressEditTexts.remove(txtAddress);
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    } else {
                        new AlertDialog.Builder(getActivity())
                                .setMessage("You need to have at least one address," +
                                        " Please add a new address before deleting this one.")
                                .setPositiveButton("Ok", null)
                                .show();
                    }
                }
            });
            linearLayout.addView(txtAddress);
            linearLayout.addView(button);
            mAddressesLayout.addView(linearLayout);
            mAddressEditTexts.add(txtAddress);
        }
    }

}
