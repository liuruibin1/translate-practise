package com.xxx.common.core.utils;

import com.xxx.common.core.constants.HttpConstant;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 字符串工具类
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    /**
     * 空字符串
     */
    private static final String NULLSTR = "";

    /**
     * 下划线
     */
    private static final char SEPARATOR = '_';

    /**
     * 获取参数不为空值
     *
     * @param value defaultValue 要判断的value
     * @return value 返回值
     */
    public static <T> T nvl(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    /**
     * * 判断一个Collection是否为空， 包含List，Set，Queue
     *
     * @param coll 要判断的Collection
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Collection<?> coll) {
        return isNull(coll) || coll.isEmpty();
    }

//    /**
//     * * 判断一个Collection是否非空，包含List，Set，Queue
//     *
//     * @param coll 要判断的Collection
//     * @return true：非空 false：空
//     */
//    public static boolean isNotEmpty(Collection<?> coll) {
//        return !isEmpty(coll);
//    }

//    /**
//     * * 判断一个对象数组是否为空
//     *
//     * @param objects 要判断的对象数组
//     *                * @return true：为空 false：非空
//     */
//    public static boolean isEmpty(Object[] objects) {
//        return isNull(objects) || (objects.length == 0);
//    }

//    /**
//     * * 判断一个对象数组是否非空
//     *
//     * @param objects 要判断的对象数组
//     * @return true：非空 false：空
//     */
//    public static boolean isNotEmpty(Object[] objects) {
//        return !isEmpty(objects);
//    }

//    /**
//     * * 判断一个Map是否为空
//     *
//     * @param map 要判断的Map
//     * @return true：为空 false：非空
//     */
//    public static boolean isEmpty(Map<?, ?> map) {
//        return isNull(map) || map.isEmpty();
//    }

//    /**
//     * * 判断一个Map是否为空
//     *
//     * @param map 要判断的Map
//     * @return true：非空 false：空
//     */
//    public static boolean isNotEmpty(Map<?, ?> map) {
//        return !isEmpty(map);
//    }

    /**
     * * 判断一个字符串是否为空串
     *
     * @param str String
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(String str) {
        return ObjectUtils.isNull(str) || NULLSTR.equals(str.trim());
    }

    /**
     * * 判断一个字符串是否为非空串
     *
     * @param str String
     * @return true：非空串 false：空串
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断一个对象是否为空
     *
     * @param object Object
     * @return true：为空 false：非空
     */
    public static boolean isNull(Object object) {
        return object == null;
    }

//    /**
//     * * 判断一个对象是否非空
//     *
//     * @param object Object
//     * @return true：非空 false：空
//     */
//    public static boolean isNotNull(Object object) {
//        return !isNull(object);
//    }

//    /**
//     * * 判断一个对象是否是数组类型（Java基本型别的数组）
//     *
//     * @param object 对象
//     * @return true：是数组 false：不是数组
//     */
//    public static boolean isArray(Object object) {
//        return isNotNull(object) && object.getClass().isArray();
//    }

    /**
     * 去空格
     */
    public static String trim(String str) {
        return (str == null ? "" : str.trim());
    }

    /**
     * 截取字符串
     *
     * @param str   字符串
     * @param start 开始
     * @return 结果
     */
    public static String substring(final String str, int start) {
        if (str == null) {
            return NULLSTR;
        }

        if (start < 0) {
            start = str.length() + start;
        }

        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return NULLSTR;
        }

        return str.substring(start);
    }

    /**
     * 截取字符串
     *
     * @param str   字符串
     * @param start 开始
     * @param end   结束
     * @return 结果
     */
    public static String substring(final String str, int start, int end) {
        if (str == null) {
            return NULLSTR;
        }

        if (end < 0) {
            end = str.length() + end;
        }
        if (start < 0) {
            start = str.length() + start;
        }

        if (end > str.length()) {
            end = str.length();
        }

        if (start > end) {
            return NULLSTR;
        }

        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }

        return str.substring(start, end);
    }

    /**
     * 判断是否为空，并且不是空白字符
     *
     * @param str 要判断的value
     * @return 结果
     */
    public static boolean hasText(String str) {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

//    /**
//     * 格式化文本, {} 表示占位符<br>
//     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
//     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
//     * 例：<br>
//     * 通常使用：format("this is {} for {}", "a", "b") -> this is a for b<br>
//     * 转义{}： format("this is \\{} for {}", "a", "b") -> this is \{} for a<br>
//     * 转义\： format("this is \\\\{} for {}", "a", "b") -> this is \a for b<br>
//     *
//     * @param template 文本模板，被替换的部分用 {} 表示
//     * @param params   参数值
//     * @return 格式化后的文本
//     */
//    public static String format(String template, Object... params) {
//        if (isEmpty(params) || isEmpty(template)) {
//            return template;
//        }
//        return StrFormatter.format(template, params);
//    }

    /**
     * 是否为http(s)://开头
     *
     * @param link 链接
     * @return 结果
     */
    public static boolean isHttp(String link) {
        return StringUtils.startsWithAny(link, HttpConstant.HTTP, HttpConstant.HTTPS);
    }

//    /**
//     * 判断给定的collection列表中是否包含数组array 判断给定的数组array中是否包含给定的元素value
//     *
//     * @param collection 给定的集合
//     * @param array      给定的数组
//     * @return boolean 结果
//     */
//    public static boolean containsAny(Collection<String> collection, String... array) {
//        if (isEmpty(collection) || isEmpty(array)) {
//            return false;
//        } else {
//            for (String str : array) {
//                if (collection.contains(str)) {
//                    return true;
//                }
//            }
//            return false;
//        }
//    }

    /**
     * 驼峰转下划线命名
     */
    public static String toUnderScoreCase(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        // 前置字符是否大写
        boolean preCharIsUpperCase = true;
        // 当前字符是否大写
        boolean curreCharIsUpperCase = true;
        // 下一字符是否大写
        boolean nexteCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (i > 0) {
                preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
            } else {
                preCharIsUpperCase = false;
            }

            curreCharIsUpperCase = Character.isUpperCase(c);

            if (i < (str.length() - 1)) {
                nexteCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }

            if (preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase) {
                sb.append(SEPARATOR);
            } else if ((i != 0 && !preCharIsUpperCase) && curreCharIsUpperCase) {
                sb.append(SEPARATOR);
            }
            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 是否包含字符串
     *
     * @param str  验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inStringIgnoreCase(String str, String... strs) {
        if (str != null && strs != null) {
            for (String s : strs) {
                if (str.equalsIgnoreCase(trim(s))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。 例如：HELLO_WORLD->HelloWorld
     *
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String convertToCamelCase(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母大写
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        // 用下划线将原始字符串分割
        String[] camels = name.split("_");
        for (String camel : camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 首字母大写
            result.append(camel.substring(0, 1).toUpperCase());
            result.append(camel.substring(1).toLowerCase());
        }
        return result.toString();
    }

    /**
     * 驼峰式命名法
     * 例如：user_name->userName
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }
        if (s.indexOf(SEPARATOR) == -1) {
            return s;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 查找指定字符串是否匹配指定字符串列表中的任意一个字符串
     *
     * @param str  指定字符串
     * @param strs 需要检查的字符串数组
     * @return 是否匹配
     */
    public static boolean matches(String str, List<String> strs) {
        if (isEmpty(str) || isEmpty(strs)) {
            return false;
        }
        for (String pattern : strs) {
            if (isMatch(pattern, str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断url是否与规则配置:
     * ? 表示单个字符;
     * * 表示一层路径内的任意字符串，不可跨层级;
     * ** 表示任意层路径;
     *
     * @param pattern 匹配规则
     * @param url     需要匹配的url
     * @return
     */
    public static boolean isMatch(String pattern, String url) {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match(pattern, url);
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        return (T) obj;
    }

    /**
     * 数字左边补齐0，使之达到指定长度。注意，如果数字转换为字符串后，长度大于size，则只保留 最后size个字符。
     *
     * @param num  数字对象
     * @param size 字符串指定长度
     * @return 返回数字的字符串格式，该字符串为指定长度。
     */
    public static final String padl(final Number num, final int size) {
        return padl(num.toString(), size, '0');
    }

    /**
     * 字符串左补齐。如果原始字符串s长度大于size，则只保留最后size个字符。
     *
     * @param s    原始字符串
     * @param size 字符串指定长度
     * @param c    用于补齐的字符
     * @return 返回指定长度的字符串，由原字符串左补齐或截取得到。
     */
    public static final String padl(final String s, final int size, final char c) {
        final StringBuilder sb = new StringBuilder(size);
        if (s != null) {
            final int len = s.length();
            if (s.length() <= size) {
                for (int i = size - len; i > 0; i--) {
                    sb.append(c);
                }
                sb.append(s);
            } else {
                return s.substring(len - size, len);
            }
        } else {
            for (int i = size; i > 0; i--) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 去除特殊字符、空格，替换为单个横杆
     *
     * @param str
     * @return
     */
    public static String toSlug(String str) {
        str = str.replaceAll("^\\W*", "");
        str = str.replaceAll("\\W*$", "");
        str = str.replaceAll("[\\W*,\\s*]", "-");
        str = str.replaceAll("-+", "-");
        str = str.toLowerCase();
        return str;
    }

    public static List<List<String>> group(List<String> originalList, int groupSize) {
        int listSize = originalList.size();
        int groupCount = (int) Math.ceil((double) listSize / groupSize);
        List<List<String>> groupedLists = new ArrayList<>();
        for (int i = 0; i < groupCount; i++) {
            groupedLists.add(new ArrayList<>());
        }
        for (int i = 0; i < listSize; i++) {
            int groupIndex = i / groupSize;
            groupedLists.get(groupIndex).add(originalList.get(i));
        }
        return groupedLists;
    }

    //    public static void main(String[] args) {
    //
    //        String str = "Access www.peapods.site";
    //        System.out.println(toSlug(str));
    //
    //        str = "! ARBchest.com";
    //        System.out.println(toSlug(str));
    //
    //        str = "! Access stETH.claims to claim rewards";
    //        System.out.println(toSlug(str));
    //
    //        str = "! apy-aave.com";
    //        System.out.println(toSlug(str));
    //
    //        str = "! fetpool.com";
    //        System.out.println(toSlug(str));
    //
    //        str = "! matic-nft.com";
    //        System.out.println(toSlug(str));
    //
    //        str = "! syncpool.xyz";
    //        System.out.println(toSlug(str));
    //
    //        str = "! unibotpool.com";
    //        System.out.println(toSlug(str));
    //
    //        str = "!RISE Music";
    //        System.out.println(toSlug(str));
    //
    //        str = "!Role Selection!";
    //        System.out.println(toSlug(str));
    //
    //        str = "!Steamboat Willie remake";
    //        System.out.println(toSlug(str));
    //
    //        str = "!vibez_2";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"1958\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"ATMA\" The Demon Priest\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Abyss of Worlds: Harbingers of the Apocalypse\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Activation of the Absolute\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Americas Cup #1\" by Greg Stirling\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"An Ode to Maria\" - Exhibition Lineup\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Armelle\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Awakening\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Beautiful Woman #1\" by Greg Stirling\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Cabinet de formes organiques...\" (chapter I)\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Censored.\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Coin Cidence\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Cosmic Alchemy\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Cosmos\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Don´t think too much\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Duke Nukem: Nostalgia Retro Edition\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Eagle Nebula\" by Greg Stirling\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Ehigie Eben\" - The powerful sword\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Emerald\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Evolving Portals\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"FRENS IN HIGH PLACES\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Fishrooms\" & \"SeaTrain\" series, Colorful fish, beautiful sea,\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Flowers of Tales\" for Smartphones\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Fur : Touch for eyes\" for Smartphones\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Give children a bright future.\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Give children a bright future.\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Give children a bright future.\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Great Personalities Of  The  World\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Great Personalities Of  The  World\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Guudbye Jawn\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Harmony of Beauty\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Invisible Women\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"JIEI\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"JIEI\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Joy Bringer\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Kiku Ningyo\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"LOVERS CriptoArte\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Limited Edition: Violin - Digital Art\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"London Cipher\" by ARTJEDI1\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Maison Hannon reenchanted - Loop\" (Maria Corte), by ARTTS\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Maison Hannon reenchanted - Mosaic\" (Maria Corte), by ARTTS\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Maison Hannon reenchanted - Permanence\" (Maria Corte), by ARTTS\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Maison Hannon reenchanted - Twilight\" (Maria Corte), by ARTTS\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Malerei\" Naturfotografie\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Me, Mysel & I\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Meaning And Memories.\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"My Father, My Pillar 2\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"NFT_STUDIES\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Nkemjika\" -My own is supreme\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Nostalgic Dimensions\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Not a muse. The somebody\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"OIL MONEY\" AND THE SWEAT-STAINED DREAMS OF BOYS\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"ONE\"   by LxS\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Omnipresent\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Party Snaps: Analog Charm\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Peace Dove.\" SABET x Costas Lenas\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Pwalls\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Reformulation of the Absolute\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Relicts of the Unknown\" Preliminary Drop\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Retro Perspective\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"SHIFT SIMMERS SLIPS\" (diaries and notes)\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"SHINJUKU THIEF PARTY\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"SILENT LANGUAGE OF LOVE\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Sakura's Dream Canvas: A Glimpse into Artistic Wonders\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Steal His Look\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Steel Tails\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"THE CONTRACT\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"The Capricious Master of Discord\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"The Volcano's Sacrifice\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"The origami mystery: Queen of the night\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"The problem\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Triumph Bonneville #1\" by Greg Stirling\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"Ubuntu Ngabantu,\" a Zulu philosophy translating to \"I am what I am because of who we all are\".";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"hip hop rabbit\" Daromi\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"i\" for imaginations\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"intence\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"life in motion\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"the MONOLITH signature\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "# Access usdtv3.com to claim rewards";
    //        System.out.println(toSlug(str));
    //
    //        str = "# Access usdtv3.com to claim rewards";
    //        System.out.println(toSlug(str));
    //
    //        str = "# HOME HISTORY";
    //        System.out.println(toSlug(str));
    //
    //        str = "# dextools.events";
    //        System.out.println(toSlug(str));
    //
    //        str = "# liquidlayer.win";
    //        System.out.println(toSlug(str));
    //
    //        str = "# mb-aave.com";
    //        System.out.println(toSlug(str));
    //
    //        str = "# status.gifts";
    //        System.out.println(toSlug(str));
    //
    //        str = "# truebit.gift";
    //        System.out.println(toSlug(str));
    //
    //        str = "#0";
    //        System.out.println(toSlug(str));
    //
    //        str = "#000 Collection";
    //        System.out.println(toSlug(str));
    //
    //        str = "#0worth -2092";
    //        System.out.println(toSlug(str));
    //
    //        str = "#1";
    //        System.out.println(toSlug(str));
    //
    //        str = "#1";
    //        System.out.println(toSlug(str));
    //
    //        str = "#1";
    //        System.out.println(toSlug(str));
    //
    //        str = "#1";
    //        System.out.println(toSlug(str));
    //
    //        str = "#1 Tobacco Monkey";
    //        System.out.println(toSlug(str));
    //
    //        str = "#1 the last women";
    //        System.out.println(toSlug(str));
    //
    //        str = "#30secondsproject";
    //        System.out.println(toSlug(str));
    //
    //        str = "#458";
    //        System.out.println(toSlug(str));
    //
    //        str = "#5";
    //        System.out.println(toSlug(str));
    //
    //        str = "#5Yellow";
    //        System.out.println(toSlug(str));
    //
    //        str = "#? TAVBVMICWMG";
    //        System.out.println(toSlug(str));
    //
    //        str = "#DP%Single Poem%Digital%Eth";
    //        System.out.println(toSlug(str));
    //
    //        str = "#E00050rr";
    //        System.out.println(toSlug(str));
    //
    //        str = "#E00050rr";
    //        System.out.println(toSlug(str));
    //
    //        str = "#E44E29";
    //        System.out.println(toSlug(str));
    //
    //        str = "#E44E29 OE";
    //        System.out.println(toSlug(str));
    //
    //        str = "#E44E29-B";
    //        System.out.println(toSlug(str));
    //
    //        str = "#E44E29-D";
    //        System.out.println(toSlug(str));
    //
    //        str = "#IAmAI";
    //        System.out.println(toSlug(str));
    //
    //        str = "#STARLINKFORGAZA";
    //        System.out.println(toSlug(str));
    //
    //        str = "#TRASHART OF THE DAY";
    //        System.out.println(toSlug(str));
    //
    //        str = "#VeeBreakZero";
    //        System.out.println(toSlug(str));
    //
    //        str = "#Winter home";
    //        System.out.println(toSlug(str));
    //
    //        str = "#Woman_Life_Freedom";
    //        System.out.println(toSlug(str));
    //
    //        str = "#Woman_Life_Freedom";
    //        System.out.println(toSlug(str));
    //
    //        str = "#_abstract_collection_001";
    //        System.out.println(toSlug(str));
    //
    //        str = "#_abstract_collection_001";
    //        System.out.println(toSlug(str));
    //
    //        str = "#d models";
    //        System.out.println(toSlug(str));
    //
    //        str = "#daysuntiltomorrow";
    //        System.out.println(toSlug(str));
    //
    //        str = "#goodby";
    //        System.out.println(toSlug(str));
    //
    //        str = "#themeappreciation #国際お題鑑賞会";
    //        System.out.println(toSlug(str));
    //
    //        str = "$1 Art";
    //        System.out.println(toSlug(str));
    //
    //        str = "$10 000 FOR FREE";
    //        System.out.println(toSlug(str));
    //
    //        str = "$10 000 FOR FREE";
    //        System.out.println(toSlug(str));
    //
    //        str = "$29";
    //        System.out.println(toSlug(str));
    //
    //        str = "$5 stETH Voucher";
    //        System.out.println(toSlug(str));
    //
    //        str = "$666 in your mind";
    //        System.out.println(toSlug(str));
    //
    //        str = "$AURA";
    //        System.out.println(toSlug(str));
    //
    //        str = "$Bear BRC20";
    //        System.out.println(toSlug(str));
    //
    //        str = "$Blur Bunnies";
    //        System.out.println(toSlug(str));
    //
    //        str = "$CALendar Girls Special Edition Series";
    //        System.out.println(toSlug(str));
    //
    //        str = "$FARTS";
    //        System.out.println(toSlug(str));
    //
    //        str = "$GRIN";
    //        System.out.println(toSlug(str));
    //
    //        str = "$GTA Punks";
    //        System.out.println(toSlug(str));
    //
    //        str = "$HOLA 0.01% Revenue Share NFTs";
    //        System.out.println(toSlug(str));
    //
    //        str = "$MOGsters";
    //        System.out.println(toSlug(str));
    //
    //        str = "$SCHIZO Miner";
    //        System.out.println(toSlug(str));
    //
    //        str = "$USS PILL";
    //        System.out.println(toSlug(str));
    //
    //        str = "$alvator Mundi";
    //        System.out.println(toSlug(str));
    //
    //        str = "$er’s Castle";
    //        System.out.println(toSlug(str));
    //
    //        str = "' dextools.win";
    //        System.out.println(toSlug(str));
    //
    //        str = "'' A S T R A L ''";
    //        System.out.println(toSlug(str));
    //
    //        str = "'' In Silence ''";
    //        System.out.println(toSlug(str));
    //
    //        str = "'' Tired Mind ''";
    //        System.out.println(toSlug(str));
    //
    //        str = "''Colorful World''";
    //        System.out.println(toSlug(str));
    //
    //        str = "\"''You, Me and Me''\"";
    //        System.out.println(toSlug(str));
    //
    //        str = "'0x0ai Mysterybox NFT";
    //        System.out.println(toSlug(str));
    //
    //        str = "'83 seeds from a vanishing mountain' by Sofia Crespo x Anna Ridler";
    //        System.out.println(toSlug(str));
    //
    //        str = "'GROK Mysterybox NFT";
    //        System.out.println(toSlug(str));
    //
    //        str = "'PEPE Mysterybox NFT";
    //        System.out.println(toSlug(str));
    //
    //        str = "'SEED' of Love";
    //        System.out.println(toSlug(str));
    //
    //        str = "'SOUL OF JAPAN: Re' by Adri The Ghost";
    //        System.out.println(toSlug(str));
    //
    //        str = "'TIS THE SEASON";
    //        System.out.println(toSlug(str));
    //
    //        str = "'TIS THE SEASON 2";
    //        System.out.println(toSlug(str));
    //
    //        str = "'The Awakening' by Iness Rychlik";
    //        System.out.println(toSlug(str));
    //
    //        str = "'www.bitrockpool.xyz";
    //        System.out.println(toSlug(str));
    //
    //        str = "'www.etepool.xyz";
    //        System.out.println(toSlug(str));
    //
    //        str = "'www.gomining.site";
    //        System.out.println(toSlug(str));
    //
    //        str = "'www.ixspool.xyz";
    //        System.out.println(toSlug(str));
    //
    //        str = "'www.lootbot.top";
    //        System.out.println(toSlug(str));
    //
    //        str = "'www.pendlepool.xyz";
    //        System.out.println(toSlug(str));
    //
    //        str = "'www.realio.site";
    //        System.out.println(toSlug(str));
    //
    //        str = "'www.shido.top";
    //        System.out.println(toSlug(str));
    //
    //        str = "(81-82-83-84) by Daniel Catt";
    //        System.out.println(toSlug(str));
    //
    //        str = "(B)APEVOLUTION";
    //        System.out.println(toSlug(str));
    //
    //        str = "(DO NOT BUY)";
    //        System.out.println(toSlug(str));
    //
    //        str = "(N)";
    //        System.out.println(toSlug(str));
    //
    //        str = "(Reservation) Imaginary animal encyclopedia";
    //        System.out.println(toSlug(str));
    //
    //        str = "(Roses and Candles)";
    //        System.out.println(toSlug(str));
    //
    //        str = "(un)curated collective";
    //        System.out.println(toSlug(str));
    //
    //        str = "(✿◠‿◠) Barbie Lady Maker (✿◠‿◠)";
    //        System.out.println(toSlug(str));
    //
    //        str = "(￣bassets￣)";
    //        System.out.println(toSlug(str));
    //
    //        str = "(𝐮𝐧)𝐯𝐞𝐢𝐥𝐞𝐝 𝐠𝐫𝐚𝐜𝐞 𝙭 𝙂𝙈";
    //        System.out.println(toSlug(str));
    //
    //        str = "*Archived";
    //        System.out.println(toSlug(str));
    //
    //        str = "*Crescent Nature Moon*";
    //        System.out.println(toSlug(str));
    //
    //        str = "*GREATER THAN OR EQUAL TO";
    //        System.out.println(toSlug(str));
    //
    //        str = "+33th";
    //        System.out.println(toSlug(str));
    //
    //        str = "-";
    //        System.out.println(toSlug(str));
    //
    //        str = "- AI Girls NFT -";
    //        System.out.println(toSlug(str));
    //
    //        str = "- SOULMATE -";
    //        System.out.println(toSlug(str));
    //
    //        str = "- SUNLIGHT -";
    //        System.out.println(toSlug(str));
    //
    //        str = "-------#xxx4";
    //        System.out.println(toSlug(str));
    //
    //        str = "--------111";
    //        System.out.println(toSlug(str));
    //
    //        str = "-Harrison-M-";
    //        System.out.println(toSlug(str));
    //
    //        str = "-Harrison-M-";
    //        System.out.println(toSlug(str));
    //
    //        str = "-Off Shoulders Collection-";
    //        System.out.println(toSlug(str));
    //
    //        str = "-Suzuchii Collection-";
    //        System.out.println(toSlug(str));
    //
    //        str = ".....1";
    //        System.out.println(toSlug(str));
    //
    //        str = "...fantastische Welt der Illusionen...";
    //        System.out.println(toSlug(str));
    //
    //        str = ".CYBERPUNK-LANTERN-DEER";
    //        System.out.println(toSlug(str));
    //
    //        str = ".eav";
    //        System.out.println(toSlug(str));
    //
    //        str = ".nobackground";
    //        System.out.println(toSlug(str));
    //
    //        str = "// ConAct // - Sculptures";
    //        System.out.println(toSlug(str));
    //
    //        str = "// STAMPED //";
    //        System.out.println(toSlug(str));
    //
    //        str = "//STAMPED//";
    //        System.out.println(toSlug(str));
    //
    //        str = "//STILLS//";
    //        System.out.println(toSlug(str));
    //
    //        str = "//\\BODY//\\PSYCHE//\\LANGUAGE//\\";
    //        System.out.println(toSlug(str));
    //
    //        str = "/1";
    //        System.out.println(toSlug(str));
    //
    //        str = "/PS2.pso.iso[]";
    //        System.out.println(toSlug(str));
    //
    //        str = "/REMIX/";
    //        System.out.println(toSlug(str));
    //
    //        str = "/VOID-NEXUS/";
    //        System.out.println(toSlug(str));
    //
    //        str = "/ｂ▄▂ｅήᵈ² Ͱ҉̅҉̅҉̅҉̅҉̅🍭​⛓️";
    //        System.out.println(toSlug(str));
    //
    //        str = "0 Twitter Hex ETH";
    //        System.out.println(toSlug(str));
    //
    //        str = "0 to 1";
    //        System.out.println(toSlug(str));
    //
    //        str = "0.6 ETH";
    //        System.out.println(toSlug(str));
    //
    //        str = "00-0-00";
    //        System.out.println(toSlug(str));
    //
    //        str = "000000 KINGS";
    //        System.out.println(toSlug(str));
    //
    //        str = "000001 - lazuliluzal";
    //        System.out.println(toSlug(str));
    //
    //        str = "00lilis";
    //        System.out.println(toSlug(str));
    //
    //        str = "00x1";
    //        System.out.println(toSlug(str));
    //
    //        str = "01'1'";
    //        System.out.println(toSlug(str));
    //
    //        str = "0106ChurchoftheNativity";
    //        System.out.println(toSlug(str));
    //
    //        str = "01L-P45T3L5";
    //        System.out.println(toSlug(str));
    //
    //        str = "01_Machu Picchu";
    //        System.out.println(toSlug(str));
    //
    //        str = "02_Stonehenge";
    //        System.out.println(toSlug(str));
    //
    //        str = "02_Stonehenge";
    //        System.out.println(toSlug(str));
    //
    //        str = "03 Steps";
    //        System.out.println(toSlug(str));
    //
    //        str = "07 Photo Collection";
    //        System.out.println(toSlug(str));
    //
    //        str = "0773H_World.exe";
    //        System.out.println(toSlug(str));
    //
    //        str = "0FF";
    //        System.out.println(toSlug(str));
    //
    //        str = "0N1 0R1G1NS Airdrop";
    //        System.out.println(toSlug(str));
    //
    //        str = "0N1 0R1G1NS Airdrop";
    //        System.out.println(toSlug(str));
    //
    //        str = "0N1 0R1G1NS Airdrop";
    //        System.out.println(toSlug(str));
    //
    //        str = "0N1 FORCE: Juju Smith-Schuster 0N1 KA1 Bobblehead Token";
    //        System.out.println(toSlug(str));
    //
    //        str = "0N1 R0N1N";
    //        System.out.println(toSlug(str));
    //
    //        str = "0N1 R0N1N";
    //        System.out.println(toSlug(str));
    //
    //        str = "0XLBOTS";
    //        System.out.println(toSlug(str));
    //
    //        str = "0XLBOTS";
    //        System.out.println(toSlug(str));
    //
    //        str = "0XMAYKI";
    //        System.out.println(toSlug(str));
    //
    //        str = "0XSHAPES";
    //        System.out.println(toSlug(str));
    //
    //        str = "0l250l";
    //        System.out.println(toSlug(str));
    //
    //        str = "0o";
    //        System.out.println(toSlug(str));
    //
    //        str = "0x0.ai New Year Gift";
    //        System.out.println(toSlug(str));
    //
    //        str = "0x048";
    //        System.out.println(toSlug(str));
    //    }

}