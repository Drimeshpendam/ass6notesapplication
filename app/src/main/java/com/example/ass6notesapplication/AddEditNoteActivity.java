package com.example.ass6notesapplication;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditNoteActivity extends AppCompatActivity {

    private EditText titleEditText, contentEditText;
    private Button saveButton, deleteButton;
    private NotesDatabaseHelper dbHelper;
    private int noteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        dbHelper = new NotesDatabaseHelper(this);
        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);

        // Check if we're editing an existing note
        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId", -1);

        if (noteId != -1) {
            titleEditText.setText(intent.getStringExtra("noteTitle"));
            contentEditText.setText(intent.getStringExtra("noteContent"));
            deleteButton.setVisibility(View.VISIBLE);
        }

        saveButton.setOnClickListener(v -> saveNote());
        deleteButton.setOnClickListener(v -> deleteNote());
    }

    private void saveNote() {
        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();

        if (noteId == -1) {
            // Add new note
            dbHelper.addNote(title, content);
        } else {
            // Update existing note
            dbHelper.updateNote(noteId, title, content);
        }

        finish();
    }

    private void deleteNote() {
        if (noteId != -1) {
            dbHelper.deleteNote(noteId);
            finish();
        }
    }
}
