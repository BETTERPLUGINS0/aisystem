package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;

@Desc("Defines if an object is allowed to place in carvings, surfaces or both.")
public enum CarvingMode {
   @Desc("Only place this object on surfaces (NOT under carvings)")
   SURFACE_ONLY,
   @Desc("Only place this object under carvings (NOT on the surface)")
   CARVING_ONLY,
   @Desc("This object can place anywhere")
   ANYWHERE;

   public boolean supportsCarving() {
      return this.equals(ANYWHERE) || this.equals(CARVING_ONLY);
   }

   public boolean supportsSurface() {
      return this.equals(ANYWHERE) || this.equals(SURFACE_ONLY);
   }

   // $FF: synthetic method
   private static CarvingMode[] $values() {
      return new CarvingMode[]{SURFACE_ONLY, CARVING_ONLY, ANYWHERE};
   }
}
