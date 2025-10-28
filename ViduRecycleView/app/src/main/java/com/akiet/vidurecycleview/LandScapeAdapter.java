package com.akiet.vidurecycleview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class LandScapeAdapter extends RecyclerView.Adapter<LandScapeAdapter.ItemLandScapeViewHolder> {
    Context context;
    ArrayList<LandScape> lstData;

    public LandScapeAdapter(Context context, ArrayList<LandScape> lstData) {
        this.context = context;
        this.lstData = lstData;
    }

    @NonNull
    @Override
    public ItemLandScapeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater cai_bom = LayoutInflater.from(context);
        View viewItem =  cai_bom.inflate(R.layout.item_land,parent,false);
        ItemLandScapeViewHolder viewHolderCreated = new ItemLandScapeViewHolder(viewItem);
        return viewHolderCreated;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemLandScapeViewHolder holder, int position) {
        // lấy đối tượng hiển thị
        LandScape landScapeHienThi = lstData.get(position);
        // Trích thông tin
        String caption = landScapeHienThi.getLandCaption();
        String tenFileAnh = landScapeHienThi.getLandImageFileName();
        // Đặt vào các trường thông tin của holder
        holder.tvCaption.setText(caption);
        // Đặt ảnh
        String packageName = holder.itemView.getContext().getPackageName();
        int imageID = holder.itemView.getResources().getIdentifier(tenFileAnh,"mipmap", packageName);
        holder.ivLandScape.setImageResource(imageID);
    }

    @Override
    public int getItemCount() {
        return lstData.size();
    }

    class ItemLandScapeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvCaption;
        ImageView ivLandScape;

        public ItemLandScapeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCaption = itemView.findViewById(R.id.textViewCaption);
            ivLandScape = itemView.findViewById(R.id.imageViewLand);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Code ở đây
            int viTriDuocClick = getAbsoluteAdapterPosition();
            LandScape phanTuDuocChon = lstData.get(viTriDuocClick);
            // bóc thông tin
            String ten = phanTuDuocChon.getLandCaption();
            String tenFile = phanTuDuocChon.getLandImageFileName();
            // Toast Tên
            String chuoiThongBao = "Bạn vừa Click vào : " + ten;
            Toast.makeText(v.getContext(), chuoiThongBao, Toast.LENGTH_LONG).show();
        }
    }
}