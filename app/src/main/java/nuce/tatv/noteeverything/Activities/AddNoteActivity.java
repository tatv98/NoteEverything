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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import nuce.tatv.noteeverything.Models.Note;
import nuce.tatv.noteeverything.R;
import nuce.tatv.noteeverything.Remotes.GetDataService;
import nuce.tatv.noteeverything.Remotes.RetrofitClientInstance;
import nuce.tatv.noteeverything.Utils.DateFormat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvTitle, tvContent;
    private Button btnAddNote, btnExit;
    private String note_date, note_title, note_content;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_item_add);
        init();
    }

    private void init() {
        tvTitle = findViewById(R.id.tvTitle);
        tvContent = findViewById(R.id.tvContent);
        btnAddNote = findViewById(R.id.btnAddNote);
        btnExit = findViewById(R.id.btnExit);

        btnAddNote.setOnClickListener(this);
        btnExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddNote:
                if(checkValidation()){
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    DateFormat date = new DateFormat();
                    try {
                        note_date = date.Format(sdf.format(new Date()));
                        Log.d("123456", note_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Note note = new Note(note_title, note_content, "trinhta", note_date);
                    GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                    Call<Note> call = service.postNote(note);
                    call.enqueue(new Callback<Note>() {
                        @Override
                        public void onResponse(Call<Note> call, Response<Note> response) {
                            Log.d("1234", response.message());
                            Toast.makeText(AddNoteActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<Note> call, Throwable t) {
                            Log.d("12345", t.getMessage());
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
            tvTitle.setError("Bạn phải nhập tiêu đề!");
            tvTitle.requestFocus();
            return false;
        }
        if (note_content == null || note_content.equals("")){
            tvContent.setError("Bạn phải nhập nội dung!");
            tvContent.requestFocus();
            return false;
        }
        return true;
    }
}
