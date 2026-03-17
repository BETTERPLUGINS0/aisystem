package advancedplugins.pm2.cv.api.vehicle.configuration;

import advancedplugins.pm2.cv.api.enums.EnumDamageType;
import advancedplugins.pm2.cv.api.interfaces.ConfigurationSectionWritable;
import advancedplugins.pm2.cv.api.util.ConfigurationUtil;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.BiPredicate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VehicleDamageConfiguration implements ConfigurationSectionWritable {
   public static final VehicleDamageConfiguration EMPTY = new VehicleDamageConfiguration((Collection)null, false);
   @NotNull
   private final Set<VehicleDamageConfiguration.Modifier> modifiers = new LinkedHashSet();
   private boolean passThroughDamage;

   public static VehicleDamageConfiguration load(@NotNull ConfigurationSection var0) {
      LinkedHashSet var1 = new LinkedHashSet();
      Iterator var2 = ConfigurationUtil.getConfigurationSectionsAfter(var0, "modifiers", false).iterator();

      while(var2.hasNext()) {
         ConfigurationSection var3 = (ConfigurationSection)var2.next();
         var1.add(VehicleDamageConfiguration.Modifier.load(var3));
      }

      return new VehicleDamageConfiguration(var1, var0.getBoolean("pass-through-damage", false));
   }

   public VehicleDamageConfiguration(@Nullable Collection<VehicleDamageConfiguration.Modifier> var1, boolean var2) {
      this.passThroughDamage = var2;
      if (var1 != null) {
         this.modifiers.addAll(var1);
      }

   }

   @NotNull
   public Set<VehicleDamageConfiguration.Modifier> getModifiers() {
      return this.modifiers;
   }

   public void write(@NotNull ConfigurationSection var1) {
      ConfigurationUtil.writeConfigurationSectionWritables(var1.createSection("modifiers"), this.modifiers, "modifier-");
   }

   public boolean isPassThroughDamage() {
      return this.passThroughDamage;
   }

   public static class Modifier implements ConfigurationSectionWritable {
      @NotNull
      private final EnumDamageType type;
      private final float value;
      @Nullable
      private final String extraData;
      private final boolean inverted;
      private final VehicleDamageConfiguration.Modifier.CalcType calcType;

      public static VehicleDamageConfiguration.Modifier load(@NotNull ConfigurationSection var0) {
         EnumDamageType var1 = (EnumDamageType)ConfigurationUtil.loadEnum(EnumDamageType.class, var0, "type", true);
         if (var1 == null) {
            throw new InvalidConfigurationException("a valid damage type must be set");
         } else {
            float var2 = (float)var0.getDouble("value");
            if (var2 != 0.0F) {
               var2 /= 100.0F;
            }

            String var3 = var0.getString("extra-data");
            boolean var4 = var0.getBoolean("inverted", false);
            VehicleDamageConfiguration.Modifier.CalcType var5 = (VehicleDamageConfiguration.Modifier.CalcType)ConfigurationUtil.loadEnum(VehicleDamageConfiguration.Modifier.CalcType.class, var0, "calc-type", true);
            if (var5 == null) {
               var5 = VehicleDamageConfiguration.Modifier.CalcType.ADDITIVE;
            }

            return new VehicleDamageConfiguration.Modifier(var1, var2, var3, var4, var5);
         }
      }

      public Modifier(@NotNull EnumDamageType var1, float var2, @Nullable String var3, boolean var4, @NotNull VehicleDamageConfiguration.Modifier.CalcType var5) {
         this.type = var1;
         this.value = var2;
         this.extraData = var3;
         this.inverted = var4;
         this.calcType = var5;
      }

      public void write(@NotNull ConfigurationSection var1) {
         ConfigurationUtil.writeEnum(this.type, var1, "type");
         var1.set("value", this.value * 100.0F);
      }

      @NotNull
      public EnumDamageType getType() {
         return this.type;
      }

      public BiPredicate<Vehicle, String> checkExtras() {
         return (var1, var2) -> {
            if (this.getType() != EnumDamageType.WEAPONS_MECHANICS) {
               return true;
            } else if (this.getExtraData() == null) {
               return true;
            } else {
               return var2 == null ? false : this.getExtraData().equals(var2);
            }
         };
      }

      @Nullable
      public String getExtraData() {
         return this.extraData;
      }

      public double getValue() {
         return (double)this.value;
      }

      public boolean isInverted() {
         return this.inverted;
      }

      public VehicleDamageConfiguration.Modifier.CalcType getCalcType() {
         return this.calcType;
      }

      public static enum CalcType {
         ADDITIVE,
         MULTIPLICATIVE,
         DIVISIVE;

         // $FF: synthetic method
         private static VehicleDamageConfiguration.Modifier.CalcType[] $values() {
            return new VehicleDamageConfiguration.Modifier.CalcType[]{ADDITIVE, MULTIPLICATIVE, DIVISIVE};
         }
      }
   }
}
