package org.summer.net.handler;

import cn.hutool.core.util.ClassUtil;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.summer.net.OpCode;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PacketHandlerRegistry {
    private final Map<Integer, PacketHandler> handlerMap = new HashMap<>();

    public void addHandler(Integer code, PacketHandler handler) {
        handlerMap.put(code, handler);
    }

    public PacketHandler getHandler(Integer code) {
        return handlerMap.get(code);
    }

    public void loadAllHandlers() {
        try {
            Reflections reflections = new Reflections("org.summer");
            Set<Class<? extends PacketHandler>> handlerTypes = reflections.getSubTypesOf(PacketHandler.class);
            for (var handlerType : handlerTypes) {
                OpCode opCode = handlerType.getAnnotation(OpCode.class);
                if (opCode == null) {
                    continue;
                }
                addHandler(opCode.code(), handlerType.getConstructor().newInstance());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //singleton
    private static final PacketHandlerRegistry INSTANCE = new PacketHandlerRegistry();
    public static PacketHandlerRegistry getInstance() {
        return INSTANCE;
    }

}
