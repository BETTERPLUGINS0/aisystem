package com.volmit.iris.util.interpolation;

public enum InterpolationType {
   LINEAR,
   PARAMETRIC_2,
   PARAMETRIC_4,
   BEZIER,
   NONE;

   // $FF: synthetic method
   private static InterpolationType[] $values() {
      return new InterpolationType[]{LINEAR, PARAMETRIC_2, PARAMETRIC_4, BEZIER, NONE};
   }
}
