package com.nhatminh.chatapp;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthException;

public class FireBaseAuthError {
    private Context context;
    private Task<AuthResult> task;


    public enum AuthError {
        ERROR_USER_NOT_FOUND,
        ERROR_WRONG_PASSWORD,
        EMAIL_TAKEN,
        ERROR_WEAK_PASSWORD,
        ERROR_INVALID_EMAIL

    }

    public FireBaseAuthError(Context context, Task<AuthResult> task)
    {
        this.context = context;
        this.task = task;
    }

    public void showError()
    {
        String errorCode="";
        Exception authFail = task.getException();
        if (authFail instanceof FirebaseAuthException) {
            errorCode = ((FirebaseAuthException) authFail).getErrorCode();
        }

        AuthError fireBaseAuthError;
        try {
            fireBaseAuthError = AuthError.valueOf(errorCode);
        }
        catch (IllegalArgumentException exception)
        {
            Toast.makeText(context, errorCode, Toast.LENGTH_SHORT).show();
            return;
        }

        switch(fireBaseAuthError)
        {
            case ERROR_USER_NOT_FOUND:
                Toast.makeText(context, context.getResources().getString(R.string.login_auth_error_user_not_found), Toast.LENGTH_SHORT).show();
                break;
            case ERROR_WRONG_PASSWORD:
                Toast.makeText(context, context.getResources().getString(R.string.login_auth_error_wrong_password), Toast.LENGTH_SHORT).show();
                break;
            case EMAIL_TAKEN:
                Toast.makeText(context, context.getResources().getString(R.string.your_account_cannot_be_created_because_this_email_address_is_already_in_use), Toast.LENGTH_SHORT).show();
                break;
            case ERROR_WEAK_PASSWORD:
                Toast.makeText(context, context.getResources().getString(R.string.your_password_must_have_at_least_6_characters), Toast.LENGTH_SHORT).show();
                break;
            case ERROR_INVALID_EMAIL:
                Toast.makeText(context, context.getResources().getString(R.string.your_email_is_not_valid), Toast.LENGTH_SHORT).show();
                break;

        }

    }


}
