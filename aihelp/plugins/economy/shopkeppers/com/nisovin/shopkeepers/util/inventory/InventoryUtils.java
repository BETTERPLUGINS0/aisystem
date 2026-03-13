package com.nisovin.shopkeepers.util.inventory;

import com.nisovin.shopkeepers.api.ShopkeepersPlugin;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import com.nisovin.shopkeepers.util.annotations.ReadWrite;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitScheduler;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class InventoryUtils {
   private static final ItemStack[] EMPTY_ITEMSTACK_ARRAY = new ItemStack[0];

   public static ItemStack[] emptyItemStackArray() {
      return EMPTY_ITEMSTACK_ARRAY;
   }

   public static boolean hasInventoryOpen(Player player) {
      InventoryType inventoryType = player.getOpenInventory().getType();
      return inventoryType != InventoryType.CRAFTING && inventoryType != InventoryType.CREATIVE;
   }

   public static boolean containsAtLeast(@ReadOnly ItemStack[] contents, Predicate<? super ItemStack> predicate, int amount) {
      Validate.notNull(contents, (String)"contents is null");
      Validate.notNull(predicate, (String)"predicate is null");
      if (amount <= 0) {
         return true;
      } else {
         int remainingAmount = amount;
         ItemStack[] var4 = contents;
         int var5 = contents.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            ItemStack itemStack = var4[var6];
            if (itemStack != null && !ItemUtils.isEmpty(itemStack) && predicate.test(itemStack)) {
               remainingAmount -= itemStack.getAmount();
               if (remainingAmount <= 0) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public static boolean containsAtLeast(@ReadOnly ItemStack[] contents, ItemData itemData, int amount) {
      return containsAtLeast(contents, ItemUtils.matchingItems(itemData), amount);
   }

   public static boolean containsAtLeast(@ReadOnly ItemStack[] contents, @ReadOnly ItemStack itemStack, int amount) {
      return containsAtLeast(contents, ItemUtils.similarItems(itemStack), amount);
   }

   public static boolean containsAtLeast(@ReadOnly ItemStack[] contents, UnmodifiableItemStack itemStack, int amount) {
      return containsAtLeast(contents, ItemUtils.similarItems(itemStack), amount);
   }

   public static boolean contains(@ReadOnly ItemStack[] contents, ItemData itemData) {
      return containsAtLeast((ItemStack[])contents, (ItemData)itemData, 1);
   }

   public static boolean contains(@ReadOnly ItemStack[] contents, @ReadOnly ItemStack itemStack) {
      return containsAtLeast((ItemStack[])contents, (ItemStack)itemStack, 1);
   }

   public static boolean containsAtLeast(Iterable<? extends ItemStack> contents, Predicate<? super ItemStack> predicate, int amount) {
      Validate.notNull(contents, (String)"contents is null");
      Validate.notNull(predicate, (String)"predicate is null");
      if (amount <= 0) {
         return true;
      } else {
         int remainingAmount = amount;
         Iterator var4 = contents.iterator();

         while(var4.hasNext()) {
            ItemStack itemStack = (ItemStack)var4.next();
            if (itemStack != null && !ItemUtils.isEmpty(itemStack) && predicate.test(itemStack)) {
               remainingAmount -= itemStack.getAmount();
               if (remainingAmount <= 0) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public static boolean containsAtLeast(Iterable<? extends ItemStack> contents, ItemData itemData, int amount) {
      return containsAtLeast(contents, ItemUtils.matchingItems(itemData), amount);
   }

   public static boolean containsAtLeast(Iterable<? extends ItemStack> contents, @ReadOnly ItemStack itemStack, int amount) {
      return containsAtLeast(contents, ItemUtils.similarItems(itemStack), amount);
   }

   public static boolean contains(Iterable<? extends ItemStack> contents, ItemData itemData) {
      return containsAtLeast((Iterable)contents, (ItemData)itemData, 1);
   }

   public static boolean contains(Iterable<? extends ItemStack> contents, @ReadOnly ItemStack itemStack) {
      return containsAtLeast((Iterable)contents, (ItemStack)itemStack, 1);
   }

   public static int addItems(@ReadWrite ItemStack[] contents, @ReadOnly ItemStack itemStack) {
      return addItems(contents, itemStack, ItemUtils.getItemStackAmount(itemStack));
   }

   public static int addItems(@ReadWrite ItemStack[] contents, @ReadOnly ItemStack item, int amount) {
      return addItems(contents, UnmodifiableItemStack.ofNonNull(item), amount);
   }

   public static int addItems(@ReadWrite ItemStack[] contents, UnmodifiableItemStack itemStack) {
      return addItems(contents, itemStack, ItemUtils.getItemStackAmount(itemStack));
   }

   public static int addItems(@ReadWrite ItemStack[] contents, UnmodifiableItemStack item, int amount) {
      Validate.notNull(contents, (String)"contents is null");
      Validate.notNull(item, (String)"item is null");
      Validate.isTrue(amount >= 0, "amount is negative");
      if (amount == 0) {
         return 0;
      } else {
         int remaining = amount;
         int maxStackSize = item.getMaxStackSize();
         int size = contents.length;

         int slot;
         ItemStack slotItem;
         for(slot = 0; slot < size; ++slot) {
            slotItem = contents[slot];
            if (slotItem != null && !ItemUtils.isEmpty(slotItem)) {
               int slotAmount = slotItem.getAmount();
               if (slotAmount < maxStackSize && item.isSimilar(slotItem)) {
                  slotItem = slotItem.clone();
                  contents[slot] = slotItem;
                  int newAmount = slotAmount + remaining;
                  if (newAmount <= maxStackSize) {
                     slotItem.setAmount(newAmount);
                     return 0;
                  }

                  slotItem.setAmount(maxStackSize);
                  remaining -= maxStackSize - slotAmount;

                  assert remaining != 0;
               }
            }
         }

         assert remaining > 0;

         for(slot = 0; slot < size; ++slot) {
            slotItem = contents[slot];
            if (ItemUtils.isEmpty(slotItem)) {
               ItemStack stack;
               if (remaining <= maxStackSize) {
                  stack = item.copy();
                  stack.setAmount(remaining);
                  contents[slot] = stack;
                  return 0;
               }

               stack = item.copy();
               stack.setAmount(maxStackSize);
               contents[slot] = stack;
               remaining -= maxStackSize;
            }
         }

         return remaining;
      }
   }

   public static int removeItems(@ReadWrite ItemStack[] contents, ItemData itemData, int amount) {
      return removeItems(contents, ItemUtils.matchingItems(itemData), amount);
   }

   public static int removeItems(@ReadWrite ItemStack[] contents, @ReadOnly ItemStack itemStack) {
      return removeItems(contents, ItemUtils.similarItems(itemStack), itemStack.getAmount());
   }

   public static int removeItems(@ReadWrite ItemStack[] contents, UnmodifiableItemStack itemStack) {
      return removeItems(contents, ItemUtils.similarItems(itemStack), itemStack.getAmount());
   }

   public static int removeItems(@ReadWrite ItemStack[] contents, Predicate<? super ItemStack> itemMatcher, int amount) {
      Validate.notNull(contents, (String)"contents is null");
      Validate.notNull(itemMatcher, (String)"itemMatcher is null");
      Validate.isTrue(amount >= 0, "amount is negative");
      if (amount == 0) {
         return 0;
      } else {
         boolean removeAll = amount == Integer.MAX_VALUE;
         int remaining = amount;

         for(int slot = 0; slot < contents.length; ++slot) {
            ItemStack slotItem = contents[slot];
            if (slotItem != null && !ItemUtils.isEmpty(slotItem) && itemMatcher.test(slotItem)) {
               if (removeAll) {
                  contents[slot] = null;
               } else {
                  int newAmount = slotItem.getAmount() - remaining;
                  if (newAmount > 0) {
                     slotItem = slotItem.clone();
                     contents[slot] = slotItem;
                     slotItem.setAmount(newAmount);
                     return 0;
                  }

                  contents[slot] = null;
                  remaining = -newAmount;
                  if (remaining == 0) {
                     return 0;
                  }
               }
            }
         }

         if (removeAll) {
            return 0;
         } else {
            return remaining;
         }
      }
   }

   public static void setStorageContents(Inventory inventory, @ReadOnly ItemStack[] contents) {
      setContents(inventory, contents);
   }

   public static void setContents(Inventory inventory, @ReadOnly ItemStack[] contents) {
      setContents(inventory, 0, contents);
   }

   public static void setContents(Inventory inventory, int slotOffset, @ReadOnly ItemStack[] contents) {
      Validate.notNull(inventory, (String)"inventory is null");
      Validate.notNull(contents, (String)"contents is null");
      int length = contents.length;

      for(int slot = 0; slot < length; ++slot) {
         ItemStack newItem = contents[slot];
         int inventorySlot = slotOffset + slot;
         ItemStack currentItem = inventory.getItem(inventorySlot);
         if (!Objects.equals(newItem, currentItem)) {
            inventory.setItem(inventorySlot, newItem);
         }
      }

   }

   public static void updateInventoryLater(Inventory inventory) {
      Validate.notNull(inventory, (String)"inventory is null");
      Player owner = null;
      if (inventory instanceof PlayerInventory) {
         assert inventory.getHolder() instanceof Player;

         owner = (Player)Unsafe.castNonNull(inventory.getHolder());
         updateInventoryLater(owner);
      }

      Iterator var2 = inventory.getViewers().iterator();

      while(var2.hasNext()) {
         HumanEntity viewer = (HumanEntity)var2.next();
         if (viewer instanceof Player && !viewer.equals(owner)) {
            updateInventoryLater((Player)viewer);
         }
      }

   }

   public static void updateInventoryLater(Player player) {
      Validate.notNull(player, (String)"player is null");
      BukkitScheduler var10000 = Bukkit.getScheduler();
      ShopkeepersPlugin var10001 = ShopkeepersPlugin.getInstance();
      Objects.requireNonNull(player);
      var10000.runTask(var10001, player::updateInventory);
   }

   public static void closeInventoryDelayed(InventoryView inventoryView) {
      Validate.notNull(inventoryView, (String)"inventoryView is null");
      Bukkit.getScheduler().runTask(ShopkeepersPlugin.getInstance(), () -> {
         InventoryView openInventoryView = inventoryView.getPlayer().getOpenInventory();
         if (inventoryView == openInventoryView) {
            inventoryView.close();
         }

      });
   }

   public static void closeInventoryDelayed(Player player) {
      BukkitScheduler var10000 = Bukkit.getScheduler();
      ShopkeepersPlugin var10001 = ShopkeepersPlugin.getInstance();
      Objects.requireNonNull(player);
      var10000.runTask(var10001, player::closeInventory);
   }

   public static void setItemDelayed(Inventory inventory, int slot, @Nullable @ReadOnly ItemStack itemStack) {
      Validate.notNull(inventory, (String)"inventory is null");
      Bukkit.getScheduler().runTask(ShopkeepersPlugin.getInstance(), () -> {
         inventory.setItem(slot, itemStack);
      });
   }

   private InventoryUtils() {
   }
}
