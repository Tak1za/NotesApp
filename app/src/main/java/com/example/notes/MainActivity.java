package com.example.notes;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static java.util.Arrays.asList;

public class MainActivity extends AppCompatActivity {
    ListView notesListView;
    static ArrayList<String> notesList;
    static ArrayAdapter<String> arrayAdapter;
    SharedPreferences sharedPreferences;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addNote:
                Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        actionBar.setDisplayShowTitleEnabled(false);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.background_dark));
        sharedPreferences = this.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);

        notesListView = findViewById(R.id.notesListView);

        String data = sharedPreferences.getString("notes", null);

        if(data == null){
            notesList = new ArrayList<String>();
        } else {
            notesList = new ArrayList(Arrays.asList(data.split(",")));
        }
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.my_list, R.id.note, notesList);
        notesListView.setAdapter(arrayAdapter);

        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
                view.startAnimation(anim);
                Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
                intent.putExtra("noteNumber", position);
                startActivity(intent);
            }
        });

        notesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notesList.remove(position);
                                arrayAdapter.notifyDataSetChanged();

                                StringBuilder sb = new StringBuilder();
                                for(int i=0; i<notesList.size(); i++){
                                    sb.append(notesList.get(i)).append(",");
                                }
                                sharedPreferences.edit().putString("notes", sb.toString()).apply();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }
        });
    }
}
