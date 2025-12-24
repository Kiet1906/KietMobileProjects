package com.akiet.ghichucanhan.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akiet.ghichucanhan.R;
import com.akiet.ghichucanhan.adapter.NoteAdapter;
import com.akiet.ghichucanhan.model.Note;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    private ProgressBar progressBar;
    private LinearLayout emptyView;
    private NoteAdapter adapter;
    private List<Note> noteList = new ArrayList<>();
    private List<Note> noteListFull = new ArrayList<>();

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ListenerRegistration listenerRegistration;

    private String[] colors = {"#FFFFFF", "#FFCDD2", "#F8BBD0", "#E1BEE7",
            "#C5CAE9", "#BBDEFB", "#B2DFDB", "#C8E6C9",
            "#FFF9C4", "#FFECB3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Kiểm tra đăng nhập
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        recyclerView = findViewById(R.id.recyclerView);
        fabAdd = findViewById(R.id.fabAdd);
        progressBar = findViewById(R.id.progressBar);
        emptyView = findViewById(R.id.emptyView);

        adapter = new NoteAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Click để sửa, long click để ghim
        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                showNoteDialog(note);
            }

            @Override
            public void onItemLongClick(Note note) {
                togglePin(note);
            }
        });

        fabAdd.setOnClickListener(v -> showNoteDialog(null));

        enableSwipeToDelete();
        loadNotesRealtime();
    }

    // Load danh sách ghi chú realtime
    private void loadNotesRealtime() {
        String uid = mAuth.getCurrentUser().getUid();
        progressBar.setVisibility(View.VISIBLE);

        Log.d(TAG, "Starting realtime listener for user: " + uid);

        listenerRegistration = db.collection("users")
                .document(uid)
                .collection("notes")
                .addSnapshotListener((snapshots, error) -> {
                    progressBar.setVisibility(View.GONE);

                    if (error != null) {
                        Log.e(TAG, "Listen failed: " + error.getMessage());
                        Toast.makeText(this, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (snapshots == null) {
                        Log.d(TAG, "Snapshots is null");
                        return;
                    }

                    Log.d(TAG, "Received snapshot with " + snapshots.size() + " documents");

                    noteList.clear();
                    for (DocumentSnapshot doc : snapshots.getDocuments()) {
                        Note note = doc.toObject(Note.class);
                        if (note != null) {
                            note.setId(doc.getId());
                            noteList.add(note);
                            Log.d(TAG, "Loaded note: " + note.getTitle());
                        }
                    }

                    noteListFull = new ArrayList<>(noteList);
                    adapter.setNotes(noteList);

                    // Hiển thị empty view nếu chưa có ghi chú
                    if (noteList.isEmpty()) {
                        emptyView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                });
    }

    // Dialog thêm/sửa ghi chú
    private void showNoteDialog(Note currentNote) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.dialog_note);
        dialog.setCancelable(true);

        EditText edtTitle = dialog.findViewById(R.id.edtTitle);
        EditText edtContent = dialog.findViewById(R.id.edtContent);
        CheckBox cbPinned = dialog.findViewById(R.id.cbPinned);
        LinearLayout colorContainer = dialog.findViewById(R.id.colorContainer);

        String[] selectedColor = {currentNote != null ? currentNote.getColor() : "#FFFFFF"};

        // Tạo các nút chọn màu
        for (String color : colors) {
            View colorView = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80, 80);
            params.setMargins(10, 10, 10, 10);
            colorView.setLayoutParams(params);
            colorView.setBackgroundColor(Color.parseColor(color));

            if (color.equals(selectedColor[0])) {
                colorView.setScaleX(1.2f);
                colorView.setScaleY(1.2f);
            }

            colorView.setOnClickListener(v -> {
                selectedColor[0] = color;
                for (int i = 0; i < colorContainer.getChildCount(); i++) {
                    View child = colorContainer.getChildAt(i);
                    child.setScaleX(1.0f);
                    child.setScaleY(1.0f);
                }
                v.setScaleX(1.2f);
                v.setScaleY(1.2f);
            });

            colorContainer.addView(colorView);
        }

        if (currentNote != null) {
            edtTitle.setText(currentNote.getTitle());
            edtContent.setText(currentNote.getContent());
            cbPinned.setChecked(currentNote.isPinned());
        }

        dialog.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());

        dialog.findViewById(R.id.btnSave).setOnClickListener(v -> {
            String title = edtTitle.getText().toString().trim();
            String content = edtContent.getText().toString().trim();
            boolean pinned = cbPinned.isChecked();

            if (title.isEmpty()) {
                title = "Không có tiêu đề";
            }

            String uid = mAuth.getCurrentUser().getUid();
            CollectionReference notesRef = db.collection("users")
                    .document(uid)
                    .collection("notes");

            if (currentNote == null) {
                // Thêm mới
                Note note = new Note(
                        null,
                        title,
                        content,
                        pinned,
                        selectedColor[0],
                        System.currentTimeMillis(),
                        uid
                );

                Log.d(TAG, "Adding new note: " + title);

                notesRef.add(note)
                        .addOnSuccessListener(docRef -> {
                            Log.d(TAG, "Note added successfully with ID: " + docRef.getId());
                            Toast.makeText(this, "Đã thêm ghi chú", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Failed to add note: " + e.getMessage());
                            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                // Cập nhật
                Log.d(TAG, "Updating note: " + currentNote.getId());

                currentNote.setTitle(title);
                currentNote.setContent(content);
                currentNote.setPinned(pinned);
                currentNote.setColor(selectedColor[0]);
                currentNote.setTimestamp(System.currentTimeMillis());

                notesRef.document(currentNote.getId())
                        .set(currentNote)
                        .addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "Note updated successfully");
                            Toast.makeText(this, "Đã cập nhật", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Failed to update note: " + e.getMessage());
                            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }

            dialog.dismiss();
        });

        dialog.show();
    }

    // Vuốt để xóa
    private void enableSwipeToDelete() {
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView rv,
                                  @NonNull RecyclerView.ViewHolder vh,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder vh, int direction) {
                Note note = adapter.getNotes().get(vh.getAdapterPosition());
                deleteNoteWithUndo(note);
            }
        };

        new ItemTouchHelper(callback).attachToRecyclerView(recyclerView);
    }

    // Xóa với hoàn tác
    private void deleteNoteWithUndo(Note note) {
        String uid = mAuth.getCurrentUser().getUid();

        Log.d(TAG, "Deleting note: " + note.getId());

        db.collection("users")
                .document(uid)
                .collection("notes")
                .document(note.getId())
                .delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Note deleted successfully"))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to delete note: " + e.getMessage());
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        Snackbar.make(recyclerView, "Đã xóa", Snackbar.LENGTH_LONG)
                .setAction("Hoàn tác", v -> {
                    Log.d(TAG, "Restoring note: " + note.getTitle());
                    db.collection("users")
                            .document(uid)
                            .collection("notes")
                            .add(note)
                            .addOnSuccessListener(docRef -> Log.d(TAG, "Note restored"))
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Failed to restore note: " + e.getMessage());
                                Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .show();
    }

    // Ghim/bỏ ghim
    private void togglePin(Note note) {
        String uid = mAuth.getCurrentUser().getUid();
        boolean newState = !note.isPinned();

        Log.d(TAG, "Toggling pin for note: " + note.getId() + " to " + newState);

        db.collection("users")
                .document(uid)
                .collection("notes")
                .document(note.getId())
                .update("pinned", newState)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Pin updated successfully");
                    String msg = newState ? "Đã ghim" : "Đã bỏ ghim";
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to update pin: " + e.getMessage());
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Tìm kiếm
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String q) {
                filter(q);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String q) {
                filter(q);
                return false;
            }
        });
        return true;
    }

    // Lọc ghi chú theo từ khóa
    private void filter(String q) {
        if (q == null || q.trim().isEmpty()) {
            adapter.setNotes(noteListFull);
            return;
        }

        List<Note> result = new ArrayList<>();
        String query = removeDiacritics(q.toLowerCase());

        for (Note n : noteListFull) {
            String title = removeDiacritics(n.getTitle().toLowerCase());
            String content = removeDiacritics(n.getContent().toLowerCase());

            if (title.contains(query) || content.contains(query)) {
                result.add(n);
            }
        }
        adapter.setNotes(result);
    }

    // Loại bỏ dấu tiếng Việt
    private String removeDiacritics(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
    }

    // Đăng xuất
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        return true;
    }

    // Hủy listener khi destroy
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listenerRegistration != null) {
            Log.d(TAG, "Removing listener");
            listenerRegistration.remove();
        }
    }
}