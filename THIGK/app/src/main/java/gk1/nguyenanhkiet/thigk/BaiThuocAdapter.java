package gk1.nguyenanhkiet.thigk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BaiThuocAdapter extends RecyclerView.Adapter<BaiThuocAdapter.ViewHolder> {
    private List<BaiThuoc> baiThuocList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(BaiThuoc baiThuoc);
    }

    public BaiThuocAdapter(List<BaiThuoc> list, OnItemClickListener listener) {
        this.baiThuocList = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaiThuoc baiThuoc = baiThuocList.get(position);
        holder.textView.setText(baiThuoc.getTen());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(baiThuoc));
    }

    @Override
    public int getItemCount() {
        return baiThuocList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}