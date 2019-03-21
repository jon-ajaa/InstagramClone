package com.azhardevelop.example.com.instagramclone;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    private ArrayList<HashMap<String, String>> postData;
    private String url;
    private RecyclerView lvPost;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lvPost = view.findViewById(R.id.listPost);
        lvPost.setLayoutManager(new LinearLayoutManager(getActivity()));

        postData = new ArrayList<>();
        url = "https://mzdzharserver.000webhostapp.com/SMPIDN/webdatabase/api_tampilpost.php";
        showData();
    }

    private void showData() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET
                , url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.d("Log", "onResponse: " + response.toString());
                try {
                    JSONArray jsonArray = response.getJSONArray("post");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(a);
                        HashMap<String, String> rowData = new HashMap<>();
                        rowData.put("username", jsonObject.getString("username"));
                        rowData.put("caption", jsonObject.getString("caption"));
                        rowData.put("waktu", jsonObject.getString("waktu"));
                        rowData.put("p_image", jsonObject.getString("p_image"));
                        rowData.put("id_user", jsonObject.getString("id_user"));
                        rowData.put("id_post", jsonObject.getString("id_post"));
                        rowData.put("gambar", jsonObject.getString("gambar"));
                        postData.add(rowData);
                    }
                    // menampilkan datanya di komponen
                    PostAdapter postAdapter = new PostAdapter(getActivity(), postData);
                    lvPost.setAdapter(postAdapter);
                } catch (JSONException e) {
                    Log.d("log", "JSONException " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("log", "onErrorResponse; " + error.getMessage());
                Toast.makeText(getActivity(), "Gagal Menampilkan data.\n Coba Lagi", Toast.LENGTH_SHORT).show();
            }
        });

        // menjalankan request
        RequestQueue myRequestQueue = Volley.newRequestQueue(getActivity());
        myRequestQueue.add(myRequest);
    }
}


