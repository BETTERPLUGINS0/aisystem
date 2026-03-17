package advancedplugins.pm2.cv.api.upgrade;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.configuration.GuiConfiguration;
import advancedplugins.pm2.cv.api.interfaces.ConfigurationSectionWritable;
import advancedplugins.pm2.cv.api.interfaces.IDeyed;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class UpgradeConfiguration implements IDeyed, ConfigurationSectionWritable {
   @NotNull
   private final String id;
   @NotNull
   private final GuiConfiguration.IndexedItem item;
   @NotNull
   private final List<Upgrade> upgrades;
   @NotNull
   private final String title;
   private final int rows;

   @NotNull
   public String getId() {
      return this.id;
   }

   public void write(@NotNull ConfigurationSection var1) {
      var1.set("id", this.id);
      this.item.write(var1);
      ConfigurationSection var2 = var1.createSection("upgrades");
      Iterator var3 = this.upgrades.iterator();

      while(var3.hasNext()) {
         Upgrade var4 = (Upgrade)var3.next();
         var4.write(var2.getConfigurationSection(var4.getId()));
      }

   }

   public static UpgradeConfiguration load(ConfigurationSection var0) {
      String var1 = var0.getString("id");
      GuiConfiguration.IndexedItem var2 = new GuiConfiguration.IndexedItem(var0);
      if (var1 != null && var2 != null) {
         ConfigurationSection var3 = var0.getConfigurationSection("upgrades");
         if (var3 == null) {
            InfiniteVehicles.getPlugin().getLogger().warning("Failed to load upgrade at: " + var0.getCurrentPath() + " (missing upgrades section)");
            return null;
         } else {
            ArrayList var4 = new ArrayList();
            Iterator var5 = var3.getKeys(false).iterator();

            while(var5.hasNext()) {
               String var6 = (String)var5.next();
               Upgrade var7 = Upgrade.load(var3.getConfigurationSection(var6), var6);
               if (var7 != null) {
                  var4.add(var7);
               }
            }

            if (var4.isEmpty()) {
               InfiniteVehicles.getPlugin().getLogger().warning("Failed to load upgrade at: " + var0.getCurrentPath() + " (failed to load upgrades)");
               return null;
            } else {
               String var8 = var0.getString("title");
               if (var8 == null) {
                  InfiniteVehicles.getPlugin().getLogger().warning("Failed to load upgrade at: " + var0.getCurrentPath() + " (missing title)");
                  return null;
               } else {
                  int var9 = var0.getInt("rows", 3);
                  if (var9 >= 1 && var9 <= 9) {
                     return new UpgradeConfiguration(var1, var2, var4, var8, var9);
                  } else {
                     InfiniteVehicles.getPlugin().getLogger().warning("Failed to load upgrade at: " + var0.getCurrentPath() + " (rows must be between 1 and 9)");
                     return null;
                  }
               }
            }
         }
      } else {
         InfiniteVehicles.getPlugin().getLogger().warning("Failed to load upgrade at: " + var0.getCurrentPath() + " (missing id, material or display name)");
         return null;
      }
   }

   @NotNull
   public GuiConfiguration.IndexedItem getItem() {
      return this.item;
   }

   @NotNull
   public List<Upgrade> getUpgrades() {
      return this.upgrades;
   }

   @NotNull
   public String getTitle() {
      return this.title;
   }

   public int getRows() {
      return this.rows;
   }

   public UpgradeConfiguration(@NotNull String var1, @NotNull GuiConfiguration.IndexedItem var2, @NotNull List<Upgrade> var3, @NotNull String var4, int var5) {
      this.id = var1;
      this.item = var2;
      this.upgrades = var3;
      this.title = var4;
      this.rows = var5;
   }
}
