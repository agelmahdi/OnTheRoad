package com.graduation.a3ltreq.ontheroad;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.graduation.a3ltreq.ontheroad.Adapter.ViewPagerAdapter;
import com.graduation.a3ltreq.ontheroad.Fragment.RescueFragment;
import com.graduation.a3ltreq.ontheroad.Fragment.ProfileFragment;
import com.graduation.a3ltreq.ontheroad.Fragment.TimeLineFragment;
import com.graduation.a3ltreq.ontheroad.activity.LoginActivity;
import com.graduation.a3ltreq.ontheroad.app.Utility;
import com.graduation.a3ltreq.ontheroad.helper.SessionManager;
import com.graduation.a3ltreq.ontheroad.helper.TimelineContract;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private SessionManager session;

    private final int [] icons={
            R.drawable.question_64,
            R.drawable.towing_64,
            R.drawable.repair_tools_64

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AdView mAdView = (AdView) findViewById(R.id.ads_banner);
        // Initialize the Mobile Ads SDK.

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        mAdView.loadAd(adRequest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        session = new SessionManager(this);

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        HashMap<String, String> user = Utility.getUserDetails(this);
        String name = user.get(TimelineContract.PickEntry.COLUMN_USER_NAME);
        String email = user.get(TimelineContract.PickEntry.COLUMN_KEY_EMAIL);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, name);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, email);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(icons[0]);
        tabLayout.getTabAt(1).setIcon(icons[1]);
        tabLayout.getTabAt(2).setIcon(icons[2]);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new TimeLineFragment(),"Towing");
        adapter.addFrag(new RescueFragment(),"Rescue");
        adapter.addFrag(new ProfileFragment(),"Profile");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_logout:
                logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    private void logoutUser() {
        session.setLogin(false);

        deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();


    }

    private void deleteUsers() {
        // Delete All Rows
        AsyncTask<Void, Void, Void> delet = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                getContentResolver().delete(TimelineContract.PickEntry.CONTENT_URI_LOGIN, TimelineContract.PickEntry.COLUMN_KEY_ID,null);
                return null;
            }
        };
        delet.execute();
    }
}
