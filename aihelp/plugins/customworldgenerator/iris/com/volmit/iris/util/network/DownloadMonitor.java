package com.volmit.iris.util.network;

@FunctionalInterface
public interface DownloadMonitor {
   void onUpdate(DL.DownloadState state, double progress, long elapsed, long estimated, long bps, long iobps, long size, long downloaded, long buffer, double bufferuse);
}
