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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private FirebaseAuth firebaseAuth;
    private ImageView imageView;
    private TextView displayNameTextEdit;
    private TextView displayEmail;
    private NavigationView nvDrawer;
    private DatabaseReference firebaseRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mDrawerLayout = findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        nvDrawer = findViewById(R.id.nv);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupDrawerContent(nvDrawer);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseRef = FirebaseDatabase.getInstance().getReference();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new DashBoardFragment()).commit();


        View header = nvDrawer.getHeaderView(0);
        imageView = header.findViewById(R.id.profileImagemView);
        displayNameTextEdit = header.findViewById(R.id.profileName);
        displayEmail = header.findViewById(R.id.profileEmail);

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

        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






    }

    private void showData(DataSnapshot dataSnapshot) {
        String uid = firebaseAuth.getCurrentUser().getUid().toString();
        for (DataSnapshot ds : dataSnapshot.getChildren()){
            User user = new User();
            user.setUsername(ds.child(uid).getValue(User.class).getUsername());
            user.setDob(ds.child(uid).getValue(User.class).getDob());
            user.setEmail(ds.child(uid).getValue(User.class).getEmail());
            displayNameTextEdit.setText(user.username);
            displayEmail.setText(user.email);



        }
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
