package com.volmit.iris.core.link.data;

import com.ssomar.score.api.executableitems.ExecutableItemsAPI;
import com.volmit.iris.Iris;
import com.volmit.iris.core.link.ExternalDataProvider;
import com.volmit.iris.core.link.Identifier;
import com.volmit.iris.util.collection.KMap;
import java.util.Collection;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Optional;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ExecutableItemsDataProvider extends ExternalDataProvider {
   public ExecutableItemsDataProvider() {
      super("ExecutableItems");
   }

   public void init() {
      Iris.info("Setting up ExecutableItems Link...");
   }

   @NotNull
   public ItemStack getItemStack(@NotNull Identifier itemId, @NotNull KMap<String, Object> customNbt) {
      return (ItemStack)ExecutableItemsAPI.getExecutableItemsManager().getExecutableItem(var1.key()).map((var0) -> {
         return var0.buildItem(1, Optional.empty());
      }).orElseThrow(() -> {
         return new MissingResourceException("Failed to find ItemData!", var1.namespace(), var1.key());
      });
   }

   @NotNull
   public Collection<Identifier> getTypes(@NotNull DataType dataType) {
      return var1 != DataType.ITEM ? List.of() : ExecutableItemsAPI.getExecutableItemsManager().getExecutableItemIdsList().stream().map((var0) -> {
         return new Identifier("executable_items", var0);
      }).filter(var1.asPredicate(this)).toList();
   }

   public boolean isValidProvider(@NotNull Identifier key, DataType dataType) {
      return var1.namespace().equalsIgnoreCase("executable_items") && var2 == DataType.ITEM;
   }
}
