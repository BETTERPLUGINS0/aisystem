package advancedplugins.pm2.cv.api.enums;

public enum EnumInjectorPriority {
   LOWEST,
   LOW,
   NORMAL,
   HIGH,
   HIGHEST,
   MONITOR;

   // $FF: synthetic method
   private static EnumInjectorPriority[] $values() {
      return new EnumInjectorPriority[]{LOWEST, LOW, NORMAL, HIGH, HIGHEST, MONITOR};
   }
}
