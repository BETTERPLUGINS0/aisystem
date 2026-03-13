package com.nisovin.shopkeepers.tradelog;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.util.bukkit.RegistryUtils;
import com.nisovin.shopkeepers.util.yaml.YamlUtils;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public class TradeLogUtils {
   @Nullable
   private static Boolean USES_NEW_PAPER_FORMAT = null;

   private static boolean usesNewPaperFormat() {
      if (USES_NEW_PAPER_FORMAT == null) {
         ItemStack itemStack = new ItemStack(Material.STONE, 1);
         Map<String, Object> serialized = itemStack.serialize();
         USES_NEW_PAPER_FORMAT = serialized.containsKey("id");
      }

      assert USES_NEW_PAPER_FORMAT != null;

      return USES_NEW_PAPER_FORMAT;
   }

   public static String getItemMetadata(UnmodifiableItemStack itemStack) {
      assert itemStack != null;

      Map<String, Object> itemData = itemStack.serialize();
      itemData.remove("type");
      itemData.remove("amount");
      itemData.remove("id");
      itemData.remove("count");
      String yaml = YamlUtils.toCompactYaml(itemData);
      return yaml;
   }

   public static ItemStack loadItemStack(Material itemType, int amount, @Nullable String metadata) {
      ItemStack itemStack = new ItemStack(itemType, amount);
      if (metadata != null && !metadata.isEmpty()) {
         Map<String, Object> itemData = (Map)Unsafe.castNonNull(YamlUtils.fromYaml(metadata));
         if (usesNewPaperFormat()) {
            itemData.put("id", RegistryUtils.getKeyOrThrow(itemType).toString());
            itemData.put("count", amount);
         } else {
            itemData.put("type", itemType.name());
            itemData.put("amount", amount);
         }

         return ItemStack.deserialize(itemData);
      } else {
         return itemStack;
      }
   }

   private TradeLogUtils() {
   }
}
