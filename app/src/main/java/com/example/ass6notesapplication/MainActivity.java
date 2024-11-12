package com.example.ass6notesapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NotesDatabaseHelper dbHelper;
    private ListView listView;
    private Button addNoteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new NotesDatabaseHelper(this);
        listView = findViewById(R.id.listView);
        addNoteButton = findViewById(R.id.addNoteButton);

        // Set up the Add Note button
        addNoteButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
            startActivity(intent);
        });

        // Display all notes
        displayNotes();

        // Set up the item click listener for editing and deleting notes
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Note selectedNote = (Note) parent.getItemAtPosition(position);
            Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
            intent.putExtra("noteId", selectedNote.getId());
            intent.putExtra("noteTitle", selectedNote.getTitle());
            intent.putExtra("noteContent", selectedNote.getContent());
            startActivity(intent);
        });
    }

    // Method to display all notes
    private void displayNotes() {
        List<Note> notes = dbHelper.getAllNotes();
        ArrayAdapter<Note> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(adapter);
    }
}
