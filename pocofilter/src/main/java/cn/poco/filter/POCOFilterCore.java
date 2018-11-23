package cn.poco.filter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.os.Environment;

import java.io.File;

import cn.poco.filter.POCOFilterConstant.CHANNEL;
import cn.poco.filter.POCOFilterConstant.COMPOSITE;
import cn.poco.filter.util.MaskCreator;
import cn.poco.image.PocoNativeFilter;

public class POCOFilterCore {


    /**
     * 天空效果合成
     *
     * @param dest A图
     * @param sky B图(天空样图)
     * @param color B图色值
     * @param opacity 混合透明度
     */
    public static Bitmap magicSky(Bitmap dest, Bitmap sky, int color, int opacity) {
        if (dest == null || sky == null) {
            return null;
        }

        if (opacity == 0) {
            return dest;
        }

        if (dest.getWidth() != sky.getWidth() || dest.getHeight() != sky.getHeight()) {
            return dest;
        }

        PocoNativeFilter.magicsky(dest, sky, color, opacity);

        return dest;
    }

    /**
     * 模拟毛玻璃
     *
     * @param dest 透明    颜色 |   |
     * @param colors - 0x ** ******
     */
    public static Bitmap fakeGlass(Bitmap dest, int colors) {
        if (dest == null) {
            return null;
        }

        if (dest.getWidth() > 640 || dest.getHeight() > 920) {
            float scale = 640f / dest.getWidth();
            Bitmap small = Bitmap
                    .createBitmap(640, (int) (dest.getHeight() * scale), Bitmap.Config.ARGB_8888);
            resizeBitmap(small, dest);
            PocoNativeFilter.fakeglass(small, 100, colors);
            resizeBitmap(dest, small);
            small.recycle();
            small = null;
        } else {
            int radius = (int) (dest.getWidth() / 3.5f);
            if (radius < 3) {
                return dest;
            }
            PocoNativeFilter.fakeglass(dest, radius, colors);
        }

        return dest;
    }

