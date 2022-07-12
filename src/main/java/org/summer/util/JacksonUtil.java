package org.summer.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.summer.net.packet.Packet;

import java.nio.charset.StandardCharsets;

public class JacksonUtil {
    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toObject(byte[] jsonBytes, Class<T> clazz) {
        return toObject(new String(jsonBytes, StandardCharsets.UTF_8), clazz);
    }

    public static <T> T getPayload(Packet packet, Class<T> clazz) {
        return toObject(packet.getPayload(), clazz);
    }

    public static String toJson(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] toJsonBytes(Object object) {
        return toJson(object).getBytes(StandardCharsets.UTF_8);
    }

}
