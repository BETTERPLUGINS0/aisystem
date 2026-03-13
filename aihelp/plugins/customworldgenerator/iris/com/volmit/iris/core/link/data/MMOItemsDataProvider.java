package com.volmit.iris.core.link.data;

import com.volmit.iris.Iris;
import com.volmit.iris.core.link.ExternalDataProvider;
import com.volmit.iris.core.link.Identifier;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.scheduling.J;
import java.util.Collection;
import java.util.List;
import java.util.MissingResourceException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.ItemTier;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.block.CustomBlock;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MMOItemsDataProvider extends ExternalDataProvider {
   public MMOItemsDataProvider() {
      super("MMOItems");
   }

   public void init() {
      Iris.info("Setting up MMOItems Link...");
   }

   @NotNull
   public BlockData getBlockData(@NotNull Identifier blockId, @NotNull KMap<String, String> state) {
      int var3 = -1;

      try {
         var3 = Integer.parseInt(var1.key());
      } catch (NumberFormatException var5) {
      }

      CustomBlock var4 = this.api().getCustomBlocks().getBlock(var3);
      if (var4 == null) {
         throw new MissingResourceException("Failed to find BlockData!", var1.namespace(), var1.key());
      } else {
         return var4.getState().getBlockData();
      }
   }

   @NotNull
   public ItemStack getItemStack(@NotNull Identifier itemId, @NotNull KMap<String, Object> customNbt) {
      String[] var3 = var1.namespace().split("_", 2);
      if (var3.length != 2) {
         throw new MissingResourceException("Failed to find ItemData!", var1.namespace(), var1.key());
      } else {
         CompletableFuture var4 = new CompletableFuture();
         Runnable var5 = () -> {
            try {
               Type var5 = this.api().getTypes().get(var3[1]);
               int var6 = -1;
               ItemTier var7 = null;
               if (var2 != null) {
                  var6 = (Integer)var2.getOrDefault("level", -1);
                  var7 = this.api().getTiers().get(String.valueOf(var2.get("tier")));
               }

               if (var5 == null) {
                  var4.complete((Object)null);
                  return;
               }

               ItemStack var8;
               if (var6 != -1 && var7 != null) {
                  var8 = this.api().getItem(var5, var1.key(), var6, var7);
               } else {
                  var8 = this.api().getItem(var5, var1.key());
               }

               var4.complete(var8);
            } catch (Throwable var9) {
               var4.completeExceptionally(var9);
            }

         };
         if (Bukkit.isPrimaryThread()) {
            var5.run();
         } else {
            J.s(var5);
         }

         ItemStack var6 = null;

         try {
            var6 = (ItemStack)var4.get();
         } catch (ExecutionException | InterruptedException var8) {
         }

         return var6;
      }
   }

   @NotNull
   public Collection<Identifier> getTypes(@NotNull DataType dataType) {
      Object var10000;
      switch(var1) {
      case ENTITY:
         var10000 = List.of();
         break;
      case BLOCK:
         var10000 = this.api().getCustomBlocks().getBlockIds().stream().map((var0) -> {
            return new Identifier("mmoitems", String.valueOf(var0));
         }).filter(var1.asPredicate(this)).toList();
         break;
      case ITEM:
         Supplier var2 = () -> {
            return this.api().getTypes().getAll().stream().flatMap((var1x) -> {
               return this.api().getTemplates().getTemplateNames(var1x).stream().map((var1) -> {
                  return new Identifier("mmoitems_" + var1x.getId(), var1);
               });
            }).filter(var1.asPredicate(this)).toList();
         };
         var10000 = Bukkit.isPrimaryThread() ? (Collection)var2.get() : (Collection)J.sfut(var2).join();
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return (Collection)var10000;
   }

   public boolean isValidProvider(@NotNull Identifier id, DataType dataType) {
      if (var2 == DataType.ENTITY) {
         return false;
      } else {
         return var2 == DataType.ITEM ? var1.namespace().split("_", 2).length == 2 : var1.namespace().equals("mmoitems");
      }
   }

   private MMOItems api() {
      return MMOItems.plugin;
   }
}
