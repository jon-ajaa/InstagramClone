package com.azhardevelop.example.com.instagramclone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> implements View.OnClickListener {
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
        String Urlgambar = "https://mzdzharserver.000webhostapp/SMPIDN/webdatabase/img";
        Glide.with(context)
                .load(Urlgambar + listData.get(i).get("gambar"))
                .into(postViewHolder.imgPost);
        Glide.with(context)
                .load(Urlgambar + listData.get(i).get("p_image"))
                .into(postViewHolder.imgProfil);
        postViewHolder.btnShare.setOnClickListener(this);
        postViewHolder.btnLike.setOnClickListener(this);
        postViewHolder.btnFav.setOnClickListener(this);
        postViewHolder.btnMore.setOnClickListener(this);
        postViewHolder.btnComment.setOnClickListener(this);
    }


    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(context, "Antum menekan Tombol", Toast.LENGTH_SHORT).show();
    }


    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView txtUserCap, txtUser, txtCaption;
        ImageView imgPost, btnLike, btnMore, btnShare, btnFav, btnComment;
        CircleImageView imgProfil;

        public PostViewHolder(View itemView) {
            super(itemView);
            txtUser = itemView.findViewById(R.id.txt_username);
            txtUserCap = itemView.findViewById(R.id.txt_usernamecap);
            txtCaption = itemView.findViewById(R.id.txt_caption);
            imgPost = itemView.findViewById(R.id.img_post);
            imgProfil = itemView.findViewById(R.id.profile);
            btnComment = itemView.findViewById(R.id.btn_comment);
            btnFav = itemView.findViewById(R.id.btn_fav);
            btnMore = itemView.findViewById(R.id.btn_more);
            btnLike = itemView.findViewById(R.id.btn_like);
            btnShare = itemView.findViewById(R.id.btn_share);
        }
    }

}
