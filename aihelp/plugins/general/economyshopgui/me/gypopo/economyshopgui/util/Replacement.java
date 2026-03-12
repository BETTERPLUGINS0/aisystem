package me.gypopo.economyshopgui.util;

import java.util.function.Supplier;

public class Replacement {
   public String key;
   public Supplier<String> value;

   public Replacement(String key, Supplier<String> value) {
      this.key = key;
      this.value = value;
   }
}
