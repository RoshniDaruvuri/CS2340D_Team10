package com.example.wandersyncteam10.view;

import com.example.wandersyncteam10.Model.Note;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.wandersyncteam10.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth; // Added import
import com.google.firebase.auth.FirebaseUser; // Added import
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashSet;
import java.util.Set;

/**
 * Activity for managing and displaying notes.
 */
public class NotesActivity extends AppCompatActivity {

    private static final String TAG = "NotesActivity";

    private FirebaseFirestore db;
    private FirebaseAuth mAuth; // FirebaseAuth instance
    private LinearLayout notesContainer;
    private Set<String> notesSet;
    private String userId; // Dynamic user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        notesContainer = findViewById(R.id.notesContainer);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth
        notesSet = new HashSet<>();

        // Get current user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
            // Restore notes from Firestore
            restoreNotes();
        } else {
            // Handle the case where the user is not authenticated
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            // Redirect to login screen or handle appropriately
            Intent intent = new Intent(NotesActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        findViewById(R.id.backButton).setOnClickListener(view -> {
            Intent intent = new Intent(NotesActivity.this, LogisticsActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.addButton).setOnClickListener(v -> {
            findViewById(R.id.inputLayout).setVisibility(View.VISIBLE);
            EditText inputText = findViewById(R.id.inputText);
            inputText.requestFocus();
        });

        EditText inputText = findViewById(R.id.inputText);
        inputText.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                addNote();
                return true;
            }
            return false;
        });
    }

    /**
     * Adds a new note when the user presses Enter.
     */
    private void addNote() {
        EditText inputText = findViewById(R.id.inputText);
        String noteText = inputText.getText().toString().trim();

        if (!noteText.isEmpty()) {
            // Save note to Firestore
            saveNoteToFirestore(noteText);

            // Create a new TextView to display the note
            TextView noteTextView = new TextView(this);
            noteTextView.setText(noteText);
            noteTextView.setTextSize(16);
            noteTextView.setPadding(16, 16, 16, 16);
            notesContainer.addView(noteTextView);

            inputText.setText(""); // Clear the EditText
            findViewById(R.id.inputLayout).setVisibility(View.GONE); // Hide the layout
        } else {
            Toast.makeText(this, "Note cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Saves the given note to Firestore under the specific user's collection.
     *
     * @param note The note text to be saved.
     */
    private void saveNoteToFirestore(String note) {
        // Create a note object
        Note newNote = new Note(note);

        // Save to Firestore under the authenticated user's collection
        db.collection("users")
                .document(userId)
                .collection("notes")
                .add(newNote)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Note successfully written with ID: " + documentReference.getId());
                    Toast.makeText(NotesActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding note", e);
                    Toast.makeText(NotesActivity.this, "Failed to save note", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Restores notes from Firestore and displays them in the notes container.
     */
    private void restoreNotes() {
        db.collection("users")
                .document(userId)
                .collection("notes")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        notesContainer.removeAllViews(); // Clear existing views
                        for (DocumentSnapshot document : task.getResult()) {
                            Note note = document.toObject(Note.class);
                            if (note != null && note.getText() != null) {
                                String noteText = note.getText();
                                // Avoid adding duplicate notes
                                if (!notesSet.contains(noteText)) {
                                    // Create a new TextView for each saved note
                                    TextView noteTextView = new TextView(this);
                                    noteTextView.setText(noteText);
                                    noteTextView.setTextSize(16);
                                    noteTextView.setPadding(16, 16, 16, 16);
                                    notesContainer.addView(noteTextView);
                                    notesSet.add(noteText);
                                }
                            }
                        }
                        Log.d(TAG, "Notes successfully restored");
                    } else {
                        Log.w(TAG, "Error getting notes.", task.getException());
                        Toast.makeText(this, "Failed to load notes", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
