package fr.xephi.authme.libs.org.postgresql.replication;

import java.nio.ByteBuffer;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class LogSequenceNumber implements Comparable<LogSequenceNumber> {
   public static final LogSequenceNumber INVALID_LSN = valueOf(0L);
   private final long value;

   private LogSequenceNumber(long value) {
      this.value = value;
   }

   public static LogSequenceNumber valueOf(long value) {
      return new LogSequenceNumber(value);
   }

   public static LogSequenceNumber valueOf(String strValue) {
      int slashIndex = strValue.lastIndexOf(47);
      if (slashIndex <= 0) {
         return INVALID_LSN;
      } else {
         String logicalXLogStr = strValue.substring(0, slashIndex);
         int logicalXlog = (int)Long.parseLong(logicalXLogStr, 16);
         String segmentStr = strValue.substring(slashIndex + 1, strValue.length());
         int segment = (int)Long.parseLong(segmentStr, 16);
         ByteBuffer buf = ByteBuffer.allocate(8);
         buf.putInt(logicalXlog);
         buf.putInt(segment);
         buf.position(0);
         long value = buf.getLong();
         return valueOf(value);
      }
   }

   public long asLong() {
      return this.value;
   }

   public String asString() {
      ByteBuffer buf = ByteBuffer.allocate(8);
      buf.putLong(this.value);
      buf.position(0);
      int logicalXlog = buf.getInt();
      int segment = buf.getInt();
      return String.format("%X/%X", logicalXlog, segment);
   }

   public boolean equals(@Nullable Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         LogSequenceNumber that = (LogSequenceNumber)o;
         return this.value == that.value;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return (int)(this.value ^ this.value >>> 32);
   }

   public String toString() {
      return "LSN{" + this.asString() + '}';
   }

   public int compareTo(LogSequenceNumber o) {
      if (this.value == o.value) {
         return 0;
      } else {
         return this.value + Long.MIN_VALUE < o.value + Long.MIN_VALUE ? -1 : 1;
      }
   }
}
