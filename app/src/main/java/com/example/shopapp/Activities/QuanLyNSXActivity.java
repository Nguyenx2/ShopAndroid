package com.example.shopapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shopapp.Adapters.NhaSanXuatAdapter;
import com.example.shopapp.Models.NhaSanXuat;
import com.example.shopapp.R;
import com.example.shopapp.Utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuanLyNSXActivity extends AppCompatActivity {
    ListView lvNhaSanXuat;

    Toolbar tbQuanLyNSX;
    ArrayList<NhaSanXuat> listNhaSanXuat;
    NhaSanXuatAdapter nhaSanXuatAdapter;

    DatabaseReference nhaSanXuatRef = FirebaseUtils.getChildRef("NhaSanXuat");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_nsxactivity);

        tbQuanLyNSX = findViewById(R.id.tbQuanLyNSX);

        lvNhaSanXuat = findViewById(R.id.lvNhaSanXuat);
        listNhaSanXuat = new ArrayList<>();
        nhaSanXuatAdapter = new NhaSanXuatAdapter(QuanLyNSXActivity.this, R.layout.lv_nha_san_xuat, listNhaSanXuat);
        lvNhaSanXuat.setAdapter(nhaSanXuatAdapter);

        registerForContextMenu(lvNhaSanXuat);
        docDuLieu();
        ActionBar();
    }

    private void ActionBar() {
        setSupportActionBar(tbQuanLyNSX);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tbQuanLyNSX.setNavigationIcon(R.drawable.back);
        tbQuanLyNSX.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // Thao tac tren thanh menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_qlsx, menu);

        MenuItem menuSearch = menu.findItem(R.id.nhaSXSearch);
        SearchView searchView = (SearchView) menuSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                nhaSanXuatAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                nhaSanXuatAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void docDuLieu() {
        nhaSanXuatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listNhaSanXuat.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    NhaSanXuat nhaSanXuat = data.getValue(NhaSanXuat.class);
                    if (nhaSanXuat != null) {
                        listNhaSanXuat.add(nhaSanXuat);
                    }
                }
                nhaSanXuatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Thao tac voi nha san xuat

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        NhaSanXuat nhaSanXuat = nhaSanXuatAdapter.listNhaSanXuat.get(info.position);
        String title = "Thao tác với: " + nhaSanXuat.getTenNhaSX();
        menu.setHeaderTitle(title);
        menu.add(0, v.getId(), 0, "Gọi điện thoại");
        menu.add(0, v.getId(), 1, "Gửi Email");
        menu.add(0, v.getId(), 2, "Xem địa chỉ");
        menu.add(0, v.getId(), 3, "Sửa thông tin");
        menu.add(0, v.getId(), 4, "Xóa nhà sản xuất");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        NhaSanXuat nhaSanXuat = nhaSanXuatAdapter.getListNhaSanXuat().get(info.position);

        switch (item.getOrder()) {
            case 0:
                Intent goiDienThoaiIntent = new Intent(Intent.ACTION_DIAL);
                goiDienThoaiIntent.setData(Uri.parse("tel:" + nhaSanXuat.getSoDienThoai()));
                startActivity(goiDienThoaiIntent);
                break;
            case 1:
                Intent intent = new Intent(QuanLyNSXActivity.this, GuiEmailNhaSanXuatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("email", nhaSanXuat.getEmail());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case 2:
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + nhaSanXuat.getDiaChi());

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null){
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(this, "Vui lòng tải Google Map để sử dụng !", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                Intent suaNhaSanXuatIntent = new Intent(getBaseContext(), SuaNhaSanXuatActivity.class);
                Bundle bundle3 = new Bundle();
                bundle3.putString("id", nhaSanXuat.getId());
                suaNhaSanXuatIntent.putExtras(bundle3);
                startActivity(suaNhaSanXuatIntent);
                break;
            case 4:
                String thongDiep = "Bạn thực sự muốn xóa: " + nhaSanXuat.getTenNhaSX() + " ?";
                AlertDialog.Builder builder = new AlertDialog.Builder(QuanLyNSXActivity.this);
                builder.setTitle("Xóa nhà sản xuất");
                builder.setMessage(thongDiep);
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        nhaSanXuatRef.child(nhaSanXuat.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(QuanLyNSXActivity.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(QuanLyNSXActivity.this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        docDuLieu();
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
                break;
        }
        return true;
    }
}