package advancedplugins.pm2.cv.vehicle;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.configuration.AdminLogs;
import advancedplugins.pm2.cv.api.configuration.Configuration;
import advancedplugins.pm2.cv.api.configuration.GuiConfiguration;
import advancedplugins.pm2.cv.api.configuration.LangConfiguration;
import advancedplugins.pm2.cv.api.enums.EnumPlaceholder;
import advancedplugins.pm2.cv.api.event.VehicleClickedEvent;
import advancedplugins.pm2.cv.api.interfaces.VersionSensible;
import advancedplugins.pm2.cv.api.service.GuiBuilderService;
import advancedplugins.pm2.cv.api.upgrade.Upgrade;
import advancedplugins.pm2.cv.api.upgrade.UpgradeConfiguration;
import advancedplugins.pm2.cv.api.upgrade.UpgradeRequirement;
import advancedplugins.pm2.cv.api.upgrade.UpgradeTier;
import advancedplugins.pm2.cv.api.util.SpamUtil;
import advancedplugins.pm2.cv.api.util.inventory.ItemStackUtil;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.api.vehicle.VehicleSeat;
import advancedplugins.pm2.cv.api.vehicle.configuration.VehicleRepairConfiguration;
import advancedplugins.pm2.cv.api.vehicle.item.storage.VehicleItemStorage;
import es.outlook.adriansrj.spigui.buttons.SGButton;
import es.outlook.adriansrj.spigui.buttons.SGButtonListener;
import es.outlook.adriansrj.spigui.menu.SGMenu;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GuiSubHandler {
   final VehicleHandlerImpl vehicleHandler;

   private static void repairVehicle(@NotNull Vehicle vehicle, @NotNull Player player) {
      VehicleRepairConfiguration var2 = var0.getConfiguration().getRepair();
      if (var2 != null) {
         var0.setHealth(var0.getHealth() + (float)var2.getRepairAmount());
         var1.playSound(var1.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.8F, 0.5F);
         var1.sendMessage(String.format(LangConfiguration.REPAIRED_VEHICLE.value(), Math.round(var0.getHealth())));
      }
   }

   GuiSubHandler(VehicleHandlerImpl vehicleHandler) {
      this.vehicleHandler = var1;
   }

   void processClickedVehicle(@NotNull VehicleClickedEvent event) {
      if (!var1.isCancelled() && var1.getClickType() == VehicleClickedEvent.ClickType.RIGHT_CLICK_CROUCHING) {
         if (var1.getVehicle().isKeyed() && !var1.getVehicle().isTheOwner(var1.getPlayer())) {
            var1.getPlayer().sendMessage(LangConfiguration.VEHICLE_KEY_NOT_OWNER.value());
            return;
         }

         this.openVehicleGui(var1.getVehicle(), var1.getPlayer());
      }

   }

   void openVehicleGui(@NotNull Vehicle vehicle, @NotNull Player player) {
      try {
         (new GuiSubHandler.VehicleGui(var1)).open(var2);
      } catch (InvalidConfigurationException var4) {
         var4.printStackTrace();
      }

   }

   private static int slotCheck(int slot) {
      if (var0 >= 0 && var0 <= GuiConfiguration.ROWS * 9) {
         return var0;
      } else {
         throw new InvalidConfigurationException("invalid gui item slot: " + var0);
      }
   }

   @NotNull
   public static ItemStack buildIcon(@NotNull GuiConfiguration.Item item, @NotNull Object object) {
      ItemStack var2 = ItemStackUtil.buildCustomItem(var0.getMaterial(), var0.getCustomModelData(), format(var0.getDisplayName(), var1), format(var0.getDescription(), var1), ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_DESTROYS);
      if (ItemStackUtil.isHead(var0.getMaterial()) && StringUtils.isNotBlank(var0.getHeadTexture())) {
         InfiniteVehicles.getTexturedHeadService().applyTexture(var2, var0.getHeadTexture());
      }

      return var2;
   }

   @Nullable
   private static String format(@Nullable String string, @NotNull Object object) {
      if (var0 == null) {
         return null;
      } else {
         EnumPlaceholder[] var2 = EnumPlaceholder.values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            EnumPlaceholder var5 = var2[var4];
            Vehicle var6;
            switch(var5) {
            case FUEL_LEVEL:
               if (var1 instanceof Vehicle) {
                  var0 = var5.format(var0, String.valueOf(((Vehicle)var1).getFuelLevel()));
               }
               break;
            case FUEL_LEVEL_PERCENTAGE:
               if (var1 instanceof Vehicle) {
                  var6 = (Vehicle)var1;
                  double var10 = (double)(var6.getFuelLevel() / var6.getFuelCapacity()) * 100.0D;
                  var0 = var5.format(var0, String.valueOf(var10));
               }
               break;
            case FUEL_CAPACITY:
               if (var1 instanceof Vehicle) {
                  var0 = var5.format(var0, String.valueOf(((Vehicle)var1).getFuelCapacity()));
               }
               break;
            case SEAT_INDEX:
               if (var1 instanceof GuiSubHandler.SeatWrapper) {
                  var0 = var5.format(var0, String.valueOf(((GuiSubHandler.SeatWrapper)var1).index + 1));
               }
               break;
            case HEALTH:
               if (var1 instanceof Vehicle) {
                  var6 = (Vehicle)var1;
                  var0 = var5.format(var0, String.valueOf(var6.getHealth()));
               }
               break;
            case MAX_HEALTH:
               if (var1 instanceof Vehicle) {
                  var6 = (Vehicle)var1;
                  var0 = var5.format(var0, String.valueOf(var6.getMaxHealth()));
               }
               break;
            case NEED_REPAIR:
               if (var1 instanceof Vehicle) {
                  var6 = (Vehicle)var1;
                  boolean var7 = (double)var6.getHealth() < (double)var6.getMaxHealth() * 0.5D;
                  boolean var8 = var6.getHealth() < var6.getMaxHealth() && (double)var6.getHealth() >= (double)var6.getMaxHealth() * 0.5D;
                  boolean var9 = var6.getHealth() == var6.getMaxHealth();
                  var0 = var5.format(var0, var7 ? "&cRequired" : (var8 ? "&eCaution" : (var9 ? "&aHealthy" : "&cRequired")));
               }
            }
         }

         return var0;
      }
   }

   @Nullable
   private static List<String> format(@Nullable List<String> stringList, @NotNull Object object) {
      if (var0 == null) {
         return null;
      } else {
         ArrayList var2 = new ArrayList();
         Iterator var3 = var0.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            var2.add(format(var4, var1));
         }

         return var2;
      }
   }

   private static class VehicleGui {
      @NotNull
      private final Vehicle vehicle;

      public VehicleGui(@NotNull Vehicle vehicle) {
         this.vehicle = var1;
      }

      private void open(@NotNull Player player) {
         SGMenu var2 = ((GuiBuilderService)Objects.requireNonNull((GuiBuilderService)InfiniteVehicles.getService(GuiBuilderService.class))).get().create(GuiConfiguration.TITLE, GuiConfiguration.ROWS);
         if (GuiConfiguration.FUEL_DISPLAY_ITEM != null) {
            var2.setButton(GuiSubHandler.slotCheck(GuiConfiguration.FUEL_DISPLAY_ITEM.getSlot()), new SGButton(GuiSubHandler.buildIcon(GuiConfiguration.FUEL_DISPLAY_ITEM, this.vehicle)));
         }

         SGButton var3;
         if (GuiConfiguration.PICKUP_ITEM != null && Configuration.PICKUP_ENABLE.booleanValue() && this.vehicle.getConfiguration().hasPickupItem() && (this.vehicle.isTheOwner(var1) || !Configuration.PICKUP_ONLY_OWNER.booleanValue())) {
            var3 = new SGButton(GuiSubHandler.buildIcon(GuiConfiguration.PICKUP_ITEM, this.vehicle));
            var3.setListener((var2x) -> {
               if (!this.vehicle.isIn(var1)) {
                  InfiniteVehicles.getVehicleHandler().pickupVehicle(this.vehicle, var1, false, true);
                  var1.closeInventory();
               }
            });
            var2.setButton(GuiSubHandler.slotCheck(GuiConfiguration.PICKUP_ITEM.getSlot()), var3);
         }

         if (GuiConfiguration.ADMIN_PICKUP_ITEM != null && (var1.isOp() || var1.hasPermission("infinitevehicles.admin"))) {
            String var7 = (String)Optional.ofNullable(this.vehicle.getOwner()).map(Player::getName).orElse("Unknown");
            ItemStack var4 = GuiSubHandler.buildIcon(GuiConfiguration.ADMIN_PICKUP_ITEM, this.vehicle);
            ItemMeta var5 = var4.getItemMeta();
            if (var5 != null && var5.hasLore()) {
               List var6 = (List)((List)Objects.requireNonNull(var5.getLore())).stream().map((var1x) -> {
                  return var1x.replace("%player%", var7);
               }).collect(Collectors.toList());
               var5.setLore(var6);
               var4.setItemMeta(var5);
            }

            SGButton var8 = new SGButton(var4);
            var8.setListener((var2x) -> {
               InfiniteVehicles.getVehicleHandler().pickupVehicle(this.vehicle, var1, true, true);
               AdminLogs.logVehiclePickup(var1.getName(), this.vehicle.getConfiguration().getName(), this.vehicle.getOwner() == null ? "Unknown Owner" : this.vehicle.getOwner().getName());
               var1.closeInventory();
            });
            var2.setButton(GuiSubHandler.slotCheck(GuiConfiguration.ADMIN_PICKUP_ITEM.getSlot()), var8);
         }

         if (GuiConfiguration.SEATS_GUI_ITEM != null) {
            var3 = new SGButton(GuiSubHandler.buildIcon(GuiConfiguration.SEATS_GUI_ITEM, this.vehicle));
            var3.setListener((var1x) -> {
               (new GuiSubHandler.SeatsGui(this.vehicle)).open((Player)var1x.getWhoClicked());
            });
            var2.setButton(GuiSubHandler.slotCheck(GuiConfiguration.SEATS_GUI_ITEM.getSlot()), var3);
         }

         if (this.vehicle.getUpgradeConfiguration() != null) {
            var3 = new SGButton(GuiSubHandler.buildIcon(this.vehicle.getUpgradeConfiguration().getItem(), this.vehicle));
            var3.setListener((var1x) -> {
               try {
                  (new GuiSubHandler.UpgradeGui(this.vehicle)).open((Player)var1x.getWhoClicked());
               } catch (InvalidConfigurationException var3) {
                  throw new RuntimeException(var3);
               }
            });
            var2.setButton(GuiSubHandler.slotCheck(this.vehicle.getUpgradeConfiguration().getItem().getSlot()), var3);
         }

         if (GuiConfiguration.STORAGE_GUI_ITEM != null) {
            var3 = new SGButton(GuiSubHandler.buildIcon(GuiConfiguration.STORAGE_GUI_ITEM, this.vehicle));
            var3.setListener((var1x) -> {
               (new GuiSubHandler.StorageGUI(this.vehicle)).open((Player)var1x.getWhoClicked());
            });
            var2.setButton(GuiSubHandler.slotCheck(GuiConfiguration.STORAGE_GUI_ITEM.getSlot()), var3);
         }

         if (this.vehicle.getConfiguration().getRepair() != null && GuiConfiguration.REPAIR_ITEM != null) {
            var3 = new SGButton(GuiSubHandler.buildIcon(GuiConfiguration.REPAIR_ITEM, this.vehicle));
            var3.setListener((var2x) -> {
               boolean var3 = this.vehicle.getConfiguration().getRepair().test(var1);
               String var10000 = String.valueOf(var1.getUniqueId());
               if (SpamUtil.isSpam(var10000 + "_repair_" + this.vehicle.hashCode())) {
                  String var10001 = LangConfiguration.REPAIR_TOO_FAST.value();
                  Object[] var10002 = new Object[1];
                  String var10005 = String.valueOf(var1.getUniqueId());
                  var10002[0] = SpamUtil.getRemainingSeconds(var10005 + "_repair_" + this.vehicle.hashCode());
                  var1.sendMessage(String.format(var10001, var10002));
               } else if (this.vehicle.getHealth() == this.vehicle.getMaxHealth() && var3) {
                  var1.sendMessage(LangConfiguration.REPAIR_NOT_NEEDED.value());
               } else {
                  if (var3) {
                     SpamUtil.addSpam(String.valueOf(var1.getUniqueId()) + "_repair_" + this.vehicle.hashCode(), (long)this.vehicle.getConfiguration().getRepair().getCooldown());
                  }

                  if (!this.vehicle.getConfiguration().getRepair().takeOne(var1)) {
                     var1.sendMessage(LangConfiguration.REPAIR_NOT_ENOUGH.value());
                  } else {
                     GuiSubHandler.repairVehicle(this.vehicle, var1);

                     try {
                        (new GuiSubHandler.VehicleGui(this.vehicle)).open(var1);
                     } catch (InvalidConfigurationException var5) {
                        var5.printStackTrace();
                     }

                  }
               }
            });
            var2.setButton(GuiSubHandler.slotCheck(GuiConfiguration.REPAIR_ITEM.getSlot()), var3);
         }

         var1.closeInventory();
         var1.openInventory(var2.getInventory());
      }
   }

   private static class SeatWrapper {
      private final int index;
      private final int row;
      @NotNull
      private final VehicleSeat seat;

      public SeatWrapper(int index, int row, @NotNull VehicleSeat seat) {
         this.index = var1;
         this.row = var2;
         this.seat = var3;
      }
   }

   private static class StorageGUI {
      @NotNull
      private final Vehicle vehicle;
      private final List<VehicleItemStorage> vehicleItemStorage;
      private SGMenu handle;

      public StorageGUI(@NotNull Vehicle vehicle) {
         this.vehicle = var1;
         this.vehicleItemStorage = var1.getStorage();
      }

      @VersionSensible
      private boolean build(Player player) {
         if (this.vehicleItemStorage.isEmpty()) {
            if (this.vehicle.getStorageSize().getSlots() > 0) {
               this.vehicleItemStorage.add(new VehicleItemStorage(this.vehicle, this.vehicle.getStorageSize().getSlots()));
               this.openStorage(var1);
            }

            return false;
         } else if (this.vehicleItemStorage.size() == 1) {
            this.openStorage(var1);
            return false;
         } else {
            this.openSelector();
            return true;
         }
      }

      private void openStorage(Player player) {
         VehicleItemStorage var2 = (VehicleItemStorage)this.vehicleItemStorage.get(0);
         Inventory var3 = var2.getHolder().getInventory();
         if (GuiConfiguration.STORAGE_GUI_ITEM != null) {
            var1.openInventory(var3);
         }
      }

      private void openSelector() {
         int var1 = this.vehicleItemStorage.size() % 9 + 1;
         if (var1 > 6) {
            var1 = 6;
         }

         this.handle = ((GuiBuilderService)Objects.requireNonNull((GuiBuilderService)InfiniteVehicles.getService(GuiBuilderService.class))).get().create("Select Storage Container", var1);
         if (GuiConfiguration.STORAGE_GUI_ITEM != null) {
            for(int var2 = 0; var2 < this.vehicleItemStorage.size(); ++var2) {
               ItemStack var3 = new ItemStack(Material.CHEST);
               ItemMeta var4 = var3.getItemMeta();
               ((ItemMeta)Objects.requireNonNull(var4)).setDisplayName(ItemStackUtil.colorize("&fItem Storage Container nr." + var2 + "1"));
               var4.setLore(ItemStackUtil.colorize(Arrays.asList("", "&7Click here to open")));
               var3.setItemMeta(var4);
               this.handle.setButton(var2, (new SGButton(var3)).withListener(this.buildListener()));
            }

         }
      }

      private SGButtonListener buildListener() {
         return (var1) -> {
            if (this.vehicleItemStorage.size() - 1 >= var1.getSlot()) {
               HumanEntity var2 = var1.getWhoClicked();
               var2.openInventory(((VehicleItemStorage)this.vehicleItemStorage.get(var1.getSlot())).getHolder().getInventory());
            }
         };
      }

      private void open(@NotNull Player player) {
         if (this.build(var1)) {
            var1.closeInventory();
            var1.openInventory(this.handle.getInventory());
         }
      }
   }

   private static class SeatsGui {
      @NotNull
      private final Vehicle vehicle;
      private SGMenu handle;
      private final List<VehicleSeat> seats = new ArrayList();
      private final List<GuiSubHandler.SeatWrapper> wrappers = new ArrayList();

      public SeatsGui(@NotNull Vehicle vehicle) {
         this.vehicle = var1;
      }

      @VersionSensible
      private void build() {
         this.seats.clear();
         this.seats.addAll(this.vehicle.getSeats());
         this.wrappers.clear();
         this.seats.sort((var0, var1x) -> {
            Vector3D var2 = var0.getConfiguration().getOffset();
            Vector3D var3 = var1x.getConfiguration().getOffset();
            double var4 = var2.getZ();
            double var6 = var3.getZ();
            double var8 = var2.getX();
            double var10 = var3.getX();
            if (Math.abs(var4 - var6) >= 1.0D) {
               return var4 > var6 ? -1 : 1;
            } else if (var8 != var10) {
               return var8 < var10 ? -1 : 1;
            } else {
               return 0;
            }
         });
         Double var1 = null;
         int var2 = 1;
         int var3 = 0;
         int var4 = 0;

         for(int var5 = 0; var5 < this.seats.size(); ++var5) {
            VehicleSeat var6 = (VehicleSeat)this.seats.get(var5);
            Vector3D var7 = var6.getConfiguration().getOffset();
            if (var1 == null) {
               var1 = var7.getZ();
            }

            if (Math.abs(var1 - var7.getZ()) >= 1.0D) {
               var1 = var7.getZ();
               ++var2;
               if (var3 > var4) {
                  var4 = var3;
               }

               var3 = 0;
            } else {
               ++var3;
            }

            this.wrappers.add(new GuiSubHandler.SeatWrapper(var5, var2 - 1, var6));
         }

         if (var2 <= 6 && var4 <= 9) {
            this.buildMethodA(var2);
         } else {
            this.buildMethodB(var2);
         }

      }

      private void buildMethodA(int rowCount) {
         this.handle = ((GuiBuilderService)Objects.requireNonNull((GuiBuilderService)InfiniteVehicles.getService(GuiBuilderService.class))).get().create(GuiConfiguration.TITLE, var1);
         if (GuiConfiguration.SEAT_ITEM != null) {
            for(int var2 = 0; var2 < var1; ++var2) {
               List var3 = this.getWrappersByRow(var2);
               var3.sort((var0, var1x) -> {
                  if (var0.index != var1x.index) {
                     return var0.index < var1x.index ? -1 : 1;
                  } else {
                     return 0;
                  }
               });
               int var4 = var2 * 9;
               int var5 = var4 + 4;
               if (var3.size() < 9) {
                  var4 += (9 - var3.size()) / 2;
               }

               int var6 = 0;

               for(Iterator var7 = var3.iterator(); var7.hasNext(); ++var6) {
                  GuiSubHandler.SeatWrapper var8 = (GuiSubHandler.SeatWrapper)var7.next();
                  if (var4 + var6 == var5 && var3.size() % 2 == 0) {
                     ++var6;
                  }

                  GuiConfiguration.Item var9 = GuiConfiguration.SEAT_ITEM;
                  if (var8.seat.isMain() && GuiConfiguration.OPERATOR_SEAT_ITEM != null) {
                     var9 = GuiConfiguration.OPERATOR_SEAT_ITEM;
                  }

                  this.handle.setButton(var4 + var6, (new SGButton(GuiSubHandler.buildIcon(var9, var8))).withListener(this.buildListener(var8)));
               }
            }

         }
      }

      private List<GuiSubHandler.SeatWrapper> getWrappersByRow(int row) {
         return (List)this.wrappers.stream().filter((var1x) -> {
            return var1x.row == var1;
         }).collect(Collectors.toList());
      }

      private void buildMethodB(int rowCount) {
         this.handle = ((GuiBuilderService)Objects.requireNonNull((GuiBuilderService)InfiniteVehicles.getService(GuiBuilderService.class))).get().create(GuiConfiguration.TITLE, Math.min(var1, 6));
         if (GuiConfiguration.SEAT_ITEM != null) {
            GuiSubHandler.SeatWrapper var3;
            GuiConfiguration.Item var4;
            for(Iterator var2 = this.wrappers.iterator(); var2.hasNext(); this.handle.addButton((new SGButton(GuiSubHandler.buildIcon(var4, var3))).withListener(this.buildListener(var3)))) {
               var3 = (GuiSubHandler.SeatWrapper)var2.next();
               var4 = GuiConfiguration.SEAT_ITEM;
               if (var3.seat.isMain() && GuiConfiguration.OPERATOR_SEAT_ITEM != null) {
                  var4 = GuiConfiguration.OPERATOR_SEAT_ITEM;
               }
            }

         }
      }

      private SGButtonListener buildListener(GuiSubHandler.SeatWrapper wrapper) {
         return (var1x) -> {
            HumanEntity var2 = var1x.getWhoClicked();
            if (!var1.seat.isOccupied()) {
               if (var1.seat.isMain() && Configuration.OWNERSHIP_ONLY_OWNER.booleanValue() && !var1.seat.getVehicle().isTheOwner(var2.getUniqueId())) {
                  String var10001 = LangConfiguration.PREFIX.value();
                  var2.sendMessage(var10001 + LangConfiguration.GUI_OWNERSHIP.value());
               } else {
                  var1.seat.setPassenger(var2);
               }
            }
         };
      }

      private void open(@NotNull Player player) {
         this.build();
         var1.closeInventory();
         var1.openInventory(this.handle.getInventory());
      }
   }

   private static class UpgradeGui {
      @NotNull
      private final Vehicle vehicle;

      public void open(@NotNull Player player) {
         UpgradeConfiguration var2 = this.vehicle.getUpgradeConfiguration();
         if (var2 == null) {
            throw new InvalidConfigurationException("vehicle has no upgrade configuration");
         } else {
            SGMenu var3 = ((GuiBuilderService)Objects.requireNonNull((GuiBuilderService)InfiniteVehicles.getService(GuiBuilderService.class))).get().create(ChatColor.translateAlternateColorCodes('&', var2.getTitle()), var2.getRows());
            Iterator var4 = var2.getUpgrades().iterator();

            while(true) {
               Upgrade var5;
               int var6;
               int var7;
               do {
                  if (!var4.hasNext()) {
                     var1.openInventory(var3.getInventory());
                     return;
                  }

                  var5 = (Upgrade)var4.next();
                  var6 = var5.getSlot();
                  var7 = (Integer)this.vehicle.getUpgradeTier(var1.getUniqueId()).getOrDefault(var5.getId(), -2);
               } while(var7 == -2);

               UpgradeTier var8 = (UpgradeTier)var5.getUpgradeTiers().get(var7);
               if (var8 == null) {
                  var8 = (UpgradeTier)var5.getUpgradeTiers().get(var7 + 1);
               }

               int var9 = -1;

               UpgradeTier var11;
               for(Iterator var10 = var5.getUpgradeTiers().values().iterator(); var10.hasNext(); var9 = Math.max(var9, var11.getTier())) {
                  var11 = (UpgradeTier)var10.next();
               }

               if (var9 == -1) {
                  return;
               }

               boolean var13;
               if (var7 > var9) {
                  var8 = (UpgradeTier)var5.getUpgradeTiers().get(var9);
                  var13 = false;
               } else {
                  var13 = true;
               }

               if (var8 != null) {
                  SGButton var14 = new SGButton(GuiSubHandler.buildIcon(var13 ? var8.getItem() : var8.getSelectedItem(), this.vehicle));
                  var14.setListener((var5x) -> {
                     if (!var13) {
                        var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot upgrade this vehicle anymore!"));
                     } else {
                        Iterator var6 = var8.getUpgradeRequirements().iterator();

                        String var8x;
                        do {
                           if (!var6.hasNext()) {
                              var8.getUpgradeRequirements().forEach((var3) -> {
                                 var3.takeRequirement(var5, var8, var1);
                              });
                              this.vehicle.setUpgradeTier(var1.getUniqueId(), var5.getId(), var8.getTier() + 1);

                              try {
                                 (new GuiSubHandler.UpgradeGui(this.vehicle)).open((Player)var5x.getWhoClicked());
                              } catch (InvalidConfigurationException var9) {
                                 var5x.getWhoClicked().sendMessage(var9.getMessage());
                                 return;
                              }

                              var1.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Successfully upgraded the vehicle!"));
                              return;
                           }

                           UpgradeRequirement var7 = (UpgradeRequirement)var6.next();
                           var8x = var7.testRequirement(var5, var8, var1);
                        } while(var8x == null);

                        var1.sendMessage(ChatColor.translateAlternateColorCodes('&', var8x));
                     }
                  });
                  var3.setButton(GuiSubHandler.slotCheck(var6), var14);
               }
            }
         }
      }

      @NotNull
      public Vehicle getVehicle() {
         return this.vehicle;
      }

      public UpgradeGui(@NotNull final Vehicle vehicle) {
         this.vehicle = var1;
      }
   }
}
