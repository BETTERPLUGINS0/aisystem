package org.avarion.yaml;

public enum Leniency {
   LENIENT,
   STRICT,
   UNDEFINED;

   // $FF: synthetic method
   private static Leniency[] $values() {
      return new Leniency[]{LENIENT, STRICT, UNDEFINED};
   }
}
