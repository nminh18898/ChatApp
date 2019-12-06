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

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etEmail;
    EditText etPassword;
    EditText etConfirmPassword;
    Button btnCreateAccount;
    private FirebaseAuth mFirebaseAuth;
    ProgressBar pbSignUpProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        pbSignUpProgress = findViewById(R.id.pbSignUpProgress);

        btnCreateAccount.setOnClickListener(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnCreateAccount:
                registerUser();

                break;
        }

    }

    /**
     * Create new user in database, then go to FillInformationActivity if success or show
     * error why fail to register new user.
     *
     */


    private void registerUser()
    {

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();


        if(!isInputValid(email, password, confirmPassword))
        {
            return;
        }

        pbSignUpProgress.setVisibility(View.VISIBLE);
        pbSignUpProgress.invalidate();

        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    Intent toUserInformationForm = new Intent(SignUpActivity.this, UserInformationSignUpForm.class);
                    startActivity(toUserInformationForm);

                }
                else
                {
                    FireBaseAuthError fireBaseAuthError = new FireBaseAuthError(SignUpActivity.this, task);
                    fireBaseAuthError.showError();
                }
                pbSignUpProgress.setVisibility(View.GONE);

            }
        });




    }


    /**
     * Check if user input is valid (include: input not empty, password and confirm password must match)
     */
    private boolean isInputValid(String email, String password, String confirmPassword)
    {
        if (email.isEmpty()) {
            etEmail.setError(getResources().getString(R.string.can_not_be_empty));
            return false;
        }
        if (password.isEmpty()) {
            etPassword.setError(getResources().getString(R.string.can_not_be_empty));
            return false;
        }
        if(confirmPassword.isEmpty())
        {
            etConfirmPassword.setError(getResources().getString(R.string.can_not_be_empty));
            return false;
        }

        if(!password.equals(confirmPassword))
        {
            etConfirmPassword.setError(getResources().getString(R.string.your_password_and_confirm_password_do_not_match));
            return false;
        }

        return true;

    }


}
