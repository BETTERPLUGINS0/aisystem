package fr.xephi.authme.libs.de.mkammerer.argon2;

public final class Argon2Helper {
   private static final long MILLIS_IN_NANOS = 1000000L;
   private static final int WARMUP_RUNS = 10;
   private static final int MIN_ITERATIONS = 1;
   private static final int MIN_MEMORY = 8;
   private static final int MIN_PARALLELISM = 1;

   private Argon2Helper() {
   }

   public static int findIterations(Argon2 argon2, long maxMillisecs, int memory, int parallelism) {
      return findIterations(argon2, maxMillisecs, memory, parallelism, new Argon2Helper.NoopLogger());
   }

   public static int findIterations(Argon2 argon2, long maxMillisecs, int memory, int parallelism, Argon2Helper.IterationLogger logger) {
      char[] password = "password".toCharArray();
      warmup(argon2, password);
      int iterations = 0;

      long took;
      do {
         ++iterations;
         long start = System.nanoTime() / 1000000L;
         argon2.hash(iterations, memory, parallelism, password);
         long end = System.nanoTime() / 1000000L;
         took = end - start;
         logger.log(iterations, took);
      } while(took <= maxMillisecs);

      return iterations - 1;
   }

   private static void warmup(Argon2 argon2, char[] password) {
      for(int i = 0; i < 10; ++i) {
         argon2.hash(1, 8, 1, (char[])password);
      }

   }

   public static class NoopLogger implements Argon2Helper.IterationLogger {
      public void log(int iterations, long millisecs) {
      }
   }

   public interface IterationLogger {
      void log(int var1, long var2);
   }
}
