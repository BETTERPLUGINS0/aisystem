/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.viaversion.viaversion.api.Via
 *  com.viaversion.viaversion.api.connection.UserConnection
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.SoundCategory
 *  org.bukkit.boss.BarColor
 *  org.bukkit.boss.BarFlag
 *  org.bukkit.boss.BarStyle
 *  org.bukkit.boss.BossBar
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.util.Vector
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.platform.bukkit;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.lang.invoke.MethodHandle;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.permission.PermissionChecker;
import net.kyori.adventure.platform.bukkit.BukkitComponentSerializer;
import net.kyori.adventure.platform.bukkit.MinecraftReflection;
import net.kyori.adventure.platform.facet.Facet;
import net.kyori.adventure.platform.facet.FacetBase;
import net.kyori.adventure.platform.facet.FacetPointers;
import net.kyori.adventure.platform.facet.Knob;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.translation.Translator;
import net.kyori.adventure.util.TriState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class BukkitFacet<V extends CommandSender>
extends FacetBase<V> {
    protected BukkitFacet(@Nullable Class<? extends V> clazz) {
        super(clazz);
    }

    static final class PlayerPointers
    extends BukkitFacet<Player>
    implements Facet.Pointers<Player> {
        private static final MethodHandle LOCALE_SUPPORTED = MinecraftReflection.findMethod(Player.class, "getLocale", String.class, new Class[0]);

        PlayerPointers() {
            super(Player.class);
        }

        @Override
        public void contributePointers(Player player, Pointers.Builder builder) {
            builder.withDynamic(Identity.UUID, () -> ((Player)player).getUniqueId());
            builder.withDynamic(Identity.DISPLAY_NAME, () -> BukkitComponentSerializer.legacy().deserializeOrNull(player.getDisplayName()));
            builder.withDynamic(Identity.LOCALE, () -> {
                if (LOCALE_SUPPORTED != null) {
                    try {
                        return Translator.parseLocale(LOCALE_SUPPORTED.invoke(player));
                    } catch (Throwable throwable) {
                        Knob.logError(throwable, "Failed to call getLocale() for %s", player);
                    }
                }
                return Locale.getDefault();
            });
            builder.withStatic(FacetPointers.TYPE, FacetPointers.Type.PLAYER);
            builder.withDynamic(FacetPointers.WORLD, () -> Key.key(player.getWorld().getName()));
        }
    }

    static final class ConsoleCommandSenderPointers
    extends BukkitFacet<ConsoleCommandSender>
    implements Facet.Pointers<ConsoleCommandSender> {
        ConsoleCommandSenderPointers() {
            super(ConsoleCommandSender.class);
        }

        @Override
        public void contributePointers(ConsoleCommandSender consoleCommandSender, Pointers.Builder builder) {
            builder.withStatic(FacetPointers.TYPE, FacetPointers.Type.CONSOLE);
        }
    }

    static final class CommandSenderPointers
    extends BukkitFacet<CommandSender>
    implements Facet.Pointers<CommandSender> {
        CommandSenderPointers() {
            super(CommandSender.class);
        }

        @Override
        public void contributePointers(CommandSender commandSender, Pointers.Builder builder) {
            builder.withDynamic(Identity.NAME, () -> ((CommandSender)commandSender).getName());
            builder.withStatic(PermissionChecker.POINTER, string -> {
                if (commandSender.isPermissionSet(string)) {
                    return commandSender.hasPermission(string) ? TriState.TRUE : TriState.FALSE;
                }
                return TriState.NOT_SET;
            });
        }
    }

    static final class TabList
    extends Message<Player>
    implements Facet.TabList<Player, String> {
        private static final boolean SUPPORTED = MinecraftReflection.hasMethod(Player.class, "setPlayerListHeader", String.class);

        TabList() {
            super(Player.class);
        }

        @Override
        public boolean isSupported() {
            return SUPPORTED && super.isSupported();
        }

        @Override
        public void send(Player player, @Nullable String string, @Nullable String string2) {
            if (string != null && string2 != null) {
                player.setPlayerListHeaderFooter(string, string2);
            } else if (string != null) {
                player.setPlayerListHeader(string);
            } else if (string2 != null) {
                player.setPlayerListFooter(string2);
            }
        }
    }

    static final class ViaHook
    implements Function<Player, UserConnection> {
        ViaHook() {
        }

        @Override
        public UserConnection apply(@NotNull Player player) {
            return Via.getManager().getConnectionManager().getConnectedClient(player.getUniqueId());
        }
    }

    static class BossBar
    extends Message<Player>
    implements Facet.BossBar<Player> {
        protected final org.bukkit.boss.BossBar bar = Bukkit.createBossBar((String)"", (BarColor)BarColor.PINK, (BarStyle)BarStyle.SOLID, (BarFlag[])new BarFlag[0]);

        protected BossBar(@NotNull Collection<Player> collection) {
            super(Player.class);
            this.bar.setVisible(false);
            for (Player player : collection) {
                this.bar.addPlayer(player);
            }
        }

        @Override
        public void bossBarInitialized(@NotNull net.kyori.adventure.bossbar.BossBar bossBar) {
            Facet.BossBar.super.bossBarInitialized(bossBar);
            this.bar.setVisible(true);
        }

        @Override
        public void bossBarNameChanged(@NotNull net.kyori.adventure.bossbar.BossBar bossBar, @NotNull Component component, @NotNull Component component2) {
            if (!this.bar.getPlayers().isEmpty()) {
                this.bar.setTitle(this.createMessage((Player)this.bar.getPlayers().get(0), component2));
            }
        }

        @Override
        public void bossBarProgressChanged(@NotNull net.kyori.adventure.bossbar.BossBar bossBar, float f, float f2) {
            this.bar.setProgress((double)f2);
        }

        @Override
        public void bossBarColorChanged(@NotNull net.kyori.adventure.bossbar.BossBar bossBar, @NotNull BossBar.Color color, @NotNull BossBar.Color color2) {
            BarColor barColor = this.color(color2);
            if (barColor != null) {
                this.bar.setColor(barColor);
            }
        }

        @Nullable
        private BarColor color(@NotNull BossBar.Color color) {
            if (color == BossBar.Color.PINK) {
                return BarColor.PINK;
            }
            if (color == BossBar.Color.BLUE) {
                return BarColor.BLUE;
            }
            if (color == BossBar.Color.RED) {
                return BarColor.RED;
            }
            if (color == BossBar.Color.GREEN) {
                return BarColor.GREEN;
            }
            if (color == BossBar.Color.YELLOW) {
                return BarColor.YELLOW;
            }
            if (color == BossBar.Color.PURPLE) {
                return BarColor.PURPLE;
            }
            if (color == BossBar.Color.WHITE) {
                return BarColor.WHITE;
            }
            Knob.logUnsupported(this, (Object)color);
            return null;
        }

        @Override
        public void bossBarOverlayChanged(@NotNull net.kyori.adventure.bossbar.BossBar bossBar, @NotNull BossBar.Overlay overlay, @NotNull BossBar.Overlay overlay2) {
            BarStyle barStyle = this.style(overlay2);
            if (barStyle != null) {
                this.bar.setStyle(barStyle);
            }
        }

        @Nullable
        private BarStyle style(@NotNull BossBar.Overlay overlay) {
            if (overlay == BossBar.Overlay.PROGRESS) {
                return BarStyle.SOLID;
            }
            if (overlay == BossBar.Overlay.NOTCHED_6) {
                return BarStyle.SEGMENTED_6;
            }
            if (overlay == BossBar.Overlay.NOTCHED_10) {
                return BarStyle.SEGMENTED_10;
            }
            if (overlay == BossBar.Overlay.NOTCHED_12) {
                return BarStyle.SEGMENTED_12;
            }
            if (overlay == BossBar.Overlay.NOTCHED_20) {
                return BarStyle.SEGMENTED_20;
            }
            Knob.logUnsupported(this, (Object)overlay);
            return null;
        }

        @Override
        public void bossBarFlagsChanged(@NotNull net.kyori.adventure.bossbar.BossBar bossBar, @NotNull Set<BossBar.Flag> set, @NotNull Set<BossBar.Flag> set2) {
            BarFlag barFlag;
            for (BossBar.Flag flag : set2) {
                barFlag = this.flag(flag);
                if (barFlag == null) continue;
                this.bar.removeFlag(barFlag);
            }
            for (BossBar.Flag flag : set) {
                barFlag = this.flag(flag);
                if (barFlag == null) continue;
                this.bar.addFlag(barFlag);
            }
        }

        @Nullable
        private BarFlag flag(@NotNull BossBar.Flag flag) {
            if (flag == BossBar.Flag.DARKEN_SCREEN) {
                return BarFlag.DARKEN_SKY;
            }
            if (flag == BossBar.Flag.PLAY_BOSS_MUSIC) {
                return BarFlag.PLAY_BOSS_MUSIC;
            }
            if (flag == BossBar.Flag.CREATE_WORLD_FOG) {
                return BarFlag.CREATE_FOG;
            }
            Knob.logUnsupported(this, (Object)flag);
            return null;
        }

        @Override
        public void addViewer(@NotNull Player player) {
            this.bar.addPlayer(player);
        }

        @Override
        public void removeViewer(@NotNull Player player) {
            this.bar.removePlayer(player);
        }

        @Override
        public boolean isEmpty() {
            return !this.bar.isVisible() || this.bar.getPlayers().isEmpty();
        }

        @Override
        public void close() {
            this.bar.removeAll();
        }
    }

    static class BossBarBuilder
    extends BukkitFacet<Player>
    implements Facet.BossBar.Builder<Player, BossBar> {
        private static final boolean SUPPORTED = MinecraftReflection.hasClass("org.bukkit.boss.BossBar");

        protected BossBarBuilder() {
            super(Player.class);
        }

        @Override
        public boolean isSupported() {
            return super.isSupported() && SUPPORTED;
        }

        @Override
        public @NotNull BossBar createBossBar(@NotNull Collection<Player> collection) {
            return new BossBar(collection);
        }
    }

    static class SoundWithCategory
    extends Sound {
        private static final boolean SUPPORTED = MinecraftReflection.hasMethod(Player.class, "stopSound", String.class, MinecraftReflection.findClass("org.bukkit.SoundCategory"));

        SoundWithCategory() {
        }

        @Override
        public boolean isSupported() {
            return super.isSupported() && SUPPORTED;
        }

        @Override
        public void playSound(@NotNull Player player, @NotNull net.kyori.adventure.sound.Sound sound, @NotNull Vector vector) {
            SoundCategory soundCategory = this.category(sound.source());
            if (soundCategory == null) {
                super.playSound(player, sound, vector);
            } else {
                String string = SoundWithCategory.name(sound.name());
                player.playSound(vector.toLocation(player.getWorld()), string, soundCategory, sound.volume(), sound.pitch());
            }
        }

        @Override
        public void stopSound(@NotNull Player player, @NotNull SoundStop soundStop) {
            SoundCategory soundCategory = this.category(soundStop.source());
            if (soundCategory == null) {
                super.stopSound(player, soundStop);
            } else {
                String string = SoundWithCategory.name(soundStop.sound());
                player.stopSound(string, soundCategory);
            }
        }

        @Nullable
        private SoundCategory category( @Nullable Sound.Source source) {
            if (source == null) {
                return null;
            }
            if (source == Sound.Source.MASTER) {
                return SoundCategory.MASTER;
            }
            if (source == Sound.Source.MUSIC) {
                return SoundCategory.MUSIC;
            }
            if (source == Sound.Source.RECORD) {
                return SoundCategory.RECORDS;
            }
            if (source == Sound.Source.WEATHER) {
                return SoundCategory.WEATHER;
            }
            if (source == Sound.Source.BLOCK) {
                return SoundCategory.BLOCKS;
            }
            if (source == Sound.Source.HOSTILE) {
                return SoundCategory.HOSTILE;
            }
            if (source == Sound.Source.NEUTRAL) {
                return SoundCategory.NEUTRAL;
            }
            if (source == Sound.Source.PLAYER) {
                return SoundCategory.PLAYERS;
            }
            if (source == Sound.Source.AMBIENT) {
                return SoundCategory.AMBIENT;
            }
            if (source == Sound.Source.VOICE) {
                return SoundCategory.VOICE;
            }
            Knob.logUnsupported(this, (Object)source);
            return null;
        }
    }

    static class Sound
    extends Position
    implements Facet.Sound<Player, Vector> {
        private static final boolean KEY_SUPPORTED = MinecraftReflection.hasClass("org.bukkit.NamespacedKey");
        private static final boolean STOP_SUPPORTED = MinecraftReflection.hasMethod(Player.class, "stopSound", String.class);
        private static final MethodHandle STOP_ALL_SUPPORTED = MinecraftReflection.findMethod(Player.class, "stopAllSounds", Void.TYPE, new Class[0]);

        Sound() {
        }

        @Override
        public void playSound(@NotNull Player player, @NotNull net.kyori.adventure.sound.Sound sound, @NotNull Vector vector) {
            String string = Sound.name(sound.name());
            Location location = vector.toLocation(player.getWorld());
            player.playSound(location, string, sound.volume(), sound.pitch());
        }

        @Override
        public void stopSound(@NotNull Player player, @NotNull SoundStop soundStop) {
            if (STOP_SUPPORTED) {
                String string = Sound.name(soundStop.sound());
                if (string.isEmpty() && STOP_ALL_SUPPORTED != null) {
                    try {
                        STOP_ALL_SUPPORTED.invoke(player);
                    } catch (Throwable throwable) {
                        Knob.logError(throwable, "Could not invoke stopAllSounds on %s", player);
                    }
                    return;
                }
                player.stopSound(string);
            }
        }

        @NotNull
        protected static String name(@Nullable Key key) {
            if (key == null) {
                return "";
            }
            if (KEY_SUPPORTED) {
                return key.asString();
            }
            return key.value();
        }
    }

    static class Position
    extends BukkitFacet<Player>
    implements Facet.Position<Player, Vector> {
        protected Position() {
            super(Player.class);
        }

        @Override
        @NotNull
        public Vector createPosition(@NotNull Player player) {
            return player.getLocation().toVector();
        }

        @Override
        @NotNull
        public Vector createPosition(double d, double d2, double d3) {
            return new Vector(d, d2, d3);
        }
    }

    static class Chat
    extends Message<CommandSender>
    implements Facet.Chat<CommandSender, String> {
        protected Chat() {
            super(CommandSender.class);
        }

        @Override
        public void sendMessage(@NotNull CommandSender commandSender, @NotNull Identity identity, @NotNull String string, @NotNull Object object) {
            commandSender.sendMessage(string);
        }
    }

    static class Message<V extends CommandSender>
    extends BukkitFacet<V>
    implements Facet.Message<V, String> {
        protected Message(@Nullable Class<? extends V> clazz) {
            super(clazz);
        }

        @Override
        @NotNull
        public String createMessage(@NotNull V v, @NotNull Component component) {
            return BukkitComponentSerializer.legacy().serialize(component);
        }
    }
}

