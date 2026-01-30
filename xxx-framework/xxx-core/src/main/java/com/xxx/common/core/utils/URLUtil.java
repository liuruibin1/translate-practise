package com.xxx.common.core.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class URLUtil {

    public static String encodeUTF_8(String str) {
        return URLEncoder.encode(str, StandardCharsets.UTF_8);
    }

    public static String decodeUTF_8(String str) {
        return URLDecoder.decode(str, StandardCharsets.UTF_8);
    }

}
