package com.kartikonline.user.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kartikonline.user.R;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText emailInput;
    private TextInputEditText passInput;
    private TextInputEditText confirmPassInput;
    private Toolbar toolbar;

    private CheckBox agreeToTermsCheck;

    private TextView signInText;
    private Button signUpButton;

    private String htmlTextForgotPass;
    private String htmlTextSignUp;

    String userID, email, confirmPassword, password;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fstore;
    private FirebaseUser currentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar = findViewById(R.id.toolbar);
        emailInput = findViewById(R.id.et_email_input);
        passInput = findViewById(R.id.et_password_input);
        confirmPassInput = findViewById(R.id.et_confirm_pass_input);
        signUpButton = findViewById(R.id.btn_sign_up);
        signInText = findViewById(R.id.tv_sign_in_text);
        agreeToTermsCheck = (CheckBox) findViewById(R.id.check_terms_and_condtions);

        initActionBar();


        htmlTextSignUp = "<u>Sign in now</u>";

        signInText.setText(Html.fromHtml(htmlTextSignUp));

        fAuth= FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        currentUser = fAuth.getCurrentUser();

        if(currentUser != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        passInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length() < 6){
                    passInput.setError(" Password must be greater than 6 characters");
                }


            }
        });


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailInput.getText().toString().trim();
                password = passInput.getText().toString().trim();
                confirmPassword = confirmPassInput.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    emailInput.setError("email is required");
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    passInput.setError("password is required");
                    return;
                } else if (password.length() < 6) {
                    passInput.setError("password must be greater than 6");
                    return;
                } else if(!password.equals(confirmPassword)) {
                    confirmPassInput.setError("passwords don't match");
                } else if(!agreeToTermsCheck.isChecked()){
                    agreeToTermsCheck.setError("please agree to the terms to sign in");
                } else {
                    fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                signUpButton.setEnabled(false);
                                Toast.makeText(RegisterActivity.this, "User Created in Auth .", Toast.LENGTH_SHORT).show();
                                userID = fAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fstore.collection("wholesaleusers").document(userID);
                                HashMap<String, Object> user = new HashMap<>();
                                user.put("userEmail", email);
                                user.put("password", password);
                                //user.put("lastActive", )
                                documentReference.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(Task<Void> task) {

                                        Toast.makeText(RegisterActivity.this, "User set in user table ", Toast.LENGTH_SHORT).show();

                                    }
                                });

                                startActivity(new Intent(getApplicationContext(), RegisterFormActivity.class));
                                finish();

                            } else {

                                Toast.makeText(RegisterActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

                }

            }
        });

        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInText.setEnabled(false);
                onBackPressed();
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void initActionBar(){
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Register");
        }
    }

}
