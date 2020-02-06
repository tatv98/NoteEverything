package nuce.tatv.noteeverything.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import nuce.tatv.noteeverything.Activities.EditExpenseActivity;
import nuce.tatv.noteeverything.Activities.MainActivity;
import nuce.tatv.noteeverything.Fragments.ExpenseFragment;
import nuce.tatv.noteeverything.Models.Expense;
import nuce.tatv.noteeverything.R;

public class ExpenseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Expense> listExpense;

    public ExpenseAdapter(Context context, List<Expense> listExpense) {
        this.context = context;
        this.listExpense = listExpense;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_custom_row_header_layout, parent, false);
                return new HeaderViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_custom_row_content_layout, parent, false);
                return new RowViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Expense expense = listExpense.get(position);
        if (expense != null){
            if (!expense.isHeader()){
                ((RowViewHolder) holder).ivThumbnail.setImageResource(expense.getExpenseThumbnail());
                ((RowViewHolder) holder).tvTitle.setText(expense.getExpenseTitle());
                ((RowViewHolder) holder).tvContent.setText(expense.getExpenseContent());

                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                symbols.setDecimalSeparator(',');
                DecimalFormat decimalFormat = new DecimalFormat("#,### đ", symbols);
                ((RowViewHolder) holder).tvAmount.setText(decimalFormat.format(expense.getExpenseAmount()));

                ((RowViewHolder) holder).vgForeground.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentExpense = new Intent(context, EditExpenseActivity.class);
                        Bundle bundleExpense = new Bundle();
                        bundleExpense.putInt("expense_id", expense.getExpenseId());
                        bundleExpense.putInt("expense_thumbnail", expense.getExpenseThumbnail());
                        bundleExpense.putString("expense_title", expense.getExpenseTitle());
                        bundleExpense.putString("expense_content", expense.getExpenseContent());
                        bundleExpense.putString("expense_date", expense.getExpenseDate());
                        bundleExpense.putInt("expense_amount", expense.getExpenseAmount());
                        intentExpense.putExtras(bundleExpense);
                        ((MainActivity)context).startActivityForResult(intentExpense, ExpenseFragment.REQUEST_CODE_EDIT);
                    }
                });
            }else {
                ((HeaderViewHolder) holder).tvDate.setText(expense.getExpenseDate());
                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                symbols.setDecimalSeparator(',');
                DecimalFormat decimalFormat = new DecimalFormat("#,### đ", symbols);
                ((HeaderViewHolder) holder).tvSumAmount.setText(decimalFormat.format(expense.getExpenseAmount()));
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (listExpense != null){
            Expense expense = listExpense.get(position);
            if (expense != null){
                if (!expense.isHeader()){
                    return 1;
                }
                else return 0;
            }
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        if (listExpense == null) return 0;
        return listExpense.size();
    }
    public static class HeaderViewHolder extends RecyclerView.ViewHolder{
        TextView tvDate, tvSumAmount;
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvSumAmount = itemView.findViewById(R.id.tvSumAmount);
        }
    }

    public static class RowViewHolder extends RecyclerView.ViewHolder{
        ImageView ivThumbnail;
        TextView tvTitle, tvContent, tvAmount;
        RelativeLayout vgForeground;
        public RowViewHolder(@NonNull View itemView) {
            super(itemView);
            vgForeground = itemView.findViewById(R.id.vgForeground);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvAmount = itemView.findViewById(R.id.tvAmount);
        }
    }
}
