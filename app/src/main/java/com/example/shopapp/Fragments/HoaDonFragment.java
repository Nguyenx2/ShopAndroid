package com.example.shopapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shopapp.Adapters.HoaDonAdapter;
import com.example.shopapp.Adapters.SanPhamDaMuaAdapter;
import com.example.shopapp.Models.GioHangItem;
import com.example.shopapp.Models.HoaDon;
import com.example.shopapp.R;
import com.example.shopapp.Utils.FirebaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HoaDonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HoaDonFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HoaDonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DaMuaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HoaDonFragment newInstance(String param1, String param2) {
        HoaDonFragment fragment = new HoaDonFragment();
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
    }
    DatabaseReference hoaDonRef = FirebaseUtils.getChildRef("HoaDon");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ListView lvHoaDon;
    ArrayList<HoaDon> listHoaDon = new ArrayList<>();
    HoaDonAdapter hoaDonAdapter;
    TextView tvThongBao;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hoa_don, container, false);

        docDuLieu();
        tvThongBao = view.findViewById(R.id.tv_thong_bao);
        lvHoaDon = view.findViewById(R.id.lv_hoa_don);
        hoaDonAdapter = new HoaDonAdapter(getActivity(), R.layout.lv_hoa_don, listHoaDon);
        lvHoaDon.setAdapter(hoaDonAdapter);
        return view;
    }

    private void docDuLieu(){
        hoaDonRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listHoaDon.clear();
                for (DataSnapshot data : snapshot.getChildren()){
                    String userId = data.child("userId").getValue(String.class);
                    if (userId != null && userId.equals(user.getUid())){
                        HoaDon hoaDon = data.getValue(HoaDon.class);
                        listHoaDon.add(hoaDon);
                    }
                }

                Collections.sort(listHoaDon, comparator);
                if (listHoaDon.size() > 0){
                    tvThongBao.setVisibility(View.INVISIBLE);
                } else {
                    tvThongBao.setVisibility(View.VISIBLE);
                }
                hoaDonAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    Comparator<HoaDon> comparator = new Comparator<HoaDon>() {
        @Override
        public int compare(HoaDon hd1, HoaDon hd2) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date1 = sdf.parse(hd1.getCreatedAt());
                Date date2 = sdf.parse(hd2.getCreatedAt());
                return  date2.compareTo(date1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        }
    };
}