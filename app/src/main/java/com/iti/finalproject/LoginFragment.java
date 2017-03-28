package com.iti.finalproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.List;


public class LoginFragment extends Fragment implements View.OnClickListener {


    public LoginFragment() {}
    SenderInteface senderInteface;

    private final static String TAG = "test2";

    private EditText email;
    private EditText pass;
    private Button   signin_btn;
    private TextView signUp;
    private TextView forgetPass;

    FirebaseAuth   firebaseAuth;
    ProgressDialog progressDialog;
    private CallbackManager mCallbackManager;

    private GoogleApiClient mGoogleApiClient;
    private SignInButton    signInButton;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        View v      =  inflater.inflate(R.layout.fragment_login, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();

        email       = (EditText) v.findViewById(R.id.email);
        pass        = (EditText) v.findViewById(R.id.pass);
        signin_btn  = (Button)   v.findViewById(R.id.login);
        signUp      = (TextView) v.findViewById(R.id.sign_up);
        forgetPass  = (TextView) v.findViewById(R.id.forgot_pass);
        Button btnFacebookLogin = (Button) v.findViewById(R.id.login_button);

        progressDialog = new ProgressDialog(getContext());

        signin_btn.setOnClickListener(this);
        forgetPass.setOnClickListener(this);

        //-------------------google----------------------

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton = (SignInButton) v.findViewById(R.id.google_btn);
        signInButton.setOnClickListener(this);

        TextView googleButtonText = (TextView) signInButton.getChildAt(0);
        googleButtonText.setTypeface(Typeface.create("sans-serif-medium", android.R.style.TextAppearance_DeviceDefault_Widget));
        googleButtonText.setText("Google");
        googleButtonText.setTextSize(15);
        googleButtonText.setTextColor(Color.BLACK);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //Toast.makeText(getActivity(), user.getDisplayName(), Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(getContext(),HomeActivity.class);
                    startActivity(i);
                    getActivity().finish();
                }
            }
        };
        //------------------------------------------------------
        btnFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> permissions = Arrays.asList("email", "public_profile");
                LoginManager loginManager = LoginManager.getInstance();
                loginManager.logInWithReadPermissions(LoginFragment.this, permissions);
                loginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                progressDialog.setMessage("Log In..........");
                                progressDialog.show();
                                firebaseAuthWithFacebook(loginResult.getAccessToken());
                            }

                            @Override
                            public void onCancel() {
                            }

                            @Override
                            public void onError(FacebookException exception) {
                                Toast.makeText(getActivity(), exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });
        return v;
    }

    private void firebaseAuthWithFacebook(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        senderInteface = (SenderInteface) getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                senderInteface.SignUpFragment();
            }
        });
        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                senderInteface.ForgotPassFragment(email.getText().toString());
            }
        });

    }




    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("email",email.getText().toString());

    }

    @Override
    public void onClick(View view) {
            //---login button--------
            if (view == signin_btn){
                userLogin();
            }

           //----google---------
            if(view == signInButton){
                signIn();
            }

        }

    //------------mail , pass-----------
    private void userLogin() {

        String email_value = email.getText().toString().trim();
        String pass_value  = pass.getText().toString().trim();

        if (TextUtils.isEmpty(email_value)){
            //email is empty
            Toast.makeText(getContext(),"pleas enter email",Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(pass_value)){
            //email is empty
            Toast.makeText(getContext(),"pleas enter pass",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Log In..........");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email_value,pass_value)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    //-----------google--------------------
    private void signIn() {
        progressDialog.setMessage("Log In..........");
        progressDialog.show();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 2017);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 2017) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    GoogleSignInAccount account = result.getSignInAccount();
                    firebaseAuthWithGoogle(account);
                } else {
                    Snackbar.make(signin_btn,"Failed",Snackbar.LENGTH_LONG).show();
                }
            }
        }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getContext(), "Authentication failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
