package com.volmit.iris.core.events;

import com.volmit.iris.Iris;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.object.InventorySlotType;
import com.volmit.iris.engine.object.IrisLootTable;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.scheduling.J;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootContext.Builder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IrisLootEvent extends Event {
   private static final HandlerList handlers = new HandlerList();
   private static final LootTable EMPTY = new LootTable() {
      @NotNull
      public NamespacedKey getKey() {
         return new NamespacedKey(Iris.instance, "empty");
      }

      @NotNull
      public Collection<ItemStack> populateLoot(@Nullable Random random, @NotNull LootContext context) {
         return List.of();
      }

      public void fillInventory(@NotNull Inventory inventory, @Nullable Random random, @NotNull LootContext context) {
      }
   };
   private final Engine engine;
   private final Block block;
   private final InventorySlotType slot;
   private final KList<IrisLootTable> tables;

   public IrisLootEvent(Engine engine, Block block, InventorySlotType slot, KList<IrisLootTable> tables) {
      this.engine = var1;
      this.block = var2;
      this.slot = var3;
      this.tables = var4;
   }

   public HandlerList getHandlers() {
      return handlers;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public static boolean callLootEvent(KList<ItemStack> loot, Inventory inv, World world, int x, int y, int z) {
      InventoryHolder var6 = var1.getHolder();
      Location var7 = new Location(var2, (double)var3, (double)var4, (double)var5);
      if (var6 == null) {
         var6 = new InventoryHolder() {
            @NotNull
            public Inventory getInventory() {
               return var1;
            }
         };
      }

      LootContext var8 = (new Builder(var7)).build();
      LootGenerateEvent var9 = new LootGenerateEvent(var2, (Entity)null, var6, EMPTY, var8, var0, true);
      if (!Bukkit.isPrimaryThread()) {
         Iris.warn("LootGenerateEvent was not called on the main thread, please report this issue.");
         Thread.dumpStack();
         J.sfut(() -> {
            Bukkit.getPluginManager().callEvent(var9);
         }).join();
      } else {
         Bukkit.getPluginManager().callEvent(var9);
      }

      return var9.isCancelled();
   }

   @Generated
   public Engine getEngine() {
      return this.engine;
   }

   @Generated
   public Block getBlock() {
      return this.block;
   }

   @Generated
   public InventorySlotType getSlot() {
      return this.slot;
   }

   @Generated
   public KList<IrisLootTable> getTables() {
      return this.tables;
   }
}
