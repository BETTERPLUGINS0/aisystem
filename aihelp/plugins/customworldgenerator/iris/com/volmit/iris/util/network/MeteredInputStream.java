package com.volmit.iris.util.network;

import java.io.InputStream;

public class MeteredInputStream extends InputStream {
   private final InputStream os;
   private long written;
   private long totalWritten;
   private long since;
   private boolean auto;
   private long interval;
   private long bps;

   public MeteredInputStream(InputStream os, long interval) {
      this.os = var1;
      this.written = 0L;
      this.totalWritten = 0L;
      this.auto = true;
      this.interval = var2;
      this.bps = 0L;
      this.since = System.currentTimeMillis();
   }

   public MeteredInputStream(InputStream os) {
      this(var1, 100L);
      this.auto = false;
   }

   public int read() {
      ++this.written;
      ++this.totalWritten;
      if (this.auto && System.currentTimeMillis() - this.getSince() > this.interval) {
         this.pollRead();
      }

      return this.os.read();
   }

   public long getSince() {
      return this.since;
   }

   public long getRead() {
      return this.written;
   }

   public long pollRead() {
      long var1 = this.written;
      this.written = 0L;
      double var3 = (double)(System.currentTimeMillis() - this.since) / 1000.0D;
      this.bps = (long)((double)var1 / var3);
      this.since = System.currentTimeMillis();
      return var1;
   }

   public void close() {
      this.os.close();
   }

   public boolean isAuto() {
      return this.auto;
   }

   public void setAuto(boolean auto) {
      this.auto = var1;
   }

   public long getInterval() {
      return this.interval;
   }

   public void setInterval(long interval) {
      this.interval = var1;
   }

   public long getTotalRead() {
      return this.totalWritten;
   }

   public long getBps() {
      return this.bps;
   }
}
