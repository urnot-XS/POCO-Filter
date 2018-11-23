package cn.poco.image;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class PocoNativeFilter {

    static {
        System.loadLibrary("PocoImage");
    }

    public static native int compositewithmask(Bitmap dest, Bitmap src, Bitmap mask, int dx, int dy, int dw, int dh,
                                               int sx, int sy, int sw, int sh, int channel, int comOp, int opacity);

    public static native int compositeImageChannel(Bitmap dest, Bitmap src, int channel, int comOp, int opacity);

    public static native int compositeImageRectChannel(Bitmap dest, Bitmap src, int dx, int dy, int dw, int dh, int sx,
                                                       int sy, int sw, int sh, int channel, int comOp, int opacity);

    public static native int compositefun(Bitmap dest, Bitmap mask, int inver, int comoperator, int opacity);

    public static native int magicsky(Bitmap dest, Bitmap sky, int color, int opacity);

    public static native int paintmaskblur(Bitmap destBmp, int radius, int paintcolor);

    public static native int fakeglass(Bitmap destBmp, int radius, int colors);

    public static native int sharpenImageFast(Bitmap dest, Bitmap src, int percent);

    public static native int xProIIFilter(Bitmap destBmp);

    public static native int LomoFi(Bitmap destBmp);

    public static native int f1977(Bitmap destBmp, Bitmap maskBmp);

    public static native int studio(Bitmap destBmp);

    public static native int cerbbeanNoon(Bitmap destBmp, Bitmap maskBmp);

    public static native int colorFeverGray(Bitmap destBmp, Bitmap maskBmp);

    public static native int colorFeverGreen(Bitmap destBmp, Bitmap maskBmp);

    public static native int colorFeverRed2(Bitmap destBmp, Bitmap maskBmp);

    public static native int polaroidGreen(Bitmap destBmp);

    public static native int polaroidYellow(Bitmap destBmp);

    public static native int sketch(Bitmap dest, int threshold);

    public static native int foodColor(Bitmap destBmp);

    public static native int HDRDarkenBlue(Bitmap destBmp, Bitmap maskBmp);

    public static native int magickPurple(Bitmap destBmp, Bitmap maskBmp);

    public static native int HDRRed(Bitmap destBmp);

    public static native int toaster(Bitmap dest);

    public static native int brannan(Bitmap dest);

    public static native int Scratches(Bitmap destBmp);

    public static native int Vivo(Bitmap destBmp, Bitmap mask);

    public static native int nash(Bitmap dest);

    public static native int vale(Bitmap dest);

    public static native int walden(Bitmap dest);

    public static native int amaro(Bitmap dest);

    public static native int trudirect(Bitmap dest);

    public static native int darkGreen(Bitmap dest, Bitmap darkg_mask);

    public static native int split(Bitmap dest);

    public static native int pureWhite(Bitmap destBmp, Bitmap maskResBmp);

    public static native int simpleElegant(Bitmap destBmp, Bitmap maskResBmp1, Bitmap maskResBmp2);

    public static native int pinkWorld(Bitmap destBmp, Bitmap maskResBmp1, Bitmap maskResBmp2);

    public static native int gradientBlue(Bitmap dest);

    public static native int wbFilm(Bitmap dest);

    public static native int xiyang(Bitmap dest, Bitmap mask);

    public static native int pro(Bitmap dest, Bitmap mask);

    public static native int lightengreen(Bitmap dest, Bitmap mask);

    public static native int lightengreen2(Bitmap dest, Bitmap mask);

    public static native int lomo2(Bitmap dest, Bitmap mask);

    public static native int simpleElegant3(Bitmap dest, Bitmap mask1, Bitmap mask2);

    public static native int country(Bitmap destBmp);

    public static native int portraitHDR(Bitmap dest);

    public static native int cate(Bitmap dest);

    public static native int changeContrast(Bitmap dest, int value);

    public static native int moreBeauteLittle(Bitmap destBmp);

    public static native int moreBeauteWB(Bitmap destBmp);

    public static native int dePurple(Bitmap dest);

    public static native int ShapeMatting(Bitmap dest, Bitmap mask);

    public static native int extractShade(Bitmap dest, Bitmap mask);

    public static native int crazyBeautyDefault(Bitmap dest, String file_pre, String file_all, int isCache);
}
