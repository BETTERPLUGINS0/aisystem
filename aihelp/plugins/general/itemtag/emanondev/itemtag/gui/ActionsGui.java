package emanondev.itemtag.gui;

import emanondev.itemedit.Util;
import emanondev.itemedit.aliases.Aliases;
import emanondev.itemedit.gui.Gui;
import emanondev.itemedit.gui.PagedGui;
import emanondev.itemedit.utility.ItemUtils;
import emanondev.itemedit.utility.VersionUtils;
import emanondev.itemtag.ItemTag;
import emanondev.itemtag.TagItem;
import emanondev.itemtag.actions.ActionsUtility;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ActionsGui implements Gui {
   private final TagItem tagItem;
   private final Inventory inventory;
   private final Player target;
   private final String commandAlias;
   private final String subCommandAlias;
   private int editorCooldown = 1;
   private int editorValue = 1;

   public ActionsGui(Player target, ItemStack item, String commandAlias, String subCommandAlias) {
      String title = this.getLanguageMessage("gui.actions.title", new String[]{"%player_name%", target.getName()});
      this.inventory = Bukkit.createInventory(this, 18, title);
      this.target = target;
      this.tagItem = ItemTag.getTagItem(item);
      this.commandAlias = commandAlias;
      this.subCommandAlias = subCommandAlias;
      this.updateInventory();
   }

   public void onClose(InventoryCloseEvent event) {
   }

   public void onClick(InventoryClickEvent event) {
      if (event.getWhoClicked().equals(this.target)) {
         if (this.inventory.equals(event.getClickedInventory())) {
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
               switch(event.getSlot()) {
               case 0:
               case 1:
               case 9:
                  this.getTargetPlayer().openInventory((new ActionsGui.ActionTypeGui()).getInventory());
                  return;
               case 2:
               case 3:
               case 4:
               case 10:
               case 11:
               case 12:
               case 13:
               default:
                  return;
               case 5:
                  if (event.getClick() == ClickType.SHIFT_RIGHT) {
                     ActionsUtility.setPermission(this.tagItem, (String)null);
                     this.updateInventory();
                     return;
                  }

                  this.getTargetPlayer().closeInventory();
                  this.sendClickableText(this, "permission");
                  return;
               case 6:
                  switch(event.getClick()) {
                  case LEFT:
                     ActionsUtility.setCooldownMs(this.tagItem, Math.max(0, ActionsUtility.getCooldownMs(this.tagItem) - this.editorCooldown * 1000));
                     break;
                  case SHIFT_RIGHT:
                     this.editorCooldown = Math.min(1000000, this.editorCooldown * 10);
                     break;
                  case RIGHT:
                     ActionsUtility.setCooldownMs(this.tagItem, (int)Math.min(2147483647L, (long)ActionsUtility.getCooldownMs(this.tagItem) + (long)this.editorCooldown * 1000L));
                     break;
                  case SHIFT_LEFT:
                     this.editorCooldown = Math.max(1, this.editorCooldown / 10);
                     break;
                  default:
                     return;
                  }

                  this.updateInventory();
                  return;
               case 7:
                  ActionsUtility.setConsume(this.tagItem, !ActionsUtility.getConsume(this.tagItem));
                  this.updateInventory();
                  return;
               case 8:
                  switch(event.getClick()) {
                  case LEFT:
                     ActionsUtility.setUses(this.tagItem, Math.max(-1, ActionsUtility.getUses(this.tagItem) - this.editorValue));
                     if (ActionsUtility.getDisplayUses(this.tagItem)) {
                        ActionsUtility.updateUsesDisplay(this.tagItem.getItem());
                     }
                     break;
                  case SHIFT_RIGHT:
                     this.editorValue = Math.min(1000000, this.editorValue * 10);
                     break;
                  case RIGHT:
                     ActionsUtility.setUses(this.tagItem, (int)Math.min(2147483647L, (long)ActionsUtility.getUses(this.tagItem) + (long)this.editorValue));
                     if (ActionsUtility.getDisplayUses(this.tagItem)) {
                        ActionsUtility.updateUsesDisplay(this.tagItem.getItem());
                     }
                     break;
                  case SHIFT_LEFT:
                     this.editorValue = Math.max(1, this.editorValue / 10);
                     break;
                  default:
                     return;
                  }

                  this.updateInventory();
                  return;
               case 14:
                  ActionsUtility.setDisplayUses(this.tagItem, !ActionsUtility.getDisplayUses(this.tagItem));
                  if (ActionsUtility.getDisplayUses(this.tagItem)) {
                     ActionsUtility.updateUsesDisplay(this.tagItem.getItem());
                  }

                  this.updateInventory();
                  return;
               case 15:
                  ActionsUtility.setVisualCooldown(this.tagItem, !ActionsUtility.getVisualCooldown(this.tagItem));
                  this.updateInventory();
                  return;
               case 16:
                  if (event.getClick() == ClickType.SHIFT_RIGHT) {
                     ActionsUtility.setCooldownId(this.tagItem, (String)null);
                     this.updateInventory();
                     return;
                  }

                  this.getTargetPlayer().closeInventory();
                  this.sendClickableText(this, "cooldownid");
                  return;
               case 17:
                  switch(event.getClick()) {
                  case LEFT:
                     ActionsUtility.setMaxUses(this.tagItem, Math.max(-1, ActionsUtility.getMaxUses(this.tagItem) - this.editorValue));
                     if (ActionsUtility.getDisplayUses(this.tagItem)) {
                        ActionsUtility.updateUsesDisplay(this.tagItem.getItem());
                     }
                     break;
                  case SHIFT_RIGHT:
                     this.editorValue = Math.min(1000000, this.editorValue * 10);
                     break;
                  case RIGHT:
                     ActionsUtility.setMaxUses(this.tagItem, (int)Math.min(2147483647L, (long)ActionsUtility.getMaxUses(this.tagItem) + (long)this.editorValue));
                     if (ActionsUtility.getDisplayUses(this.tagItem)) {
                        ActionsUtility.updateUsesDisplay(this.tagItem.getItem());
                     }
                     break;
                  case SHIFT_LEFT:
                     this.editorValue = Math.max(1, this.editorValue / 10);
                     break;
                  default:
                     return;
                  }

                  this.updateInventory();
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
      return this.target;
   }

   @NotNull
   public ItemTag getPlugin() {
      return ItemTag.get();
   }

   private void updateInventory() {
      ItemStack item;
      try {
         item = this.loadLanguageDescription(this.getGuiItem("gui.actions.addline", Material.LIME_DYE), "gui.actions.addline", new String[0]);
      } catch (Throwable var14) {
         item = Util.getDyeItemFromColor(DyeColor.LIME);
         item = this.loadLanguageDescription(this.getGuiItem("gui.actions.addline", item.getType(), item.getDurability()), "gui.actions.addline", new String[0]);
      }

      this.getInventory().setItem(0, item);

      try {
         item = this.loadLanguageDescription(this.getGuiItem("gui.actions.set", Material.BLUE_DYE), "gui.actions.set", new String[0]);
      } catch (Throwable var13) {
         item = Util.getDyeItemFromColor(DyeColor.BLUE);
         item = this.loadLanguageDescription(this.getGuiItem("gui.actions.set", item.getType(), item.getDurability()), "gui.actions.set", new String[0]);
      }

      this.getInventory().setItem(9, item);

      try {
         item = this.loadLanguageDescription(this.getGuiItem("gui.actions.remove", Material.RED_DYE), "gui.actions.remove", new String[0]);
      } catch (Throwable var12) {
         item = Util.getDyeItemFromColor(DyeColor.RED);
         item = this.loadLanguageDescription(this.getGuiItem("gui.actions.remove", item.getType(), item.getDurability()), "gui.actions.remove", new String[0]);
      }

      this.getInventory().setItem(1, item);
      item = this.getGuiItem("gui.actions.consume", Material.APPLE);
      ItemMeta meta = this.loadLanguageDescription(ItemUtils.getMeta(item), "gui.actions.consume", new String[]{"%value%", Aliases.BOOLEAN.getName(ActionsUtility.getConsume(this.tagItem))});
      if (!ActionsUtility.getConsume(this.tagItem)) {
         meta.addEnchant(Enchantment.LURE, 1, true);
      } else {
         meta.removeEnchant(Enchantment.LURE);
      }

      item.setItemMeta(meta);
      this.getInventory().setItem(7, item);
      this.getInventory().setItem(8, this.loadLanguageDescription(this.getGuiItem("gui.actions.uses", Material.IRON_PICKAXE), "gui.actions.uses", new String[]{"%value%", ActionsUtility.getUses(this.tagItem) == -1 ? "-1 (Unlimited)" : String.valueOf(ActionsUtility.getUses(this.tagItem)), "%editor%", String.valueOf(this.editorValue), "%editor-prev%", String.valueOf(Math.max(1, this.editorValue / 10)), "%editor-next%", String.valueOf(Math.min(1000000, this.editorValue * 10))}));
      if (VersionUtils.isVersionAfter(1, 9)) {
         this.getInventory().setItem(17, this.loadLanguageDescription(this.getGuiItem("gui.actions.maxuses", Material.DIAMOND_PICKAXE), "gui.actions.maxuses", new String[]{"%value%", ActionsUtility.getMaxUses(this.tagItem) == -1 ? "-1 (Unlimited)" : String.valueOf(ActionsUtility.getMaxUses(this.tagItem)), "%editor%", String.valueOf(this.editorValue), "%editor-prev%", String.valueOf(Math.max(1, this.editorValue / 10)), "%editor-next%", String.valueOf(Math.min(1000000, this.editorValue * 10))}));
         item = this.getGuiItem("gui.actions.displayuses", Material.PAINTING);
         meta = this.loadLanguageDescription(ItemUtils.getMeta(item), "gui.actions.displayuses", new String[]{"%value%", Aliases.BOOLEAN.getName(ActionsUtility.getDisplayUses(this.tagItem))});
         if (ActionsUtility.getDisplayUses(this.tagItem)) {
            meta.addEnchant(Enchantment.LURE, 1, true);
         } else {
            meta.removeEnchant(Enchantment.LURE);
         }

         item.setItemMeta(meta);
         this.getInventory().setItem(14, item);
      }

      try {
         this.getInventory().setItem(5, this.loadLanguageDescription(this.getGuiItem("gui.actions.permission", Material.IRON_BARS), "gui.actions.permission", new String[]{"%value%", ActionsUtility.getPermission(this.tagItem) == null ? "<none>" : ActionsUtility.getPermission(this.tagItem)}));
      } catch (Error var15) {
         this.getInventory().setItem(5, this.loadLanguageDescription(this.getGuiItem("gui.actions.permission", Material.valueOf("IRON_FENCE")), "gui.actions.permission", new String[]{"%value%", ActionsUtility.getPermission(this.tagItem) == null ? "<none>" : ActionsUtility.getPermission(this.tagItem)}));
      }

      this.getInventory().setItem(6, this.loadLanguageDescription(this.getGuiItem("gui.actions.cooldown", Material.COMPASS), "gui.actions.cooldown", new String[]{"%value_s%", String.valueOf(ActionsUtility.getCooldownMs(this.tagItem) / 1000), "%editor%", String.valueOf(this.editorCooldown), "%editor-prev%", String.valueOf(Math.max(1, this.editorCooldown / 10)), "%editor-next%", String.valueOf(Math.min(1000000, this.editorCooldown * 10))}));
      this.getInventory().setItem(16, this.loadLanguageDescription(this.getGuiItem("gui.actions.cooldownid", Material.NAME_TAG), "gui.actions.cooldownid", new String[]{"%value%", ActionsUtility.getCooldownId(this.tagItem)}));
      item = this.getGuiItem("gui.actions.visualcooldown", Material.ENDER_PEARL);
      meta = this.loadLanguageDescription(ItemUtils.getMeta(item), "gui.actions.visualcooldown", new String[]{"%value%", Aliases.BOOLEAN.getName(ActionsUtility.getVisualCooldown(this.tagItem))});
      if (ActionsUtility.getVisualCooldown(this.tagItem)) {
         meta.addEnchant(Enchantment.LURE, 1, true);
      } else {
         meta.removeEnchant(Enchantment.LURE);
      }

      item.setItemMeta(meta);
      this.getInventory().setItem(15, item);
      item = this.getGuiItem("gui.actions.info", Material.PAPER);
      meta = this.loadLanguageDescription(ItemUtils.getMeta(item), "gui.actions.info", new String[0]);
      List<String> lore = new ArrayList(meta.hasLore() ? meta.getLore() : Collections.emptyList());
      List<String> list = ActionsUtility.getActions(this.tagItem);
      if (list != null) {
         Iterator var5 = list.iterator();

         while(var5.hasNext()) {
            String action = (String)var5.next();
            if (action != null && !action.isEmpty()) {
               int index = action.indexOf("%%:%%");
               String actionPre = action.substring(0, index);
               String actionPost = action.substring(index + "%%:%%".length());
               if (actionPost.startsWith("-pin")) {
                  try {
                     actionPost = actionPost.substring(69);
                  } catch (Exception var11) {
                  }
               }

               lore.add(ChatColor.YELLOW + actionPre + " " + ChatColor.WHITE + actionPost);
            }
         }
      }

      meta.setLore(lore);
      item.setItemMeta(meta);
      this.getInventory().setItem(2, item);
   }

   private void sendClickableText(Gui gui, String postClickable) {
      Util.sendMessage(gui.getTargetPlayer(), (new ComponentBuilder(gui.getLanguageMessage("gui.actions.click-interact", new String[0]))).event(new HoverEvent(Action.SHOW_TEXT, (new ComponentBuilder(gui.getLanguageMessage("gui.actions.click-hover", new String[0]))).create())).event(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND, "/" + this.commandAlias + " " + this.subCommandAlias + " " + postClickable + " ")).create());
   }

   private class ActionTypeGui implements PagedGui {
      private static final int ROWS = 5;
      private final Inventory inventory;
      private final int page;
      private final int maxPages;

      public ActionTypeGui() {
         this(1);
      }

      public ActionTypeGui(int param2) {
         if (page < 1) {
            throw new NullPointerException();
         } else {
            String title = this.getLanguageMessage("gui.actions.title", new String[]{"%player_name%", ActionsGui.this.target.getName()});
            this.inventory = Bukkit.createInventory(this, 54, title);
            List<String> actionsList = ActionsUtility.getActions(ActionsGui.this.tagItem);
            int actions = (actionsList == null ? 0 : actionsList.size()) + 1;
            int maxPages = actions / 45 + (actions % 45 == 0 ? 0 : 1);
            if (page > maxPages) {
               page = maxPages;
            }

            this.maxPages = maxPages;
            this.page = page;
            this.updateInventory();
         }
      }

      public int getPage() {
         return this.page;
      }

      public void onClose(InventoryCloseEvent event) {
      }

      public void onClick(InventoryClickEvent event) {
         if (event.getWhoClicked().equals(ActionsGui.this.target)) {
            if (this.inventory.equals(event.getClickedInventory())) {
               if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
                  if (event.getSlot() > this.inventory.getSize() - 9) {
                     switch(this.inventory.getSize() - event.getSlot()) {
                     case 2:
                        ActionsGui.this.target.openInventory((ActionsGui.this.new ActionTypeGui(this.page + 1)).getInventory());
                        return;
                     case 5:
                        this.getTargetPlayer().openInventory(ActionsGui.this.getInventory());
                        return;
                     default:
                        ActionsGui.this.target.openInventory((ActionsGui.this.new ActionTypeGui(this.page - 1)).getInventory());
                     }
                  } else {
                     List<String> actions = ActionsUtility.getActions(ActionsGui.this.tagItem);
                     if (actions == null) {
                        actions = Collections.emptyList();
                     }

                     if (this.page > 1) {
                        if ((this.page - 1) * 5 * 9 > actions.size()) {
                           actions = Collections.emptyList();
                        } else {
                           actions = actions.subList((this.page - 1) * 5 * 9, actions.size());
                        }
                     }

                     if (actions.size() > event.getSlot()) {
                        switch(event.getClick()) {
                        case LEFT:
                           this.getTargetPlayer().closeInventory();
                           ActionsGui.this.sendClickableText(this, "addline " + (event.getSlot() + 1));
                           return;
                        case SHIFT_RIGHT:
                           List<String> actionsx = new ArrayList(actions);
                           actionsx.remove(event.getSlot());
                           ActionsUtility.setActions(ActionsGui.this.tagItem, actionsx);
                           this.updateInventory();
                        default:
                           return;
                        case RIGHT:
                           String action = (String)actions.get(event.getSlot());
                           int index = action.indexOf("%%:%%");
                           String actionPre = action.substring(0, index);
                           String actionPost = action.substring(index + "%%:%%".length());
                           if (actionPost.startsWith("-pin")) {
                              try {
                                 actionPost = actionPost.substring(69);
                              } catch (Exception var8) {
                              }
                           }

                           this.getTargetPlayer().closeInventory();
                           ActionsGui.this.sendClickableText(this, "set " + (event.getSlot() + 1) + " " + actionPre + " " + actionPost);
                        }
                     } else {
                        if (actions.size() == event.getSlot() && event.getClick() == ClickType.LEFT) {
                           this.getTargetPlayer().closeInventory();
                           ActionsGui.this.sendClickableText(this, "add");
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

      private void updateInventory() {
         List<String> actions = ActionsUtility.getActions(ActionsGui.this.tagItem);
         if (actions != null) {
            for(int guiIndex = 0; guiIndex < 45; ++guiIndex) {
               int elementIndex = (this.page - 1) * 5 * 9 + guiIndex;
               if (elementIndex >= actions.size()) {
                  this.inventory.setItem(guiIndex, (ItemStack)null);
               } else {
                  int separatorIndex = ((String)actions.get(elementIndex)).indexOf("%%:%%");
                  String actionPre = ((String)actions.get(elementIndex)).substring(0, separatorIndex);
                  String actionPost = ((String)actions.get(elementIndex)).substring(separatorIndex + "%%:%%".length());
                  if (actionPost.startsWith("-pin")) {
                     try {
                        actionPost = actionPost.substring(69);
                     } catch (Exception var12) {
                     }
                  }

                  ItemStack itemx;
                  try {
                     itemx = this.getGuiItem("gui.actionslines.line", Material.COMMAND_BLOCK);
                  } catch (Error var11) {
                     itemx = this.getGuiItem("gui.actionslines.line", Material.valueOf("COMMAND"));
                  }

                  ItemMeta metax = itemx.getItemMeta();
                  String action = this.getPlugin().getLanguageConfig(ActionsGui.this.target).getMessage("gui.actionslines.actionformat", "", new String[]{"%type%", actionPre, "%info%", actionPost});
                  this.loadLanguageDescription(metax, "gui.actionslines.element", new String[]{"%action%", action});
                  itemx.setAmount(Math.min(125, elementIndex + 1));
                  itemx.setItemMeta(metax);
                  this.inventory.setItem(guiIndex, itemx);
               }
            }
         }

         if (actions == null || actions.size() < 45) {
            ItemStack item;
            try {
               item = this.getGuiItem("gui.actionslines.line", Material.COMMAND_BLOCK);
            } catch (Error var10) {
               item = this.getGuiItem("gui.actionslines.line", Material.valueOf("COMMAND"));
            }

            ItemMeta meta = item.getItemMeta();
            this.loadLanguageDescription(meta, "gui.actionslines.add", new String[0]);
            item.setItemMeta(meta);
            item.setAmount(actions == null ? 1 : actions.size() + 1);
            this.inventory.setItem(actions == null ? 0 : actions.size(), item);
         }

         this.inventory.setItem(49, this.getBackItem());
         if (this.page > 1) {
            this.inventory.setItem(46, this.getPreviousPageItem());
         } else {
            this.inventory.setItem(46, (ItemStack)null);
         }

         if (this.page < this.maxPages) {
            this.inventory.setItem(52, this.getNextPageItem());
         } else {
            this.inventory.setItem(46, (ItemStack)null);
         }

      }

      @NotNull
      public Inventory getInventory() {
         return this.inventory;
      }

      public Player getTargetPlayer() {
         return ActionsGui.this.target;
      }

      @NotNull
      public ItemTag getPlugin() {
         return ItemTag.get();
      }
   }
}
