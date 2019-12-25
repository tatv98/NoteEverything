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

import nuce.tatv.noteeverything.Models.User;
import nuce.tatv.noteeverything.R;
import nuce.tatv.noteeverything.Remotes.GetDataService;
import nuce.tatv.noteeverything.Remotes.RetrofitClientInstance;
import nuce.tatv.noteeverything.Utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            edtEmail.setError("Email must input!");
            edtEmail.requestFocus();
        } else if (username.equals("")) {
            edtUser.setError("Username must input!");
            edtUser.requestFocus();
        } else if (password.equals("")) {
            edtPass.setError("Password must input!");
            edtPass.requestFocus();
        }else if (confirm.equals("")) {
            edtConfirm.setError("Confirm password must input!");
            edtConfirm.requestFocus();
        }else if (!password.equals(confirm)){
            edtConfirm.setError("Confirm password not match!");
            edtConfirm.requestFocus();
        }else if (!m.find())
            Toast.makeText(this, "Your Email Id is Invalid!", Toast.LENGTH_SHORT).show();
        else {
            User user = new User();
            user.setUserName(username);
            user.setUserEmail(email);
            user.setUserPass(password);
            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<User> call = service.postUser(user);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Log.d("1234", response.toString());
                    if (response.message().equals("OK")) Toast.makeText(getApplication(), "Signup success!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.d("1234", t.toString());
                    Toast.makeText(getApplication(), "Signup false!", Toast.LENGTH_SHORT).show();
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
