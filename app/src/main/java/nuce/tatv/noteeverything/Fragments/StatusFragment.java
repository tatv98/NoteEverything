package nuce.tatv.noteeverything.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import nuce.tatv.noteeverything.R;
public class StatusFragment extends Fragment {
    View vStatus;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vStatus = inflater.inflate(R.layout.status_fragment, container, false);
        return vStatus;
    }


}
