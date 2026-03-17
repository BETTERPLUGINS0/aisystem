package advancedplugins.pm2.cv.api.vehicle.configuration;

import advancedplugins.pm2.cv.api.interfaces.ConfigurationSectionWritable;
import advancedplugins.pm2.cv.api.util.inventory.ItemStackUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class VehicleAmmoConfiguration implements ConfigurationSectionWritable {
   private final int maxAmmo;
   private final ItemStack ammoItem;

   public void write(@NotNull ConfigurationSection var1) {
   }

   public static VehicleAmmoConfiguration load(@NotNull ConfigurationSection var0) {
      try {
         ConfigurationSection var1 = var0.getConfigurationSection("ammo-item");
         if (var1 == null) {
            throw new RuntimeException("'ammoItemSection' not found!");
         } else {
            ItemStack var2 = ItemStackUtil.loadItem(var1);
            int var3 = var0.getInt("max-ammo");
            return new VehicleAmmoConfiguration(var3, var2);
         }
      } catch (Exception var4) {
         throw new InvalidConfigurationException("Failed to load VehicleAmmoConfiguration: " + var4.getMessage(), var4);
      }
   }

   public static VehicleAmmoConfiguration.VehicleAmmoConfigurationBuilder builder() {
      return new VehicleAmmoConfiguration.VehicleAmmoConfigurationBuilder();
   }

   public int getMaxAmmo() {
      return this.maxAmmo;
   }

   public ItemStack getAmmoItem() {
      return this.ammoItem;
   }

   public VehicleAmmoConfiguration(int var1, ItemStack var2) {
      this.maxAmmo = var1;
      this.ammoItem = var2;
   }

   public static class VehicleAmmoConfigurationBuilder {
      private int maxAmmo;
      private ItemStack ammoItem;

      VehicleAmmoConfigurationBuilder() {
      }

      public VehicleAmmoConfiguration.VehicleAmmoConfigurationBuilder maxAmmo(int var1) {
         this.maxAmmo = var1;
         return this;
      }

      public VehicleAmmoConfiguration.VehicleAmmoConfigurationBuilder ammoItem(ItemStack var1) {
         this.ammoItem = var1;
         return this;
      }

      public VehicleAmmoConfiguration build() {
         return new VehicleAmmoConfiguration(this.maxAmmo, this.ammoItem);
      }

      public String toString() {
         int var10000 = this.maxAmmo;
         return "VehicleAmmoConfiguration.VehicleAmmoConfigurationBuilder(maxAmmo=" + var10000 + ", ammoItem=" + String.valueOf(this.ammoItem) + ")";
      }
   }
}
