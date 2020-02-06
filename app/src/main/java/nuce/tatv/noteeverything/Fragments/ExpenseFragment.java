package nuce.tatv.noteeverything.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import nuce.tatv.noteeverything.Activities.AddExpenseActivity;
import nuce.tatv.noteeverything.Activities.MainActivity;
import nuce.tatv.noteeverything.Activities.ReportExpenseActivity;
import nuce.tatv.noteeverything.Adapters.ExpenseAdapter;
import nuce.tatv.noteeverything.Models.Expense;
import nuce.tatv.noteeverything.Models.Response;
import nuce.tatv.noteeverything.R;
import nuce.tatv.noteeverything.Remotes.GetDataService;
import nuce.tatv.noteeverything.Remotes.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;


public class ExpenseFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{
    public static final int REQUEST_CODE_ADD = 112;
    public static final int REQUEST_CODE_EDIT = 113;
    View vExpense;
    RecyclerView gvListExpense;
    SwipeRefreshLayout mySwipeRefreshLayout;
    List<Expense> listExpense;
    ImageView ivReport;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vExpense = inflater.inflate(R.layout.expense_fragment, container, false);
        init();
        return vExpense;
    }

    private void init() {
        setHasOptionsMenu(true);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Expense");

        listExpense = new ArrayList<>();
        ivReport = vExpense.findViewById(R.id.ivReport);
        ivReport.setOnClickListener(this);
        gvListExpense = vExpense.findViewById(R.id.vgListExpense);
        mySwipeRefreshLayout = vExpense.findViewById(R.id.mySwipeRefreshLayout);
        mySwipeRefreshLayout.setOnRefreshListener(this);

        getAllExpense();
    }

    private void getAllExpense() {
        listExpense.clear();
        final GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<Response> call = service.getExpense(MainActivity.USER_NAME);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                String date = "";
                for (Expense expense: response.body().getData().getExpense()){
                    Log.d("aaa", expense.toString());
                    expense.setHeader(false);
                    String temp = expense.getExpenseDate();
                    if (expense.getExpenseDate().equals(date)){
                        listExpense.add(expense);
                    }else {
                        Expense expenseHeader = new Expense(true, expense.getExpenseDate());
                        listExpense.add(expenseHeader);
                        listExpense.add(expense);
                    }
                    date = temp;

                }
                if(listExpense.size() > 1){
                    int sum = 0;
                    int position = 0;
                    for (int i = 1; i < listExpense.size(); i++){
                        if (listExpense.get(i).isHeader()){
                            listExpense.get(position).setExpenseAmount(sum);
                            position = i;
                            sum = 0;
                        }else {
                            sum += listExpense.get(i).getExpenseAmount();
                        }
                    }
                    listExpense.get(position).setExpenseAmount(sum);
                }


                ExpenseAdapter adapterExpense = new ExpenseAdapter(getActivity(), listExpense);
                @SuppressLint("WrongConstant") RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
                gvListExpense.setLayoutManager(layoutManager);
                gvListExpense.setItemAnimator(new DefaultItemAnimator());
                gvListExpense.setAdapter(adapterExpense);
                adapterExpense.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem itemExpense = menu.add(1, R.id.itemAddExpense, 1, "Thêm khoản chi");
        itemExpense.setIcon(R.drawable.ic_expense);
        itemExpense.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemAddExpense:
                startActivityForResult(new Intent(getActivity(), AddExpenseActivity.class), REQUEST_CODE_ADD);
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD || requestCode == REQUEST_CODE_EDIT){
            if (resultCode == Activity.RESULT_OK) {
                getAllExpense();
            }
        }
    }

    @Override
    public void onRefresh() {
        getAllExpense();
        mySwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivReport:
                startActivity(new Intent(getActivity(), ReportExpenseActivity.class));
                break;
        }
    }
}


