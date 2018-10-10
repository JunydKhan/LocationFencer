package com.locationfencer.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by JUNAID_KHAN on 09/10/2018.
 */

@Dao
public interface AppDao  {

    /*CREATE NOTES CRUDS*/
    @Query("SELECT * FROM notes")
    List<Note> getAllNotes();

    @Insert
    void insertNote(Note note);

    @Update
    void updateNote(Note note);


    /*SET REMINDERS CRUDS*/
    @Query("SELECT * FROM locations WHERE requestId LIKE :requestId")
    List<Location> selectLocations(String requestId);

    @Insert
    void insertLocation(Location location);

    /*SET BOOKMARKS CRUDS*/
    @Insert
    void insertBookMark(BookMark bookMark);

    @Delete
    void deleteBookMark(BookMark bookMark);

    @Query("SELECT * FROM bookmark")
    List<BookMark> getAllBookmarks();


}
