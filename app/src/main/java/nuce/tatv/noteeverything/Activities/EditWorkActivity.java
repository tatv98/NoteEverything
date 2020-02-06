package nuce.tatv.noteeverything.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import nuce.tatv.noteeverything.Models.Response;
import nuce.tatv.noteeverything.Models.Work;
import nuce.tatv.noteeverything.R;
import nuce.tatv.noteeverything.Remotes.GetDataService;
import nuce.tatv.noteeverything.Remotes.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;

public class EditWorkActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtTitle, edtDate, edtTime, edtPlace;
    private ImageView ivPickDate, ivPickTime;
    private Button btnUpdate, btnExit;
    private CheckBox cbFinish;
    private Integer work_id;
    private String work_title, work_place, work_deadline, work_status;
    private final Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener date;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_edit_layout);
        init();
    }

    private void init() {
        edtTitle = findViewById(R.id.edtTitle);
        edtDate = findViewById(R.id.edtDate);
        edtTime = findViewById(R.id.edtTime);
        edtPlace = findViewById(R.id.edtPlace);
        btnUpdate = findViewById(R.id.btnUpdateWork);
        btnExit = findViewById(R.id.btnCancel);
        ivPickDate = findViewById(R.id.ivPickDate);
        ivPickTime = findViewById(R.id.ivPickTime);
        cbFinish = findViewById(R.id.cbFinish);

        btnUpdate.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        ivPickDate.setOnClickListener(this);
        ivPickTime.setOnClickListener(this);

        getBundleIntent();
        edtTitle.setText(work_title);
        edtPlace.setText(work_place);
        edtDate.setText(work_deadline.substring(4, 14));
        edtTime.setText(work_deadline.substring(0, 3));
        if(work_status.equals("Chưa hoàn thành")){
            cbFinish.setChecked(false);
        }else {
            cbFinish.setChecked(true);
        }


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
    private void getBundleIntent(){
        Intent intentGetWork = getIntent();
        Bundle bundleWork = intentGetWork.getExtras();
        work_id = bundleWork.getInt("work_id");
        work_title = bundleWork.getString("work_title");
        work_place = bundleWork.getString("work_place");
        work_deadline = bundleWork.getString("work_deadline");
        work_status = bundleWork.getString("work_status");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnUpdateWork:
                updateWork();
                break;
            case R.id.btnCancel:
                finish();
                break;
            case R.id.ivPickDate:
                new DatePickerDialog(EditWorkActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.ivPickTime:
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditWorkActivity.this, new TimePickerDialog.OnTimeSetListener() {
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

    private void updateWork() {
        if (cbFinish.isChecked()){
            work_status = "Đã hoàn thành";
        }else {
            work_status = "Chưa hoàn thành";
        }
        Work work = new Work(work_id, work_title, edtPlace.getText().toString(), edtTime.getText().toString() + " " + edtDate.getText().toString(), work_status, MainActivity.USER_NAME);
        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<Response> call = getDataService.updateWork(work);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Toast.makeText(EditWorkActivity.this, "Đã hoàn thành: " + work_title, Toast.LENGTH_SHORT).show();
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
