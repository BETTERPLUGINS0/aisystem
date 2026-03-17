package advancedplugins.pm2.cv.api.configuration;

import advancedplugins.pm2.cv.api.InfiniteVehiclesPluginBase;
import advancedplugins.pm2.cv.api.util.ColorUtil;
import advancedplugins.pm2.cv.api.util.LangFormatter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

public final class LangConfiguration {
   public static final LangConfiguration.Entry PREFIX = new LangConfiguration.Entry("command.prefix", ChatColor.translateAlternateColorCodes('&', "&7[&fInfinite&bVehicles&7] &r"));
   public static final LangConfiguration.Entry COMMAND_UNKNOWN_ITEM;
   public static final LangConfiguration.Entry COMMAND_INVALID_RADIUS;
   public static final LangConfiguration.Entry COMMAND_UNKNOWN_VEHICLE;
   public static final LangConfiguration.Entry VEHICLE_SUCCESS_REMOVE;
   public static final LangConfiguration.Entry VEHICLE_SUCCESS_SPAWN;
   public static final LangConfiguration.Entry ITEM_SUCCESS_GIVE;
   public static final LangConfiguration.Entry SPAWN_CANNOT_PLACE_HERE;
   public static final LangConfiguration.Entry FUEL_TANK_FULL;
   public static final LangConfiguration.Entry GUI_OWNERSHIP;
   public static final LangConfiguration.Entry VEHICLE_STORAGE_NAME;
   public static final LangConfiguration.Entry VEHICLE_STORAGE_PICK_UP_WARNING;
   public static final LangConfiguration.Entry LEADERBOARD_NOT_ENABLED;
   public static final LangConfiguration.Entry RELOADING_PLUGIN;
   public static final LangConfiguration.Entry RELOADED_PLUGIN;
   public static final LangConfiguration.Entry COMMAND_UNKNOWN_UPGRADE;
   public static final LangConfiguration.Entry UNKNOWN_UPGRADE_TYPE;
   public static final LangConfiguration.Entry UNKNOWN_UPGRADE_TIER;
   public static final LangConfiguration.Entry NO_ITEM_META;
   public static final LangConfiguration.Entry UNKNOWN_PLAYER;
   public static final LangConfiguration.Entry COMMAND_UNKNOWN_LOCATION;
   public static final LangConfiguration.Entry PLUGIN_RELOADING;
   public static final LangConfiguration.Entry VEHICLES_MENU_TITLE;
   public static final LangConfiguration.Entry VEHICLES_MENU_PREV_ITEM_NAME;
   public static final LangConfiguration.Entry VEHICLES_MENU_NEXT_ITEM_NAME;
   public static final LangConfiguration.Entry VEHICLES_MENU_PREV_ITEM_LORE;
   public static final LangConfiguration.Entry VEHICLES_MENU_NEXT_ITEM_LORE;
   public static final LangConfiguration.Entry ERROR_INVENTORY_FULL;
   public static final LangConfiguration.Entry REPAIRED_VEHICLE;
   public static final LangConfiguration.Entry REPAIR_NOT_NEEDED;
   public static final LangConfiguration.Entry REPAIR_TOO_FAST;
   public static final LangConfiguration.Entry REPAIR_NOT_ENOUGH;
   public static final LangConfiguration.Entry VEHICLE_KEY_ERROR;
   public static final LangConfiguration.Entry VEHICLE_KEY_SET;
   public static final LangConfiguration.Entry VEHICLE_KEY_NOT_OWNER;

   public static void load(@NotNull InfiniteVehiclesPluginBase var0) {
      File var1 = new File(var0.getDataFolder(), "LanguageConfiguration.yml");
      if (!var1.exists()) {
         var1.getParentFile().mkdirs();

         try {
            Files.createFile(var1.toPath());
         } catch (IOException var10) {
            throw new IllegalStateException("couldn't generate language configuration file", var10);
         }
      }

      YamlConfiguration var2 = YamlConfiguration.loadConfiguration(var1);
      Field[] var3 = LangConfiguration.class.getFields();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Field var6 = var3[var5];
         if (LangConfiguration.Entry.class.isAssignableFrom(var6.getType())) {
            try {
               ((LangConfiguration.Entry)var6.get((Object)null)).load(var2);
            } catch (IllegalAccessException var9) {
               var9.printStackTrace();
            }
         }
      }

