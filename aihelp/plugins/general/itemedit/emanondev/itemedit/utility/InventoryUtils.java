package emanondev.itemedit.utility;

import emanondev.itemedit.ItemEdit;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

public final class InventoryUtils {
   private static final Map<Class<?>, Method> getTopInventory = VersionUtils.hasFoliaAPI() ? new ConcurrentHashMap() : new HashMap();
   private static final Map<Class<?>, Method> getBottomInventory = VersionUtils.hasFoliaAPI() ? new ConcurrentHashMap() : new HashMap();
   private static final Set<EquipmentSlot> playerEquipmentSlots = loadPlayerEquipmentSlot();

   private InventoryUtils() {
      throw new UnsupportedOperationException();
   }

   public static Inventory getTopInventory(@NotNull InventoryEvent event) {
      return VersionUtils.isVersionAfter(1, 21) ? event.getView().getTopInventory() : getTopInventoryP(event.getView());
   }

   public static Inventory getTopInventory(@NotNull Player player) {
      return VersionUtils.isVersionAfter(1, 21) ? player.getOpenInventory().getTopInventory() : getTopInventoryP(player.getOpenInventory());
   }

   private static Inventory getTopInventoryP(@NotNull Object view) {
      Method method = (Method)getTopInventory.get(view.getClass());
      if (method == null) {
         method = ReflectionUtils.getMethod(view.getClass(), "getTopInventory");
         getTopInventory.put(view.getClass(), method);
      }

      return (Inventory)ReflectionUtils.invokeMethod(view, method);
   }

   public static Inventory getBottomInventory(@NotNull InventoryEvent event) {
      return VersionUtils.isVersionAfter(1, 21) ? event.getView().getBottomInventory() : getBottomInventoryP(event.getView());
   }

   private static Inventory getBottomInventoryP(@NotNull Object view) {
      Method method = (Method)getBottomInventory.get(view.getClass());
      if (method == null) {
         method = ReflectionUtils.getMethod(view.getClass(), "getBottomInventory");
         getBottomInventory.put(view.getClass(), method);
      }

      return (Inventory)ReflectionUtils.invokeMethod(view, method);
   }

   public static void updateView(@NotNull Player player) {
      ItemEdit var10000 = ItemEdit.get();
      Objects.requireNonNull(player);
      SchedulerUtils.run(var10000, (Player)player, player::updateInventory);
   }

   public static void updateViewDelayed(@NotNull Player player) {
      ItemEdit var10000 = ItemEdit.get();
      Objects.requireNonNull(player);
      SchedulerUtils.runLater(var10000, (Player)player, 1L, player::updateInventory);
   }

   public static int giveAmount(@NotNull HumanEntity player, @NotNull ItemStack item, @Range(from = 0L,to = 2147483647L) int amount, @NotNull InventoryUtils.ExcessMode mode) {
      ItemStack itemClone = item.clone();
      if (amount == 0) {
         return 0;
      } else {
         int remains = amount;

         while(remains > 0) {
            itemClone.setAmount(Math.min(itemClone.getMaxStackSize(), remains));
            HashMap<Integer, ItemStack> map = player.getInventory().addItem(new ItemStack[]{itemClone});
            remains -= Math.min(itemClone.getMaxStackSize(), remains);
            if (!map.isEmpty()) {
               remains += ((ItemStack)map.get(0)).getAmount();
               break;
            }
         }

         if (player instanceof Player) {
            updateViewDelayed((Player)player);
         }

         if (remains == 0) {
            return amount;
         } else {
            switch(mode.ordinal()) {
            case 0:
               while(remains > 0) {
                  int drop = Math.min(remains, 64);
                  itemClone.setAmount(drop);
                  ItemStack itemCopy = new ItemStack(itemClone);
                  Location loc = player.getEyeLocation();
                  SchedulerUtils.run(ItemEdit.get(), (Location)loc, () -> {
                     player.getWorld().dropItem(loc, itemCopy);
                  });
                  remains -= drop;
               }

               return amount;
            case 1:
               return amount - remains;
            case 2:
               removeAmount(player, itemClone, amount - remains, InventoryUtils.LackMode.REMOVE_MAX_POSSIBLE);
               return 0;
            default:
               throw new UnsupportedOperationException();
            }
         }
      }
   }

