package com.volmit.iris.util.network;

import com.volmit.iris.util.collection.KSet;
import com.volmit.iris.util.io.IO;
import com.volmit.iris.util.scheduling.ChronoLatch;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

public abstract class DL {
   protected File d;
   protected URL u;
   protected ChronoLatch latch;
   protected KSet<DL.DownloadFlag> flags;
   protected MeteredOutputStream o;
   protected DL.DownloadState state;
   protected int timeout;
   protected long size;
   protected long start;
   protected long downloaded;
   protected long currentChunk;
   protected long lastChunk;
   protected long bps;
   protected int bufferSize;
   protected long lastPull;
   protected DownloadMonitor m;

   public DL(URL u, File d, DL.DownloadFlag... downloadFlags) {
      this.d = var2;
      this.u = var1;
      this.size = -1L;
      this.lastPull = -1L;
      this.downloaded = 0L;
      this.bufferSize = 262144;
      this.currentChunk = 0L;
      this.lastChunk = -1L;
      this.bps = -1L;
      this.start = -1L;
      this.timeout = 10000;
      this.state = DL.DownloadState.NEW;
      this.flags = new KSet(new DL.DownloadFlag[0]);
      this.latch = new ChronoLatch(500L);
      this.flags.addAll(Arrays.asList(var3));
   }

   public void monitor(DownloadMonitor m) {
      this.m = var1;
   }

   public void update() {
      if (this.m != null) {
         this.m.onUpdate(this.state, this.getProgress(), this.getElapsed(), this.getTimeLeft(), this.bps, this.getDiskBytesPerSecond(), this.size, this.downloaded, (long)this.bufferSize, this.getBufferUse());
      }

   }

   public boolean hasFlag(DL.DownloadFlag f) {
      return this.flags.contains(var1);
   }

   public boolean isState(DL.DownloadState s) {
      return this.state.equals(var1);
   }

   protected void state(DL.DownloadState s) {
      this.state = var1;
      this.update();
   }

   public int getBufferSize() {
      return this.bufferSize;
   }

   public void start() {
      if (!this.isState(DL.DownloadState.NEW)) {
         throw new DownloadException("Cannot start download while " + this.state.toString());
      } else {
         this.state(DL.DownloadState.STARTING);
         if (this.hasFlag(DL.DownloadFlag.CALCULATE_SIZE)) {
            this.size = this.calculateSize();
         }

         this.start = System.currentTimeMillis();
         this.downloaded = 0L;
         this.bps = 0L;
         this.lastChunk = System.currentTimeMillis();
         this.o = new MeteredOutputStream(new FileOutputStream(this.d), 100L);
         this.openStream();
         this.state(DL.DownloadState.DOWNLOADING);
      }
   }

   protected abstract long download();

   protected abstract void openStream();

   protected abstract void closeStream();

   public void downloadChunk() {
      if (!this.isState(DL.DownloadState.DOWNLOADING)) {
         throw new DownloadException("Cannot download while " + this.state.toString());
      } else {
         long var1 = this.download();
         this.lastPull = var1;
         if (var1 < 0L) {
            this.finishDownload();
         } else {
            this.downloaded += var1;
            this.currentChunk += var1;
            double var3 = (double)(System.currentTimeMillis() - this.lastChunk) / 1000.0D;
            this.bps = (long)((double)this.currentChunk / var3);
            if (this.latch.flip()) {
               this.update();
            }

         }
      }
   }

   public double getBufferUse() {
      return (double)this.lastPull / (double)this.bufferSize;
   }

   private void finishDownload() {
      if (!this.isState(DL.DownloadState.NEW)) {
         throw new DownloadException("Cannot finish download while " + this.state.toString());
      } else {
         this.closeStream();
         this.o.close();
         this.state(DL.DownloadState.COMPLETE);
      }
   }

   public long getElapsed() {
      return System.currentTimeMillis() - this.start;
   }

   public long getRemaining() {
      return this.size - this.downloaded;
   }

   public long getTimeLeft() {
      return (long)((double)this.getRemaining() / (double)this.bps * 1000.0D);
   }

   public long getDiskBytesPerSecond() {
      return this.o == null ? -1L : this.o.getBps();
   }

   public long getBytesPerSecond() {
      return this.bps;
   }

   public double getProgress() {
      return this.hasProgress() ? (double)this.downloaded / (double)this.size : -1.0D;
   }

   public boolean hasProgress() {
      return this.size > 0L;
   }

   private long calculateSize() {
      URLConnection var1 = this.u.openConnection();
      var1.setConnectTimeout(this.timeout);
      var1.setReadTimeout(this.timeout);
      var1.connect();
      return var1.getContentLengthLong();
   }

   public static enum DownloadState {
      NEW,
      STARTING,
      DOWNLOADING,
      FINALIZING,
      COMPLETE,
      FAILED;

      // $FF: synthetic method
      private static DL.DownloadState[] $values() {
         return new DL.DownloadState[]{NEW, STARTING, DOWNLOADING, FINALIZING, COMPLETE, FAILED};
      }
   }

   public static enum DownloadFlag {
      CALCULATE_SIZE;

      // $FF: synthetic method
      private static DL.DownloadFlag[] $values() {
         return new DL.DownloadFlag[]{CALCULATE_SIZE};
      }
   }

   public static class Download extends DL {
      protected InputStream in;
      protected byte[] buf;

      public Download(URL u, File d, DL.DownloadFlag... downloadFlags) {
         super(var1, var2, var3);
      }

      protected long download() {
         return IO.transfer(this.in, this.o, this.buf, this.bufferSize);
      }

      protected void openStream() {
         this.in = this.u.openStream();
         this.buf = new byte[8192];
      }

      protected void closeStream() {
         this.in.close();
      }
   }

   public static class DoubleBufferedDownload extends DL.Download {
      protected BufferedOutputStream os;

      public DoubleBufferedDownload(URL u, File d, DL.DownloadFlag... downloadFlags) {
         super(var1, var2, var3);
      }

      protected void openStream() {
         this.os = new BufferedOutputStream(this.o, 131072);
         this.in = new BufferedInputStream(this.u.openStream(), 131072);
         this.buf = new byte[16384];
      }
   }

   public static class ThrottledDownload extends DL.Download {
      private final long mbps;

      public ThrottledDownload(URL u, File d, long mbps, DL.DownloadFlag... downloadFlags) {
         super(var1, var2, var5);
         this.mbps = var3;
      }

      protected long download() {
         if (this.getBytesPerSecond() > this.mbps) {
            try {
               Thread.sleep(40L);
            } catch (InterruptedException var2) {
               var2.printStackTrace();
            }

            return IO.transfer(this.in, this.o, 8192, this.mbps / 20L);
         } else {
            return IO.transfer(this.in, this.o, 8192, (long)this.bufferSize);
         }
      }
   }
}
