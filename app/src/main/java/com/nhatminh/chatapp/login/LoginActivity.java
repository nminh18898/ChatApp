package com.nhatminh.chatapp.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.nhatminh.chatapp.R;
import com.nhatminh.chatapp.home.HomeActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin, btnRegister;
    EditText etEmail, etPassword;
    ProgressBar pbSignInProgress;

    private FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        btnLogin = findViewById(R.id.btnLogin);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        pbSignInProgress = findViewById(R.id.pbSignInProgress);

        mFirebaseAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

    }

    /**
     * Login authentication with email and password
     */
    private void signIn(String email, String password) {
        pbSignInProgress.setVisibility(View.VISIBLE);
        pbSignInProgress.invalidate();
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pbSignInProgress.setVisibility(View.GONE);
                        if(task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            FireBaseAuthError fireBaseAuthError = new FireBaseAuthError(LoginActivity.this, task);
                            fireBaseAuthError.showError();
                        }
                        pbSignInProgress.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnLogin:
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                 if (!isEmailAndPasswordEmpty(email,password)) {
                    signIn(email, password);
                }

                break;

            case R.id.btnRegister:
                Intent toSignUpActivity = new Intent(this, SignUpActivity.class);
                startActivity(toSignUpActivity);
                break;
        }

    }

    boolean isEmailAndPasswordEmpty(String email, String password)
    {
        if (email.isEmpty()) {
            etEmail.setError(getResources().getString(R.string.can_not_be_empty));
            return true;
        }
        if (password.isEmpty()) {
            etPassword.setError(getResources().getString(R.string.can_not_be_empty));
            return true;
        }
        return false;
    }
}
