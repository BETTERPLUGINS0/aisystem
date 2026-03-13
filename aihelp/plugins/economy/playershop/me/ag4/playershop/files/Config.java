package me.ag4.playershop.files;

import me.ag4.playershop.PlayerShop;
import me.ag4.playershop.Utils;

public enum Config {
   HOLOGRAM_FIRST_LINE("Hologram.First-Line"),
   HOLOGRAM_SECOND_LINE("Hologram.Second-Line"),
   IS_WORLDGUARD_ENABLE("WorldGuard.Enable");

   private final String key;

   private Config(String param3) {
      this.key = key;
   }

   public String toString() {
      return Utils.hex(PlayerShop.getInstance().config().getString(this.key));
   }

   public Integer toInteger() {
      return PlayerShop.getInstance().config().getInt(this.key);
   }

   public Boolean toBoolean() {
      return PlayerShop.getInstance().config().getBoolean(this.key);
   }

   // $FF: synthetic method
   private static Config[] $values() {
      return new Config[]{HOLOGRAM_FIRST_LINE, HOLOGRAM_SECOND_LINE, IS_WORLDGUARD_ENABLE};
   }
}
