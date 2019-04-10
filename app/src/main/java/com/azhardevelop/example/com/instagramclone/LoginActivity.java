package com.azhardevelop.example.com.instagramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText txtUser, txtPassw;
    private Button btnLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtUser = findViewById(R.id.edUser1);
        txtPassw = findViewById(R.id.edPw2);

        TextView register = findViewById(R.id.txtSign);
        btnLog = findViewById(R.id.btnLogin1);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginData();
            }
        });

    }

    private void loginData() {
        if (TextUtils.isEmpty(txtUser.getText().toString())){
            txtUser.setError("Username harus diisi");
            txtUser.requestFocus();
        }else if (TextUtils.isEmpty(txtPassw.getText().toString())){
            txtPassw.setError("Password harus di Isi");
            txtPassw.requestFocus();
        }else{
            String URL = "https://mzdzharserver.000webhostapp.com/SMPIDN/webdatabase/api_simpandataregister.php";
            ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setTitle("Menyimpan Data...");
            progressDialog.setMessage("Tunggu Sebentar...");
            progressDialog.show();

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String hasil = jsonObject.getString("hasil");
                                String pesan = jsonObject.getString("pesan");
                                if (hasil.equalsIgnoreCase("true")){
                                    Toast.makeText(LoginActivity.this, pesan, Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                                    finish();
                                }else {
                                    Toast.makeText(LoginActivity.this, pesan, Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, "Terjadi Kesalahan Coba Lagi", Toast.LENGTH_SHORT).show();
                    Log.e("log", "onErrorResponse: " + error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> parameter = new HashMap<String, String>();
                    parameter.put("username", txtUser.getText().toString());
                    parameter.put("password", txtPassw.getText().toString());
                    return parameter;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue((this));
            requestQueue.add(jsonObjectRequest);
        }
    }


}
