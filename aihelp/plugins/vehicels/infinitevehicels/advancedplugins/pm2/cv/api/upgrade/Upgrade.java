package advancedplugins.pm2.cv.api.upgrade;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.interfaces.ConfigurationSectionWritable;
import advancedplugins.pm2.cv.api.interfaces.IDeyed;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class Upgrade implements ConfigurationSectionWritable, IDeyed {
   @NotNull
   private final String id;
   private final int slot;
   private final Map<Integer, UpgradeTier> upgradeTiers;

   public void write(@NotNull ConfigurationSection var1) {
      var1.set("slot", this.slot);
      ConfigurationSection var2 = var1.createSection("tiers");
      Iterator var3 = this.upgradeTiers.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         ((UpgradeTier)var4.getValue()).write(var2.getConfigurationSection(((Integer)var4.getKey()).toString()));
      }

   }

   public static Upgrade load(ConfigurationSection var0, String var1) {
      int var2 = var0.getInt("slot");
      ConfigurationSection var3 = var0.getConfigurationSection("tiers");
      if (var3 == null) {
         return null;
      } else {
         HashMap var4 = new HashMap();
         Iterator var5 = var3.getKeys(false).iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();

            try {
               var4.put(Integer.parseInt(var6), UpgradeTier.load(var3.getConfigurationSection(var6), Integer.parseInt(var6)));
            } catch (NumberFormatException var8) {
               Logger var10000 = InfiniteVehicles.getPlugin().getLogger();
               String var10001 = var0.getCurrentPath();
               var10000.warning("Failed to load upgrade tier at: " + var10001 + " (invalid tier number: " + var6 + ")");
            }
         }

         return var4.isEmpty() ? null : new Upgrade(var1, var2, var4);
      }
   }

   @NotNull
   public String getId() {
      return this.id;
   }

   public int getSlot() {
      return this.slot;
   }

   public Map<Integer, UpgradeTier> getUpgradeTiers() {
      return this.upgradeTiers;
   }

   public Upgrade(@NotNull String var1, int var2, Map<Integer, UpgradeTier> var3) {
      this.id = var1;
      this.slot = var2;
      this.upgradeTiers = var3;
   }
}
