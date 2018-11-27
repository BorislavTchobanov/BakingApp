package com.example.android.bakingapp.utilities;

import android.view.View;
import android.widget.ImageView;

import com.example.android.bakingapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class ImageHandler {

    public static void loadImage(String imageUrl, final ImageView imageView, final View progressBar) {
        if (imageUrl != null && imageUrl.trim().isEmpty()) {
            imageUrl = null;
        }
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.no_image_available)
                .error(R.drawable.no_image_available)
                .into(imageView, new Callback() {

                    @Override
                    public void onSuccess() {
                        imageView.setVisibility(View.VISIBLE);
//                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {
//                        progressBar.setVisibility(View.INVISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                    }
                });
    }
}
