package nuce.tatv.noteeverything.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nuce.tatv.noteeverything.Adapters.ShowListGroupAdapter;
import nuce.tatv.noteeverything.Models.Expense;
import nuce.tatv.noteeverything.Models.Group;
import nuce.tatv.noteeverything.Models.Response;
import nuce.tatv.noteeverything.R;
import nuce.tatv.noteeverything.Remotes.GetDataService;
import nuce.tatv.noteeverything.Remotes.RetrofitClientInstance;
import nuce.tatv.noteeverything.Utils.DateFormat;
import retrofit2.Call;
import retrofit2.Callback;

public class AddExpenseActivity extends AppCompatActivity implements View.OnClickListener {
    public static int REQUEST_CODE_ADDEXPENSE = 112;
    TextView tvDate;
    EditText edtContent, edtAmount;
    Spinner spnGroup;
    Button btnAdd, btnCancel;
    List<Group> listGroup;
    ShowListGroupAdapter showListGroupAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_add_layout);
        init();
        showAllGroup();
        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void init() {
        tvDate = findViewById(R.id.tvDate);
        edtContent = findViewById(R.id.edtContent);
        edtAmount = findViewById(R.id.edtAmount);
        spnGroup = findViewById(R.id.spnGroup);
        btnAdd = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnCancel);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        tvDate.setText(sdf.format(new Date()));
    }
    private void showAllGroup(){
        listGroup = new ArrayList<>();
        Group gEating = new Group(R.drawable.ic_eating, "Ăn uống");
        Group gMove = new Group(R.drawable.ic_car, "Di chuyển");
        Group gTravel = new Group(R.drawable.ic_travel, "Du lịch");
        Group gShopping = new Group(R.drawable.ic_shoppingbasket, "Mua sắm");
        Group gOther = new Group(R.drawable.ic_other, "Khác");
        listGroup.add(gEating);
        listGroup.add(gMove);
        listGroup.add(gTravel);
        listGroup.add(gShopping);
        listGroup.add(gOther);
        showListGroupAdapter = new ShowListGroupAdapter(this, R.layout.layout_custom_spiner_group, listGroup);
        spnGroup.setAdapter(showListGroupAdapter);
        showListGroupAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btnConfirm:
                int positionGroup = spnGroup.getSelectedItemPosition();
                int thumbnail = listGroup.get(positionGroup).getmThumbnail();
                String expense_title = listGroup.get(positionGroup).getmGroup();
                String expense_content = edtContent.getText().toString();
                String expense_date = tvDate.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                DateFormat date = new DateFormat();
                try {
                    expense_date = date.Format(sdf.format(new Date()));
                    Log.d("123456", expense_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (expense_title == null || expense_title.equals("")){
                    Toast.makeText(this, "Bạn chưa chọn nhóm!", Toast.LENGTH_SHORT).show();
                    spnGroup.requestFocus();
                } else if (edtAmount.getText().toString() == null || edtAmount.getText().toString().equals("")){
                    Toast.makeText(this, "Bạn chưa nhập số tiền!", Toast.LENGTH_SHORT).show();
                    edtAmount.requestFocus();
                } else {
                    int expense_amount = Integer.parseInt(edtAmount.getText().toString());
                    Expense expense = new Expense(thumbnail, expense_title, expense_content, expense_amount, MainActivity.USER_NAME, expense_date);
                    Log.d("1111", expense.toString());

                    GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                    Call<Response> call = service.postExpense(expense);
                    call.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                            Log.d("1111", response.body().toString());
                            Toast.makeText(AddExpenseActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<Response> call, Throwable t) {

                        }
                    });
                }
                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }
}
