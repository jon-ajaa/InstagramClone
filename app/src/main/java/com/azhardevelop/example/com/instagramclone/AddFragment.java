package com.azhardevelop.example.com.instagramclone;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {


    public AddFragment() {
        // Required empty public constructor
    }

    TextInputEditText inCaption, inUser;
    ImageView inGambar;
    Button btnSimpan;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inCaption = view.findViewById(R.id.inncaptions);
        inGambar = view.findViewById(R.id.inGambar);
        inUser = view.findViewById(R.id.inusers);
        btnSimpan = view.findViewById(R.id.btnsimpan);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });


    }

    private void insertData() {
        if (TextUtils.isEmpty(inUser.getText().toString())){
            inUser.setError("Nama User harus di Isi");
            inUser.requestFocus();
        }else{
            String URL = "https://mzdzharserver.000webhostapp.com/SMPIDN/webdatabase/api_simpanpost.php";
            ProgressDialog progressDialog = new ProgressDialog(getActivity());
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
                            Toast.makeText(getActivity(), pesan, Toast.LENGTH_LONG).show();
                            ((MainActivity) getActivity()).loadFragment(new HomeFragment());
                        }else {
                            Toast.makeText(getActivity(), pesan, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "Terjadi Kesalahan Coba Lagi", Toast.LENGTH_SHORT).show();
                    Log.e("log", "onErrorResponse: " + error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> parameter = new HashMap<String, String>();
                    parameter.put("gambar", inGambar.getText().toString());
                    parameter.put("caption", inCaption.getText().toString());
                    parameter.put("iduser", inUser.getText().toString());

                    return parameter;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue((getActivity()));
            requestQueue.add(jsonObjectRequest);
        }
    }
}
