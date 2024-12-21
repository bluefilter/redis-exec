package io.redispro.redisexec.dto;

import lombok.Getter;

@Getter
public enum RedisDataType {
    STRING("string"),
    LIST("list"),
    SET("set"),
    ZSET("zset"), // Sorted Set
    HASH("hash"),
    STREAM("stream"),
    BITMAP("bitmap"), // 비트맵 (Bitmap)
    HYPERLOGLOG("hyperloglog"), // 하이퍼로그로그 (HyperLogLog)
    GEO("geo"), // 지리적 데이터 (Geospatial)
    NONE("none"); // Key가 존재하지 않을 때

    private final String typeName;

    RedisDataType(String typeName) {
        this.typeName = typeName;
    }

    public static RedisDataType fromTypeName(String typeName) {
        for (RedisDataType type : RedisDataType.values()) {
            if (type.typeName.equalsIgnoreCase(typeName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown Redis data type: " + typeName);
    }
}
