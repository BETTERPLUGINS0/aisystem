package me.SuperRonanCraft.BetterRTP.references.invs;

import java.io.Serializable;

public enum SETTINGS_TYPE {
   BOOLEAN(Boolean.class),
   STRING(String.class),
   INTEGER(Integer.class);

   private Serializable type;

   private SETTINGS_TYPE(Serializable type) {
      this.type = type;
   }

   Serializable getType() {
      return this.type;
   }

   // $FF: synthetic method
   private static SETTINGS_TYPE[] $values() {
      return new SETTINGS_TYPE[]{BOOLEAN, STRING, INTEGER};
   }
}
