package nuce.tatv.noteeverything.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import nuce.tatv.noteeverything.Models.Response;
import nuce.tatv.noteeverything.Models.Work;
import nuce.tatv.noteeverything.R;
import nuce.tatv.noteeverything.Remotes.GetDataService;
import nuce.tatv.noteeverything.Remotes.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;

public class AddWorkActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtTitle, edtDate, edtTime, edtPlace;
    private ImageView ivPickDate, ivPickTime;
    private Button btnAdd, btnExit;
    private final Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener date;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_add_layout);
        init();
    }

    private void init() {
        edtTitle = findViewById(R.id.edtTitle);
        edtDate = findViewById(R.id.edtDate);
        edtTime = findViewById(R.id.edtTime);
        edtPlace = findViewById(R.id.edtPlace);
        btnAdd = findViewById(R.id.btnAddWork);
        btnExit = findViewById(R.id.btnCancel);
        ivPickDate = findViewById(R.id.ivPickDate);
        ivPickTime = findViewById(R.id.ivPickTime);

        btnAdd.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        ivPickDate.setOnClickListener(this);
        ivPickTime.setOnClickListener(this);

//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault());
//        edtDeadline.setText(sdf.format(new Date()));


        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
    }
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        edtDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddWork:
                addWork();
                break;
            case R.id.btnCancel:
                finish();
                break;
            case R.id.ivPickDate:
                new DatePickerDialog(AddWorkActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.ivPickTime:
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddWorkActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edtTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                break;
        }
    }

    private void addWork() {
        if (edtTitle.getText().toString().equals("") || edtTitle.getText().toString() == null){
            Toast.makeText(this, "Bạn chưa chọn tiêu đề!", Toast.LENGTH_SHORT).show();
            edtTitle.requestFocus();
        }else {
            Work work = new Work(edtTitle.getText().toString(), edtPlace.getText().toString(),edtTime.getText().toString() + " " + edtDate.getText().toString(), "Chưa hoàn thành", MainActivity.USER_NAME);
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<Response> call = getDataService.postWork(work);
            call.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    Toast.makeText(AddWorkActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {

                }
            });
        }
    }
}
