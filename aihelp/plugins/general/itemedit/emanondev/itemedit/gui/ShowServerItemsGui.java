package emanondev.itemedit.gui;

import emanondev.itemedit.ItemEdit;
import emanondev.itemedit.UtilsString;
import emanondev.itemedit.YMLConfig;
import emanondev.itemedit.command.ServerItemCommand;
import emanondev.itemedit.storage.ServerStorage;
import emanondev.itemedit.utility.InventoryUtils;
import emanondev.itemedit.utility.ItemUtils;
import java.util.ArrayList;
import java.util.Collections;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ShowServerItemsGui implements PagedGui {
   private static final YMLConfig GUI_CONFIG = ItemEdit.get().getConfig();
   private final Inventory inventory;
   private final Player target;
   private final int page;
   private int rows;
   private ArrayList<String> ids;
   private boolean showItems = true;

   public ShowServerItemsGui(Player player, int page) {
      if (player == null) {
         throw new NullPointerException();
      } else if (page < 1) {
         throw new NullPointerException();
      } else {
         this.target = player;
         this.rows = GUI_CONFIG.loadInteger("gui.serveritems.rows", 6);
         if (this.rows < 1 || this.rows > 5) {
            this.rows = Math.min(5, Math.max(1, this.rows));
         }

         ServerStorage storage = ItemEdit.get().getServerStorage();
         ArrayList<String> list = new ArrayList(storage.getIds());
         Collections.sort(list);
         int maxPages = list.size() / (this.rows * 9) + (list.size() % (this.rows * 9) == 0 ? 0 : 1);
         if (page > maxPages) {
            page = maxPages;
         }

         this.page = page;
         String title = UtilsString.fix(GUI_CONFIG.loadMessage("gui.serveritems.title", "", false), player, true, "%player_name%", this.target.getName(), "%page%", String.valueOf(page));
         this.inventory = Bukkit.createInventory(this, (this.rows + 1) * 9, title);
         this.updateInventory();
         this.inventory.setItem(this.rows * 9 + 4, this.getPageInfoItem());
         if (page > 1) {
            this.inventory.setItem(this.rows * 9 + 1, this.getPreviousPageItem());
         }

         if (page < maxPages) {
            this.inventory.setItem(this.rows * 9 + 7, this.getNextPageItem());
         }

      }
   }

   public void updateInventory() {
      ServerStorage storage = ItemEdit.get().getServerStorage();
      ArrayList<String> list = new ArrayList(storage.getIds());
      this.ids = list;
      Collections.sort(list);

      for(int i = 0; i < this.rows * 9; ++i) {
         int slot = this.rows * 9 * (this.page - 1) + i;
         if (slot >= list.size()) {
            this.inventory.setItem(i, (ItemStack)null);
         } else {
            ItemStack item = storage.getItem((String)list.get(slot));
            if (item == null) {
               (new NullPointerException("invalid id " + (String)list.get(slot))).printStackTrace();
            } else if (this.showItems) {
               this.inventory.setItem(i, item);
            } else {
               ItemStack display = item.clone();
               ItemMeta meta = ItemUtils.getMeta(display);
               meta.addItemFlags(ItemFlag.values());
               meta.setLore(Collections.singletonList(UtilsString.fix((String)("&9Nick: &e" + storage.getNick((String)list.get(slot))), (Player)null, true)));
               meta.setDisplayName(UtilsString.fix((String)("&9ID: &e" + (String)list.get(slot)), (Player)null, true));
               display.setItemMeta(meta);
               this.inventory.setItem(i, display);
            }
         }
      }

   }

   private ItemStack getPageInfoItem() {
      return this.loadLanguageDescription(this.getGuiItem("gui.serveritems.page-info", Material.NAME_TAG), "gui.serveritems.page-info.description", new String[]{"%player_name%", this.target.getName(), "%page%", String.valueOf(this.page)});
   }

   public int getPage() {
      return this.page;
   }

   public void onClose(InventoryCloseEvent event) {
   }

   public void onClick(InventoryClickEvent event) {
      if (event.getWhoClicked().equals(this.target)) {
         if (this.inventory.equals(event.getClickedInventory())) {
            if (!ItemUtils.isAirOrNull(event.getCurrentItem())) {
               if (event.getSlot() > this.inventory.getSize() - 9) {
                  switch(this.inventory.getSize() - event.getSlot()) {
                  case 2:
                     this.target.openInventory((new ShowServerItemsGui(this.target, this.page + 1)).getInventory());
                     return;
                  case 5:
                     this.showItems = !this.showItems;
                     this.updateInventory();
                     return;
                  default:
                     this.target.openInventory((new ShowServerItemsGui(this.target, this.page - 1)).getInventory());
                  }
               } else {
                  int slot = this.rows * 9 * (this.page - 1) + event.getSlot();
                  String id = (String)this.ids.get(slot);
                  ServerStorage storage = ItemEdit.get().getServerStorage();
                  ItemStack item = storage.getItem(id);
                  if (ItemUtils.isAirOrNull(item)) {
                     this.updateInventory();
                  } else {
                     switch(event.getClick()) {
                     case LEFT:
                        InventoryUtils.giveAmount(this.target, item, 1, InventoryUtils.ExcessMode.DELETE_EXCESS);
                        return;
                     case SHIFT_LEFT:
                        InventoryUtils.giveAmount(this.target, item, 64, InventoryUtils.ExcessMode.DELETE_EXCESS);
                        return;
                     case SHIFT_RIGHT:
                        if (!event.getWhoClicked().hasPermission("itemedit.serveritem.delete")) {
                           ServerItemCommand.get().sendPermissionLackMessage("itemedit.serveritem.delete", event.getWhoClicked());
                           return;
                        }

                        storage.remove(id);
                        this.updateInventory();
                        return;
                     default:
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
   }

   @NotNull
   public Inventory getInventory() {
      return this.inventory;
   }

   public Player getTargetPlayer() {
      return this.target;
   }

   @NotNull
   public ItemEdit getPlugin() {
      return ItemEdit.get();
   }
}
