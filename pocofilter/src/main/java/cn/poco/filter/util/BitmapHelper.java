package cn.poco.filter.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;

public final class BitmapHelper {

    private static final float MEM_SCALE = 2f / 13f;

    public static Bitmap decodeRes(Context context, int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resId, opts);

        if (reqWidth < 1) {
            reqWidth = opts.outWidth << 1;
        }
        if (reqHeight < 1) {
            reqHeight = opts.outHeight << 1;
        }
        opts.inSampleSize =
                opts.outWidth / reqWidth < opts.outHeight / reqHeight ? opts.outWidth / reqWidth
                        : opts.outHeight / reqHeight;
        if (opts.inSampleSize < 1) {
            opts.inSampleSize = 1;
        }

        long maxMem = (long) (Runtime.getRuntime().maxMemory() * (double) MEM_SCALE);
        int bpp = 4;
        long imgMem =
                opts.outWidth / opts.inSampleSize * opts.outHeight / opts.inSampleSize * bpp;
        if (imgMem > maxMem) {
            opts.inSampleSize = (int) Math.ceil(Math
                    .sqrt((long) opts.outWidth * opts.outHeight * bpp / (double) maxMem));
        }

        opts.inJustDecodeBounds = false;
        opts.inDither = true;
        opts.inPreferredConfig = Config.ARGB_8888;
        return BitmapFactory.decodeResource(context.getResources(), resId, opts);
    }

    public static Bitmap decodeFixRes(Context context, int resId, int reqWidth, int reqHeight) {
        Bitmap out = decodeRes(context, resId, reqWidth, reqHeight);
        if (out == null) {
            return null;
        }

        Bitmap fixBitmap = createFixBitmap(out, reqWidth, reqHeight, CUT_TYPE.CENTER, 0,
                Config.ARGB_8888);
        out.recycle();
        return fixBitmap;
    }

    public interface CUT_TYPE {

        int START = 1;
        int CENTER = 2;
        int END = 3;
    }

    /**
     * 按比例缩放, 输出大小与要求的一致（自动裁剪超出部分）
     *
     * @param bitmap 原始bitmap
     * @param reqWidth 输出的图片宽
     * @param reqHeight 输出的图片高
     * @param cutType START(开始截取) CENTER(中间截取) END(尾部截取)
     * @param rotate 原始bitmap需要旋转的角度0, 90, 180, 270...
     * @param config 像素类型
     */
    public static Bitmap createFixBitmap(Bitmap bitmap, int reqWidth, int reqHeight, int cutType,
                                         int rotate, Config config) {
        if (bitmap == null) {
            return null;
        }

        Bitmap fixBitmap;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (rotate % 180 != 0) {
            width += height;
            height = width - height;
            width -= height;
        }

        float scale = (float) reqWidth / width;
        if (scale * height < reqHeight) {
            scale = (float) reqHeight / height;
        }

        Matrix matrix = new Matrix();
        switch (cutType) {
            case CUT_TYPE.START:
                switch (rotate % 360) {
                    case 0:
                        matrix.postScale(scale, scale, 0, 0);
                        break;
                    case 90:
                        matrix.postTranslate(width, 0);
                        matrix.postRotate(rotate, width, 0);
                        matrix.postScale(scale, scale, 0, 0);
                        break;
                    case 180:
                        matrix.postTranslate(width, height);
                        matrix.postRotate(rotate, width, height);
                        matrix.postScale(scale, scale, 0, 0);
                        break;
                    case 270:
                        matrix.postTranslate(0, height);
                        matrix.postRotate(rotate, 0, height);
                        matrix.postScale(scale, scale, 0, 0);
                        break;
                    default:
                        break;
                }
                break;
            case CUT_TYPE.CENTER:
                matrix.postTranslate((reqWidth - bitmap.getWidth()) / 2f,
                        (reqHeight - bitmap.getHeight()) / 2f);
                matrix.postRotate(rotate, reqWidth / 2f, reqHeight / 2f);
                matrix.postScale(scale, scale, reqWidth / 2f, reqHeight / 2f);
                break;
            case CUT_TYPE.END:
                switch (rotate % 360) {
                    case 0:
                        matrix.postTranslate(reqWidth - width, reqHeight - height);
                        matrix.postScale(scale, scale, reqWidth, reqHeight);
                        break;
                    case 90:
                        matrix.postRotate(rotate, 0, 0);
                        matrix.postTranslate(reqWidth, reqHeight - height);
                        matrix.postScale(scale, scale, reqWidth, reqHeight);
                        break;
                    case 180:
                        matrix.postRotate(rotate, 0, 0);
                        matrix.postTranslate(reqWidth, reqHeight);
                        matrix.postScale(scale, scale, reqWidth, reqHeight);
                        break;
                    case 270:
                        matrix.postRotate(rotate, 0, 0);
                        matrix.postTranslate(reqWidth - width, reqHeight);
                        matrix.postScale(scale, scale, reqWidth, reqHeight);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }

        fixBitmap = Bitmap.createBitmap(reqWidth, reqHeight, config);
        Canvas canvas = new Canvas(fixBitmap);
        canvas.setDrawFilter(
                new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        canvas.drawBitmap(bitmap, matrix, paint);

        return fixBitmap;
    }
}
