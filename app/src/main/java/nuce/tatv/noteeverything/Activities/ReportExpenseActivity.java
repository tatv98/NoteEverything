package nuce.tatv.noteeverything.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import nuce.tatv.noteeverything.Models.Expense;
import nuce.tatv.noteeverything.Models.Response;
import nuce.tatv.noteeverything.R;
import nuce.tatv.noteeverything.Remotes.GetDataService;
import nuce.tatv.noteeverything.Remotes.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;

public class ReportExpenseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TextWatcher {
    PieChart lineChartExpense;
    Spinner spnMonth;
    EditText edtYear;
    List<Expense> listExpense;
    TextView tvSumExpense;
    ArrayList<String> months;
    long sumExpense, sumEat, sumTravel, sumBuy, sumMove, sumOther;
    Calendar calendar = Calendar.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_report);
        init();

    }

    public void init() {
        listExpense = new ArrayList<>();
        getAllExpense();
        lineChartExpense = findViewById(R.id.lineChartExpense);
        spnMonth = findViewById(R.id.spnMonth);
        edtYear = findViewById(R.id.edtYear);
        edtYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        edtYear.addTextChangedListener(this);
        spnMonth.post(new Runnable() {
            @Override
            public void run() {
                spnMonth.setSelection(calendar.get(Calendar.MONTH));
            }
        });
        spnMonth.setOnItemSelectedListener(this);
        tvSumExpense = findViewById(R.id.tvSumExpense);

        months = new ArrayList<>();
        for(int i = 1; i <= 12; i++){
            months.add(String.valueOf(i));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, months);
        spnMonth.setAdapter(arrayAdapter);
    }

    String[] group = new String[]{"Ăn uống", "Du lịch", "Di chuyển", "Mua sắm", "Khác"};
    private void setData(){
        lineChartExpense.setUsePercentValues(true);
        Description desc = new Description();
        desc.setText("Tỉ lệ chi tiêu");
        desc.setTextSize(16);
        desc.setTextColor(Color.parseColor("#FF03A9F4"));
        lineChartExpense.setDescription(desc);
        lineChartExpense.setDrawHoleEnabled(true);
//        lineChartExpense.setMaxAngle(180);
//        lineChartExpense.setRotationAngle(180);
        lineChartExpense.setCenterTextOffset(0, -20);
        ArrayList<PieEntry> values = new ArrayList<>();
        if (sumExpense != 0){
            sumEat = sumEat*100/sumExpense;
            sumTravel = sumTravel*100/sumExpense;
            sumMove = sumMove*100/sumExpense;
            sumBuy = sumBuy*100/sumExpense;
            sumOther = sumOther*100/sumExpense;
        }
        values.add(new PieEntry((float)(sumEat), group[0]));
        values.add(new PieEntry((float)(sumTravel), group[1]));
        values.add(new PieEntry((float)(sumMove), group[2]));
        values.add(new PieEntry((float)(sumBuy), group[3]));
        values.add(new PieEntry((float)(sumOther), group[4]));

        PieDataSet dataSet = new PieDataSet(values, null);
        dataSet.setSelectionShift(5f);
        dataSet.setSliceSpace(3f);
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        dataSet.setXValuePosition(null);
//        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);

        lineChartExpense.setData(data);
        lineChartExpense.invalidate();

        lineChartExpense.animateY(1000, Easing.EaseInOutCubic);
    }


    private void getAllExpense() {

        listExpense.clear();
        final GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<Response> call = service.getExpense(MainActivity.USER_NAME);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                for (Expense expense : response.body().getData().getExpense()) {
                    listExpense.add(expense);
                }
                getExpense();
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });
    }

    public void getExpense(){
        sumExpense = 0; sumEat = 0; sumTravel = 0; sumBuy = 0; sumMove = 0; sumOther = 0;
        int positionMonth = spnMonth.getSelectedItemPosition();
        int month = Integer.parseInt(months.get(positionMonth));
        for (Expense expense: listExpense){
            if (month == Integer.parseInt(expense.getExpenseDate().substring(3, 5)) && edtYear.getText().toString().equals(expense.getExpenseDate().substring(6, 10))){
                sumExpense += expense.getExpenseAmount();
                switch (expense.getExpenseTitle()){
                    case "Ăn uống":
                        sumEat += expense.getExpenseAmount();
                        break;
                    case "Mua sắm":
                        sumBuy += expense.getExpenseAmount();
                        break;
                    case "Di chuyển":
                        sumMove += expense.getExpenseAmount();
                        break;
                    case "Du lịch":
                        sumTravel += expense.getExpenseAmount();
                        break;
                    case "Khác":
                        sumOther += expense.getExpenseAmount();
                        break;
                }
            }
        }
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        DecimalFormat decimalFormat = new DecimalFormat("#,### đ", symbols);
        tvSumExpense.setText(decimalFormat.format(sumExpense));
        setData();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        getExpense();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        getExpense();
    }

    public class MyValueFormatter extends IndexAxisValueFormatter{
        private DecimalFormat mFormat;
        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0.0");
        }
        @Override
        public String getFormattedValue(float value) {
            return mFormat.format(value) + " %";
        }
    }
}
