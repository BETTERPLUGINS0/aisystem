package fr.xephi.authme.libs.net.kyori.adventure.platform.bukkit;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import fr.xephi.authme.libs.net.kyori.adventure.identity.Identity;
import fr.xephi.authme.libs.net.kyori.adventure.key.Key;
import fr.xephi.authme.libs.net.kyori.adventure.permission.PermissionChecker;
import fr.xephi.authme.libs.net.kyori.adventure.platform.facet.Facet;
import fr.xephi.authme.libs.net.kyori.adventure.platform.facet.FacetBase;
import fr.xephi.authme.libs.net.kyori.adventure.platform.facet.FacetPointers;
import fr.xephi.authme.libs.net.kyori.adventure.platform.facet.Knob;
import fr.xephi.authme.libs.net.kyori.adventure.pointer.Pointer;
import fr.xephi.authme.libs.net.kyori.adventure.sound.SoundStop;
import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.translation.Translator;
import fr.xephi.authme.libs.net.kyori.adventure.util.TriState;
import java.lang.invoke.MethodHandle;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
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

class BukkitFacet<V extends CommandSender> extends FacetBase<V> {
   protected BukkitFacet(@Nullable final Class<? extends V> viewerClass) {
      super(viewerClass);
   }

   static final class PlayerPointers extends BukkitFacet<Player> implements Facet.Pointers<Player> {
      private static final MethodHandle LOCALE_SUPPORTED = MinecraftReflection.findMethod(Player.class, "getLocale", String.class);

      PlayerPointers() {
         super(Player.class);
      }

      public void contributePointers(final Player viewer, final fr.xephi.authme.libs.net.kyori.adventure.pointer.Pointers.Builder builder) {
         Pointer var10001 = Identity.UUID;
         Objects.requireNonNull(viewer);
         builder.withDynamic(var10001, viewer::getUniqueId);
         builder.withDynamic(Identity.DISPLAY_NAME, () -> {
            return BukkitComponentSerializer.legacy().deserializeOrNull(viewer.getDisplayName());
         });
         builder.withDynamic(Identity.LOCALE, () -> {
            if (LOCALE_SUPPORTED != null) {
               try {
                  return Translator.parseLocale(LOCALE_SUPPORTED.invoke(viewer));
               } catch (Throwable var2) {
                  Knob.logError(var2, "Failed to call getLocale() for %s", viewer);
               }
            }

            return Locale.getDefault();
         });
         builder.withStatic(FacetPointers.TYPE, FacetPointers.Type.PLAYER);
         builder.withDynamic(FacetPointers.WORLD, () -> {
            return Key.key(viewer.getWorld().getName());
         });
      }
   }

   static final class ConsoleCommandSenderPointers extends BukkitFacet<ConsoleCommandSender> implements Facet.Pointers<ConsoleCommandSender> {
      ConsoleCommandSenderPointers() {
         super(ConsoleCommandSender.class);
      }

      public void contributePointers(final ConsoleCommandSender viewer, final fr.xephi.authme.libs.net.kyori.adventure.pointer.Pointers.Builder builder) {
         builder.withStatic(FacetPointers.TYPE, FacetPointers.Type.CONSOLE);
      }
   }

   static final class CommandSenderPointers extends BukkitFacet<CommandSender> implements Facet.Pointers<CommandSender> {
      CommandSenderPointers() {
         super(CommandSender.class);
      }

      public void contributePointers(final CommandSender viewer, final fr.xephi.authme.libs.net.kyori.adventure.pointer.Pointers.Builder builder) {
         Pointer var10001 = Identity.NAME;
         Objects.requireNonNull(viewer);
         builder.withDynamic(var10001, viewer::getName);
         builder.withStatic(PermissionChecker.POINTER, (perm) -> {
            if (viewer.isPermissionSet(perm)) {
               return viewer.hasPermission(perm) ? TriState.TRUE : TriState.FALSE;
            } else {
               return TriState.NOT_SET;
            }
         });
      }
   }

