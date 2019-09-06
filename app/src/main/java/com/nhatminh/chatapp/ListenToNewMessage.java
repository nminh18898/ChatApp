package com.nhatminh.chatapp;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ListenToNewMessage extends IntentService {


    public ListenToNewMessage(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {


    }
}
