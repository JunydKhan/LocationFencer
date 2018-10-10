package com.locationfencer.activities;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.locationfencer.R;
import com.locationfencer.adapters.NotesAdapter;
import com.locationfencer.database.AppDatabase;
import com.locationfencer.database.Note;
import com.locationfencer.utils.AppGlobals;
import com.locationfencer.utils.AppUtils;

import java.util.Collections;
import java.util.List;

public class ShowNotesActivity extends Activity implements NotesAdapter.OnNoteMarkAsCallback {

    AppDatabase appDatabase;
    private List<Note> noteList;
    private NotesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtils.setFullScreen(this);
        setContentView(R.layout.activity_show_notes);
        getAppDatabase();
        bindListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showNotesListing();
    }

    private void bindListener() {
        findViewById(R.id.iv_add_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowNotesActivity.this, SaveNoteActivity.class));
            }
        });
    }

    private void getAppDatabase() {
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, AppGlobals.DATABASE_NAME).allowMainThreadQueries().build();
    }

    private void showNotesListing() {
        noteList = appDatabase.appDao().getAllNotes();
        hideListingIfNotFoundNotes(noteList);

        //TO CHANGE THE ORDER BY DESC
        Collections.reverse(noteList);
        adapter = new NotesAdapter(this, noteList);
        RecyclerView recyclerView = findViewById(R.id.rv_notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void hideListingIfNotFoundNotes(List<Note> noteList) {
        if (noteList.size() == 0) {
            findViewById(R.id.tv_no_notes_found).setVisibility(View.VISIBLE);
            findViewById(R.id.rv_notes).setVisibility(View.GONE);
        } else {
            findViewById(R.id.rv_notes).setVisibility(View.VISIBLE);
            findViewById(R.id.tv_no_notes_found).setVisibility(View.GONE);
        }

        return;
    }

    @Override
    public void onMarkedAs(Note note, boolean isCompleted) {
        int index = noteList.indexOf(note);
        if (index != -1) {
            Note newNote = noteList.get(index);
            newNote.setCompleted(isCompleted);
            if (isCompleted)
                newNote.setNoteStatus("Completed");
            else
                newNote.setNoteStatus("Pending");

            noteList.remove(index);
            noteList.add(index, newNote);
            adapter.notifyDataSetChanged();
            appDatabase.appDao().updateNote(newNote);
        }
    }

}
