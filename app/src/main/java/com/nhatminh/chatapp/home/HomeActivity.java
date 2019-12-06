package com.nhatminh.chatapp.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nhatminh.chatapp.R;
import com.nhatminh.chatapp.Utils.GlobalConstants;
import com.nhatminh.chatapp.account.AccountFragment;
import com.nhatminh.chatapp.conversation.ChatRoomFragment;
import com.nhatminh.chatapp.friend.FriendListFragment;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView navigationView;

    Fragment messageFragment;
    Fragment friendListFragment;
    Fragment accountFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

        messageFragment = new ChatRoomFragment();
        friendListFragment = new FriendListFragment();
        accountFragment = new FriendListFragment();


        // default fragment
        loadFragment(messageFragment);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.navigation_message:
                loadFragment(messageFragment);
                return true;

            case R.id.navigation_friends:
                loadFragment(friendListFragment);
                return true;

            case R.id.navigation_account:
                loadFragment(accountFragment);
                return true;
        }
        return false;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }
}
