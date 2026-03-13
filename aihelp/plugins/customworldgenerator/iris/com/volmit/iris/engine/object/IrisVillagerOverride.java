package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.DependsOn;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KList;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.Generated;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Snippet("villager-override")
@Desc("Override cartographer map trades with others or disable the trade altogether")
public class IrisVillagerOverride {
   @Desc("Disable the trade altogether.\nIf a cartographer villager gets a new explorer map trade:\nIf this is enabled -> the trade is removed\nIf this is disabled -> the trade is replaced with the \"override\" setting below\nDefault is true, so if you omit this, trades will be removed.")
   private boolean disableTrade = true;
   @DependsOn({"disableTrade"})
   @Required
   @Desc("The items to override the cartographer trade with.\nBy default, this is:\n    3 emeralds + 3 glass blocks -> 1 spyglass.\n    Can trade 3 to 5 times")
   @ArrayType(
      min = 1,
      type = IrisVillagerTrade.class
   )
   private KList<IrisVillagerTrade> items;

   public KList<IrisVillagerTrade> getValidItems() {
      KList var1 = new KList();
      Stream var10000 = this.getItems().stream().filter(IrisVillagerTrade::isValidItems);
      Objects.requireNonNull(var1);
      var10000.forEach(var1::add);
      return var1.size() == 0 ? null : var1;
   }

   @Generated
   public IrisVillagerOverride() {
      this.items = new KList(new IrisVillagerTrade[]{(new IrisVillagerTrade()).setIngredient1(new ItemStack(Material.EMERALD, 3)).setIngredient2(new ItemStack(Material.GLASS, 3)).setResult(new ItemStack(Material.SPYGLASS)).setMinTrades(3).setMaxTrades(5)});
   }

   @Generated
   public IrisVillagerOverride(final boolean disableTrade, final KList<IrisVillagerTrade> items) {
      this.items = new KList(new IrisVillagerTrade[]{(new IrisVillagerTrade()).setIngredient1(new ItemStack(Material.EMERALD, 3)).setIngredient2(new ItemStack(Material.GLASS, 3)).setResult(new ItemStack(Material.SPYGLASS)).setMinTrades(3).setMaxTrades(5)});
      this.disableTrade = var1;
      this.items = var2;
   }

   @Generated
   public boolean isDisableTrade() {
      return this.disableTrade;
   }

   @Generated
   public KList<IrisVillagerTrade> getItems() {
      return this.items;
   }

   @Generated
   public IrisVillagerOverride setDisableTrade(final boolean disableTrade) {
      this.disableTrade = var1;
      return this;
   }

   @Generated
   public IrisVillagerOverride setItems(final KList<IrisVillagerTrade> items) {
      this.items = var1;
      return this;
   }

   @Generated
   public String toString() {
      boolean var10000 = this.isDisableTrade();
      return "IrisVillagerOverride(disableTrade=" + var10000 + ", items=" + String.valueOf(this.getItems()) + ")";
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisVillagerOverride)) {
         return false;
      } else {
         IrisVillagerOverride var2 = (IrisVillagerOverride)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.isDisableTrade() != var2.isDisableTrade()) {
            return false;
         } else {
            KList var3 = this.getItems();
            KList var4 = var2.getItems();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisVillagerOverride;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var4 = var2 * 59 + (this.isDisableTrade() ? 79 : 97);
      KList var3 = this.getItems();
      var4 = var4 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }
}
