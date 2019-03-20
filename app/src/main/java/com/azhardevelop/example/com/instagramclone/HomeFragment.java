package com.azhardevelop.example.com.instagramclone;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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

import de.hdodenhof.circleimageview.CircleImageView;


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
    TextView txtUsername, txtUsercap, txtCaption;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lvPost = view.findViewById(R.id.listPost);
        txtUsername = view.findViewById(R.id.txt_username);
        txtUsercap = view.findViewById(R.id.txt_usernamecap);
        txtCaption = view.findViewById(R.id.txt_caption);

        postData = new ArrayList<HashMap<String, String>>();
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
                Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_LONG).show();
                try {
                    JSONArray jsonArray = response.getJSONArray("post");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(a);
                        HashMap<String, String> rowData = new HashMap<String, String>();
                        rowData.put("username", jsonObject.getString("username"));
                        rowData.put("caption", jsonObject.getString("caption"));
                        postData.add(rowData);
                    }
                    // menampilkan datanya di komponen
                    PostAdapter postAdapter = new PostAdapter(getActivity(),postData);
                    lvPost.setAdapter(postAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // menjalankan request
        RequestQueue myRequestQueue = Volley.newRequestQueue(getActivity());
        myRequestQueue.add(myRequest);
    }
}