   static final class TabList extends BukkitFacet.Message<Player> implements Facet.TabList<Player, String> {
      private static final boolean SUPPORTED = MinecraftReflection.hasMethod(Player.class, "setPlayerListHeader", String.class);

      TabList() {
         super(Player.class);
      }

      public boolean isSupported() {
         return SUPPORTED && super.isSupported();
      }

      public void send(final Player viewer, @Nullable final String header, @Nullable final String footer) {
         if (header != null && footer != null) {
            viewer.setPlayerListHeaderFooter(header, footer);
         } else if (header != null) {
            viewer.setPlayerListHeader(header);
         } else if (footer != null) {
            viewer.setPlayerListFooter(footer);
         }

      }
   }

   static final class ViaHook implements Function<Player, UserConnection> {
      public UserConnection apply(@NotNull final Player player) {
         return Via.getManager().getConnectionManager().getConnectedClient(player.getUniqueId());
      }
   }

   static class BossBar extends BukkitFacet.Message<Player> implements Facet.BossBar<Player> {
      protected final org.bukkit.boss.BossBar bar;

      protected BossBar(@NotNull final Collection<Player> viewers) {
         super(Player.class);
         this.bar = Bukkit.createBossBar("", BarColor.PINK, BarStyle.SOLID, new BarFlag[0]);
         this.bar.setVisible(false);
         Iterator var2 = viewers.iterator();

         while(var2.hasNext()) {
            Player viewer = (Player)var2.next();
            this.bar.addPlayer(viewer);
         }

      }

      public void bossBarInitialized(@NotNull final fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar bar) {
         Facet.BossBar.super.bossBarInitialized(bar);
         this.bar.setVisible(true);
      }

      public void bossBarNameChanged(@NotNull final fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar bar, @NotNull final Component oldName, @NotNull final Component newName) {
         if (!this.bar.getPlayers().isEmpty()) {
            this.bar.setTitle(this.createMessage((Player)this.bar.getPlayers().get(0), newName));
         }

      }

      public void bossBarProgressChanged(@NotNull final fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar bar, final float oldPercent, final float newPercent) {
         this.bar.setProgress((double)newPercent);
      }

      public void bossBarColorChanged(@NotNull final fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar bar, @NotNull final fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Color oldColor, @NotNull final fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Color newColor) {
         BarColor color = this.color(newColor);
         if (color != null) {
            this.bar.setColor(color);
         }

      }

      @Nullable
      private BarColor color(@NotNull final fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Color color) {
         if (color == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Color.PINK) {
            return BarColor.PINK;
         } else if (color == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Color.BLUE) {
            return BarColor.BLUE;
         } else if (color == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Color.RED) {
            return BarColor.RED;
         } else if (color == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Color.GREEN) {
            return BarColor.GREEN;
         } else if (color == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Color.YELLOW) {
            return BarColor.YELLOW;
         } else if (color == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Color.PURPLE) {
            return BarColor.PURPLE;
         } else if (color == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Color.WHITE) {
            return BarColor.WHITE;
         } else {
            Knob.logUnsupported(this, color);
            return null;
         }
      }

      public void bossBarOverlayChanged(@NotNull final fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar bar, @NotNull final fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Overlay oldOverlay, @NotNull final fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Overlay newOverlay) {
         BarStyle style = this.style(newOverlay);
         if (style != null) {
            this.bar.setStyle(style);
         }

      }

      @Nullable
      private BarStyle style(@NotNull final fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Overlay overlay) {
         if (overlay == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Overlay.PROGRESS) {
            return BarStyle.SOLID;
         } else if (overlay == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Overlay.NOTCHED_6) {
            return BarStyle.SEGMENTED_6;
         } else if (overlay == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Overlay.NOTCHED_10) {
            return BarStyle.SEGMENTED_10;
         } else if (overlay == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Overlay.NOTCHED_12) {
            return BarStyle.SEGMENTED_12;
         } else if (overlay == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Overlay.NOTCHED_20) {
            return BarStyle.SEGMENTED_20;
         } else {
            Knob.logUnsupported(this, overlay);
            return null;
         }
      }