      try {
         var2.save(var1);
      } catch (IOException var8) {
         var8.printStackTrace();
      }

   }

   static {
      COMMAND_UNKNOWN_ITEM = new LangConfiguration.Entry("command.unknown-item", String.valueOf(ChatColor.RED) + "An item with that name doesn't exist!");
      COMMAND_INVALID_RADIUS = new LangConfiguration.Entry("command.invalid-radius", String.valueOf(ChatColor.RED) + "The radius is invalid! Must be between %s and %s");
      COMMAND_UNKNOWN_VEHICLE = new LangConfiguration.Entry("command.unknown-vehicle", String.valueOf(ChatColor.RED) + "A vehicle with that name doesn't exist!");
      VEHICLE_SUCCESS_REMOVE = new LangConfiguration.Entry("command.vehicle.success-remove", String.valueOf(ChatColor.GREEN) + "Successfully removed %s vehicles!");
      VEHICLE_SUCCESS_SPAWN = new LangConfiguration.Entry("command.vehicle.success-spawn", String.valueOf(ChatColor.GREEN) + "Successfully spawned vehicle of type %s!");
      ITEM_SUCCESS_GIVE = new LangConfiguration.Entry("command.item.success-give", String.valueOf(ChatColor.GREEN) + "Successfully gave %s %s to %s!");
      SPAWN_CANNOT_PLACE_HERE = new LangConfiguration.Entry("spawn.cannot-place-here", String.valueOf(ChatColor.RED) + "Cannot place this vehicle here!");
      FUEL_TANK_FULL = new LangConfiguration.Entry("fuel.tank-full", String.valueOf(ChatColor.RED) + "Vehicle tank is already full!");
      GUI_OWNERSHIP = new LangConfiguration.Entry("gui.ownership.not-the-owner", String.valueOf(ChatColor.RED) + "You cannot use a vehicle you don't own!");
      VEHICLE_STORAGE_NAME = new LangConfiguration.Entry("vehicle.storage.name", ChatColor.translateAlternateColorCodes('&', "%s Storage"));
      VEHICLE_STORAGE_PICK_UP_WARNING = new LangConfiguration.Entry("vehicle.storage.warning.pickup", ChatColor.translateAlternateColorCodes('&', "&cPicking that vehicle up will drop the items in its storage! Click again to pick it up."));
      LEADERBOARD_NOT_ENABLED = new LangConfiguration.Entry("vehicle.leaderboard.not-enabled", ChatColor.translateAlternateColorCodes('&', "&cLeaderboard is not enabled for this vehicle!"));
      RELOADING_PLUGIN = new LangConfiguration.Entry("command.reloading-plugin", ChatColor.translateAlternateColorCodes('&', "&cReloading plugin..."));
      RELOADED_PLUGIN = new LangConfiguration.Entry("command.reloaded-plugin", ChatColor.translateAlternateColorCodes('&', "&aSuccessfully reloaded the plugin in %s ms!"));
      COMMAND_UNKNOWN_UPGRADE = new LangConfiguration.Entry("command.unknown-upgrade", ChatColor.translateAlternateColorCodes('&', "&cAn upgrade with that name doesn't exist!"));
      UNKNOWN_UPGRADE_TYPE = new LangConfiguration.Entry("command.unknown-upgrade-type", ChatColor.translateAlternateColorCodes('&', "&cAn upgrade type with that type doesn't exist!"));
      UNKNOWN_UPGRADE_TIER = new LangConfiguration.Entry("command.unknown-upgrade-tier", ChatColor.translateAlternateColorCodes('&', "&cAn upgrade tier with that tier number doesn't exist!"));
      NO_ITEM_META = new LangConfiguration.Entry("command.no-item-meta", ChatColor.translateAlternateColorCodes('&', "&cThe set item cannot be given due to no ItemMeta being present for the item's material! Contact an admin!"));
      UNKNOWN_PLAYER = new LangConfiguration.Entry("command.unknown-player", ChatColor.translateAlternateColorCodes('&', "&cAn unknown player with that name doesn't exist!"));
      COMMAND_UNKNOWN_LOCATION = new LangConfiguration.Entry("command.unknown-location", ChatColor.translateAlternateColorCodes('&', "&cPlease provide a valid location!"));
      PLUGIN_RELOADING = new LangConfiguration.Entry("command.plugin-reloading", ChatColor.translateAlternateColorCodes('&', "&cThe plugin is already reloading! Please wait for it to finish before reloading again!"));
      VEHICLES_MENU_TITLE = new LangConfiguration.Entry("vehicles-menu.title", ColorUtil.translate("&f&lInfinite&b&lVehicles &8| &6 Page %s"));
      VEHICLES_MENU_PREV_ITEM_NAME = new LangConfiguration.Entry("vehicles-menu.prev-item.name", "&bGo back to page %s");
      VEHICLES_MENU_NEXT_ITEM_NAME = new LangConfiguration.Entry("vehicles-menu.next-item,name", "&bGo to page %s");
      VEHICLES_MENU_PREV_ITEM_LORE = new LangConfiguration.Entry("vehicles-menu.prev-item.lore", "");
      VEHICLES_MENU_NEXT_ITEM_LORE = new LangConfiguration.Entry("vehicles-menu.next-item.lore", "");
      ERROR_INVENTORY_FULL = new LangConfiguration.Entry("error.inventory-full", "&cYour inventory seems to be full, unable to add the item(s).");
      REPAIRED_VEHICLE = new LangConfiguration.Entry("info.repaired-vehicle", "&aRepaired your vehicle to %s health.");
      REPAIR_NOT_NEEDED = new LangConfiguration.Entry("error.repair-not-needed", "&cThe vehicle is already at full health! No repair needed!");
      REPAIR_TOO_FAST = new LangConfiguration.Entry("error.repair-too-fast", "&cWoah! That's too fast, you can repair the vehicle again in %s seconds");
      REPAIR_NOT_ENOUGH = new LangConfiguration.Entry("error.repair-not-enough", "&cYou don't have enough materials to repair the vehicle!");
      VEHICLE_KEY_ERROR = new LangConfiguration.Entry("error.vehicle-key", "&cEither the vehicle is locked or that key has been used to lock another vehicle!");
      VEHICLE_KEY_SET = new LangConfiguration.Entry("vehicle-key-set", "&bVehicle locked! Now other players cannot drive this vehicle.");
      VEHICLE_KEY_NOT_OWNER = new LangConfiguration.Entry("error.vehicle-key-not-owner", "&cSorry but you are not the owner of this vehicle!");
   }

   public static final class Entry {
      @NotNull
      private final String key;
      @NotNull
      private final String defaultValue;
      @NotNull
      private String value;

      private Entry(@NotNull String var1, @NotNull String var2) {
         this.key = var1;
         this.defaultValue = var2;
         this.value = var2;
      }

      @NotNull
      public String value() {
         return ColorUtil.translate(this.value);
      }

      @NotNull
      public List<String> asList() {
         return this.value().isEmpty() ? new ArrayList() : new ArrayList(Arrays.asList(this.value().split("\n")));
      }

      @NotNull
      public List<String> asList(Object... var1) {
         return this.value().isEmpty() ? new ArrayList() : new ArrayList(Arrays.asList(String.format(this.value(), var1).split("\n")));
      }

      @NotNull
      public LangFormatter valueWithFormat(boolean var1) {
         return var1 ? LangFormatter.single(this.value()) : LangFormatter.multi(this.value());
      }

      private void load(@NotNull ConfigurationSection var1) {
         String var2 = var1.getString(this.key);
         if (StringUtils.isBlank(var2)) {
            this.value = this.defaultValue;
            var1.set(this.key, this.decompileColors(this.defaultValue));
         } else {
            this.value = ChatColor.translateAlternateColorCodes('&', var2);
         }

      }

      private String decompileColors(String var1) {
         char[] var2 = var1.toCharArray();

         for(int var3 = 0; var3 < var2.length - 1; ++var3) {
            if (var2[var3] == 167 && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf(var2[var3 + 1]) != -1) {
               var2[var3] = '&';
            }
         }

         return new String(var2);
      }
   }
}
