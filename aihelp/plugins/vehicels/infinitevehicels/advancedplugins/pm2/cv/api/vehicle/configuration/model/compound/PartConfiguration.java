package advancedplugins.pm2.cv.api.vehicle.configuration.model.compound;

import advancedplugins.pm2.cv.api.interfaces.ConfigurationSectionWritable;
import advancedplugins.pm2.cv.api.interfaces.IDeyed;
import advancedplugins.pm2.cv.api.interfaces.Identifiable;
import advancedplugins.pm2.cv.api.util.ConfigurationUtil;
import advancedplugins.pm2.cv.api.util.MathUtil;
import advancedplugins.pm2.cv.api.util.inventory.ItemStackUtil;
import advancedplugins.pm2.cv.api.vehicle.configuration.data.BannerStyle;
import advancedplugins.pm2.cv.api.vehicle.configuration.data.DataParser;
import advancedplugins.pm2.cv.api.vehicle.configuration.data.DataParsers;
import advancedplugins.pm2.cv.api.vehicle.configuration.data.HeadTexture;
import java.util.UUID;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import me.PM2.infinitevehicles.xseries.XMaterial;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PartConfiguration implements Identifiable, IDeyed, ConfigurationSectionWritable {
   @NotNull
   private final UUID identifier;
   @NotNull
   private final String id;
   @NotNull
   private final Material material;
   @Nullable
   private final Integer customModelData;
   @Nullable
   private final Color color;
   @Nullable
   private final Object data;
   @Nullable
   private final Vector3D offset;
   @Nullable
   private final Vector3D rotation;
   @Nullable
   private final Vector3D scale;
   @Nullable
   private final ItemStack item;

   public static PartConfiguration load(@NotNull ConfigurationSection var0) {
      Material var1 = loadMaterial(var0);
      Integer var2 = loadCustomModelData(var0);
      Object var3 = parseData(var0);
      Vector3D var4 = (Vector3D)safeLoadLibraryObject(Vector3D.class, var0, "offset");
      Vector3D var5 = (Vector3D)safeLoadLibraryObject(Vector3D.class, var0, "rotation");
      Vector3D var6 = (Vector3D)safeLoadLibraryObject(Vector3D.class, var0, "scale");
      return new PartConfiguration(Identifiable.loadIdentifierOrGenerate(var0), IDeyed.loadId(var0), var1, var2, (Color)null, var3, var4, var5 != null ? MathUtil.toRadians(var5) : null, var6, (ItemStack)null);
   }

   private static Material loadMaterial(@NotNull ConfigurationSection var0) {
      Material var1 = (Material)ConfigurationUtil.loadEnum(Material.class, var0, "material");
      XMaterial var2 = (XMaterial)XMaterial.matchXMaterial(var0.contains("material") ? var0.getString("material").trim().toUpperCase() : "unknown").orElse((Object)null);
      if (var1 == null && var2 != null && var2.get() != null) {
         var1 = var2.get();
      }

      if (var1 == null) {
         throw new InvalidConfigurationException("Invalid material: " + var0.getString("material"));
      } else {
         return var1;
      }
   }

   @Nullable
   private static Integer loadCustomModelData(@NotNull ConfigurationSection var0) {
      Object var1 = var0.get("custom-model-data");
      return var1 instanceof Number ? ((Number)var1).intValue() : null;
   }

   @Nullable
   private static Object parseData(@NotNull ConfigurationSection var0) {
      ConfigurationSection var1 = var0.getConfigurationSection("data");
      if (var1 == null) {
         return null;
      } else {
         DataParser var2 = DataParsers.matchParser(var1);
         if (var2 != null) {
            try {
               return var2.parse(var1);
            } catch (Exception var4) {
               throw new InvalidConfigurationException("Error parsing data: " + var4.getMessage(), var4);
            }
         } else {
            return null;
         }
      }
   }

   private static <T> T safeLoadLibraryObject(Class<T> var0, ConfigurationSection var1, String var2) {
      try {
         return ConfigurationUtil.loadLibraryObject(var0, var1, var2);
      } catch (Exception var4) {
         return null;
      }
   }

   @NotNull
   public ItemStack getItemStack() {
      return this.item == null ? this.buildItemStack() : this.item;
   }

   @NotNull
   private ItemStack buildItemStack() {
      ItemStack var1 = ItemStackUtil.buildCustomItem(this.material, this.customModelData);
      this.applyData(var1);
      return var1;
   }

   private void applyData(@NotNull ItemStack var1) {
      try {
         if (ItemStackUtil.isHead(this.material) && this.data instanceof HeadTexture) {
            ((HeadTexture)this.data).applyTexture(var1);
         } else if (ItemStackUtil.isBanner(this.material) && this.data instanceof BannerStyle) {
            ((BannerStyle)this.data).applyStyle(var1);
         }
      } catch (Exception var3) {
      }

   }

   public void write(@NotNull ConfigurationSection var1) {
      Identifiable.writeIdentifier((Identifiable)this, var1);
      IDeyed.writeId((IDeyed)this, var1);
      var1.set("material", this.material.name());
      this.writeCustomModelData(var1);
      this.writeData(var1);
      this.writeVectors(var1);
   }

   private void writeCustomModelData(@NotNull ConfigurationSection var1) {
      if (this.customModelData != null) {
         var1.set("custom-model-data", this.customModelData);
      }

   }

   private void writeData(@NotNull ConfigurationSection var1) {
      if (this.data != null) {
         DataParser var2 = DataParsers.getParser(this.data.getClass());
         if (var2 != null) {
            try {
               var2.write(this.data, var1.createSection("data"));
            } catch (Exception var4) {
            }
         }
      }

   }

   private void writeVectors(@NotNull ConfigurationSection var1) {
      this.writeNullableVector(Vector3D.class, this.offset, var1, "offset");
      this.writeNullableVector(Vector3D.class, this.rotation != null ? MathUtil.toDegrees(this.rotation) : null, var1, "rotation");
      this.writeNullableVector(Vector3D.class, this.scale, var1, "scale");
   }

   private <T> void writeNullableVector(Class<T> var1, @Nullable T var2, @NotNull ConfigurationSection var3, @NotNull String var4) {
      if (var2 != null) {
         try {
            ConfigurationUtil.writeLibraryObject(var1, var2, var3.createSection(var4));
         } catch (Exception var6) {
         }
      }

   }

   public static PartConfiguration.PartConfigurationBuilder builder() {
      return new PartConfiguration.PartConfigurationBuilder();
   }

   @NotNull
   public UUID getIdentifier() {
      return this.identifier;
   }

   @NotNull
   public String getId() {
      return this.id;
   }

   @NotNull
   public Material getMaterial() {
      return this.material;
   }

   @Nullable
   public Integer getCustomModelData() {
      return this.customModelData;
   }

   @Nullable
   public Color getColor() {
      return this.color;
   }

   @Nullable
   public Object getData() {
      return this.data;
   }

   @Nullable
   public Vector3D getOffset() {
      return this.offset;
   }

   @Nullable
   public Vector3D getRotation() {
      return this.rotation;
   }

   @Nullable
   public Vector3D getScale() {
      return this.scale;
   }

   @Nullable
   public ItemStack getItem() {
      return this.item;
   }

   public PartConfiguration(@NotNull UUID var1, @NotNull String var2, @NotNull Material var3, @Nullable Integer var4, @Nullable Color var5, @Nullable Object var6, @Nullable Vector3D var7, @Nullable Vector3D var8, @Nullable Vector3D var9, @Nullable ItemStack var10) {
      this.identifier = var1;
      this.id = var2;
      this.material = var3;
      this.customModelData = var4;
      this.color = var5;
      this.data = var6;
      this.offset = var7;
      this.rotation = var8;
      this.scale = var9;
      this.item = var10;
   }

   public static class PartConfigurationBuilder {
      private UUID identifier;
      private String id;
      private Material material;
      private Integer customModelData;
      private Color color;
      private Object data;
      private Vector3D offset;
      private Vector3D rotation;
      private Vector3D scale;
      private ItemStack item;

      PartConfigurationBuilder() {
      }

      public PartConfiguration.PartConfigurationBuilder identifier(@NotNull UUID var1) {
         this.identifier = var1;
         return this;
      }

      public PartConfiguration.PartConfigurationBuilder id(@NotNull String var1) {
         this.id = var1;
         return this;
      }

      public PartConfiguration.PartConfigurationBuilder material(@NotNull Material var1) {
         this.material = var1;
         return this;
      }

      public PartConfiguration.PartConfigurationBuilder customModelData(@Nullable Integer var1) {
         this.customModelData = var1;
         return this;
      }

      public PartConfiguration.PartConfigurationBuilder color(@Nullable Color var1) {
         this.color = var1;
         return this;
      }

      public PartConfiguration.PartConfigurationBuilder data(@Nullable Object var1) {
         this.data = var1;
         return this;
      }

      public PartConfiguration.PartConfigurationBuilder offset(@Nullable Vector3D var1) {
         this.offset = var1;
         return this;
      }

      public PartConfiguration.PartConfigurationBuilder rotation(@Nullable Vector3D var1) {
         this.rotation = var1;
         return this;
      }

      public PartConfiguration.PartConfigurationBuilder scale(@Nullable Vector3D var1) {
         this.scale = var1;
         return this;
      }

      public PartConfiguration.PartConfigurationBuilder item(@Nullable ItemStack var1) {
         this.item = var1;
         return this;
      }

      public PartConfiguration build() {
         return new PartConfiguration(this.identifier, this.id, this.material, this.customModelData, this.color, this.data, this.offset, this.rotation, this.scale, this.item);
      }

      public String toString() {
         String var10000 = String.valueOf(this.identifier);
         return "PartConfiguration.PartConfigurationBuilder(identifier=" + var10000 + ", id=" + this.id + ", material=" + String.valueOf(this.material) + ", customModelData=" + this.customModelData + ", color=" + String.valueOf(this.color) + ", data=" + String.valueOf(this.data) + ", offset=" + String.valueOf(this.offset) + ", rotation=" + String.valueOf(this.rotation) + ", scale=" + String.valueOf(this.scale) + ", item=" + String.valueOf(this.item) + ")";
      }
   }
}
