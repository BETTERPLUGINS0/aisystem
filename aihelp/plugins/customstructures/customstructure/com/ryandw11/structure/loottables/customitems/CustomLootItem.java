package com.ryandw11.structure.loottables.customitems;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.exceptions.LootTableException;
import com.ryandw11.structure.loottables.ConfigLootItem;
import com.ryandw11.structure.loottables.LootTable;
import java.util.logging.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class CustomLootItem extends ConfigLootItem {
   private ItemStack itemStack;

   public CustomLootItem(LootTable lootTable, String itemID, int weight, String amount) {
      super(lootTable, itemID, weight, amount);
   }

   public void constructItem(ConfigurationSection configurationSection) {
      ItemStack item = CustomStructures.getInstance().getCustomItemManager().getItem(configurationSection.getString("Key"));
      if (item == null) {
         Logger var10000 = CustomStructures.getInstance().getLogger();
         String var10001 = this.getItemID();
         var10000.warning("Cannot find a custom item with the id of " + var10001 + " in the " + this.getLootTable().getName() + " loot table!");
         throw new LootTableException("Cannot find a custom item with the id of " + this.getItemID() + "!");
      } else {
         this.itemStack = item;
      }
   }

   public ItemStack getItemStack() {
      ItemStack clone = this.itemStack.clone();
      clone.setAmount(this.getAmount());
      return clone;
   }
}
