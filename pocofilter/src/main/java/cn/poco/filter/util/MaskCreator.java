package cn.poco.filter.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader.TileMode;

public class MaskCreator {

    public static Bitmap createMask(int width, int height, int color) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(color);
        return bitmap;
    }

    public static Bitmap createMask(int width, int height, int r, int g, int b) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRGB(r, g, b);
        return bitmap;
    }

    public static Bitmap createDarkCornerMask(int width, int height, int color1, int color2) {
        int[] colors = {color1, color2};
        float[] stops = {0.0f, 1.0f};
        return createDarkCornerMask(width, height, colors, stops);
    }

    public static Bitmap createDarkCornerMask(int width, int height, int color1, int color2,
                                              int color3, float position1, float position2,
                                              float position3) {
        int[] colors = {color1, color2, color3};
        float[] stops = {position1, position2, position3};
        return createDarkCornerMask(width, height, colors, stops);
    }

    public static Bitmap createDarkCornerMask(int width, int height, int color1, int color2,
                                              int color3, int color4, float stop1, float stop2,
                                              float stop3, float stop4) {
        int[] colors = {color1, color2, color3, color4};
        float[] stops = {stop1, stop2, stop3, stop4};
        return createDarkCornerMask(width, height, colors, stops);
    }

    public static Bitmap createDarkCornerMask(int width, int height, int color1, int color2,
                                              int color3, int color4, int color5, float stop1,
                                              float stop2, float stop3, float stop4, float stop5) {
        int[] colors = {color1, color2, color3, color4, color5};
        float[] stops = {stop1, stop2, stop3, stop4, stop5};
        return createDarkCornerMask(width, height, colors, stops);
    }

    public static Bitmap createDarkCornerMask(int width, int height, int[] colors, float[] stops) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        int radius = (int) Math.sqrt(halfWidth * halfWidth + halfHeight * halfHeight);
        RadialGradient shader = new RadialGradient(halfWidth, halfHeight, radius, colors, stops,
                TileMode.CLAMP);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(shader);
        canvas.drawCircle(halfWidth, halfHeight, radius, paint);
        return bitmap;
    }

    public static Bitmap createMagicPurpleMask(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawARGB(0, 0, 0, 0);
        int[] colors= new int[]{0xffffffff, 0x00000000, 0x00000000, 0xffffffff};
        float[] stops= new float[]{0.0f, 0.4f, 0.6f, 1.0f};
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, 0, width, height, colors, stops,
                TileMode.CLAMP);
        paint.setShader(shader);
        canvas.drawRect(0, 0, width, height, paint);
        return bitmap;
    }
}
