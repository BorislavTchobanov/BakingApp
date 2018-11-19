package com.example.android.bakingapp.utilities;

import android.view.View;
import android.widget.ImageView;

import com.example.android.bakingapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class ImageHandler {

    public static void loadImage(String imageUrl, final ImageView poster, final View progressBar) {
        if (imageUrl.isEmpty()) {
            return;
        }
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.temp_pic)
                .error(R.drawable.temp_pic)
                .into(poster, new Callback() {

                    @Override
                    public void onSuccess() {
                        poster.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        poster.setVisibility(View.VISIBLE);
                    }
                });
    }
}
