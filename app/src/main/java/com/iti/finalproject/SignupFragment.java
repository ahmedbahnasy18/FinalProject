package com.iti.finalproject;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class SignupFragment extends Fragment {

    public static final int REQUEST_LOCATION_PERMISSION = 154;
    public static final int REQUEST_ENABLE_LOCATION = 5154;
    public static final int REQUEST_PLACES_AUTOCOMPLETE = 4545;

    private EditText name;
    private EditText email;
    private EditText pass;
    private EditText address;
    private EditText phone;
    private Button signup_btn;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    LocationManager locationManager;

    List<Address> addresses = null;


    public SignupFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_signup, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());

        name = (EditText) v.findViewById(R.id.user_name);
        email = (EditText) v.findViewById(R.id.email);
        pass = (EditText) v.findViewById(R.id.pass);
        phone = (EditText) v.findViewById(R.id.phone);
        address = (EditText) v.findViewById(R.id.address);
        signup_btn = (Button) v.findViewById(R.id.signUp_btn);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        v.findViewById(R.id.search_for_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findPlace();
            }
        });

        v.findViewById(R.id.current_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                    } else {
                        requestLocation();
                    }
                }
        });
        return v;
    }

    public void findPlace() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(getActivity());
            startActivityForResult(intent, REQUEST_PLACES_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION && grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
        }
    }

    public void requestLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_ENABLE_LOCATION);
                Toast.makeText(getActivity(), "Please Enable GPS", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.setMessage("Retrieving Your Location...");
                progressDialog.show();
                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 100, mLocationListener);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PLACES_AUTOCOMPLETE && resultCode == RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace(getContext(), data);
            address.setText(place.getAddress());
        } else if (requestCode == REQUEST_ENABLE_LOCATION){
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                requestLocation();
        }
    }

    private void registerUser() {

        name.setError(null);
        email.setError(null);
        pass.setError(null);
        phone.setError(null);
        address.setError(null);

        boolean cancel = false;
        View focusView = null;

        String name_value = name.getText().toString().trim();
        String email_value = email.getText().toString().trim();
        String pass_value = pass.getText().toString();
        final String phone_value = phone.getText().toString().trim();
        final String address_value = address.getText().toString().trim();

        if (TextUtils.isEmpty(address_value)) {
            address.setError("This Field is Required");
            focusView = address;
            cancel = true;
        }

        if (TextUtils.isEmpty(phone_value)) {
            phone.setError("This Field is Required");
            focusView = phone;
            cancel = true;
        } else if (!phone_value.matches("01[120][0-9]{8}")) {
            phone.setError("This Phone Number is Invalid");
            focusView = phone;
            cancel = true;
        }

        if (TextUtils.isEmpty(pass_value)) {
            pass.setError("This Field is Required");
            focusView = pass;
            cancel = true;
        }

        if (TextUtils.isEmpty(email_value)) {
            email.setError("This Field is Required");
            focusView = email;
            cancel = true;
        }

        if (TextUtils.isEmpty(name_value)) {
            name.setError("This Field is Required");
            focusView = name;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email_value, pass_value)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.updateProfile(new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name.getText().toString()).build());
                                DatabaseAdapter.getInstance().addUserInfo(user.getUid(), phone_value, Collections.singletonList(address_value));
                                getActivity().finish();
                                startActivity(new Intent(getContext(), HomeActivity.class));

                                Toast.makeText(getContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();
                                String err = task.getException().getLocalizedMessage();
                                Toast.makeText(getContext(), TextUtils.isEmpty(err) ? "Registration failed" : err, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Geocoder geocoder = new Geocoder(getActivity());
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                int i = 0;
                String address_name = "";
                while (addresses.get(0).getAddressLine(i) != null) {
                    address_name += addresses.get(0).getAddressLine(i++) + ", ";
                    address.setText(address_name);
                    locationManager.removeUpdates(mLocationListener);
                    progressDialog.dismiss();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

}
