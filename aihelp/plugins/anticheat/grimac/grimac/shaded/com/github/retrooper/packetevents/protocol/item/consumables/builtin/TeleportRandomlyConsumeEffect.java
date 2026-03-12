package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.consumables.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.consumables.ConsumeEffect;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.consumables.ConsumeEffectTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class TeleportRandomlyConsumeEffect extends ConsumeEffect<TeleportRandomlyConsumeEffect> {
   private final float diameter;

   public TeleportRandomlyConsumeEffect(float diameter) {
      super(ConsumeEffectTypes.TELEPORT_RANDOMLY);
      this.diameter = diameter;
   }

   public static TeleportRandomlyConsumeEffect read(PacketWrapper<?> wrapper) {
      float diameter = wrapper.readFloat();
      return new TeleportRandomlyConsumeEffect(diameter);
   }

   public static void write(PacketWrapper<?> wrapper, TeleportRandomlyConsumeEffect effect) {
      wrapper.writeFloat(effect.diameter);
   }

   public float getDiameter() {
      return this.diameter;
   }
}
