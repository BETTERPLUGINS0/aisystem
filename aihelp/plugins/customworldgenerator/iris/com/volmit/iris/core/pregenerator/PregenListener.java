package com.volmit.iris.core.pregenerator;

public interface PregenListener {
   void onTick(double chunksPerSecond, double chunksPerMinute, double regionsPerMinute, double percent, long generated, long totalChunks, long chunksRemaining, long eta, long elapsed, String method, boolean cached);

   void onChunkGenerating(int x, int z);

   default void onChunkGenerated(int x, int z) {
      this.onChunkGenerated(x, z, false);
   }

   void onChunkGenerated(int x, int z, boolean cached);

   void onRegionGenerated(int x, int z);

   void onRegionGenerating(int x, int z);

   void onChunkCleaned(int x, int z);

   void onRegionSkipped(int x, int z);

   void onNetworkStarted(int x, int z);

   void onNetworkFailed(int x, int z);

   void onNetworkReclaim(int revert);

   void onNetworkGeneratedChunk(int x, int z);

   void onNetworkDownloaded(int x, int z);

   void onClose();

   void onSaving();

   void onChunkExistsInRegionGen(int x, int z);
}
