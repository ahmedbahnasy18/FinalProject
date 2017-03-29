package com.iti.finalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public Chief oChief;
    private List<Chief> chiefsList = new ArrayList<>();
    public List<Item> currentBasket = new ArrayList<>();
    private RecyclerView recyclerView;
    private ChiefAdapter mAdapter;
    private ActionBarDrawerToggle toggle;
    public boolean isItemsFragmentVisible;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    toggle.setDrawerIndicatorEnabled(false);
                    toggle.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
                } else {
                    setTitle(getString(R.string.app_name));
                    toggle.setDrawerIndicatorEnabled(true);
                }
            }
        });
        isItemsFragmentVisible = false;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                View navigationViewHeader = navigationView.getHeaderView(0);
                ((TextView) navigationViewHeader.findViewById(R.id.nav_header_name)).setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                ((TextView) navigationViewHeader.findViewById(R.id.nav_header_email)).setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            }
        }, 500);
        navigationView.setNavigationItemSelectedListener(this);

        setTitle("ChiefsList");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DatabaseAdapter.getInstance().getDatabase().getReference("Chiefs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chiefsList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    chiefsList.add(postSnapshot.getValue(Chief.class));
                }
                mAdapter = new ChiefAdapter(HomeActivity.this, chiefsList, new CustomItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        oChief = chiefsList.get(position);
                        ChiefItemsFragment chiefItemsFragment = ChiefItemsFragment.objInstance(oChief);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_home,chiefItemsFragment)
                                .addToBackStack("")
                                .commit();
                    }
                });
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (currentBasket.size() > 0 && isItemsFragmentVisible) {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you like to change the chief ?")
                    .setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            currentBasket.clear();
                            HomeActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("No", null).show();
        } else {

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        ((SearchView) menu.findItem(R.id.action_search).getActionView())
                .setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.filter(s);
                return false;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            onBackPressed();
        }else if (id == R.id.info) {
            ChiefInfoFragment chiefInfoFragment = ChiefInfoFragment.objInstance(oChief);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_home, chiefInfoFragment)
                    .addToBackStack("")
                    .commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_Profile) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_home,new UserProfileFragment(),"profile")
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_Oreders) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_home, new orderList_fragment(),"orders")
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_sign_out){
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else if (id == R.id.nav_About_US) {
            new AlertDialog.Builder(this)
                    .setTitle("About")
                    .setMessage("Made by:\n" +
                            "\t\tAhmed Bahnasy\n" +
                            "\t\tEsraa Gomaa\n" +
                            "\t\tSara Mohammed\n" +
                            "\t\tMostafa Saleh")
                    .setPositiveButton("Close", null)
                    .show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
}
