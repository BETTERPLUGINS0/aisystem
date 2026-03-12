package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.timelines;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttribute;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.modifiers.AttributeModifier;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class TimelineTrack<T, A> {
   private final AttributeModifier<T, A> modifier;
   private final KeyframeTrack<A> argumentTrack;

   public TimelineTrack(AttributeModifier<T, A> modifier, KeyframeTrack<A> argumentTrack) {
      this.modifier = modifier;
      this.argumentTrack = argumentTrack;
   }

   public static <T> NbtCodec<TimelineTrack<T, ?>> codec(EnvironmentAttribute<T> attribute) {
      final NbtCodec<AttributeModifier<T, ?>> modifierCodec = attribute.getType().getModifierCodec();
      return (new NbtMapCodec<TimelineTrack<T, ?>>() {
         public TimelineTrack<T, ?> decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
            AttributeModifier<T, ?> modifier = (AttributeModifier)compound.getOr("modifier", modifierCodec, AttributeModifier.override(), wrapper);
            return (TimelineTrack)TimelineTrack.codec(attribute, modifier).decode(compound, wrapper);
         }

         public void encode(NBTCompound compound, PacketWrapper<?> wrapper, TimelineTrack<T, ?> value) throws NbtCodecException {
            this.encode0(compound, wrapper, value);
         }

         private <A> void encode0(NBTCompound compound, PacketWrapper<?> wrapper, TimelineTrack<T, A> value) throws NbtCodecException {
            if (value.modifier != AttributeModifier.override()) {
               compound.set("modifier", value.modifier, modifierCodec, wrapper);
            }

            TimelineTrack.codec(attribute, value.modifier).encode(compound, wrapper, value);
         }
      }).codec();
   }

   public static <T, A> NbtMapCodec<TimelineTrack<T, A>> codec(EnvironmentAttribute<T> attribute, AttributeModifier<T, A> modifier) {
      return KeyframeTrack.mapCodec(modifier.argumentCodec(attribute)).apply((track) -> {
         return new TimelineTrack(modifier, track);
      }, TimelineTrack::getArgumentTrack);
   }

   public AttributeModifier<T, A> getModifier() {
      return this.modifier;
   }

   public KeyframeTrack<A> getArgumentTrack() {
      return this.argumentTrack;
   }
}
