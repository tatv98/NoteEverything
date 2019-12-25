package nuce.tatv.noteeverything.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import nuce.tatv.noteeverything.Fragments.ExpenseFragment;
import nuce.tatv.noteeverything.Fragments.NoteFragment;
import nuce.tatv.noteeverything.Fragments.SettingFragment;
import nuce.tatv.noteeverything.Fragments.StatusFragment;
import nuce.tatv.noteeverything.R;

public class MainActivity extends AppCompatActivity {
    public static String USER_NAME  = null;
    public static String USER_EMAIL = null;
    private TextView tvUser, tvEmail;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        init();
    }
    public void init(){
        toolbar = findViewById(R.id.toolbar);
        mDrawer = findViewById(R.id.drawer_layout);
        nvDrawer = findViewById(R.id.nav_view);

        View view  = nvDrawer.getHeaderView(0);
        tvUser = view.findViewById(R.id.tvUser);
        tvEmail = view.findViewById(R.id.tvEmail);

        Intent intent = getIntent();
        tvUser.setText(intent.getStringExtra("user"));
        tvEmail.setText(intent.getStringExtra("email"));

        setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nvDrawer.setItemIconTintList(null);
        // Setup drawer view
        setupDrawerContent(nvDrawer);
        //create
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction tranExpense = fragmentManager.beginTransaction();
        ExpenseFragment expenseFragment = new ExpenseFragment();
        tranExpense.replace(R.id.flContent, expenseFragment);
        tranExpense.commit();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }
    public void selectDrawerItem(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.nav_expense:
                FragmentTransaction tranExpense = fragmentManager.beginTransaction();
                ExpenseFragment expenseFragment = new ExpenseFragment();
                tranExpense.replace(R.id.flContent, expenseFragment);
                menuItem.setChecked(true);
                mDrawer.closeDrawers();
                break;
            case R.id.nav_status:
                FragmentTransaction tranStatus = fragmentManager.beginTransaction();
                StatusFragment statusFragment = new StatusFragment();
                tranStatus.replace(R.id.flContent, statusFragment);
                tranStatus.commit();
                menuItem.setChecked(true);
                mDrawer.closeDrawers();
                break;
            case R.id.nav_note:
                FragmentTransaction tranNote = fragmentManager.beginTransaction();
                NoteFragment noteFragment = new NoteFragment();
                tranNote.replace(R.id.flContent, noteFragment);
                tranNote.commit();
                menuItem.setChecked(true);
                mDrawer.closeDrawers();
                break;
            case R.id.nav_setting:
                FragmentTransaction tranSetting = fragmentManager.beginTransaction();
                SettingFragment settingFragment = new SettingFragment();
                tranSetting.replace(R.id.flContent, settingFragment);
                tranSetting.commit();
                menuItem.setChecked(true);
                mDrawer.closeDrawers();
                break;
            case R.id.nav_share:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Please install my application^^");
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, "Share to:"));
                break;
            case R.id.nav_logout:
                logOut();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment: getSupportFragmentManager().getFragments()){
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void logOut(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
