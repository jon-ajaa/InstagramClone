package com.azhardevelop.example.com.instagramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextView txtRegister;
    private TextInputEditText txtUser, txtPassw;
    private Button btnLog;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
            txtRegister = findViewById(R.id.txtSign);
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        txtPassw = findViewById(R.id.edPw2);
        txtUser = findViewById(R.id.edUser1);
        btnLog = findViewById(R.id.btnLogin);

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginData();
            }
        });
    }

    private void loginData() {
        final String username = txtUser.getText().toString().trim();
        final String password = txtPassw.getText().toString().trim();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Login Process...");
        showDialogs();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppVar.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains(AppVar.LOGIN_SUCCESS)) {
                            hideDialog();
                            gotoCourseActivity();
                        } else {
                            hideDialog();
                            Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                        }
                    }
                },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //You can handle error here if you want
                hideDialog();
                Toast.makeText(LoginActivity.this, "The server unreachable", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(AppVar.KEY_EMAIL, username);
                params.put(AppVar.KEY_PASSWORD, password);
                return params;
            }
        };
    }

    private void gotoCourseActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));

    }


    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void showDialogs() {
        if (progressDialog.isShowing())
            progressDialog.show();
    }
}
