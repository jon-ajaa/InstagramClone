package com.azhardevelop.example.com.instagramclone;

import android.app.ProgressDialog;
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

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText txtEmail, txtUser, txtPass;
    private Button btnSignUps;
    private TextView txtLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtEmail = findViewById(R.id.edMailReg2);
        txtUser = findViewById(R.id.edUserReg2);
        txtPass = findViewById(R.id.edPwReg2);
        txtLog = findViewById(R.id.txtLogin);
        txtLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSignUps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpData();
            }
        });
    }

    private void signUpData() {
        if (TextUtils.isEmpty(txtEmail.getText().toString())){
            txtEmail.setError("E-mail harus di Isi!");
            txtEmail.requestFocus();
        }else if (TextUtils.isEmpty(txtUser.getText().toString())){
            txtUser.setError("Nama User harus di Isi! dan tidak boleh ada Huruf besar");
            txtUser.requestFocus();
        }else if (TextUtils.isEmpty(txtPass.getText().toString())){
            txtPass.setError("Password harus di Isi!");
        }else {
            String URL = "https://mzdzharserver.000webhostapp.com/SMPIDN/webdatabase/api_simpandataregister.php";
            ProgressDialog progressDialog = new ProgressDialog(this);
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
                                    Toast.makeText(RegisterActivity.this, pesan, Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(RegisterActivity.this, pesan, Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RegisterActivity.this, "Terjadi Kesalahan Coba Lagi", Toast.LENGTH_SHORT).show();
                    Log.e("log", "onErrorResponse: " + error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> parameter = new HashMap<String, String>();
                    parameter.put("email", txtEmail.getText().toString());
                    parameter.put("username", txtUser.getText().toString());
                    parameter.put("password", txtPass.getText().toString());

                    return parameter;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue((this));
            requestQueue.add(jsonObjectRequest);

        }
    }
}
