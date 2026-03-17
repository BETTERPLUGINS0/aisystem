package advancedplugins.pm2.cv.api.enums;

public enum EnumItemAction {
   SPAWN,
   FUEL,
   KEY;

   // $FF: synthetic method
   private static EnumItemAction[] $values() {
      return new EnumItemAction[]{SPAWN, FUEL, KEY};
   }
}
