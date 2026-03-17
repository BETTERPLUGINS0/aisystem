/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  io.netty.buffer.Unpooled
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.World
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Damageable
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Wither
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.plugin.Plugin
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.platform.bukkit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.chat.ChatType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.nbt.BinaryTagIO;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.nbt.StringBinaryTag;
import net.kyori.adventure.platform.bukkit.BukkitAudience;
import net.kyori.adventure.platform.bukkit.BukkitComponentSerializer;
import net.kyori.adventure.platform.bukkit.BukkitEmitter;
import net.kyori.adventure.platform.bukkit.BukkitFacet;
import net.kyori.adventure.platform.bukkit.CraftBukkitAccess;
import net.kyori.adventure.platform.bukkit.MinecraftComponentSerializer;
import net.kyori.adventure.platform.bukkit.MinecraftReflection;
import net.kyori.adventure.platform.bukkit.PaperFacet;
import net.kyori.adventure.platform.facet.Facet;
import net.kyori.adventure.platform.facet.FacetBase;
import net.kyori.adventure.platform.facet.FacetComponentFlattener;
import net.kyori.adventure.platform.facet.Knob;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class CraftBukkitFacet<V extends CommandSender>
extends FacetBase<V> {
    private static final Class<?> CLASS_NMS_ENTITY = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("Entity"), MinecraftReflection.findMcClassName("world.entity.Entity"));
    private static final Class<?> CLASS_CRAFT_ENTITY = MinecraftReflection.findCraftClass("entity.CraftEntity");
    private static final MethodHandle CRAFT_ENTITY_GET_HANDLE = MinecraftReflection.findMethod(CLASS_CRAFT_ENTITY, "getHandle", CLASS_NMS_ENTITY, new Class[0]);
    @Nullable
    static final Class<? extends Player> CLASS_CRAFT_PLAYER = MinecraftReflection.findCraftClass("entity.CraftPlayer", Player.class);
    @Nullable
    static final MethodHandle CRAFT_PLAYER_GET_HANDLE;
    @Nullable
    private static final MethodHandle ENTITY_PLAYER_GET_CONNECTION;
    @Nullable
    private static final MethodHandle PLAYER_CONNECTION_SEND_PACKET;
    private static final boolean SUPPORTED;
    @Nullable
    private static final Class<?> CLASS_CHAT_COMPONENT;
    @Nullable
    private static final Class<?> CLASS_MESSAGE_TYPE;
    @Nullable
    private static final Object MESSAGE_TYPE_CHAT;
    @Nullable
    private static final Object MESSAGE_TYPE_SYSTEM;
    @Nullable
    private static final Object MESSAGE_TYPE_ACTIONBAR;
    @Nullable
    private static final MethodHandle LEGACY_CHAT_PACKET_CONSTRUCTOR;
    @Nullable
    private static final MethodHandle CHAT_PACKET_CONSTRUCTOR;
    @Nullable
    private static final Class<?> CLASS_TITLE_PACKET;
    @Nullable
    private static final Class<?> CLASS_TITLE_ACTION;
    private static final MethodHandle CONSTRUCTOR_TITLE_MESSAGE;
    @Nullable
    private static final MethodHandle CONSTRUCTOR_TITLE_TIMES;
    @Nullable
    private static final Object TITLE_ACTION_TITLE;
    @Nullable
    private static final Object TITLE_ACTION_SUBTITLE;
    @Nullable
    private static final Object TITLE_ACTION_ACTIONBAR;
    @Nullable
    private static final Object TITLE_ACTION_CLEAR;
    @Nullable
    private static final Object TITLE_ACTION_RESET;

    protected CraftBukkitFacet(@Nullable Class<? extends V> clazz) {
        super(clazz);
    }

    @Override
    public boolean isSupported() {
        return super.isSupported() && SUPPORTED;
    }

    static {
        Object object = MinecraftReflection.findCraftClass("entity.CraftPlayer");
        Object object2 = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("Packet"), MinecraftReflection.findMcClassName("network.protocol.Packet"));
        Object object3 = null;
        MethodHandle methodHandle = null;
        MethodHandle methodHandle2 = null;
        if (object != null && object2 != null) {
            try {
                Class<?> clazz;
                Method method = ((Class)object).getMethod("getHandle", new Class[0]);
                Class<?> clazz2 = method.getReturnType();
                object3 = MinecraftReflection.lookup().unreflect(method);
                Field field = MinecraftReflection.findField(clazz2, "playerConnection", "connection");
                Class<?> clazz3 = null;
                if (field != null) {
                    methodHandle = MinecraftReflection.lookup().unreflectGetter(field);
                    clazz3 = field.getType();
                } else {
                    clazz = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("PlayerConnection"), MinecraftReflection.findMcClassName("server.network.PlayerConnection"), MinecraftReflection.findMcClassName("server.network.ServerGamePacketListenerImpl"));
                    for (Field field2 : clazz2.getDeclaredFields()) {
                        int n = field2.getModifiers();
                        if (!Modifier.isPublic(n) || Modifier.isFinal(n) || clazz != null && !field2.getType().equals(clazz)) continue;
                        methodHandle = MinecraftReflection.lookup().unreflectGetter(field2);
                        clazz3 = field2.getType();
                    }
                }
                clazz = MinecraftReflection.findClass(MinecraftReflection.findMcClassName("server.network.ServerCommonPacketListenerImpl"));
                if (clazz != null) {
                    clazz3 = clazz;
                }
                methodHandle2 = MinecraftReflection.searchMethod(clazz3, (Integer)1, new String[]{"sendPacket", "send"}, Void.TYPE, new Class[]{object2});
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to initialize CraftBukkit sendPacket", new Object[0]);
            }
        }
        CRAFT_PLAYER_GET_HANDLE = object3;
        ENTITY_PLAYER_GET_CONNECTION = methodHandle;
        PLAYER_CONNECTION_SEND_PACKET = methodHandle2;
        SUPPORTED = Knob.isEnabled("craftbukkit", true) && MinecraftComponentSerializer.isSupported() && CRAFT_PLAYER_GET_HANDLE != null && ENTITY_PLAYER_GET_CONNECTION != null && PLAYER_CONNECTION_SEND_PACKET != null;
        CLASS_CHAT_COMPONENT = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("IChatBaseComponent"), MinecraftReflection.findMcClassName("network.chat.IChatBaseComponent"), MinecraftReflection.findMcClassName("network.chat.Component"));
        CLASS_MESSAGE_TYPE = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("ChatMessageType"), MinecraftReflection.findMcClassName("network.chat.ChatMessageType"), MinecraftReflection.findMcClassName("network.chat.ChatType"));
        if (CLASS_MESSAGE_TYPE != null && !CLASS_MESSAGE_TYPE.isEnum()) {
            MESSAGE_TYPE_CHAT = 0;
            MESSAGE_TYPE_SYSTEM = 1;
            MESSAGE_TYPE_ACTIONBAR = 2;
        } else {
            MESSAGE_TYPE_CHAT = MinecraftReflection.findEnum(CLASS_MESSAGE_TYPE, "CHAT", 0);
            MESSAGE_TYPE_SYSTEM = MinecraftReflection.findEnum(CLASS_MESSAGE_TYPE, "SYSTEM", 1);
            MESSAGE_TYPE_ACTIONBAR = MinecraftReflection.findEnum(CLASS_MESSAGE_TYPE, "GAME_INFO", 2);
        }
        object = null;
        object2 = null;
        try {
            if (CLASS_CHAT_COMPONENT != null) {
                object3 = MinecraftReflection.needClass(MinecraftReflection.findNmsClassName("PacketPlayOutChat"), MinecraftReflection.findMcClassName("network.protocol.game.PacketPlayOutChat"), MinecraftReflection.findMcClassName("network.protocol.game.ClientboundChatPacket"), MinecraftReflection.findMcClassName("network.protocol.game.ClientboundSystemChatPacket"));
                if (MESSAGE_TYPE_CHAT == Integer.valueOf(0)) {
                    object2 = MinecraftReflection.findConstructor(object3, new Class[]{CLASS_CHAT_COMPONENT, Boolean.TYPE});
                }
                if (object2 == null) {
                    object2 = MinecraftReflection.findConstructor(object3, new Class[]{CLASS_CHAT_COMPONENT, Integer.TYPE});
                }
                if (object2 == null) {
                    object2 = MinecraftReflection.findConstructor(object3, new Class[]{CLASS_CHAT_COMPONENT});
                }
                if (object2 == null) {
                    if (CLASS_MESSAGE_TYPE != null) {
                        object2 = MinecraftReflection.findConstructor(object3, new Class[]{CLASS_CHAT_COMPONENT, CLASS_MESSAGE_TYPE, UUID.class});
                    }
                } else if (MESSAGE_TYPE_CHAT == Integer.valueOf(0)) {
                    if (((MethodHandle)object2).type().parameterType(1).equals(Boolean.TYPE)) {
                        object2 = MethodHandles.insertArguments((MethodHandle)object2, 1, Boolean.FALSE);
                        object2 = MethodHandles.dropArguments((MethodHandle)object2, 1, Integer.class, UUID.class);
                    } else {
                        object2 = MethodHandles.dropArguments((MethodHandle)object2, 2, UUID.class);
                    }
                } else {
                    object2 = MethodHandles.dropArguments(object2, 1, new Class[]{CLASS_MESSAGE_TYPE == null ? Object.class : CLASS_MESSAGE_TYPE, UUID.class});
                }
                if ((object = MinecraftReflection.findConstructor(object3, new Class[]{CLASS_CHAT_COMPONENT, Byte.TYPE})) == null) {
                    object = MinecraftReflection.findConstructor(object3, new Class[]{CLASS_CHAT_COMPONENT, Integer.TYPE});
                }
            }
        } catch (Throwable throwable) {
            Knob.logError(throwable, "Failed to initialize ClientboundChatPacket constructor", new Object[0]);
        }
        CHAT_PACKET_CONSTRUCTOR = object2;
        LEGACY_CHAT_PACKET_CONSTRUCTOR = object;
        CLASS_TITLE_PACKET = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("PacketPlayOutTitle"), MinecraftReflection.findMcClassName("network.protocol.game.PacketPlayOutTitle"));
        CLASS_TITLE_ACTION = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("PacketPlayOutTitle$EnumTitleAction"), MinecraftReflection.findMcClassName("network.protocol.game.PacketPlayOutTitle$EnumTitleAction"));
        CONSTRUCTOR_TITLE_MESSAGE = MinecraftReflection.findConstructor(CLASS_TITLE_PACKET, CLASS_TITLE_ACTION, CLASS_CHAT_COMPONENT);
        CONSTRUCTOR_TITLE_TIMES = MinecraftReflection.findConstructor(CLASS_TITLE_PACKET, Integer.TYPE, Integer.TYPE, Integer.TYPE);
        TITLE_ACTION_TITLE = MinecraftReflection.findEnum(CLASS_TITLE_ACTION, "TITLE", 0);
        TITLE_ACTION_SUBTITLE = MinecraftReflection.findEnum(CLASS_TITLE_ACTION, "SUBTITLE", 1);
        TITLE_ACTION_ACTIONBAR = MinecraftReflection.findEnum(CLASS_TITLE_ACTION, "ACTIONBAR");
        TITLE_ACTION_CLEAR = MinecraftReflection.findEnum(CLASS_TITLE_ACTION, "CLEAR");
        TITLE_ACTION_RESET = MinecraftReflection.findEnum(CLASS_TITLE_ACTION, "RESET");
    }

    static final class Translator
    extends FacetBase<Server>
    implements FacetComponentFlattener.Translator<Server> {
        private static final Class<?> CLASS_LANGUAGE = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("LocaleLanguage"), MinecraftReflection.findMcClassName("locale.LocaleLanguage"), MinecraftReflection.findMcClassName("locale.Language"));
        private static final MethodHandle LANGUAGE_GET_INSTANCE;
        private static final MethodHandle LANGUAGE_GET_OR_DEFAULT;

        private static MethodHandle unreflectUnchecked(Method method) {
            try {
                method.setAccessible(true);
                return MinecraftReflection.lookup().unreflect(method);
            } catch (IllegalAccessException illegalAccessException) {
                return null;
            }
        }

        Translator() {
            super(Server.class);
        }

        @Override
        public boolean isSupported() {
            return super.isSupported() && LANGUAGE_GET_INSTANCE != null && LANGUAGE_GET_OR_DEFAULT != null;
        }

        @Override
        @NotNull
        public String valueOrDefault(@NotNull Server server, @NotNull String string) {
            try {
                return LANGUAGE_GET_OR_DEFAULT.invoke(LANGUAGE_GET_INSTANCE.invoke(), string);
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to transate key '%s'", string);
                return string;
            }
        }

        static {
            if (CLASS_LANGUAGE == null) {
                LANGUAGE_GET_INSTANCE = null;
                LANGUAGE_GET_OR_DEFAULT = null;
            } else {
                LANGUAGE_GET_INSTANCE = Arrays.stream(CLASS_LANGUAGE.getDeclaredMethods()).filter(method -> Modifier.isStatic(method.getModifiers()) && !Modifier.isPrivate(method.getModifiers()) && method.getReturnType().equals(CLASS_LANGUAGE) && method.getParameterCount() == 0).findFirst().map(Translator::unreflectUnchecked).orElse(null);
                LANGUAGE_GET_OR_DEFAULT = Arrays.stream(CLASS_LANGUAGE.getDeclaredMethods()).filter(method -> !Modifier.isStatic(method.getModifiers()) && Modifier.isPublic(method.getModifiers()) && method.getParameterCount() == 1 && method.getParameterTypes()[0] == String.class && method.getReturnType().equals(String.class)).findFirst().map(Translator::unreflectUnchecked).orElse(null);
            }
        }
    }

    static class TabList
    extends PacketFacet<Player>
    implements Facet.TabList<Player, Object> {
        private static final Class<?> CLIENTBOUND_TAB_LIST_PACKET = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("PacketPlayOutPlayerListHeaderFooter"), MinecraftReflection.findMcClassName("network.protocol.game.PacketPlayOutPlayerListHeaderFooter"), MinecraftReflection.findMcClassName("network.protocol.game.ClientboundTabListPacket"));
        @Nullable
        private static final MethodHandle CLIENTBOUND_TAB_LIST_PACKET_CTOR_PRE_1_17 = MinecraftReflection.findConstructor(CLIENTBOUND_TAB_LIST_PACKET, new Class[0]);
        @Nullable
        protected static final MethodHandle CLIENTBOUND_TAB_LIST_PACKET_CTOR = MinecraftReflection.findConstructor(CLIENTBOUND_TAB_LIST_PACKET, CraftBukkitFacet.access$500(), CraftBukkitFacet.access$500());
        @Nullable
        private static final Field CRAFT_PLAYER_TAB_LIST_HEADER = MinecraftReflection.findField(CLASS_CRAFT_PLAYER, "playerListHeader");
        @Nullable
        private static final Field CRAFT_PLAYER_TAB_LIST_FOOTER = MinecraftReflection.findField(CLASS_CRAFT_PLAYER, "playerListFooter");
        @Nullable
        protected static final MethodHandle CLIENTBOUND_TAB_LIST_PACKET_SET_HEADER = TabList.first(MinecraftReflection.findSetterOf(MinecraftReflection.findField(CLIENTBOUND_TAB_LIST_PACKET, PaperFacet.NATIVE_COMPONENT_CLASS, "adventure$header")), MinecraftReflection.findSetterOf(MinecraftReflection.findField(CLIENTBOUND_TAB_LIST_PACKET, CraftBukkitFacet.access$500(), "header", "a")));
        @Nullable
        protected static final MethodHandle CLIENTBOUND_TAB_LIST_PACKET_SET_FOOTER = TabList.first(MinecraftReflection.findSetterOf(MinecraftReflection.findField(CLIENTBOUND_TAB_LIST_PACKET, PaperFacet.NATIVE_COMPONENT_CLASS, "adventure$footer")), MinecraftReflection.findSetterOf(MinecraftReflection.findField(CLIENTBOUND_TAB_LIST_PACKET, CraftBukkitFacet.access$500(), "footer", "b")));

        TabList() {
        }

        private static MethodHandle first(MethodHandle ... methodHandleArray) {
            for (int i = 0; i < methodHandleArray.length; ++i) {
                MethodHandle methodHandle = methodHandleArray[i];
                if (methodHandle == null) continue;
                return methodHandle;
            }
            return null;
        }

        @Override
        public boolean isSupported() {
            return (CLIENTBOUND_TAB_LIST_PACKET_CTOR != null || CLIENTBOUND_TAB_LIST_PACKET_CTOR_PRE_1_17 != null && CLIENTBOUND_TAB_LIST_PACKET_SET_HEADER != null && CLIENTBOUND_TAB_LIST_PACKET_SET_FOOTER != null) && super.isSupported();
        }

        protected Object create117Packet(Player player, @Nullable Object object, @Nullable Object object2) {
            return CLIENTBOUND_TAB_LIST_PACKET_CTOR.invoke(object == null ? this.createMessage(player, (Component)Component.empty()) : object, object2 == null ? this.createMessage(player, (Component)Component.empty()) : object2);
        }

        @Override
        public void send(Player player, @Nullable Object object, @Nullable Object object2) {
            try {
                Object object3;
                if (CRAFT_PLAYER_TAB_LIST_HEADER != null && CRAFT_PLAYER_TAB_LIST_FOOTER != null) {
                    if (object == null) {
                        object = CRAFT_PLAYER_TAB_LIST_HEADER.get(player);
                    } else {
                        CRAFT_PLAYER_TAB_LIST_HEADER.set(player, object);
                    }
                    if (object2 == null) {
                        object2 = CRAFT_PLAYER_TAB_LIST_FOOTER.get(player);
                    } else {
                        CRAFT_PLAYER_TAB_LIST_FOOTER.set(player, object2);
                    }
                }
                if (CLIENTBOUND_TAB_LIST_PACKET_CTOR != null) {
                    object3 = this.create117Packet(player, object, object2);
                } else {
                    object3 = CLIENTBOUND_TAB_LIST_PACKET_CTOR_PRE_1_17.invoke();
                    CLIENTBOUND_TAB_LIST_PACKET_SET_HEADER.invoke(object3, object == null ? this.createMessage(player, (Component)Component.empty()) : object);
                    CLIENTBOUND_TAB_LIST_PACKET_SET_FOOTER.invoke(object3, object2 == null ? this.createMessage(player, (Component)Component.empty()) : object2);
                }
                this.sendPacket(player, object3);
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to send tab list header and footer to %s", player);
            }
        }
    }

    static final class BossBarWither
    extends FakeEntity<Wither>
    implements Facet.BossBarEntity<Player, Location> {
        private volatile boolean initialized = false;

        private BossBarWither(@NotNull Collection<Player> collection) {
            super(Wither.class, collection.iterator().next().getWorld().getSpawnLocation());
            this.invisible(true);
            this.metadata(20, 890);
        }

        @Override
        public void bossBarInitialized(@NotNull net.kyori.adventure.bossbar.BossBar bossBar) {
            Facet.BossBarEntity.super.bossBarInitialized(bossBar);
            this.initialized = true;
        }

        @Override
        @NotNull
        public Location createPosition(@NotNull Player player) {
            Location location = super.createPosition(player);
            location.setPitch(location.getPitch() - 30.0f);
            location.setYaw(location.getYaw() + 0.0f);
            location.add(location.getDirection().multiply(40));
            return location;
        }

        @Override
        public boolean isEmpty() {
            return !this.initialized || this.viewers.isEmpty();
        }

        public static class Builder
        extends CraftBukkitFacet<Player>
        implements Facet.BossBar.Builder<Player, BossBarWither> {
            protected Builder() {
                super(Player.class);
            }

            @Override
            @NotNull
            public BossBarWither createBossBar(@NotNull Collection<Player> collection) {
                return new BossBarWither(collection);
            }
        }
    }

    static class FakeEntity<E extends Entity>
    extends PacketFacet<Player>
    implements Facet.FakeEntity<Player, Location>,
    Listener {
        private static final Class<? extends World> CLASS_CRAFT_WORLD = MinecraftReflection.findCraftClass("CraftWorld", World.class);
        private static final Class<?> CLASS_NMS_LIVING_ENTITY = MinecraftReflection.findNmsClass("EntityLiving");
        private static final Class<?> CLASS_DATA_WATCHER = MinecraftReflection.findNmsClass("DataWatcher");
        private static final MethodHandle CRAFT_WORLD_CREATE_ENTITY = MinecraftReflection.findMethod(CLASS_CRAFT_WORLD, "createEntity", CraftBukkitFacet.access$1100(), Location.class, Class.class);
        private static final MethodHandle NMS_ENTITY_GET_BUKKIT_ENTITY = MinecraftReflection.findMethod(CraftBukkitFacet.access$1100(), "getBukkitEntity", CraftBukkitFacet.access$900(), new Class[0]);
        private static final MethodHandle NMS_ENTITY_GET_DATA_WATCHER = MinecraftReflection.findMethod(CraftBukkitFacet.access$1100(), "getDataWatcher", CLASS_DATA_WATCHER, new Class[0]);
        private static final MethodHandle NMS_ENTITY_SET_LOCATION = MinecraftReflection.findMethod(CraftBukkitFacet.access$1100(), "setLocation", Void.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE);
        private static final MethodHandle NMS_ENTITY_SET_INVISIBLE = MinecraftReflection.findMethod(CraftBukkitFacet.access$1100(), "setInvisible", Void.TYPE, Boolean.TYPE);
        private static final MethodHandle DATA_WATCHER_WATCH = MinecraftReflection.findMethod(CLASS_DATA_WATCHER, "watch", Void.TYPE, Integer.TYPE, Object.class);
        private static final Class<?> CLASS_SPAWN_LIVING_PACKET = MinecraftReflection.findNmsClass("PacketPlayOutSpawnEntityLiving");
        private static final MethodHandle NEW_SPAWN_LIVING_PACKET = MinecraftReflection.findConstructor(CLASS_SPAWN_LIVING_PACKET, CLASS_NMS_LIVING_ENTITY);
        private static final Class<?> CLASS_ENTITY_DESTROY_PACKET = MinecraftReflection.findNmsClass("PacketPlayOutEntityDestroy");
        private static final MethodHandle NEW_ENTITY_DESTROY_PACKET = MinecraftReflection.findConstructor(CLASS_ENTITY_DESTROY_PACKET, int[].class);
        private static final Class<?> CLASS_ENTITY_METADATA_PACKET = MinecraftReflection.findNmsClass("PacketPlayOutEntityMetadata");
        private static final MethodHandle NEW_ENTITY_METADATA_PACKET = MinecraftReflection.findConstructor(CLASS_ENTITY_METADATA_PACKET, Integer.TYPE, CLASS_DATA_WATCHER, Boolean.TYPE);
        private static final Class<?> CLASS_ENTITY_TELEPORT_PACKET = MinecraftReflection.findNmsClass("PacketPlayOutEntityTeleport");
        private static final MethodHandle NEW_ENTITY_TELEPORT_PACKET = MinecraftReflection.findConstructor(CLASS_ENTITY_TELEPORT_PACKET, CraftBukkitFacet.access$1100());
        private static final Class<?> CLASS_ENTITY_WITHER = MinecraftReflection.findNmsClass("EntityWither");
        private static final Class<?> CLASS_WORLD = MinecraftReflection.findNmsClass("World");
        private static final Class<?> CLASS_WORLD_SERVER = MinecraftReflection.findNmsClass("WorldServer");
        private static final MethodHandle CRAFT_WORLD_GET_HANDLE = MinecraftReflection.findMethod(CLASS_CRAFT_WORLD, "getHandle", CLASS_WORLD_SERVER, new Class[0]);
        private static final MethodHandle NEW_ENTITY_WITHER = MinecraftReflection.findConstructor(CLASS_ENTITY_WITHER, CLASS_WORLD);
        private static final boolean SUPPORTED = (CRAFT_WORLD_CREATE_ENTITY != null || NEW_ENTITY_WITHER != null && CRAFT_WORLD_GET_HANDLE != null) && CraftBukkitFacet.access$1000() != null && NMS_ENTITY_GET_BUKKIT_ENTITY != null && NMS_ENTITY_GET_DATA_WATCHER != null;
        private final E entity;
        private final Object entityHandle;
        protected final Set<Player> viewers;

        protected FakeEntity(@NotNull Class<E> clazz, @NotNull Location location) {
            this(BukkitAudience.PLUGIN.get(), clazz, location);
        }

        protected FakeEntity(@NotNull Plugin plugin, @NotNull Class<E> clazz, @NotNull Location location) {
            Entity entity = null;
            Object object = null;
            if (SUPPORTED) {
                try {
                    if (CRAFT_WORLD_CREATE_ENTITY != null) {
                        Object object2 = CRAFT_WORLD_CREATE_ENTITY.invoke(location.getWorld(), location, clazz);
                        entity = NMS_ENTITY_GET_BUKKIT_ENTITY.invoke(object2);
                    } else if (Wither.class.isAssignableFrom(clazz) && NEW_ENTITY_WITHER != null) {
                        Object object3 = NEW_ENTITY_WITHER.invoke(CRAFT_WORLD_GET_HANDLE.invoke(location.getWorld()));
                        entity = NMS_ENTITY_GET_BUKKIT_ENTITY.invoke(object3);
                    }
                    if (CLASS_CRAFT_ENTITY.isInstance(entity)) {
                        object = CRAFT_ENTITY_GET_HANDLE.invoke(entity);
                    }
                } catch (Throwable throwable) {
                    Knob.logError(throwable, "Failed to create fake entity: %s", clazz.getSimpleName());
                }
            }
            this.entity = entity;
            this.entityHandle = object;
            this.viewers = new HashSet<Player>();
            if (this.isSupported()) {
                plugin.getServer().getPluginManager().registerEvents((Listener)this, plugin);
            }
        }

        @Override
        public boolean isSupported() {
            return super.isSupported() && this.entity != null && this.entityHandle != null;
        }

        @EventHandler(ignoreCancelled=false, priority=EventPriority.MONITOR)
        public void onPlayerMove(PlayerMoveEvent playerMoveEvent) {
            Player player = playerMoveEvent.getPlayer();
            if (this.viewers.contains(player)) {
                this.teleport(player, this.createPosition(player));
            }
        }

        @Nullable
        public Object createSpawnPacket() {
            if (this.entity instanceof LivingEntity) {
                try {
                    return NEW_SPAWN_LIVING_PACKET.invoke(this.entityHandle);
                } catch (Throwable throwable) {
                    Knob.logError(throwable, "Failed to create spawn packet: %s", this.entity);
                }
            }
            return null;
        }

        @Nullable
        public Object createDespawnPacket() {
            try {
                return NEW_ENTITY_DESTROY_PACKET.invoke(this.entity.getEntityId());
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to create despawn packet: %s", this.entity);
                return null;
            }
        }

        @Nullable
        public Object createMetadataPacket() {
            try {
                Object object = NMS_ENTITY_GET_DATA_WATCHER.invoke(this.entityHandle);
                return NEW_ENTITY_METADATA_PACKET.invoke(this.entity.getEntityId(), object, false);
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to create update metadata packet: %s", this.entity);
                return null;
            }
        }

        @Nullable
        public Object createLocationPacket() {
            try {
                return NEW_ENTITY_TELEPORT_PACKET.invoke(this.entityHandle);
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to create teleport packet: %s", this.entity);
                return null;
            }
        }

        public void broadcastPacket(@Nullable Object object) {
            for (Player player : this.viewers) {
                this.sendPacket(player, object);
            }
        }

        @Override
        @NotNull
        public Location createPosition(@NotNull Player player) {
            return player.getLocation();
        }

        @Override
        @NotNull
        public Location createPosition(double d, double d2, double d3) {
            return new Location(null, d, d2, d3);
        }

        @Override
        public void teleport(@NotNull Player player, @Nullable Location location) {
            if (location == null) {
                this.viewers.remove(player);
                this.sendPacket(player, this.createDespawnPacket());
                return;
            }
            if (!this.viewers.contains(player)) {
                this.sendPacket(player, this.createSpawnPacket());
                this.viewers.add(player);
            }
            try {
                NMS_ENTITY_SET_LOCATION.invoke(this.entityHandle, location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw());
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to set entity location: %s %s", this.entity, location);
            }
            this.sendPacket(player, this.createLocationPacket());
        }

        @Override
        public void metadata(int n, @NotNull Object object) {
            if (DATA_WATCHER_WATCH != null) {
                try {
                    Object object2 = NMS_ENTITY_GET_DATA_WATCHER.invoke(this.entityHandle);
                    DATA_WATCHER_WATCH.invoke(object2, n, object);
                } catch (Throwable throwable) {
                    Knob.logError(throwable, "Failed to set entity metadata: %s %s=%s", this.entity, n, object);
                }
                this.broadcastPacket(this.createMetadataPacket());
            }
        }

        @Override
        public void invisible(boolean bl) {
            if (NMS_ENTITY_SET_INVISIBLE != null) {
                try {
                    NMS_ENTITY_SET_INVISIBLE.invoke(this.entityHandle, bl);
                } catch (Throwable throwable) {
                    Knob.logError(throwable, "Failed to change entity visibility: %s", this.entity);
                }
            }
        }

        @Override
        @Deprecated
        public void health(float f) {
            if (this.entity instanceof Damageable) {
                Damageable damageable = (Damageable)this.entity;
                damageable.setHealth((double)f * (damageable.getMaxHealth() - (double)0.1f) + (double)0.1f);
                this.broadcastPacket(this.createMetadataPacket());
            }
        }

        @Override
        public void name(@NotNull Component component) {
            this.entity.setCustomName(BukkitComponentSerializer.legacy().serialize(component));
            this.broadcastPacket(this.createMetadataPacket());
        }

        @Override
        public void close() {
            HandlerList.unregisterAll((Listener)this);
            for (Player player : new LinkedList<Player>(this.viewers)) {
                this.teleport(player, null);
            }
        }
    }

    static final class BossBar
    extends BukkitFacet.BossBar {
        private static final Class<?> CLASS_CRAFT_BOSS_BAR;
        private static final Class<?> CLASS_BOSS_BAR_ACTION;
        private static final Object BOSS_BAR_ACTION_TITLE;
        private static final MethodHandle CRAFT_BOSS_BAR_HANDLE;
        private static final MethodHandle NMS_BOSS_BATTLE_SET_NAME;
        private static final MethodHandle NMS_BOSS_BATTLE_SEND_UPDATE;

        private BossBar(@NotNull Collection<Player> collection) {
            super(collection);
        }

        @Override
        public void bossBarNameChanged(@NotNull net.kyori.adventure.bossbar.BossBar bossBar, @NotNull Component component, @NotNull Component component2) {
            try {
                Object object = CRAFT_BOSS_BAR_HANDLE.invoke(this.bar);
                Object object2 = MinecraftComponentSerializer.get().serialize(component2);
                NMS_BOSS_BATTLE_SET_NAME.invoke(object, object2);
                NMS_BOSS_BATTLE_SEND_UPDATE.invoke(object, BOSS_BAR_ACTION_TITLE);
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to set CraftBossBar name: %s %s", this.bar, component2);
                super.bossBarNameChanged(bossBar, component, component2);
            }
        }

        static {
            Object object;
            Object object2;
            Object object3;
            Object object4;
            Object object5;
            CLASS_CRAFT_BOSS_BAR = MinecraftReflection.findCraftClass("boss.CraftBossBar");
            Class<Object> clazz = null;
            Object object6 = null;
            clazz = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("PacketPlayOutBoss$Action"), MinecraftReflection.findMcClassName("network.protocol.game.PacketPlayOutBoss$Action"), MinecraftReflection.findMcClassName("network.protocol.game.ClientboundBossEventPacket$Operation"));
            if (clazz == null || !clazz.isEnum()) {
                clazz = null;
                object5 = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("PacketPlayOutBoss"), MinecraftReflection.findMcClassName("network.protocol.game.PacketPlayOutBoss"), MinecraftReflection.findMcClassName("network.protocol.game.ClientboundBossEventPacket"));
                object4 = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("BossBattle"), MinecraftReflection.findMcClassName("world.BossBattle"), MinecraftReflection.findMcClassName("world.BossEvent"));
                if (object5 != null && object4 != null) {
                    try {
                        object3 = MethodType.methodType(object5, object4);
                        try {
                            ((Class)object5).getDeclaredMethod("createUpdateNamePacket", new Class[]{object4});
                            object2 = "createUpdateNamePacket";
                        } catch (NoSuchMethodException noSuchMethodException) {
                            object2 = "c";
                        }
                        object = MinecraftReflection.lookup().findStatic((Class<?>)object5, (String)object2, (MethodType)object3);
                        object6 = LambdaMetafactory.metafactory(MinecraftReflection.lookup(), "apply", MethodType.methodType(Function.class), ((MethodType)object3).generic(), (MethodHandle)object, (MethodType)object3).getTarget().invoke();
                        clazz = Function.class;
                    } catch (Throwable throwable) {
                        Knob.logError(throwable, "Failed to initialize CraftBossBar constructor", new Object[0]);
                    }
                }
            } else {
                object6 = MinecraftReflection.findEnum(clazz, "UPDATE_NAME", 3);
            }
            CLASS_BOSS_BAR_ACTION = clazz;
            BOSS_BAR_ACTION_TITLE = object6;
            object5 = null;
            object4 = null;
            object3 = null;
            if (CLASS_CRAFT_BOSS_BAR != null && CLASS_CHAT_COMPONENT != null && BOSS_BAR_ACTION_TITLE != null) {
                try {
                    object2 = MinecraftReflection.needField(CLASS_CRAFT_BOSS_BAR, "handle");
                    object5 = MinecraftReflection.lookup().unreflectGetter((Field)object2);
                    object = ((Field)object2).getType();
                    for (Field field : ((Class)object).getFields()) {
                        if (!field.getType().equals(CLASS_CHAT_COMPONENT)) continue;
                        object4 = MinecraftReflection.lookup().unreflectSetter(field);
                        break;
                    }
                    object3 = MinecraftReflection.findMethod(object, new String[]{"sendUpdate", "a", "broadcast"}, Void.TYPE, new Class[]{CLASS_BOSS_BAR_ACTION});
                } catch (Throwable throwable) {
                    Knob.logError(throwable, "Failed to initialize CraftBossBar constructor", new Object[0]);
                }
            }
            CRAFT_BOSS_BAR_HANDLE = object5;
            NMS_BOSS_BATTLE_SET_NAME = object4;
            NMS_BOSS_BATTLE_SEND_UPDATE = object3;
        }

        public static class Builder
        extends CraftBukkitFacet<Player>
        implements Facet.BossBar.Builder<Player, BossBar> {
            protected Builder() {
                super(Player.class);
            }

            @Override
            public boolean isSupported() {
                return super.isSupported() && CLASS_CRAFT_BOSS_BAR != null && CRAFT_BOSS_BAR_HANDLE != null && NMS_BOSS_BATTLE_SET_NAME != null && NMS_BOSS_BATTLE_SEND_UPDATE != null;
            }

            @Override
            public @NotNull BossBar createBossBar(@NotNull Collection<Player> collection) {
                return new BossBar(collection);
            }
        }
    }

    static final class BookPre1_13
    extends AbstractBook {
        private static final String PACKET_TYPE_BOOK_OPEN = "MC|BOpen";
        private static final Class<?> CLASS_BYTE_BUF = MinecraftReflection.findClass("io.netty.buffer.ByteBuf");
        private static final Class<?> CLASS_PACKET_CUSTOM_PAYLOAD = MinecraftReflection.findNmsClass("PacketPlayOutCustomPayload");
        private static final Class<?> CLASS_PACKET_DATA_SERIALIZER = MinecraftReflection.findNmsClass("PacketDataSerializer");
        private static final MethodHandle NEW_PACKET_CUSTOM_PAYLOAD = MinecraftReflection.findConstructor(CLASS_PACKET_CUSTOM_PAYLOAD, String.class, CLASS_PACKET_DATA_SERIALIZER);
        private static final MethodHandle NEW_PACKET_BYTE_BUF = MinecraftReflection.findConstructor(CLASS_PACKET_DATA_SERIALIZER, CLASS_BYTE_BUF);

        BookPre1_13() {
        }

        @Override
        public boolean isSupported() {
            return super.isSupported() && CLASS_BYTE_BUF != null && CLASS_PACKET_CUSTOM_PAYLOAD != null && NEW_PACKET_CUSTOM_PAYLOAD != null;
        }

        @Override
        protected void sendOpenPacket(@NotNull Player player) {
            ByteBuf byteBuf = Unpooled.buffer();
            byteBuf.writeByte(0);
            Object object = NEW_PACKET_BYTE_BUF.invoke(byteBuf);
            this.sendMessage(player, NEW_PACKET_CUSTOM_PAYLOAD.invoke(PACKET_TYPE_BOOK_OPEN, object));
        }
    }

    static final class Book1_13
    extends AbstractBook {
        private static final Class<?> CLASS_BYTE_BUF = MinecraftReflection.findClass("io.netty.buffer.ByteBuf");
        private static final Class<?> CLASS_PACKET_CUSTOM_PAYLOAD = MinecraftReflection.findNmsClass("PacketPlayOutCustomPayload");
        private static final Class<?> CLASS_FRIENDLY_BYTE_BUF = MinecraftReflection.findNmsClass("PacketDataSerializer");
        private static final Class<?> CLASS_RESOURCE_LOCATION = MinecraftReflection.findNmsClass("MinecraftKey");
        private static final Object PACKET_TYPE_BOOK_OPEN;
        private static final MethodHandle NEW_PACKET_CUSTOM_PAYLOAD;
        private static final MethodHandle NEW_FRIENDLY_BYTE_BUF;

        Book1_13() {
        }

        @Override
        public boolean isSupported() {
            return super.isSupported() && CLASS_BYTE_BUF != null && NEW_PACKET_CUSTOM_PAYLOAD != null && PACKET_TYPE_BOOK_OPEN != null;
        }

        @Override
        protected void sendOpenPacket(@NotNull Player player) {
            ByteBuf byteBuf = Unpooled.buffer();
            byteBuf.writeByte(0);
            Object object = NEW_FRIENDLY_BYTE_BUF.invoke(byteBuf);
            this.sendMessage(player, NEW_PACKET_CUSTOM_PAYLOAD.invoke(PACKET_TYPE_BOOK_OPEN, object));
        }

        static {
            NEW_PACKET_CUSTOM_PAYLOAD = MinecraftReflection.findConstructor(CLASS_PACKET_CUSTOM_PAYLOAD, CLASS_RESOURCE_LOCATION, CLASS_FRIENDLY_BYTE_BUF);
            NEW_FRIENDLY_BYTE_BUF = MinecraftReflection.findConstructor(CLASS_FRIENDLY_BYTE_BUF, CLASS_BYTE_BUF);
            Object var0 = null;
            if (CLASS_RESOURCE_LOCATION != null) {
                try {
                    var0 = CLASS_RESOURCE_LOCATION.getConstructor(String.class).newInstance("minecraft:book_open");
                } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException reflectiveOperationException) {
                    // empty catch block
                }
            }
            PACKET_TYPE_BOOK_OPEN = var0;
        }
    }

    static final class BookPost1_13
    extends AbstractBook {
        private static final Class<?> CLASS_ENUM_HAND = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("EnumHand"), MinecraftReflection.findMcClassName("world.EnumHand"), MinecraftReflection.findMcClassName("world.InteractionHand"));
        private static final Object HAND_MAIN = MinecraftReflection.findEnum(CLASS_ENUM_HAND, "MAIN_HAND", 0);
        private static final Class<?> PACKET_OPEN_BOOK = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("PacketPlayOutOpenBook"), MinecraftReflection.findMcClassName("network.protocol.game.PacketPlayOutOpenBook"), MinecraftReflection.findMcClassName("network.protocol.game.ClientboundOpenBookPacket"));
        private static final MethodHandle NEW_PACKET_OPEN_BOOK = MinecraftReflection.findConstructor(PACKET_OPEN_BOOK, CLASS_ENUM_HAND);

        BookPost1_13() {
        }

        @Override
        public boolean isSupported() {
            return super.isSupported() && HAND_MAIN != null && NEW_PACKET_OPEN_BOOK != null;
        }

        @Override
        protected void sendOpenPacket(@NotNull Player player) {
            this.sendMessage(player, NEW_PACKET_OPEN_BOOK.invoke(HAND_MAIN));
        }
    }

    protected static abstract class AbstractBook
    extends PacketFacet<Player>
    implements Facet.Book<Player, Object, ItemStack> {
        protected static final int HAND_MAIN = 0;
        private static final Material BOOK_TYPE = (Material)MinecraftReflection.findEnum(Material.class, "WRITTEN_BOOK");
        private static final ItemStack BOOK_STACK = BOOK_TYPE == null ? null : new ItemStack(BOOK_TYPE);
        private static final String BOOK_TITLE = "title";
        private static final String BOOK_AUTHOR = "author";
        private static final String BOOK_PAGES = "pages";
        private static final String BOOK_RESOLVED = "resolved";
        private static final Class<?> CLASS_NBT_TAG_COMPOUND = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("NBTTagCompound"), MinecraftReflection.findMcClassName("nbt.CompoundTag"), MinecraftReflection.findMcClassName("nbt.NBTTagCompound"));
        private static final Class<?> CLASS_NBT_IO = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("NBTCompressedStreamTools"), MinecraftReflection.findMcClassName("nbt.NbtIo"), MinecraftReflection.findMcClassName("nbt.NBTCompressedStreamTools"));
        private static final MethodHandle NBT_IO_DESERIALIZE;
        private static final Class<?> CLASS_CRAFT_ITEMSTACK;
        private static final Class<?> CLASS_MC_ITEMSTACK;
        private static final MethodHandle MC_ITEMSTACK_SET_TAG;
        private static final MethodHandle CRAFT_ITEMSTACK_NMS_COPY;
        private static final MethodHandle CRAFT_ITEMSTACK_CRAFT_MIRROR;

        protected AbstractBook() {
        }

        protected abstract void sendOpenPacket(@NotNull Player var1);

        @Override
        public boolean isSupported() {
            return super.isSupported() && NBT_IO_DESERIALIZE != null && MC_ITEMSTACK_SET_TAG != null && CRAFT_ITEMSTACK_CRAFT_MIRROR != null && CRAFT_ITEMSTACK_NMS_COPY != null && BOOK_STACK != null;
        }

        @Override
        @NotNull
        public String createMessage(@NotNull Player player, @NotNull Component component) {
            return (String)BukkitComponentSerializer.gson().serialize(component);
        }

        @Override
        @NotNull
        public ItemStack createBook(@NotNull String string, @NotNull String string2, @NotNull Iterable<Object> iterable) {
            return this.applyTag(BOOK_STACK, AbstractBook.tagFor(string, string2, iterable));
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        @Deprecated
        public void openBook(@NotNull Player player, @NotNull ItemStack itemStack) {
            PlayerInventory playerInventory = player.getInventory();
            ItemStack itemStack2 = playerInventory.getItemInHand();
            try {
                playerInventory.setItemInHand(itemStack);
                this.sendOpenPacket(player);
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to send openBook packet: %s", itemStack);
            } finally {
                playerInventory.setItemInHand(itemStack2);
            }
        }

        private static CompoundBinaryTag tagFor(@NotNull String string, @NotNull String string2, @NotNull Iterable<Object> iterable) {
            ListBinaryTag.Builder<StringBinaryTag> builder = ListBinaryTag.builder(BinaryTagTypes.STRING);
            for (Object object : iterable) {
                builder.add(StringBinaryTag.of((String)object));
            }
            return ((CompoundBinaryTag.Builder)((CompoundBinaryTag.Builder)((CompoundBinaryTag.Builder)((CompoundBinaryTag.Builder)CompoundBinaryTag.builder().putString(BOOK_TITLE, string)).putString(BOOK_AUTHOR, string2)).put(BOOK_PAGES, builder.build())).putByte(BOOK_RESOLVED, (byte)1)).build();
        }

        @NotNull
        private Object createTag(@NotNull CompoundBinaryTag compoundBinaryTag) {
            Object object;
            TrustedByteArrayOutputStream trustedByteArrayOutputStream = new TrustedByteArrayOutputStream();
            BinaryTagIO.writer().write(compoundBinaryTag, trustedByteArrayOutputStream);
            DataInputStream dataInputStream = new DataInputStream(trustedByteArrayOutputStream.toInputStream());
            try {
                object = NBT_IO_DESERIALIZE.invoke(dataInputStream);
            } catch (Throwable throwable) {
                try {
                    try {
                        dataInputStream.close();
                    } catch (Throwable throwable2) {
                        throwable.addSuppressed(throwable2);
                    }
                    throw throwable;
                } catch (Throwable throwable3) {
                    throw new IOException(throwable3);
                }
            }
            dataInputStream.close();
            return object;
        }

        private ItemStack applyTag(@NotNull ItemStack itemStack, CompoundBinaryTag compoundBinaryTag) {
            if (CRAFT_ITEMSTACK_NMS_COPY == null || MC_ITEMSTACK_SET_TAG == null || CRAFT_ITEMSTACK_CRAFT_MIRROR == null) {
                return itemStack;
            }
            try {
                Object object = CRAFT_ITEMSTACK_NMS_COPY.invoke(itemStack);
                Object object2 = this.createTag(compoundBinaryTag);
                MC_ITEMSTACK_SET_TAG.invoke(object, object2);
                return CRAFT_ITEMSTACK_CRAFT_MIRROR.invoke(object);
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to apply NBT tag to ItemStack: %s %s", itemStack, compoundBinaryTag);
                return itemStack;
            }
        }

        static {
            MethodHandle methodHandle = null;
            if (CLASS_NBT_IO != null) {
                for (Method method : CLASS_NBT_IO.getDeclaredMethods()) {
                    Class<?> clazz;
                    if (!Modifier.isStatic(method.getModifiers()) || !method.getReturnType().equals(CLASS_NBT_TAG_COMPOUND) || method.getParameterCount() != 1 || !(clazz = method.getParameterTypes()[0]).equals(DataInputStream.class) && !clazz.equals(DataInput.class)) continue;
                    try {
                        methodHandle = MinecraftReflection.lookup().unreflect(method);
                    } catch (IllegalAccessException illegalAccessException) {}
                    break;
                }
            }
            NBT_IO_DESERIALIZE = methodHandle;
            CLASS_CRAFT_ITEMSTACK = MinecraftReflection.findCraftClass("inventory.CraftItemStack");
            CLASS_MC_ITEMSTACK = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("ItemStack"), MinecraftReflection.findMcClassName("world.item.ItemStack"));
            MC_ITEMSTACK_SET_TAG = MinecraftReflection.searchMethod(CLASS_MC_ITEMSTACK, (Integer)1, "setTag", Void.TYPE, CLASS_NBT_TAG_COMPOUND);
            CRAFT_ITEMSTACK_NMS_COPY = MinecraftReflection.findStaticMethod(CLASS_CRAFT_ITEMSTACK, "asNMSCopy", CLASS_MC_ITEMSTACK, ItemStack.class);
            CRAFT_ITEMSTACK_CRAFT_MIRROR = MinecraftReflection.findStaticMethod(CLASS_CRAFT_ITEMSTACK, "asCraftMirror", CLASS_CRAFT_ITEMSTACK, CLASS_MC_ITEMSTACK);
        }

        private static final class TrustedByteArrayOutputStream
        extends ByteArrayOutputStream {
            private TrustedByteArrayOutputStream() {
            }

            public InputStream toInputStream() {
                return new ByteArrayInputStream(this.buf, 0, this.count);
            }
        }
    }

    static final class Book_1_20_5
    extends PacketFacet<Player>
    implements Facet.Book<Player, Object, ItemStack> {
        Book_1_20_5() {
        }

        @Override
        public boolean isSupported() {
            return super.isSupported() && CraftBukkitAccess.Book_1_20_5.isSupported();
        }

        @Override
        @Nullable
        public ItemStack createBook(@NotNull String string, @NotNull String string2, @NotNull Iterable<Object> iterable) {
            try {
                Object object2;
                ItemStack itemStack = new ItemStack(Material.WRITTEN_BOOK);
                ArrayList<Object> arrayList = new ArrayList<Object>();
                for (Object object2 : iterable) {
                    arrayList.add(CraftBukkitAccess.Book_1_20_5.CREATE_FILTERABLE.invoke(object2));
                }
                Object object3 = CraftBukkitAccess.Book_1_20_5.NEW_BOOK_CONTENT.invoke(CraftBukkitAccess.Book_1_20_5.CREATE_FILTERABLE.invoke(string), string2, 0, arrayList, true);
                object2 = CraftBukkitAccess.Book_1_20_5.CRAFT_ITEMSTACK_NMS_COPY.invoke(itemStack);
                CraftBukkitAccess.Book_1_20_5.MC_ITEMSTACK_SET.invoke(object2, CraftBukkitAccess.Book_1_20_5.WRITTEN_BOOK_COMPONENT_TYPE, object3);
                return CraftBukkitAccess.Book_1_20_5.CRAFT_ITEMSTACK_CRAFT_MIRROR.invoke(object2);
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to apply written_book_content component to ItemStack", new Object[0]);
                return null;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void openBook(@NotNull Player player, @NotNull ItemStack itemStack) {
            PlayerInventory playerInventory = player.getInventory();
            ItemStack itemStack2 = playerInventory.getItemInHand();
            try {
                playerInventory.setItemInHand(itemStack);
                this.sendMessage(player, CraftBukkitAccess.Book_1_20_5.NEW_PACKET_OPEN_BOOK.invoke(CraftBukkitAccess.Book_1_20_5.HAND_MAIN));
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to send openBook packet: %s", itemStack);
            } finally {
                playerInventory.setItemInHand(itemStack2);
            }
        }
    }

    static class Title
    extends PacketFacet<Player>
    implements Facet.Title<Player, Object, List<Object>, List<?>> {
        Title() {
        }

        @Override
        public boolean isSupported() {
            return super.isSupported() && CONSTRUCTOR_TITLE_MESSAGE != null && CONSTRUCTOR_TITLE_TIMES != null;
        }

        @Override
        @NotNull
        public List<Object> createTitleCollection() {
            return new ArrayList<Object>();
        }

        @Override
        public void contributeTitle(@NotNull List<Object> list, @NotNull Object object) {
            try {
                list.add(CONSTRUCTOR_TITLE_MESSAGE.invoke(TITLE_ACTION_TITLE, object));
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to invoke title packet constructor", new Object[0]);
            }
        }

        @Override
        public void contributeSubtitle(@NotNull List<Object> list, @NotNull Object object) {
            try {
                list.add(CONSTRUCTOR_TITLE_MESSAGE.invoke(TITLE_ACTION_SUBTITLE, object));
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to invoke subtitle packet constructor", new Object[0]);
            }
        }

        @Override
        public void contributeTimes(@NotNull List<Object> list, int n, int n2, int n3) {
            try {
                list.add(CONSTRUCTOR_TITLE_TIMES.invoke(n, n2, n3));
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to invoke title animations packet constructor", new Object[0]);
            }
        }

        @Override
        @Nullable
        public List<?> completeTitle(@NotNull List<Object> list) {
            return list;
        }

        @Override
        public void showTitle(@NotNull Player player, @NotNull List<?> list) {
            for (Object obj : list) {
                this.sendMessage(player, obj);
            }
        }

        @Override
        public void clearTitle(@NotNull Player player) {
            try {
                if (TITLE_ACTION_CLEAR != null) {
                    this.sendPacket(player, CONSTRUCTOR_TITLE_MESSAGE.invoke(TITLE_ACTION_CLEAR, null));
                } else {
                    player.sendTitle("", "", -1, -1, -1);
                }
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to clear title", new Object[0]);
            }
        }

        @Override
        public void resetTitle(@NotNull Player player) {
            try {
                if (TITLE_ACTION_RESET != null) {
                    this.sendPacket(player, CONSTRUCTOR_TITLE_MESSAGE.invoke(TITLE_ACTION_RESET, null));
                } else {
                    player.resetTitle();
                }
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to clear title", new Object[0]);
            }
        }
    }

    static class Title_1_17
    extends PacketFacet<Player>
    implements Facet.Title<Player, Object, List<Object>, List<?>> {
        private static final Class<?> PACKET_SET_TITLE = MinecraftReflection.findMcClass("network.protocol.game.ClientboundSetTitleTextPacket");
        private static final Class<?> PACKET_SET_SUBTITLE = MinecraftReflection.findMcClass("network.protocol.game.ClientboundSetSubtitleTextPacket");
        private static final Class<?> PACKET_SET_TITLE_ANIMATION = MinecraftReflection.findMcClass("network.protocol.game.ClientboundSetTitlesAnimationPacket");
        private static final Class<?> PACKET_CLEAR_TITLES = MinecraftReflection.findMcClass("network.protocol.game.ClientboundClearTitlesPacket");
        private static final MethodHandle CONSTRUCTOR_SET_TITLE = MinecraftReflection.findConstructor(PACKET_SET_TITLE, CraftBukkitFacet.access$500());
        private static final MethodHandle CONSTRUCTOR_SET_SUBTITLE = MinecraftReflection.findConstructor(PACKET_SET_SUBTITLE, CraftBukkitFacet.access$500());
        private static final MethodHandle CONSTRUCTOR_SET_TITLE_ANIMATION = MinecraftReflection.findConstructor(PACKET_SET_TITLE_ANIMATION, Integer.TYPE, Integer.TYPE, Integer.TYPE);
        private static final MethodHandle CONSTRUCTOR_CLEAR_TITLES = MinecraftReflection.findConstructor(PACKET_CLEAR_TITLES, Boolean.TYPE);

        Title_1_17() {
        }

        @Override
        public boolean isSupported() {
            return super.isSupported() && CONSTRUCTOR_SET_TITLE != null && CONSTRUCTOR_SET_SUBTITLE != null && CONSTRUCTOR_SET_TITLE_ANIMATION != null && CONSTRUCTOR_CLEAR_TITLES != null;
        }

        @Override
        @NotNull
        public List<Object> createTitleCollection() {
            return new ArrayList<Object>();
        }

        @Override
        public void contributeTitle(@NotNull List<Object> list, @NotNull Object object) {
            try {
                list.add(CONSTRUCTOR_SET_TITLE.invoke(object));
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to invoke title packet constructor", new Object[0]);
            }
        }

        @Override
        public void contributeSubtitle(@NotNull List<Object> list, @NotNull Object object) {
            try {
                list.add(CONSTRUCTOR_SET_SUBTITLE.invoke(object));
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to invoke subtitle packet constructor", new Object[0]);
            }
        }

        @Override
        public void contributeTimes(@NotNull List<Object> list, int n, int n2, int n3) {
            try {
                list.add(CONSTRUCTOR_SET_TITLE_ANIMATION.invoke(n, n2, n3));
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to invoke title animations packet constructor", new Object[0]);
            }
        }

        @Override
        @Nullable
        public List<?> completeTitle(@NotNull List<Object> list) {
            return list;
        }

        @Override
        public void showTitle(@NotNull Player player, @NotNull List<?> list) {
            for (Object obj : list) {
                this.sendMessage(player, obj);
            }
        }

        @Override
        public void clearTitle(@NotNull Player player) {
            try {
                if (CONSTRUCTOR_CLEAR_TITLES != null) {
                    this.sendPacket(player, CONSTRUCTOR_CLEAR_TITLES.invoke(false));
                } else {
                    player.sendTitle("", "", -1, -1, -1);
                }
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to clear title", new Object[0]);
            }
        }

        @Override
        public void resetTitle(@NotNull Player player) {
            try {
                if (CONSTRUCTOR_CLEAR_TITLES != null) {
                    this.sendPacket(player, CONSTRUCTOR_CLEAR_TITLES.invoke(true));
                } else {
                    player.resetTitle();
                }
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to clear title", new Object[0]);
            }
        }
    }

    static class EntitySound
    extends PacketFacet<Player>
    implements PartialEntitySound {
        private static final Class<?> CLASS_CLIENTBOUND_CUSTOM_SOUND = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("PacketPlayOutCustomSoundEffect"), MinecraftReflection.findMcClassName("network.protocol.game.ClientboundCustomSoundPacket"), MinecraftReflection.findMcClassName("network.protocol.game.PacketPlayOutCustomSoundEffect"));
        private static final Class<?> CLASS_VEC3 = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("Vec3D"), MinecraftReflection.findMcClassName("world.phys.Vec3D"), MinecraftReflection.findMcClassName("world.phys.Vec3"));
        private static final MethodHandle NEW_CLIENTBOUND_ENTITY_SOUND;
        private static final MethodHandle NEW_CLIENTBOUND_CUSTOM_SOUND;
        private static final MethodHandle NEW_VEC3;
        private static final MethodHandle NEW_RESOURCE_LOCATION;
        private static final MethodHandle REGISTRY_GET_OPTIONAL;
        private static final Object REGISTRY_SOUND_EVENT;

        EntitySound() {
        }

        @Override
        public boolean isSupported() {
            return super.isSupported() && NEW_CLIENTBOUND_ENTITY_SOUND != null && NEW_RESOURCE_LOCATION != null && REGISTRY_SOUND_EVENT != null && REGISTRY_GET_OPTIONAL != null && CRAFT_ENTITY_GET_HANDLE != null && CraftBukkitAccess.EntitySound.isSupported();
        }

        @Override
        public Object createForEntity(Sound sound, Entity entity) {
            try {
                Object object = this.toNativeEntity(entity);
                if (object == null) {
                    return null;
                }
                Object object2 = this.toVanilla(sound.source());
                if (object2 == null) {
                    return null;
                }
                Object object3 = NEW_RESOURCE_LOCATION.invoke(sound.name().namespace(), sound.name().value());
                Optional optional = REGISTRY_GET_OPTIONAL.invoke(REGISTRY_SOUND_EVENT, object3);
                long l = sound.seed().orElseGet(() -> ThreadLocalRandom.current().nextLong());
                if (optional.isPresent()) {
                    return NEW_CLIENTBOUND_ENTITY_SOUND.invoke(optional.get(), object2, object, sound.volume(), sound.pitch(), l);
                }
                if (NEW_CLIENTBOUND_CUSTOM_SOUND != null && NEW_VEC3 != null) {
                    Location location = entity.getLocation();
                    return NEW_CLIENTBOUND_CUSTOM_SOUND.invoke(object3, object2, NEW_VEC3.invoke(location.getX(), location.getY(), location.getZ()), sound.volume(), sound.pitch(), l);
                }
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to send sound tracking an entity", new Object[0]);
            }
            return null;
        }

        @Override
        public void playSound(@NotNull Player player, Object object) {
            this.sendPacket(player, object);
        }

        static {
            NEW_VEC3 = MinecraftReflection.findConstructor(CLASS_VEC3, Double.TYPE, Double.TYPE, Double.TYPE);
            NEW_RESOURCE_LOCATION = MinecraftReflection.findConstructor(CraftBukkitAccess.CLASS_RESOURCE_LOCATION, String.class, String.class);
            REGISTRY_GET_OPTIONAL = MinecraftReflection.searchMethod(CraftBukkitAccess.CLASS_REGISTRY, (Integer)1, "getOptional", Optional.class, CraftBukkitAccess.CLASS_RESOURCE_LOCATION);
            Object object = MinecraftReflection.findConstructor(CraftBukkitAccess.EntitySound.CLASS_CLIENTBOUND_ENTITY_SOUND, CraftBukkitAccess.EntitySound.CLASS_SOUND_EVENT, CraftBukkitAccess.EntitySound.CLASS_SOUND_SOURCE, CLASS_NMS_ENTITY, Float.TYPE, Float.TYPE, Long.TYPE);
            if (object == null && (object = MinecraftReflection.findConstructor(CraftBukkitAccess.EntitySound.CLASS_CLIENTBOUND_ENTITY_SOUND, CraftBukkitAccess.EntitySound.CLASS_SOUND_EVENT, CraftBukkitAccess.EntitySound.CLASS_SOUND_SOURCE, CLASS_NMS_ENTITY, Float.TYPE, Float.TYPE)) != null) {
                object = MethodHandles.dropArguments((MethodHandle)object, 5, Long.TYPE);
            }
            NEW_CLIENTBOUND_ENTITY_SOUND = object;
            object = MinecraftReflection.findConstructor(CLASS_CLIENTBOUND_CUSTOM_SOUND, CraftBukkitAccess.CLASS_RESOURCE_LOCATION, CraftBukkitAccess.EntitySound.CLASS_SOUND_SOURCE, CLASS_VEC3, Float.TYPE, Float.TYPE, Long.TYPE);
            if (object == null && (object = MinecraftReflection.findConstructor(CLASS_CLIENTBOUND_CUSTOM_SOUND, CraftBukkitAccess.CLASS_RESOURCE_LOCATION, CraftBukkitAccess.EntitySound.CLASS_SOUND_SOURCE, CLASS_VEC3, Float.TYPE, Float.TYPE)) != null) {
                object = MethodHandles.dropArguments((MethodHandle)object, 5, Long.TYPE);
            }
            NEW_CLIENTBOUND_CUSTOM_SOUND = object;
            object = null;
            if (CraftBukkitAccess.CLASS_REGISTRY != null) {
                try {
                    Field field = MinecraftReflection.findField(CraftBukkitAccess.CLASS_REGISTRY, "SOUND_EVENT");
                    if (field != null) {
                        object = field.get(null);
                    } else {
                        Object object2 = null;
                        for (Field field2 : CraftBukkitAccess.CLASS_REGISTRY.getDeclaredFields()) {
                            int n = 28;
                            if ((field2.getModifiers() & 0x1C) != 28 || !field2.getType().equals(CraftBukkitAccess.CLASS_WRITABLE_REGISTRY)) continue;
                            field2.setAccessible(true);
                            object2 = field2.get(null);
                            break;
                        }
                        if (object2 != null) {
                            object = REGISTRY_GET_OPTIONAL.invoke(object2, NEW_RESOURCE_LOCATION.invoke("minecraft", "sound_event")).orElse(null);
                        }
                    }
                } catch (Throwable throwable) {
                    Knob.logError(throwable, "Failed to initialize EntitySound CraftBukkit facet", new Object[0]);
                }
            }
            REGISTRY_SOUND_EVENT = object;
        }
    }

    static class EntitySound_1_19_3
    extends PacketFacet<Player>
    implements PartialEntitySound {
        EntitySound_1_19_3() {
        }

        @Override
        public boolean isSupported() {
            return CraftBukkitAccess.EntitySound_1_19_3.isSupported() && super.isSupported();
        }

        @Override
        public Object createForEntity(Sound sound, Entity entity) {
            try {
                Object object = CraftBukkitAccess.EntitySound_1_19_3.NEW_RESOURCE_LOCATION.invoke(sound.name().namespace(), sound.name().value());
                Optional optional = CraftBukkitAccess.EntitySound_1_19_3.REGISTRY_GET_OPTIONAL.invoke(CraftBukkitAccess.EntitySound_1_19_3.SOUND_EVENT_REGISTRY, object);
                Object object2 = optional.isPresent() ? optional.get() : CraftBukkitAccess.EntitySound_1_19_3.SOUND_EVENT_CREATE_VARIABLE_RANGE.invoke(object);
                Object object3 = CraftBukkitAccess.EntitySound_1_19_3.REGISTRY_WRAP_AS_HOLDER.invoke(CraftBukkitAccess.EntitySound_1_19_3.SOUND_EVENT_REGISTRY, object2);
                long l = sound.seed().orElseGet(() -> ThreadLocalRandom.current().nextLong());
                return CraftBukkitAccess.EntitySound_1_19_3.NEW_CLIENTBOUND_ENTITY_SOUND.invoke(object3, this.toVanilla(sound.source()), this.toNativeEntity(entity), sound.volume(), sound.pitch(), l);
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to send sound tracking an entity", new Object[0]);
                return null;
            }
        }

        @Override
        public void playSound(@NotNull Player player, Object object) {
            this.sendPacket(player, object);
        }
    }

    private static interface PartialEntitySound
    extends Facet.EntitySound<Player, Object> {
        public static final Map<String, Object> MC_SOUND_SOURCE_BY_NAME = new ConcurrentHashMap<String, Object>();

        @Override
        default public Object createForSelf(Player viewer, @NotNull Sound sound) {
            return this.createForEntity(sound, (Entity)viewer);
        }

        @Override
        default public Object createForEmitter(@NotNull Sound sound, @NotNull Sound.Emitter emitter) {
            Entity entity;
            if (emitter instanceof BukkitEmitter) {
                entity = ((BukkitEmitter)emitter).entity;
            } else if (emitter instanceof Entity) {
                entity = (Entity)emitter;
            } else {
                return null;
            }
            return this.createForEntity(sound, entity);
        }

        default public Object toNativeEntity(Entity entity) throws Throwable {
            if (!CLASS_CRAFT_ENTITY.isInstance(entity)) {
                return null;
            }
            return CRAFT_ENTITY_GET_HANDLE.invoke(entity);
        }

        default public Object toVanilla(Sound.Source source) throws Throwable {
            if (MC_SOUND_SOURCE_BY_NAME.isEmpty()) {
                for (Object enumConstant : CraftBukkitAccess.EntitySound.CLASS_SOUND_SOURCE.getEnumConstants()) {
                    MC_SOUND_SOURCE_BY_NAME.put(CraftBukkitAccess.EntitySound.SOUND_SOURCE_GET_NAME.invoke(enumConstant), enumConstant);
                }
            }
            return MC_SOUND_SOURCE_BY_NAME.get(Sound.Source.NAMES.key(source));
        }

        public Object createForEntity(Sound var1, Entity var2);
    }

    static class ActionBarLegacy
    extends PacketFacet<Player>
    implements Facet.ActionBar<Player, Object> {
        ActionBarLegacy() {
        }

        @Override
        public boolean isSupported() {
            return super.isSupported() && LEGACY_CHAT_PACKET_CONSTRUCTOR != null;
        }

        @Override
        @Nullable
        public Object createMessage(@NotNull Player player, @NotNull Component component) {
            TextComponent textComponent = Component.text(BukkitComponentSerializer.legacy().serialize(component));
            try {
                return LEGACY_CHAT_PACKET_CONSTRUCTOR.invoke(super.createMessage(player, (Component)textComponent), (byte)2);
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to invoke PacketPlayOutChat constructor: %s", textComponent);
                return null;
            }
        }
    }

    static class ActionBar
    extends PacketFacet<Player>
    implements Facet.ActionBar<Player, Object> {
        ActionBar() {
        }

        @Override
        public boolean isSupported() {
            return super.isSupported() && TITLE_ACTION_ACTIONBAR != null;
        }

        @Override
        @Nullable
        public Object createMessage(@NotNull Player player, @NotNull Component component) {
            try {
                return CONSTRUCTOR_TITLE_MESSAGE.invoke(TITLE_ACTION_ACTIONBAR, super.createMessage(player, component));
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to invoke PacketPlayOutTitle constructor: %s", component);
                return null;
            }
        }
    }

    static class ActionBar_1_17
    extends PacketFacet<Player>
    implements Facet.ActionBar<Player, Object> {
        @Nullable
        private static final Class<?> CLASS_SET_ACTION_BAR_TEXT_PACKET = MinecraftReflection.findMcClass("network.protocol.game.ClientboundSetActionBarTextPacket");
        @Nullable
        private static final MethodHandle CONSTRUCTOR_ACTION_BAR = MinecraftReflection.findConstructor(CLASS_SET_ACTION_BAR_TEXT_PACKET, CraftBukkitFacet.access$500());

        ActionBar_1_17() {
        }

        @Override
        public boolean isSupported() {
            return super.isSupported() && CONSTRUCTOR_ACTION_BAR != null;
        }

        @Override
        @Nullable
        public Object createMessage(@NotNull Player player, @NotNull Component component) {
            try {
                return CONSTRUCTOR_ACTION_BAR.invoke(super.createMessage(player, component));
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to invoke PacketPlayOutTitle constructor: %s", component);
                return null;
            }
        }
    }

    static class Chat
    extends PacketFacet<CommandSender>
    implements Facet.Chat<CommandSender, Object> {
        Chat() {
        }

        @Override
        public boolean isSupported() {
            return super.isSupported() && CHAT_PACKET_CONSTRUCTOR != null;
        }

        @Override
        public void sendMessage(@NotNull CommandSender commandSender, @NotNull Identity identity, @NotNull Object object, @NotNull Object object2) {
            Object object3 = object2 == MessageType.CHAT ? MESSAGE_TYPE_CHAT : MESSAGE_TYPE_SYSTEM;
            try {
                this.sendMessage(commandSender, CHAT_PACKET_CONSTRUCTOR.invoke(object, object3, identity.uuid()));
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to invoke PacketPlayOutChat constructor: %s %s", object, object3);
            }
        }
    }

    static class Chat1_19_3
    extends Chat {
        Chat1_19_3() {
        }

        @Override
        public boolean isSupported() {
            return super.isSupported() && CraftBukkitAccess.Chat1_19_3.isSupported();
        }

        @Override
        public void sendMessage(@NotNull CommandSender commandSender, @NotNull Identity identity, @NotNull Object object, @NotNull Object object2) {
            if (!(object2 instanceof ChatType.Bound)) {
                super.sendMessage(commandSender, identity, object, object2);
            } else {
                ChatType.Bound bound = (ChatType.Bound)object2;
                try {
                    Object object3;
                    Object object4 = this.createMessage(commandSender, bound.name());
                    Object object5 = bound.target() != null ? this.createMessage(commandSender, bound.target()) : null;
                    Object object6 = CraftBukkitAccess.Chat1_19_3.ACTUAL_GET_REGISTRY_ACCESS.invoke(CraftBukkitAccess.Chat1_19_3.SERVER_PLAYER_GET_LEVEL.invoke(CRAFT_PLAYER_GET_HANDLE.invoke(commandSender)));
                    Object t = CraftBukkitAccess.Chat1_19_3.REGISTRY_ACCESS_GET_REGISTRY_OPTIONAL.invoke(object6, CraftBukkitAccess.Chat1_19_3.CHAT_TYPE_RESOURCE_KEY).orElseThrow(NoSuchElementException::new);
                    Object object7 = CraftBukkitAccess.Chat1_19_3.NEW_RESOURCE_LOCATION.invoke(bound.type().key().namespace(), bound.type().key().value());
                    if (CraftBukkitAccess.Chat1_19_3.CHAT_TYPE_BOUND_NETWORK_CONSTRUCTOR != null) {
                        Object t2 = CraftBukkitAccess.Chat1_19_3.REGISTRY_GET_OPTIONAL.invoke(t, object7).orElseThrow(NoSuchElementException::new);
                        int n = CraftBukkitAccess.Chat1_19_3.REGISTRY_GET_ID.invoke(t, t2);
                        if (n < 0) {
                            throw new IllegalArgumentException("Could not get a valid network id from " + object2);
                        }
                        object3 = CraftBukkitAccess.Chat1_19_3.CHAT_TYPE_BOUND_NETWORK_CONSTRUCTOR.invoke(n, object4, object5);
                    } else {
                        Object t3 = CraftBukkitAccess.Chat1_19_3.REGISTRY_GET_HOLDER.invoke(t, object7).orElseThrow(NoSuchElementException::new);
                        object3 = CraftBukkitAccess.Chat1_19_3.CHAT_TYPE_BOUND_CONSTRUCTOR.invoke(t3, object4, Optional.ofNullable(object5));
                    }
                    this.sendMessage(commandSender, CraftBukkitAccess.Chat1_19_3.DISGUISED_CHAT_PACKET_CONSTRUCTOR.invoke(object, object3));
                } catch (Throwable throwable) {
                    Knob.logError(throwable, "Failed to send a 1.19.3+ message: %s %s", object, object2);
                }
            }
        }
    }

    static class PacketFacet<V extends CommandSender>
    extends CraftBukkitFacet<V>
    implements Facet.Message<V, Object> {
        protected PacketFacet() {
            super(CLASS_CRAFT_PLAYER);
        }

        public void sendPacket(@NotNull Player player, @Nullable Object object) {
            if (object == null) {
                return;
            }
            try {
                PLAYER_CONNECTION_SEND_PACKET.invoke(ENTITY_PLAYER_GET_CONNECTION.invoke(CRAFT_PLAYER_GET_HANDLE.invoke(player)), object);
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to invoke CraftBukkit sendPacket: %s", object);
            }
        }

        public void sendMessage(@NotNull V v, @Nullable Object object) {
            this.sendPacket((Player)v, object);
        }

        @Override
        @Nullable
        public Object createMessage(@NotNull V v, @NotNull Component component) {
            try {
                return MinecraftComponentSerializer.get().serialize(component);
            } catch (Throwable throwable) {
                Knob.logError(throwable, "Failed to serialize net.minecraft.server IChatBaseComponent: %s", component);
                return null;
            }
        }
    }
}

