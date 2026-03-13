package com.volmit.iris.util.scheduling.jobs;

import com.volmit.iris.util.network.DL;
import com.volmit.iris.util.network.DownloadMonitor;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class DownloadJob implements Job {
   private final DL.Download download;
   private int tw = 1;
   private int cw = 0;

   public DownloadJob(String url, File destination) {
      this.download = new DL.Download(new URL(var1), var2, new DL.DownloadFlag[]{DL.DownloadFlag.CALCULATE_SIZE});
      this.download.monitor(new DownloadMonitor() {
         public void onUpdate(DL.DownloadState state, double progress, long elapsed, long estimated, long bps, long iobps, long size, long downloaded, long buffer, double bufferuse) {
            if (var12 == -1L) {
               DownloadJob.this.tw = 1;
            } else {
               DownloadJob.this.tw = (int)(var12 / 100L);
               DownloadJob.this.cw = (int)(var14 / 100L);
            }

         }
      });
   }

   public String getName() {
      return "Downloading";
   }

   public void execute() {
      try {
         this.download.start();

         while(this.download.isState(DL.DownloadState.DOWNLOADING)) {
            this.download.downloadChunk();
         }
      } catch (IOException var2) {
         var2.printStackTrace();
      }

      this.cw = this.tw;
   }

   public void completeWork() {
   }

   public int getTotalWork() {
      return this.tw;
   }

   public int getWorkCompleted() {
      return this.cw;
   }
}
