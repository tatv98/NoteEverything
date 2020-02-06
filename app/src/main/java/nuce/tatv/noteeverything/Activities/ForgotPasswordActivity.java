package nuce.tatv.noteeverything.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import nuce.tatv.noteeverything.R;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnRetrieval;
    private TextView tvBackToLogin;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword_layout);
        init();
    }

    private void init() {
        btnRetrieval = findViewById(R.id.btnRetrieval);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        btnRetrieval.setOnClickListener(this);
        tvBackToLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRetrieval:
                break;
            case R.id.tvBackToLogin:
                finish();
                break;
        }
    }
}
