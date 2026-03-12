package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.consumables.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.consumables.ConsumeEffect;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.consumables.ConsumeEffectTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class ClearAllEffectsConsumeEffect extends ConsumeEffect<ClearAllEffectsConsumeEffect> {
   public static final ClearAllEffectsConsumeEffect INSTANCE = new ClearAllEffectsConsumeEffect();

   private ClearAllEffectsConsumeEffect() {
      super(ConsumeEffectTypes.CLEAR_ALL_EFFECTS);
   }

   public static ClearAllEffectsConsumeEffect read(PacketWrapper<?> ignoredWrapper) {
      return INSTANCE;
   }

   public static void write(PacketWrapper<?> ignoredWrapper, ClearAllEffectsConsumeEffect ignoredEffect) {
   }
}
