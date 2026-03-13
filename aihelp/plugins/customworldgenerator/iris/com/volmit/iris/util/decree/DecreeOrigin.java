package com.volmit.iris.util.decree;

import com.volmit.iris.util.plugin.VolmitSender;

public enum DecreeOrigin {
   PLAYER,
   CONSOLE,
   BOTH;

   public boolean validFor(VolmitSender sender) {
      if (var1.isPlayer()) {
         return this.equals(PLAYER) || this.equals(BOTH);
      } else {
         return this.equals(CONSOLE) || this.equals(BOTH);
      }
   }

   // $FF: synthetic method
   private static DecreeOrigin[] $values() {
      return new DecreeOrigin[]{PLAYER, CONSOLE, BOTH};
   }
}
