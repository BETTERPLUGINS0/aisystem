package org.apache.commons.io.file;

import java.math.BigInteger;
import java.util.Objects;

public class Counters {
   public static Counters.Counter bigIntegerCounter() {
      return new Counters.BigIntegerCounter();
   }

   public static Counters.PathCounters bigIntegerPathCounters() {
      return new Counters.BigIntegerPathCounters();
   }

   public static Counters.Counter longCounter() {
      return new Counters.LongCounter();
   }

   public static Counters.PathCounters longPathCounters() {
      return new Counters.LongPathCounters();
   }

   public static Counters.Counter noopCounter() {
      return Counters.NoopCounter.INSTANCE;
   }

   public static Counters.PathCounters noopPathCounters() {
      return Counters.NoopPathCounters.INSTANCE;
   }

   private static final class BigIntegerCounter implements Counters.Counter {
      private BigInteger value;

      private BigIntegerCounter() {
         this.value = BigInteger.ZERO;
      }

      public void add(long var1) {
         this.value = this.value.add(BigInteger.valueOf(var1));
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (!(var1 instanceof Counters.Counter)) {
            return false;
         } else {
            Counters.Counter var2 = (Counters.Counter)var1;
            return Objects.equals(this.value, var2.getBigInteger());
         }
      }

      public long get() {
         return this.value.longValueExact();
      }

      public BigInteger getBigInteger() {
         return this.value;
      }

      public Long getLong() {
         return this.value.longValueExact();
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.value});
      }

      public void increment() {
         this.value = this.value.add(BigInteger.ONE);
      }

      public String toString() {
         return this.value.toString();
      }

      public void reset() {
         this.value = BigInteger.ZERO;
      }

      // $FF: synthetic method
      BigIntegerCounter(Object var1) {
         this();
      }
   }

   private static final class BigIntegerPathCounters extends Counters.AbstractPathCounters {
      protected BigIntegerPathCounters() {
         super(Counters.bigIntegerCounter(), Counters.bigIntegerCounter(), Counters.bigIntegerCounter());
      }
   }

   private static final class LongCounter implements Counters.Counter {
      private long value;

      private LongCounter() {
      }

      public void add(long var1) {
         this.value += var1;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (!(var1 instanceof Counters.Counter)) {
            return false;
         } else {
            Counters.Counter var2 = (Counters.Counter)var1;
            return this.value == var2.get();
         }
      }

      public long get() {
         return this.value;
      }

      public BigInteger getBigInteger() {
         return BigInteger.valueOf(this.value);
      }

      public Long getLong() {
         return this.value;
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.value});
      }

      public void increment() {
         ++this.value;
      }

      public String toString() {
         return Long.toString(this.value);
      }

      public void reset() {
         this.value = 0L;
      }

      // $FF: synthetic method
      LongCounter(Object var1) {
         this();
      }
   }

   private static final class LongPathCounters extends Counters.AbstractPathCounters {
      protected LongPathCounters() {
         super(Counters.longCounter(), Counters.longCounter(), Counters.longCounter());
      }
   }

   private static final class NoopCounter implements Counters.Counter {
      static final Counters.NoopCounter INSTANCE = new Counters.NoopCounter();

      public void add(long var1) {
      }

      public long get() {
         return 0L;
      }

      public BigInteger getBigInteger() {
         return BigInteger.ZERO;
      }

      public Long getLong() {
         return 0L;
      }

      public void increment() {
      }
   }

   private static final class NoopPathCounters extends Counters.AbstractPathCounters {
      static final Counters.NoopPathCounters INSTANCE = new Counters.NoopPathCounters();

      private NoopPathCounters() {
         super(Counters.noopCounter(), Counters.noopCounter(), Counters.noopCounter());
      }
   }

   public interface PathCounters {
      Counters.Counter getByteCounter();

      Counters.Counter getDirectoryCounter();

      Counters.Counter getFileCounter();

      default void reset() {
      }
   }

   public interface Counter {
      void add(long var1);

      long get();

      BigInteger getBigInteger();

      Long getLong();

      void increment();

      default void reset() {
      }
   }

   private static class AbstractPathCounters implements Counters.PathCounters {
      private final Counters.Counter byteCounter;
      private final Counters.Counter directoryCounter;
      private final Counters.Counter fileCounter;

      protected AbstractPathCounters(Counters.Counter var1, Counters.Counter var2, Counters.Counter var3) {
         this.byteCounter = var1;
         this.directoryCounter = var2;
         this.fileCounter = var3;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (!(var1 instanceof Counters.AbstractPathCounters)) {
            return false;
         } else {
            Counters.AbstractPathCounters var2 = (Counters.AbstractPathCounters)var1;
            return Objects.equals(this.byteCounter, var2.byteCounter) && Objects.equals(this.directoryCounter, var2.directoryCounter) && Objects.equals(this.fileCounter, var2.fileCounter);
         }
      }

      public Counters.Counter getByteCounter() {
         return this.byteCounter;
      }

      public Counters.Counter getDirectoryCounter() {
         return this.directoryCounter;
      }

      public Counters.Counter getFileCounter() {
         return this.fileCounter;
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.byteCounter, this.directoryCounter, this.fileCounter});
      }

      public void reset() {
         this.byteCounter.reset();
         this.directoryCounter.reset();
         this.fileCounter.reset();
      }

      public String toString() {
         return String.format("%,d files, %,d directories, %,d bytes", this.fileCounter.get(), this.directoryCounter.get(), this.byteCounter.get());
      }
   }
}
