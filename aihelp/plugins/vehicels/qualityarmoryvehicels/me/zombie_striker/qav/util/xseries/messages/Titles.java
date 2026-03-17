/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.util.xseries.messages;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Function;
import me.zombie_striker.qav.util.xseries.reflection.XReflection;
import me.zombie_striker.qav.util.xseries.reflection.minecraft.MinecraftClassHandle;
import me.zombie_striker.qav.util.xseries.reflection.minecraft.MinecraftConnection;
import me.zombie_striker.qav.util.xseries.reflection.minecraft.MinecraftPackage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Titles {
    private static final Object TITLE_ACTION_TITLE;
    private static final Object TITLE_ACTION_SUBTITLE;
    private static final Object TITLE_ACTION_TIMES;
    private static final Object TITLE_ACTION_CLEAR;
    private static final MethodHandle PACKET_PLAY_OUT_TITLE;
    private static final MethodHandle CHAT_COMPONENT_TEXT;
    private String title;
    private String subtitle;
    private final int fadeIn;
    private final int stay;
    private final int fadeOut;
    private static final boolean SUPPORTS_TITLES;

    public Titles(String string, String string2, int n, int n2, int n3) {
        this.title = string;
        this.subtitle = string2;
        this.fadeIn = n;
        this.stay = n2;
        this.fadeOut = n3;
    }

    public Titles copy() {
        return new Titles(this.title, this.subtitle, this.fadeIn, this.stay, this.fadeOut);
    }

    public void send(Player player) {
        Titles.sendTitle(player, this.fadeIn, this.stay, this.fadeOut, this.title, this.subtitle);
    }

    public static void sendTitle(@NotNull Player player, int n, int n2, int n3, @Nullable String string, @Nullable String string2) {
        Objects.requireNonNull(player, "Cannot send title to null player");
        if (string == null && string2 == null) {
            return;
        }
        if (SUPPORTS_TITLES) {
            player.sendTitle(string, string2, n, n2, n3);
            return;
        }
        try {
            Object object;
            Object object2 = PACKET_PLAY_OUT_TITLE.invoke(TITLE_ACTION_TIMES, CHAT_COMPONENT_TEXT.invoke(string), n, n2, n3);
            MinecraftConnection.sendPacket(player, object2);
            if (string != null) {
                object = PACKET_PLAY_OUT_TITLE.invoke(TITLE_ACTION_TITLE, CHAT_COMPONENT_TEXT.invoke(string), n, n2, n3);
                MinecraftConnection.sendPacket(player, object);
            }
            if (string2 != null) {
                object = PACKET_PLAY_OUT_TITLE.invoke(TITLE_ACTION_SUBTITLE, CHAT_COMPONENT_TEXT.invoke(string2), n, n2, n3);
                MinecraftConnection.sendPacket(player, object);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static void sendTitle(@NotNull Player player, @NotNull String string, @NotNull String string2) {
        Titles.sendTitle(player, 10, 20, 10, string, string2);
    }

    public static Titles sendTitle(@NotNull Player player, @NotNull ConfigurationSection configurationSection) {
        Titles titles = Titles.parseTitle(configurationSection, null);
        titles.send(player);
        return titles;
    }

    public static Titles parseTitle(@NotNull ConfigurationSection configurationSection) {
        return Titles.parseTitle(configurationSection, null);
    }

    public static Titles parseTitle(@NotNull ConfigurationSection configurationSection, @Nullable Function<String, String> function) {
        String string = configurationSection.getString("title");
        String string2 = configurationSection.getString("subtitle");
        if (function != null) {
            string = function.apply(string);
            string2 = function.apply(string2);
        }
        int n = configurationSection.getInt("fade-in");
        int n2 = configurationSection.getInt("stay");
        int n3 = configurationSection.getInt("fade-out");
        if (n < 1) {
            n = 10;
        }
        if (n2 < 1) {
            n2 = 20;
        }
        if (n3 < 1) {
            n3 = 10;
        }
        return new Titles(string, string2, n, n2, n3);
    }

    public String getTitle() {
        return this.title;
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public void setTitle(String string) {
        this.title = string;
    }

    public void setSubtitle(String string) {
        this.subtitle = string;
    }

    public static void clearTitle(@NotNull Player player) {
        Object object;
        Objects.requireNonNull(player, "Cannot clear title from null player");
        if (XReflection.supports(11)) {
            player.resetTitle();
            return;
        }
        try {
            object = PACKET_PLAY_OUT_TITLE.invoke(TITLE_ACTION_CLEAR, null, -1, -1, -1);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return;
        }
        MinecraftConnection.sendPacket(player, object);
    }

    public static void sendTabList(@NotNull String string, @NotNull String string2, Player ... playerArray) {
        Objects.requireNonNull(playerArray, "Cannot send tab title to null players");
        Objects.requireNonNull(string, "Tab title header cannot be null");
        Objects.requireNonNull(string2, "Tab title footer cannot be null");
        if (XReflection.supports(13)) {
            for (Player player : playerArray) {
                player.setPlayerListHeaderFooter(string, string2);
            }
            return;
        }
        try {
            Class clazz = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.chat").named("IChatBaseComponent").unreflect();
            Class clazz2 = (Class)XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS, "network.protocol.game").named("PacketPlayOutPlayerListHeaderFooter").unreflect();
            Method method = clazz.getDeclaredClasses()[0].getMethod("a", String.class);
            Object object = method.invoke(null, "{\"text\":\"" + string + "\"}");
            Object object2 = method.invoke(null, "{\"text\":\"" + string2 + "\"}");
            Object t = clazz2.getConstructor(new Class[0]).newInstance(new Object[0]);
            Field field = clazz2.getDeclaredField("a");
            Field field2 = clazz2.getDeclaredField("b");
            field.setAccessible(true);
            field.set(t, object);
            field2.setAccessible(true);
            field2.set(t, object2);
            for (Player player : playerArray) {
                MinecraftConnection.sendPacket(player, t);
            }
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to send tablist: " + string + " - " + string2, exception);
        }
    }

    static {
        boolean bl;
        MethodHandle methodHandle = null;
        MethodHandle methodHandle2 = null;
        Object var2_2 = null;
        Object var3_3 = null;
        Object var4_4 = null;
        Object var5_5 = null;
        try {
            Player.class.getDeclaredMethod("sendTitle", String.class, String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE);
            bl = true;
        } catch (NoSuchMethodException noSuchMethodException) {
            bl = false;
        }
        SUPPORTS_TITLES = bl;
        if (!SUPPORTS_TITLES) {
            MinecraftClassHandle minecraftClassHandle = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS).named("ChatComponentText");
            MinecraftClassHandle minecraftClassHandle2 = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS).named("PacketPlayOutTitle");
            MinecraftClassHandle minecraftClassHandle3 = XReflection.ofMinecraft().inPackage(MinecraftPackage.NMS).named("IChatBaseComponent");
            Class<?> clazz = ((Class)minecraftClassHandle2.unreflect()).getDeclaredClasses()[0];
            block16: for (Object obj : clazz.getEnumConstants()) {
                switch (obj.toString()) {
                    case "TIMES": {
                        var2_2 = obj;
                        continue block16;
                    }
                    case "TITLE": {
                        var3_3 = obj;
                        continue block16;
                    }
                    case "SUBTITLE": {
                        var4_4 = obj;
                        continue block16;
                    }
                    case "CLEAR": {
                        var5_5 = obj;
                    }
                }
            }
            try {
                methodHandle2 = minecraftClassHandle.constructor(String.class).reflect();
                methodHandle = minecraftClassHandle2.constructor(clazz, (Class)minecraftClassHandle3.unreflect(), Integer.TYPE, Integer.TYPE, Integer.TYPE).reflect();
            } catch (ReflectiveOperationException reflectiveOperationException) {
                reflectiveOperationException.printStackTrace();
            }
        }
        TITLE_ACTION_TITLE = var3_3;
        TITLE_ACTION_SUBTITLE = var4_4;
        TITLE_ACTION_TIMES = var2_2;
        TITLE_ACTION_CLEAR = var5_5;
        PACKET_PLAY_OUT_TITLE = methodHandle;
        CHAT_COMPONENT_TEXT = methodHandle2;
    }
}

