package advancedplugins.pm2.cv.api.vehicle.item.storage;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.configuration.LangConfiguration;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.models.api.utils.data.ItemUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class VehicleItemHolder implements InventoryHolder {
   public static final YamlConfiguration vehicleItemStorageYml;
   public static final File vehicleStorageFile;
   public static final ItemStack BLOCKING_ITEM;
   private static final File storageFolder;
   public static NamespacedKey BLOCKING_ITEM_NAMESPACE = new NamespacedKey(InfiniteVehicles.getPlugin(), "blocking_item_reserved");
   private final Vehicle vehicle;
   private final Inventory storage;
   private UUID uuid;
   private final int slots;

   public VehicleItemHolder(Vehicle var1, int var2) {
      this.vehicle = var1;
      String var3 = String.format(LangConfiguration.VEHICLE_STORAGE_NAME.value(), var1.getConfiguration().getName());
      int var4 = var2;
      this.slots = var2;
      if (var2 < 9) {
         var4 = 9;
      }

      if (var4 == 9) {
         this.storage = Bukkit.createInventory(this, InventoryType.DROPPER, var3);
         if (var2 < 9) {
            int var5 = (9 - var2) / 2;

            for(int var6 = 0; var6 < 9; ++var6) {
               if (var6 < var5 || var6 >= var5 + var2) {
                  this.storage.setItem(var6, BLOCKING_ITEM);
               }
            }
         }
      } else {
         this.storage = Bukkit.createInventory(this, var2, var3);
      }

      this.uuid = UUID.randomUUID();
   }

   public static List<VehicleItemStorage> load(Vehicle var0) {
      ConfigurationSection var1 = vehicleItemStorageYml.getConfigurationSection("vehicles." + String.valueOf(var0.getUniqueId()) + ".storage");
      ArrayList var2 = new ArrayList();
      if (var1 == null) {
         return new ArrayList();
      } else {
         Iterator var3 = var1.getKeys(false).iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            int var5 = var1.getInt(var4 + ".size");
            VehicleItemStorage var6 = new VehicleItemStorage(var0, var5);

            for(int var7 = 0; var7 < var6.getHolder().getInventory().getSize(); ++var7) {
               ItemStack var8 = var1.getItemStack(var4 + ".items." + var7, (ItemStack)null);
               if (var8 != null && !var8.getType().isAir()) {
                  var6.getHolder().getInventory().setItem(var7, var8);
               }
            }

            var6.getHolder().setUniqueId(UUID.fromString(var4));
            var2.add(var6);
         }

         return var2;
      }
   }

   protected void setUniqueId(UUID var1) {
      this.uuid = var1;
   }

   public void save() {
      this.storage.remove(BLOCKING_ITEM);
      vehicleItemStorageYml.set("vehicles." + String.valueOf(this.vehicle.getUniqueId()) + ".storage." + this.uuid.toString() + ".size", this.slots);

      for(int var1 = 0; var1 < this.storage.getContents().length; ++var1) {
         vehicleItemStorageYml.set("vehicles." + String.valueOf(this.vehicle.getUniqueId()) + ".storage." + this.uuid.toString() + ".items." + var1, this.storage.getContents()[var1]);
      }

      try {
         vehicleItemStorageYml.save(vehicleStorageFile);
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   @NotNull
   public Inventory getInventory() {
      return this.storage;
   }

   public Vehicle getVehicle() {
      return this.vehicle;
   }

   static {
      BLOCKING_ITEM = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
      ItemUtils.meta(BLOCKING_ITEM, (var0) -> {
         var0.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&r"));
         var0.getPersistentDataContainer().set(BLOCKING_ITEM_NAMESPACE, PersistentDataType.STRING, "yes");
      });
      storageFolder = new File(InfiniteVehicles.getPlugin().getDataFolder(), "storage");
      storageFolder.mkdirs();
      vehicleStorageFile = new File(storageFolder, "VehicleItemStorage.yml");
      if (!vehicleStorageFile.exists()) {
         try {
            vehicleStorageFile.createNewFile();
         } catch (IOException var1) {
            throw new IllegalStateException("couldn't generate vehicle inventory storage file", var1);
         }
      }

      vehicleItemStorageYml = YamlConfiguration.loadConfiguration(vehicleStorageFile);
   }
}
