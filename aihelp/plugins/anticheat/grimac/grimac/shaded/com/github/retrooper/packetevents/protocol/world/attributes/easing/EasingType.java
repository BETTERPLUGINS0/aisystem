package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.easing;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Either;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface EasingType extends MappedEntity {
   NbtCodec<EasingType> CODEC = NbtCodecs.either(NbtCodecs.forRegistry(EasingTypes.getRegistry()), CubicBezierEasingType.CODEC).apply(Either::unwrap, (type) -> {
      return type instanceof CubicBezierEasingType ? Either.createRight((CubicBezierEasingType)type) : Either.createLeft(type);
   });

   float apply(float x);
}
