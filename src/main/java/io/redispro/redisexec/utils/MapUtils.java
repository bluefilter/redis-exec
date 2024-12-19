package io.redispro.redisexec.utils;

import java.util.Map;

public class MapUtils {
    public static String getStr(Map<String, Object> map, String key) {
        if (null != map) {
            return (String) map.getOrDefault(key, null);
        } else
            return null;
    }
}
