package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.consumables.builtin;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.consumables.ConsumeEffect;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.consumables.ConsumeEffectTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class PlaySoundConsumeEffect extends ConsumeEffect<PlaySoundConsumeEffect> {
   private final Sound sound;

   public PlaySoundConsumeEffect(Sound sound) {
      super(ConsumeEffectTypes.PLAY_SOUND);
      this.sound = sound;
   }

   public static PlaySoundConsumeEffect read(PacketWrapper<?> wrapper) {
      Sound sound = Sound.read(wrapper);
      return new PlaySoundConsumeEffect(sound);
   }

   public static void write(PacketWrapper<?> wrapper, PlaySoundConsumeEffect effect) {
      Sound.write(wrapper, effect.sound);
   }

   public Sound getSound() {
      return this.sound;
   }
}
