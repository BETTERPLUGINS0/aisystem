package advancedplugins.pm2.cv.models.v1_20_R1;

import advancedplugins.pm2.cv.models.api.utils.reflections.ReflectionUtils;
import lombok.Generated;
import net.minecraft.server.level.PlayerChunkMap.EntityTracker;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.item.InstrumentItem;
import net.minecraft.world.item.ItemStack;

public enum NMSMethods implements ReflectionUtils.MethodEnum {
   TRACKED_ENTITY_getEffectiveRange(EntityTracker.class, "b", "getEffectiveRange", new Class[0]),
   SERVER_GAME_PACKET_LISTENER_IMPL_checkLimit(PlayerConnection.class, "checkLimit", "checkLimit", new Class[]{Long.TYPE}),
   INSTRUMENT_ITEM_getInstrument(InstrumentItem.class, "d", "getInstrument", new Class[]{ItemStack.class});

   private final Class<?> target;
   private final String obfuscated;
   private final String mapped;
   private final Class<?>[] parameterClasses;

   private NMSMethods(Class<?> target, String obfuscated, String mapped, Class<?>... parameterClasses) {
      this.target = var3;
      this.obfuscated = var4;
      this.mapped = var5;
      this.parameterClasses = var6;
   }

   public Class<?> target() {
      return this.getTarget();
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
   private static NMSMethods[] $values() {
      return new NMSMethods[]{TRACKED_ENTITY_getEffectiveRange, SERVER_GAME_PACKET_LISTENER_IMPL_checkLimit, INSTRUMENT_ITEM_getInstrument};
   }
}
