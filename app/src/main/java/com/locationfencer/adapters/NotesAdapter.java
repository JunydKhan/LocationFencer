package com.locationfencer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.locationfencer.R;
import com.locationfencer.database.Note;

import java.util.List;

/**
 * Created by JUNAID_KHAN on 10/10/2018.
 */

public class NotesAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<Note> noteList;
    private OnNoteMarkAsCallback noteMarkAsCallback;

    public NotesAdapter(Context context, List<Note> noteList){
        this.context = context;
        this.noteMarkAsCallback = (OnNoteMarkAsCallback) context;
        this.noteList = noteList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.item_note,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Note note = noteList.get(position);
        NotesViewHolder notesViewHolder = (NotesViewHolder) holder;
        notesViewHolder.textViewNoteText.setText(note.getNoteText());
        notesViewHolder.textViewNoteStatus.setText(note.getNoteStatus());
        if(note.isCompleted()){
            notesViewHolder.checkBoxMarkAs.setChecked(true);
            notesViewHolder.textViewNoteStatus.setTextColor(context.getResources().getColor(R.color.colorGreen));
        }else{
            notesViewHolder.checkBoxMarkAs.setChecked(false);
            notesViewHolder.textViewNoteStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
        bindListeners(notesViewHolder,position);
    }

    private void bindListeners(NotesViewHolder viewHolder, final int position) {
        viewHolder.checkBoxMarkAs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isPressed()){
                     if(noteMarkAsCallback != null)
                         noteMarkAsCallback.onMarkedAs(noteList.get(position),isChecked);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
    public class NotesViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewNoteText, textViewNoteStatus;
        private CheckBox checkBoxMarkAs;

        public NotesViewHolder(View itemView) {
            super(itemView);
            textViewNoteText = itemView.findViewById(R.id.tv_note_text);
            textViewNoteStatus = itemView.findViewById(R.id.tv_note_status);
            checkBoxMarkAs = itemView.findViewById(R.id.cb_mark_as);
        }
    }

    public interface OnNoteMarkAsCallback {
        void onMarkedAs(Note note, boolean isCompleted);
    }
}
