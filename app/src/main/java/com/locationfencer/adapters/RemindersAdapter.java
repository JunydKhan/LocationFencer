package com.locationfencer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.locationfencer.R;
import com.locationfencer.database.Location;

import java.util.List;

/**
 * Created by JUNAID_KHAN on 15/10/2018.
 */

public class RemindersAdapter extends RecyclerView.Adapter{

    private Context context;
    private List<Location> locationList;
    private OnReminderGeofencingCallback onReminderGeofencingCallback;
    private OnReminderDeleteCallback onReminderDeleteCallback;

    public interface OnReminderGeofencingCallback {
        void onReminderMarkerClick(int position);
    }

    public interface OnReminderDeleteCallback {
        void onReminderDelete(int position);
    }

    public RemindersAdapter(Context context, List<Location> locationList) {
        this.context = context;
        this.onReminderGeofencingCallback = (OnReminderGeofencingCallback) context;
        this.onReminderDeleteCallback = (OnReminderDeleteCallback) context;
        this.locationList = locationList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RemindersViewHolder(LayoutInflater.from(context).inflate(R.layout.item_reminder,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        RemindersViewHolder remindersViewHolder = (RemindersViewHolder) holder;
        final Location location = locationList.get(position);

        remindersViewHolder.textViewPlaceName.setText(location.getPlaceName());
        remindersViewHolder.textViewReminderText.setText(location.getReminder());
        remindersViewHolder.textViewLatLng.setText(location.getLatitude() + " , " + location.getLongitude());
        remindersViewHolder.imageViewGeofence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onReminderGeofencingCallback != null){
                    onReminderGeofencingCallback.onReminderMarkerClick(position);
                }
            }
        });
        remindersViewHolder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onReminderDeleteCallback != null){
                    onReminderDeleteCallback.onReminderDelete(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    class RemindersViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewPlaceName,textViewLatLng,textViewReminderText;
        private ImageView imageViewDelete,imageViewGeofence;
        public RemindersViewHolder(View itemView) {
            super(itemView);
            textViewPlaceName = itemView.findViewById(R.id.tv_place_name);
            textViewLatLng = itemView.findViewById(R.id.tv_place_latlng);
            textViewReminderText = itemView.findViewById(R.id.tv_reminder_text);
            imageViewDelete = itemView.findViewById(R.id.iv_delete_reminder);
            imageViewGeofence = itemView.findViewById(R.id.iv_marker);
        }
    }
}
