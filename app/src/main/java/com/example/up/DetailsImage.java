package com.example.up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Base64;


public class DetailsImage extends AppCompatActivity {
    ImageView imageView;
    RelativeLayout layout;
    private boolean isImageScaled;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_image);

        getSupportActionBar().hide();

        layout = findViewById(R.id.relativeLayout);
        imageView = findViewById(R.id.Image_Details);

        layout.setOnTouchListener(new Swipe(DetailsImage.this) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                try
                {
                    Profile.maskaProfile.imageProfile.delete();
                }
                catch(Exception exception)
                {
                    Toast.makeText(DetailsImage.this, "При удаление картинки возникла ошибка!", Toast.LENGTH_LONG).show();
                }
                startActivity(new Intent(DetailsImage.this, Profile.class));
            }
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                startActivity(new Intent(DetailsImage.this, Profile.class));
            }
            @Override
            public void onDoubleClick() {
                super.onDoubleClick();

                if (!isImageScaled) imageView.animate().scaleX(2f).scaleY(2f).setDuration(500);
                if (isImageScaled) imageView.animate().scaleX(1f).scaleY(1f).setDuration(500);
                isImageScaled = !isImageScaled;

            }
        });

        if(Profile.maskaProfile.getImageProfile().exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(Profile.maskaProfile.getImageProfile().getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
        }
    }

    public void onClickDelete(View view)
    {
        try
        {
            Profile.maskaProfile.imageProfile.delete();
        }
        catch(Exception exception)
        {
            Toast.makeText(this, "При удаление картинки возникла ошибка!", Toast.LENGTH_LONG).show();
        }
        startActivity(new Intent(this, Profile.class));
    }

    public void Perehod(View v) {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }
}