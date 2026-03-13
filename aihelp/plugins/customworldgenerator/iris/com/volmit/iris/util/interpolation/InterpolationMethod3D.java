package com.volmit.iris.util.interpolation;

import com.volmit.iris.engine.object.annotations.Desc;

@Desc("An interpolation method (or function) is simply a method of smoothing a position based on surrounding points on a grid. Bicubic for example is smoother, but has 4 times the checks than Bilinear for example. Try using BILINEAR_STARCAST_9 for beautiful results.")
public enum InterpolationMethod3D {
   TRILINEAR,
   TRICUBIC,
   TRIHERMITE,
   TRISTARCAST_3,
   TRISTARCAST_6,
   TRISTARCAST_9,
   TRISTARCAST_12,
   TRILINEAR_TRISTARCAST_3,
   TRILINEAR_TRISTARCAST_6,
   TRILINEAR_TRISTARCAST_9,
   TRILINEAR_TRISTARCAST_12,
   NONE;

   // $FF: synthetic method
   private static InterpolationMethod3D[] $values() {
      return new InterpolationMethod3D[]{TRILINEAR, TRICUBIC, TRIHERMITE, TRISTARCAST_3, TRISTARCAST_6, TRISTARCAST_9, TRISTARCAST_12, TRILINEAR_TRISTARCAST_3, TRILINEAR_TRISTARCAST_6, TRILINEAR_TRISTARCAST_9, TRILINEAR_TRISTARCAST_12, NONE};
   }
}
