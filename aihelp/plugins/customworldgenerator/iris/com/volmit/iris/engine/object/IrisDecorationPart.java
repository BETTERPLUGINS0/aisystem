package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;

@Desc("Represents a location where decorations should go")
public enum IrisDecorationPart {
   @Desc("The default, decorate anywhere")
   NONE,
   @Desc("Targets shore lines (typically for sugar cane)")
   SHORE_LINE,
   @Desc("Target sea surfaces (typically for lilypads)")
   SEA_SURFACE,
   @Desc("Targets the sea floor (entire placement must be bellow sea level)")
   SEA_FLOOR,
   @Desc("Decorates on cave & carving ceilings or underside of overhangs")
   CEILING;

   // $FF: synthetic method
   private static IrisDecorationPart[] $values() {
      return new IrisDecorationPart[]{NONE, SHORE_LINE, SEA_SURFACE, SEA_FLOOR, CEILING};
   }
}
