package com.example.up;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.acl.LastOwnerException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class Profile extends AppCompatActivity {
    View v;
    private TextView nickname;
    private ImageView icon;
    private List<MaskaProfile> maskaProfiles = new ArrayList<>();
    AdapterProfile pAdapter;
    OutputStream outputStream;

    public static MaskaProfile maskaProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().hide();

       // v = findViewById(com.google.android.material.R.id.ghost_view);

        nickname = findViewById(R.id.Profile_Name);
        nickname.setText(Login.maskaUser.getNickName());
        icon = findViewById(R.id.Profile_Image);
        new DownloadImageTask((ImageView) icon).execute(Login.maskaUser.getAvatar());

        GridView gridView = findViewById(R.id.Profile_ListFoto);
        pAdapter = new AdapterProfile(Profile.this, maskaProfiles);
        gridView.setAdapter(pAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                maskaProfile = maskaProfiles.get(i);
                startActivity(new Intent(Profile.this, DetailsImage.class));
            }
        });


        GetImage();
    }

    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Ошибка", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private void GetImage() {
        maskaProfiles.clear();
        pAdapter.notifyDataSetInvalidated();
        String path = getApplicationInfo().dataDir + "/MyFiles";
        File directory = new File(path);
        File[] files = directory.listFiles();
        int j = 0;
        if (files != null) {


            for (int i = 0; i < files.length; i++) {
                Long last = files[i].lastModified();
                MaskaProfile tempProduct = new MaskaProfile(
                        j,
                        files[i].getAbsolutePath(),
                        files[i],
                        getTime(last)
                );
                maskaProfiles.add(tempProduct);
                pAdapter.notifyDataSetInvalidated();
            }
        }

    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Bitmap bitmap = null;
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Uri selectedImage = result.getData().getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        File dir = new File(getApplicationInfo().dataDir + "/MyFiles/");
                        dir.mkdirs();
                        File file = new File(dir, System.currentTimeMillis() + ".jpg");
                        try {
                            outputStream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                            outputStream.flush();
                            outputStream.close();
                            Toast.makeText(Profile.this, "Изображение успешно сохранено", Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(Profile.this, "При сохранение изображения возникла ошибка!", Toast.LENGTH_LONG).show();
                        }
                        GetImage();
                    }
                }
            });

    public void onClickAddImage(View v) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        someActivityResultLauncher.launch(photoPickerIntent);
    }

    public static final String getTime(final long timeInMillis)
    {
        final SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeInMillis);
        c.setTimeZone(TimeZone.getDefault());
        return format.format(c.getTime());
    }

    public void onClickExit(View v) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void onClickMenu(View v) {
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }
}