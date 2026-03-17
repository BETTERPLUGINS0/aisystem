package advancedplugins.pm2.cv.api.upgrade;

import advancedplugins.pm2.cv.api.configuration.GuiConfiguration;
import advancedplugins.pm2.cv.api.interfaces.ConfigurationSectionWritable;
import advancedplugins.pm2.cv.api.vehicle.controller.VehicleControllerProperties;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;

public class UpgradeTier implements ConfigurationSectionWritable {
   private final int tier;
   @NotNull
   private final GuiConfiguration.Item item;
   @NotNull
   private final GuiConfiguration.Item selectedItem;
   @NotNull
   private final GuiConfiguration.Item physicalItem;
   @NotNull
   private final List<UpgradeRequirement> upgradeRequirements;
   @NotNull
   private final VehicleControllerProperties upgradeProperties;

   public void write(@NotNull ConfigurationSection var1) {
      this.item.write(var1);
      if (!this.selectedItem.getDescription().isEmpty()) {
         var1.set("selected-description", this.selectedItem.getDescription());
      }

      ConfigurationSection var2 = var1.createSection("requirements");
      Iterator var3 = this.upgradeRequirements.iterator();

      while(var3.hasNext()) {
         UpgradeRequirement var4 = (UpgradeRequirement)var3.next();
         var2.set(var4.getType(), var4.getAmount());
      }

      this.upgradeProperties.write(var1.createSection("upgrades"));
   }

   public static UpgradeTier load(ConfigurationSection var0, int var1) {
      GuiConfiguration.Item var2 = new GuiConfiguration.Item(var0);
      GuiConfiguration.Item var3 = new GuiConfiguration.Item(var0);
      List var4 = var0.getStringList("selected-description");
      if (!var4.isEmpty() && var3.getDescription() != null) {
         var3.getDescription().clear();
         var3.getDescription().addAll(var4);
      }

      ArrayList var5 = new ArrayList();
      ConfigurationSection var6 = var0.getConfigurationSection("requirements");
      if (var6 != null) {
         Iterator var7 = var6.getKeys(false).iterator();

         while(var7.hasNext()) {
            String var8 = (String)var7.next();
            int var9 = var6.getInt(var8);
            var5.add(UpgradeRequirement.load(var8, var9));
         }
      }

      ConfigurationSection var11 = var0.getConfigurationSection("upgrades");
      if (var11 == null) {
         throw new InvalidConfigurationException("Failed to load upgrade tier at: " + var0.getCurrentPath() + " (missing upgrade properties section)");
      } else {
         VehicleControllerProperties var12 = VehicleControllerProperties.load(var11);
         ConfigurationSection var13 = var0.getConfigurationSection("physical-item");
         if (var13 == null) {
            throw new InvalidConfigurationException("Failed to load upgrade tier at: " + var0.getCurrentPath() + " (missing physical item section)");
         } else {
            GuiConfiguration.Item var10 = new GuiConfiguration.Item(var13);
            return new UpgradeTier(var1, var2, var3, var10, var5, var12);
         }
      }
   }

   public int getTier() {
      return this.tier;
   }

   @NotNull
   public GuiConfiguration.Item getItem() {
      return this.item;
   }

   @NotNull
   public GuiConfiguration.Item getSelectedItem() {
      return this.selectedItem;
   }

   @NotNull
   public GuiConfiguration.Item getPhysicalItem() {
      return this.physicalItem;
   }

   @NotNull
   public List<UpgradeRequirement> getUpgradeRequirements() {
      return this.upgradeRequirements;
   }

   @NotNull
   public VehicleControllerProperties getUpgradeProperties() {
      return this.upgradeProperties;
   }

   public UpgradeTier(int var1, @NotNull GuiConfiguration.Item var2, @NotNull GuiConfiguration.Item var3, @NotNull GuiConfiguration.Item var4, @NotNull List<UpgradeRequirement> var5, @NotNull VehicleControllerProperties var6) {
      this.tier = var1;
      this.item = var2;
      this.selectedItem = var3;
      this.physicalItem = var4;
      this.upgradeRequirements = var5;
      this.upgradeProperties = var6;
   }
}
