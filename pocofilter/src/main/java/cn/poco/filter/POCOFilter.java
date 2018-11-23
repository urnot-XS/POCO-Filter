package cn.poco.filter;

import android.content.Context;
import android.graphics.Bitmap;

import cn.poco.filter.POCOFilterConstant.FILTER;
import cn.poco.filter.util.BitmapHelper;

public class POCOFilter {

    private final static int SHARPEN_VALUE = 14;

    public static Bitmap filter(Context context, Bitmap bitmap, int filter) {
        if (context == null || bitmap == null) {
            return null;
        }

        Bitmap filtered;

        switch (filter) {
            case FILTER.CERBBEAN: // 牛皮纸
                Bitmap cerbbean = BitmapHelper
                        .decodeFixRes(context, R.drawable.poco_filter_cerbbean,
                                bitmap.getWidth(),
                                bitmap.getHeight());
                filtered = POCOFilterCore.cerbbeanNoon(bitmap, cerbbean);
                cerbbean.recycle();
                cerbbean = null;
                break;
            case FILTER.F1977: // 老照片
                Bitmap f1997 = BitmapHelper.decodeFixRes(context, R.drawable.poco_filter_f1977,
                        bitmap.getWidth(),
                        bitmap.getHeight());
                filtered = POCOFilterCore.f1977(bitmap, f1997);
                f1997.recycle();
                f1997 = null;
                break;
            case FILTER.LOMOFI: // LOMO
                filtered = POCOFilterCore.lomoFi(bitmap);
                break;
            case FILTER.POLAROID_G: // 小资绿
                filtered = POCOFilterCore.polaroidGreen(bitmap);
                break;
            case FILTER.POLAROID_Y: // 小资黄
                filtered = POCOFilterCore.polaroidYellow(bitmap);
                break;
            case FILTER.STUDIO: // 日系风
                filtered = POCOFilterCore.studio(bitmap);
                break;
            case FILTER.XPROIIFILTER: // 胶片
                filtered = POCOFilterCore.xProIIFilter(bitmap);
                break;
            case FILTER.SHARPEN: // 锐化
                filtered = POCOFilterCore.sharpen(bitmap, SHARPEN_VALUE);
            case FILTER.SKETCH: // 黑白素描
                filtered = POCOFilterCore.sharpen(POCOFilterCore.sketch(bitmap), 50);
                break;
            case FILTER.COLORFEVERGRAY: // 老旧黑白
                Bitmap colorFeverGray = BitmapHelper
                        .decodeFixRes(context, R.drawable.poco_filter_fire,
                                bitmap.getWidth(),
                                bitmap.getHeight());
                filtered = POCOFilterCore
                        .sharpen(POCOFilterCore.colorFeverGray(bitmap, colorFeverGray),
                                SHARPEN_VALUE);
                colorFeverGray.recycle();
                colorFeverGray = null;
                break;
            case FILTER.COLORFEVERRED2: // 渲染溢光
                Bitmap colorFeverRed2 = BitmapHelper
                        .decodeFixRes(context, R.drawable.poco_filter_fire3,
                                bitmap.getWidth(),
                                bitmap.getHeight());
                filtered = POCOFilterCore
                        .sharpen(POCOFilterCore.colorFeverRed2(bitmap, colorFeverRed2),
                                SHARPEN_VALUE);
                colorFeverRed2.recycle();
                colorFeverRed2 = null;
                break;
            case FILTER.COLORFEVERGREEN: // 往时回忆
                Bitmap colorFeverGreen = BitmapHelper
                        .decodeFixRes(context, R.drawable.poco_filter_fire, bitmap.getWidth(),
                                bitmap.getHeight());
                filtered = POCOFilterCore
                        .sharpen(POCOFilterCore.colorFeverGreen(bitmap, colorFeverGreen),
                                SHARPEN_VALUE);
                colorFeverGreen.recycle();
                colorFeverGreen = null;
                break;
            case FILTER.SUNSET: // 夕阳
                filtered = POCOFilterCore.sunset(bitmap);
                break;
            case FILTER.FOODCOLOR: // 食物鲜艳、颜色鲜艳
                filtered = POCOFilterCore.foodColor(bitmap);
                break;
            case FILTER.HDRDARKENBLUE: // 暗角魅蓝
                filtered = POCOFilterCore.HDRDarkenBlue(bitmap);
                break;
            case FILTER.HDRRED: // HDR偏红
                filtered = POCOFilterCore.HDRRed(bitmap);
                break;
            case FILTER.MAGICKPURPLE: // 魔幻紫色
                filtered = POCOFilterCore.magicPurple(bitmap);
                break;
            case FILTER.WHITENING: // 美白嫩肤
                filtered = POCOFilterCore.moreBeauteLittle(bitmap);
                break;
            case FILTER.HOLIDAY: // 古典黄
                filtered = POCOFilterCore.toaster(bitmap);
                break;
            case FILTER.WINTER: // 怀旧蓝
                filtered = POCOFilterCore.Brannan(bitmap);
                break;
            case FILTER.PORTRAIT_HDR: // HDR
                filtered = POCOFilterCore.portraitHDR(bitmap);
                break;
            case FILTER.VIVO: // 华丽重彩
                Bitmap vivo = BitmapHelper.decodeFixRes(context, R.drawable.poco_filter_vivo_mask,
                        bitmap.getWidth(), bitmap.getHeight());
                filtered = POCOFilterCore.vivo(bitmap, vivo);
                vivo.recycle();
                vivo = null;
                break;
            case FILTER.SCRATCHES: // 色彩明亮
                filtered = POCOFilterCore.scratches(bitmap);
                break;
            case FILTER.FOODCOLOR_NEW: // 食物
                filtered = POCOFilterCore.cate(bitmap);
                break;
            case FILTER.WALDEN: // 反转片
                filtered = POCOFilterCore.walden(bitmap);
                break;
            case FILTER.VALENCIA: // 夏日风
                filtered = POCOFilterCore.vale(bitmap);
                break;
            case FILTER.NASHVILLE: // 乡村
                filtered = POCOFilterCore.nash(bitmap);
                break;
            case FILTER.AMARO: // 橄榄青
                filtered = POCOFilterCore.amaro(bitmap);
                break;
            case FILTER.PURE_WHITE: // 纯白
                Bitmap pureWhite = BitmapHelper
                        .decodeFixRes(context, R.drawable.poco_filter_pure_white,
                                bitmap.getWidth(), bitmap.getHeight());
                filtered = POCOFilterCore.pureWhite(bitmap, pureWhite);
                pureWhite.recycle();
                pureWhite = null;
                break;
            case FILTER.SIMPLE_ELEGANT: // 雅淡
                Bitmap elegant1 = BitmapHelper
                        .decodeFixRes(context, R.drawable.poco_filter_simple_elegant_1,
                                bitmap.getWidth(), bitmap.getHeight());
                Bitmap elegant2 = BitmapHelper
                        .decodeFixRes(context, R.drawable.poco_filter_simple_elegant_2,
                                bitmap.getWidth(), bitmap.getHeight());
                filtered = POCOFilterCore.simpleElegant(bitmap, elegant1, elegant2);
                elegant1.recycle();
                elegant1 = null;
                elegant2.recycle();
                elegant2 = null;
                break;
            case FILTER.TRUDIRECT: // 记忆
                Bitmap truMask1 = BitmapHelper
                        .decodeFixRes(context, R.drawable.poco_filter_tru_mask_1,
                                bitmap.getWidth(), bitmap.getHeight());
                Bitmap truMask2 = BitmapHelper
                        .decodeFixRes(context, R.drawable.poco_filter_tru_mask_2,
                                bitmap.getWidth(), bitmap.getHeight());
                filtered = POCOFilterCore.trudirect(bitmap, truMask1, truMask2);
                truMask1.recycle();
                truMask1 = null;
                truMask2.recycle();
                truMask2 = null;
                break;
            case FILTER.DARK_GREEN: // 青绿
                Bitmap darkMask = BitmapHelper
                        .decodeFixRes(context, R.drawable.poco_filter_darkg_mask,
                                bitmap.getWidth(), bitmap.getHeight());
                filtered = POCOFilterCore.darkGreen(bitmap, darkMask);
                darkMask.recycle();
                darkMask = null;
                break;
            case FILTER.PINKWORLD: // 粉红世界
                Bitmap pinkMask1 = BitmapHelper
                        .decodeFixRes(context, R.drawable.poco_filter_pink_mask_1,
                                bitmap.getWidth(), bitmap.getHeight());
                Bitmap pinkMask2 = BitmapHelper
                        .decodeFixRes(context, R.drawable.poco_filter_pink_mask_2,
                                bitmap.getWidth(), bitmap.getHeight());
                filtered = POCOFilterCore.pinkWorld(bitmap, pinkMask1, pinkMask2);
                pinkMask1.recycle();
                pinkMask1 = null;
                pinkMask2.recycle();
                pinkMask2 = null;
                break;
            case FILTER.SPLIT: // 黑白漏光
                Bitmap splitMask1 = BitmapHelper
                        .decodeFixRes(context, R.drawable.poco_filter_split_mask_1,
                                bitmap.getWidth(), bitmap.getHeight());
                Bitmap splitMask2 = BitmapHelper
                        .decodeFixRes(context, R.drawable.poco_filter_split_mask_2,
                                bitmap.getWidth(), bitmap.getHeight());
                filtered = POCOFilterCore.split(bitmap, splitMask1, splitMask2);
                splitMask1.recycle();
                splitMask1 = null;
                splitMask2.recycle();
                splitMask2 = null;
                break;
            case FILTER.GRADIENTBLUE: // 迷幻浅蓝
                filtered = POCOFilterCore.gradientBlue(bitmap);
                break;
            case FILTER.WBFILM: // 黑白纪实
                filtered = POCOFilterCore.wbFilm(bitmap);
                break;
            case FILTER.PRO: // 浓墨
                Bitmap proMask = BitmapHelper.decodeFixRes(context, R.drawable.poco_filter_pro_mask,
                        bitmap.getWidth(), bitmap.getHeight());
                filtered = POCOFilterCore.pro(bitmap, proMask);
                proMask.recycle();
                proMask = null;
                break;
            case FILTER.LIGHTENGREEN: // 绿野
                filtered = POCOFilterCore.lightenGreen(bitmap);
                break;
            case FILTER.LIGHTENGREEN2: // 哑光绿
                filtered = POCOFilterCore.lightenGreen2(bitmap);
                break;
            case FILTER.WENYI: // 文艺
                Bitmap lomoMask = BitmapHelper
                        .decodeFixRes(context, R.drawable.poco_filter_lomo_mask,
                                bitmap.getWidth(), bitmap.getHeight());
                filtered = POCOFilterCore.lomo2(bitmap, lomoMask);
                lomoMask.recycle();
                lomoMask = null;
                break;
            case FILTER.SIMPLEELEGANT3: // 朴素
                filtered = POCOFilterCore.simpleElegant3(bitmap);
                break;
            case FILTER.COUNTRY: // 厚重黄
                filtered = POCOFilterCore.country(bitmap);
                break;
            case FILTER.WB: // 黑白人像
                filtered = POCOFilterCore.moreBeauteWB(bitmap);
                break;
            case FILTER.DEPURPLE: // 去紫边
                filtered = POCOFilterCore.dePurple(bitmap);
                break;
            default:
                filtered = bitmap;
                break;
        }

        return filtered;
    }
}
