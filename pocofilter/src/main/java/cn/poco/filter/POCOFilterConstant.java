package cn.poco.filter;

/**
 * @author Mark Chan <a href="markchan2gm@gmail.com">Contact me.</a>
 * @version 1.0
 * @since 12/27/17
 */
public interface POCOFilterConstant {

    interface FILTER {

        int XPROIIFILTER = 0x2001; // 胶片
        int LOMOFI = 0x2002; // LOMO
        int F1977 = 0x2003; // 老照片
        int STUDIO = 0x2004; // 日系风
        int CERBBEAN = 0x2005; // 牛皮纸
        int POLAROID_G = 0x2006; // 宝丽莱绿
        int POLAROID_Y = 0x2007; // 宝丽莱黄
        int COLORFEVERRED = 0x2008; // 重口味红
        int COLORFEVERYELLOW = 0x2009; // 重口味黄
        int SKETCH = 0x200A; // 黑白素描
        int COLORFEVERGRAY = 0x200B; // 老旧黑白
        int COLORFEVERRED2 = 0x200C; // 渲染溢光
        int COLORFEVERRED3 = 0x200D; // 艳丽反转
        int COLORFEVERGREEN = 0x200E; // 往时回忆
        int COLORFEVERGREEN2 = 0x2010; // 青色映画
        int COLORFEVERYELLOW2 = 0x2011; // 胶卷过曝
        int SUNSET = 0x2012; // 夕阳
        int LOMO4 = 0x2013; // 四格四色
        int LOMO2 = 0x2014; // 二格二色
        int FOODCOLOR = 0x2015; // 颜色鲜艳
        int HDRDARKENBLUE = 0x2016; // 暗角魅蓝
        int HDRRED = 0x2017; // HDR偏红
        int HDR = 0x2018; // HDR重
        int MAGICKPURPLE = 0x2019; // 魔幻紫色
        int GRANDBLUE = 0x201A; // 幽静蓝
        int REDSUN = 0x201B; // 光芒四射
        int WHITENING = 0x201C; // 美白嫩肤
        int HOLIDAY = 0x201D; // 假日
        int WINTER = 0x201E; // 怀旧蓝
        int PORTRAIT_HDR = 0x201F; // HDR
        int VIVO = 0x2025; // 华丽重彩
        int SCRATCHES = 0x2026; // 色彩明亮
        int FOODCOLOR_NEW = 0x2027; // 美食特效
        int AMARO = 0x2028; // 橄榄青
        int NASHVILLE = 0x2029; // 乡村
        int VALENCIA = 0x202A; // 夏日风
        int WALDEN = 0x202B; // 反转片
        int PURE_WHITE = 0x202C; // 纯白
        int TRUDIRECT = 0x202D; // 记忆
        int DARK_GREEN = 0x202E; // 青绿
        int SIMPLE_ELEGANT = 0x202F; // 雅淡
        int PINKWORLD = 0x2031; // 粉红世界
        int SPLIT = 0x2030; // 黑白漏光
        int GRADIENTBLUE = 0x2032; // 迷幻浅蓝
        int WBFILM = 0x2033; // 黑白纪实
        int PRO = 0x2034; // 浓墨
        int LIGHTENGREEN = 0x2035; // 绿野
        int LIGHTENGREEN2 = 0x2036; // 哑光绿
        int WENYI = 0x2037; // 文艺
        int BRIGHTGREEN = 0x2038; // 纯绿
        int SIMPLEELEGANT3 = 0x2039; // 朴素
        int COUNTRY = 0x203A; // 厚重黄
        int WB = 0x2096; // 赫本
        int DEPURPLE = 0x3003; // 去紫边
        int SHARPEN = 0x3004; // 锐化
    }

    interface COMPOSITE {

        int UNDEFINED = 0;
        int NORMAL = 1; // 图层模式: 无
        int MODULUS_ADD = 2;
        int ATOP = 3;
        int BLEND = 4;
        int BUMPMAP = 5;
        int CHANGE_MASK = 6;
        int CLEAR = 7;
        int COLOR_BURN = 8; // 颜色加深
        int COLOR_DODGE = 9; // 颜色减淡
        int COLORIZE = 10;
        int COPY_BLACK = 11;
        int COPY_BLUE = 12;
        int COPY = 13;
        int COPY_CYAN = 14;
        int COPY_GREEN = 15;
        int COPY_MAGENTA = 16;
        int COPY_OPACITY = 17;
        int COPY_RED = 18;
        int COPY_YELLOW = 19;
        int DARKEN = 20; // 变暗
        int DST_ATOP = 21;
        int DST = 22;
        int DST_IN = 23;
        int DST_OUT = 24;
        int DST_OVER = 25;
        int DIFFERENCE = 26; // 差值
        int DISPLACE = 27;
        int DISSOLVE = 28;
        int EXCLUSION = 29; // 排除
        int HARD_LIGHT = 30; // 强光
        int HUE = 31;
        int IN = 32;
        int LIGHTEN = 33; // 变亮
        int LINEAR_LIGHT = 34; // 线性光
        int LUMINIZE = 35;
        int MINUS = 36;
        int MODULATE = 37;
        int MULTIPLY = 38; // 正片叠底
        int OUT = 39;
        int OVER = 40;
        int OVERLAY = 41; // 叠加
        int PLUS = 42;
        int REPLACE = 43;
        int SATURATE = 44;
        int Screen = 45; // 滤色
        int SoftLight = 46; // 柔光
        int SRC_ATOP = 47;
        int SRC = 48;
        int SRC_IN = 49;
        int SRC_OUT = 50;
        int SRC_OVER = 51;
        int MODULUS_SUBTRACT = 52;
        int THRESHOLD = 53;
        int XOR = 54;
        int DIVIDE = 55;
        int DISTORT = 56;
        int BLUR = 57;
        int PEGTOP_LIGHT = 58;
        int VIVID_LIGHT = 59; // 亮光
        int PIN_LIGHT = 60;
        int LINEAR_DODGE = 61; // 线性减淡
        int LINEAR_BURN = 62; // 线性加深
        int MATHEMATICS = 63;
    }

    interface CHANNEL {

        int UNDEFINED = 0x0000;
        int ALL = 0xff;
        int RED = 0x0001;
        int GRAY = 0x0001;
        int CYAN = 0x0001;
        int GREEN = 0x0002;
        int MAGENTA = 0x0002;
        int BLUE = 0x0004;
        int YELLOW = 0x0004;
        int ALPHA = 0x0008;
        int OPACITY = 0x0008;
        int MATTE = 0x0008;  // deprecated
        int BLACK = 0x0020;
        int INDEX = 0x0020;
        int DEFAULTS = (ALL & ~OPACITY);
    }
}
