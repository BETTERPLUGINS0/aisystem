package com.nisovin.shopkeepers.util.inventory;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import java.util.Collections;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class ItemSerialization {
   private static final String ITEM_META_SERIALIZATION_KEY = "ItemMeta";

   @Nullable
   public static Map<? extends String, ?> serializeItemMeta(@Nullable @ReadOnly ItemMeta itemMeta) {
      if (!Bukkit.getItemFactory().equals(itemMeta, (ItemMeta)null)) {
         assert itemMeta != null;

         return (Map)Unsafe.cast(itemMeta.serialize());
      } else {
         return null;
      }
   }

   public static Map<? extends String, ?> serializeItemMetaOrEmpty(@ReadOnly ItemMeta itemMeta) {
      Map<? extends String, ?> serializedItemMeta = serializeItemMeta(itemMeta);
      return serializedItemMeta != null ? serializedItemMeta : Collections.emptyMap();
   }

   @Nullable
   public static ItemMeta deserializeItemMeta(@Nullable @ReadOnly Map<? extends String, ?> itemMetaData) {
      if (itemMetaData == null) {
         return null;
      } else {
         Class<? extends ConfigurationSerializable> serializableItemMetaClass = ConfigurationSerialization.getClassByAlias("ItemMeta");
         if (serializableItemMetaClass == null) {
            throw new IllegalStateException("Missing ItemMeta ConfigurationSerializable class for key/alias 'ItemMeta'!");
         } else {
            ItemMeta itemMeta = (ItemMeta)ConfigurationSerialization.deserializeObject((Map)Unsafe.cast(itemMetaData), serializableItemMetaClass);
            return itemMeta;
         }
      }
   }

   private ItemSerialization() {
   }
}
