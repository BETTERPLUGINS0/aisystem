package com.volmit.iris.core.link.data;

import com.volmit.iris.Iris;
import com.volmit.iris.core.link.ExternalDataProvider;
import com.volmit.iris.core.link.Identifier;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.reflect.WrappedField;
import com.willfp.ecoitems.items.EcoItem;
import com.willfp.ecoitems.items.EcoItems;
import java.util.Collection;
import java.util.List;
import java.util.MissingResourceException;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EcoItemsDataProvider extends ExternalDataProvider {
   private WrappedField<EcoItem, ItemStack> itemStack;
   private WrappedField<EcoItem, NamespacedKey> id;

   public EcoItemsDataProvider() {
      super("EcoItems");
   }

   public void init() {
      Iris.info("Setting up EcoItems Link...");
      this.itemStack = new WrappedField(EcoItem.class, "_itemStack");
      if (this.itemStack.hasFailed()) {
         Iris.error("Failed to set up EcoItems Link: Unable to fetch ItemStack field!");
      }

      this.id = new WrappedField(EcoItem.class, "id");
      if (this.id.hasFailed()) {
         Iris.error("Failed to set up EcoItems Link: Unable to fetch id field!");
      }

   }

   @NotNull
   public ItemStack getItemStack(@NotNull Identifier itemId, @NotNull KMap<String, Object> customNbt) {
      EcoItem var3 = EcoItems.INSTANCE.getByID(var1.key());
      if (var3 == null) {
         throw new MissingResourceException("Failed to find Item!", var1.namespace(), var1.key());
      } else {
         return ((ItemStack)this.itemStack.get(var3)).clone();
      }
   }

   @NotNull
   public Collection<Identifier> getTypes(@NotNull DataType dataType) {
      return var1 != DataType.ITEM ? List.of() : EcoItems.INSTANCE.values().stream().map((var1x) -> {
         return Identifier.fromNamespacedKey((NamespacedKey)this.id.get(var1x));
      }).filter(var1.asPredicate(this)).toList();
   }

   public boolean isValidProvider(@NotNull Identifier id, DataType dataType) {
      return var1.namespace().equalsIgnoreCase("ecoitems") && var2 == DataType.ITEM;
   }
}
