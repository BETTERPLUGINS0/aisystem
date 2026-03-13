package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;

@Desc("Represents a basic font style to apply to a font family")
public enum IrisFontStyle {
   @Desc("Plain old text")
   PLAIN,
   @Desc("Italicized Text")
   ITALIC,
   @Desc("Bold Text")
   BOLD;

   // $FF: synthetic method
   private static IrisFontStyle[] $values() {
      return new IrisFontStyle[]{PLAIN, ITALIC, BOLD};
   }
}
