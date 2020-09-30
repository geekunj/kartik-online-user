package com.kartikonline.user.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.kartikonline.user.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;


public class LoginActivity extends AppCompatActivity {

    private static final String EMAIL = "email";

    private TextInputEditText emailInput;
    private TextInputEditText passInput;
    private TextView forgotPasswordText;
    private TextView signUpText;
    private Button signInButton;
    private Button fbLoginButton;
    private Button googleLoginButton;
    private Toolbar toolbar;

    private String htmlTextForgotPass;
    private String htmlTextSignUp;

    private String userID;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fstore;
    private CallbackManager callbackManager;

    @Override
    protected void onStart() {
        super.onStart();
        signUpText.setEnabled(true);
        FirebaseUser currentUser = fAuth.getCurrentUser();
        if(currentUser != null) {
            userID = fAuth.getCurrentUser().getUid();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        callbackManager = CallbackManager.Factory.create();

        fbLoginButton = findViewById(R.id.btn_facebook_login);
        emailInput = findViewById(R.id.et_email_input);
        passInput = findViewById(R.id.et_password_input);
        signInButton = findViewById(R.id.btn_sign_in);
        signUpText = findViewById(R.id.tv_sign_up_text);
        forgotPasswordText = findViewById(R.id.tv_forgot_pass_text);
        toolbar = findViewById(R.id.toolbar);

        initActionBar();

        htmlTextForgotPass = "<u>Forgot Password?</u>";
        htmlTextSignUp = "<u>Sign up now</u>";

        signUpText.setText(Html.fromHtml(htmlTextSignUp));

        fAuth= FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        //fbLoginButton.setReadPermissions(Arrays.asList(EMAIL));
        LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_ONLY);

        fbLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,Arrays.asList(EMAIL));
                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }

                            @Override
                            public void onCancel() {
                                // App code
                            }

                            @Override
                            public void onError(FacebookException exception) {
                                // App code
                            }
                        });
            }
        });




        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                String password = passInput.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    emailInput.setError("email is required");
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    passInput.setError("password is required");
                    return;
                } else if (password.length() < 6) {
                    passInput.setError("password must be greater than 6");
                    return;
                } else {
                    fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                signInButton.setClickable(false);
                                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Register user first", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
        });

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpText.setEnabled(false);
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }*/

    public void initActionBar(){
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Login");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}
