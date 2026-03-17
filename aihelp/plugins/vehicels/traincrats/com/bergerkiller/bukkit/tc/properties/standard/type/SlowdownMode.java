package com.bergerkiller.bukkit.tc.properties.standard.type;

import java.util.Locale;

public enum SlowdownMode {
   FRICTION,
   GRAVITY;

   public final String getKey() {
      return this.name().toLowerCase(Locale.ENGLISH);
   }

   // $FF: synthetic method
   private static SlowdownMode[] $values() {
      return new SlowdownMode[]{FRICTION, GRAVITY};
   }
}
