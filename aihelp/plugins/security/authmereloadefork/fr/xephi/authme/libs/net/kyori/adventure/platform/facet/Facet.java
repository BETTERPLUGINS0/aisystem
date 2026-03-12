package fr.xephi.authme.libs.net.kyori.adventure.platform.facet;

import fr.xephi.authme.libs.net.kyori.adventure.audience.MessageType;
import fr.xephi.authme.libs.net.kyori.adventure.identity.Identity;
import fr.xephi.authme.libs.net.kyori.adventure.sound.SoundStop;
import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import java.io.Closeable;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public interface Facet<V> {
   @SafeVarargs
   @NotNull
   static <V, F extends Facet<? extends V>> Collection<F> of(@NotNull final Supplier<F>... suppliers) {
      List<F> facets = new LinkedList();
      Supplier[] var2 = suppliers;
      int var3 = suppliers.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Supplier supplier = var2[var4];

         Facet facet;
         try {
            facet = (Facet)supplier.get();
         } catch (NoClassDefFoundError var8) {
            Knob.logMessage("Skipped facet: %s", supplier.getClass().getName());
            continue;
         } catch (Throwable var9) {
            Knob.logError(var9, "Failed facet: %s", supplier);
            continue;
         }

         if (!facet.isSupported()) {
            Knob.logMessage("Skipped facet: %s", facet);
         } else {
            facets.add(facet);
            Knob.logMessage("Added facet: %s", facet);
         }
      }

      return facets;
   }

   @Nullable
   static <V, F extends Facet<V>> F of(@Nullable final Collection<F> facets, @Nullable final V viewer) {
      if (facets != null && viewer != null) {
         Iterator var2 = facets.iterator();

         while(var2.hasNext()) {
            Facet facet = (Facet)var2.next();

            try {
               if (facet.isApplicable(viewer)) {
                  Knob.logMessage("Selected facet: %s for %s", facet, viewer);
                  return facet;
               }

               if (Knob.DEBUG) {
                  Knob.logMessage("Not selecting %s for %s", facet, viewer);
               }
            } catch (ClassCastException var5) {
               if (Knob.DEBUG) {
                  Knob.logMessage("Exception while getting facet %s for %s: %s", facet, viewer, var5.getMessage());
               }
            }
         }

         return null;
      } else {
         return null;
      }
   }

   default boolean isSupported() {
      return true;
   }

   default boolean isApplicable(@NotNull final V viewer) {
      return true;
   }

   public interface Pointers<V> extends Facet<V> {
      void contributePointers(final V viewer, final fr.xephi.authme.libs.net.kyori.adventure.pointer.Pointers.Builder builder);
   }

   public interface TabList<V, M> extends Facet.Message<V, M> {
      void send(final V viewer, @Nullable final M header, @Nullable final M footer);
   }

   public interface FakeEntity<V, P> extends Facet.Position<V, P>, Closeable {
      void teleport(@NotNull final V viewer, @Nullable final P position);

      void metadata(final int position, @NotNull final Object data);

      void invisible(final boolean invisible);

      void health(final float health);

      void name(@NotNull final Component name);

      void close();
   }

   public interface BossBarEntity<V, P> extends Facet.BossBar<V>, Facet.FakeEntity<V, P> {
      int OFFSET_PITCH = 30;
      int OFFSET_YAW = 0;
      int OFFSET_MAGNITUDE = 40;
      int INVULNERABLE_KEY = 20;
      int INVULNERABLE_TICKS = 890;

      default void bossBarProgressChanged(@NotNull final fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar bar, final float oldProgress, final float newProgress) {
         this.health(newProgress);
      }

      default void bossBarNameChanged(@NotNull final fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar bar, @NotNull final Component oldName, @NotNull final Component newName) {
         this.name(newName);
      }

      default void addViewer(@NotNull final V viewer) {
         this.teleport(viewer, this.createPosition(viewer));
      }

      default void removeViewer(@NotNull final V viewer) {
         this.teleport(viewer, (Object)null);
      }
   }

   public interface BossBarPacket<V> extends Facet.BossBar<V> {
      int ACTION_ADD = 0;
      int ACTION_REMOVE = 1;
      int ACTION_HEALTH = 2;
      int ACTION_TITLE = 3;
      int ACTION_STYLE = 4;
      int ACTION_FLAG = 5;

      default int createColor(@NotNull final fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Color color) {
         if (color == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Color.PURPLE) {
            return 5;
         } else if (color == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Color.PINK) {
            return 0;
         } else if (color == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Color.BLUE) {
            return 1;
         } else if (color == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Color.RED) {
            return 2;
         } else if (color == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Color.GREEN) {
            return 3;
         } else if (color == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Color.YELLOW) {
            return 4;
         } else if (color == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Color.WHITE) {
            return 6;
         } else {
            Knob.logUnsupported(this, color);
            return 5;
         }
      }

      default int createOverlay(@NotNull final fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Overlay overlay) {
         if (overlay == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Overlay.PROGRESS) {
            return 0;
         } else if (overlay == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Overlay.NOTCHED_6) {
            return 1;
         } else if (overlay == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Overlay.NOTCHED_10) {
            return 2;
         } else if (overlay == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Overlay.NOTCHED_12) {
            return 3;
         } else if (overlay == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Overlay.NOTCHED_20) {
            return 4;
         } else {
            Knob.logUnsupported(this, overlay);
            return 0;
         }
      }

      default byte createFlag(final byte flagBit, @NotNull final Set<fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Flag> flagsAdded, @NotNull final Set<fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Flag> flagsRemoved) {
         byte bit = flagBit;
         Iterator var5 = flagsAdded.iterator();

         fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Flag flag;
         while(var5.hasNext()) {
            flag = (fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Flag)var5.next();
            if (flag == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Flag.DARKEN_SCREEN) {
               bit = (byte)(bit | 1);
            } else if (flag == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Flag.PLAY_BOSS_MUSIC) {
               bit = (byte)(bit | 2);
            } else if (flag == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Flag.CREATE_WORLD_FOG) {
               bit = (byte)(bit | 4);
            } else {
               Knob.logUnsupported(this, flag);
            }
         }

         var5 = flagsRemoved.iterator();

         while(var5.hasNext()) {
            flag = (fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Flag)var5.next();
            if (flag == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Flag.DARKEN_SCREEN) {
               bit &= -2;
            } else if (flag == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Flag.PLAY_BOSS_MUSIC) {
               bit &= -3;
            } else if (flag == fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Flag.CREATE_WORLD_FOG) {
               bit &= -5;
            } else {
               Knob.logUnsupported(this, flag);
            }
         }

         return bit;
      }
   }

   public interface BossBar<V> extends fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar.Listener, Closeable {
      int PROTOCOL_BOSS_BAR = 356;

      default void bossBarInitialized(@NotNull final fr.xephi.authme.libs.net.kyori.adventure.bossbar.BossBar bar) {
         this.bossBarNameChanged(bar, bar.name(), bar.name());
         this.bossBarColorChanged(bar, bar.color(), bar.color());
         this.bossBarProgressChanged(bar, bar.progress(), bar.progress());
         this.bossBarFlagsChanged(bar, bar.flags(), Collections.emptySet());
         this.bossBarOverlayChanged(bar, bar.overlay(), bar.overlay());
      }

      void addViewer(@NotNull final V viewer);

      void removeViewer(@NotNull final V viewer);

      boolean isEmpty();

      void close();

      @FunctionalInterface
      public interface Builder<V, B extends Facet.BossBar<V>> extends Facet<V> {
         @NotNull
         B createBossBar(@NotNull final Collection<V> viewer);
      }
   }

   public interface Book<V, M, B> extends Facet.Message<V, M> {
      @Nullable
      B createBook(@NotNull final String title, @NotNull final String author, @NotNull final Iterable<M> pages);

      void openBook(@NotNull final V viewer, @NotNull final B book);
   }

   public interface EntitySound<V, M> extends Facet<V> {
      M createForSelf(final V viewer, @NotNull final fr.xephi.authme.libs.net.kyori.adventure.sound.Sound sound);

      M createForEmitter(@NotNull final fr.xephi.authme.libs.net.kyori.adventure.sound.Sound sound, @NotNull final fr.xephi.authme.libs.net.kyori.adventure.sound.Sound.Emitter emitter);

      void playSound(@NotNull final V viewer, final M message);
   }

   public interface Sound<V, P> extends Facet.Position<V, P> {
      void playSound(@NotNull final V viewer, @NotNull final fr.xephi.authme.libs.net.kyori.adventure.sound.Sound sound, @NotNull final P position);

      void stopSound(@NotNull final V viewer, @NotNull final SoundStop sound);
   }

   public interface Position<V, P> extends Facet<V> {
      @Nullable
      P createPosition(@NotNull final V viewer);

      @NotNull
      P createPosition(final double x, final double y, final double z);
   }

   public interface TitlePacket<V, M, C, T> extends Facet.Title<V, M, C, T> {
      int ACTION_TITLE = 0;
      int ACTION_SUBTITLE = 1;
      int ACTION_ACTIONBAR = 2;
      int ACTION_TIMES = 3;
      int ACTION_CLEAR = 4;
      int ACTION_RESET = 5;
   }

   public interface Title<V, M, C, T> extends Facet.Message<V, M> {
      int PROTOCOL_ACTION_BAR = 310;
      long MAX_SECONDS = 461168601842738790L;

      @NotNull
      C createTitleCollection();

      void contributeTitle(@NotNull final C coll, @NotNull final M title);

      void contributeSubtitle(@NotNull final C coll, @NotNull final M subtitle);

      void contributeTimes(@NotNull final C coll, final int inTicks, final int stayTicks, final int outTicks);

      @Nullable
      T completeTitle(@NotNull final C coll);

      void showTitle(@NotNull final V viewer, @NotNull final T title);

      void clearTitle(@NotNull final V viewer);

      void resetTitle(@NotNull final V viewer);

      default int toTicks(@Nullable final Duration duration) {
         if (duration != null && !duration.isNegative()) {
            return duration.getSeconds() > 461168601842738790L ? Integer.MAX_VALUE : (int)(duration.getSeconds() * 20L + (long)(duration.getNano() / 50000000));
         } else {
            return -1;
         }
      }
   }

   public interface ActionBar<V, M> extends Facet.Message<V, M> {
      void sendMessage(@NotNull final V viewer, @NotNull final M message);
   }

   public interface ChatPacket<V, M> extends Facet.Chat<V, M> {
      byte TYPE_CHAT = 0;
      byte TYPE_SYSTEM = 1;
      byte TYPE_ACTION_BAR = 2;

      default byte createMessageType(@NotNull final MessageType type) {
         if (type == MessageType.CHAT) {
            return 0;
         } else if (type == MessageType.SYSTEM) {
            return 1;
         } else {
            Knob.logUnsupported(this, type);
            return 0;
         }
      }
   }

   public interface Chat<V, M> extends Facet.Message<V, M> {
      void sendMessage(@NotNull final V viewer, @NotNull final Identity source, @NotNull final M message, @NotNull final Object type);
   }

   public interface Message<V, M> extends Facet<V> {
      int PROTOCOL_HEX_COLOR = 713;
      int PROTOCOL_JSON = 5;

      @Nullable
      M createMessage(@NotNull final V viewer, @NotNull final Component message);
   }
}
