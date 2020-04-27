package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;

import java.util.HashSet;

import static com.example.notes.MainActivity.notesList;
import static com.example.notes.MainActivity.arrayAdapter;

public class NoteActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    EditText titleEditText;
    EditText noteEditText;
    int noteNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        titleEditText = findViewById(R.id.titleEditText);
        noteEditText = findViewById(R.id.noteEditText);

        sharedPreferences = this.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.background_dark));

        Intent intent = getIntent();
        noteNumber = intent.getIntExtra("noteNumber", -1);

        if(noteNumber != -1) {
            titleEditText.setText(notesList.get(noteNumber));
            noteEditText.setText(sharedPreferences.getString(notesList.get(noteNumber), ""));
        } else {
            notesList.add("");
            noteNumber = notesList.size() - 1;
        }
    }

    @Override
    public void onBackPressed() {
        if(titleEditText.getText().toString().equals("") || titleEditText.getText().toString() == null){
            int noteTitle = notesList.size();
            sharedPreferences.edit().putString("Note " + noteTitle, noteEditText.getText().toString()).apply();
            notesList.set(noteNumber, "Note " + noteTitle);
        }else{
            sharedPreferences.edit().putString(titleEditText.getText().toString(), noteEditText.getText().toString()).apply();
            notesList.set(noteNumber, titleEditText.getText().toString());
        }
        arrayAdapter.notifyDataSetChanged();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<notesList.size(); i++){
            sb.append(notesList.get(i)).append(",");
        }
        sharedPreferences.edit().putString("notes", sb.toString()).apply();
        super.onBackPressed();
    }
}
