package com.iti.finalproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import static com.facebook.FacebookSdk.getApplicationContext;


public class SignupFragment extends Fragment {

    private EditText name;
    private EditText email;
    private EditText pass;
    private EditText address;
    private EditText phone;
    private Button signup_btn;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    LocationManager locationManager;
    private Intent gpsSettingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

    double lat;
    double longti;
    List<Address> addresses = null;

    locationlis mLocationListener;


    public SignupFragment() {}



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_signup, container, false);

        mLocationListener = new locationlis();

        firebaseAuth    = FirebaseAuth.getInstance();
        progressDialog  = new ProgressDialog(getActivity());

        name            = (EditText) v.findViewById(R.id.user_name);
        email           = (EditText) v.findViewById(R.id.email);
        pass            = (EditText) v.findViewById(R.id.pass);
        phone           = (EditText) v.findViewById(R.id.phone);
        address         = (EditText) v.findViewById(R.id.address);
        signup_btn      = (Button)   v.findViewById(R.id.signUp_btn);

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

                //---------------GPS permission-------------
                progressDialog.setMessage("loading location..........");
                progressDialog.show();
                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
                if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                    startActivity(gpsSettingsIntent);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, mLocationListener);

            }
        });


        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }

    //  ------------------to open google API autocomplet -----------------*/
    public void findPlace() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(getActivity());
            startActivityForResult(intent, 1);
        }
            catch (GooglePlayServicesRepairableException e) {

        }   catch (GooglePlayServicesNotAvailableException e) {

        }
    }

    // A place has been received; use requestCode to track the request.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getContext(), data);
                address.setText(place.getAddress());
            }
        }
    }

    private void registerUser () {

        name.setError(null);
        email.setError(null);
        pass.setError(null);
        phone.setError(null);
        address.setError(null);

        boolean cancel = false;

        String name_value = name.getText().toString().trim();
        String email_value = email.getText().toString().trim();
        String pass_value  = pass.getText().toString();
        final String phone_value = phone.getText().toString().trim();
        final String address_value = address.getText().toString().trim();

        if (TextUtils.isEmpty(name_value)){
            name.setError("This Field is Required");
            cancel = true;
        }
        if (TextUtils.isEmpty(email_value)){
            //email is empty
            email.setError("This Field is Required");
            cancel = true;
        }

        if (TextUtils.isEmpty(pass_value)){
            //email is empty
            pass.setError("This Field is Required");
            cancel = true;
        }

        if (TextUtils.isEmpty(phone_value)){
            phone.setError("This Field is Required");
            cancel = true;
        }

        if(TextUtils.isEmpty(address_value)){
            address.setError("This Field is Required");
            cancel = true;
        }

        if (!cancel) {

            progressDialog.setMessage("Registering User..........");
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
                                Toast.makeText(getContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    class locationlis implements LocationListener {

        Geocoder geocoder = new Geocoder(getApplicationContext());

        @Override
        public void onLocationChanged(Location location) {

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
    }

}
