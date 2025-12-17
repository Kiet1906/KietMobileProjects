package com.akiet.ghichucanhan;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import com.google.android.material.appbar.MaterialToolbar;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton fabAdd;
    NoteAdapter adapter;
    List<Note> noteList = new ArrayList<>();
    List<Note> noteListFull = new ArrayList<>(); // để search
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    ListenerRegistration listenerRegistration; // để lắng nghe realtime

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);


        // Authentication check
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        recyclerView = findViewById(R.id.recyclerView);
        fabAdd = findViewById(R.id.fabAdd);
        findViewById(R.id.emptyView).setVisibility(noteList.isEmpty() ? View.VISIBLE : View.GONE);

        db = FirebaseFirestore.getInstance();

        // Setup RecyclerView
        adapter = new NoteAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                showNoteDialog(note); // sửa
            }

            @Override
            public void onItemLongClick(Note note) {
                // show quick delete confirmation? For now delete directly with Firestore
                deleteNoteWithUndo(note);
            }
        });

        fabAdd.setOnClickListener(v -> showNoteDialog(null)); // thêm mới

        enableSwipeToDelete();

        loadNotesRealtime();
    }

    private void loadNotesRealtime() {
        String uid = mAuth.getCurrentUser() == null ? null : mAuth.getCurrentUser().getUid();
        if (uid == null) {
            Toast.makeText(this, "Người dùng chưa đăng nhập", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        listenerRegistration = db.collection("users").document(uid).collection("notes")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    noteList.clear();
                    if (snapshots != null) {
                        for (DocumentSnapshot doc : snapshots) {
                            Note note = doc.toObject(Note.class);
                            if (note != null) {
                                note.setId(doc.getId());
                                noteList.add(note);
                            }
                        }
                    }
                    // cập nhật adapter và bản sao dùng cho tìm kiếm
                    noteListFull = new ArrayList<>(noteList);
                    adapter.setNotes(noteList);
                    findViewById(R.id.emptyView).setVisibility(noteList.isEmpty() ? View.VISIBLE : View.GONE);
                });
    }

    private void showNoteDialog(Note currentNote) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_note);
        TextInputEditText edtTitle = dialog.findViewById(R.id.edtTitle);
        TextInputEditText edtContent = dialog.findViewById(R.id.edtContent);

        if (currentNote != null) {
            edtTitle.setText(currentNote.getTitle());
            edtContent.setText(currentNote.getContent());
        }

        dialog.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.btnSave).setOnClickListener(v -> {
            String title = edtTitle.getText() == null ? "" : edtTitle.getText().toString().trim();
            String content = edtContent.getText() == null ? "" : edtContent.getText().toString().trim();
            if (title.isEmpty()) title = "Không có tiêu đề";

            Note note = new Note(title, content, System.currentTimeMillis(), "#FFFFFF");
            CollectionReference notesRef = db.collection("users")
                    .document(mAuth.getCurrentUser().getUid()).collection("notes");

            if (currentNote == null) {
                // Thêm mới
                notesRef.add(note).addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Đã thêm ghi chú", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }).addOnFailureListener(ex -> {
                    Toast.makeText(this, "Thêm thất bại: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                });
            } else {
                // Sửa — giữ nguyên id hiện tại
                notesRef.document(currentNote.getId()).set(note)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Đã cập nhật", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }).addOnFailureListener(ex -> {
                            Toast.makeText(this, "Cập nhật thất bại: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                        });
            }
        });

        dialog.show();
    }

    private void enableSwipeToDelete() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Note note = adapter.getNotes().get(position);
                        deleteNoteWithUndo(note);
                    }
                };

        new ItemTouchHelper(simpleItemTouchCallback).attachToRecyclerView(recyclerView);
    }

    private void deleteNoteWithUndo(Note note) {
        if (note == null || note.getId() == null) return;
        String uid = mAuth.getCurrentUser() == null ? null : mAuth.getCurrentUser().getUid();
        if (uid == null) return;

        // xóa tạm khỏi UI
        List<Note> backup = new ArrayList<>(noteList);
        noteList.remove(note);
        adapter.setNotes(noteList);

        // xóa Firestore
        db.collection("users").document(uid).collection("notes")
                .document(note.getId()).delete()
                .addOnFailureListener(e -> Toast.makeText(this, "Xóa thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show());

        // show Snackbar để undo
        Snackbar.make(findViewById(R.id.recyclerView), "Đã xóa", Snackbar.LENGTH_LONG)
                .setAction("Hoàn tác", v -> {
                    // hoàn tác: thêm lại vào firestore (tạo mới với dữ liệu cũ)
                    if (note != null) {
                        db.collection("users").document(uid).collection("notes")
                                .add(note)
                                .addOnSuccessListener(docRef -> {
                                    Toast.makeText(MainActivity.this, "Hoàn tác thành công", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Hoàn tác thất bại", Toast.LENGTH_SHORT).show());
                    }
                }).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listenerRegistration != null) listenerRegistration.remove();
    }

    // Menu: search + logout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Tìm ghi chú...");
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterNotes(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterNotes(newText);
                return false;
            }
        });

        return true;
    }

    private void filterNotes(String query) {
        if (query == null || query.trim().isEmpty()) {
            adapter.setNotes(noteListFull);
            noteList = new ArrayList<>(noteListFull);
            return;
        }
        String q = query.toLowerCase();
        List<Note> filtered = new ArrayList<>();
        for (Note n : noteListFull) {
            if ((n.getTitle() != null && n.getTitle().toLowerCase().contains(q)) ||
                    (n.getContent() != null && n.getContent().toLowerCase().contains(q))) {
                filtered.add(n);
            }
        }
        adapter.setNotes(filtered);
        noteList = new ArrayList<>(filtered);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
