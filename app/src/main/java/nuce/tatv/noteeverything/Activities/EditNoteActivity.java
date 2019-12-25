package nuce.tatv.noteeverything.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;

import nuce.tatv.noteeverything.Models.Note;
import nuce.tatv.noteeverything.R;
import nuce.tatv.noteeverything.Remotes.GetDataService;
import nuce.tatv.noteeverything.Remotes.RetrofitClientInstance;
import nuce.tatv.noteeverything.Utils.DateFormat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditNoteActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tvTitle, tvContent;
    private Button btnEditNote, btnExit;
    private String note_date, note_title, note_content, user_name;
    private Integer note_id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_item_edit);
        init();
    }

    private void init() {
        tvTitle = findViewById(R.id.tvTitle);
        tvContent = findViewById(R.id.tvContent);
        btnEditNote = findViewById(R.id.btnEditNote);
        btnExit = findViewById(R.id.btnExit);

        Intent intentGetNote = getIntent();
        Bundle bundleNote = intentGetNote.getExtras();
        if (bundleNote != null){
            note_id = bundleNote.getInt("note_id");
            note_title = bundleNote.getString("note_title");
            note_content = bundleNote.getString("note_content");
            note_date = bundleNote.getString("note_date");
            user_name = bundleNote.getString("user_name");

            tvTitle.setText(note_title);
            tvContent.setText(note_content);
        }

        btnEditNote.setOnClickListener(this);
        btnExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnEditNote:
                if(checkValidation()){
                    DateFormat dateFormat = new DateFormat();
                    try {
                        note_date = dateFormat.Format(note_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Note note = new Note(note_id,tvTitle.getText().toString(), tvContent.getText().toString(), user_name, note_date);
                    GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                    Call<Note> call = service.updateNote(note);
                    call.enqueue(new Callback<Note>() {
                        @Override
                        public void onResponse(Call<Note> call, Response<Note> response) {
                            Log.d("edit_note", response.message());
                            Toast.makeText(EditNoteActivity.this, "Sửa thành công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<Note> call, Throwable t) {
                            Log.d("edit_note", t.getMessage());
                        }
                    });
                }
                break;
            case R.id.btnExit:
                finish();
                break;
        }
    }

    public boolean checkValidation(){
        note_title = tvTitle.getText().toString();
        note_content = tvContent.getText().toString();
        if (note_title == null || note_title.equals("")){
            tvTitle.setError("Không được để trống tiêu đề!");
            tvTitle.requestFocus();
            return false;
        }
        if (note_content == null || note_content.equals("")){
            tvContent.setError("Không được để trống nội dung!");
            tvContent.requestFocus();
            return false;
        }
        return true;
    }
}