      public void bossBarFlagsChanged(@NotNull final fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar bar, @NotNull final Set<fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Flag> flagsAdded, @NotNull final Set<fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Flag> flagsRemoved) {
         Iterator var4 = flagsRemoved.iterator();

         fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Flag addFlag;
         BarFlag flag;
         while(var4.hasNext()) {
            addFlag = (fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Flag)var4.next();
            flag = this.flag(addFlag);
            if (flag != null) {
               this.bar.removeFlag(flag);
            }
         }

         var4 = flagsAdded.iterator();

         while(var4.hasNext()) {
            addFlag = (fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Flag)var4.next();
            flag = this.flag(addFlag);
            if (flag != null) {
               this.bar.addFlag(flag);
            }
         }

      }

      @Nullable
      private BarFlag flag(@NotNull final fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Flag flag) {
         if (flag == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Flag.DARKEN_SCREEN) {
            return BarFlag.DARKEN_SKY;
         } else if (flag == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Flag.PLAY_BOSS_MUSIC) {
            return BarFlag.PLAY_BOSS_MUSIC;
         } else if (flag == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Flag.CREATE_WORLD_FOG) {
            return BarFlag.CREATE_FOG;
         } else {
            Knob.logUnsupported(this, flag);
            return null;
         }
      }

      public void addViewer(@NotNull final Player viewer) {
         this.bar.addPlayer(viewer);
      }

      public void removeViewer(@NotNull final Player viewer) {
         this.bar.removePlayer(viewer);
      }

      public boolean isEmpty() {
         return !this.bar.isVisible() || this.bar.getPlayers().isEmpty();
      }

      public void close() {
         this.bar.removeAll();
      }
   }

   static class BossBarBuilder extends BukkitFacet<Player> implements Facet.BossBar.Builder<Player, BukkitFacet.BossBar> {
      private static final boolean SUPPORTED = MinecraftReflection.hasClass("org.bukkit.boss.BossBar");

      protected BossBarBuilder() {
         super(Player.class);
      }

      public boolean isSupported() {
         return super.isSupported() && SUPPORTED;
      }

      @NotNull
      public BukkitFacet.BossBar createBossBar(@NotNull final Collection<Player> viewers) {
         return new BukkitFacet.BossBar(viewers);
      }
   }

   static class SoundWithCategory extends BukkitFacet.Sound {
      private static final boolean SUPPORTED = MinecraftReflection.hasMethod(Player.class, "stopSound", String.class, MinecraftReflection.findClass("org.bukkit.SoundCategory"));

      public boolean isSupported() {
         return super.isSupported() && SUPPORTED;
      }

      public void playSound(@NotNull final Player viewer, @NotNull final fr.xephi.authme.libs.net.kyori.adventure.sound.Sound sound, @NotNull final Vector vector) {
         SoundCategory category = this.category(sound.source());
         if (category == null) {
            super.playSound(viewer, sound, vector);
         } else {
            String name = name(sound.name());
            viewer.playSound(vector.toLocation(viewer.getWorld()), name, category, sound.volume(), sound.pitch());
         }

      }

      public void stopSound(@NotNull final Player viewer, @NotNull final SoundStop stop) {
         SoundCategory category = this.category(stop.source());
         if (category == null) {
            super.stopSound(viewer, stop);
         } else {
            String name = name(stop.sound());
            viewer.stopSound(name, category);
         }

      }

