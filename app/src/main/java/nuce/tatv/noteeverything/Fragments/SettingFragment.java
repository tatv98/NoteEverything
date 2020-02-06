package nuce.tatv.noteeverything.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import nuce.tatv.noteeverything.Activities.MainActivity;
import nuce.tatv.noteeverything.Models.Response;
import nuce.tatv.noteeverything.Models.User;
import nuce.tatv.noteeverything.R;
import nuce.tatv.noteeverything.Remotes.GetDataService;
import nuce.tatv.noteeverything.Remotes.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;

public class SettingFragment extends Fragment implements View.OnClickListener{
    private EditText edtEmail, edtUser, edtOldPass, edtNewPass, edtRePass;
    private Button btnUpdate, btnClear;
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
        edtEmail = vSetting.findViewById(R.id.edtEmail);
        edtUser = vSetting.findViewById(R.id.edtUser);
        edtOldPass = vSetting.findViewById(R.id.edtOldPass);
        edtNewPass = vSetting.findViewById(R.id.edtNewPass);
        edtRePass = vSetting.findViewById(R.id.edtRePass);

        btnUpdate = vSetting.findViewById(R.id.btnUpdateUser);
        btnClear = vSetting.findViewById(R.id.btnClear);

        edtEmail.setText(MainActivity.USER_EMAIL);
        edtUser.setText(MainActivity.USER_NAME);

        btnUpdate.setOnClickListener(this);
        btnClear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnUpdateUser:
                updateUser();
                break;
            case R.id.btnClear:
                Toast.makeText(getActivity(), "Đừng nghịch dại =]]]", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void updateUser() {
        if(edtOldPass.getText().toString().equals("") || edtOldPass.getText().toString() == null){
            edtOldPass.setError("Bạn chưa nhập mật khẩu cũ");
            edtOldPass.requestFocus();
        }
        else if(edtNewPass.getText().toString().equals("") || edtNewPass.getText().toString() == null){
            edtNewPass.setError("Bạn chưa nhập mật khẩu mới!");
            edtNewPass.requestFocus();
        }
        else if(edtRePass.getText().toString().equals("") || edtRePass.getText().toString() == null){
            edtRePass.setError("Bạn chưa nhập lại mật khẩu mới!");
            edtRePass.requestFocus();
        }
        else if (edtRePass.getText().toString().equals(edtNewPass.getText().toString())){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

            User user = new User(MainActivity.USER_NAME, edtNewPass.getText().toString(), MainActivity.USER_EMAIL);
            Call<Response> call = getDataService.updateUser(user);
            call.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    Toast.makeText(getActivity(), "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {

                }
            });
        }else {
            edtNewPass.setError("Mật khẩu nhập lại phải khớp với mật khẩu mới!");
            edtNewPass.requestFocus();
        }

    }
}
