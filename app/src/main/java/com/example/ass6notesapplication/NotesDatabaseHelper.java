package com.example.ass6notesapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NotesDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notes_db";
    private static final int DATABASE_VERSION = 1;

    // Table and column names
    private static final String TABLE_NOTES = "notes";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CONTENT = "content";

    // SQL query to create the table
    private static final String CREATE_TABLE_NOTES = "CREATE TABLE " + TABLE_NOTES + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TITLE + " TEXT, " +
            COLUMN_CONTENT + " TEXT);";

    // Constructor
    public NotesDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the notes table
        db.execSQL(CREATE_TABLE_NOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If the database is upgraded, delete the existing table and create a new one
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    // Add a new note to the database
    public long addNote(String title, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_CONTENT, content);

        return db.insert(TABLE_NOTES, null, values);
    }

    // Get all notes from the database
    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTES, null, null, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                    String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                    String content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT));

                    notes.add(new Note(id, title, content));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return notes;
    }

    // Get a single note by ID
    public Note getNoteById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTES, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                String content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT));
                cursor.close();
                return new Note(id, title, content);
            }
            cursor.close();
        }
        return null;
    }

    // Update a note
    public int updateNote(int id, String title, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_CONTENT, content);

        return db.update(TABLE_NOTES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Delete a note
    public void deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }
}
