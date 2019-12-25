package nuce.tatv.noteeverything.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import nuce.tatv.noteeverything.Activities.MainActivity;
import nuce.tatv.noteeverything.R;


public class ExpenseFragment extends Fragment {
    private View vExpenseManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vExpenseManager = inflater.inflate(R.layout.expense_fragment, container, false);
        init();
        return vExpenseManager;
    }

    private void init() {
        setHasOptionsMenu(true);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Expense");
    }
}


