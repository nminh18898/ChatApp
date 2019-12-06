package com.nhatminh.chatapp.login;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.nhatminh.chatapp.Utils.DateManipulation;
import com.nhatminh.chatapp.Utils.DatePickerFragment;
import com.nhatminh.chatapp.user.BasicUserInfo;
import com.nhatminh.chatapp.R;

import java.util.Date;

public class UserInformationSignUpForm extends AppCompatActivity implements View.OnClickListener, DatePickerFragment.OnDateSelectedListener {

    EditText etFirstName, etLastName;
    EditText etBirthDay;
    EditText etPhone;
    RadioGroup rgGender;
    RadioButton rbMale, rbFemale, rbGenderOthers;
    Button btnSelectBirthDay;
    Button btnSubmit;
    DialogFragment dpSelectBirthDay;
    DateManipulation dateManipulation;
    Button btnChangeProfilePicture;
    ImageView ivProfilePicture;

    boolean isUserChangeProfileImage = false;
    Uri uriNewProfilePicture;



    public static int RESULT_LOAD_IMG = 1;
    private static final int REQUEST_PERMISSIONS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information_signup_form);

        setupView();
        dateManipulation = new DateManipulation();
    }

    private void setupView()
    {
        etBirthDay = findViewById(R.id.etBirthDay);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        rgGender = findViewById(R.id.rgGender);
        etPhone = findViewById(R.id.etPhone);

        btnSelectBirthDay = findViewById(R.id.btnSelectBirthDay);
        btnSelectBirthDay.setOnClickListener(UserInformationSignUpForm.this);

        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        rbGenderOthers = findViewById(R.id.rbGenderOther);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);


        dpSelectBirthDay = new DatePickerFragment();

        btnChangeProfilePicture = findViewById(R.id.btnChangePicture);
        btnChangeProfilePicture.setOnClickListener(this);

        ivProfilePicture = findViewById(R.id.ivProfilePicturePreview);


    }

    private void createBirthdayCalendarPicker()
    {
        dpSelectBirthDay.show(getSupportFragmentManager(),"BirthDatePicker");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnSelectBirthDay:
                createBirthdayCalendarPicker();
                break;

            case R.id.btnSubmit:
                BasicUserInfo basicUserInfo = getUserInput();
                if(isUserInfoValid(basicUserInfo)) {
                    addUserBasicInfoToDatabase(basicUserInfo);
                }
                break;

            case R.id.btnChangePicture:
                if(requestPermission()) {
                    sentIntentToGetImage();
                }
                break;
        }
    }

    private void sentIntentToGetImage(){

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //photoPickerIntent.setType("image/*");
        //photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Picture"), RESULT_LOAD_IMG);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                uriNewProfilePicture = data.getData();
                Glide.with(UserInformationSignUpForm.this).load(uriNewProfilePicture).into(ivProfilePicture);
                isUserChangeProfileImage = true;
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(UserInformationSignUpForm.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(UserInformationSignUpForm.this, getResources().getString(R.string.well_we_got_nothing),Toast.LENGTH_LONG).show();
        }
    }

    public void addUserBasicInfoToDatabase(BasicUserInfo basicUserInfo)
    {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child(userId)
                .child("basicInfo")
                .setValue(basicUserInfo);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates;

        if(isUserChangeProfileImage){
            profileUpdates = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(uriNewProfilePicture)
                    .build();
        }
        else{
            profileUpdates = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(getUriOfDrawable(UserInformationSignUpForm.this,R.drawable.default_profile_picture))
                    .build();
        }


        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(getPackageName(), "User profile updated.");
                        }
                    }
                });

    }

    public static final Uri getUriOfDrawable(@NonNull Context context,
                                             @AnyRes int drawableId) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId) );
        return imageUri;
    }

    public boolean requestPermission()
    {
        boolean isPermissionGranted = false;
        // asking for permission
        if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        ) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(UserInformationSignUpForm.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(UserInformationSignUpForm.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))

            ) {
                Toast.makeText(UserInformationSignUpForm.this, getResources().getString(R.string.please_allow_this_app_to_read_and_write_to_your_storage), Toast.LENGTH_LONG).show();
                isPermissionGranted = false;

            } else {
                ActivityCompat.requestPermissions(UserInformationSignUpForm.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        }else {
            isPermissionGranted = true;

        }

        return isPermissionGranted;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean isPermissionGranted = false;

        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        isPermissionGranted = true;
                    }
                    else
                    {
                        isPermissionGranted = false;
                    }
                }
            }
        }

        if(isPermissionGranted) {
            sentIntentToGetImage();
        }
        else {
            Toast.makeText(UserInformationSignUpForm.this, getResources().getString(R.string.please_allow_this_app_to_read_and_write_to_your_storage), Toast.LENGTH_LONG).show();
        }
    }



    /** Get all user's input. Return BasicUserInfo contains all user's input
     */
    private BasicUserInfo getUserInput()
    {

       BasicUserInfo basicUserInfo = new BasicUserInfo();

       String firstName = etFirstName.getText().toString();
       String lastName = etLastName.getText().toString();
       String phone = etPhone.getText().toString();
       String birthDay = etBirthDay.getText().toString();
       int gender = -1;


       if (rbMale.isChecked()){
           gender = 1;
       }
       else if(rbFemale.isChecked()) {
           gender = 2;
       }
       else if (rbGenderOthers.isChecked()){
           gender = 0;
       }

       basicUserInfo.setFirstName(firstName);
       basicUserInfo.setLastName(lastName);
       basicUserInfo.setBirthDay(dateManipulation.stringToDate(birthDay));
       basicUserInfo.setGender(gender);
       basicUserInfo.setPhone(phone);
       return basicUserInfo;

    }
    
   
    private boolean isUserInfoValid(BasicUserInfo basicUserInfo){
        if (basicUserInfo.getFirstName().isEmpty()){
            etFirstName.setError(getResources().getString(R.string.can_not_be_empty));
            return false;
        }
        
        if(basicUserInfo.getLastName().isEmpty()){
            etLastName.setError(getResources().getString(R.string.can_not_be_empty));
            return false;
        }
        
        if(basicUserInfo.getGender() == -1) {
            Toast.makeText(this, getResources().getString(R.string.please_select_your_gender), Toast.LENGTH_SHORT).show();
            return false;
        }

        if(basicUserInfo.getBirthDay() == null) {
            Toast.makeText(this, getResources().getString(R.string.your_birthday_is_empty_or_not_valid), Toast.LENGTH_SHORT).show();
            return false;

        }

        if(basicUserInfo.getPhone().isEmpty()) {
            etPhone.setError(getResources().getString(R.string.can_not_be_empty));
            return false;
        }

        return true;
    }

    @Override
    public void selectedDate(Date selectedDate) {
        String birthDay;
        birthDay = dateManipulation.dateToString(selectedDate);
        etBirthDay.setText(birthDay);

    }
}
