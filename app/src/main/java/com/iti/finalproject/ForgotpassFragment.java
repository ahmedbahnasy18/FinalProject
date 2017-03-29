package com.iti.finalproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class ForgotpassFragment extends Fragment {

    EditText enter_mail;
    Button reset_btn;

    FirebaseAuth firebaseAuth;

    public ForgotpassFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View v     =  inflater.inflate(R.layout.fragment_forgotpass, container, false);

        enter_mail = (EditText) v.findViewById(R.id.email);
        reset_btn  = (Button) v.findViewById(R.id.reset_pass);

        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(enter_mail.getText().toString())){
                    Toast.makeText(getContext(),"You need to enter an Email address",Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseAuth.getInstance().sendPasswordResetEmail(enter_mail.getText().toString());
                Toast.makeText(getContext(),"An Email has been sent to the above email address with further instructions",Toast.LENGTH_LONG).show();

            }
        });

        return v;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String text = getArguments().getString("e_mail");
        enter_mail.setText(text);

    }
}
