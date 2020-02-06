package nuce.tatv.noteeverything.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nuce.tatv.noteeverything.Activities.EditNoteActivity;
import nuce.tatv.noteeverything.Activities.MainActivity;
import nuce.tatv.noteeverything.Fragments.NoteFragment;
import nuce.tatv.noteeverything.Models.Note;
import nuce.tatv.noteeverything.R;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder>{
    private Context context;
    private int layout;
    private List<Note> listNote;

    public NoteAdapter(Context context, int layout, List<Note> listNote) {
        this.context = context;
        this.layout = layout;
        this.listNote = listNote;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vNote = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);

        return new MyViewHolder(vNote);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Note note = listNote.get(position);
        holder.tvTitle.setText(note.getNoteTitle());
        holder.tvContent.setText(note.getNoteContent());
        holder.tvDate.setText(note.getNoteDate());

        List<String> listColor = new ArrayList<>();
        listColor.add("#CE93D8");
        listColor.add("#64B5F6");
        listColor.add("#66BB6A");
        listColor.add("#FDD835");
        listColor.add("#BCAAA4");
        listColor.add("#B0BEC5");

        String colorSelected = getRandomColor(listColor);
        holder.gvNoteItem.setBackgroundColor(Color.parseColor(colorSelected));

        holder.gvNoteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNote = new Intent(context, EditNoteActivity.class);
                Log.d("send_note", note.toString());
                Bundle bundleNote = new Bundle();
                bundleNote.putInt("note_id", note.getNoteId());
                bundleNote.putString("note_title", note.getNoteTitle());
                bundleNote.putString("note_content", note.getNoteContent());
                bundleNote.putString("note_date", note.getNoteDate());
                bundleNote.putString("user_name", note.getUserName());
                bundleNote.putInt("note_position", note.getNotePosition());
                intentNote.putExtras(bundleNote);
                ((MainActivity)context).startActivityForResult(intentNote, NoteFragment.REQUEST_CODE_EDIT);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listNote.size();
    }
    public void removeNote(int position){
        listNote.remove(position);
        notifyItemRemoved(position);
    }
    public void restoreNote(Note note, int position){
        listNote.add(position, note);
        notifyItemInserted(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvContent, tvTitle, tvDate;
        public LinearLayout gvNoteItem;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            gvNoteItem = itemView.findViewById(R.id.gvNoteItem);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }

    public static String getRandomColor(List<String> colors) {
        return colors.get(new Random().nextInt(colors.size()));
    }
}