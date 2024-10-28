package com.example.wandersyncteam10.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.wandersyncteam10.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashSet;
import java.util.Set;

public class NotesActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private LinearLayout notesContainer;
    private Set<String> notesSet;
    private String userId = "unique_user_id"; // Replace this with the actual user ID

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
        notesSet = new HashSet<>();

        // Restore notes from Firestore
        restoreNotes();

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

        // Save to Firestore under a specific user's collection
        db.collection("users")
                .document(userId)
                .collection("notes")
                .add(newNote)
                .addOnSuccessListener(documentReference -> {
                    // Optionally handle success
                })
                .addOnFailureListener(e -> {
                    // Optionally handle failure
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
                        for (DocumentSnapshot document : task.getResult()) {
                            String noteText = document.toObject(Note.class).getText();
                            // Create a new TextView for each saved note
                            TextView noteTextView = new TextView(this);
                            noteTextView.setText(noteText);
                            noteTextView.setTextSize(16);
                            noteTextView.setPadding(16, 16, 16, 16);
                            notesContainer.addView(noteTextView);
                        }
                    }
                });
    }
}


// Create a Note class to represent a note object
class Note {
    private String text;

    /**
     * need for fire store
     * */
    public Note() {

    } // Needed for Firestore

    /**
     * @param text
     * note text
     * */
    public Note(String text) {
        this.text = text;
    }

    /**
     * get text
     * @return text
     * */
    public String getText() {
        return text;
    }

    /**
     * @param text
     * note text
     * */
    public void setText(String text) {
        this.text = text;
    }
}
