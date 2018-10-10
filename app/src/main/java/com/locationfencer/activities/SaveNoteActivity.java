package com.locationfencer.activities;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.locationfencer.R;
import com.locationfencer.database.AppDatabase;
import com.locationfencer.database.Note;
import com.locationfencer.utils.AppGlobals;
import com.locationfencer.utils.AppUtils;

import java.util.UUID;

public class SaveNoteActivity extends Activity {

    AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtils.setFullScreen(this);
        setContentView(R.layout.activity_save_note);
        getAppDatabase();
        bindListener();
    }

    private void getAppDatabase() {
        appDatabase = Room.databaseBuilder(getApplicationContext(),AppDatabase.class, AppGlobals.DATABASE_NAME).allowMainThreadQueries().build();
    }

    private void bindListener() {
        findViewById(R.id.btn_create_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String noteText = findFieldById(R.id.et_note_text).getText().toString();

                if(noteText.isEmpty()){
                    Toast.makeText(SaveNoteActivity.this, "Please enter note text", Toast.LENGTH_SHORT).show();
                    return;
                }

                String noteId = UUID.randomUUID().toString();

                Note note = new Note(noteId,noteText);
                appDatabase.appDao().insertNote(note);
                Toast.makeText(SaveNoteActivity.this, "Note create successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public EditText findFieldById(@IdRes int id){
        return (EditText) findViewById(id);
    }
}
