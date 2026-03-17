package advancedplugins.pm2.cv.api.enums;

public enum EnumSurface {
   SOLID,
   DUSTY,
   SNOWY,
   SLIPPERY,
   WATER,
   LAVA,
   EMPTY,
   UNKNOWN;

   public boolean isLiquid() {
      return this == WATER || this == LAVA;
   }

   // $FF: synthetic method
   private static EnumSurface[] $values() {
      return new EnumSurface[]{SOLID, DUSTY, SNOWY, SLIPPERY, WATER, LAVA, EMPTY, UNKNOWN};
   }
}
