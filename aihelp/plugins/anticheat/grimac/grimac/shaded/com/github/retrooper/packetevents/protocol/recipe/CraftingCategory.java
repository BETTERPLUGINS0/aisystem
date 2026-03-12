package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

@ApiStatus.Obsolete
public enum CraftingCategory {
   BUILDING,
   REDSTONE,
   EQUIPMENT,
   MISC;

   // $FF: synthetic method
   private static CraftingCategory[] $values() {
      return new CraftingCategory[]{BUILDING, REDSTONE, EQUIPMENT, MISC};
   }
}
