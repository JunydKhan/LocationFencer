package com.locationfencer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.locationfencer.R;
import com.locationfencer.database.BookMark;

import java.util.List;

/**
 * Created by JUNAID_KHAN on 10/10/2018.
 */

public class BookmarksAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<BookMark> bookMarkList;
    private BookmarkDeleteCallback bookmarkDeleteCallback;
    private BookmarkOnDirectionsCallabck bookmarkOnDirectionsCallabck;

    public BookmarksAdapter(Context context, List<BookMark> bookMarkList){
        this.context = context;
        this.bookmarkDeleteCallback = (BookmarkDeleteCallback) context;
        this.bookmarkOnDirectionsCallabck = (BookmarkOnDirectionsCallabck) context;
        this.bookMarkList = bookMarkList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BookmarksViewHolder(LayoutInflater.from(context).inflate(R.layout.item_bookmark,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BookMark bookMark = bookMarkList.get(position);
        BookmarksViewHolder bookmarksViewHolder = (BookmarksViewHolder) holder;
        bookmarksViewHolder.textViewPlaceName.setText(bookMark.getPlaceName());
        bookmarksViewHolder.textViewLatLng.setText("(" + bookMark.getLatitude() + " , " + bookMark.getLatitude() + ")");

        bindListeners(bookmarksViewHolder,position);
    }

    private void bindListeners(final BookmarksViewHolder bookmarksViewHolder, final int position) {
        bookmarksViewHolder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bookmarkDeleteCallback != null)
                    bookmarkDeleteCallback.onBookmarkDelete(bookMarkList.get(position));
            }
        });

        bookmarksViewHolder.imageViewDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bookmarkOnDirectionsCallabck != null)
                    bookmarkOnDirectionsCallabck.onDirectionClick(bookMarkList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookMarkList.size();
    }

    public class BookmarksViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewPlaceName,textViewLatLng;
        private ImageView imageViewDelete, imageViewDirections;
        public BookmarksViewHolder(View itemView) {
            super(itemView);
            textViewPlaceName = itemView.findViewById(R.id.tv_place_name);
            textViewLatLng = itemView.findViewById(R.id.tv_lat_lng);
            imageViewDelete = itemView.findViewById(R.id.iv_delete_bookmark);
            imageViewDirections = itemView.findViewById(R.id.iv_directions);
        }
    }

    public interface BookmarkDeleteCallback {
        void onBookmarkDelete(BookMark bookMark);
    }

    public interface BookmarkOnDirectionsCallabck{
        void onDirectionClick(BookMark bookMark);
    }
}
