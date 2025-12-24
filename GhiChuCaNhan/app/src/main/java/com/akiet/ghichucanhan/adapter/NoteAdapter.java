package com.akiet.ghichucanhan.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akiet.ghichucanhan.R;
import com.akiet.ghichucanhan.model.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private static final String TAG = "NoteAdapter";
    private List<Note> notes = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Note note);
        void onItemLongClick(Note note);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);

        holder.tvTitle.setText(note.getTitle());
        holder.tvContent.setText(note.getContent());

        // Hiển thị icon ghim
        holder.imgPin.setVisibility(note.isPinned() ? View.VISIBLE : View.GONE);

        // Màu nền
        try {
            holder.noteBackground.setBackgroundColor(Color.parseColor(note.getColor()));
        } catch (Exception e) {
            holder.noteBackground.setBackgroundColor(Color.WHITE);
        }

        // Thời gian
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        holder.tvTime.setText(sdf.format(new Date(note.getTimestamp())));

        // Click để sửa
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(note);
            }
        });

        // Long click để ghim
        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onItemLongClick(note);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return notes == null ? 0 : notes.size();
    }

    public List<Note> getNotes() {
        return notes;
    }

    // Cập nhật danh sách
    public void setNotes(List<Note> newList) {
        if (newList == null) {
            newList = new ArrayList<>();
        }

        // Sắp xếp ghim lên đầu
        newList.sort((a, b) -> {
            if (a.isPinned() != b.isPinned()) {
                return Boolean.compare(b.isPinned(), a.isPinned());
            }
            return Long.compare(b.getTimestamp(), a.getTimestamp());
        });

        Log.d(TAG, "Setting notes: " + newList.size() + " items");

        this.notes = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent, tvTime;
        ImageView imgPin;
        LinearLayout noteBackground;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvTime = itemView.findViewById(R.id.tvTime);
            imgPin = itemView.findViewById(R.id.imgPin);
            noteBackground = itemView.findViewById(R.id.noteBackground);
        }
    }
}