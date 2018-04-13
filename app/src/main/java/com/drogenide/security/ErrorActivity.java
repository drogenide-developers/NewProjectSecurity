package com.drogenide.security;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.github.kleinerhacker.android.gif.Gif;
import com.github.kleinerhacker.android.gif.GifDrawable;
import com.github.kleinerhacker.android.gif.GifFactory;

public class ErrorActivity extends AppCompatActivity
{
    ImageView sirongif;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.error_activity);
        sirongif=findViewById(R.id.sirongif);
        final Gif myGif = GifFactory.decodeResource(getResources(), R.drawable.sirongif);
        final Drawable myDrawable = new GifDrawable(getResources(), myGif);
        sirongif.setImageDrawable(myDrawable);
    }
}
