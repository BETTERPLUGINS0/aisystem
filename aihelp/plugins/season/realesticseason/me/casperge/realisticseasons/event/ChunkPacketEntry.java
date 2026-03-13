package me.casperge.realisticseasons.event;

import com.comphenix.protocol.events.PacketEvent;
import me.casperge.realisticseasons.biome.HeightAccessor;

public class ChunkPacketEntry {
   private final PacketEvent event;
   private final int season;
   private final boolean isBedrock;
   private final int phase;
   private final int ySectionCount;
   private final HeightAccessor accessor;
   private final boolean christmasMode;
   private int weatherStatus;
   private boolean seasonchanges;

   public ChunkPacketEntry(PacketEvent var1, int var2, boolean var3, int var4, int var5, HeightAccessor var6, boolean var7, int var8, boolean var9) {
      this.event = var1;
      this.season = var2;
      this.isBedrock = var3;
      this.phase = var4;
      this.ySectionCount = var5;
      this.accessor = var6;
      this.christmasMode = var7;
      this.weatherStatus = var8;
      this.seasonchanges = var9;
   }

   public PacketEvent getEvent() {
      return this.event;
   }

   public int getSeason() {
      return this.season;
   }

   public boolean isBedrock() {
      return this.isBedrock;
   }

   public int getPhase() {
      return this.phase;
   }

   public int getySectionCount() {
      return this.ySectionCount;
   }

   public HeightAccessor getAccessor() {
      return this.accessor;
   }

   public boolean isChristmasMode() {
      return this.christmasMode;
   }

   public int getWeatherStatus() {
      return this.weatherStatus;
   }

   public boolean isSeasonchanges() {
      return this.seasonchanges;
   }
}
