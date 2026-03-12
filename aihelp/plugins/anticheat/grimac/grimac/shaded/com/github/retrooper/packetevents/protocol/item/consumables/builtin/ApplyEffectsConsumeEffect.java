package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.consumables.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.consumables.ConsumeEffect;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.consumables.ConsumeEffectTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionEffect;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.List;

public class ApplyEffectsConsumeEffect extends ConsumeEffect<ApplyEffectsConsumeEffect> {
   private final List<PotionEffect> effects;
   private final float probability;

   public ApplyEffectsConsumeEffect(List<PotionEffect> effects, float probability) {
      super(ConsumeEffectTypes.APPLY_EFFECTS);
      this.effects = effects;
      this.probability = probability;
   }

   public static ApplyEffectsConsumeEffect read(PacketWrapper<?> wrapper) {
      List<PotionEffect> effects = wrapper.readList(PotionEffect::read);
      float probability = wrapper.readFloat();
      return new ApplyEffectsConsumeEffect(effects, probability);
   }

   public static void write(PacketWrapper<?> wrapper, ApplyEffectsConsumeEffect effect) {
      wrapper.writeList(effect.effects, PotionEffect::write);
      wrapper.writeFloat(effect.probability);
   }

   public List<PotionEffect> getEffects() {
      return this.effects;
   }

   public float getProbability() {
      return this.probability;
   }
}
