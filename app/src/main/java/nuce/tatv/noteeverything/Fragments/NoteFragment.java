package nuce.tatv.noteeverything.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nuce.tatv.noteeverything.Activities.AddNoteActivity;
import nuce.tatv.noteeverything.Activities.MainActivity;
import nuce.tatv.noteeverything.Adapters.NoteAdapter;
import nuce.tatv.noteeverything.Helpers.RecyclerItemTouchHelperForNote;
import nuce.tatv.noteeverything.Helpers.RecyclerItemTouchHelperListener;
import nuce.tatv.noteeverything.Models.Note;
import nuce.tatv.noteeverything.Models.Response;
import nuce.tatv.noteeverything.R;
import nuce.tatv.noteeverything.Remotes.GetDataService;
import nuce.tatv.noteeverything.Remotes.RetrofitClientInstance;
import nuce.tatv.noteeverything.Utils.DateFormat;
import retrofit2.Call;
import retrofit2.Callback;

public class NoteFragment extends Fragment implements RecyclerItemTouchHelperListener, SwipeRefreshLayout.OnRefreshListener {
    public static final int REQUEST_CODE_ADD = 111;
    public static final int REQUEST_CODE_EDIT = 112;
    public static int POSITION_MAX;
    private CoordinatorLayout rootLayout;
    private View vNotes;
    public List<Note> listNote;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private RecyclerView gvListNote;
    private NoteAdapter noteAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vNotes = inflater.inflate(R.layout.note_fragment, container, false);
        init();
        return vNotes;
    }

    private void init() {
        setHasOptionsMenu(true);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Note");

        listNote = new ArrayList<>();
        gvListNote = vNotes.findViewById(R.id.vgListNote);
        rootLayout = vNotes.findViewById(R.id.rootLayout);
        mySwipeRefreshLayout = vNotes.findViewById(R.id.mySwipeRefreshLayout);
        mySwipeRefreshLayout.setOnRefreshListener(this);

        showAllNote();

        ItemTouchHelper.Callback itemTouchHelper
                = new RecyclerItemTouchHelperForNote(this);
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(gvListNote);
    }
    public void showAllNote(){
        POSITION_MAX = 0;
        listNote.clear();
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<Response> call = service.getNote(MainActivity.USER_NAME);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                for (Note note: response.body().getData().getNote()) {
                    if (note.getNotePosition() > POSITION_MAX) {
                        POSITION_MAX = note.getNotePosition();
                    }
                    listNote.add(note);
                    noteAdapter = new NoteAdapter(getActivity(), R.layout.note_item_custom, listNote);
                    gvListNote.setAdapter(noteAdapter);
                    gvListNote.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    gvListNote.setItemAnimator(new DefaultItemAnimator());
                    noteAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });
    }

    @Override
    public void onMove(RecyclerView.ViewHolder viewHolder, int oldPosition, int newPosition) {
        if (viewHolder instanceof NoteAdapter.MyViewHolder) {
            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Note noteI = listNote.get(oldPosition);
            Note noteJ = listNote.get(newPosition);
            int tempPosition = noteI.getNotePosition();
            noteI.setNotePosition(noteJ.getNotePosition());
            noteJ.setNotePosition(tempPosition);

            DateFormat dateFormat = new DateFormat();
            try {
                noteI.setNoteDate(dateFormat.Format(noteI.getNoteDate()));
                noteJ.setNoteDate(dateFormat.Format(noteJ.getNoteDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Call<Response> callNoteI = service.updateNote(noteI);
            callNoteI.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {

                }
            });
            Call<Response> callNoteJ = service.updateNote(noteJ);
            callNoteJ.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {

                }
            });

            if (oldPosition < newPosition) {
                for (int i = oldPosition; i < newPosition; i++) {
                    Collections.swap(listNote, i, i + 1);
                }
            } else {
                for (int i = oldPosition; i > newPosition; i--) {
                    Collections.swap(listNote, i, i - 1);
                }
            }
            noteAdapter.notifyItemMoved(oldPosition, newPosition);
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof NoteAdapter.MyViewHolder){
            String note_title = listNote.get(viewHolder.getAdapterPosition()).getNoteTitle();
            final Note note_delete = listNote.get(viewHolder.getAdapterPosition());
            final int note_position = viewHolder.getAdapterPosition();
            noteAdapter.removeNote(note_position);
            //---remove note
            final GetDataService svDelete = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            final Call<Response> callDeleteNote = svDelete.deleteNote(note_delete);
            callDeleteNote.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    Log.d("12345", response.message());
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    Log.d("123456", t.getMessage());
                }
            });

            Snackbar snackbar = Snackbar.make(rootLayout, note_title + " removed from notes!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noteAdapter.restoreNote(note_delete, note_position);
                    final GetDataService svRestore = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                    DateFormat dateFormat = new DateFormat();
                    try {
                        note_delete.setNoteDate(dateFormat.Format(note_delete.getNoteDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Call<Response> callRestoreNote = svRestore.postNote(note_delete);
                    callRestoreNote.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            Log.d("note_restore", response.message());
                        }

                        @Override
                        public void onFailure(Call<Response> call, Throwable t) {
                            Log.d("note_restore", t.getMessage());
                        }
                    });
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem itemNote = menu.add(1, R.id.itemAddNote, 1, "Thêm Note");
        itemNote.setIcon(R.drawable.ic_add_diary);
        itemNote.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemAddNote:
                startActivityForResult(new Intent(getActivity(), AddNoteActivity.class), REQUEST_CODE_ADD);
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD || requestCode == REQUEST_CODE_EDIT){
            if(resultCode == Activity.RESULT_OK){
                mySwipeRefreshLayout.setRefreshing(true);
                showAllNote();
                mySwipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    @Override
    public void onRefresh() {
        showAllNote();
        mySwipeRefreshLayout.setRefreshing(false);
    }
}
