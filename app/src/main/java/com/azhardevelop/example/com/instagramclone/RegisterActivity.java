package com.azhardevelop.example.com.instagramclone;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.*;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText txtEmail, txtUser, txtPass, txtFull;
    private Button btnSignUps;
    private TextView txtLog, txtUpload;
    private CircleImageView imgProfil;
    private Bitmap bitmap, decoded;
    private Intent intent;
    private Uri fileUri;
    public final int REQUEST_CAMERA = 0;
    public final int SELECT_FILE = 1;

    int bitmap_size = 60; // image quality 1 - 100;
    int max_resolution_image = 800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtEmail = findViewById(R.id.edMailReg2);
        txtFull = findViewById(R.id.FullReg2);
        txtUser = findViewById(R.id.edUserReg2);
        txtPass = findViewById(R.id.edPwReg2);
        imgProfil = findViewById(R.id.imgProfReg);
        txtLog = findViewById(R.id.txtLogin);
        txtUpload = findViewById(R.id.txtUp);
        btnSignUps = findViewById(R.id.btnReg2);

        txtUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        txtLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
        
        btnSignUps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerData();
            }
        });

    }


    //Mengirim gambar ke Database
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    //Method pilih gambar atau foto langsung
    private void selectImage() {
        imgProfil.setImageResource(0);
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Add Photo!");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri fileUri = getOutputMediaFileUri();
                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("onActivityResult", "requestCode " + requestCode + ", resultCode " + resultCode);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                try {
                    Log.e("CAMERA", fileUri.getPath());

                    bitmap = BitmapFactory.decodeFile(fileUri.getPath());
                    setToImageView(getResizedBitmap(bitmap, max_resolution_image));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == SELECT_FILE && data != null && data.getData() != null) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(RegisterActivity.this.getContentResolver(), data.getData());
                    setToImageView(getResizedBitmap(bitmap, max_resolution_image));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //memasukan pilihan ke CircleImageView
    private void setToImageView(Bitmap bmp) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        imgProfil.setImageBitmap(decoded);
    }
    //Mengatur Ukuran gambar
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "DeKa");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("Monitoring", "Oops! Failed create Monitoring directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_DeKa_" + timeStamp + ".jpg");

        return mediaFile;
    }
    //Method untuk Register
    private void registerData() {
        if (TextUtils.isEmpty(txtFull.getText().toString())){
            txtFull.setError("Fullname harus diisi");
            txtFull.requestFocus();
        }else if (TextUtils.isEmpty(txtEmail.getText().toString())){
            txtEmail.setError("Email harus di Isi");
            txtEmail.requestFocus();
        }else if (TextUtils.isEmpty(txtUser.getText().toString())){
            txtUser.setError("Nama User harus di Isi");
            txtUser.requestFocus();
        }else if (TextUtils.isEmpty(txtPass.getText().toString())){
            txtPass.setError("Password harus di Isi");
            txtPass.requestFocus();
        }else{
            String URL = "https://mzdzharserver.000webhostapp.com/SMPIDN/webdatabase/api_simpandataregister.php";
            ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
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
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    finish();
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
                    parameter.put("username", txtUser.getText().toString());
                    parameter.put("email", txtEmail.getText().toString());
                    parameter.put("nama", txtFull.getText().toString());
                    parameter.put("password", txtPass.getText().toString());
                    parameter.put("p_image", getStringImage(decoded));
                    return parameter;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue((this));
            requestQueue.add(jsonObjectRequest);
        }

    }

}
