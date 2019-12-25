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

public class SettingFragment extends Fragment {
    private View vSetting;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vSetting = inflater.inflate(R.layout.setting_fragment, container, false);
        init();
        return vSetting;
    }

    private void init() {
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Setting");
    }
}
