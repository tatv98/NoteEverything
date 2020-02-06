package nuce.tatv.noteeverything.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import nuce.tatv.noteeverything.Models.Response;
import nuce.tatv.noteeverything.Models.User;
import nuce.tatv.noteeverything.R;
import nuce.tatv.noteeverything.Remotes.GetDataService;
import nuce.tatv.noteeverything.Remotes.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends Activity implements View.OnClickListener{
    private TextView tvForgotPassword, tvRegister;
    private EditText edtUser, edtPass;
    private Button btnLogin;
    private CheckBox cbRemember;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        init();
    }

    public void init() {
        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        btnLogin = findViewById(R.id.btnLogin);
        cbRemember = findViewById(R.id.cbRemember);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvRegister = findViewById(R.id.tvRegister);

        sharedPreferences = getSharedPreferences("datalogin", MODE_PRIVATE);
        edtUser.setText(sharedPreferences.getString("user",""));
        edtPass.setText(sharedPreferences.getString("pass", ""));
        cbRemember.setChecked(sharedPreferences.getBoolean("checked", false));
        setListeners();
    }
    private void setListeners() {
        tvForgotPassword.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                if (isOnline()){
                    checkValidation();
                }else {
                    Toast.makeText(this, "Chưa có kết nối internet!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tvForgotPassword:
                Intent itForgotPassword = new Intent(this, ForgotPasswordActivity.class);
                startActivity(itForgotPassword);
                break;
            case R.id.tvRegister:
                Intent itRegister = new Intent(this, RegisterActivity.class);
                startActivity(itRegister);
                break;
        }
    }

    private void checkValidation() {
        final String username = edtUser.getText().toString();
        final String password = edtPass.getText().toString();
        if (username.equals("")) {
            edtUser.setError("Bạn phải nhập tài khoản!");
            edtUser.requestFocus();
        } else if (password.equals("")) {
            edtPass.setError("Bạn phải nhập mật khẩu!");
            edtPass.requestFocus();
        } else {
            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<Response> call = service.getAllUser();
            call.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    boolean check;
                    check = false;
                    for (User user: response.body().getData().getUser()){
                        Log.d("1234", user.toString());
                        if (username.equals(user.getUserName()) && password.equals(user.getUserPass())){
                            check = true;
                            if (cbRemember.isChecked()) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("user", username);
                                editor.putString("pass", password);
                                editor.putBoolean("checked", true);
                                editor.commit();
                            }else{
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.remove("user");
                                editor.remove("pass");
                                editor.remove("checked");
                                editor.commit();
                            }
                            Toast.makeText(getApplication(), "Login success!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("user", user.getUserName());
                            intent.putExtra("email", user.getUserEmail());
                            startActivity(intent);
                        }
                    }
                    if(!check){
                        Toast.makeText(getApplication(), "Tài khoản hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    Log.d("1234", t.getMessage());
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
