package com.example.bookshare;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.example.bookshare.model.Category;
import com.example.bookshare.model.GlobalData;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    private ActionBarDrawerToggle mToggle;
    private ImageView btnSearch, navImage;
    private ImageButton btnProfileOption;
    private BadgeDrawable badgeDrawable;
    SharedPreferences preferences;
    public static final String FILE_NAME="preferenceFile";

    private String url = GlobalData.url;
    private String action = "?action=";
    private String apiName = "";

    private ArrayList<Category> categoryList = new ArrayList<Category>();

    //private BottomNavigationView bnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!isConnected(getApplicationContext())){
            Intent loadAgain = new Intent(getApplicationContext(),SplashActivity.class);
            startActivity(loadAgain);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        //toolbar.setBackgroundColor(getResources().getColor(R.color.green));
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        //toolbar.setNavigationIcon(R.drawable.ic_chrome_reader_mode_black_24dp);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setIcon(R.drawable.logo_text);
        setTitle("");

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        BottomNavigationView BottomNavView = findViewById(R.id.bottom_nav_view);
        // AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(BottomNavView, navController);
//        badgeDrawable = BottomNavView.getOrCreateBadge(R.id.navigation_mycart);
//        badgeDrawable.setBackgroundColor(Color.RED);
//        badgeDrawable.setBadgeTextColor(Color.WHITE);
//        badgeDrawable.setNumber(0);
//        badgeDrawable.setVisible(false);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        preferences = getSharedPreferences(FILE_NAME,MODE_PRIVATE);


        //changing navigation drawer toggle button icon

        toggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_menu_grey_24dp, getApplication().getTheme());
        toggle.setHomeAsUpIndicator(drawable);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });


        //get categories
        //loadCategoryData();


        //Search activity call
        btnSearch = (ImageView) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
                //startActivity(intent);
            }
        });


       // getActionBar().setIcon(R.drawable.logo_text);


//        mToggle = new ActionBarDrawerToggle(this,drawer,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
//        drawer.addDrawerListener(mToggle);
//        mToggle.syncState();
//        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

//        View headerview = navigationView.getHeaderView(0);
//        btnProfileOption = (ImageButton) headerview.findViewById(R.id.btn_profile);
//        btnProfileOption.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Toast.makeText(getApplicationContext(),"clicked", Toast.LENGTH_LONG).show();
//                showProfileOptionMenu();
//            }
//        });


//        navImage = (ImageView) headerview.findViewById(R.id.navImage);
//        navImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Toast.makeText(getApplicationContext(),"clicked", Toast.LENGTH_LONG).show();
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if(id == R.id.nav_Logout){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("username", "");
            editor.putString("password", "");
            editor.putInt("userId", 0);
            editor.putString("loggedin","no");
            editor.commit();
            Toast.makeText(getApplicationContext(), "You've logged out successfully!", Toast.LENGTH_LONG).show();
            Intent goLogin = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(goLogin);
            finish();
        }
        return true;
    }

    private void showAlertDialog() {
        //init alert dialog
        final AlertDialog.Builder builder =  new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you want to leave?");
        //set listeners for dialog buttons
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish the activity
                finish();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog gone
                dialog.dismiss();
            }
        });

        //create the alert dialog and show it
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        showAlertDialog();
    }

    public void showProfileOptionMenu(){
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, btnProfileOption);
        popupMenu.getMenuInflater().inflate(R.menu.profile_menu, popupMenu.getMenu());
//        if(preferences.getString("loggedin","").equals("yes")){
//            popupMenu.getMenu().removeItem(R.id.rented_books);
//        }else{
//            popupMenu.getMenu().removeItem(R.id.profile_logout);
//        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.my_all_books:
                        break;
                    case R.id.my_books_for_rent:
                        break;
                    case R.id.my_books_for_sell:
                        break;
                    case R.id.rented_books:
                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                        break;
//                    case R.id.profile_logout:
//                        SharedPreferences.Editor editor = preferences.edit();
//                        editor.putString("username", "");
//                        editor.putString("password", "");
//                        editor.putInt("userId", 0);
//                        editor.putString("loggedin","no");
//                        editor.commit();
//                        Toast.makeText(getApplicationContext(), "You've logged out successfully!", Toast.LENGTH_LONG).show();
//                        Intent goLogin = new Intent(MainActivity.this,LoginActivity.class);
//                        startActivity(goLogin);
//                        finish();
//                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        }
        else return false;
    }
}
