package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.easing;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import java.util.Arrays;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class CubicBezierControls {
   public static final NbtCodec<CubicBezierControls> CODEC;
   private final float x1;
   private final float y1;
   private final float x2;
   private final float y2;

   public CubicBezierControls(float x1, float y1, float x2, float y2) {
      this.x1 = x1;
      this.y1 = y1;
      this.x2 = x2;
      this.y2 = y2;
   }

   public boolean isValid() {
      return this.x1 >= 0.0F && this.x1 <= 1.0F && this.x2 >= 0.0F && this.x2 <= 1.0F;
   }

   public CubicCurve calcCurveX() {
      return new CubicCurve(3.0F * this.x1 - 3.0F * this.x2 + 1.0F, -6.0F * this.x1 + 3.0F * this.x2, 3.0F * this.x1);
   }

   public CubicCurve calcCurveY() {
      return new CubicCurve(3.0F * this.y1 - 3.0F * this.y2 + 1.0F, -6.0F * this.y1 + 3.0F * this.y2, 3.0F * this.y1);
   }

   public float getX1() {
      return this.x1;
   }

   public float getY1() {
      return this.y1;
   }

   public float getX2() {
      return this.x2;
   }

   public float getY2() {
      return this.y2;
   }

   static {
      CODEC = NbtCodecs.FLOAT.applyList().validate((floats) -> {
         return floats.size() == 4;
      }).apply((l) -> {
         return new CubicBezierControls((Float)l.get(0), (Float)l.get(1), (Float)l.get(2), (Float)l.get(3));
      }, (v) -> {
         return Arrays.asList(v.x1, v.y1, v.x2, v.y2);
      }).validate(CubicBezierControls::isValid);
   }
}
