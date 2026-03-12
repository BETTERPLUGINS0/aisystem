package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.consumables.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.consumables.ConsumeEffect;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.consumables.ConsumeEffectTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class RemoveEffectsConsumeEffect extends ConsumeEffect<RemoveEffectsConsumeEffect> {
   private final MappedEntitySet<PotionType> effects;

   public RemoveEffectsConsumeEffect(MappedEntitySet<PotionType> effects) {
      super(ConsumeEffectTypes.REMOVE_EFFECTS);
      this.effects = effects;
   }

   public static RemoveEffectsConsumeEffect read(PacketWrapper<?> wrapper) {
      MappedEntitySet<PotionType> effects = MappedEntitySet.read(wrapper, PotionTypes::getById);
      return new RemoveEffectsConsumeEffect(effects);
   }

   public static void write(PacketWrapper<?> wrapper, RemoveEffectsConsumeEffect effect) {
      MappedEntitySet.write(wrapper, effect.effects);
   }

   public MappedEntitySet<PotionType> getEffects() {
      return this.effects;
   }
}