   public static int removeAmount(@NotNull HumanEntity player, @NotNull ItemStack item, @Range(from = 0L,to = 2147483647L) int amount, @NotNull InventoryUtils.LackMode mode) {
      ItemStack itemClone = item.clone();
      if (amount == 0) {
         return 0;
      } else {
         if (player instanceof Player) {
            updateViewDelayed((Player)player);
         }

         HashMap map;
         switch(mode.ordinal()) {
         case 0:
            itemClone.setAmount(amount);
            map = player.getInventory().removeItem(new ItemStack[]{itemClone});
            if (map.isEmpty()) {
               return amount;
            }

            int left = ((ItemStack)map.get(0)).getAmount();
            if (VersionUtils.isVersionAfter(1, 9)) {
               ItemStack[] extras = player.getInventory().getExtraContents();

               for(int i = 0; i < extras.length; ++i) {
                  ItemStack extra = extras[i];
                  if (extra != null && itemClone.isSimilar(extra)) {
                     int toRemove = Math.min(left, extra.getAmount());
                     left -= toRemove;
                     if (toRemove == extra.getAmount()) {
                        extras[i] = null;
                     } else {
                        extra.setAmount(extra.getAmount() - toRemove);
                        extras[i] = extra;
                     }
                  }
               }

               player.getInventory().setExtraContents(extras);
            }

            return amount - left;
         case 1:
            if (player.getInventory().containsAtLeast(itemClone, amount)) {
               itemClone.setAmount(amount);
               map = player.getInventory().removeItem(new ItemStack[]{itemClone});
               if (map.isEmpty()) {
                  return amount;
               }

               return amount - ((ItemStack)map.get(0)).getAmount();
            }

            return 0;
         default:
            throw new UnsupportedOperationException();
         }
      }
   }

   private static Set<EquipmentSlot> loadPlayerEquipmentSlot() {
      EnumSet<EquipmentSlot> slots = EnumSet.noneOf(EquipmentSlot.class);
      slots.add(EquipmentSlot.HEAD);
      slots.add(EquipmentSlot.CHEST);
      slots.add(EquipmentSlot.LEGS);
      slots.add(EquipmentSlot.FEET);
      slots.add(EquipmentSlot.HAND);

      try {
         slots.add(EquipmentSlot.valueOf("OFF_HAND"));
      } catch (Throwable var2) {
      }

      return Collections.unmodifiableSet(slots);
   }

   @NotNull
   public static Set<EquipmentSlot> getPlayerEquipmentSlots() {
      return playerEquipmentSlots;
   }

   @Nullable
   public static ItemStack getItem(@NotNull Player player, @NotNull EquipmentSlot slot) {
      try {
         return player.getInventory().getItem(slot);
      } catch (Throwable var5) {
         EntityEquipment equip = player.getEquipment();
         if (equip != null) {
            String var3 = slot.name();
            byte var4 = -1;
            switch(var3.hashCode()) {
            case 2153902:
               if (var3.equals("FEET")) {
                  var4 = 4;
               }
               break;
            case 2209903:
               if (var3.equals("HAND")) {
                  var4 = 0;
               }
               break;
            case 2213344:
               if (var3.equals("HEAD")) {
                  var4 = 3;
               }
               break;
            case 2332709:
               if (var3.equals("LEGS")) {
                  var4 = 1;
               }
               break;
            case 37796191:
               if (var3.equals("OFF_HAND")) {
                  var4 = 5;
               }
               break;
            case 64089825:
               if (var3.equals("CHEST")) {
                  var4 = 2;
               }
            }

            switch(var4) {
            case 0:
               return equip.getItemInHand();
            case 1:
               return equip.getLeggings();
            case 2:
               return equip.getChestplate();
            case 3:
               return equip.getHelmet();
            case 4:
               return equip.getBoots();
            case 5:
               return equip.getItemInOffHand();
            default:
               throw new UnsupportedOperationException();
            }
         } else {
            return null;
         }
      }
   }

   public static enum ExcessMode {
      DROP_EXCESS,
      DELETE_EXCESS,
      CANCEL;

      // $FF: synthetic method
      private static InventoryUtils.ExcessMode[] $values() {
         return new InventoryUtils.ExcessMode[]{DROP_EXCESS, DELETE_EXCESS, CANCEL};
      }
   }

   public static enum LackMode {
      REMOVE_MAX_POSSIBLE,
      CANCEL;

      // $FF: synthetic method
      private static InventoryUtils.LackMode[] $values() {
         return new InventoryUtils.LackMode[]{REMOVE_MAX_POSSIBLE, CANCEL};
      }
   }
}
