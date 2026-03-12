package emanondev.itemedit.gui;

import emanondev.itemedit.APlugin;
import emanondev.itemedit.Util;
import emanondev.itemedit.aliases.Aliases;
import emanondev.itemedit.utility.InventoryUtils;
import emanondev.itemedit.utility.ItemUtils;
import emanondev.itemedit.utility.SchedulerUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ColorListSelectorGui implements Gui {
   private static final String subPath = "gui.colorselector.";
   private final List<DyeColor> colors;
   private final Gui parent;
   private final Inventory inventory;

   public ColorListSelectorGui(@NotNull Gui parent, @NotNull List<DyeColor> colors) {
      this.colors = colors;
      this.parent = parent;
      String title = this.getLanguageMessage("gui.colorselector.title", new String[0]);
      this.inventory = Bukkit.createInventory(this, 18, title);
      this.inventory.setItem(this.inventory.getSize() - 1, this.getBackItem());
   }

   public void updateInventory() {
      int i = 0;
      List<String> list = new ArrayList();
      Iterator var3 = this.colors.iterator();

      while(var3.hasNext()) {
         DyeColor c = (DyeColor)var3.next();
         list.add(Aliases.COLOR.getName(c));
      }

      DyeColor[] var9 = DyeColor.values();
      int var10 = var9.length;

      for(int var5 = 0; var5 < var10; ++var5) {
         DyeColor color = var9[var5];
         ItemStack item = Util.getDyeItemFromColor(color);
         ItemMeta meta = ItemUtils.getMeta(item);
         meta.addItemFlags(ItemFlag.values());
         this.loadLanguageDescription(meta, "gui.colorselector.buttons.color", new String[]{"%colors%", String.join("&b, &e", list), "%color%", Aliases.COLOR.getName(color)});
         item.setItemMeta(meta);
         item.setAmount(Math.max(Math.min(101, this.colors.size()), 1));
         this.inventory.setItem(i, item);
         ++i;
      }

   }

   public void onClose(InventoryCloseEvent event) {
      SchedulerUtils.runLater(this.getPlugin(), 1L, () -> {
         if (!InventoryUtils.getTopInventory(this.getTargetPlayer()).equals(this.parent.getInventory())) {
            this.getTargetPlayer().openInventory(this.parent.getInventory());
         }

      });
   }

   public void onClick(InventoryClickEvent event) {
      if (event.getWhoClicked().equals(this.parent.getTargetPlayer())) {
         if (this.inventory.equals(event.getClickedInventory())) {
            if (!ItemUtils.isAirOrNull(event.getCurrentItem())) {
               if (event.getClick() != ClickType.DOUBLE_CLICK) {
                  if (event.getSlot() < DyeColor.values().length) {
                     switch(event.getClick()) {
                     case LEFT:
                        this.colors.add(DyeColor.values()[event.getSlot()]);
                        this.updateInventory();
                        break;
                     case RIGHT:
                        this.colors.remove(this.colors.size() - 1);
                        this.updateInventory();
                        break;
                     case SHIFT_RIGHT:
                        this.colors.clear();
                        this.updateInventory();
                     }

                  } else {
                     if (event.getSlot() == this.inventory.getSize() - 1) {
                        this.getTargetPlayer().openInventory(this.parent.getInventory());
                     }

                  }
               }
            }
         }
      }
   }

   public void onDrag(InventoryDragEvent event) {
   }

   public void onOpen(InventoryOpenEvent event) {
      this.updateInventory();
   }

   @NotNull
   public Inventory getInventory() {
      return this.inventory;
   }

   public Player getTargetPlayer() {
      return this.parent.getTargetPlayer();
   }

   @NotNull
   public APlugin getPlugin() {
      return this.parent.getPlugin();
   }
}
