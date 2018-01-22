package com.capa.capa.capa;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class HomeActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nv);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawerContent(nvDrawer);
        firebaseAuth = FirebaseAuth.getInstance();
        loadUserInformation();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
    }

    private void loadUserInformation() {

        FirebaseUser user = firebaseAuth.getCurrentUser();
        String photoUrl = user.getPhotoUrl().toString();
        String displayName = user.getDisplayName();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
    public void selectItemDrawer(MenuItem menuItem){
        Fragment myFragement;

        switch (menuItem.getItemId()){
            case R.id.db:
                myFragement = new DashBoardFragment();
                break;
            case R.id.event:
                myFragement = new EventFragment();
                break;
            case R.id.history:
                myFragement = new HistoryFragment();
                break;
            case R.id.payment:
                myFragement = new PaymentFragment();
                break;
            case R.id.setting:
                myFragement = new SettingFragment();
                break;
            case R.id.logout:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                firebaseAuth.signOut();

            default:
                myFragement = new DashBoardFragment();

        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame,myFragement).commit();

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();
    }
    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                selectItemDrawer(item);
                return true;
            }
        });
    }

}
