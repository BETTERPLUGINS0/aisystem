package advancedplugins.pm2.cv.api.item;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.enums.EnumItemAction;
import advancedplugins.pm2.cv.api.interfaces.ConfigurationSectionWritable;
import advancedplugins.pm2.cv.api.interfaces.IDeyed;
import advancedplugins.pm2.cv.api.registry.Registries;
import advancedplugins.pm2.cv.api.util.ConfigurationUtil;
import advancedplugins.pm2.cv.api.util.Constants;
import advancedplugins.pm2.cv.api.util.LangFormatter;
import advancedplugins.pm2.cv.api.util.inventory.ItemStackUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemConfiguration implements IDeyed, ConfigurationSectionWritable {
   @NotNull
   private final String id;
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
   @Nullable
   private final ItemConfiguration.Action action;

   @NotNull
   public static ItemStack buildItemStack(@NotNull ItemConfiguration var0) {
      return buildItemStack(var0, (List)null);
   }

   @NotNull
   public static ItemStack buildItemStack(@NotNull ItemConfiguration var0, @Nullable List<String> var1) {
      ArrayList var2 = new ArrayList();
      if (var0.description != null) {
         var2.addAll(var0.description);
      }

      if (var1 != null) {
         var2.addAll(var1);
      }

      ItemStack var3 = ItemStackUtil.buildCustomItem(var0.material, var0.customModelData, var0.displayName, var2);
      if (ItemStackUtil.isHead(var0.material) && StringUtils.isNotBlank(var0.headTexture)) {
         InfiniteVehicles.getTexturedHeadService().applyTexture(var3, var0.headTexture);
      }

      ItemStackUtil.setPersistentData(var3, Constants.NamespacedKeys.ITEM_ID, PersistentDataType.STRING, var0.id);
      ItemStackUtil.setPersistentData(var3, Constants.NamespacedKeys.UNIQUE_ID, PersistentDataType.STRING, UUID.randomUUID().toString());
      return var3;
   }

   @NotNull
   public static ItemStack buildFuelItemStack(@NotNull ItemConfiguration var0, float var1) {
      ItemStack var2 = buildItemStack(var0);
      if (var0.displayName != null) {
         ItemStackUtil.setDisplayName(var2, formatFuel(var0.displayName, var1));
      }

      if (var0.description != null) {
         ArrayList var3 = new ArrayList();
         var0.description.forEach((var2x) -> {
            var3.add(formatFuel(var2x, var1));
         });
         ItemStackUtil.setLore(var2, var3);
      }

      return var2;
   }

   private static String formatFuel(@NotNull String var0, float var1) {
      return LangFormatter.single(var0).arg('f', String.format("%.2f", var1)).format();
   }

   @Nullable
   public static String getItemId(@NotNull ItemStack var0) {
      return (String)ItemStackUtil.getPersistentData(var0, Constants.NamespacedKeys.ITEM_ID, PersistentDataType.STRING);
   }

   @Nullable
   public static String getItemUniqueId(@NotNull ItemStack var0) {
      return (String)ItemStackUtil.getPersistentData(var0, Constants.NamespacedKeys.UNIQUE_ID, PersistentDataType.STRING);
   }

   @Nullable
   public static ItemConfiguration matchItemConfiguration(@NotNull ItemStack var0) {
      String var1 = getItemId(var0);
      return StringUtils.isNotBlank(var1) ? (ItemConfiguration)Registries.getRegistry(ItemConfiguration.class).get(var1) : null;
   }

   public static ItemConfiguration load(@NotNull ConfigurationSection var0) {
      Material var1 = (Material)ConfigurationUtil.loadEnum(Material.class, var0, "material");
      if (var1 == null) {
         throw new InvalidConfigurationException("invalid material: " + var0.getString("material"));
      } else {
         String var2 = var0.getString("display-name");
         List var3 = var0.getStringList("description");
         String var4 = var0.getString("head-texture");
         Object var5 = var0.get("custom-model-data");
         Integer var6 = var5 instanceof Number ? ((Number)var5).intValue() : null;
         ConfigurationSection var7 = var0.getConfigurationSection("action");
         ItemConfiguration.Action var8 = var7 != null ? ItemConfiguration.Action.load(var7) : null;
         return new ItemConfiguration(IDeyed.loadId(var0), var1, var2, var3, var6, var4, var8);
      }
   }

   @NotNull
   public String getId() {
      return this.id;
   }

   @NotNull
   public ItemStack getItemStack() {
      return this.action instanceof ItemConfiguration.FuelAction ? buildFuelItemStack(this, ((ItemConfiguration.FuelAction)this.action).fuelAmount) : buildItemStack(this);
   }

   @NotNull
   public String getDisplayName() {
      return this.displayName == null ? StringUtils.capitalize(this.id.replace("-", " ").replace("_", " ")) : this.displayName;
   }

   public void write(@NotNull ConfigurationSection var1) {
      IDeyed.writeId((IDeyed)this, var1);
      ConfigurationUtil.writeEnum(this.material, var1, "material");
      var1.set("display-name", this.displayName);
      var1.set("description", this.description != null && this.description.size() > 0 ? this.description : null);
      var1.set("custom-model-data", this.customModelData);
      var1.set("head-texture", this.headTexture);
      if (this.action != null) {
         this.action.write(var1.createSection("action"));
      }

   }

   public static ItemConfiguration.ItemConfigurationBuilder builder() {
      return new ItemConfiguration.ItemConfigurationBuilder();
   }

   @NotNull
   public Material getMaterial() {
      return this.material;
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

   @Nullable
   public ItemConfiguration.Action getAction() {
      return this.action;
   }

   public ItemConfiguration(@NotNull String var1, @NotNull Material var2, @Nullable String var3, @Nullable List<String> var4, @Nullable Integer var5, @Nullable String var6, @Nullable ItemConfiguration.Action var7) {
      this.id = var1;
      this.material = var2;
      this.displayName = var3;
      this.description = var4;
      this.customModelData = var5;
      this.headTexture = var6;
      this.action = var7;
   }

   public abstract static class Action implements ConfigurationSectionWritable {
      public static ItemConfiguration.Action load(@NotNull ConfigurationSection var0) {
         EnumItemAction var1 = (EnumItemAction)ConfigurationUtil.loadEnum(EnumItemAction.class, var0, "type", true);
         if (var1 != null) {
            Object var10000;
            switch(var1) {
            case SPAWN:
               var10000 = new ItemConfiguration.SpawnAction(var0);
               break;
            case FUEL:
               var10000 = new ItemConfiguration.FuelAction(var0);
               break;
            case KEY:
               var10000 = new ItemConfiguration.KeyAction(var0);
               break;
            default:
               throw new IncompatibleClassChangeError();
            }

            return (ItemConfiguration.Action)var10000;
         } else {
            return null;
         }
      }

      @NotNull
      abstract EnumItemAction getType();

      public void write(@NotNull ConfigurationSection var1) {
         ConfigurationUtil.writeEnum(this.getType(), var1, "type");
      }
   }

   public static class FuelAction extends ItemConfiguration.Action {
      private final float fuelAmount;

      public FuelAction(@NotNull ConfigurationSection var1) {
         this.fuelAmount = (float)var1.getDouble("fuel-amount");
      }

      @NotNull
      EnumItemAction getType() {
         return EnumItemAction.FUEL;
      }

      public void write(@NotNull ConfigurationSection var1) {
         super.write(var1);
         var1.set("fuel-amount", this.fuelAmount);
      }

      public float getFuelAmount() {
         return this.fuelAmount;
      }

      public FuelAction(float var1) {
         this.fuelAmount = var1;
      }
   }

   public static class ItemConfigurationBuilder {
      private String id;
      private Material material;
      private String displayName;
      private List<String> description;
      private Integer customModelData;
      private String headTexture;
      private ItemConfiguration.Action action;

      ItemConfigurationBuilder() {
      }

      public ItemConfiguration.ItemConfigurationBuilder id(@NotNull String var1) {
         this.id = var1;
         return this;
      }

      public ItemConfiguration.ItemConfigurationBuilder material(@NotNull Material var1) {
         this.material = var1;
         return this;
      }

      public ItemConfiguration.ItemConfigurationBuilder displayName(@Nullable String var1) {
         this.displayName = var1;
         return this;
      }

      public ItemConfiguration.ItemConfigurationBuilder description(@Nullable List<String> var1) {
         this.description = var1;
         return this;
      }

      public ItemConfiguration.ItemConfigurationBuilder customModelData(@Nullable Integer var1) {
         this.customModelData = var1;
         return this;
      }

      public ItemConfiguration.ItemConfigurationBuilder headTexture(@Nullable String var1) {
         this.headTexture = var1;
         return this;
      }

      public ItemConfiguration.ItemConfigurationBuilder action(@Nullable ItemConfiguration.Action var1) {
         this.action = var1;
         return this;
      }

      public ItemConfiguration build() {
         return new ItemConfiguration(this.id, this.material, this.displayName, this.description, this.customModelData, this.headTexture, this.action);
      }

      public String toString() {
         String var10000 = this.id;
         return "ItemConfiguration.ItemConfigurationBuilder(id=" + var10000 + ", material=" + String.valueOf(this.material) + ", displayName=" + this.displayName + ", description=" + String.valueOf(this.description) + ", customModelData=" + this.customModelData + ", headTexture=" + this.headTexture + ", action=" + String.valueOf(this.action) + ")";
      }
   }

   public static class KeyAction extends ItemConfiguration.Action {
      public KeyAction(@NotNull ConfigurationSection var1) {
      }

      @NotNull
      EnumItemAction getType() {
         return EnumItemAction.KEY;
      }

      public void write(@NotNull ConfigurationSection var1) {
         super.write(var1);
      }

      public KeyAction() {
      }
   }

   public static class SpawnAction extends ItemConfiguration.Action {
      @NotNull
      private final String vehicleId;

      public SpawnAction(@NotNull ConfigurationSection var1) {
         this.vehicleId = var1.getString("vehicle-id", "");
      }

      @NotNull
      EnumItemAction getType() {
         return EnumItemAction.SPAWN;
      }

      public void write(@NotNull ConfigurationSection var1) {
         super.write(var1);
         var1.set("vehicle-id", this.vehicleId);
      }

      @NotNull
      public String getVehicleId() {
         return this.vehicleId;
      }

      public SpawnAction(@NotNull String var1) {
         this.vehicleId = var1;
      }
   }
}
