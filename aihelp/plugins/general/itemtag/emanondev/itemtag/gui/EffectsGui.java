package emanondev.itemtag.gui;

import emanondev.itemedit.aliases.Aliases;
import emanondev.itemedit.gui.PagedGui;
import emanondev.itemedit.utility.ItemUtils;
import emanondev.itemedit.utility.VersionUtils;
import emanondev.itemtag.EffectsInfo;
import emanondev.itemtag.ItemTag;
import emanondev.itemtag.ItemTagUtility;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class EffectsGui implements PagedGui {
   private static final int EFFECTS_SLOTS = 45;
   private final Player target;
   private final Inventory inventory;
   private final List<EffectsGui.EffectData> effects = new ArrayList();
   private final List<EffectsGui.EquipData> equips = new ArrayList();
   private final EffectsInfo info;
   private int page = 1;

   public EffectsGui(Player target, ItemStack item) {
      String title = this.getLanguageMessage("gui.effects.title", new String[]{"%player_name%", target.getName()});
      this.inventory = Bukkit.createInventory(this, 54, title);
      this.target = target;
      this.info = new EffectsInfo(item);
      PotionEffectType[] var4 = PotionEffectType.values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         PotionEffectType type = var4[var6];
         if (type != null) {
            if (this.info.hasEffect(type)) {
               PotionEffect effect = this.info.getEffect(type);
               if (VersionUtils.isVersionAfter(1, 13)) {
                  this.effects.add(new EffectsGui.EffectData(type, effect.getAmplifier(), effect.isAmbient(), effect.hasParticles(), effect.hasIcon()));
               } else {
                  this.effects.add(new EffectsGui.EffectData(type, effect.getAmplifier(), effect.isAmbient(), effect.hasParticles(), true));
               }
            } else {
               this.effects.add(new EffectsGui.EffectData(type, -1, true, true, true));
            }
         }
      }

      Iterator var9 = ItemTagUtility.getPlayerEquipmentSlots().iterator();

      while(var9.hasNext()) {
         EquipmentSlot slot = (EquipmentSlot)var9.next();
         this.equips.add(new EffectsGui.EquipData(slot));
      }

      this.effects.sort((e1, e2) -> {
         return Aliases.POTION_EFFECT.getName(e1.type).compareToIgnoreCase(Aliases.POTION_EFFECT.getName(e2.type));
      });
      this.updateInventory();
   }

   public void onClose(InventoryCloseEvent event) {
   }

   public void onClick(InventoryClickEvent event) {
      if (event.getWhoClicked().equals(this.target)) {
         if (this.inventory.equals(event.getClickedInventory())) {
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
               if (event.getClick() != ClickType.DOUBLE_CLICK) {
                  if (event.getSlot() == 46) {
                     this.setPage(this.getPage() - 1);
                  } else if (event.getSlot() == 47) {
                     this.setPage(this.getPage() + 1);
                  } else if (event.getSlot() > 45) {
                     ((EffectsGui.EquipData)this.equips.get(this.inventory.getSize() - event.getSlot() - 1)).onClick(event);
                  } else {
                     ((EffectsGui.EffectData)this.effects.get(event.getSlot() + (this.page - 1) * 45)).onClick(event);
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
      int i;
      for(i = 0; i + (this.page - 1) * 45 < this.effects.size() && i < 45; ++i) {
         this.inventory.setItem(i, ((EffectsGui.EffectData)this.effects.get(i + (this.page - 1) * 45)).getItem());
      }

      for(i = 0; i < this.equips.size(); ++i) {
         this.inventory.setItem(this.inventory.getSize() - 1 - i, ((EffectsGui.EquipData)this.equips.get(i)).getItem());
      }

      if (this.page > 1) {
         this.inventory.setItem(46, this.getPreviousPageItem());
      }

      if (this.page < this.getMaxPage()) {
         this.inventory.setItem(47, this.getNextPageItem());
      }

   }

   private void update(EffectsGui.EquipData equipData) {
      this.info.toggleSlot(equipData.slot);
      this.info.update();
      this.target.setItemInHand(this.info.getItem());
      this.updateInventory();
   }

   private void update(EffectsGui.EffectData effectData) {
      if (effectData.amplifier < 0) {
         this.info.removeEffect(effectData.type);
      } else {
         this.info.addEffect(EffectsInfo.craftPotionEffect(effectData.type, effectData.amplifier, effectData.ambient, effectData.particles, effectData.icon));
      }

      this.info.update();
      this.target.setItemInHand(this.info.getItem());
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

   public int getPage() {
      return this.page;
   }

   public void setPage(int page) {
      page = Math.max(1, Math.min(page, this.getMaxPage()));
      if (page != this.page) {
         this.inventory.clear();
         this.page = page;
         this.updateInventory();
      }

   }

   public int getMaxPage() {
      return this.effects.size() / 45 + (this.effects.size() % 45 == 0 ? 0 : 1);
   }

   private class EffectData {
      private final PotionEffectType type;
      private final ItemStack item;
      private int amplifier;
      private boolean ambient;
      private boolean particles;
      private boolean icon;

      private EffectData(PotionEffectType param2, int param3, boolean param4, boolean param5, boolean param6) {
         this.type = type;
         this.amplifier = amplifier;
         this.ambient = ambient;
         this.particles = particles;
         this.icon = icon;
         this.item = new ItemStack(Material.POTION);
      }

      public ItemStack getItem() {
         PotionMeta meta = (PotionMeta)ItemUtils.getMeta(this.item);
         if (VersionUtils.isVersionAfter(1, 11)) {
            meta.setColor(this.type.getColor());
         }

         meta.addItemFlags(ItemFlag.values());
         EffectsGui.this.loadLanguageDescription(meta, "gui.effects.potion", new String[]{"%effect%", Aliases.POTION_EFFECT.getName(this.type).replace("_", " "), "%level%", String.valueOf(this.amplifier + 1), "%particles%", Aliases.BOOLEAN.getName(this.particles), "%ambient%", Aliases.BOOLEAN.getName(this.ambient), "%icon%", VersionUtils.isVersionAfter(1, 13) ? Aliases.BOOLEAN.getName(this.icon) : EffectsGui.this.getLanguageMessage("gui.effects.icon-unsupported", new String[0]), "%duration%", EffectsGui.this.getLanguageMessage(this.type.isInstant() ? "gui.effects.potion-instant" : "gui.effects.potion-unlimited", new String[0]), "%middle_click%", EffectsGui.this.getLanguageMessage("gui.middleclick." + (EffectsGui.this.getTargetPlayer().getGameMode() == GameMode.CREATIVE ? "creative" : "other"), new String[0])});
         this.item.setAmount(Math.max(1, this.amplifier + 1));
         meta.clearCustomEffects();
         if (this.amplifier >= 0) {
            meta.addCustomEffect(new PotionEffect(this.type, 1, 0), true);
         }

         this.item.setItemMeta(meta);
         return this.item;
      }

      public void onClick(InventoryClickEvent event) {
         switch(event.getClick()) {
         case LEFT:
            this.amplifier = Math.min(Math.max(-1, this.amplifier - 1), 127);
            break;
         case RIGHT:
            this.amplifier = Math.min(Math.max(-1, this.amplifier + 1), 127);
            break;
         case NUMBER_KEY:
            if (event.getHotbarButton() != 0) {
               return;
            }
         case CREATIVE:
         case MIDDLE:
            if (!VersionUtils.isVersionAfter(1, 13)) {
               return;
            }

            this.icon = !this.icon;
            break;
         case SHIFT_LEFT:
            this.particles = !this.particles;
            break;
         case SHIFT_RIGHT:
            this.ambient = !this.ambient;
            break;
         default:
            return;
         }

         EffectsGui.this.update(this);
      }

      // $FF: synthetic method
      EffectData(PotionEffectType x1, int x2, boolean x3, boolean x4, boolean x5, Object x6) {
         this(x1, x2, x3, x4, x5);
      }
   }

   private class EquipData {
      private final EquipmentSlot slot;
      private final ItemStack item;

      private EquipData(EquipmentSlot param2) {
         this.slot = slot;
         switch(slot) {
         case CHEST:
            this.item = new ItemStack(Material.IRON_CHESTPLATE);
            break;
         case FEET:
            this.item = new ItemStack(Material.IRON_BOOTS);
            break;
         case HAND:
            this.item = new ItemStack(Material.IRON_SWORD);
            break;
         case HEAD:
            this.item = new ItemStack(Material.IRON_HELMET);
            break;
         case LEGS:
            this.item = new ItemStack(Material.IRON_LEGGINGS);
            break;
         case OFF_HAND:
            this.item = new ItemStack(Material.SHIELD);
            break;
         default:
            throw new IllegalArgumentException();
         }

         ItemMeta meta = this.item.getItemMeta();
         meta.addItemFlags(ItemFlag.values());
         this.item.setItemMeta(meta);
      }

      public ItemStack getItem() {
         ItemMeta meta = EffectsGui.this.loadLanguageDescription(ItemUtils.getMeta(this.item), "gui.effects.slot", new String[]{"%slot%", Aliases.EQUIPMENT_SLOTS.getName(this.slot), "%value%", Aliases.BOOLEAN.getName(EffectsGui.this.info.isValidSlot(this.slot))});
         if (EffectsGui.this.info.isValidSlot(this.slot)) {
            meta.addEnchant(Enchantment.LURE, 1, true);
         } else {
            meta.removeEnchant(Enchantment.LURE);
         }

         this.item.setItemMeta(meta);
         return this.item;
      }

      public void onClick(InventoryClickEvent event) {
         EffectsGui.this.update(this);
      }

      // $FF: synthetic method
      EquipData(EquipmentSlot x1, Object x2) {
         this(x1);
      }
   }
}
