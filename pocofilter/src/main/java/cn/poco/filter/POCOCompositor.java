package cn.poco.filter;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

import cn.poco.image.PocoNativeFilter;

public class POCOCompositor {

    /**
     * A, B图反色选项
     */
    public interface A_B_INVERSE {

        int NO = 0;
        int A = 2;
        int B = 1;
        int A_B = 3;
    }

    /**
     * 合成两张图片
     * <p>
     * 可选的混合样式:
     * <pre>
     *      颜色加深
     *      颜色减淡
     *      变暗
     *      差值
     *      排除
     *      强光
     *      变亮 / 线性变亮
     *      线性光
     *      正片叠底
     *      叠加
     *      滤色
     *      柔光
     *      亮光
     *      线性减淡
     *      线性加深
     *      点光
     *      正常
     * </pre>
     *
     * @param destA     A图
     * @param maskB     B图
     * @param inverse   A, B图反色选项
     * @param composite 混合样式
     * @param opacity   混合透明度(0-255)
     */
    public static Bitmap composite(Bitmap destA, Bitmap maskB, int inverse, int composite,
                                   int opacity) {
        if (destA == null || maskB == null) {
            return null;
        }

        if (destA.getWidth() != maskB.getWidth() || destA.getHeight() != maskB.getHeight()) {
            return destA;
        }

        if (opacity == 0) {
            return destA;
        }

        Bitmap dest;
        if (destA.getConfig() != Config.ARGB_8888) {
            dest = destA.copy(Config.ARGB_8888, true);
            destA.recycle();
            destA = null;
        } else {
            dest = destA;
        }

        Bitmap mask;
        if (maskB.getConfig() != Config.ARGB_8888) {
            mask = maskB.copy(Config.ARGB_8888, true);
            maskB.recycle();
            maskB = null;
        } else {
            mask = maskB;
        }

        PocoNativeFilter.compositefun(dest, mask, inverse, composite, opacity);

        return dest;
    }
}
