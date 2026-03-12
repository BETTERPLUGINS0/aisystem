package com.badbones69.crazyauctions.api.enums.misc;

import com.badbones69.crazyauctions.CrazyAuctions;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public enum Keys {
   auction_store_id("auction_store_id", PersistentDataType.INTEGER),
   auction_number("auction_number", PersistentDataType.STRING),
   auction_button("auction_button", PersistentDataType.STRING),
   auction_uuid("auction_uuid", PersistentDataType.STRING),
   auction_price("auction_price", PersistentDataType.INTEGER);

   private final CrazyAuctions plugin = CrazyAuctions.get();
   private final String NamespacedKey;
   private final PersistentDataType type;

   private Keys(@NotNull final String param3, @NotNull final PersistentDataType param4) {
      this.NamespacedKey = NamespacedKey;
      this.type = type;
   }

   @NotNull
   public final NamespacedKey getNamespacedKey() {
      CrazyAuctions var10002 = this.plugin;
      String var10003 = this.plugin.getName().toLowerCase();
      return new NamespacedKey(var10002, var10003 + "_" + this.NamespacedKey);
   }

   @NotNull
   public final PersistentDataType getType() {
      return this.type;
   }

   // $FF: synthetic method
   private static Keys[] $values() {
      return new Keys[]{auction_store_id, auction_number, auction_button, auction_uuid, auction_price};
   }
}
