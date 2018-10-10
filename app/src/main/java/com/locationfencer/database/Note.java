package com.locationfencer.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by JUNAID_KHAN on 09/10/2018.
 */

@Entity(tableName = "notes")
public class Note {
    @NonNull
    @PrimaryKey
    private String noteId;

    @ColumnInfo(name = "noteText")
    private String noteText;

    @ColumnInfo(name = "noteStatus")
    private String noteStatus = "pending";

    @ColumnInfo(name = "isCompleted")
    private boolean isCompleted = false;

    public Note(@NonNull String noteId, String noteText) {
        this.noteId = noteId;
        this.noteText = noteText;
    }

    public String getNoteStatus() {
        return noteStatus;
    }

    public void setNoteStatus(String noteStatus) {
        this.noteStatus = noteStatus;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @NonNull
    public String getNoteId() {
        return noteId;
    }

    public String getNoteText() {
        return noteText;
    }
}
