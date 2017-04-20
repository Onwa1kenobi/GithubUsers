package com.fifthtechnologies.githubusers.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fifthtechnologies.githubusers.R;
import com.fifthtechnologies.githubusers.fragment.MainFragment;
import com.fifthtechnologies.githubusers.utils.GitHubUser;

public class MainActivity extends AppCompatActivity {

    private static final String MAIN_FRAGMENT = "main_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        MainFragment mainFragment = new MainFragment();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.add(R.id.container, mainFragment, MAIN_FRAGMENT);
        fragmentTransaction.commit();
    }
}
