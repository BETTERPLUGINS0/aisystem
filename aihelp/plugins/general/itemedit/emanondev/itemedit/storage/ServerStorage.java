package emanondev.itemedit.storage;

import emanondev.itemedit.ItemEdit;
import emanondev.itemedit.UtilsString;
import emanondev.itemedit.utility.ItemUtils;
import java.util.Set;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ServerStorage {
   @Nullable
   ItemStack getItem(@NotNull String var1);

   @Nullable
   String getNick(@NotNull String var1);

   void setItem(@NotNull String var1, @NotNull ItemStack var2);

   void setNick(@NotNull String var1, @Nullable String var2);

   void remove(@NotNull String var1);

   void clear();

   @NotNull
   Set<String> getIds();

   default void validateID(@Nullable String id) {
      if (id == null || id.contains(" ") || id.contains(".") || id.isEmpty()) {
         throw new IllegalArgumentException();
      }
   }

   @Nullable
   default ItemStack getItem(@NotNull String id, @Nullable Player player) {
      ItemStack item = this.getItem(id);
      if (item != null && player != null) {
         if (item.hasItemMeta() && ItemEdit.get().getConfig().loadBoolean("serveritem.replace-holders", true)) {
            String[] holders = new String[]{"%player_name%", player.getName(), "%player_uuid%", player.getUniqueId().toString()};
            ItemMeta meta = ItemUtils.getMeta(item);
            meta.setDisplayName(UtilsString.fix(meta.getDisplayName(), player, true, holders));
            meta.setLore(UtilsString.fix(meta.getLore(), player, true, holders));
            item.setItemMeta(meta);
         }

         return item;
      } else {
         return item;
      }
   }

   default boolean contains(@Nullable ItemStack item) {
      return this.getId(item) != null;
   }

   @Nullable
   String getId(@Nullable ItemStack var1);

   void reload();
}
