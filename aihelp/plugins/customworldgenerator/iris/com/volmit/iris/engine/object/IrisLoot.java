package com.volmit.iris.engine.object;

import com.volmit.iris.Iris;
import com.volmit.iris.core.link.Identifier;
import com.volmit.iris.core.nms.INMS;
import com.volmit.iris.core.service.ExternalDataSVC;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.RegistryListItemType;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.data.B;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.noise.CNG;
import java.awt.Color;
import java.util.Iterator;
import java.util.Optional;
import lombok.Generated;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.Colorable;

@Snippet("loot")
@Desc("Represents a loot entry")
public class IrisLoot {
   private final transient AtomicCache<CNG> chance = new AtomicCache();
   @Desc("The target inventory slot types to fill this loot with")
   private InventorySlotType slotTypes;
   @MinNumber(1.0D)
   @Desc("The sub rarity of this loot. Calculated after this loot table has been picked.")
   private int rarity;
   @MinNumber(1.0D)
   @Desc("Minimum amount of this loot")
   private int minAmount;
   @MinNumber(1.0D)
   @Desc("Maximum amount of this loot")
   private int maxAmount;
   @MinNumber(1.0D)
   @Desc("The display name of this item")
   private String displayName;
   @MinNumber(0.0D)
   @MaxNumber(1.0D)
   @Desc("Minimum durability percent")
   private double minDurability;
   @MinNumber(0.0D)
   @MaxNumber(1.0D)
   @Desc("Maximum durability percent")
   private double maxDurability;
   @Desc("Define a custom model identifier 1.14+ only")
   private Integer customModel;
   @Desc("Set this to true to prevent it from being broken")
   private boolean unbreakable;
   @ArrayType(
      min = 1,
      type = ItemFlag.class
   )
   @Desc("The item flags to add")
   private KList<ItemFlag> itemFlags;
   @Desc("Apply enchantments to this item")
   @ArrayType(
      min = 1,
      type = IrisEnchantment.class
   )
   private KList<IrisEnchantment> enchantments;
   @Desc("Apply attribute modifiers to this item")
   @ArrayType(
      min = 1,
      type = IrisAttributeModifier.class
   )
   private KList<IrisAttributeModifier> attributes;
   @ArrayType(
      min = 1,
      type = String.class
   )
   @Desc("Add lore to this item")
   private KList<String> lore;
   @RegistryListItemType
   @Required
   @Desc("This is the item or block type. Does not accept minecraft:*, only materials such as DIAMOND_SWORD or DIRT. The exception are modded materials, as they require a namespace.")
   private String type;
   @Desc("The dye color")
   private DyeColor dyeColor;
   @Desc("The leather armor color")
   private String leatherColor;
   @Desc("Defines a custom NBT Tag for the item.")
   private KMap<String, Object> customNbt;

   public Material getType() {
      return B.getMaterial(this.type);
   }

   public ItemStack get(boolean debug, RNG rng) {
      try {
         ItemStack var3 = this.getItemStack(var2);
         if (var3 == null) {
            return new ItemStack(Material.AIR);
         } else {
            var3.setItemMeta(this.applyProperties(var3, var2, var1, (IrisLootTable)null));
            return INMS.get().applyCustomNbt(var3, this.customNbt);
         }
      } catch (Throwable var4) {
         Iris.reportError(var4);
         return new ItemStack(Material.AIR);
      }
   }

   public ItemStack get(boolean debug, boolean giveSomething, IrisLootTable table, RNG rng, int x, int y, int z) {
      if (var1) {
         this.chance.reset();
      }

      if (var2 || ((CNG)this.chance.aquire(() -> {
         return NoiseStyle.STATIC.create(var4);
      })).fit(1, this.rarity * var3.getRarity(), (double)var5, (double)var6, (double)var7) == 1) {
         try {
            ItemStack var8 = this.getItemStack(var4);
            if (var8 == null) {
               return null;
            }

            var8.setItemMeta(this.applyProperties(var8, var4, var1, var3));
            return INMS.get().applyCustomNbt(var8, this.customNbt);
         } catch (Throwable var9) {
            var9.printStackTrace();
         }
      }

      return null;
   }

