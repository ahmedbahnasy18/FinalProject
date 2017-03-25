package com.iti.finalproject;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SenderInteface{

    String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().add(R.id.activity_main,new LoginFragment()).commit();

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
    protected void onDestroy() {
        super.onDestroy();


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
