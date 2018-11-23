package com.markchan.pocofilter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import cn.poco.filter.POCOCompositor;
import cn.poco.filter.POCOCompositor.A_B_INVERSE;
import cn.poco.filter.POCOFilterConstant.COMPOSITE;
import cn.poco.filter.util.MaskCreator;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.iv);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.selfish);
        mImageView.setImageBitmap(mBitmap);
    }

    public void filter(View view) {
//        mBitmap = POCOFilterCore.sketch(mBitmap);

        Bitmap mask = MaskCreator.createMask(mBitmap.getWidth(), mBitmap.getHeight(), Color.YELLOW);
        mBitmap = POCOCompositor.composite(mBitmap, mask, A_B_INVERSE.NO, COMPOSITE.OVERLAY, 128);
        mImageView.setImageBitmap(mBitmap);
    }
}
