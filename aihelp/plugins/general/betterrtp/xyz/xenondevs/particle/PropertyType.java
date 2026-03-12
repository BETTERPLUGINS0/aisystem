package xyz.xenondevs.particle;

public enum PropertyType {
   DIRECTIONAL,
   COLORABLE,
   REQUIRES_BLOCK,
   REQUIRES_ITEM,
   REQUIRES_WATER,
   RESIZEABLE,
   DUST;

   // $FF: synthetic method
   private static PropertyType[] $values() {
      return new PropertyType[]{DIRECTIONAL, COLORABLE, REQUIRES_BLOCK, REQUIRES_ITEM, REQUIRES_WATER, RESIZEABLE, DUST};
   }
}
