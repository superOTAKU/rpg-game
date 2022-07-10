package org.summer.net.handler;

import java.util.HashMap;
import java.util.Map;

public class PacketHandlerRegistry {
    private final Map<Integer, PacketHandler> handlerMap = new HashMap<>();

    public void addHandler(Integer code, PacketHandler handler) {
        handlerMap.put(code, handler);
    }

    public PacketHandler getHandler(Integer code) {
        return handlerMap.get(code);
    }

    //singleton
    private static final PacketHandlerRegistry INSTANCE = new PacketHandlerRegistry();
    public static PacketHandlerRegistry getInstance() {
        return INSTANCE;
    }

}
