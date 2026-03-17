package advancedplugins.pm2.cv.api.vehicle.configuration.model.compound;

import advancedplugins.pm2.cv.api.interfaces.ConfigurationSectionWritable;
import advancedplugins.pm2.cv.api.interfaces.IDeyed;
import advancedplugins.pm2.cv.api.interfaces.Identifiable;
import advancedplugins.pm2.cv.api.util.ConfigurationUtil;
import advancedplugins.pm2.cv.api.util.MathUtil;
import java.util.UUID;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BoneConfiguration implements Identifiable, IDeyed, ConfigurationSectionWritable {
   @NotNull
   private final UUID identifier;
   @NotNull
   private final String id;
   @NotNull
   private final Vector3D pivot;
   @Nullable
   private final Vector3D rotation;

   public static BoneConfiguration load(@NotNull ConfigurationSection var0) {
      if (var0.getKeys(false).size() == 1 && var0.isConfigurationSection((String)var0.getKeys(false).iterator().next())) {
         String var1 = (String)var0.getKeys(false).iterator().next();
         var0 = var0.getConfigurationSection(var1);
      }

      if (var0 == null) {
         throw new InvalidConfigurationException("bone section is null");
      } else {
         Vector3D var3 = (Vector3D)ConfigurationUtil.loadLibraryObject(Vector3D.class, var0, "pivot");
         Vector3D var2 = (Vector3D)ConfigurationUtil.loadLibraryObject(Vector3D.class, var0, "rotation");
         if (var3 == null) {
            var3 = new Vector3D(0.0D, 0.0D, 0.0D);
         }

         return new BoneConfiguration(Identifiable.loadIdentifierOrGenerate(var0), IDeyed.loadId(var0, var0.getName()), var3, var2 != null ? MathUtil.toRadians(var2) : null);
      }
   }

   public void write(@NotNull ConfigurationSection var1) {
      Identifiable.writeIdentifier((Identifiable)this, var1);
      IDeyed.writeId((IDeyed)this, var1);
      ConfigurationUtil.writeLibraryObject(Vector3D.class, this.pivot, var1.createSection("pivot"));
      if (this.rotation != null) {
         ConfigurationUtil.writeLibraryObject(Vector3D.class, MathUtil.toDegrees(this.rotation), var1.createSection("rotation"));
      }

   }

   public static BoneConfiguration.BoneConfigurationBuilder builder() {
      return new BoneConfiguration.BoneConfigurationBuilder();
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
   public Vector3D getPivot() {
      return this.pivot;
   }

   @Nullable
   public Vector3D getRotation() {
      return this.rotation;
   }

   public BoneConfiguration(@NotNull UUID var1, @NotNull String var2, @NotNull Vector3D var3, @Nullable Vector3D var4) {
      this.identifier = var1;
      this.id = var2;
      this.pivot = var3;
      this.rotation = var4;
   }

   public String toString() {
      String var10000 = String.valueOf(this.getIdentifier());
      return "BoneConfiguration(identifier=" + var10000 + ", id=" + this.getId() + ", pivot=" + String.valueOf(this.getPivot()) + ", rotation=" + String.valueOf(this.getRotation()) + ")";
   }

   public static class BoneConfigurationBuilder {
      private UUID identifier;
      private String id;
      private Vector3D pivot;
      private Vector3D rotation;

      BoneConfigurationBuilder() {
      }

      public BoneConfiguration.BoneConfigurationBuilder identifier(@NotNull UUID var1) {
         this.identifier = var1;
         return this;
      }

      public BoneConfiguration.BoneConfigurationBuilder id(@NotNull String var1) {
         this.id = var1;
         return this;
      }

      public BoneConfiguration.BoneConfigurationBuilder pivot(@NotNull Vector3D var1) {
         this.pivot = var1;
         return this;
      }

      public BoneConfiguration.BoneConfigurationBuilder rotation(@Nullable Vector3D var1) {
         this.rotation = var1;
         return this;
      }

      public BoneConfiguration build() {
         return new BoneConfiguration(this.identifier, this.id, this.pivot, this.rotation);
      }

      public String toString() {
         String var10000 = String.valueOf(this.identifier);
         return "BoneConfiguration.BoneConfigurationBuilder(identifier=" + var10000 + ", id=" + this.id + ", pivot=" + String.valueOf(this.pivot) + ", rotation=" + String.valueOf(this.rotation) + ")";
      }
   }
}
