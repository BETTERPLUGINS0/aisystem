package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;

@Desc("Determines a derived channel of an image to read")
public enum IrisImageChannel {
   @Desc("The red channel of the image")
   RED,
   @Desc("Thge green channel of the image")
   GREEN,
   @Desc("The blue channel of the image")
   BLUE,
   @Desc("The saturation as a channel of the image")
   SATURATION,
   @Desc("The hue as a channel of the image")
   HUE,
   @Desc("The brightness as a channel of the image")
   BRIGHTNESS,
   @Desc("The composite of RGB as a channel of the image. Takes the average channel value (adding)")
   COMPOSITE_ADD_RGB,
   @Desc("The composite of RGB as a channel of the image. Multiplies the channels")
   COMPOSITE_MUL_RGB,
   @Desc("The composite of RGB as a channel of the image. Picks the highest channel")
   COMPOSITE_MAX_RGB,
   @Desc("The composite of HSB as a channel of the image Takes the average channel value (adding)")
   COMPOSITE_ADD_HSB,
   @Desc("The composite of HSB as a channel of the image Multiplies the channels")
   COMPOSITE_MUL_HSB,
   @Desc("The composite of HSB as a channel of the image Picks the highest channel")
   COMPOSITE_MAX_HSB,
   @Desc("The raw value as a channel (probably doesnt look very good)")
   RAW;

   // $FF: synthetic method
   private static IrisImageChannel[] $values() {
      return new IrisImageChannel[]{RED, GREEN, BLUE, SATURATION, HUE, BRIGHTNESS, COMPOSITE_ADD_RGB, COMPOSITE_MUL_RGB, COMPOSITE_MAX_RGB, COMPOSITE_ADD_HSB, COMPOSITE_MUL_HSB, COMPOSITE_MAX_HSB, RAW};
   }
}