   private ItemStack getItemStack(RNG rng) {
      if (!this.type.startsWith("minecraft:") && this.type.contains(":")) {
         Optional var2 = ((ExternalDataSVC)Iris.service(ExternalDataSVC.class)).getItemStack(Identifier.fromString(this.type), this.customNbt);
         if (var2.isEmpty()) {
            Iris.warn("Unknown Material: " + this.type);
            return new ItemStack(Material.AIR);
         } else {
            ItemStack var3 = (ItemStack)var2.get();
            var3.setAmount(Math.max(1, var1.i(this.getMinAmount(), this.getMaxAmount())));
            return var3;
         }
      } else {
         return new ItemStack(this.getType(), Math.max(1, var1.i(this.getMinAmount(), this.getMaxAmount())));
      }
   }

   private ItemMeta applyProperties(ItemStack is, RNG rng, boolean debug, IrisLootTable table) {
      ItemMeta var5 = var1.getItemMeta();
      if (var5 == null) {
         return null;
      } else {
         Iterator var6 = this.getEnchantments().iterator();

         while(var6.hasNext()) {
            IrisEnchantment var7 = (IrisEnchantment)var6.next();
            var7.apply(var2, var5);
         }

         var6 = this.getAttributes().iterator();

         while(var6.hasNext()) {
            IrisAttributeModifier var9 = (IrisAttributeModifier)var6.next();
            var9.apply(var2, var5);
         }

         var5.setUnbreakable(this.isUnbreakable());
         var6 = this.getItemFlags().iterator();

         while(var6.hasNext()) {
            ItemFlag var11 = (ItemFlag)var6.next();
            var5.addItemFlags(new ItemFlag[]{var11});
         }

         if (this.getCustomModel() != null) {
            var5.setCustomModelData(this.getCustomModel());
         }

         if (var1.getType().getMaxDurability() > 0 && var5 instanceof Damageable) {
            Damageable var8 = (Damageable)var5;
            short var13 = var1.getType().getMaxDurability();
            var8.setDamage((int)Math.round(Math.max(0.0D, Math.min((double)var13, (1.0D - var2.d(this.getMinDurability(), this.getMaxDurability())) * (double)var13))));
         }

         if (this.getLeatherColor() != null && var5 instanceof LeatherArmorMeta) {
            LeatherArmorMeta var10 = (LeatherArmorMeta)var5;
            Color var15 = Color.decode(this.getLeatherColor());
            var10.setColor(org.bukkit.Color.fromRGB(var15.getRed(), var15.getGreen(), var15.getBlue()));
         }

         if (this.getDyeColor() != null && var5 instanceof Colorable) {
            Colorable var12 = (Colorable)var5;
            var12.setColor(this.getDyeColor());
         }

         if (this.displayName != null) {
            var5.setLocalizedName(C.translateAlternateColorCodes('&', this.displayName));
            var5.setDisplayName(C.translateAlternateColorCodes('&', this.displayName));
         }

         KList var14 = new KList();
         this.getLore().forEach((var1x) -> {
            String var2 = C.translateAlternateColorCodes('&', var1x);
            if (var2.length() > 24) {
               String[] var3 = Form.wrapWords(var2, 24).split("\\Q\n\\E");
               int var4 = var3.length;

               for(int var5 = 0; var5 < var4; ++var5) {
                  String var6 = var3[var5];
                  var14.add((Object)var6.trim());
               }
            } else {
               var14.add((Object)var2);
            }

         });
         if (var3) {
            String var10001;
            if (var4 == null) {
               if (var14.isNotEmpty()) {
                  var14.add((Object)(String.valueOf(C.GRAY) + "--------------------"));
               }

               var10001 = String.valueOf(C.GRAY);
               var14.add((Object)(var10001 + "1 in " + this.getRarity() + " Chance (" + Form.pc(1.0D / (double)this.getRarity(), 5) + ")"));
            } else {
               if (var14.isNotEmpty()) {
                  var14.add((Object)(String.valueOf(C.GRAY) + "--------------------"));
               }

               var10001 = String.valueOf(C.GRAY);
               var14.add((Object)(var10001 + "From: " + var4.getName() + " (" + Form.pc(1.0D / (double)var4.getRarity(), 5) + ")"));
               var10001 = String.valueOf(C.GRAY);
               var14.add((Object)(var10001 + "1 in " + var4.getRarity() * this.getRarity() + " Chance (" + Form.pc(1.0D / (double)(var4.getRarity() * this.getRarity()), 5) + ")"));
            }
         }

         var5.setLore(var14);
         return var5;
      }
   }

