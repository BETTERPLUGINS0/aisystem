package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.attributes.easing;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.easing.CubicBezierControls;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.easing.CubicCurve;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class CubicBezierEasingType extends AbstractMappedEntity implements EasingType {
   public static final NbtCodec<CubicBezierEasingType> CODEC = (new NbtMapCodec<CubicBezierEasingType>() {
      public CubicBezierEasingType decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
         CubicBezierControls controls = (CubicBezierControls)compound.getOrThrow("cubic_bezier", CubicBezierControls.CODEC, wrapper);
         return new CubicBezierEasingType(controls);
      }

      public void encode(NBTCompound compound, PacketWrapper<?> wrapper, CubicBezierEasingType value) throws NbtCodecException {
         compound.set("cubic_bezier", value.controls, CubicBezierControls.CODEC, wrapper);
      }
   }).codec();
   private static final int NEWTON_RAPHSON_ITERATIONS = 4;
   private final CubicBezierControls controls;
   private final CubicCurve curveX;
   private final CubicCurve curveY;

   public CubicBezierEasingType(CubicBezierControls controls) {
      this((TypesBuilderData)null, controls);
   }

   @ApiStatus.Internal
   public CubicBezierEasingType(@Nullable TypesBuilderData data, CubicBezierControls controls) {
      super(data);
      this.controls = controls;
      this.curveX = controls.calcCurveX();
      this.curveY = controls.calcCurveY();
   }

   public float apply(float x) {
      float t = x;

      for(int i = 0; i < 4; ++i) {
         float gradient = this.curveX.calcSampleGradient(t);
         if (gradient < 1.0E-5F) {
            break;
         }

         float error = this.curveX.calcSample(t) - x;
         t -= error / gradient;
      }

      return this.curveY.calcSample(t);
   }
}
