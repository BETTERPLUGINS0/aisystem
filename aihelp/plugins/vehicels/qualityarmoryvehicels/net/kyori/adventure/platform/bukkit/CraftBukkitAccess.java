/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.ItemStack
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.platform.bukkit;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Optional;
import net.kyori.adventure.platform.bukkit.CraftBukkitFacet;
import net.kyori.adventure.platform.bukkit.MinecraftReflection;
import net.kyori.adventure.platform.facet.Knob;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

final class CraftBukkitAccess {
    @Nullable
    static final Class<?> CLASS_CHAT_COMPONENT = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("IChatBaseComponent"), MinecraftReflection.findMcClassName("network.chat.IChatBaseComponent"), MinecraftReflection.findMcClassName("network.chat.Component"));
    @Nullable
    static final Class<?> CLASS_REGISTRY = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("IRegistry"), MinecraftReflection.findMcClassName("core.IRegistry"), MinecraftReflection.findMcClassName("core.Registry"));
    @Nullable
    static final Class<?> CLASS_SERVER_LEVEL = MinecraftReflection.findClass(MinecraftReflection.findMcClassName("server.level.WorldServer"), MinecraftReflection.findMcClassName("server.level.ServerLevel"));
    @Nullable
    static final Class<?> CLASS_LEVEL = MinecraftReflection.findClass(MinecraftReflection.findMcClassName("world.level.World"), MinecraftReflection.findMcClassName("world.level.Level"));
    @Nullable
    static final Class<?> CLASS_REGISTRY_ACCESS = MinecraftReflection.findClass(MinecraftReflection.findMcClassName("core.IRegistryCustom"), MinecraftReflection.findMcClassName("core.RegistryAccess"));
    @Nullable
    static final Class<?> CLASS_RESOURCE_KEY = MinecraftReflection.findClass(MinecraftReflection.findMcClassName("resources.ResourceKey"));
    @Nullable
    static final Class<?> CLASS_RESOURCE_LOCATION = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("MinecraftKey"), MinecraftReflection.findMcClassName("resources.MinecraftKey"), MinecraftReflection.findMcClassName("resources.ResourceLocation"));
    @Nullable
    static final Class<?> CLASS_NMS_ENTITY = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("Entity"), MinecraftReflection.findMcClassName("world.entity.Entity"));
    @Nullable
    static final Class<?> CLASS_BUILT_IN_REGISTRIES = MinecraftReflection.findClass(MinecraftReflection.findMcClassName("core.registries.BuiltInRegistries"));
    @Nullable
    static final Class<?> CLASS_HOLDER = MinecraftReflection.findClass(MinecraftReflection.findMcClassName("core.Holder"));
    @Nullable
    static final Class<?> CLASS_WRITABLE_REGISTRY = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("IRegistryWritable"), MinecraftReflection.findMcClassName("core.IRegistryWritable"), MinecraftReflection.findMcClassName("core.WritableRegistry"));

    private CraftBukkitAccess() {
    }

    static final class Book_1_20_5 {
        static final Class<?> CLASS_CRAFT_ITEMSTACK;
        static final Class<?> CLASS_MC_ITEMSTACK;
        static final Class<?> CLASS_MC_DATA_COMPONENT_TYPE;
        static final Class<?> CLASS_MC_BOOK_CONTENT;
        static final Class<?> CLASS_MC_FILTERABLE;
        static final Class<?> CLASS_CRAFT_REGISTRY;
        static final MethodHandle CREATE_FILTERABLE;
        static final MethodHandle GET_REGISTRY;
        static final MethodHandle CREATE_REGISTRY_KEY;
        static final MethodHandle NEW_RESOURCE_LOCATION;
        static final MethodHandle NEW_BOOK_CONTENT;
        static final MethodHandle REGISTRY_GET_OPTIONAL;
        static final Class<?> CLASS_ENUM_HAND;
        static final Object HAND_MAIN;
        static final MethodHandle MC_ITEMSTACK_SET;
        static final MethodHandle CRAFT_ITEMSTACK_NMS_COPY;
        static final MethodHandle CRAFT_ITEMSTACK_CRAFT_MIRROR;
        static final Object WRITTEN_BOOK_COMPONENT_TYPE;
        static final Class<?> PACKET_OPEN_BOOK;
        static final MethodHandle NEW_PACKET_OPEN_BOOK;

        Book_1_20_5() {
        }

        static boolean isSupported() {
            return WRITTEN_BOOK_COMPONENT_TYPE != null && CREATE_FILTERABLE != null && NEW_BOOK_CONTENT != null && CRAFT_ITEMSTACK_NMS_COPY != null && MC_ITEMSTACK_SET != null && CRAFT_ITEMSTACK_CRAFT_MIRROR != null && NEW_PACKET_OPEN_BOOK != null && HAND_MAIN != null;
        }

        static {
            Object var1_1;
            block5: {
                CLASS_CRAFT_ITEMSTACK = MinecraftReflection.findCraftClass("inventory.CraftItemStack");
                CLASS_MC_ITEMSTACK = MinecraftReflection.findMcClass("world.item.ItemStack");
                CLASS_MC_DATA_COMPONENT_TYPE = MinecraftReflection.findMcClass("core.component.DataComponentType");
                CLASS_MC_BOOK_CONTENT = MinecraftReflection.findMcClass("world.item.component.WrittenBookContent");
                CLASS_MC_FILTERABLE = MinecraftReflection.findMcClass("server.network.Filterable");
                CLASS_CRAFT_REGISTRY = MinecraftReflection.findCraftClass("CraftRegistry");
                CREATE_FILTERABLE = MinecraftReflection.searchMethod(CLASS_MC_FILTERABLE, (Integer)9, "passThrough", CLASS_MC_FILTERABLE, Object.class);
                GET_REGISTRY = MinecraftReflection.findStaticMethod(CLASS_CRAFT_REGISTRY, "getMinecraftRegistry", CLASS_REGISTRY, CLASS_RESOURCE_KEY);
                CREATE_REGISTRY_KEY = MinecraftReflection.searchMethod(CLASS_RESOURCE_KEY, (Integer)9, "createRegistryKey", CLASS_RESOURCE_KEY, CLASS_RESOURCE_LOCATION);
                NEW_RESOURCE_LOCATION = MinecraftReflection.findConstructor(CLASS_RESOURCE_LOCATION, String.class, String.class);
                NEW_BOOK_CONTENT = MinecraftReflection.findConstructor(CLASS_MC_BOOK_CONTENT, CLASS_MC_FILTERABLE, String.class, Integer.TYPE, List.class, Boolean.TYPE);
                REGISTRY_GET_OPTIONAL = MinecraftReflection.searchMethod(CLASS_REGISTRY, (Integer)1, "getOptional", Optional.class, CLASS_RESOURCE_LOCATION);
                CLASS_ENUM_HAND = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("EnumHand"), MinecraftReflection.findMcClassName("world.EnumHand"), MinecraftReflection.findMcClassName("world.InteractionHand"));
                HAND_MAIN = MinecraftReflection.findEnum(CLASS_ENUM_HAND, "MAIN_HAND", 0);
                MC_ITEMSTACK_SET = MinecraftReflection.searchMethod(CLASS_MC_ITEMSTACK, (Integer)1, "set", Object.class, CLASS_MC_DATA_COMPONENT_TYPE, Object.class);
                CRAFT_ITEMSTACK_NMS_COPY = MinecraftReflection.findStaticMethod(CLASS_CRAFT_ITEMSTACK, "asNMSCopy", CLASS_MC_ITEMSTACK, ItemStack.class);
                CRAFT_ITEMSTACK_CRAFT_MIRROR = MinecraftReflection.findStaticMethod(CLASS_CRAFT_ITEMSTACK, "asCraftMirror", CLASS_CRAFT_ITEMSTACK, CLASS_MC_ITEMSTACK);
                PACKET_OPEN_BOOK = MinecraftReflection.findClass(MinecraftReflection.findMcClassName("network.protocol.game.PacketPlayOutOpenBook"), MinecraftReflection.findMcClassName("network.protocol.game.ClientboundOpenBookPacket"));
                NEW_PACKET_OPEN_BOOK = MinecraftReflection.findConstructor(PACKET_OPEN_BOOK, CLASS_ENUM_HAND);
                Object object = null;
                var1_1 = null;
                try {
                    if (GET_REGISTRY == null || CREATE_REGISTRY_KEY == null || NEW_RESOURCE_LOCATION == null || REGISTRY_GET_OPTIONAL == null) break block5;
                    Object object2 = CREATE_REGISTRY_KEY.invoke(NEW_RESOURCE_LOCATION.invoke("minecraft", "data_component_type"));
                    try {
                        object = GET_REGISTRY.invoke(object2);
                    } catch (Exception exception) {
                        // empty catch block
                    }
                    if (object != null) {
                        var1_1 = REGISTRY_GET_OPTIONAL.invoke(object, NEW_RESOURCE_LOCATION.invoke("minecraft", "written_book_content")).orElse(null);
                    }
                } catch (Throwable throwable) {
                    Knob.logError(throwable, "Failed to initialize Book_1_20_5 CraftBukkit facet", new Object[0]);
                }
            }
            WRITTEN_BOOK_COMPONENT_TYPE = var1_1;
        }
    }

    static final class EntitySound_1_19_3 {
        @Nullable
        static final MethodHandle NEW_RESOURCE_LOCATION = MinecraftReflection.findConstructor(CLASS_RESOURCE_LOCATION, String.class, String.class);
        @Nullable
        static final MethodHandle REGISTRY_GET_OPTIONAL = MinecraftReflection.searchMethod(CLASS_REGISTRY, (Integer)1, "getOptional", Optional.class, CLASS_RESOURCE_LOCATION);
        @Nullable
        static final MethodHandle REGISTRY_WRAP_AS_HOLDER = MinecraftReflection.searchMethod(CLASS_REGISTRY, (Integer)1, "wrapAsHolder", CLASS_HOLDER, Object.class);
        @Nullable
        static final MethodHandle SOUND_EVENT_CREATE_VARIABLE_RANGE = MinecraftReflection.searchMethod(EntitySound.CLASS_SOUND_EVENT, (Integer)9, "createVariableRangeEvent", EntitySound.CLASS_SOUND_EVENT, CLASS_RESOURCE_LOCATION);
        @Nullable
        static final MethodHandle NEW_CLIENTBOUND_ENTITY_SOUND = MinecraftReflection.findConstructor(EntitySound.CLASS_CLIENTBOUND_ENTITY_SOUND, CLASS_HOLDER, EntitySound.CLASS_SOUND_SOURCE, CLASS_NMS_ENTITY, Float.TYPE, Float.TYPE, Long.TYPE);
        @Nullable
        static final Object SOUND_EVENT_REGISTRY;

        private EntitySound_1_19_3() {
        }

        static boolean isSupported() {
            return NEW_CLIENTBOUND_ENTITY_SOUND != null && SOUND_EVENT_REGISTRY != null && NEW_RESOURCE_LOCATION != null && REGISTRY_GET_OPTIONAL != null && REGISTRY_WRAP_AS_HOLDER != null && SOUND_EVENT_CREATE_VARIABLE_RANGE != null;
        }

        static {
            Object object = null;
            try {
                Field field = MinecraftReflection.findField(CLASS_BUILT_IN_REGISTRIES, CLASS_REGISTRY, "SOUND_EVENT");
                if (field != null) {
                    object = field.get(null);
                } else if (CLASS_BUILT_IN_REGISTRIES != null && REGISTRY_GET_OPTIONAL != null && NEW_RESOURCE_LOCATION != null) {
                    Object object2 = null;
                    for (Field field2 : CLASS_BUILT_IN_REGISTRIES.getDeclaredFields()) {
                        int n = 26;
                        if ((field2.getModifiers() & 0x1A) != 26 || !field2.getType().equals(CLASS_WRITABLE_REGISTRY)) continue;
                        field2.setAccessible(true);
                        object2 = field2.get(null);
                        break;
                    }
                    if (object2 != null) {
                        object = REGISTRY_GET_OPTIONAL.invoke(object2, NEW_RESOURCE_LOCATION.invoke("minecraft", "sound_event")).orElse(null);
                    }
                }
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to initialize EntitySound_1_19_3 CraftBukkit facet", new Object[0]);
            }
            SOUND_EVENT_REGISTRY = object;
        }
    }

    static final class EntitySound {
        @Nullable
        static final Class<?> CLASS_CLIENTBOUND_ENTITY_SOUND = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("PacketPlayOutEntitySound"), MinecraftReflection.findMcClassName("network.protocol.game.PacketPlayOutEntitySound"), MinecraftReflection.findMcClassName("network.protocol.game.ClientboundSoundEntityPacket"));
        @Nullable
        static final Class<?> CLASS_SOUND_SOURCE = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("SoundCategory"), MinecraftReflection.findMcClassName("sounds.SoundCategory"), MinecraftReflection.findMcClassName("sounds.SoundSource"));
        @Nullable
        static final Class<?> CLASS_SOUND_EVENT = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("SoundEffect"), MinecraftReflection.findMcClassName("sounds.SoundEffect"), MinecraftReflection.findMcClassName("sounds.SoundEvent"));
        @Nullable
        static final MethodHandle SOUND_SOURCE_GET_NAME;

        private EntitySound() {
        }

        static boolean isSupported() {
            return SOUND_SOURCE_GET_NAME != null;
        }

        static {
            MethodHandle methodHandle = null;
            if (CLASS_SOUND_SOURCE != null) {
                for (Method method : CLASS_SOUND_SOURCE.getDeclaredMethods()) {
                    if (!method.getReturnType().equals(String.class) || method.getParameterCount() != 0 || "name".equals(method.getName()) || !Modifier.isPublic(method.getModifiers())) continue;
                    try {
                        methodHandle = MinecraftReflection.lookup().unreflect(method);
                    } catch (IllegalAccessException illegalAccessException) {}
                    break;
                }
            }
            SOUND_SOURCE_GET_NAME = methodHandle;
        }
    }

    static final class Chat1_19_3 {
        @Nullable
        static final MethodHandle NEW_RESOURCE_LOCATION = MinecraftReflection.findConstructor(CLASS_RESOURCE_LOCATION, String.class, String.class);
        @Nullable
        static final MethodHandle RESOURCE_KEY_CREATE = MinecraftReflection.searchMethod(CLASS_RESOURCE_KEY, (Integer)9, "create", CLASS_RESOURCE_KEY, CLASS_RESOURCE_KEY, CLASS_RESOURCE_LOCATION);
        @Nullable
        static final MethodHandle SERVER_PLAYER_GET_LEVEL = MinecraftReflection.searchMethod(CraftBukkitFacet.CRAFT_PLAYER_GET_HANDLE.type().returnType(), (Integer)1, "getLevel", CLASS_SERVER_LEVEL, new Class[0]);
        @Nullable
        static final MethodHandle SERVER_LEVEL_GET_REGISTRY_ACCESS = MinecraftReflection.searchMethod(CLASS_SERVER_LEVEL, (Integer)1, "registryAccess", CLASS_REGISTRY_ACCESS, new Class[0]);
        @Nullable
        static final MethodHandle LEVEL_GET_REGISTRY_ACCESS = MinecraftReflection.searchMethod(CLASS_LEVEL, (Integer)1, "registryAccess", CLASS_REGISTRY_ACCESS, new Class[0]);
        @Nullable
        static final MethodHandle ACTUAL_GET_REGISTRY_ACCESS = SERVER_LEVEL_GET_REGISTRY_ACCESS == null ? LEVEL_GET_REGISTRY_ACCESS : SERVER_LEVEL_GET_REGISTRY_ACCESS;
        @Nullable
        static final MethodHandle REGISTRY_ACCESS_GET_REGISTRY_OPTIONAL = MinecraftReflection.searchMethod(CLASS_REGISTRY_ACCESS, (Integer)1, "registry", Optional.class, CLASS_RESOURCE_KEY);
        @Nullable
        static final MethodHandle REGISTRY_GET_OPTIONAL = MinecraftReflection.searchMethod(CLASS_REGISTRY, (Integer)1, "getOptional", Optional.class, CLASS_RESOURCE_LOCATION);
        @Nullable
        static final MethodHandle REGISTRY_GET_HOLDER = MinecraftReflection.searchMethod(CLASS_REGISTRY, (Integer)1, "getHolder", Optional.class, CLASS_RESOURCE_LOCATION);
        @Nullable
        static final MethodHandle REGISTRY_GET_ID = MinecraftReflection.searchMethod(CLASS_REGISTRY, (Integer)1, "getId", Integer.TYPE, Object.class);
        @Nullable
        static final MethodHandle DISGUISED_CHAT_PACKET_CONSTRUCTOR;
        @Nullable
        static final MethodHandle CHAT_TYPE_BOUND_NETWORK_CONSTRUCTOR;
        @Nullable
        static final MethodHandle CHAT_TYPE_BOUND_CONSTRUCTOR;
        static final Object CHAT_TYPE_RESOURCE_KEY;

        private Chat1_19_3() {
        }

        static boolean isSupported() {
            return ACTUAL_GET_REGISTRY_ACCESS != null && REGISTRY_ACCESS_GET_REGISTRY_OPTIONAL != null && REGISTRY_GET_OPTIONAL != null && (CHAT_TYPE_BOUND_NETWORK_CONSTRUCTOR != null || CHAT_TYPE_BOUND_CONSTRUCTOR != null) && DISGUISED_CHAT_PACKET_CONSTRUCTOR != null && CHAT_TYPE_RESOURCE_KEY != null;
        }

        static {
            MethodHandle methodHandle = null;
            MethodHandle methodHandle2 = null;
            MethodHandle methodHandle3 = null;
            Object object = null;
            try {
                MethodHandle methodHandle4;
                Object object2;
                Class<?> clazz;
                Object object3 = MinecraftReflection.findClass(MinecraftReflection.findMcClassName("network.chat.ChatType$BoundNetwork"));
                if (object3 == null && (clazz = MinecraftReflection.findClass(MinecraftReflection.findMcClassName("network.chat.ChatMessageType"))) != null) {
                    object2 = clazz.getClasses();
                    int n = ((Class<?>[])object2).length;
                    for (int i = 0; i < n; ++i) {
                        Object object4 = object2[i];
                        methodHandle = MinecraftReflection.findConstructor(object4, new Class[]{Integer.TYPE, CLASS_CHAT_COMPONENT, CLASS_CHAT_COMPONENT});
                        if (methodHandle == null) continue;
                        object3 = object4;
                        break;
                    }
                }
                if ((clazz = MinecraftReflection.findClass(MinecraftReflection.findMcClassName("network.chat.ChatType$BoundNetwork"))) == null && (object2 = MinecraftReflection.findClass(MinecraftReflection.findMcClassName("network.chat.ChatMessageType"))) != null) {
                    for (Class<?> clazz2 : ((Class)object2).getClasses()) {
                        methodHandle2 = MinecraftReflection.findConstructor(clazz2, CLASS_HOLDER, CLASS_CHAT_COMPONENT, Optional.class);
                        if (methodHandle2 == null) continue;
                        clazz = clazz2;
                        break;
                    }
                }
                if ((object2 = MinecraftReflection.findClass(MinecraftReflection.findMcClassName("network.protocol.game.ClientboundDisguisedChatPacket"))) != null) {
                    if (object3 != null) {
                        methodHandle3 = MinecraftReflection.findConstructor(object2, new Class[]{CLASS_CHAT_COMPONENT, object3});
                    } else if (clazz != null) {
                        methodHandle3 = MinecraftReflection.findConstructor(object2, new Class[]{CLASS_CHAT_COMPONENT, clazz});
                    }
                }
                if (NEW_RESOURCE_LOCATION != null && RESOURCE_KEY_CREATE != null && (methodHandle4 = MinecraftReflection.searchMethod(CLASS_RESOURCE_KEY, (Integer)9, "createRegistryKey", CLASS_RESOURCE_KEY, CLASS_RESOURCE_LOCATION)) != null) {
                    object = methodHandle4.invoke(NEW_RESOURCE_LOCATION.invoke("minecraft", "chat_type"));
                }
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to initialize 1.19.3 chat support", new Object[0]);
            }
            DISGUISED_CHAT_PACKET_CONSTRUCTOR = methodHandle3;
            CHAT_TYPE_BOUND_NETWORK_CONSTRUCTOR = methodHandle;
            CHAT_TYPE_BOUND_CONSTRUCTOR = methodHandle2;
            CHAT_TYPE_RESOURCE_KEY = object;
        }
    }
}

