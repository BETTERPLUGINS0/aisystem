package emanondev.itemedit.gui;

import emanondev.itemedit.ItemEdit;
import emanondev.itemedit.Util;
import emanondev.itemedit.aliases.Aliases;
import emanondev.itemedit.utility.ItemUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class FireworkEditor implements Gui {
   private static final String subPath = "gui.firework.";
   private final FireworkMeta meta;
   private final Player target;
   private final Inventory inventory;
   private final List<FireworkEditor.FireworkEffectData> effects = new ArrayList();
   private final ItemStack firework;

   public FireworkEditor(Player target, ItemStack item) {
      if (item == null || !(item.getItemMeta() instanceof FireworkMeta)) {
         try {
            item = new ItemStack(Material.FIREWORK_ROCKET);
         } catch (Exception var5) {
            item = new ItemStack(Material.valueOf("FIREWORK"));
         }
      }

      this.firework = item.clone();
      this.meta = (FireworkMeta)ItemUtils.getMeta(this.firework);
      this.target = target;
      String title = this.getLanguageMessage("gui.firework.title", new String[0]);
      this.inventory = Bukkit.createInventory(this, 54, title);

      for(int i = 0; i < 9; ++i) {
         if (i < this.meta.getEffects().size()) {
            this.effects.add(new FireworkEditor.FireworkEffectData((FireworkEffect)this.meta.getEffects().get(i)));
         } else {
            this.effects.add(new FireworkEditor.FireworkEffectData());
         }
      }

   }

   private static List<DyeColor> translateToDyeColor(List<Color> colors) {
      if (colors == null) {
         return null;
      } else {
         List<DyeColor> list = new ArrayList();
         Iterator var2 = colors.iterator();

         while(var2.hasNext()) {
            Color color = (Color)var2.next();
            DyeColor col = DyeColor.getByFireworkColor(color);
            if (col != null) {
               list.add(col);
            }
         }

         return list;
      }
   }

   private static List<Color> translateToColor(List<DyeColor> colors) {
      if (colors == null) {
         return null;
      } else {
         List<Color> list = new ArrayList();
         Iterator var2 = colors.iterator();

         while(var2.hasNext()) {
            DyeColor color = (DyeColor)var2.next();
            Color col = color.getFireworkColor();
            if (col != null) {
               list.add(col);
            }
         }

         return list;
      }
   }

   @NotNull
   public ItemEdit getPlugin() {
      return ItemEdit.get();
   }

   public void onClose(InventoryCloseEvent event) {
      try {
         this.target.getInventory().setItemInMainHand(this.firework);
      } catch (Throwable var3) {
         this.target.getInventory().setItemInHand(this.firework);
      }

   }

   public void onClick(InventoryClickEvent event) {
      if (event.getWhoClicked().equals(this.target)) {
         if (this.inventory.equals(event.getClickedInventory())) {
            if (!ItemUtils.isAirOrNull(event.getCurrentItem())) {
               if (event.getClick() != ClickType.DOUBLE_CLICK) {
                  if (event.getSlot() >= 9) {
                     if (event.getSlot() < 45) {
                        if (((FireworkEditor.FireworkEffectData)this.effects.get(event.getSlot() % 9)).active) {
                           ((FireworkEditor.FireworkEffectData)this.effects.get(event.getSlot() % 9)).onClick(event.getSlot() / 9, event);
                           this.updateInventory();
                        }
                     } else if (event.getSlot() == 47) {
                        if (event.isLeftClick()) {
                           this.meta.setPower((this.meta.getPower() + 1) % 6);
                        } else {
                           this.meta.setPower((this.meta.getPower() - 1 + 6) % 6);
                        }

                        this.updateInventory();
                     } else {
                        if (event.getSlot() == 49) {
                           this.target.getInventory().addItem(new ItemStack[]{this.firework});
                        }

                     }
                  } else {
                     if (event.getClick() == ClickType.MIDDLE || event.getClick() == ClickType.CREATIVE || event.getClick() == ClickType.NUMBER_KEY && event.getHotbarButton() == 0) {
                        ((FireworkEditor.FireworkEffectData)this.effects.get(event.getSlot())).active = !((FireworkEditor.FireworkEffectData)this.effects.get(event.getSlot())).active;
                     }

                     if (event.isLeftClick()) {
                        if (event.getSlot() == 0) {
                           return;
                        }

                        this.effects.add(event.getSlot() - 1, (FireworkEditor.FireworkEffectData)this.effects.remove(event.getSlot()));
                     } else if (event.isRightClick()) {
                        if (event.getSlot() == 8) {
                           return;
                        }

                        this.effects.add(event.getSlot() + 1, (FireworkEditor.FireworkEffectData)this.effects.remove(event.getSlot()));
                     }

                     this.updateInventory();
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
      this.meta.clearEffects();

      for(int i = 0; i < 9; ++i) {
         ItemStack item = ((FireworkEditor.FireworkEffectData)this.effects.get(i)).getPositionItem();
         item.setAmount(i + 1);
         this.getInventory().setItem(i, item);
         this.getInventory().setItem(i + 9, ((FireworkEditor.FireworkEffectData)this.effects.get(i)).getTypeItem());
         this.getInventory().setItem(i + 18, ((FireworkEditor.FireworkEffectData)this.effects.get(i)).getColorsItem());
         this.getInventory().setItem(i + 27, ((FireworkEditor.FireworkEffectData)this.effects.get(i)).getFadeColorsItem());
         this.getInventory().setItem(i + 36, ((FireworkEditor.FireworkEffectData)this.effects.get(i)).getTrailFlickerItem());
         if (((FireworkEditor.FireworkEffectData)this.effects.get(i)).active && !((FireworkEditor.FireworkEffectData)this.effects.get(i)).colors.isEmpty()) {
            this.meta.addEffect(((FireworkEditor.FireworkEffectData)this.effects.get(i)).getEffect());
         }
      }

      this.firework.setItemMeta(this.meta);
      this.getInventory().setItem(49, this.firework);

      ItemStack item;
      try {
         item = new ItemStack(Material.GUNPOWDER);
      } catch (Throwable var3) {
         item = new ItemStack(Material.valueOf("SULPHUR"));
      }

      item.setAmount(this.meta.getPower() + 1);
      ItemMeta powerMeta = ItemUtils.getMeta(item);
      powerMeta.addItemFlags(ItemFlag.values());
      this.loadLanguageDescription(powerMeta, "gui.firework.buttons.power", new String[]{"%power%", String.valueOf(this.meta.getPower() + 1)});
      item.setItemMeta(powerMeta);
      this.getInventory().setItem(47, item);
   }

   @NotNull
   public Inventory getInventory() {
      return this.inventory;
   }

   public Player getTargetPlayer() {
      return this.target;
   }

   private class FireworkEffectData {
      private final List<DyeColor> colors;
      private final List<DyeColor> fadeColors;
      private Type type;
      private boolean flicker;
      private boolean trail;
      private boolean active;

      private FireworkEffectData() {
         this((Type)null, (List)null, (List)null, false, false);
      }

      private FireworkEffectData(FireworkEffect param2) {
         this(effect.getType(), FireworkEditor.translateToDyeColor(effect.getColors()), FireworkEditor.translateToDyeColor(effect.getFadeColors()), effect.hasFlicker(), effect.hasTrail());
         this.active = true;
      }

      private FireworkEffectData(Type param2, List<DyeColor> param3, List<DyeColor> param4, boolean param5, boolean param6) {
         this.colors = new ArrayList();
         this.fadeColors = new ArrayList();
         this.active = false;
         if (type != null) {
            this.type = type;
         } else {
            this.type = Type.values()[(int)(Math.random() * (double)Type.values().length)];
         }

         if (colors != null) {
            this.colors.addAll(colors);
         }

         if (this.colors.isEmpty()) {
            this.colors.add(DyeColor.values()[(int)(Math.random() * (double)DyeColor.values().length)]);
         }

         if (fadeColors != null) {
            this.fadeColors.addAll(fadeColors);
         }

         this.flicker = flicker;
         this.trail = trail;
      }

      public ItemStack getTypeItem() {
         if (!this.active) {
            return null;
         } else {
            ItemStack item;
            switch(this.type) {
            case BALL:
               try {
                  item = new ItemStack(Material.FIREWORK_STAR);
               } catch (Throwable var5) {
                  item = new ItemStack(Material.valueOf("FIREWORK_CHARGE"));
               }
               break;
            case BALL_LARGE:
               try {
                  item = new ItemStack(Material.FIRE_CHARGE);
               } catch (Exception var4) {
                  item = new ItemStack(Material.valueOf("FIREBALL"));
               }
               break;
            case BURST:
               item = new ItemStack(Material.FEATHER);
               break;
            case CREEPER:
               try {
                  item = new ItemStack(Material.CREEPER_HEAD);
               } catch (Throwable var3) {
                  item = new ItemStack(Material.valueOf("SKULL"), 1, (short)0, (byte)4);
               }
               break;
            case STAR:
               item = new ItemStack(Material.GOLD_NUGGET);
               break;
            default:
               item = new ItemStack(Material.ARROW);
            }

            ItemMeta meta = ItemUtils.getMeta(item);
            meta.addItemFlags(ItemFlag.values());
            FireworkEditor.this.loadLanguageDescription(meta, "gui.firework.buttons.type", new String[]{"%type%", Aliases.FIREWORK_TYPE.getName((Enum)this.type)});
            item.setItemMeta(meta);
            return item;
         }
      }

      public ItemStack getColorsItem() {
         if (!this.active) {
            return null;
         } else {
            ItemStack item = Util.getDyeItemFromColor(!this.colors.isEmpty() ? DyeColor.LIGHT_BLUE : DyeColor.RED);
            ItemMeta meta = ItemUtils.getMeta(item);
            meta.addItemFlags(ItemFlag.values());
            List<String> colorNames = new ArrayList();
            Iterator var4 = this.colors.iterator();

            while(var4.hasNext()) {
               DyeColor color = (DyeColor)var4.next();
               colorNames.add(Aliases.COLOR.getName(color));
            }

            FireworkEditor.this.loadLanguageDescription(meta, "gui.firework.buttons.colors", new String[]{"%colors%", String.join("&b, &e", colorNames)});
            item.setItemMeta(meta);
            item.setAmount(Math.max(Math.min(101, this.colors.size()), 1));
            return item;
         }
      }

      public ItemStack getFadeColorsItem() {
         if (!this.active) {
            return null;
         } else {
            ItemStack item = Util.getDyeItemFromColor(DyeColor.BLUE);
            ItemMeta meta = ItemUtils.getMeta(item);
            meta.addItemFlags(ItemFlag.values());
            List<String> colorNames = new ArrayList();
            Iterator var4 = this.fadeColors.iterator();

            while(var4.hasNext()) {
               DyeColor color = (DyeColor)var4.next();
               colorNames.add(Aliases.COLOR.getName(color));
            }

            FireworkEditor.this.loadLanguageDescription(meta, "gui.firework.buttons.fadecolors", new String[]{"%colors%", String.join("&b, &e", colorNames)});
            item.setItemMeta(meta);
            item.setAmount(Math.max(Math.min(101, this.fadeColors.size()), 1));
            return item;
         }
      }

      public ItemStack getTrailFlickerItem() {
         if (!this.active) {
            return null;
         } else {
            ItemStack item = this.trail ? new ItemStack(Material.DIAMOND) : Util.getDyeItemFromColor(DyeColor.GRAY);
            ItemMeta meta = ItemUtils.getMeta(item);
            meta.addItemFlags(ItemFlag.values());
            if (this.flicker) {
               meta.addEnchant(Enchantment.LURE, 1, true);
            }

            FireworkEditor.this.loadLanguageDescription(meta, "gui.firework.buttons.flags.info", new String[]{"%status%", FireworkEditor.this.getLanguageMessage("gui.firework.buttons.flags." + (this.trail ? (this.flicker ? "both" : "trail") : (this.flicker ? "flicker" : "none")), new String[0])});
            item.setItemMeta(meta);
            return item;
         }
      }

      public ItemStack getPositionItem() {
         ItemStack item = Util.getDyeItemFromColor(this.active ? (this.colors.isEmpty() ? DyeColor.RED : DyeColor.LIME) : DyeColor.GRAY);
         ItemMeta meta = ItemUtils.getMeta(item);
         meta.addItemFlags(ItemFlag.values());
         FireworkEditor.this.loadLanguageDescription(meta, "gui.firework.buttons.position", new String[]{"%middle_click%", FireworkEditor.this.getLanguageMessage("gui.middleclick." + (FireworkEditor.this.getTargetPlayer().getGameMode() == GameMode.CREATIVE ? "creative" : "other"), new String[0])});
         item.setItemMeta(meta);
         return item;
      }

      public FireworkEffect getEffect() {
         return FireworkEffect.builder().with(this.type).flicker(this.flicker).trail(this.trail).withFade(FireworkEditor.translateToColor(this.fadeColors)).withColor(FireworkEditor.translateToColor(this.colors)).build();
      }

      public void onClick(int line, InventoryClickEvent event) {
         switch(line) {
         case 1:
            if (event.isLeftClick()) {
               this.type = Type.values()[(this.type.ordinal() + 1) % Type.values().length];
            } else {
               this.type = Type.values()[(this.type.ordinal() - 1 + Type.values().length) % Type.values().length];
            }

            return;
         case 2:
            FireworkEditor.this.getTargetPlayer().openInventory((new ColorListSelectorGui(FireworkEditor.this, this.colors)).getInventory());
            return;
         case 3:
            FireworkEditor.this.getTargetPlayer().openInventory((new ColorListSelectorGui(FireworkEditor.this, this.fadeColors)).getInventory());
            return;
         case 4:
            if (event.isLeftClick()) {
               this.trail = !this.trail;
            }

            if (event.isRightClick()) {
               this.flicker = !this.flicker;
            }

            return;
         default:
         }
      }

      // $FF: synthetic method
      FireworkEffectData(FireworkEffect x1, Object x2) {
         this((FireworkEffect)x1);
      }

      // $FF: synthetic method
      FireworkEffectData(Object x1) {
         this();
      }
   }
}
