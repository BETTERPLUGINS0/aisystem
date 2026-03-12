package fr.xephi.authme.output;

public enum LogLevel {
   INFO(3),
   FINE(2),
   DEBUG(1);

   private int value;

   private LogLevel(int param3) {
      this.value = value;
   }

   public boolean includes(LogLevel level) {
      return this.value <= level.value;
   }

   // $FF: synthetic method
   private static LogLevel[] $values() {
      return new LogLevel[]{INFO, FINE, DEBUG};
   }
}
