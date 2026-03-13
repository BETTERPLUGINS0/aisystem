package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.function.IOConsumer;
import org.apache.commons.io.function.IOFunction;

public class ThresholdingOutputStream extends OutputStream {
   private static final IOFunction<ThresholdingOutputStream, OutputStream> NOOP_OS_GETTER = (var0) -> {
      return NullOutputStream.NULL_OUTPUT_STREAM;
   };
   private final int threshold;
   private final IOConsumer<ThresholdingOutputStream> thresholdConsumer;
   private final IOFunction<ThresholdingOutputStream, OutputStream> outputStreamGetter;
   private long written;
   private boolean thresholdExceeded;

   public ThresholdingOutputStream(int var1) {
      this(var1, IOConsumer.noop(), NOOP_OS_GETTER);
   }

   public ThresholdingOutputStream(int var1, IOConsumer<ThresholdingOutputStream> var2, IOFunction<ThresholdingOutputStream, OutputStream> var3) {
      this.threshold = var1;
      this.thresholdConsumer = var2 == null ? IOConsumer.noop() : var2;
      this.outputStreamGetter = var3 == null ? NOOP_OS_GETTER : var3;
   }

   protected void checkThreshold(int var1) {
      if (!this.thresholdExceeded && this.written + (long)var1 > (long)this.threshold) {
         this.thresholdExceeded = true;
         this.thresholdReached();
      }

   }

   public void close() {
      try {
         this.flush();
      } catch (IOException var2) {
      }

      this.getStream().close();
   }

   public void flush() {
      this.getStream().flush();
   }

   public long getByteCount() {
      return this.written;
   }

   protected OutputStream getStream() {
      return (OutputStream)this.outputStreamGetter.apply(this);
   }

   public int getThreshold() {
      return this.threshold;
   }

   public boolean isThresholdExceeded() {
      return this.written > (long)this.threshold;
   }

   protected void resetByteCount() {
      this.thresholdExceeded = false;
      this.written = 0L;
   }

   protected void setByteCount(long var1) {
      this.written = var1;
   }

   protected void thresholdReached() {
      this.thresholdConsumer.accept(this);
   }

   public void write(byte[] var1) {
      this.checkThreshold(var1.length);
      this.getStream().write(var1);
      this.written += (long)var1.length;
   }

   public void write(byte[] var1, int var2, int var3) {
      this.checkThreshold(var3);
      this.getStream().write(var1, var2, var3);
      this.written += (long)var3;
   }

   public void write(int var1) {
      this.checkThreshold(1);
      this.getStream().write(var1);
      ++this.written;
   }
}
