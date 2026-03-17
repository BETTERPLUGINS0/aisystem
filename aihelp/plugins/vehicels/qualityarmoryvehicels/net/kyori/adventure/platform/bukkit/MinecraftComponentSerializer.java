/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Experimental
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.platform.bukkit;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;
import net.kyori.adventure.platform.bukkit.BukkitComponentSerializer;
import net.kyori.adventure.platform.bukkit.MinecraftReflection;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Experimental
public final class MinecraftComponentSerializer
implements ComponentSerializer<Component, Component, Object> {
    private static final MinecraftComponentSerializer INSTANCE = new MinecraftComponentSerializer();
    @Nullable
    private static final Class<?> CLASS_JSON_DESERIALIZER = MinecraftReflection.findClass("com.goo".concat("gle.gson.JsonDeserializer"));
    @Nullable
    private static final Class<?> CLASS_CHAT_COMPONENT = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("IChatBaseComponent"), MinecraftReflection.findMcClassName("network.chat.IChatBaseComponent"), MinecraftReflection.findMcClassName("network.chat.Component"));
    @Nullable
    private static final Class<?> CLASS_CRAFT_REGISTRY = MinecraftReflection.findCraftClass("CraftRegistry");
    @Nullable
    private static final Class<?> CLASS_REGISTRY_ACCESS = MinecraftReflection.findClass(MinecraftReflection.findMcClassName("core.IRegistryCustom"), MinecraftReflection.findMcClassName("core.RegistryAccess"));
    @Nullable
    private static final MethodHandle GET_REGISTRY = MinecraftReflection.findStaticMethod(CLASS_CRAFT_REGISTRY, "getMinecraftRegistry", CLASS_REGISTRY_ACCESS, new Class[0]);
    private static final AtomicReference<RuntimeException> INITIALIZATION_ERROR = new AtomicReference<UnsupportedOperationException>(new UnsupportedOperationException());
    private static final Object MC_TEXT_GSON;
    private static final MethodHandle TEXT_SERIALIZER_DESERIALIZE;
    private static final MethodHandle TEXT_SERIALIZER_SERIALIZE;
    private static final MethodHandle TEXT_SERIALIZER_DESERIALIZE_TREE;
    private static final MethodHandle TEXT_SERIALIZER_SERIALIZE_TREE;
    private static final boolean SUPPORTED;

    public static boolean isSupported() {
        return SUPPORTED;
    }

    @NotNull
    public static MinecraftComponentSerializer get() {
        return INSTANCE;
    }

    @Override
    @NotNull
    public Component deserialize(@NotNull Object object) {
        if (!SUPPORTED) {
            throw INITIALIZATION_ERROR.get();
        }
        try {
            JsonElement jsonElement;
            if (TEXT_SERIALIZER_SERIALIZE_TREE != null) {
                jsonElement = TEXT_SERIALIZER_SERIALIZE_TREE.invoke(object);
            } else if (MC_TEXT_GSON != null) {
                jsonElement = ((Gson)MC_TEXT_GSON).toJsonTree(object);
            } else {
                return BukkitComponentSerializer.gson().deserialize(TEXT_SERIALIZER_SERIALIZE.invoke(object));
            }
            return BukkitComponentSerializer.gson().serializer().fromJson(jsonElement, Component.class);
        } catch (Throwable throwable) {
            throw new UnsupportedOperationException(throwable);
        }
    }

    @Override
    @NotNull
    public Object serialize(@NotNull Component component) {
        if (!SUPPORTED) {
            throw INITIALIZATION_ERROR.get();
        }
        if (TEXT_SERIALIZER_DESERIALIZE_TREE != null || MC_TEXT_GSON != null) {
            JsonElement jsonElement = BukkitComponentSerializer.gson().serializer().toJsonTree(component);
            try {
                if (TEXT_SERIALIZER_DESERIALIZE_TREE != null) {
                    return TEXT_SERIALIZER_DESERIALIZE_TREE.invoke(jsonElement);
                }
                return ((Gson)MC_TEXT_GSON).fromJson(jsonElement, CLASS_CHAT_COMPONENT);
            } catch (Throwable throwable) {
                throw new UnsupportedOperationException(throwable);
            }
        }
        try {
            return TEXT_SERIALIZER_DESERIALIZE.invoke((String)BukkitComponentSerializer.gson().serialize(component));
        } catch (Throwable throwable) {
            throw new UnsupportedOperationException(throwable);
        }
    }

    static {
        Object object = null;
        MethodHandle methodHandle = null;
        MethodHandle methodHandle2 = null;
        MethodHandle methodHandle3 = null;
        MethodHandle methodHandle4 = null;
        try {
            if (CLASS_CHAT_COMPONENT != null) {
                Object object2;
                Object object3 = GET_REGISTRY != null ? GET_REGISTRY.invoke() : null;
                Class<?> clazz2 = Arrays.stream(CLASS_CHAT_COMPONENT.getClasses()).filter(clazz -> {
                    if (CLASS_JSON_DESERIALIZER != null) {
                        return CLASS_JSON_DESERIALIZER.isAssignableFrom((Class<?>)clazz);
                    }
                    for (Class<?> clazz2 : clazz.getInterfaces()) {
                        if (!clazz2.getSimpleName().equals("JsonDeserializer")) continue;
                        return true;
                    }
                    return false;
                }).findAny().orElse(MinecraftReflection.findNmsClass("ChatSerializer"));
                if (clazz2 != null && (object2 = (Field)Arrays.stream(clazz2.getDeclaredFields()).filter(field -> Modifier.isStatic(field.getModifiers())).filter(field -> field.getType().equals(Gson.class)).findFirst().orElse(null)) != null) {
                    ((AccessibleObject)object2).setAccessible(true);
                    object = ((Field)object2).get(null);
                }
                object2 = new ArrayList();
                if (clazz2 != null) {
                    object2.add(clazz2);
                }
                object2.addAll(Arrays.asList(CLASS_CHAT_COMPONENT.getClasses()));
                Iterator iterator = object2.iterator();
                while (iterator.hasNext()) {
                    Class clazz3 = (Class)iterator.next();
                    Method[] methodArray = clazz3.getDeclaredMethods();
                    Method method2 = Arrays.stream(methodArray).filter(method -> Modifier.isStatic(method.getModifiers())).filter(method -> CLASS_CHAT_COMPONENT.isAssignableFrom(method.getReturnType())).filter(method -> method.getParameterCount() == 1 && method.getParameterTypes()[0].equals(String.class)).min(Comparator.comparing(Method::getName)).orElse(null);
                    Method method3 = Arrays.stream(methodArray).filter(method -> Modifier.isStatic(method.getModifiers())).filter(method -> method.getReturnType().equals(String.class)).filter(method -> method.getParameterCount() == 1 && CLASS_CHAT_COMPONENT.isAssignableFrom(method.getParameterTypes()[0])).findFirst().orElse(null);
                    Method method4 = Arrays.stream(methodArray).filter(method -> Modifier.isStatic(method.getModifiers())).filter(method -> CLASS_CHAT_COMPONENT.isAssignableFrom(method.getReturnType())).filter(method -> method.getParameterCount() == 1 && method.getParameterTypes()[0].equals(JsonElement.class)).findFirst().orElse(null);
                    Method method5 = Arrays.stream(methodArray).filter(method -> Modifier.isStatic(method.getModifiers())).filter(method -> method.getReturnType().equals(JsonElement.class)).filter(method -> method.getParameterCount() == 1 && CLASS_CHAT_COMPONENT.isAssignableFrom(method.getParameterTypes()[0])).findFirst().orElse(null);
                    Method method6 = Arrays.stream(methodArray).filter(method -> Modifier.isStatic(method.getModifiers())).filter(method -> CLASS_CHAT_COMPONENT.isAssignableFrom(method.getReturnType())).filter(method -> method.getParameterCount() == 2).filter(method -> method.getParameterTypes()[0].equals(JsonElement.class)).filter(method -> method.getParameterTypes()[1].isInstance(object3)).findFirst().orElse(null);
                    Method method7 = Arrays.stream(methodArray).filter(method -> Modifier.isStatic(method.getModifiers())).filter(method -> method.getReturnType().equals(JsonElement.class)).filter(method -> method.getParameterCount() == 2).filter(method -> CLASS_CHAT_COMPONENT.isAssignableFrom(method.getParameterTypes()[0])).filter(method -> method.getParameterTypes()[1].isInstance(object3)).findFirst().orElse(null);
                    if (method2 != null) {
                        methodHandle = MinecraftReflection.lookup().unreflect(method2);
                    }
                    if (method3 != null) {
                        methodHandle2 = MinecraftReflection.lookup().unreflect(method3);
                    }
                    if (method4 != null) {
                        methodHandle3 = MinecraftReflection.lookup().unreflect(method4);
                    } else if (method6 != null) {
                        method6.setAccessible(true);
                        methodHandle3 = MethodHandles.insertArguments(MinecraftReflection.lookup().unreflect(method6), 1, object3);
                    }
                    if (method5 != null) {
                        methodHandle4 = MinecraftReflection.lookup().unreflect(method5);
                        continue;
                    }
                    if (method7 == null) continue;
                    method7.setAccessible(true);
                    methodHandle4 = MethodHandles.insertArguments(MinecraftReflection.lookup().unreflect(method7), 1, object3);
                }
            }
        } catch (Throwable throwable) {
            INITIALIZATION_ERROR.set(new UnsupportedOperationException("Error occurred during initialization", throwable));
        }
        MC_TEXT_GSON = object;
        TEXT_SERIALIZER_DESERIALIZE = methodHandle;
        TEXT_SERIALIZER_SERIALIZE = methodHandle2;
        TEXT_SERIALIZER_DESERIALIZE_TREE = methodHandle3;
        TEXT_SERIALIZER_SERIALIZE_TREE = methodHandle4;
        SUPPORTED = MC_TEXT_GSON != null || TEXT_SERIALIZER_DESERIALIZE != null && TEXT_SERIALIZER_SERIALIZE != null || TEXT_SERIALIZER_DESERIALIZE_TREE != null && TEXT_SERIALIZER_SERIALIZE_TREE != null;
    }
}

