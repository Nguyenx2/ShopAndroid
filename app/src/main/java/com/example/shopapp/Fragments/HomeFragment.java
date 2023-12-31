package com.example.shopapp.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.shopapp.Activities.QuanLySanPhamActivity;
import com.example.shopapp.Activities.ThemSanPhamActivity;
import com.example.shopapp.Adapters.SanPhamAdapter;
import com.example.shopapp.Models.SanPham;
import com.example.shopapp.R;
import com.example.shopapp.UserActivities.KhachHangActivity;
import com.example.shopapp.UserActivities.XemSanPhamActivity;
import com.example.shopapp.Utils.FirebaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private GridView gvSanPham;

    private ViewFlipper vfTrangChu;
    DatabaseReference sanPhamRef = FirebaseUtils.getChildRef("SanPham");
    Animation in, out;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    ArrayList<SanPham> listSanPham = new ArrayList<>();
    SanPhamAdapter sanPhamAdapter;
    Toolbar tbTrangChu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        gvSanPham = view.findViewById(R.id.gv_san_pham);
        vfTrangChu = view.findViewById(R.id.vf_trang_chu);
        tbTrangChu = view.findViewById(R.id.tb_trang_chu);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(tbTrangChu);

        in = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        out = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);

        vfTrangChu.setInAnimation(in);
        vfTrangChu.setOutAnimation(out);
        vfTrangChu.setFlipInterval(3000);
        vfTrangChu.setAutoStart(true);

        GridView gvSanPham = view.findViewById(R.id.gv_san_pham);
        sanPhamAdapter = new SanPhamAdapter(getActivity(), R.layout.gv_san_pham, listSanPham);
        gvSanPham.setAdapter(sanPhamAdapter);

        initListener();
        return view;
    }

    private void initListener() {
        docDuLieu();
        clickItem();
    }

    private void clickItem() {
        gvSanPham.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SanPham sp = sanPhamAdapter.getListSanPham().get(i);
                Intent intent = new Intent(getActivity(), XemSanPhamActivity.class);
                intent.putExtra("sp", sp);
                startActivity(intent);
            }
        });
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu_qlsp, menu);
        menu.findItem(R.id.themMoi).setVisible(false);
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
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sapXepGiamDan){
            sapXepGiamDan();
        } else if (id == R.id.sapXepTangDan){
            sapXepTangDan();
        }
        return true;
    }

    private void sapXepGiamDan() {
        Collections.sort(listSanPham, new Comparator<SanPham>() {
            @Override
            public int compare(SanPham sp1, SanPham sp2) {
                return Double.compare(sp2.getGiaSP(), sp1.getGiaSP());
            }
        });
        sanPhamAdapter = new SanPhamAdapter(getActivity(), R.layout.gv_san_pham, listSanPham);
        gvSanPham.setAdapter(sanPhamAdapter);
    }

    private void sapXepTangDan() {
        Collections.sort(listSanPham, new Comparator<SanPham>() {
            @Override
            public int compare(SanPham sp1, SanPham sp2) {
                return Double.compare(sp1.getGiaSP(), sp2.getGiaSP());
            }
        });
        sanPhamAdapter = new SanPhamAdapter(getActivity(), R.layout.gv_san_pham, listSanPham);
        gvSanPham.setAdapter(sanPhamAdapter);
    }
}