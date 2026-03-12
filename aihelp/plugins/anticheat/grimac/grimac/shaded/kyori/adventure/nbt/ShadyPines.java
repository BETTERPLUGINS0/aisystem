package ac.grim.grimac.shaded.kyori.adventure.nbt;

final class ShadyPines {
   private ShadyPines() {
   }

   static int floor(final double dv) {
      int iv = (int)dv;
      return dv < (double)iv ? iv - 1 : iv;
   }

   static int floor(final float fv) {
      int iv = (int)fv;
      return fv < (float)iv ? iv - 1 : iv;
   }
}
