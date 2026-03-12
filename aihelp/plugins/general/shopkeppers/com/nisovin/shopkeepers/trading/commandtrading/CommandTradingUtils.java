package com.nisovin.shopkeepers.trading.commandtrading;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import com.nisovin.shopkeepers.util.annotations.ReadWrite;
import com.nisovin.shopkeepers.util.bukkit.NamespacedKeyUtils;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class CommandTradingUtils {
   private static final NamespacedKey KEY_TRADED_COMMAND = NamespacedKeyUtils.create("shopkeepers", "traded_command");

   public static void setTradedCommand(@ReadWrite ItemStack itemStack, @Nullable String tradedCommand) {
      Validate.isTrue(!ItemUtils.isEmpty(itemStack), "itemStack is empty");

      assert itemStack != null;

      ItemMeta meta = (ItemMeta)Unsafe.assertNonNull(itemStack.getItemMeta());
      PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
      if (StringUtils.isEmpty(tradedCommand)) {
         dataContainer.remove(KEY_TRADED_COMMAND);
      } else {
         dataContainer.set(KEY_TRADED_COMMAND, (PersistentDataType)Unsafe.assertNonNull(PersistentDataType.STRING), (String)Unsafe.assertNonNull(tradedCommand));
      }

      itemStack.setItemMeta(meta);
   }

   public static String getTradedCommand(@ReadOnly ItemStack itemStack) {
      if (ItemUtils.isEmpty(itemStack)) {
         return null;
      } else {
         assert itemStack != null;

         ItemMeta meta = (ItemMeta)Unsafe.assertNonNull(itemStack.getItemMeta());
         PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
         String tradedCommand = (String)dataContainer.get(KEY_TRADED_COMMAND, (PersistentDataType)Unsafe.assertNonNull(PersistentDataType.STRING));
         return StringUtils.getNotEmpty(tradedCommand);
      }
   }

   @Nullable
   public static String getTradedCommand(@Nullable UnmodifiableItemStack itemStack) {
      return getTradedCommand(ItemUtils.asItemStackOrNull(itemStack));
   }
}