   @Generated
   public IrisLoot() {
      this.slotTypes = InventorySlotType.STORAGE;
      this.rarity = 1;
      this.minAmount = 1;
      this.maxAmount = 1;
      this.displayName = null;
      this.minDurability = 0.0D;
      this.maxDurability = 1.0D;
      this.customModel = null;
      this.unbreakable = false;
      this.itemFlags = new KList();
      this.enchantments = new KList();
      this.attributes = new KList();
      this.lore = new KList();
      this.type = "";
      this.dyeColor = null;
      this.leatherColor = null;
   }

   @Generated
   public IrisLoot(final InventorySlotType slotTypes, final int rarity, final int minAmount, final int maxAmount, final String displayName, final double minDurability, final double maxDurability, final Integer customModel, final boolean unbreakable, final KList<ItemFlag> itemFlags, final KList<IrisEnchantment> enchantments, final KList<IrisAttributeModifier> attributes, final KList<String> lore, final String type, final DyeColor dyeColor, final String leatherColor, final KMap<String, Object> customNbt) {
      this.slotTypes = InventorySlotType.STORAGE;
      this.rarity = 1;
      this.minAmount = 1;
      this.maxAmount = 1;
      this.displayName = null;
      this.minDurability = 0.0D;
      this.maxDurability = 1.0D;
      this.customModel = null;
      this.unbreakable = false;
      this.itemFlags = new KList();
      this.enchantments = new KList();
      this.attributes = new KList();
      this.lore = new KList();
      this.type = "";
      this.dyeColor = null;
      this.leatherColor = null;
      this.slotTypes = var1;
      this.rarity = var2;
      this.minAmount = var3;
      this.maxAmount = var4;
      this.displayName = var5;
      this.minDurability = var6;
      this.maxDurability = var8;
      this.customModel = var10;
      this.unbreakable = var11;
      this.itemFlags = var12;
      this.enchantments = var13;
      this.attributes = var14;
      this.lore = var15;
      this.type = var16;
      this.dyeColor = var17;
      this.leatherColor = var18;
      this.customNbt = var19;
   }

   @Generated
   public AtomicCache<CNG> getChance() {
      return this.chance;
   }

   @Generated
   public InventorySlotType getSlotTypes() {
      return this.slotTypes;
   }

   @Generated
   public int getRarity() {
      return this.rarity;
   }

   @Generated
   public int getMinAmount() {
      return this.minAmount;
   }

   @Generated
   public int getMaxAmount() {
      return this.maxAmount;
   }

   @Generated
   public String getDisplayName() {
      return this.displayName;
   }

   @Generated
   public double getMinDurability() {
      return this.minDurability;
   }

   @Generated
   public double getMaxDurability() {
      return this.maxDurability;
   }

   @Generated
   public Integer getCustomModel() {
      return this.customModel;
   }

   @Generated
   public boolean isUnbreakable() {
      return this.unbreakable;
   }

   @Generated
   public KList<ItemFlag> getItemFlags() {
      return this.itemFlags;
   }

   @Generated
   public KList<IrisEnchantment> getEnchantments() {
      return this.enchantments;
   }

   @Generated
   public KList<IrisAttributeModifier> getAttributes() {
      return this.attributes;
   }

   @Generated
   public KList<String> getLore() {
      return this.lore;
   }

   @Generated
   public DyeColor getDyeColor() {
      return this.dyeColor;
   }

   @Generated
   public String getLeatherColor() {
      return this.leatherColor;
   }

   @Generated
   public KMap<String, Object> getCustomNbt() {
      return this.customNbt;
   }

   @Generated
   public IrisLoot setSlotTypes(final InventorySlotType slotTypes) {
      this.slotTypes = var1;
      return this;
   }

