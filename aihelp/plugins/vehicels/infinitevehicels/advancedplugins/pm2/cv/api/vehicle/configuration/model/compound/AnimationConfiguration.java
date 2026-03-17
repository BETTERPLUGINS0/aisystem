package advancedplugins.pm2.cv.api.vehicle.configuration.model.compound;

import advancedplugins.pm2.cv.api.enums.EnumInterpolationMode;
import advancedplugins.pm2.cv.api.enums.EnumLoopMode;
import advancedplugins.pm2.cv.api.interfaces.ConfigurationSectionWritable;
import advancedplugins.pm2.cv.api.interfaces.IDeyed;
import advancedplugins.pm2.cv.api.util.ConfigurationUtil;
import advancedplugins.pm2.cv.api.vehicle.VehicleState;
import gnu.trove.set.hash.THashSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;

public class AnimationConfiguration implements ConfigurationSectionWritable, IDeyed {
   @NotNull
   private final String id;
   @NotNull
   private final EnumInterpolationMode interpolationMode;
   @NotNull
   private final EnumLoopMode loopMode;
   @NotNull
   private final List<AnimationKeyframeConfiguration> keyframes;
   @NotNull
   private final Set<String> statesToApply = new THashSet();

   public static AnimationConfiguration load(@NotNull ConfigurationSection var0) {
      String var1 = IDeyed.loadId(var0);
      EnumInterpolationMode var2 = (EnumInterpolationMode)ConfigurationUtil.loadEnum(EnumInterpolationMode.class, var0, "interpolation-mode");
      EnumLoopMode var3 = (EnumLoopMode)ConfigurationUtil.loadEnum(EnumLoopMode.class, var0, "loop-mode");
      if (var2 == null) {
         throw new InvalidConfigurationException("invalid interpolation mode");
      } else if (var3 == null) {
         throw new InvalidConfigurationException("invalid loop mode");
      } else {
         ConfigurationSection var4 = var0.getConfigurationSection("keyframes");
         ArrayList var5 = new ArrayList();
         String var7;
         if (var4 != null) {
            Iterator var6 = var4.getKeys(false).iterator();

            while(var6.hasNext()) {
               var7 = (String)var6.next();
               ConfigurationSection var8 = var4.getConfigurationSection(var7);
               AnimationKeyframeConfiguration var9 = var8 != null ? AnimationKeyframeConfiguration.load(var8) : null;
               if (var9 != null) {
                  var5.add(var9);
               }
            }
         }

         if (var5.size() == 0) {
            throw new InvalidConfigurationException("at least one keyframe must be set");
         } else {
            THashSet var10 = new THashSet();
            var7 = var0.getString("states-to-apply");
            if (StringUtils.isNotBlank(var7)) {
               String[] var11 = var7.split(",");
               Stream var10000 = Arrays.stream(var11).filter(StringUtils::isNotBlank).map((var0x) -> {
                  return var0x.toLowerCase().trim();
               });
               Objects.requireNonNull(var10);
               var10000.forEach(var10::add);
            }

            return new AnimationConfiguration(var1, var2, var3, var5, var10);
         }
      }
   }

   public AnimationConfiguration(@NotNull String var1, @NotNull EnumInterpolationMode var2, @NotNull EnumLoopMode var3, @NotNull List<AnimationKeyframeConfiguration> var4, @NotNull Collection<String> var5) {
      this.id = var1;
      this.interpolationMode = var2;
      this.loopMode = var3;
      this.keyframes = var4;
      this.statesToApply.addAll(var5);
   }

   public boolean appliesTo(@NotNull VehicleState var1) {
      Iterator var2 = this.statesToApply.iterator();

      String var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (String)var2.next();
      } while(!var1.getName().equalsIgnoreCase(var3));

      return true;
   }

   public void write(@NotNull ConfigurationSection var1) {
      IDeyed.writeId((IDeyed)this, var1);
      ConfigurationUtil.writeEnum(this.interpolationMode, var1, "interpolation-mode");
      ConfigurationUtil.writeEnum(this.loopMode, var1, "loop-mode");
      ConfigurationSection var2 = var1.createSection("keyframes");

      for(int var3 = 0; var3 < this.keyframes.size(); ++var3) {
         ((AnimationKeyframeConfiguration)this.keyframes.get(var3)).write(var2.createSection("keyframe-" + var3));
      }

      if (this.statesToApply.size() > 0) {
         var1.set("states-to-apply", String.join(",", this.statesToApply));
      } else {
         var1.set("states-to-apply", (Object)null);
      }

   }

   @NotNull
   public String getId() {
      return this.id;
   }

   @NotNull
   public EnumInterpolationMode getInterpolationMode() {
      return this.interpolationMode;
   }

   @NotNull
   public EnumLoopMode getLoopMode() {
      return this.loopMode;
   }

   @NotNull
   public List<AnimationKeyframeConfiguration> getKeyframes() {
      return this.keyframes;
   }

   @NotNull
   public Set<String> getStatesToApply() {
      return this.statesToApply;
   }

   public String toString() {
      String var10000 = this.getId();
      return "AnimationConfiguration(id=" + var10000 + ", interpolationMode=" + String.valueOf(this.getInterpolationMode()) + ", loopMode=" + String.valueOf(this.getLoopMode()) + ", keyframes=" + String.valueOf(this.getKeyframes()) + ", statesToApply=" + String.valueOf(this.getStatesToApply()) + ")";
   }
}
