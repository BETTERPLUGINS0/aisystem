package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.timelines;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.easing.EasingType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.easing.EasingTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.List;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class KeyframeTrack<T> {
   private final List<Keyframe<T>> keyframes;
   private final EasingType easingType;

   public KeyframeTrack(List<Keyframe<T>> keyframes, EasingType easingType) {
      this.keyframes = keyframes;
      this.easingType = easingType;
   }

   public static <T> NbtMapCodec<KeyframeTrack<T>> mapCodec(NbtCodec<T> valueCodec) {
      final NbtCodec<List<Keyframe<T>>> keyframesCodec = Keyframe.codec(valueCodec).applyList();
      return new NbtMapCodec<KeyframeTrack<T>>() {
         public KeyframeTrack<T> decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
            List<Keyframe<T>> keyframes = (List)compound.getOrThrow("keyframes", keyframesCodec, wrapper);
            EasingType easingType = (EasingType)compound.getOr("ease", EasingType.CODEC, EasingTypes.LINEAR, wrapper);
            return new KeyframeTrack(keyframes, easingType);
         }

         public void encode(NBTCompound compound, PacketWrapper<?> wrapper, KeyframeTrack<T> value) throws NbtCodecException {
            compound.set("keyframes", value.keyframes, keyframesCodec, wrapper);
            if (value.easingType != EasingTypes.LINEAR) {
               compound.set("ease", value.easingType, EasingType.CODEC, wrapper);
            }

         }
      };
   }

   public List<Keyframe<T>> getKeyframes() {
      return this.keyframes;
   }

   public EasingType getEasingType() {
      return this.easingType;
   }
}
