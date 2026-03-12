package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.easing;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.FloatUnaryOperator;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticEasingType extends AbstractMappedEntity implements EasingType {
   private final FloatUnaryOperator operator;

   @ApiStatus.Internal
   public StaticEasingType(@Nullable TypesBuilderData data, FloatUnaryOperator operator) {
      super(data);
      this.operator = operator;
   }

   public float apply(float x) {
      return this.operator.apply(x);
   }
}
