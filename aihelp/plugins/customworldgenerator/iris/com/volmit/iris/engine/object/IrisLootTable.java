package com.volmit.iris.engine.object;

import com.volmit.iris.core.loader.IrisRegistrant;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.plugin.VolmitSender;
import lombok.Generated;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

@Desc("Represents a loot table. Biomes, Regions & Objects can add or replace the virtual table with these loot tables")
public class IrisLootTable extends IrisRegistrant {
   @Required
   @Desc("The name of this loot table")
   @MinNumber(2.0D)
   private String name = "";
   @MinNumber(1.0D)
   @Desc("The rarity as in 1 in X chance")
   private int rarity = 1;
   @MinNumber(1.0D)
   @Desc("The maximum amount of loot that can be picked in this table at a time.")
   private int maxPicked = 5;
   @MinNumber(0.0D)
   @Desc("The minimum amount of loot that can be picked in this table at a time.")
   private int minPicked = 1;
   @MinNumber(1.0D)
   @Desc("The maximum amount of tries to generate loot")
   private int maxTries = 10;
   @Desc("The loot in this table")
   @ArrayType(
      min = 1,
      type = IrisLoot.class
   )
   private KList<IrisLoot> loot = new KList();

   public KList<ItemStack> getLoot(boolean debug, RNG rng, InventorySlotType slot, World world, int x, int y, int z) {
      KList var8 = new KList();
      int var9 = 0;
      int var10 = 0;
      int var11 = var2.i(this.getMinPicked(), this.getMaxPicked());

      while(var9 < var11 && var10++ < this.getMaxTries()) {
         int var12 = var2.i(this.loot.size());
         IrisLoot var13 = (IrisLoot)this.loot.get(var12);
         if (var13.getSlotTypes() == var3) {
            ItemStack var14 = var13.get(var1, false, this, var2, var5, var6, var7);
            if (var14 != null && var14.getType() != Material.AIR) {
               var8.add((Object)var14);
               ++var9;
            }
         }
      }

      return var8;
   }

   public String getFolderName() {
      return "loot";
   }

   public String getTypeName() {
      return "Loot";
   }

   public void scanForErrors(JSONObject p, VolmitSender sender) {
   }

   @Generated
   public IrisLootTable() {
   }

   @Generated
   public IrisLootTable(final String name, final int rarity, final int maxPicked, final int minPicked, final int maxTries, final KList<IrisLoot> loot) {
      this.name = var1;
      this.rarity = var2;
      this.maxPicked = var3;
      this.minPicked = var4;
      this.maxTries = var5;
      this.loot = var6;
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public int getRarity() {
      return this.rarity;
   }

   @Generated
   public int getMaxPicked() {
      return this.maxPicked;
   }

   @Generated
   public int getMinPicked() {
      return this.minPicked;
   }

   @Generated
   public int getMaxTries() {
      return this.maxTries;
   }

   @Generated
   public KList<IrisLoot> getLoot() {
      return this.loot;
   }

   @Generated
   public IrisLootTable setName(final String name) {
      this.name = var1;
      return this;
   }

   @Generated
   public IrisLootTable setRarity(final int rarity) {
      this.rarity = var1;
      return this;
   }

   @Generated
   public IrisLootTable setMaxPicked(final int maxPicked) {
      this.maxPicked = var1;
      return this;
   }

   @Generated
   public IrisLootTable setMinPicked(final int minPicked) {
      this.minPicked = var1;
      return this;
   }

   @Generated
   public IrisLootTable setMaxTries(final int maxTries) {
      this.maxTries = var1;
      return this;
   }

   @Generated
   public IrisLootTable setLoot(final KList<IrisLoot> loot) {
      this.loot = var1;
      return this;
   }

   @Generated
   public String toString() {
      String var10000 = this.getName();
      return "IrisLootTable(name=" + var10000 + ", rarity=" + this.getRarity() + ", maxPicked=" + this.getMaxPicked() + ", minPicked=" + this.getMinPicked() + ", maxTries=" + this.getMaxTries() + ", loot=" + String.valueOf(this.getLoot()) + ")";
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisLootTable)) {
         return false;
      } else {
         IrisLootTable var2 = (IrisLootTable)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getRarity() != var2.getRarity()) {
            return false;
         } else if (this.getMaxPicked() != var2.getMaxPicked()) {
            return false;
         } else if (this.getMinPicked() != var2.getMinPicked()) {
            return false;
         } else if (this.getMaxTries() != var2.getMaxTries()) {
            return false;
         } else {
            String var3 = this.getName();
            String var4 = var2.getName();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            KList var5 = this.getLoot();
            KList var6 = var2.getLoot();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisLootTable;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var5 = var2 * 59 + this.getRarity();
      var5 = var5 * 59 + this.getMaxPicked();
      var5 = var5 * 59 + this.getMinPicked();
      var5 = var5 * 59 + this.getMaxTries();
      String var3 = this.getName();
      var5 = var5 * 59 + (var3 == null ? 43 : var3.hashCode());
      KList var4 = this.getLoot();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }
}
