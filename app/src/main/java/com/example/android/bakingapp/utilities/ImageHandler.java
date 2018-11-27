package com.example.android.bakingapp.utilities;

import android.widget.ImageView;

import com.example.android.bakingapp.R;
import com.squareup.picasso.Picasso;


public class ImageHandler {

    public static void loadImage(String imageUrl, final ImageView imageView) {
        if (imageUrl != null && imageUrl.trim().isEmpty()) {
            imageUrl = null;
        }
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.no_image_available)
                .error(R.drawable.no_image_available)
                .into(imageView);
    }
}
