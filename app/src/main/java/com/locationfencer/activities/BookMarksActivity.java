package com.locationfencer.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.locationfencer.R;
import com.locationfencer.adapters.BookmarksAdapter;
import com.locationfencer.database.AppDatabase;
import com.locationfencer.database.BookMark;
import com.locationfencer.utils.AppGlobals;
import com.locationfencer.utils.AppUtils;
import com.locationfencer.utils.BackNavigationActivity;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class BookMarksActivity extends BackNavigationActivity implements BookmarksAdapter.BookmarkDeleteCallback, BookmarksAdapter.BookmarkOnDirectionsCallabck {

    private static final int PLACE_PICKER_REQUEST = 112;
    private AppDatabase appDatabse;
    private List<BookMark> bookmarksList;
    private BookmarksAdapter adapter;
    private Place place;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtils.setFullScreen(this);
        setContentView(R.layout.activity_book_marks);
        getAppDatabase();
        bindListeners();
        fetchBookmarkListing();
    }

    private void bindListeners() {
        findViewById(R.id.iv_add_bookmark).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(BookMarksActivity.this), PLACE_PICKER_REQUEST);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        setOnBackClickListener();
    }

    private void getAppDatabase() {
        appDatabse = Room.databaseBuilder(this, AppDatabase.class, AppGlobals.DATABASE_NAME).allowMainThreadQueries().build();
    }

    private void fetchBookmarkListing() {
        bookmarksList = appDatabse.appDao().getAllBookmarks();
        if (bookmarksList.size() == 0)
            hideListingIfNotFound(bookmarksList);

        Collections.reverse(bookmarksList);
        adapter = new BookmarksAdapter(this, bookmarksList);
        RecyclerView recyclerView = findViewById(R.id.rv_bookmarks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void hideListingIfNotFound(List<BookMark> bookmarksList) {
        if (bookmarksList.size() == 0) {
            findViewById(R.id.tv_no_bookmarks_found).setVisibility(View.VISIBLE);
            findViewById(R.id.rv_bookmarks).setVisibility(View.GONE);
        } else {
            findViewById(R.id.rv_bookmarks).setVisibility(View.VISIBLE);
            findViewById(R.id.tv_no_bookmarks_found).setVisibility(View.GONE);
        }
    }

    @Override
    public void onBookmarkDelete(final BookMark bookMark) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Bookmark");
        builder.setMessage("Are you sure you to delete this bookmark?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                bookmarksList.remove(bookMark);
                appDatabse.appDao().deleteBookMark(bookMark);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onDirectionClick(BookMark bookMark) {
        double latitude = bookMark.getLatitude();
        double longitude = bookMark.getLongitude();

        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a maps application", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                place = PlacePicker.getPlace(data, this);
                String bookMarkId = UUID.randomUUID().toString();
                String placeName = place.getName().toString();
                double latitude = place.getLatLng().latitude;
                double longitude = place.getLatLng().longitude;

                BookMark bookMark = new BookMark(bookMarkId, placeName, latitude, longitude);
                appDatabse.appDao().insertBookMark(bookMark);
                bookmarksList.add(bookMark);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
