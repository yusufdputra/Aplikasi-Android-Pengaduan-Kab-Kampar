package com.ysf.pengaduankecelakaan.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysf.pengaduankecelakaan.R;

import static com.ysf.pengaduankecelakaan.helper.KEYS.KEY_NAMA;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     ItemPengaduan.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class ItemPengaduan extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_ITEM_COUNT = "item_count";
    private static String[] getpusat_keramaian;
    private int [] bg_item = {
            R.drawable.bg_yellow_round,
            R.drawable.bg_pink_round
    };

    private int [] logo_item = {
            R.drawable.sekolah,
            R.drawable.ic_cart
    };

    private int [] color_title = {
            R.color.font_yellow,
            R.color.font_pink
    };

    // TODO: Customize parameters
    public static ItemPengaduan newInstance(int itemCount, String[] pusat_keramaian) {
        final ItemPengaduan fragment = new ItemPengaduan();
        final Bundle args = new Bundle();
        args.putInt(ARG_ITEM_COUNT, itemCount);
        getpusat_keramaian = pusat_keramaian;
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_item_pengaduan_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_pengaduan);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(new ItemAdapter(getArguments().getInt(ARG_ITEM_COUNT)));
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        final TextView tv_nama;
        final ConstraintLayout cl_bgItem;
        final ImageView iv_logo;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            // TODO: Customize the item layout
            super(inflater.inflate(R.layout.fragment_item_pengaduan_list_dialog_item, parent, false));
            tv_nama = itemView.findViewById(R.id.tv_nama);

            cl_bgItem = itemView.findViewById(R.id.cl_background);
            iv_logo = itemView.findViewById(R.id.iv_logo);

        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final int mItemCount;

        ItemAdapter(int itemCount) {
            mItemCount = itemCount;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tv_nama.setText(getpusat_keramaian[position]);
            holder.tv_nama.setTextColor(color_title[position]);
            holder.cl_bgItem.setBackgroundResource(bg_item[position]);
            holder.iv_logo.setImageResource(logo_item[position]);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), FormPengaduanActivity.class);
                    intent.putExtra(KEY_NAMA, getpusat_keramaian[position]);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mItemCount;
        }

    }

}