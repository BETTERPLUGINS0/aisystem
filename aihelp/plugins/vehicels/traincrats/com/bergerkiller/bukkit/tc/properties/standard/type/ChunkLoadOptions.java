package com.bergerkiller.bukkit.tc.properties.standard.type;

import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.common.utils.StreamUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public final class ChunkLoadOptions {
   public static final ChunkLoadOptions DEFAULT;
   public static final ChunkLoadOptions LEGACY_FALSE;
   public static final ChunkLoadOptions LEGACY_TRUE;
   private final ChunkLoadOptions.Mode mode;
   private final int radius;

   public static ChunkLoadOptions of(ChunkLoadOptions.Mode mode, int radius) {
      return new ChunkLoadOptions(mode, Math.max(0, radius));
   }

   private ChunkLoadOptions(ChunkLoadOptions.Mode mode, int radius) {
      this.mode = mode;
      this.radius = radius;
   }

   public ChunkLoadOptions.Mode mode() {
      return this.mode;
   }

   public boolean keepLoaded() {
      return this.mode != ChunkLoadOptions.Mode.DISABLED;
   }

   public int radius() {
      return this.radius;
   }

   public ChunkLoadOptions withMode(ChunkLoadOptions.Mode newMode) {
      return of(newMode, this.radius);
   }

   public ChunkLoadOptions withRadius(int newRadius) {
      return of(this.mode, newRadius);
   }

   public int hashCode() {
      return this.radius * 4 + this.mode.ordinal();
   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ChunkLoadOptions)) {
         return false;
      } else {
         ChunkLoadOptions other = (ChunkLoadOptions)o;
         return this.mode == other.mode && this.radius == other.radius;
      }
   }

   public String toString() {
      return "ChunkLoadOptions{keepLoaded=" + this.keepLoaded() + ", mode=" + this.mode().name() + ", radius=" + this.radius() + "}";
   }

   static {
      DEFAULT = new ChunkLoadOptions(ChunkLoadOptions.Mode.DISABLED, 2);
      LEGACY_FALSE = new ChunkLoadOptions(ChunkLoadOptions.Mode.DISABLED, 2);
      LEGACY_TRUE = new ChunkLoadOptions(ChunkLoadOptions.Mode.FULL, 2);
   }

   public static enum Mode {
      DISABLED(Arrays.asList("disabled", "false"), 0),
      FULL(Arrays.asList("full", "true"), 2),
      REDSTONE(Collections.singletonList("redstone"), 1),
      MINIMAL(Collections.singletonList("minimal"), 0);

      private final List<String> names;
      private final int perChunkRadius;
      private static final List<String> allNames = (List)Stream.of(values()).flatMap((m) -> {
         return m.getNames().stream();
      }).collect(StreamUtil.toUnmodifiableList());
      private static final Map<String, ChunkLoadOptions.Mode> byName = new HashMap();

      private Mode(List<String> names, int perChunkRadius) {
         this.names = names;
         this.perChunkRadius = perChunkRadius;
      }

      public int getPerChunkRadius() {
         return this.perChunkRadius;
      }

      public List<String> getNames() {
         return this.names;
      }

      public static List<String> getAllNames() {
         return allNames;
      }

      public static Optional<ChunkLoadOptions.Mode> fromName(String name) {
         ChunkLoadOptions.Mode mode = (ChunkLoadOptions.Mode)byName.get(name);
         if (mode != null) {
            return Optional.of(mode);
         } else {
            mode = (ChunkLoadOptions.Mode)byName.get(name.toUpperCase(Locale.ENGLISH));
            if (mode != null) {
               return Optional.of(mode);
            } else {
               return ParseUtil.isBool(name) ? Optional.of(ParseUtil.parseBool(name) ? FULL : DISABLED) : Optional.empty();
            }
         }
      }

      // $FF: synthetic method
      private static ChunkLoadOptions.Mode[] $values() {
         return new ChunkLoadOptions.Mode[]{DISABLED, FULL, REDSTONE, MINIMAL};
      }

      static {
         ChunkLoadOptions.Mode[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            ChunkLoadOptions.Mode mode = var0[var2];
            Iterator var4 = mode.getNames().iterator();

            while(var4.hasNext()) {
               String name = (String)var4.next();
               byName.put(name, mode);
               byName.put(name.toUpperCase(Locale.ENGLISH), mode);
            }
         }

      }
   }
}