    /**
     * 模拟毛玻璃
     *
     * @param radius 高斯模糊半径, 与模糊百分比有关
     * @param nativeColors 传入底层函数的颜色, 备注：有时会没效果, 一般传0x00000000或overlayColor
     * @param overlayColor 模糊后叠加的颜色, 颜色前2位是透明度, 后6位是颜色值
     */
    public static Bitmap fakeGlass(Bitmap dest, int radius, int nativeColors, int overlayColor) {
        if (dest == null) {
            return null;
        }

        if (radius < 3) {
            radius = 100;
        }

        if (dest.getWidth() > 640 || dest.getHeight() > 920) {
            float scale = 640f / dest.getWidth();

            Bitmap small = Bitmap
                    .createBitmap(640, (int) (dest.getHeight() * scale), Bitmap.Config.ARGB_8888);
            resizeBitmap(small, dest);
            PocoNativeFilter.fakeglass(small, radius, nativeColors);
            resizeBitmap(dest, small);

            small.recycle();
            small = null;
        } else {
            PocoNativeFilter.fakeglass(dest, radius, nativeColors);
        }

        if (!dest.isRecycled()) {
            Canvas canvas = new Canvas(dest);
            canvas.setDrawFilter(
                    new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            canvas.drawColor(overlayColor, PorterDuff.Mode.SRC_OVER);
        }

        return dest;
    }

    public static void resizeBitmap(Bitmap destBmp, Bitmap srcBmp) {
        if (srcBmp == null) {
            return;
        }

        int width = destBmp.getWidth();
        int height = destBmp.getHeight();
        Canvas canvas = new Canvas(destBmp);
        canvas.setDrawFilter(
                new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        Matrix matrix = new Matrix();
        matrix.postScale((float) width / srcBmp.getWidth(), (float) height / srcBmp.getHeight());
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawBitmap(srcBmp, matrix, paint);
    }

    /**
     * 调节锐度
     *
     * @src 源Bitmap
     * @value 亮度值, 范围在[0, 100]。
     * @ 返回一个处理后的新Bitmap
     */
    public static Bitmap sharpen(Bitmap src, int percent) {
        percent = percent < 0 ? 0 : (percent > 100 ? 100 : percent);
        percent = (int) (70.0 * percent / 100);

        Bitmap dest = src.copy(Bitmap.Config.ARGB_8888, true);

        if (0 == percent) {
            return dest;
        }

        PocoNativeFilter.sharpenImageFast(dest, src, percent);

        return dest;
    }

    /**
     * 胶片
     */
    public static Bitmap xProIIFilter(Bitmap destBmp) {
        if (destBmp.getConfig() != Bitmap.Config.ARGB_8888) {
            return null;
        }
        PocoNativeFilter.xProIIFilter(destBmp);
        return destBmp;
    }

    /**
     * LOMO
     */
    public static Bitmap lomoFi(Bitmap destBmp) {
        if (destBmp.getConfig() != Bitmap.Config.ARGB_8888) {
            return null;
        }
        PocoNativeFilter.LomoFi(destBmp);
        return destBmp;
    }

    /**
     * 老照片
     */
    public static Bitmap f1977(Bitmap destBitmap, Bitmap noiseBitmap) {
        if (destBitmap.getConfig() != Bitmap.Config.ARGB_8888) {
            return null;
        }

        int width = destBitmap.getWidth();
        int height = destBitmap.getHeight();
        if (width == noiseBitmap.getWidth() && height == noiseBitmap.getHeight()) {
            PocoNativeFilter.f1977(destBitmap, noiseBitmap);
            noiseBitmap.recycle();
            noiseBitmap = null;
        } else {
            Bitmap noiseMask = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            resizeBitmap(noiseMask, noiseBitmap);
            noiseBitmap.recycle();
            noiseBitmap = null;

            PocoNativeFilter.f1977(destBitmap, noiseMask);

            noiseMask.recycle();
            noiseMask = null;
        }

        return destBitmap;
    }

    /**
     * 日系风
     */
    public static Bitmap studio(Bitmap destBmp) {
        if (destBmp.getConfig() != Bitmap.Config.ARGB_8888) {
            return null;
        }
        PocoNativeFilter.studio(destBmp);
        return destBmp;
    }

    /**
     * 牛皮纸
     */
    public static Bitmap cerbbeanNoon(Bitmap destBmp, Bitmap maskRes) {
        if (destBmp.getConfig() != Bitmap.Config.ARGB_8888) {
            return null;
        }
        int width = destBmp.getWidth();
        int height = destBmp.getHeight();

        if (width == maskRes.getWidth() && height == maskRes.getHeight()) {
            PocoNativeFilter.cerbbeanNoon(destBmp, maskRes);
            maskRes.recycle();
            maskRes = null;
        } else {
            Bitmap temp = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            resizeBitmap(temp, maskRes);
            maskRes.recycle();
            maskRes = null;

            PocoNativeFilter.cerbbeanNoon(destBmp, temp);

            temp.recycle();
            temp = null;
        }
        return destBmp;
    }

    /**
     * 老旧黑白
     */
    public static Bitmap colorFeverGray(Bitmap destBmp, Bitmap shader) {
        if (destBmp.getConfig() != Bitmap.Config.ARGB_8888) {
            return null;
        }

        if (destBmp.getWidth() == shader.getWidth()
                && destBmp.getHeight() == shader.getHeight()) {
            PocoNativeFilter.colorFeverGray(destBmp, shader);
            shader.recycle();
            shader = null;
        } else {
            Bitmap shaderBmp = Bitmap.createBitmap(destBmp.getWidth(),
                    destBmp.getHeight(), Bitmap.Config.ARGB_8888);
            resizeBitmap(shaderBmp, shader);
            shader.recycle();
            shader = null;

            PocoNativeFilter.colorFeverGray(destBmp, shaderBmp);

            shaderBmp.recycle();
            shaderBmp = null;
        }
        return destBmp;
    }

    /**
     * 往时回忆
     */
    public static Bitmap colorFeverGreen(Bitmap destBmp, Bitmap shader) {
        if (destBmp.getConfig() != Bitmap.Config.ARGB_8888) {
            return null;
        }

        if (destBmp.getWidth() == shader.getWidth()
                && shader.getHeight() == destBmp.getHeight()) {
            PocoNativeFilter.colorFeverGreen(destBmp, shader);
            shader.recycle();
            shader = null;
        } else {
            Bitmap shaderBmp = Bitmap.createBitmap(destBmp.getWidth(),
                    destBmp.getHeight(), Bitmap.Config.ARGB_8888);
            resizeBitmap(shaderBmp, shader);
            shader.recycle();
            shader = null;

            PocoNativeFilter.colorFeverGreen(destBmp, shaderBmp);

            shaderBmp.recycle();
            shaderBmp = null;
        }
        return destBmp;
    }

    /**
     * 渲染溢光
     */
    public static Bitmap colorFeverRed2(Bitmap destBmp, Bitmap shader) {
        if (destBmp.getConfig() != Bitmap.Config.ARGB_8888) {
            return null;
        }
        if (destBmp.getWidth() == shader.getWidth()
                && destBmp.getHeight() == shader.getHeight()) {
            Canvas canvas = new Canvas(destBmp);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setAlpha(255);
            canvas.drawBitmap(shader, 0, 0, paint);
            PocoNativeFilter.colorFeverRed2(destBmp, shader);
            shader.recycle();
            shader = null;
        } else {
            Bitmap shaderBmp = Bitmap.createBitmap(destBmp.getWidth(),
                    destBmp.getHeight(), Bitmap.Config.ARGB_8888);
            resizeBitmap(shaderBmp, shader);
            shader.recycle();
            shader = null;

            Canvas canvas = new Canvas(destBmp);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setAlpha(255);
            canvas.drawBitmap(shaderBmp, 0, 0, paint);

            PocoNativeFilter.colorFeverRed2(destBmp, shaderBmp);

            shaderBmp.recycle();
            shaderBmp = null;
        }
        return destBmp;
    }

    /**
     * 小资绿
     */
    public static Bitmap polaroidGreen(Bitmap destBmp) {
        if (destBmp.getConfig() != Bitmap.Config.ARGB_8888) {
            return null;
        }
        PocoNativeFilter.polaroidGreen(destBmp);
        return destBmp;
    }

    /**
     * 小资黄
     */
    public static Bitmap polaroidYellow(Bitmap destBmp) {
        if (destBmp.getConfig() != Bitmap.Config.ARGB_8888) {
            return null;
        }
        PocoNativeFilter.polaroidYellow(destBmp);
        return destBmp;
    }

    /**
     * 黑白素描
     */
    public static Bitmap sketch(Bitmap srcBmp) {
        if (srcBmp.getConfig() != Bitmap.Config.ARGB_8888) {
            return null;
        }
        PocoNativeFilter.polaroidYellow(srcBmp);
        return srcBmp;
    }

    /**
     * 颜色鲜艳
     */
    public static Bitmap foodColor(Bitmap destBmp) {
        if (destBmp.getConfig() != Bitmap.Config.ARGB_8888) {
            return null;
        }
        PocoNativeFilter.foodColor(destBmp);
        return destBmp;
    }

    /**
     * 暗角魅蓝
     */
    public static Bitmap HDRDarkenBlue(Bitmap destBmp) {
        Bitmap tempBmp = MaskCreator.createDarkCornerMask(640, 640, 0xffffffff,
                0xffffffff, 0xffdcdcdc, 0xff161616, 0.0f, 0.65f, 0.75f, 1.0f);
        Bitmap maskBmp = Bitmap.createBitmap(destBmp.getWidth(),
                destBmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(maskBmp);
        Matrix matrix = new Matrix();
        matrix.postScale((float) maskBmp.getWidth() / tempBmp.getWidth(),
                (float) maskBmp.getHeight() / tempBmp.getHeight());
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawBitmap(tempBmp, matrix, paint);

        PocoNativeFilter.HDRDarkenBlue(destBmp, maskBmp);

        maskBmp.recycle();
        maskBmp = null;
        return destBmp;
    }

    /**
     * 魔幻紫色
     */
    public static Bitmap magicPurple(Bitmap destBmp) {
        if (destBmp.getConfig() != Bitmap.Config.ARGB_8888) {
            return null;
        }

        Bitmap maskBmp = MaskCreator.createMagicPurpleMask(destBmp.getWidth(),
                destBmp.getHeight());

        PocoNativeFilter.magickPurple(destBmp, maskBmp);

        return destBmp;
    }

    /**
     * HDR红
     */
    public static Bitmap HDRRed(Bitmap destBmp) {

        if (destBmp.getConfig() != Bitmap.Config.ARGB_8888) {
            return null;
        }
        PocoNativeFilter.HDRRed(destBmp);

        return destBmp;
    }

    /**
     * 古典黄
     */
    public static Bitmap toaster(Bitmap destBmp) {
        if (destBmp.getConfig() != Bitmap.Config.ARGB_8888) {
            return null;
        }

        PocoNativeFilter.toaster(destBmp);

        return destBmp;
    }

    /**
     * 怀旧蓝
     */
    public static Bitmap Brannan(Bitmap destBmp) {
        if (destBmp.getConfig() != Bitmap.Config.ARGB_8888) {
            return null;
        }

        PocoNativeFilter.brannan(destBmp);

        return destBmp;
    }

    /**
     * 色彩明亮
     */
    public static Bitmap scratches(Bitmap dest) {
        if (dest.getConfig() != Bitmap.Config.ARGB_8888) {
            return null;
        }
        PocoNativeFilter.Scratches(dest);
        return dest;
    }

    /**
     * 华丽重彩
     */
    public static Bitmap vivo(Bitmap dest, Bitmap vivomask) {

        if (dest.getConfig() != Bitmap.Config.ARGB_8888) {
            return null;
        }

        int width = dest.getWidth();
        int height = dest.getHeight();

        if (width == vivomask.getWidth() && height == vivomask.getHeight()) {
            PocoNativeFilter.Vivo(dest, vivomask);
            vivomask.recycle();
            vivomask = null;
        } else {
            Bitmap shaderBmp = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            resizeBitmap(shaderBmp, vivomask);
            vivomask.recycle();
            vivomask = null;

            PocoNativeFilter.Vivo(dest, shaderBmp);

            shaderBmp.recycle();
            shaderBmp = null;
        }
        return dest;
    }

    /**
     * 乡村
     */
    public static Bitmap nash(Bitmap destBmp) {
        if (destBmp.getConfig() != Bitmap.Config.ARGB_8888) {
            return null;
        }

        PocoNativeFilter.nash(destBmp);

        return destBmp;
    }

    /**
     * 夏日风
     */
    public static Bitmap vale(Bitmap destBmp) {
        if (destBmp.getConfig() != Bitmap.Config.ARGB_8888) {
            return null;
        }

        PocoNativeFilter.vale(destBmp);

        return destBmp;
    }

    /**
     * 反转片
     */
    public static Bitmap walden(Bitmap destBmp) {
        if ((null == destBmp)
                || (Bitmap.Config.ARGB_8888 != destBmp.getConfig())) {
            return null;
        }

        PocoNativeFilter.walden(destBmp);

        return destBmp;
    }

    /**
     * 橄榄青
     */
    public static Bitmap amaro(Bitmap destBmp) {
        if ((null == destBmp)
                || (Bitmap.Config.ARGB_8888 != destBmp.getConfig())) {
            return null;
        }

        PocoNativeFilter.amaro(destBmp);

        return destBmp;
    }

    /**
     * 记忆
     */
    public static Bitmap trudirect(Bitmap destBmp, Bitmap tru_mask1, Bitmap tru_mask2) {
        if ((null == destBmp) || Bitmap.Config.ARGB_8888 != destBmp.getConfig()) {
            return null;
        }
        if ((null == tru_mask1)
                || Bitmap.Config.ARGB_8888 != tru_mask1.getConfig()) {
            return null;
        }
        if ((null == tru_mask2)
                || Bitmap.Config.ARGB_8888 != tru_mask2.getConfig()) {
            return null;
        }

        PocoNativeFilter.trudirect(destBmp);

        int width = destBmp.getWidth();
        int height = destBmp.getHeight();
        Rect rect = new Rect(0, 0, width, height);

        if (width == tru_mask1.getWidth() && height == tru_mask1.getHeight()) {
            compositeImageRectChannel(destBmp, tru_mask1,
                    rect, rect, CHANNEL.ALL,
                    COMPOSITE.Screen, 255);
            tru_mask1.recycle();
            tru_mask1 = null;
        } else {
            Bitmap shaderBmp = Bitmap.createBitmap(destBmp.getWidth(),
                    destBmp.getHeight(), Bitmap.Config.ARGB_8888);
            resizeBitmap(shaderBmp, tru_mask1);
            tru_mask1.recycle();
            tru_mask1 = null;

            compositeImageRectChannel(destBmp, shaderBmp,
                    rect, rect, CHANNEL.ALL,
                    COMPOSITE.Screen, 255);

            shaderBmp.recycle();
            shaderBmp = null;
        }

        if (width == tru_mask2.getWidth() && height == tru_mask2.getHeight()) {
            compositeImageRectChannel(destBmp, tru_mask2,
                    rect, rect, CHANNEL.ALL,
                    COMPOSITE.LIGHTEN, 255);

            tru_mask2.recycle();
            tru_mask2 = null;
        } else {
            Bitmap shaderBmp = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            resizeBitmap(shaderBmp, tru_mask2);
            tru_mask2.recycle();
            tru_mask2 = null;

            compositeImageRectChannel(destBmp, shaderBmp,
                    rect, rect, CHANNEL.ALL,
                    COMPOSITE.LIGHTEN, 255);

            shaderBmp.recycle();
            shaderBmp = null;
        }

        return destBmp;
    }

    /**
     * 青绿
     */
    public static Bitmap darkGreen(Bitmap destBmp, Bitmap darkg_mask) {
        if ((null == destBmp) || (Bitmap.Config.ARGB_8888 != destBmp.getConfig())) {
            return null;
        }
        if ((null == darkg_mask) || (Bitmap.Config.ARGB_8888 != darkg_mask.getConfig())) {
            return null;
        }

        int width = destBmp.getWidth();
        int height = destBmp.getHeight();
        if (width == darkg_mask.getWidth() && height == darkg_mask.getHeight()) {
            PocoNativeFilter.darkGreen(destBmp, darkg_mask);
            darkg_mask.recycle();
            darkg_mask = null;
        } else {
            Bitmap shaderBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            resizeBitmap(shaderBmp, darkg_mask);
            darkg_mask.recycle();
            darkg_mask = null;

            PocoNativeFilter.darkGreen(destBmp, shaderBmp);

            shaderBmp.recycle();
            shaderBmp = null;
        }
        return destBmp;
    }

    /**
     * 黑白漏光
     */
    public static Bitmap split(Bitmap destBmp, Bitmap split_mask1, Bitmap split_mask2) {
        if ((null == destBmp)
                || (Bitmap.Config.ARGB_8888 != destBmp.getConfig())) {
            return null;
        }
        if ((null == split_mask1)
                || (Bitmap.Config.ARGB_8888 != split_mask1.getConfig())) {
            return null;
        }
        if ((null == split_mask2)
                || (Bitmap.Config.ARGB_8888 != split_mask2.getConfig())) {
            return null;
        }
        int width = destBmp.getWidth();
        int height = destBmp.getHeight();

        PocoNativeFilter.split(destBmp);

        Bitmap shaderBmp = Bitmap
                .createBitmap(destBmp.getWidth(), destBmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(shaderBmp);
        canvas.setDrawFilter(
                new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        int[] linearGradientColors = new int[]{0x5affffff, 0x00ffffff, 0x5a000000};
        float[] linearGradientPositions = new float[]{0.0f, 0.5f, 1.0f};
        LinearGradient linearGradientShader = new LinearGradient(0, 0, width, height,
                linearGradientColors, linearGradientPositions, TileMode.REPEAT);
        Paint shaderPaint = new Paint();
        shaderPaint.setShader(linearGradientShader);
        shaderPaint.setAntiAlias(true);
        Rect r = new Rect(0, 0, width, height);
        canvas.drawRect(r, shaderPaint);
        PocoNativeFilter
                .compositeImageChannel(destBmp, shaderBmp, CHANNEL.ALL,
                        COMPOSITE.NORMAL, 255);
        PocoNativeFilter.changeContrast(destBmp, 10);

        Rect rect = new Rect(0, 0, width, height);
        if (width == split_mask1.getWidth()
                && height == split_mask1.getHeight()) {
            compositeImageRectChannel(destBmp, split_mask1,
                    rect, rect, CHANNEL.ALL,
                    COMPOSITE.COLOR_DODGE, 255);
            split_mask1.recycle();
            split_mask1 = null;
        } else {
            shaderBmp.eraseColor(0x00000000);
            resizeBitmap(shaderBmp, split_mask1);
            split_mask1.recycle();
            split_mask1 = null;

            compositeImageRectChannel(destBmp, shaderBmp,
                    rect, rect, CHANNEL.ALL,
                    COMPOSITE.COLOR_DODGE, 255);
        }

        if (width == split_mask2.getWidth()
                && height == split_mask2.getHeight()) {
            compositeImageRectChannel(destBmp, split_mask2,
                    rect, rect, CHANNEL.ALL,
                    COMPOSITE.Screen, 255);
            split_mask2.recycle();
            split_mask2 = null;
        } else {
            shaderBmp.eraseColor(0x00000000);
            resizeBitmap(shaderBmp, split_mask2);
            split_mask2.recycle();
            split_mask2 = null;

            compositeImageRectChannel(destBmp, shaderBmp,
                    rect, rect, CHANNEL.ALL,
                    COMPOSITE.Screen, 255);

            shaderBmp.recycle();
            shaderBmp = null;
        }

        return destBmp;
    }

    /**
     * 纯白
     */
    public static Bitmap pureWhite(Bitmap destBmp, Bitmap maskResBmp) {
        if (Bitmap.Config.ARGB_8888 != destBmp.getConfig()) {
            return null;
        }

        int width = destBmp.getWidth();
        int height = destBmp.getHeight();
        if (width == maskResBmp.getWidth() && height == maskResBmp.getHeight()) {
            PocoNativeFilter.pureWhite(destBmp, maskResBmp);
            maskResBmp.recycle();
            maskResBmp = null;
        } else {
            Bitmap maskBmp = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            resizeBitmap(maskBmp, maskResBmp);
            maskResBmp.recycle();
            maskResBmp = null;

            PocoNativeFilter.pureWhite(destBmp, maskBmp);

            maskBmp.recycle();
            maskBmp = null;
        }
        return destBmp;
    }

    /**
     * 雅淡
     */
    public static Bitmap simpleElegant(Bitmap destBmp, Bitmap maskResBmp1,
                                       Bitmap maskResBmp2) {
        if (Bitmap.Config.ARGB_8888 != destBmp.getConfig()) {
            return null;
        }

        int width = destBmp.getWidth();
        int height = destBmp.getHeight();

        if (width == maskResBmp1.getWidth()
                && height == maskResBmp1.getHeight()
                && width == maskResBmp2.getWidth()
                && height == maskResBmp2.getHeight()) {
            PocoNativeFilter.simpleElegant(destBmp, maskResBmp1, maskResBmp2);
            maskResBmp1.recycle();
            maskResBmp1 = null;
            maskResBmp2.recycle();
            maskResBmp2 = null;
        } else {
            Bitmap maskBmp1 = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            resizeBitmap(maskBmp1, maskResBmp1);
            maskResBmp1.recycle();
            maskResBmp1 = null;

            Bitmap maskBmp2 = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            resizeBitmap(maskBmp2, maskResBmp2);
            maskResBmp2.recycle();
            maskResBmp2 = null;

            PocoNativeFilter.simpleElegant(destBmp, maskBmp1, maskBmp2);

            maskBmp1.recycle();
            maskBmp1 = null;
            maskBmp2.recycle();
            maskBmp2 = null;
        }
        return destBmp;
    }

    /**
     * 粉红世界
     */
    public static Bitmap pinkWorld(Bitmap destBmp, Bitmap maskResBmp1,
                                   Bitmap maskResBmp2) {
        if (Bitmap.Config.ARGB_8888 != destBmp.getConfig()) {
            return null;
        }

        int width = destBmp.getWidth();
        int height = destBmp.getHeight();
        if (width == maskResBmp1.getWidth()
                && height == maskResBmp1.getHeight()
                && width == maskResBmp2.getWidth()
                && height == maskResBmp2.getHeight()) {
            PocoNativeFilter.pinkWorld(destBmp, maskResBmp1, maskResBmp2);
            maskResBmp1.recycle();
            maskResBmp1 = null;
            maskResBmp2.recycle();
            maskResBmp2 = null;
        } else {
            Bitmap maskBmp1 = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            resizeBitmap(maskBmp1, maskResBmp1);
            maskResBmp1.recycle();
            maskResBmp1 = null;

            Bitmap maskBmp2 = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            resizeBitmap(maskBmp2, maskResBmp2);
            maskResBmp2.recycle();
            maskResBmp2 = null;

            PocoNativeFilter.pinkWorld(destBmp, maskBmp1, maskBmp2);

            maskBmp1.recycle();
            maskBmp1 = null;
            maskBmp2.recycle();
            maskBmp2 = null;
        }
        return destBmp;
    }

    /**
     * 迷幻浅蓝
     */
    public static Bitmap gradientBlue(Bitmap destBmp) {
        if (Bitmap.Config.ARGB_8888 != destBmp.getConfig()) {
            return null;
        }

        PocoNativeFilter.gradientBlue(destBmp);

        int width = destBmp.getWidth();
        int height = destBmp.getHeight();

        Bitmap shaderBmp = Bitmap.createBitmap(destBmp.getWidth(),
                destBmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(shaderBmp);
        canvas.setDrawFilter(
                new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        int[] linearGradientColors = new int[]{0xcc000000, 0x00000000, 0x00000000};
        float[] linearGradientPositions = new float[]{0.0f, 0.45f, 1.0f};
        LinearGradient linearGradientShader = new LinearGradient(width / 2, height, width / 2, 0,
                linearGradientColors,
                linearGradientPositions,
                TileMode.REPEAT);
        Paint shaderPaint = new Paint();
        shaderPaint.setShader(linearGradientShader);
        shaderPaint.setAntiAlias(true);
        Rect r = new Rect(0, 0, width, height);
        canvas.drawRect(r, shaderPaint);
        PocoNativeFilter.compositeImageChannel(destBmp, shaderBmp,
                CHANNEL.ALL,
                COMPOSITE.OVERLAY, 255);

        shaderBmp.eraseColor(0x00000000);
        canvas = new Canvas(shaderBmp);
        canvas.setDrawFilter(
                new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        linearGradientColors = new int[]{0xffd8faff, 0xe6ace6f1, 0x6bffffff, 0x00ffffff};
        linearGradientPositions = new float[]{0.0f, 0.1f, 0.58f, 1.0f};
        linearGradientShader = new LinearGradient(0, 0, (int) (0.18 * height), height,
                linearGradientColors,
                linearGradientPositions,
                TileMode.CLAMP);
        shaderPaint.reset();
        shaderPaint.setShader(linearGradientShader);
        shaderPaint.setAntiAlias(true);
        canvas.drawRect(r, shaderPaint);
        PocoNativeFilter.compositeImageChannel(destBmp, shaderBmp,
                CHANNEL.ALL,
                COMPOSITE.COLOR_BURN, 89);

        shaderBmp.eraseColor(0x00000000);
        canvas = new Canvas(shaderBmp);
        canvas.setDrawFilter(
                new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        linearGradientColors = new int[]{0xff7ccaff, 0xff57bbff, 0xffffffff, 0xffffffff};
        linearGradientPositions = new float[]{0.0f, 0.28f, 0.65f, 1.0f};
        linearGradientShader = new LinearGradient(0, 0, (int) (0.18 * height), height,
                linearGradientColors,
                linearGradientPositions,
                TileMode.CLAMP);
        shaderPaint.reset();
        shaderPaint.setShader(linearGradientShader);
        shaderPaint.setAntiAlias(true);
        canvas.drawRect(r, shaderPaint);
        PocoNativeFilter.compositeImageChannel(destBmp, shaderBmp,
                CHANNEL.ALL,
                COMPOSITE.SoftLight, 255);
        shaderBmp.recycle();
        shaderBmp = null;

        return destBmp;
    }

    /**
     * 黑白纪实
     */
    public static Bitmap wbFilm(Bitmap destBmp) {
        if (destBmp.getConfig() != Bitmap.Config.ARGB_8888) {
            return null;
        }

        PocoNativeFilter.wbFilm(destBmp);

        return destBmp;
    }

    /**
     * 夕阳
     */
    public static Bitmap sunset(Bitmap destBmp) {
        if (destBmp.getConfig() != Bitmap.Config.ARGB_8888) {
            return null;
        }

        int width = destBmp.getWidth();
        int height = destBmp.getHeight();
        int cx = width / 2;
        int cy = height / 2;

        int radius = (int) (Math.sqrt((width / 2) * (width / 2) + (height / 2)
                * (height / 2)));
        Bitmap maskBmp = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(maskBmp);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG));

        int[] radialGradientColors = new int[]{0x00ffffff, 0xbf3c311b};

        float[] radialGradientPositions = new float[]{0.0f, 1.0f};
        RadialGradient radialGradientShader = new RadialGradient(cx, cy,
                radius, radialGradientColors, radialGradientPositions,
                TileMode.CLAMP);
        Paint shaderPaint = new Paint();
        shaderPaint.setShader(radialGradientShader);
        shaderPaint.setAntiAlias(true);
        canvas.drawCircle(cx, cy, radius, shaderPaint);

        PocoNativeFilter.xiyang(destBmp, maskBmp);

        maskBmp.recycle();
        maskBmp = null;
        return destBmp;
    }

    /**
     * 浓墨
     */
    public static Bitmap pro(Bitmap destBmp, Bitmap maskBmp) {
        if ((destBmp == null)
                || (Bitmap.Config.ARGB_8888 != destBmp.getConfig())) {
            return null;
        }
        if (maskBmp == null) {
            return null;
        }

        int width = destBmp.getWidth();
        int height = destBmp.getHeight();

        if (width == maskBmp.getWidth() && height == maskBmp.getHeight()) {
            PocoNativeFilter.pro(destBmp, maskBmp);
            maskBmp.recycle();
            maskBmp = null;
        } else {
            Bitmap mask = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            resizeBitmap(mask, maskBmp);
            maskBmp.recycle();
            maskBmp = null;

            PocoNativeFilter.pro(destBmp, mask);

            mask.recycle();
            mask = null;
        }
        return destBmp;
    }

    /**
     * 绿野
     */
    public static Bitmap lightenGreen(Bitmap destBmp) {
        if ((null == destBmp)
                || (Bitmap.Config.ARGB_8888 != destBmp.getConfig())) {
            return null;
        }

        int width = destBmp.getWidth();
        int height = destBmp.getHeight();
        Bitmap mask = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mask);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG));
        int[] linearGradientColors = new int[]{0xff00061a, 0x00a7e9ff,
                0xff000930};
        float[] linearGradientPositions = new float[]{0.0f, 0.5f, 1.0f};
        LinearGradient linearGradientShader = new LinearGradient(0, height,
                width, 0, linearGradientColors, linearGradientPositions,
                TileMode.CLAMP);
        Paint shaderPaint = new Paint();
        shaderPaint.setShader(linearGradientShader);
        shaderPaint.setAntiAlias(true);
        Rect r = new Rect(0, 0, width, height);
        canvas.drawRect(r, shaderPaint);

        PocoNativeFilter.lightengreen(destBmp, mask);

        mask.recycle();
        mask = null;
        return destBmp;
    }

    /**
     * 哑光绿
     */
    public static Bitmap lightenGreen2(Bitmap destBmp) {
        if ((null == destBmp)
                || (Bitmap.Config.ARGB_8888 != destBmp.getConfig())) {
            return null;
        }

        int width = destBmp.getWidth();
        int height = destBmp.getHeight();
        Bitmap mask = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mask);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG));
        int[] linearGradientColors = new int[]{0x4093b5bf, 0x40fff3ef,
                0x45fcf6f4, 0x456cc4e5};
        float[] linearGradientPositions = new float[]{0.0f, 0.11f, 0.57f,
                1.0f};
        LinearGradient linearGradientShader = new LinearGradient(0, height / 2,
                width, height / 2, linearGradientColors,
                linearGradientPositions, TileMode.CLAMP);
        Paint shaderPaint = new Paint();
        shaderPaint.setShader(linearGradientShader);
        shaderPaint.setAntiAlias(true);
        Rect r = new Rect(0, 0, width, height);
        canvas.drawRect(r, shaderPaint);

        PocoNativeFilter.lightengreen2(destBmp, mask);

        mask.recycle();
        mask = null;

        return destBmp;
    }

    /**
     * 文艺
     */
    public static Bitmap lomo2(Bitmap destBmp, Bitmap maskBmp) {
        if ((null == destBmp)
                || (Bitmap.Config.ARGB_8888 != destBmp.getConfig())) {
            return null;
        }

        int width = destBmp.getWidth();
        int height = destBmp.getHeight();
        if (width == maskBmp.getWidth() && height == maskBmp.getHeight()) {
            PocoNativeFilter.lomo2(destBmp, maskBmp);
            maskBmp.recycle();
            maskBmp = null;
        } else {
            Bitmap mask = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            resizeBitmap(mask, maskBmp);
            maskBmp.recycle();
            maskBmp = null;

            PocoNativeFilter.lomo2(destBmp, mask);

            mask.recycle();
            mask = null;
        }
        return destBmp;
    }

    /**
     * 朴素
     */
    public static Bitmap simpleElegant3(Bitmap destBmp) {
        if ((null == destBmp)
                || (Bitmap.Config.ARGB_8888 != destBmp.getConfig())) {
            return null;
        }

        int width = destBmp.getWidth();
        int height = destBmp.getHeight();
        Bitmap mask1 = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mask1);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG));
        int[] linearGradientColors = new int[]{0xff579d98, 0xff213c3a};
        float[] linearGradientPositions = new float[]{0.0f, 1.0f};
        LinearGradient linearGradientShader = new LinearGradient(0, 0, width,
                height, linearGradientColors, linearGradientPositions,
                TileMode.CLAMP);
        Paint shaderPaint = new Paint();
        shaderPaint.setShader(linearGradientShader);
        shaderPaint.setAntiAlias(true);
        Rect r = new Rect(0, 0, width, height);
        canvas.drawRect(r, shaderPaint);

        Bitmap mask2 = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        canvas = new Canvas(mask2);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG));
        linearGradientColors = new int[]{0xfffdfdfd, 0xff000000};
        linearGradientPositions = new float[]{0.0f, 1.0f};
        linearGradientShader = new LinearGradient(0, 0, height, width,
                linearGradientColors, linearGradientPositions, TileMode.CLAMP);
        shaderPaint = new Paint();
        shaderPaint.setShader(linearGradientShader);
        shaderPaint.setAntiAlias(true);

        canvas.drawRect(r, shaderPaint);

        PocoNativeFilter.simpleElegant3(destBmp, mask1, mask2);

        mask1.recycle();
        mask1 = null;
        mask2.recycle();
        mask2 = null;

        return destBmp;
    }

    /**
     * 厚重黄
     */
    public static Bitmap country(Bitmap destBmp) {
        if (destBmp.getConfig() != Bitmap.Config.ARGB_8888) {
            return null;
        }

        PocoNativeFilter.country(destBmp);

        return destBmp;
    }

    /**
     * HDR轻
     */
    public static Bitmap portraitHDR(Bitmap destBmp) {
        if (destBmp.getConfig() != Bitmap.Config.ARGB_8888) {
            return null;
        }

        PocoNativeFilter.portraitHDR(destBmp);

        return destBmp;
    }

    /**
     * 美食特效
     */
    public static Bitmap cate(Bitmap destBmp) {
        if (destBmp.getConfig() != Bitmap.Config.ARGB_8888) {
            return null;
        }

        PocoNativeFilter.cate(destBmp);
        return destBmp;
    }

    /**
     * 去紫边
     */
    public static Bitmap dePurple(Bitmap destBmp) {
        if (null == destBmp || destBmp.getConfig() != Bitmap.Config.ARGB_8888) {
            return null;
        }

        PocoNativeFilter.dePurple(destBmp);

        return destBmp;
    }

    /**
     * 美白嫩肤
     */
    public static Bitmap moreBeauteLittle(Bitmap destBmp) {
        if (null == destBmp || destBmp.getConfig() != Bitmap.Config.ARGB_8888) {
            return null;
        }

        PocoNativeFilter.moreBeauteLittle(destBmp);

        return destBmp;
    }

    /**
     * 黑白人像
     */
    public static Bitmap moreBeauteWB(Bitmap destBmp) {
        if (null == destBmp || destBmp.getConfig() != Bitmap.Config.ARGB_8888) {
            return null;
        }

        PocoNativeFilter.moreBeauteWB(destBmp);

        return destBmp;
    }

    /**
     * 新版默认美颜效果 V2.5
     *
     * @param isCache 是否需要缓存的接口, 保存时置0, 其他地方置1
     */
    public static Bitmap crazyBeauty(Bitmap destBmp, int isCache) {
        if (null == destBmp || destBmp.getConfig() != Bitmap.Config.ARGB_8888) {
            return null;
        }

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + "jane" + File.separator + "appdata" +
                File.separator + "beautify" + File.separator + "crazy_beauty_file_all.img";
        File file_all = new File(path).getParentFile();
        if (file_all != null) {
            file_all.mkdirs();
        }

        String firstSP = Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + "jane" + File.separator + "appdata" +
                File.separator + "beautify" + File.separator + "crazy_beauty_file_pre.img";
        File file_pre = new File(firstSP).getParentFile();
        if (file_pre != null) {
            file_pre.mkdirs();
        }

        PocoNativeFilter.crazyBeautyDefault(destBmp, firstSP, path, isCache);

        return destBmp;
    }

    private static int compositeImageRectChannel(Bitmap dest, Bitmap src, Rect dr, Rect sr,
                                                 int channel, int comOp, int opacity) {
        return PocoNativeFilter
                .compositeImageRectChannel(dest, src, dr.left, dr.top, dr.width(), dr.height(),
                        sr.left, sr.top, sr.width(), sr.height(), channel, comOp, opacity);
    }
}
