package nuce.tatv.noteeverything.Activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nuce.tatv.noteeverything.Models.Response;
import nuce.tatv.noteeverything.Models.User;
import nuce.tatv.noteeverything.R;
import nuce.tatv.noteeverything.Remotes.GetDataService;
import nuce.tatv.noteeverything.Remotes.RetrofitClientInstance;
import nuce.tatv.noteeverything.Utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtEmail, edtUser, edtPass, edtConfirm;
    private Button btnRegister;
    private TextView tvBackToLogin;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        init();
        setListeners();
    }
    private void init() {
        edtEmail = findViewById(R.id.edtEmail);
        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        edtConfirm = findViewById(R.id.edtConfirmPass);
        btnRegister = findViewById(R.id.btnRegister);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
    }
    private void setListeners() {
        btnRegister.setOnClickListener(this);
        tvBackToLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegister:
                if (isOnline()){
                    checkValidation();
                }else {
                    Toast.makeText(this, "Không có kết nối internet!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tvBackToLogin:
                finish();
                break;
        }
    }
    private void checkValidation() {
        String email = edtEmail.getText().toString();
        String username = edtUser.getText().toString();
        String password = edtPass.getText().toString();
        String confirm = edtConfirm.getText().toString();

        // Check patter for email id
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(email);

        if (email.equals("")) {
            edtEmail.setError("Bạn phải nhập email!");
            edtEmail.requestFocus();
        } else if (username.equals("")) {
            edtUser.setError("Bạn phải nhập tài khoản!");
            edtUser.requestFocus();
        } else if (password.equals("")) {
            edtPass.setError("Bạn phải nhập mật khẩu!");
            edtPass.requestFocus();
        }else if (confirm.equals("")) {
            edtConfirm.setError("Bạn phải xác nhận mật khẩut!");
            edtConfirm.requestFocus();
        }else if (!password.equals(confirm)){
            edtConfirm.setError("Xác nhận mật khẩu không khớp!");
            edtConfirm.requestFocus();
        }else if (!m.find())
            Toast.makeText(this, "Email không đúng định dạng!", Toast.LENGTH_SHORT).show();
        else {
            User user = new User(username,password, email);
            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<Response> call = service.postUser(user);
            call.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    Toast.makeText(getApplication(), "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    Toast.makeText(getApplication(), "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private Boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni != null && ni.isConnected()) {
            return true;
        }
        return false;
    }
}
