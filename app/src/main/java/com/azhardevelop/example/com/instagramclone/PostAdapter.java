package com.azhardevelop.example.com.instagramclone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>{
    private ArrayList<HashMap<String, String>> listData;
    private Context context;

    public PostAdapter(FragmentActivity activity,
                        ArrayList<HashMap<String, String>> postdata) {

        this.context = activity;
        this.listData = postdata;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View is = LayoutInflater.from(context).inflate(R.layout.item_post, viewGroup, false);
        return new PostViewHolder(is);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder postViewHolder, int i) {
        postViewHolder.txtUser.setText(listData.get(i).get("username"));
        postViewHolder.txtUserCap.setText(listData.get(i).get("caption"));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView txtUserCap, txtUser, txtCaption;

        public PostViewHolder(View itemView) {
            super(itemView);
            txtUser = itemView.findViewById(R.id.txt_username);
            txtUserCap = itemView.findViewById(R.id.txt_usernamecap);
            txtCaption = itemView.findViewById(R.id.txt_caption);
        }
    }

}
