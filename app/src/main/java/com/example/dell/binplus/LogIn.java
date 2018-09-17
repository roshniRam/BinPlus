package com.example.dell.binplus;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.android.gms.auth.api.Auth;


public class LogIn extends AppCompatActivity {

//    private EditText inputEmail, inputPassword;
//    private FirebaseAuth auth;
//    private ProgressBar progressBar;
//    private Button btnSignup, btnLogin, btnReset;

    EditText loginEmail,loginPassword;
    Button loginButton,signUp,newPassButton;
    private static final int RC_SIGN_IN = 9001;
    private SignInButton signInButton;
    FirebaseAuth firebaseAuth;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

//        //Get Firebase auth instance
//        auth = FirebaseAuth.getInstance();
//
//        if (auth.getCurrentUser() != null) {
//            startActivity(new Intent(LogIn.this, MainActivity.class));
//            finish();
//        }
//
//        inputEmail = findViewById(R.id.email);
//        inputPassword = findViewById(R.id.password);
//        progressBar = findViewById(R.id.progressBar);
//        btnSignup = findViewById(R.id.btn_signup);
//        btnLogin = findViewById(R.id.btn_login);
//        btnReset = findViewById(R.id.btn_reset_password);
//
//        //Get Firebase auth instance
//        auth = FirebaseAuth.getInstance();
//
//        btnSignup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LogIn.this, SignUp.class));
//            }
//        });
//
//        btnReset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LogIn.this, ResetPasswrodActivity.class));
//            }
//        });
//
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email = inputEmail.getText().toString();
//                final String password = inputPassword.getText().toString();
//
//                if (TextUtils.isEmpty(email)) {
//                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (TextUtils.isEmpty(password)) {
//                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                progressBar.setVisibility(View.VISIBLE);
//
//                //authenticate user
//                auth.signInWithEmailAndPassword(email, password)
//                        .addOnCompleteListener(LogIn.this, new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                // If sign in fails, display a message to the user. If sign in succeeds
//                                // the auth state listener will be notified and logic to handle the
//                                // signed in user can be handled in the listener.
//                                progressBar.setVisibility(View.GONE);
//                                if (!task.isSuccessful()) {
//                                    // there was an error
//                                    if (password.length() < 6) {
//                                        inputPassword.setError(getString(R.string.minimum_password));
//                                    } else {
//                                        Toast.makeText(LogIn.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
//                                    }
//                                } else {
//                                    Intent intent = new Intent(LogIn.this, MainActivity.class);
//                                    startActivity(intent);
//                                    finish();
//                                }
//                            }
//                        });
//            }
//        });

        loginEmail = (EditText) findViewById(R.id.email);
        loginPassword = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.loginButton);
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        newPassButton = (Button) findViewById(R.id.forgotpass);
        signUp = (Button) findViewById(R.id.signUp);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogIn.this, SignUp.class));
            }
        });

        newPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogIn.this, ResetPasswrodActivity.class));
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        if(firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }



    }

    private void signIn() {
        Intent signIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signIntent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                authWithGoogle(account);
            }
        }
    }

    private void authWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Auth Error",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