   @Generated
   public IrisLoot setRarity(final int rarity) {
      this.rarity = var1;
      return this;
   }

   @Generated
   public IrisLoot setMinAmount(final int minAmount) {
      this.minAmount = var1;
      return this;
   }

   @Generated
   public IrisLoot setMaxAmount(final int maxAmount) {
      this.maxAmount = var1;
      return this;
   }

   @Generated
   public IrisLoot setDisplayName(final String displayName) {
      this.displayName = var1;
      return this;
   }

   @Generated
   public IrisLoot setMinDurability(final double minDurability) {
      this.minDurability = var1;
      return this;
   }

   @Generated
   public IrisLoot setMaxDurability(final double maxDurability) {
      this.maxDurability = var1;
      return this;
   }

   @Generated
   public IrisLoot setCustomModel(final Integer customModel) {
      this.customModel = var1;
      return this;
   }

   @Generated
   public IrisLoot setUnbreakable(final boolean unbreakable) {
      this.unbreakable = var1;
      return this;
   }

   @Generated
   public IrisLoot setItemFlags(final KList<ItemFlag> itemFlags) {
      this.itemFlags = var1;
      return this;
   }

   @Generated
   public IrisLoot setEnchantments(final KList<IrisEnchantment> enchantments) {
      this.enchantments = var1;
      return this;
   }

   @Generated
   public IrisLoot setAttributes(final KList<IrisAttributeModifier> attributes) {
      this.attributes = var1;
      return this;
   }

   @Generated
   public IrisLoot setLore(final KList<String> lore) {
      this.lore = var1;
      return this;
   }

   @Generated
   public IrisLoot setType(final String type) {
      this.type = var1;
      return this;
   }

   @Generated
   public IrisLoot setDyeColor(final DyeColor dyeColor) {
      this.dyeColor = var1;
      return this;
   }

   @Generated
   public IrisLoot setLeatherColor(final String leatherColor) {
      this.leatherColor = var1;
      return this;
   }

