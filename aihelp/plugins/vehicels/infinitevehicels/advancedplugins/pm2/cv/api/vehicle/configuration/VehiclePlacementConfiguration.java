package advancedplugins.pm2.cv.api.vehicle.configuration;

import advancedplugins.pm2.cv.api.interfaces.ConfigurationSectionWritable;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class VehiclePlacementConfiguration implements ConfigurationSectionWritable {
   public static final VehiclePlacementConfiguration DEFAULTS = new VehiclePlacementConfiguration(new ArrayList(), false, false, false, false, -1);
   private final List<String> worlds;
   private final boolean whitelist;
   private final boolean blacklist;
   private final boolean addOnExit;
   private final boolean addOnPlace;
   private final int placeLimit;

   public static VehiclePlacementConfiguration load(@NotNull ConfigurationSection var0) {
      boolean var1 = var0.getBoolean("whitelist");
      boolean var2 = var0.getBoolean("blacklist");
      boolean var3 = var0.getBoolean("add-on-exit");
      boolean var4 = var0.getBoolean("add-on-place");
      List var5 = var0.getStringList("worlds");
      int var6 = var0.getInt("place-limit", -1);
      return new VehiclePlacementConfiguration(var5, var1 && !var2, var2 && !var1, var3, var4, var6);
   }

   public void write(@NotNull ConfigurationSection var1) {
      var1.set("worlds", this.worlds);
      var1.set("whitelist", this.whitelist && !this.blacklist);
      var1.set("blacklist", this.blacklist && !this.whitelist);
      var1.set("add-on-exit", this.addOnExit);
      var1.set("add-on-place", this.addOnPlace);
   }

   public VehiclePlacementConfiguration(List<String> var1, boolean var2, boolean var3, boolean var4, boolean var5, int var6) {
      this.worlds = var1;
      this.whitelist = var2;
      this.blacklist = var3;
      this.addOnExit = var4;
      this.addOnPlace = var5;
      this.placeLimit = var6;
   }

   public List<String> getWorlds() {
      return this.worlds;
   }

   public boolean isWhitelist() {
      return this.whitelist;
   }

   public boolean isBlacklist() {
      return this.blacklist;
   }

   public boolean isAddOnExit() {
      return this.addOnExit;
   }

   public boolean isAddOnPlace() {
      return this.addOnPlace;
   }

   public int getPlaceLimit() {
      return this.placeLimit;
   }
}
