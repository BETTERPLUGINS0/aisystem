package emanondev.itemedit.gui;

import emanondev.itemedit.APlugin;
import emanondev.itemedit.YMLConfig;
import emanondev.itemedit.utility.ItemUtils;
import java.util.Collections;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Gui extends InventoryHolder {
   void onClose(InventoryCloseEvent var1);

   void onClick(InventoryClickEvent var1);

   void onDrag(InventoryDragEvent var1);

   void onOpen(InventoryOpenEvent var1);

   @NotNull
   Inventory getInventory();

   Player getTargetPlayer();

   @NotNull
   APlugin getPlugin();

   default String getLanguageMessage(@NotNull String fullPath, String... holders) {
      return this.getPlugin().getLanguageConfig(this.getTargetPlayer()).loadMessage(fullPath, "", (Player)null, true, holders);
   }

   default List<String> getLanguageMultiMessage(@NotNull String fullPath, String... holders) {
      return this.getPlugin().getLanguageConfig(this.getTargetPlayer()).loadMultiMessage(fullPath, Collections.emptyList(), (Player)null, true, holders);
   }

   @Contract("null,_,_->null;!null,_,_->!null")
   default ItemStack loadLanguageDescription(@Nullable ItemStack item, @NotNull String fullPath, String... holders) {
      if (item == null) {
         return null;
      } else {
         ItemMeta meta = item.getItemMeta();
         this.loadLanguageDescription(meta, fullPath, holders);
         item.setItemMeta(meta);
         return item;
      }
   }

   @Contract("null,_,_->null;!null,_,_->!null")
   default ItemMeta loadLanguageDescription(@Nullable ItemMeta meta, @NotNull String fullPath, String... holders) {
      if (meta == null) {
         return null;
      } else {
         List<String> list = this.getPlugin().getLanguageConfig(this.getTargetPlayer()).loadMultiMessage(fullPath, (List)null, this.getTargetPlayer(), true, holders);
         meta.setDisplayName(list != null && !list.isEmpty() ? (String)list.get(0) : " ");
         if (list != null && !list.isEmpty()) {
            meta.setLore(list.subList(1, list.size()));
         }

         return meta;
      }
   }

   @NotNull
   default ItemStack getGuiItem(@NotNull String path, @NotNull Material defMaterial) {
      return this.getGuiItem(path, defMaterial, 0);
   }

   @NotNull
   default ItemStack getGuiItem(@NotNull String path, @NotNull Material defMaterial, int defDurability) {
      YMLConfig config = this.getPlugin().getConfig("gui.yml");
      ItemStack item = new ItemStack(config.loadMaterial(path + ".material", defMaterial));
      ItemMeta meta = ItemUtils.getMeta(item);
      meta.addItemFlags(ItemFlag.values());
      if (config.getBoolean(path + ".glow", false)) {
         meta.addEnchant(Enchantment.LURE, 1, true);
      }

      item.setItemMeta(meta);
      int dur = config.loadInteger(path + ".durability", defDurability);
      if (dur > 0) {
         item.setDurability((short)dur);
      }

      return item;
   }

   @NotNull
   default ItemStack getBackItem() {
      return this.loadLanguageDescription(this.getGuiItem("buttons.back", Material.BARRIER), "gui.back.description");
   }
}
