package com.example.shopapp.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toolbar;

import com.example.shopapp.Adapters.SanPhamAdapter;
import com.example.shopapp.Models.SanPham;
import com.example.shopapp.R;
import com.example.shopapp.Utils.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class QuanLySanPhamActivity extends AppCompatActivity {
    ListView lvSanPham;
    ArrayList<SanPham> listSanPham;
    androidx.appcompat.widget.Toolbar tbQuanLySP;
    SanPhamAdapter sanPhamAdapter;

    DatabaseReference sanPhamRef = FirebaseUtils.getChildRef("SanPham");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_san_pham);
        lvSanPham = findViewById(R.id.lvSanPham);

        tbQuanLySP = findViewById(R.id.tbQuanLySP);

        listSanPham = new ArrayList<>();
        sanPhamAdapter = new SanPhamAdapter(QuanLySanPhamActivity.this, R.layout.lv_san_pham, listSanPham);
        lvSanPham.setAdapter(sanPhamAdapter);


        docDuLieu();
        ActionBar();
    }

    private void ActionBar() {
        setSupportActionBar(tbQuanLySP);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tbQuanLySP.setNavigationIcon(R.drawable.back);
        tbQuanLySP.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_qlsp, menu);
        MenuItem menuSearch = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) menuSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                sanPhamAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                sanPhamAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.themMoi) {
            Intent intent = new Intent(getBaseContext(), ThemSanPhamActivity.class);
            startActivity(intent);
        } else if (id == R.id.sapXepGiamDan) {
            sapXepGiamDan();
        } else if (id == R.id.sapXepTangDan) {
            sapXepTangDan();
        }
        return true;
    }


    private void docDuLieu() {
        sanPhamRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listSanPham.clear();
                for (DataSnapshot data :snapshot.getChildren()) {
                    SanPham sp = data.getValue(SanPham.class);
                    if (sp != null) {
                        listSanPham.add(sp);
                    }
                }
                sanPhamAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sapXepGiamDan() {
        Collections.sort(listSanPham, new Comparator<SanPham>() {
            @Override
            public int compare(SanPham sp1, SanPham sp2) {
                return Double.compare(sp2.getGiaSP(), sp1.getGiaSP());
            }
        });
        sanPhamAdapter = new SanPhamAdapter(QuanLySanPhamActivity.this, R.layout.lv_san_pham, listSanPham);
        lvSanPham.setAdapter(sanPhamAdapter);
    }

    private void sapXepTangDan() {
        Collections.sort(listSanPham, new Comparator<SanPham>() {
            @Override
            public int compare(SanPham sp1, SanPham sp2) {
                return Double.compare(sp1.getGiaSP(), sp2.getGiaSP());
            }
        });
        sanPhamAdapter = new SanPhamAdapter(QuanLySanPhamActivity.this, R.layout.lv_san_pham, listSanPham);
        lvSanPham.setAdapter(sanPhamAdapter);
    }
}