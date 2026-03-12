package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

public class PotionEffect {
   private final PotionType type;
   private final PotionEffect.Properties properties;

   public PotionEffect(PotionType type, int amplifier, int duration, boolean ambient, boolean showParticles, boolean showIcon, @Nullable PotionEffect.Properties hiddenEffect) {
      this(type, new PotionEffect.Properties(amplifier, duration, ambient, showParticles, showIcon, hiddenEffect));
   }

   public PotionEffect(PotionType type, PotionEffect.Properties properties) {
      this.type = type;
      this.properties = properties;
   }

   public static PotionEffect read(PacketWrapper<?> wrapper) {
      PotionType type = (PotionType)wrapper.readMappedEntity(PotionTypes::getById);
      PotionEffect.Properties props = PotionEffect.Properties.read(wrapper);
      return new PotionEffect(type, props);
   }

   public static void write(PacketWrapper<?> wrapper, PotionEffect effect) {
      wrapper.writeMappedEntity(effect.type);
      PotionEffect.Properties.write(wrapper, effect.properties);
   }

   public static class Properties {
      private final int amplifier;
      private final int duration;
      private final boolean ambient;
      private final boolean showParticles;
      private final boolean showIcon;
      @Nullable
      private final PotionEffect.Properties hiddenEffect;

      public Properties(int amplifier, int duration, boolean ambient, boolean showParticles, boolean showIcon, @Nullable PotionEffect.Properties hiddenEffect) {
         this.amplifier = amplifier;
         this.duration = duration;
         this.ambient = ambient;
         this.showParticles = showParticles;
         this.showIcon = showIcon;
         this.hiddenEffect = hiddenEffect;
      }

      public static PotionEffect.Properties read(PacketWrapper<?> wrapper) {
         int amplifier = wrapper.readVarInt();
         int duration = wrapper.readVarInt();
         boolean ambient = wrapper.readBoolean();
         boolean showParticles = wrapper.readBoolean();
         boolean showIcon = wrapper.readBoolean();
         PotionEffect.Properties hiddenEffect = (PotionEffect.Properties)wrapper.readOptional(PotionEffect.Properties::read);
         return new PotionEffect.Properties(amplifier, duration, ambient, showParticles, showIcon, hiddenEffect);
      }

      public static void write(PacketWrapper<?> wrapper, PotionEffect.Properties props) {
         wrapper.writeVarInt(props.amplifier);
         wrapper.writeVarInt(props.duration);
         wrapper.writeBoolean(props.ambient);
         wrapper.writeBoolean(props.showParticles);
         wrapper.writeBoolean(props.showIcon);
         wrapper.writeOptional(props.hiddenEffect, PotionEffect.Properties::write);
      }
   }
}
