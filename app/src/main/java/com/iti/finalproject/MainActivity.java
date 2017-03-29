package com.iti.finalproject;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements SenderInteface{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().add(R.id.activity_main,new LoginFragment()).commit();

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                }
            }
        });
    }

    @Override
    public void SignUpFragment() {

        SignupFragment signUpFragment = new SignupFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main,signUpFragment).addToBackStack("").commit();

    }

    @Override
    public void ForgotPassFragment(String user_mail) {

        ForgotpassFragment forgotpassFragment = new ForgotpassFragment();
        Bundle bundle = new Bundle();
        bundle.putString("e_mail",user_mail);
        forgotpassFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_main,forgotpassFragment).addToBackStack("").commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
    }

}
