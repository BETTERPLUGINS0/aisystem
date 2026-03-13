package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;

@Desc("Use 3D Interpolation on scaled objects if they are larger than the origin.")
public enum IrisObjectPlacementScaleInterpolator {
   @Desc("Don't interpolate, big cubes")
   NONE,
   @Desc("Uses linear interpolation in 3 dimensions, generally pretty good, but slow")
   TRILINEAR,
   @Desc("Uses cubic spline interpolation in 3 dimensions, even better, but extreme slowdowns")
   TRICUBIC,
   @Desc("Uses hermite spline interpolation in 3 dimensions, even better, but extreme slowdowns")
   TRIHERMITE;

   // $FF: synthetic method
   private static IrisObjectPlacementScaleInterpolator[] $values() {
      return new IrisObjectPlacementScaleInterpolator[]{NONE, TRILINEAR, TRICUBIC, TRIHERMITE};
   }
}
