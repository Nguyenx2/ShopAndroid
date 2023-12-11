package com.example.shopapp.UserActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.shopapp.Fragments.CaNhanFragment;
import com.example.shopapp.Fragments.DaMuaFragment;
import com.example.shopapp.Fragments.GioHangFragment;
import com.example.shopapp.Fragments.HoaDonFragment;
import com.example.shopapp.Fragments.HomeFragment;
import com.example.shopapp.R;
import com.example.shopapp.Utils.FirebaseUtils;
import com.example.shopapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class KhachHangActivity extends AppCompatActivity {
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference userRef = FirebaseUtils.getChildRef("User");
    ActivityMainBinding binding;
    FrameLayout frameLayout;
    BottomNavigationView bottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        initUi();

        if (getIntent().hasExtra("fragmentToOpen")) {
            String fragmentTag = getIntent().getStringExtra("fragmentToOpen");
            if (fragmentTag.equals("GioHangFragment")){
                rFragment(new GioHangFragment());
                bottomNav.getMenu().findItem(R.id.gio_hang).setChecked(true);
            }
        } else {
            rFragment(new HomeFragment());
        }

        initListener();
    }

    private void initUi(){
        frameLayout = findViewById(R.id.frame_layout);
        bottomNav = findViewById(R.id.bottom_nav);
    }

    private void initListener(){
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    rFragment(new HomeFragment());
                } else if (itemId == R.id.hoa_don) {
                    rFragment(new HoaDonFragment());
                } else if (itemId == R.id.gio_hang) {
                    rFragment(new GioHangFragment());
                } else if (itemId == R.id.da_mua) {
                    rFragment(new DaMuaFragment());
                } else if (itemId == R.id.ca_nhan) {
                    rFragment(new CaNhanFragment());
                }
                return true;
            }
        });
    }

    private void rFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}