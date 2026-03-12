package emanondev.itemedit.gui;

import emanondev.itemedit.ItemEdit;
import emanondev.itemedit.Util;
import emanondev.itemedit.aliases.Aliases;
import emanondev.itemedit.utility.ItemUtils;
import emanondev.itemedit.utility.VersionUtils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class BannerEditor implements Gui {
   private static final String subPath = "gui.banner.";
   private static final PatternType[] TYPES = ItemUtils.getPatternTypesFiltered();
   private final Player target;
   private final Inventory inventory;
   private final List<BannerEditor.BannerData> layers = new ArrayList();
   private final ItemStack banner;
   private BannerMeta meta;

   public BannerEditor(Player target, ItemStack item) {
      if (item == null || !(item.getItemMeta() instanceof BannerMeta)) {
         try {
            item = new ItemStack(Material.WHITE_BANNER);
         } catch (Exception var5) {
            item = new ItemStack(Material.valueOf("BANNER"));
         }
      }

      this.banner = item;
      this.meta = (BannerMeta)ItemUtils.getMeta(this.banner);
      this.target = target;
      String title = this.getLanguageMessage("gui.banner.title", new String[0]);
      this.inventory = Bukkit.createInventory(this, 54, title);

      for(int i = 0; i < 8; ++i) {
         if (i < this.meta.getPatterns().size()) {
            this.layers.add(new BannerEditor.BannerData((Pattern)this.meta.getPatterns().get(i)));
         } else {
            this.layers.add(new BannerEditor.BannerData());
         }
      }

      this.updateInventory();
   }

   @NotNull
   public ItemEdit getPlugin() {
      return ItemEdit.get();
   }

   public void onClose(InventoryCloseEvent event) {
   }

   public void onClick(InventoryClickEvent event) {
      if (event.getWhoClicked().equals(this.target)) {
         if (this.inventory.equals(event.getClickedInventory())) {
            if (!ItemUtils.isAirOrNull(event.getCurrentItem())) {
               if (event.getSlot() % 9 == 0) {
                  this.target.openInventory((new BannerEditor.ColorSelector((BannerEditor.BannerData)null)).getInventory());
               } else {
                  BannerEditor.BannerData layer = (BannerEditor.BannerData)this.layers.get(event.getSlot() % 9 - 1);
                  if (event.getSlot() > 9 && event.getSlot() < 18) {
                     if (event.getClick() == ClickType.MIDDLE || event.getClick() == ClickType.CREATIVE || event.getClick() == ClickType.NUMBER_KEY && event.getHotbarButton() == 0) {
                        layer.active = !layer.active;
                     } else if (event.isLeftClick()) {
                        if (event.getSlot() == 10) {
                           return;
                        }

                        this.layers.add(event.getSlot() - 2 - 9, (BannerEditor.BannerData)this.layers.remove(event.getSlot() - 9 - 1));
                     } else if (event.isRightClick()) {
                        if (event.getSlot() == 17) {
                           return;
                        }

                        this.layers.add(event.getSlot() - 9, (BannerEditor.BannerData)this.layers.remove(event.getSlot() - 9 - 1));
                     }

                     this.updateInventory();
                  } else if (event.getSlot() < 45) {
                     if (layer.active) {
                        layer.onClick(event.getSlot() / 9, event);
                        this.updateInventory();
                     }
                  } else {
                     if (event.getSlot() == 49) {
                        if (event.isLeftClick()) {
                           try {
                              this.target.getInventory().setItemInMainHand(this.banner);
                           } catch (Throwable var4) {
                              this.target.getInventory().setItemInHand(this.banner);
                           }
                        } else {
                           this.target.getInventory().addItem(new ItemStack[]{this.banner});
                        }
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
      this.meta.setPatterns(new ArrayList());
      ItemStack item = this.banner.clone();
      item.setItemMeta(this.meta);
      this.meta = (BannerMeta)ItemUtils.getMeta(item);
      item.setAmount(1);
      this.getInventory().setItem(0, item);
      DyeColor bcolor = Util.getColorFromBanner(item);
      item = Util.getDyeItemFromColor(bcolor);
      ItemMeta bmeta = ItemUtils.getMeta(item);
      bmeta.addItemFlags(ItemFlag.values());
      this.loadLanguageDescription(bmeta, "gui.banner.buttons.color", new String[]{"%color%", Aliases.COLOR.getName(bcolor)});
      item.setItemMeta(bmeta);
      this.getInventory().setItem(27, item);

      for(int i = 1; i < 9; ++i) {
         BannerEditor.BannerData layer = (BannerEditor.BannerData)this.layers.get(i - 1);
         item = layer.getPositionItem();
         item.setAmount(i);
         this.getInventory().setItem(i + 9, item);
         this.getInventory().setItem(i + 18, layer.getPatternTypeItem());
         this.getInventory().setItem(i + 27, layer.getColorItem());
         if (layer.active) {
            this.meta.addPattern(layer.getPattern());
            item = this.banner.clone();
            item.setItemMeta(this.meta);
            this.meta = (BannerMeta)ItemUtils.getMeta(item);
            this.getInventory().setItem(i, item);
         } else {
            this.getInventory().setItem(i, (ItemStack)null);
         }
      }

      item = this.banner.clone();
      item.setItemMeta(this.meta);
      this.getInventory().setItem(49, item);
      ItemUtils.getHandItem(this.getTargetPlayer()).setItemMeta(this.meta);
   }

   @NotNull
   public Inventory getInventory() {
      return this.inventory;
   }

   public Player getTargetPlayer() {
      return this.target;
   }

   private class BannerData {
      private Pattern pattern;
      private boolean active;

      private BannerData() {
         this.active = false;
         this.pattern = new Pattern(DyeColor.values()[(int)(Math.random() * (double)DyeColor.values().length)], BannerEditor.TYPES[(int)(Math.random() * (double)BannerEditor.TYPES.length)]);
      }

      private BannerData(Pattern param2) {
         this.active = false;
         this.pattern = pattern == null ? new Pattern(DyeColor.BLUE, BannerEditor.TYPES[0]) : pattern;
         this.active = true;
      }

      public ItemStack getPatternTypeItem() {
         if (!this.active) {
            return null;
         } else {
            ItemStack item = new ItemStack(Material.WHITE_BANNER);
            BannerMeta bMeta = (BannerMeta)ItemUtils.getMeta(item);
            bMeta.addPattern(new Pattern(DyeColor.BLACK, this.pattern.getPattern()));
            bMeta.addItemFlags(ItemFlag.values());
            BannerEditor.this.loadLanguageDescription(bMeta, "gui.banner.buttons.type", new String[]{"%type%", Aliases.PATTERN_TYPE.getName(this.pattern.getPattern())});
            item.setItemMeta(bMeta);
            return item;
         }
      }

      public ItemStack getColorItem() {
         if (!this.active) {
            return null;
         } else {
            ItemStack item = Util.getDyeItemFromColor(this.pattern.getColor());
            ItemMeta meta = ItemUtils.getMeta(item);
            meta.addItemFlags(ItemFlag.values());
            BannerEditor.this.loadLanguageDescription(meta, "gui.banner.buttons.color", new String[]{"%color%", Aliases.COLOR.getName(this.pattern.getColor())});
            item.setItemMeta(meta);
            return item;
         }
      }

      public ItemStack getPositionItem() {
         ItemStack item = this.active ? new ItemStack(Material.ITEM_FRAME) : Util.getDyeItemFromColor(DyeColor.GRAY);
         ItemMeta meta = ItemUtils.getMeta(item);
         meta.addItemFlags(ItemFlag.values());
         BannerEditor.this.loadLanguageDescription(meta, "gui.banner.buttons.position", new String[]{"%middle_click%", BannerEditor.this.getLanguageMessage("gui.middleclick." + (BannerEditor.this.getTargetPlayer().getGameMode() == GameMode.CREATIVE ? "creative" : "other"), new String[0])});
         item.setItemMeta(meta);
         return item;
      }

      public void onClick(int line, InventoryClickEvent event) {
         switch(line) {
         case 2:
            BannerEditor.this.target.openInventory((BannerEditor.this.new PatternSelector(this)).getInventory());
            return;
         case 3:
            BannerEditor.this.target.openInventory((BannerEditor.this.new ColorSelector(this)).getInventory());
            return;
         default:
         }
      }

      public void setColor(DyeColor dyeColor) {
         this.pattern = new Pattern(dyeColor, this.pattern.getPattern());
      }

      public void setPattern(PatternType type) {
         this.pattern = new Pattern(this.pattern.getColor(), type);
      }

      public Pattern getPattern() {
         return this.pattern;
      }

      // $FF: synthetic method
      BannerData(Pattern x1, Object x2) {
         this((Pattern)x1);
      }

      // $FF: synthetic method
      BannerData(Object x1) {
         this();
      }
   }

   private class ColorSelector implements Gui {
      private final BannerEditor.BannerData data;
      private final Inventory inventory;

      public ColorSelector(BannerEditor.BannerData param2) {
         this.data = data;
         String title = this.getLanguageMessage("gui.banner.color_selector_title", new String[0]);
         this.inventory = Bukkit.createInventory(this, 54, title);
         int i = 0;
         DyeColor[] var5 = DyeColor.values();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            DyeColor color = var5[var7];
            ItemStack item = Util.getDyeItemFromColor(color);
            ItemMeta meta = ItemUtils.getMeta(item);
            this.loadLanguageDescription(meta, "gui.banner.buttons.color_selector_info", new String[]{"%color%", Aliases.COLOR.getName(color)});
            item.setItemMeta(meta);
            this.inventory.setItem(i, item);
            ++i;
         }

      }

      @NotNull
      public ItemEdit getPlugin() {
         return ItemEdit.get();
      }

      public void onClose(InventoryCloseEvent event) {
      }

      public void onClick(InventoryClickEvent event) {
         if (event.getWhoClicked().equals(BannerEditor.this.target)) {
            if (this.inventory.equals(event.getClickedInventory())) {
               if (!ItemUtils.isAirOrNull(event.getCurrentItem())) {
                  if (this.data != null) {
                     this.data.setColor(DyeColor.values()[event.getSlot()]);
                  } else {
                     if (VersionUtils.isVersionAfter(1, 13)) {
                        BannerEditor.this.banner.setType(Util.getBannerItemFromColor(DyeColor.values()[event.getSlot()]));
                     } else {
                        BannerEditor.this.banner.setDurability((short)Util.getDataByColor(DyeColor.values()[event.getSlot()]));
                     }

                     BannerEditor.this.meta = (BannerMeta)ItemUtils.getMeta(BannerEditor.this.banner);
                  }

                  BannerEditor.this.updateInventory();
                  BannerEditor.this.getTargetPlayer().openInventory(BannerEditor.this.getInventory());
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
         return BannerEditor.this.target;
      }
   }

   private class PatternSelector implements Gui {
      private final BannerEditor.BannerData data;
      private final Inventory inventory;

      public PatternSelector(BannerEditor.BannerData param2) {
         if (data == null) {
            throw new NullPointerException();
         } else {
            this.data = data;
            String title = this.getLanguageMessage("gui.banner.pattern_selector_title", new String[0]);
            this.inventory = Bukkit.createInventory(this, 54, title);
            int i = 0;
            PatternType[] var5 = BannerEditor.TYPES;
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               PatternType type = var5[var7];

               ItemStack item;
               try {
                  item = new ItemStack(Material.WHITE_BANNER);
               } catch (Throwable var11) {
                  item = new ItemStack(Material.valueOf("BANNER"));
               }

               BannerMeta bMeta = (BannerMeta)ItemUtils.getMeta(item);
               bMeta.addPattern(new Pattern(DyeColor.BLACK, type));
               this.loadLanguageDescription(bMeta, "gui.banner.buttons.pattern_selector_info", new String[]{"%type%", Aliases.PATTERN_TYPE.getName(type)});
               item.setItemMeta(bMeta);
               this.inventory.setItem(i, item);
               ++i;
            }

         }
      }

      public void onClose(InventoryCloseEvent event) {
      }

      public void onClick(InventoryClickEvent event) {
         if (event.getWhoClicked().equals(BannerEditor.this.target)) {
            if (this.inventory.equals(event.getClickedInventory())) {
               if (!ItemUtils.isAirOrNull(event.getCurrentItem())) {
                  this.data.setPattern(BannerEditor.TYPES[event.getSlot()]);
                  BannerEditor.this.updateInventory();
                  BannerEditor.this.getTargetPlayer().openInventory(BannerEditor.this.getInventory());
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
         return BannerEditor.this.target;
      }

      @NotNull
      public ItemEdit getPlugin() {
         return ItemEdit.get();
      }
   }
}
