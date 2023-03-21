package com.example.up;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

public class AdapterProfile extends BaseAdapter {
    private Context mContext;
    List<MaskaProfile> maskaProfiles;

    public AdapterProfile(Context mContext, List<MaskaProfile> maskaProfiles) {
        this.mContext = mContext;
        this.maskaProfiles = maskaProfiles;
    }

    @Override
    public int getCount() {
        return maskaProfiles.size();
    }

    @Override
    public Object getItem(int i) {
        return maskaProfiles.get(i);
    }

    @Override
    public long getItemId(int i) {
        return maskaProfiles.get(i).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(mContext, R.layout.maska_profile, null);

        ImageView Image = v.findViewById(R.id.ppz);
        TextView Time = v.findViewById(R.id.time);

        MaskaProfile mask = maskaProfiles.get(position);



        if(mask.getImageProfile().exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(mask.getImageProfile().getAbsolutePath());
            Image.setImageBitmap(myBitmap);
        }
        Time.setText(mask.getData());


        return v;
    }
}