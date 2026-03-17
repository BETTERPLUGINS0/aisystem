package advancedplugins.pm2.cv.item;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.configuration.LangConfiguration;
import advancedplugins.pm2.cv.api.event.VehicleClickedEvent;
import advancedplugins.pm2.cv.api.item.ItemConfiguration;
import advancedplugins.pm2.cv.api.registry.Registries;
import advancedplugins.pm2.cv.api.util.inventory.ItemStackUtil;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.api.vehicle.configuration.VehicleConfiguration;
import advancedplugins.pm2.cv.api.vehicle.configuration.model.VehicleHitBoxConfiguration;
import advancedplugins.pm2.cv.handler.PluginHandlerAdapter;
import advancedplugins.pm2.cv.handler.PluginHandlerOptions;
import advancedplugins.pm2.cv.util.Constants;
import advancedplugins.pm2.cv.util.ItemUtil;
import com.jeff_media.morepersistentdatatypes.DataType;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import me.PM2.infinitevehicles.math.util.FastMath;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

@PluginHandlerOptions(
   eventListener = true,
   packetInjector = true
)
public final class ItemHandler extends PluginHandlerAdapter {
   @EventHandler
   public void onInventoryClick(InventoryClickEvent event) {
      Player var2 = (Player)var1.getWhoClicked();
      if (!var2.hasPermission("infinitevehicles.bypass.duplication")) {
         Inventory var4 = var1.getClickedInventory();
         if (var4 instanceof PlayerInventory) {
            PlayerInventory var3 = (PlayerInventory)var4;
            ItemStack var12 = var1.getCursor();
            if (var12 != null && ItemConfiguration.getItemUniqueId(var12) != null) {
               boolean var5 = false;
               String var6 = ItemConfiguration.getItemUniqueId(var12);
               Object var7 = null;
               int var8 = 0;
               int var9 = ItemUtil.getPDCInt(var12, Constants.NamespacedKeys.LEGAL_ITEM_AMOUNT) != null ? ItemUtil.getPDCInt(var12, Constants.NamespacedKeys.LEGAL_ITEM_AMOUNT) : 1;
               ListIterator var10 = var3.iterator();

               while(var10.hasNext()) {
                  ItemStack var11 = (ItemStack)var10.next();
                  if (var11 != null && ItemConfiguration.getItemUniqueId(var11) != null && ItemConfiguration.getItemUniqueId(var11).equals(var6)) {
                     var5 = true;
                     var8 += var11.getAmount();
                  }
               }

               if (!var5) {
                  return;
               }

               if (var8 + var9 <= var9) {
                  return;
               }

               var1.setResult(Result.DENY);
               var1.setCancelled(true);
               var1.setCursor(new ItemStack(Material.AIR));
            }

         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   public void onInteract(PlayerInteractEvent event) {
      ItemStack var2 = var1.getItem();
      if (var2 != null && ItemConfiguration.matchItemConfiguration(var2) != null) {
         var1.setCancelled(true);
      }

   }

   @EventHandler(
      priority = EventPriority.LOWEST,
      ignoreCancelled = true
   )
   public void onKeyUse(VehicleClickedEvent event) {
      if (var1.getClickType() == VehicleClickedEvent.ClickType.RIGHT_CLICK) {
         Vehicle var2 = var1.getVehicle();
         Player var3 = var1.getPlayer();
         ItemStack var4 = var3.getInventory().getItemInMainHand();
         if (var4.getType().isAir()) {
            var4 = var3.getInventory().getItemInOffHand();
         }

         ItemConfiguration var5 = ItemConfiguration.matchItemConfiguration(var4);
         ItemConfiguration.Action var6 = var5 != null ? var5.getAction() : null;
         if (var6 instanceof ItemConfiguration.KeyAction) {
            ItemConfiguration.KeyAction var7 = (ItemConfiguration.KeyAction)var6;
            var1.setCancelled(true);
            NamespacedKey var8 = new NamespacedKey(InfiniteVehicles.getPlugin(), "vehicle_key");
            ItemMeta var9 = var4.getItemMeta();
            if (var9 != null) {
               PersistentDataContainer var10 = var9.getPersistentDataContainer();
               if (!var2.isKeyed() && !var10.has(var8, PersistentDataType.STRING)) {
                  var2.setOwner(var3.getUniqueId());
                  var2.setKey(true);
                  var10.set(var8, PersistentDataType.STRING, var2.getUniqueId().toString());
                  var4.setItemMeta(var9);
                  var3.sendMessage(LangConfiguration.VEHICLE_KEY_SET.value());
               } else {
                  var3.sendMessage(LangConfiguration.VEHICLE_KEY_ERROR.value());
               }
            }
         }
      }
   }

   @EventHandler(
      priority = EventPriority.LOWEST,
      ignoreCancelled = true
   )
   public void onFuelSupply(VehicleClickedEvent event) {
      if (var1.getClickType() == VehicleClickedEvent.ClickType.RIGHT_CLICK) {
         Vehicle var2 = var1.getVehicle();
         Player var3 = var1.getPlayer();
         ItemStack var4 = var3.getInventory().getItemInMainHand();
         boolean var5 = true;
         if (var4.getType().isAir()) {
            var4 = var3.getInventory().getItemInOffHand();
            var5 = false;
         }

         ItemConfiguration var6 = ItemConfiguration.matchItemConfiguration(var4);
         ItemConfiguration.Action var7 = var6 != null ? var6.getAction() : null;
         if (var7 instanceof ItemConfiguration.FuelAction) {
            ItemConfiguration.FuelAction var8 = (ItemConfiguration.FuelAction)var7;
            var1.setCancelled(true);
            Float var9 = (Float)ItemStackUtil.getPersistentData(var4, Constants.NamespacedKeys.FUEL_ITEM_AMOUNT, PersistentDataType.FLOAT);
            if (var9 == null) {
               var9 = var8.getFuelAmount();
            }

            if (var9 > 0.0F) {
               float var10 = var2.getFuelCapacity() - var2.getFuelLevel();
               if (var10 <= 0.0F) {
                  String var10001 = LangConfiguration.PREFIX.value();
                  var3.sendMessage(var10001 + LangConfiguration.FUEL_TANK_FULL.value());
                  return;
               }

               if (var10 >= var9) {
                  var2.addFuel(var9);
                  var9 = 0.0F;
               } else {
                  var2.addFuel(var10);
                  var9 = var9 - var10;
               }

               if (var9 > 0.0F) {
                  ItemStackUtil.setPersistentData(var4, Constants.NamespacedKeys.FUEL_ITEM_AMOUNT, PersistentDataType.FLOAT, var9);
               }
            }

            if (var9 > 0.0F) {
               var4 = ItemConfiguration.buildFuelItemStack(var6, var9);
               if (var4.getItemMeta() instanceof Damageable) {
                  Damageable var14 = (Damageable)var4.getItemMeta();
                  short var11 = var4.getType().getMaxDurability();
                  if (var11 > 0) {
                     double var12 = (double)(var9 / var8.getFuelAmount());
                     var14.setDamage((int)((double)var11 - (double)var11 * var12));
                     var4.setItemMeta(var14);
                  }
               }
            } else if (var4.getAmount() > 1) {
               int var15 = var4.getAmount() - 1;
               var4 = var6.getItemStack();
               var4.setAmount(var15);
            } else {
               var4 = null;
            }

            if (var5) {
               var3.getInventory().setItemInMainHand(var4);
            } else {
               var3.getInventory().setItemInOffHand(var4);
            }

         }
      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onSpawn(PlayerInteractEvent event) {
      Player var2 = var1.getPlayer();
      ItemStack var3 = var1.getItem();
      if (var1.getAction() == Action.RIGHT_CLICK_AIR || var1.getAction() == Action.RIGHT_CLICK_BLOCK) {
         ItemConfiguration var4 = var3 != null ? ItemConfiguration.matchItemConfiguration(var3) : null;
         ItemConfiguration.Action var5 = var4 != null ? var4.getAction() : null;
         ItemConfiguration.SpawnAction var6 = var5 instanceof ItemConfiguration.SpawnAction ? (ItemConfiguration.SpawnAction)var5 : null;
         String var7 = var6 != null ? var6.getVehicleId() : null;
         VehicleConfiguration var8 = var7 != null ? (VehicleConfiguration)Registries.getRegistry(VehicleConfiguration.class).get(var7) : null;
         if (var8 != null) {
            var1.setCancelled(true);
            Block var9 = var2.getTargetBlockExact(5, FluidCollisionMode.ALWAYS);
            if (var9 != null) {
               Location var10 = this.findSpawn(var9, var8);
               boolean var11 = true;
               String var10001;
               if (var8.getPlacement().isWhitelist() || var8.getPlacement().isBlacklist()) {
                  List var12 = var8.getPlacement().getWorlds();
                  Stream var10000 = var12.stream();
                  var10001 = var2.getWorld().getName();
                  Objects.requireNonNull(var10001);
                  if (var10000.anyMatch(var10001::equalsIgnoreCase)) {
                     var11 = var8.getPlacement().isWhitelist();
                  }
               }

               if (var8.getPlacement().getPlaceLimit() != -1 && var11) {
                  int var14 = InfiniteVehicles.getVehicleHandler().getRegisteredVehicles().stream().filter((var1x) -> {
                     return var1x.getOwnerUniqueId() != null && var1x.getOwnerUniqueId().equals(var2.getUniqueId());
                  }).filter((var1x) -> {
                     return var1x.getConfiguration().getId().equals(var8.getId());
                  }).toList().size();
                  var11 = var14 < var8.getPlacement().getPlaceLimit();
               }

               if (var10 != null && var11) {
                  Vehicle var15 = InfiniteVehicles.getVehicleHandler().spawnVehicle(var8, var10, var2.getUniqueId());
                  if (ItemStackUtil.hasPersistentData(var3, Constants.NamespacedKeys.VEHICLE_FUEL_AMOUNT, PersistentDataType.FLOAT)) {
                     float var13 = (Float)ItemStackUtil.getPersistentData(var3, Constants.NamespacedKeys.VEHICLE_FUEL_AMOUNT, PersistentDataType.FLOAT);
                     var15.setFuelLevel(var13);
                  }

                  if (ItemStackUtil.hasPersistentData(var3, Constants.NamespacedKeys.VEHICLE_UPGRADES_DATA, DataType.asMap(DataType.STRING, DataType.INTEGER))) {
                     var15.getUpgradeTiers().putAll((Map)ItemStackUtil.getPersistentData(var3, Constants.NamespacedKeys.VEHICLE_UPGRADES_DATA, DataType.asMap(DataType.STRING, DataType.INTEGER)));
                  }

                  EquipmentSlot var16 = var1.getHand();
                  if (var16 == EquipmentSlot.HAND) {
                     var2.getInventory().setItemInMainHand((ItemStack)null);
                  } else if (var16 == EquipmentSlot.OFF_HAND) {
                     var2.getInventory().setItemInOffHand((ItemStack)null);
                  }

               } else {
                  var10001 = LangConfiguration.PREFIX.value();
                  var2.sendMessage(var10001 + LangConfiguration.SPAWN_CANNOT_PLACE_HERE.value());
               }
            }
         }
      }
   }

   @Nullable
   Location findSpawn(Block clickedBlock, VehicleConfiguration configuration) {
      World var3 = var1.getWorld();
      Location var4 = var1.getLocation().add(0.0D, 1.0D, 0.0D);
      VehicleHitBoxConfiguration var5 = var2.model().getHitBox();
      double var6 = var5.getWidth() / 2.0D;
      double var8 = var5.getDepth() / 2.0D;
      int var10 = (int)FastMath.floor(FastMath.min(var4.getX() - var6, var4.getX() + var6));
      int var11 = (int)FastMath.round(FastMath.max(var4.getX() - var6, var4.getX() + var6));
      int var12 = (int)FastMath.floor(var4.getY());
      int var13 = (int)FastMath.round(var4.getY() + var5.getHeight());
      int var14 = (int)FastMath.floor(FastMath.min(var4.getZ() - var8, var4.getZ() + var8));
      int var15 = (int)FastMath.round(FastMath.max(var4.getZ() - var8, var4.getZ() + var8));

      for(int var16 = var10; var16 <= var11; ++var16) {
         for(int var17 = var12; var17 <= var13; ++var17) {
            for(int var18 = var14; var18 <= var15; ++var18) {
               Block var19 = var3.getBlockAt(var16, var17, var18);
               if (!var19.isEmpty() && !var19.isPassable()) {
                  return null;
               }
            }
         }
      }

      return var4;
   }
}
