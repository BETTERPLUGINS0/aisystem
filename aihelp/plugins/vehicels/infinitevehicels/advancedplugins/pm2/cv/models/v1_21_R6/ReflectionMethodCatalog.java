package advancedplugins.pm2.cv.models.v1_21_R6;

import advancedplugins.pm2.cv.models.api.utils.reflections.ReflectionUtils;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Generated;
import net.minecraft.core.HolderLookup.a;
import net.minecraft.server.level.PlayerChunkMap.EntityTracker;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.ItemStack;

public enum ReflectionMethodCatalog implements ReflectionUtils.MethodEnum {
   CALCULATE_EFFECTIVE_RANGE(EntityTracker.class, "b", "getEffectiveRange", new Class[0]),
   VALIDATE_PACKET_LIMIT(PlayerConnection.class, "checkLimit", "checkLimit", new Class[]{Long.TYPE}),
   RETRIEVE_INSTRUMENT_DATA(InstrumentItem.class, "i", "getInstrument", new Class[]{ItemStack.class, a.class});

   private final Class<?> target;
   private final String obfuscated;
   private final String mapped;
   private final Class<?>[] parameterClasses;

   private ReflectionMethodCatalog(Class<?> param3, String param4, String param5, Class<?>... param6) {
      this.target = var3;
      this.obfuscated = var4;
      this.mapped = var5;
      this.parameterClasses = var6;
   }

   public Class<?> target() {
      return this.target;
   }

   public String getObfuscatedSignature() {
      return this.obfuscated;
   }

   public String getMappedSignature() {
      return this.mapped;
   }

   public Class<?>[] getParameterTypes() {
      return (Class[])Arrays.copyOf(this.parameterClasses, this.parameterClasses.length);
   }

   public int getParameterCount() {
      return this.parameterClasses.length;
   }

   public boolean matchesSignature(String var1, Class<?>... var2) {
      if (!var1.equals(this.mapped) && !var1.equals(this.obfuscated)) {
         return false;
      } else if (var2.length != this.parameterClasses.length) {
         return false;
      } else {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (!this.parameterClasses[var3].equals(var2[var3])) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean belongsToClass(Class<?> var1) {
      return this.target.equals(var1);
   }

   public static List<ReflectionMethodCatalog> getMethodsForClass(Class<?> var0) {
      return (List)Arrays.stream(values()).filter((var1) -> {
         return var1.belongsToClass(var0);
      }).collect(Collectors.toList());
   }

   public static ReflectionMethodCatalog findByMappedName(String var0) {
      return (ReflectionMethodCatalog)Arrays.stream(values()).filter((var1) -> {
         return var1.getMappedSignature().equals(var0);
      }).findFirst().orElse((Object)null);
   }

   @Generated
   public Class<?> getTarget() {
      return this.target;
   }

   @Generated
   public String getObfuscated() {
      return this.obfuscated;
   }

   @Generated
   public String getMapped() {
      return this.mapped;
   }

   @Generated
   public Class<?>[] getParameterClasses() {
      return this.parameterClasses;
   }

   // $FF: synthetic method
   private static ReflectionMethodCatalog[] $values() {
      return new ReflectionMethodCatalog[]{CALCULATE_EFFECTIVE_RANGE, VALIDATE_PACKET_LIMIT, RETRIEVE_INSTRUMENT_DATA};
   }
}