   @Generated
   public IrisLoot setCustomNbt(final KMap<String, Object> customNbt) {
      this.customNbt = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisLoot)) {
         return false;
      } else {
         IrisLoot var2 = (IrisLoot)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getRarity() != var2.getRarity()) {
            return false;
         } else if (this.getMinAmount() != var2.getMinAmount()) {
            return false;
         } else if (this.getMaxAmount() != var2.getMaxAmount()) {
            return false;
         } else if (Double.compare(this.getMinDurability(), var2.getMinDurability()) != 0) {
            return false;
         } else if (Double.compare(this.getMaxDurability(), var2.getMaxDurability()) != 0) {
            return false;
         } else if (this.isUnbreakable() != var2.isUnbreakable()) {
            return false;
         } else {
            Integer var3 = this.getCustomModel();
            Integer var4 = var2.getCustomModel();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            label151: {
               InventorySlotType var5 = this.getSlotTypes();
               InventorySlotType var6 = var2.getSlotTypes();
               if (var5 == null) {
                  if (var6 == null) {
                     break label151;
                  }
               } else if (var5.equals(var6)) {
                  break label151;
               }

               return false;
            }

            String var7 = this.getDisplayName();
            String var8 = var2.getDisplayName();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            label137: {
               KList var9 = this.getItemFlags();
               KList var10 = var2.getItemFlags();
               if (var9 == null) {
                  if (var10 == null) {
                     break label137;
                  }
               } else if (var9.equals(var10)) {
                  break label137;
               }

               return false;
            }

            KList var11 = this.getEnchantments();
            KList var12 = var2.getEnchantments();
            if (var11 == null) {
               if (var12 != null) {
                  return false;
               }
            } else if (!var11.equals(var12)) {
               return false;
            }

            KList var13 = this.getAttributes();
            KList var14 = var2.getAttributes();
            if (var13 == null) {
               if (var14 != null) {
                  return false;
               }
            } else if (!var13.equals(var14)) {
               return false;
            }

            KList var15 = this.getLore();
            KList var16 = var2.getLore();
            if (var15 == null) {
               if (var16 != null) {
                  return false;
               }
            } else if (!var15.equals(var16)) {
               return false;
            }

            label109: {
               Material var17 = this.getType();
               Material var18 = var2.getType();
               if (var17 == null) {
                  if (var18 == null) {
                     break label109;
                  }
               } else if (var17.equals(var18)) {
                  break label109;
               }

               return false;
            }

            label102: {
               DyeColor var19 = this.getDyeColor();
               DyeColor var20 = var2.getDyeColor();
               if (var19 == null) {
                  if (var20 == null) {
                     break label102;
                  }
               } else if (var19.equals(var20)) {
                  break label102;
               }

               return false;
            }

            String var21 = this.getLeatherColor();
            String var22 = var2.getLeatherColor();
            if (var21 == null) {
               if (var22 != null) {
                  return false;
               }
            } else if (!var21.equals(var22)) {
               return false;
            }

            KMap var23 = this.getCustomNbt();
            KMap var24 = var2.getCustomNbt();
            if (var23 == null) {
               if (var24 != null) {
                  return false;
               }
            } else if (!var23.equals(var24)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisLoot;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var18 = var2 * 59 + this.getRarity();
      var18 = var18 * 59 + this.getMinAmount();
      var18 = var18 * 59 + this.getMaxAmount();
      long var3 = Double.doubleToLongBits(this.getMinDurability());
      var18 = var18 * 59 + (int)(var3 >>> 32 ^ var3);
      long var5 = Double.doubleToLongBits(this.getMaxDurability());
      var18 = var18 * 59 + (int)(var5 >>> 32 ^ var5);
      var18 = var18 * 59 + (this.isUnbreakable() ? 79 : 97);
      Integer var7 = this.getCustomModel();
      var18 = var18 * 59 + (var7 == null ? 43 : var7.hashCode());
      InventorySlotType var8 = this.getSlotTypes();
      var18 = var18 * 59 + (var8 == null ? 43 : var8.hashCode());
      String var9 = this.getDisplayName();
      var18 = var18 * 59 + (var9 == null ? 43 : var9.hashCode());
      KList var10 = this.getItemFlags();
      var18 = var18 * 59 + (var10 == null ? 43 : var10.hashCode());
      KList var11 = this.getEnchantments();
      var18 = var18 * 59 + (var11 == null ? 43 : var11.hashCode());
      KList var12 = this.getAttributes();
      var18 = var18 * 59 + (var12 == null ? 43 : var12.hashCode());
      KList var13 = this.getLore();
      var18 = var18 * 59 + (var13 == null ? 43 : var13.hashCode());
      Material var14 = this.getType();
      var18 = var18 * 59 + (var14 == null ? 43 : var14.hashCode());
      DyeColor var15 = this.getDyeColor();
      var18 = var18 * 59 + (var15 == null ? 43 : var15.hashCode());
      String var16 = this.getLeatherColor();
      var18 = var18 * 59 + (var16 == null ? 43 : var16.hashCode());
      KMap var17 = this.getCustomNbt();
      var18 = var18 * 59 + (var17 == null ? 43 : var17.hashCode());
      return var18;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getChance());
      return "IrisLoot(chance=" + var10000 + ", slotTypes=" + String.valueOf(this.getSlotTypes()) + ", rarity=" + this.getRarity() + ", minAmount=" + this.getMinAmount() + ", maxAmount=" + this.getMaxAmount() + ", displayName=" + this.getDisplayName() + ", minDurability=" + this.getMinDurability() + ", maxDurability=" + this.getMaxDurability() + ", customModel=" + this.getCustomModel() + ", unbreakable=" + this.isUnbreakable() + ", itemFlags=" + String.valueOf(this.getItemFlags()) + ", enchantments=" + String.valueOf(this.getEnchantments()) + ", attributes=" + String.valueOf(this.getAttributes()) + ", lore=" + String.valueOf(this.getLore()) + ", type=" + String.valueOf(this.getType()) + ", dyeColor=" + String.valueOf(this.getDyeColor()) + ", leatherColor=" + this.getLeatherColor() + ", customNbt=" + String.valueOf(this.getCustomNbt()) + ")";
   }
}
