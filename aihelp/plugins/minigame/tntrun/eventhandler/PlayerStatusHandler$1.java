package tntrun.eventhandler;

import tntrun.arena.structure.StructureManager;

// $FF: synthetic class
class PlayerStatusHandler$1 {
   // $FF: synthetic field
   static final int[] $SwitchMap$tntrun$arena$structure$StructureManager$DamageEnabled = new int[StructureManager.DamageEnabled.values().length];

   static {
      try {
         $SwitchMap$tntrun$arena$structure$StructureManager$DamageEnabled[StructureManager.DamageEnabled.YES.ordinal()] = 1;
      } catch (NoSuchFieldError var3) {
      }

      try {
         $SwitchMap$tntrun$arena$structure$StructureManager$DamageEnabled[StructureManager.DamageEnabled.ZERO.ordinal()] = 2;
      } catch (NoSuchFieldError var2) {
      }

      try {
         $SwitchMap$tntrun$arena$structure$StructureManager$DamageEnabled[StructureManager.DamageEnabled.NO.ordinal()] = 3;
      } catch (NoSuchFieldError var1) {
      }

   }
}
