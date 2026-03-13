package com.volmit.iris.core.link.data;

import com.volmit.iris.Iris;
import com.volmit.iris.core.link.ExternalDataProvider;
import com.volmit.iris.core.link.Identifier;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.data.IrisCustomData;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import java.util.Collection;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemAdderDataProvider extends ExternalDataProvider {
   private volatile Set<String> itemNamespaces = Set.of();
   private volatile Set<String> blockNamespaces = Set.of();

   public ItemAdderDataProvider() {
      super("ItemsAdder");
   }

   public void init() {
      this.updateNamespaces();
   }

   @EventHandler
   public void onLoadData(ItemsAdderLoadDataEvent event) {
      this.updateNamespaces();
   }

   @NotNull
   public BlockData getBlockData(@NotNull Identifier blockId, @NotNull KMap<String, String> state) {
      CustomBlock var3 = CustomBlock.getInstance(var1.toString());
      if (var3 == null) {
         throw new MissingResourceException("Failed to find BlockData!", var1.namespace(), var1.key());
      } else {
         return IrisCustomData.of(var3.getBaseBlockData(), var1);
      }
   }

   @NotNull
   public ItemStack getItemStack(@NotNull Identifier itemId, @NotNull KMap<String, Object> customNbt) {
      CustomStack var3 = CustomStack.getInstance(var1.toString());
      if (var3 == null) {
         throw new MissingResourceException("Failed to find ItemData!", var1.namespace(), var1.key());
      } else {
         return var3.getItemStack();
      }
   }

   public void processUpdate(@NotNull Engine engine, @NotNull Block block, @NotNull Identifier blockId) {
      CustomBlock var4;
      if ((var4 = CustomBlock.place(var3.toString(), var2.getLocation())) != null) {
         var2.setBlockData(var4.getBaseBlockData(), false);
      }
   }

   @NotNull
   public Collection<Identifier> getTypes(@NotNull DataType dataType) {
      List var10000;
      switch(var1) {
      case ENTITY:
         var10000 = List.of();
         break;
      case ITEM:
         var10000 = CustomStack.getNamespacedIdsInRegistry().stream().map(Identifier::fromString).toList();
         break;
      case BLOCK:
         var10000 = CustomBlock.getNamespacedIdsInRegistry().stream().map(Identifier::fromString).toList();
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   private void updateNamespaces() {
      try {
         this.updateNamespaces(DataType.ITEM);
         this.updateNamespaces(DataType.BLOCK);
      } catch (Throwable var2) {
         Iris.warn("Failed to update ItemAdder namespaces: " + var2.getMessage());
      }

   }

   private void updateNamespaces(DataType dataType) {
      Set var2 = (Set)this.getTypes(var1).stream().map(Identifier::namespace).collect(Collectors.toSet());
      if (var1 == DataType.ITEM) {
         this.itemNamespaces = var2;
      } else {
         this.blockNamespaces = var2;
      }

      String var10000 = String.valueOf(var1);
      Iris.debug("Updated ItemAdder namespaces: " + var10000 + " - " + String.valueOf(var2));
   }

   public boolean isValidProvider(@NotNull Identifier id, DataType dataType) {
      if (var2 == DataType.ENTITY) {
         return false;
      } else {
         return var2 == DataType.ITEM ? this.itemNamespaces.contains(var1.namespace()) : this.blockNamespaces.contains(var1.namespace());
      }
   }
}
