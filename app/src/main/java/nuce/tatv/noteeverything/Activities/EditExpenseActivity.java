package nuce.tatv.noteeverything.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import nuce.tatv.noteeverything.Models.Expense;
import nuce.tatv.noteeverything.Models.Response;
import nuce.tatv.noteeverything.R;
import nuce.tatv.noteeverything.Remotes.GetDataService;
import nuce.tatv.noteeverything.Remotes.RetrofitClientInstance;
import nuce.tatv.noteeverything.Utils.DateFormat;
import retrofit2.Call;
import retrofit2.Callback;


public class EditExpenseActivity extends AppCompatActivity implements View.OnClickListener{
    TextView tvDate, tvTitle;
    EditText edtContent, edtAmount;
    Spinner spnGroup;
    Button btnEdit, btnCancel;
    ImageView btnDelete;
    String expense_content, expense_date, expense_title;
    static int expense_amount, expense_id, expense_thumbnail;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_edit_layout);
        init();
    }
    private void init() {
        tvTitle = findViewById(R.id.tvTitle);
        tvDate = findViewById(R.id.tvDate);
        edtContent = findViewById(R.id.edtContent);
        edtAmount = findViewById(R.id.edtAmount);
        spnGroup = findViewById(R.id.spnGroup);
        btnEdit = findViewById(R.id.btnConfirm);
        btnDelete = findViewById(R.id.btnTrash);
        btnCancel = findViewById(R.id.btnCancel);


        btnEdit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnDelete.setOnClickListener(this);


        Intent intentGetExpense = getIntent();
        Bundle bundleExpense = intentGetExpense.getExtras();
        if (bundleExpense != null){
            expense_id = bundleExpense.getInt("expense_id");
            expense_thumbnail = bundleExpense.getInt("expense_thumbnail");
            expense_amount = bundleExpense.getInt("expense_amount");
            expense_title = bundleExpense.getString("expense_title");
            expense_content = bundleExpense.getString("expense_content");
            expense_date = bundleExpense.getString("expense_date");

            tvTitle.setText(expense_title);
            edtContent.setText(expense_content);
            edtAmount.setText(String.valueOf(expense_amount));
            tvDate.setText(expense_date);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btnConfirm:
                editExpense();
                break;
            case R.id.btnTrash:
                deleteExpense();
                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }

    private void deleteExpense() {
        GetDataService getData = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<Response> call = getData.deleteExpense(expense_id);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Log.d("1111", response.body().toString());
                Toast.makeText(EditExpenseActivity.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });
    }

    private void editExpense() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        DateFormat date = new DateFormat();
        try {
            expense_date = date.Format(sdf.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Expense expense = new Expense(expense_id, expense_thumbnail, expense_title, edtContent.getText().toString(), Integer.parseInt(edtAmount.getText().toString()), MainActivity.USER_NAME, expense_date);
        Log.d("11111", expense.toString());
        GetDataService getData = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<Response> call = getData.updateExpense(expense);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Toast.makeText(EditExpenseActivity.this, "Sửa thành công!", Toast.LENGTH_SHORT).show();
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
