package com.ysf.pengaduankecelakaan.ui.dataPengajuan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ysf.pengaduankecelakaan.R;
import com.ysf.pengaduankecelakaan.helper.KEYS;
import com.ysf.pengaduankecelakaan.models.PengaduansModel;
import com.ysf.pengaduankecelakaan.ui.DataPengaduanActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FragmentList extends Fragment implements View.OnClickListener  {

    Bundle bundle = new Bundle();

    private RecyclerView rv_pengaduan;
    private ExtendedFloatingActionButton fab_back;
    private FrameLayout frameLayout;

    private CollectionReference ref_pengaduan;
    private FirestoreRecyclerOptions options_pengaduan;
    private FirestoreRecyclerAdapter<PengaduansModel, PengaduanViewHolder> adapterPengaduan;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_pengajuan, container, false);

        init(view);
        setOnClickListener();
        getDataPengaduan();


        return view;
    }

    private void init(View view) {
        fab_back = view.findViewById(R.id.fab_back);
        rv_pengaduan = view.findViewById(R.id.rv_pengaduan);

        ref_pengaduan = FirebaseFirestore.getInstance().collection(getResources().getString(R.string.ref_pengaduan));

    }

    private void setOnClickListener() {
        fab_back.setOnClickListener(this);
    }

    private void getDataPengaduan() {
        options_pengaduan = new FirestoreRecyclerOptions.Builder<PengaduansModel>()
                .setQuery(ref_pengaduan.orderBy("waktu_pengajuan", Query.Direction.ASCENDING), PengaduansModel.class)
                .build();
        rv_pengaduan.setLayoutManager(new LinearLayoutManager(getContext()));

    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab_back:
                getActivity().onBackPressed();
                break;
        }
    }



    @Override
    public void onStart() {
        super.onStart();

        adapterPengaduan = new FirestoreRecyclerAdapter<PengaduansModel, PengaduanViewHolder>(options_pengaduan) {
            @Override
            protected void onBindViewHolder(@NonNull PengaduanViewHolder holder, int i, @NonNull PengaduansModel pengaduansModel) {
                holder.tv_nama_pengaju.setText(pengaduansModel.getNama_pengaju());

                Date timestamp = pengaduansModel.getWaktu_pengajuan().toDate();
                SimpleDateFormat dateFormat = new SimpleDateFormat("h:m a / d-MM-y");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(timestamp);

                holder.tv_waktu.setText(dateFormat.format(calendar.getTime()));
                holder.tv_rambu.setText(pengaduansModel.getKebutuhan_rambu());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bundle.putString(KEYS.KEY_ID, getSnapshots().getSnapshot(i).getId());

                        FragmentDetail fragment = new FragmentDetail();
                        fragment.setArguments(bundle);
                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_data, fragment).addToBackStack(null).commit();
                    }
                });
            }

            @NonNull
            @Override
            public PengaduanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pengaduan, parent, false);
                return new PengaduanViewHolder(view);
            }
        };
        adapterPengaduan.startListening();
        rv_pengaduan.setAdapter(adapterPengaduan);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapterPengaduan != null) {
            adapterPengaduan.stopListening();
        }
    }

    private class PengaduanViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nama_pengaju, tv_rambu, tv_waktu;
        public PengaduanViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nama_pengaju = itemView.findViewById(R.id.tv_nama_pengaju);
            tv_rambu = itemView.findViewById(R.id.tv_rambu);
            tv_waktu = itemView.findViewById(R.id.tv_waktu);
        }
    }

}