      @Nullable
      private SoundCategory category(@Nullable final fr.xephi.authme.libs.net.kyori.adventure.sound.Sound.Source source) {
         if (source == null) {
            return null;
         } else if (source == fr.xephi.authme.libs.net.kyori.adventure.sound.Sound.Source.MASTER) {
            return SoundCategory.MASTER;
         } else if (source == fr.xephi.authme.libs.net.kyori.adventure.sound.Sound.Source.MUSIC) {
            return SoundCategory.MUSIC;
         } else if (source == fr.xephi.authme.libs.net.kyori.adventure.sound.Sound.Source.RECORD) {
            return SoundCategory.RECORDS;
         } else if (source == fr.xephi.authme.libs.net.kyori.adventure.sound.Sound.Source.WEATHER) {
            return SoundCategory.WEATHER;
         } else if (source == fr.xephi.authme.libs.net.kyori.adventure.sound.Sound.Source.BLOCK) {
            return SoundCategory.BLOCKS;
         } else if (source == fr.xephi.authme.libs.net.kyori.adventure.sound.Sound.Source.HOSTILE) {
            return SoundCategory.HOSTILE;
         } else if (source == fr.xephi.authme.libs.net.kyori.adventure.sound.Sound.Source.NEUTRAL) {
            return SoundCategory.NEUTRAL;
         } else if (source == fr.xephi.authme.libs.net.kyori.adventure.sound.Sound.Source.PLAYER) {
            return SoundCategory.PLAYERS;
         } else if (source == fr.xephi.authme.libs.net.kyori.adventure.sound.Sound.Source.AMBIENT) {
            return SoundCategory.AMBIENT;
         } else if (source == fr.xephi.authme.libs.net.kyori.adventure.sound.Sound.Source.VOICE) {
            return SoundCategory.VOICE;
         } else {
            Knob.logUnsupported(this, source);
            return null;
         }
      }
   }

   static class Sound extends BukkitFacet.Position implements Facet.Sound<Player, Vector> {
      private static final boolean KEY_SUPPORTED = MinecraftReflection.hasClass("org.bukkit.NamespacedKey");
      private static final boolean STOP_SUPPORTED = MinecraftReflection.hasMethod(Player.class, "stopSound", String.class);
      private static final MethodHandle STOP_ALL_SUPPORTED;

      public void playSound(@NotNull final Player viewer, @NotNull final fr.xephi.authme.libs.net.kyori.adventure.sound.Sound sound, @NotNull final Vector vector) {
         String name = name(sound.name());
         Location location = vector.toLocation(viewer.getWorld());
         viewer.playSound(location, name, sound.volume(), sound.pitch());
      }

      public void stopSound(@NotNull final Player viewer, @NotNull final SoundStop stop) {
         if (STOP_SUPPORTED) {
            String name = name(stop.sound());
            if (name.isEmpty() && STOP_ALL_SUPPORTED != null) {
               try {
                  STOP_ALL_SUPPORTED.invoke(viewer);
               } catch (Throwable var5) {
                  Knob.logError(var5, "Could not invoke stopAllSounds on %s", viewer);
               }

               return;
            }

            viewer.stopSound(name);
         }

      }

      @NotNull
      protected static String name(@Nullable final Key name) {
         if (name == null) {
            return "";
         } else {
            return KEY_SUPPORTED ? name.asString() : name.value();
         }
      }

      static {
         STOP_ALL_SUPPORTED = MinecraftReflection.findMethod(Player.class, "stopAllSounds", Void.TYPE);
      }
   }

   static class Position extends BukkitFacet<Player> implements Facet.Position<Player, Vector> {
      protected Position() {
         super(Player.class);
      }

      @NotNull
      public Vector createPosition(@NotNull final Player viewer) {
         return viewer.getLocation().toVector();
      }

      @NotNull
      public Vector createPosition(final double x, final double y, final double z) {
         return new Vector(x, y, z);
      }
   }

   static class Chat extends BukkitFacet.Message<CommandSender> implements Facet.Chat<CommandSender, String> {
      protected Chat() {
         super(CommandSender.class);
      }

      public void sendMessage(@NotNull final CommandSender viewer, @NotNull final Identity source, @NotNull final String message, @NotNull final Object type) {
         viewer.sendMessage(message);
      }
   }

   static class Message<V extends CommandSender> extends BukkitFacet<V> implements Facet.Message<V, String> {
      protected Message(@Nullable final Class<? extends V> viewerClass) {
         super(viewerClass);
      }

      @NotNull
      public String createMessage(@NotNull final V viewer, @NotNull final Component message) {
         return BukkitComponentSerializer.legacy().serialize(message);
      }
   }
}
