package com.nhatminh.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin, btnRegister;
    EditText etUsername, etPassword;
    ProgressBar pbSignInProgress;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private static final String ERROR_USER_NOT_FOUND = "ERROR_USER_NOT_FOUND";
    private static final String ERROR_WRONG_PASSWORD = "ERROR_WRONG_PASSWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        btnLogin = findViewById(R.id.btnLogin);
        etUsername = findViewById(R.id.etUsername);
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
                            Intent intent = new Intent(LoginActivity.this, ChatRoom.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            String errorCode="";
                            Exception authFail = task.getException();
                            if (authFail instanceof FirebaseAuthException) {
                                errorCode = ((FirebaseAuthException) authFail).getErrorCode();
                            }

                            switch(errorCode)
                            {
                                case ERROR_USER_NOT_FOUND:
                                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_auth_error_user_not_found), Toast.LENGTH_SHORT).show();
                                    break;
                                case ERROR_WRONG_PASSWORD:
                                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_auth_error_wrong_password), Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(LoginActivity.this, errorCode, Toast.LENGTH_SHORT).show();
                                    break;
                            }
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
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (username.isEmpty()) {
                    etUsername.setError(getResources().getString(R.string.can_not_be_empty));
                    return;
                }
                if (password.isEmpty()) {
                    etPassword.setError(getResources().getString(R.string.can_not_be_empty));
                    return;
                }

                //For testing, handle later
                signIn("123@gmail.com", "123456");
                break;
        }

    }
}
