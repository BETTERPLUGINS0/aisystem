package advancedplugins.pm2.cv.api.configuration;

import advancedplugins.pm2.cv.api.InfiniteVehiclesPluginBase;
import advancedplugins.pm2.cv.api.enums.EnumPlaceholder;
import advancedplugins.pm2.cv.api.interfaces.ConfigurationSectionWritable;
import advancedplugins.pm2.cv.api.interfaces.VersionSensible;
import advancedplugins.pm2.cv.api.util.ConfigurationUtil;
import advancedplugins.pm2.cv.api.util.MathUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class GuiConfiguration {
   public static int ROWS;
   public static String TITLE;
   @Nullable
   public static GuiConfiguration.IndexedItem FUEL_DISPLAY_ITEM;
   @Nullable
   public static GuiConfiguration.IndexedItem PICKUP_ITEM;
   @Nullable
   public static GuiConfiguration.IndexedItem ADMIN_PICKUP_ITEM;
   @Nullable
   public static GuiConfiguration.IndexedItem SEATS_GUI_ITEM;
   @Nullable
   public static GuiConfiguration.IndexedItem STORAGE_GUI_ITEM;
   @Nullable
   public static GuiConfiguration.Item SEAT_ITEM;
   @Nullable
   public static GuiConfiguration.Item OPERATOR_SEAT_ITEM;
   @Nullable
   public static GuiConfiguration.IndexedItem REPAIR_ITEM;
   private static final int DEFAULT_ROWS = 3;
   private static final String DEFAULT_TITLE = "&0Vehicle";
   @NotNull
   private static final GuiConfiguration.IndexedItem DEFAULT_FUEL_DISPLAY_ITEM;
   @NotNull
   private static final GuiConfiguration.IndexedItem DEFAULT_PICKUP_ITEM;
   @NotNull
   private static final GuiConfiguration.IndexedItem DEFAULT_ADMIN_PICKUP_ITEM;
   @NotNull
   private static final GuiConfiguration.IndexedItem DEFAULT_SEATS_GUI_ITEM;
   @NotNull
   private static final GuiConfiguration.IndexedItem DEFAULT_STORAGE_GUI_ITEM;
   @NotNull
   private static final GuiConfiguration.Item DEFAULT_SEAT_ITEM;
   @NotNull
   private static final GuiConfiguration.Item DEFAULT_OPERATOR_SEAT_ITEM;
   @NotNull
   private static final GuiConfiguration.IndexedItem DEFAULT_REPAIR_ITEM;

   @VersionSensible
   public static void load(@NotNull InfiniteVehiclesPluginBase var0) {
      File var1 = new File(var0.getDataFolder(), "GuiConfiguration.yml");
      boolean var2 = !var1.exists();
      if (!var1.exists()) {
         var1.getParentFile().mkdirs();

         try {
            Files.createFile(var1.toPath());
         } catch (IOException var13) {
            throw new IllegalStateException("couldn't generate gui configuration file", var13);
         }
      }

      YamlConfiguration var3 = YamlConfiguration.loadConfiguration(var1);
      if (var2) {
         var3.set("rows", 3);
         var3.set("title", "&0Vehicle");
         DEFAULT_FUEL_DISPLAY_ITEM.write(var3.createSection("fuel-display-item"));
         DEFAULT_PICKUP_ITEM.write(var3.createSection("pickup-item"));
         DEFAULT_ADMIN_PICKUP_ITEM.write(var3.createSection("admin-pickup-item"));
         DEFAULT_SEATS_GUI_ITEM.write(var3.createSection("seats-gui-item"));
         DEFAULT_SEAT_ITEM.write(var3.createSection("seat-item"));
         DEFAULT_OPERATOR_SEAT_ITEM.write(var3.createSection("operator-seat-item"));
         DEFAULT_STORAGE_GUI_ITEM.write(var3.createSection("inventory-item"));
         DEFAULT_REPAIR_ITEM.write(var3.createSection("repair-item"));

         try {
            var3.save(var1);
         } catch (IOException var12) {
            throw new IllegalStateException("couldn't generate default gui configuration", var12);
         }
      }

      ROWS = MathUtil.clamp(var3.getInt("rows"), 0, 6);
      TITLE = var3.getString("title", "");
      ConfigurationSection var4 = var3.getConfigurationSection("fuel-display-item");
      ConfigurationSection var5 = var3.getConfigurationSection("pickup-item");
      ConfigurationSection var6 = var3.getConfigurationSection("admin-pickup-item");
      ConfigurationSection var7 = var3.getConfigurationSection("seats-gui-item");
      ConfigurationSection var8 = var3.getConfigurationSection("inventory-item");
      ConfigurationSection var9 = var3.getConfigurationSection("seat-item");
      ConfigurationSection var10 = var3.getConfigurationSection("operator-seat-item");
      ConfigurationSection var11 = var3.getConfigurationSection("repair-item");
      FUEL_DISPLAY_ITEM = var4 != null ? new GuiConfiguration.IndexedItem(var4) : null;
      PICKUP_ITEM = var5 != null ? new GuiConfiguration.IndexedItem(var5) : null;
      ADMIN_PICKUP_ITEM = var6 != null ? new GuiConfiguration.IndexedItem(var6) : null;
      SEATS_GUI_ITEM = var7 != null ? new GuiConfiguration.IndexedItem(var7) : null;
      STORAGE_GUI_ITEM = var8 != null ? new GuiConfiguration.IndexedItem(var8) : null;
      SEAT_ITEM = var9 != null ? new GuiConfiguration.Item(var9) : null;
      OPERATOR_SEAT_ITEM = var10 != null ? new GuiConfiguration.Item(var10) : null;
      REPAIR_ITEM = var11 != null ? new GuiConfiguration.IndexedItem(var11) : null;
   }

   static {
      Material var10003 = Material.PLAYER_HEAD;
      String[] var10005 = new String[]{"", null};
      String var10008 = String.valueOf(EnumPlaceholder.FUEL_LEVEL);
      var10005[1] = "&4" + var10008 + " / " + String.valueOf(EnumPlaceholder.FUEL_CAPACITY) + " &6(" + String.valueOf(EnumPlaceholder.FUEL_LEVEL_PERCENTAGE) + "%)";
      DEFAULT_FUEL_DISPLAY_ITEM = new GuiConfiguration.IndexedItem(11, var10003, "&6Current Fuel", Arrays.asList(var10005), (Integer)null, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGI4OWRlNDhhOWI4N2NmZDA3YzcwNGYyYmU1ZTVhOGNjNDVlODA3OWQzOGZhYWVkZjEzYjE1ZDE1YTEwYTcwYyJ9fX0=");
      DEFAULT_PICKUP_ITEM = new GuiConfiguration.IndexedItem(0, Material.PLAYER_HEAD, "&6Pick Up", Arrays.asList("", "&6Click here to pick up"), (Integer)null, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTg5YWQ1MDJiNDc1NjIzZGVhYzAxZjBjZmY5NmY4NjUxZGQ2ZDkzYjEzNWMwYjU3NTQ4N2NhMGE0NTM5ZTcyIn19fQ==");
      DEFAULT_ADMIN_PICKUP_ITEM = new GuiConfiguration.IndexedItem(1, Material.PLAYER_HEAD, "&6Admin Pick Up", Arrays.asList("", "&6Click here to pick up with the admin privilege", "&6Original owner of the vehicle -&f %player%"), (Integer)null, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTg5YWQ1MDJiNDc1NjIzZGVhYzAxZjBjZmY5NmY4NjUxZGQ2ZDkzYjEzNWMwYjU3NTQ4N2NhMGE0NTM5ZTcyIn19fQ==");
      DEFAULT_SEATS_GUI_ITEM = new GuiConfiguration.IndexedItem(13, Material.PLAYER_HEAD, "&6Seats", (List)null, (Integer)null, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTI2ZWU4ZThiOWNiYThmODc2ZGM3NmY4ZmIwNjcwYjJhYmRjOTJjNjRlNGViNjc1ZWU0ZTU4MmRkYTQ4ZWZhOCJ9fX0=");
      DEFAULT_STORAGE_GUI_ITEM = new GuiConfiguration.IndexedItem(15, Material.PLAYER_HEAD, "&6Storage", List.of("&fClick here to see the Vehicle's Storage!"), (Integer)null, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDVjNmRjMmJiZjUxYzM2Y2ZjNzcxNDU4NWE2YTU2ODNlZjJiMTRkNDdkOGZmNzE0NjU0YTg5M2Y1ZGE2MjIifX19");
      DEFAULT_SEAT_ITEM = new GuiConfiguration.Item(Material.SADDLE, "&6Passenger N°" + String.valueOf(EnumPlaceholder.SEAT_INDEX) + " Seat", (List)null, (Integer)null, (String)null);
      DEFAULT_OPERATOR_SEAT_ITEM = new GuiConfiguration.Item(Material.PLAYER_HEAD, "&4Operator Seat", (List)null, (Integer)null, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTI2ZWU4ZThiOWNiYThmODc2ZGM3NmY4ZmIwNjcwYjJhYmRjOTJjNjRlNGViNjc1ZWU0ZTU4MmRkYTQ4ZWZhOCJ9fX0=");
      DEFAULT_REPAIR_ITEM = new GuiConfiguration.IndexedItem(18, Material.PLAYER_HEAD, "&6Repair", List.of("&fHealth: &7" + String.valueOf(EnumPlaceholder.HEALTH) + " / " + String.valueOf(EnumPlaceholder.MAX_HEALTH), "&fRepair: " + String.valueOf(EnumPlaceholder.NEED_REPAIR)), (Integer)null, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTI2ZWU4ZThiOWNiYThmODc2ZGM3NmY4ZmIwNjcwYjJhYmRjOTJjNjRlNGViNjc1ZWU0ZTU4MmRkYTQ4ZWZhOCJ9fX0=");
   }

   public static class IndexedItem extends GuiConfiguration.Item {
      private final int slot;

      public IndexedItem(int var1, @NotNull Material var2, @Nullable String var3, @Nullable List<String> var4, @Nullable Integer var5, @Nullable String var6) {
         super(var2, var3, var4, var5, var6);
         this.slot = var1;
      }

      @VersionSensible
      public IndexedItem(@NotNull ConfigurationSection var1) {
         super(var1);
         int var2 = var1.getInt("slot");
         if (var2 >= 0 && var2 <= 53) {
            this.slot = var2;
         } else {
            throw new InvalidConfigurationException("invalid slot: " + var2);
         }
      }

      public void write(@NotNull ConfigurationSection var1) {
         var1.set("slot", this.slot);
         super.write(var1);
      }

      public int getSlot() {
         return this.slot;
      }
   }

   public static class Item implements ConfigurationSectionWritable {
      @NotNull
      private final Material material;
      @Nullable
      private final String displayName;
      @Nullable
      private final List<String> description;
      @Nullable
      private final Integer customModelData;
      @Nullable
      private final String headTexture;

      public Item(@NotNull Material var1, @Nullable String var2, @Nullable List<String> var3, @Nullable Integer var4, @Nullable String var5) {
         this.material = var1;
         this.displayName = var2;
         this.description = var3;
         this.customModelData = var4;
         this.headTexture = var5;
      }

      public Item(@NotNull ConfigurationSection var1) {
         Material var2 = null;

         try {
            var2 = Material.valueOf(var1.getString("material"));
         } catch (IllegalArgumentException var4) {
         }

         if (var2 != null) {
            this.material = var2;
            this.displayName = var1.getString("display-name");
            this.description = var1.getStringList("description");
            this.headTexture = var1.getString("head-texture");
            Object var3 = var1.get("custom-model-data");
            this.customModelData = var3 instanceof Number ? ((Number)var3).intValue() : null;
         } else {
            throw new InvalidConfigurationException("invalid material: " + var1.getString("material"));
         }
      }

      public void write(@NotNull ConfigurationSection var1) {
         ConfigurationUtil.writeEnum(this.material, var1, "material");
         var1.set("display-name", this.displayName);
         var1.set("description", this.description != null && this.description.size() > 0 ? this.description : null);
         var1.set("custom-model-data", this.customModelData);
         var1.set("head-texture", this.headTexture);
      }

      @NotNull
      public Material getMaterial() {
         return this.material;
      }

      @Nullable
      public String getDisplayName() {
         return this.displayName;
      }

      @Nullable
      public List<String> getDescription() {
         return this.description;
      }

      @Nullable
      public Integer getCustomModelData() {
         return this.customModelData;
      }

      @Nullable
      public String getHeadTexture() {
         return this.headTexture;
      }
   }
}
