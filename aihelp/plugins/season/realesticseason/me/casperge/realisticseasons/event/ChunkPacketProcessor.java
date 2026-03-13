package me.casperge.realisticseasons.event;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketEvent;
import java.util.ArrayList;
import java.util.List;
import me.casperge.interfaces.ProtocolLibUtils;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.biome.HeightAccessor;
import org.bukkit.Bukkit;

public class ChunkPacketProcessor implements Runnable {
   static final int MAXQUEUE = 20000;
   private List<ChunkPacketEntry> entries = new ArrayList();
   private ProtocolLibUtils protutils;

   public ChunkPacketProcessor(ProtocolLibUtils var1) {
      this.protutils = var1;
   }

   public void run() {
      while(RealisticSeasons.isEnabled.get()) {
         synchronized(this) {
            if (!this.entries.isEmpty()) {
               this.handleEntry();
            } else {
               try {
                  this.wait();
               } catch (InterruptedException var4) {
                  var4.printStackTrace();
               }
            }
         }
      }

   }

   private synchronized void handleEntry() {
      ChunkPacketEntry var1 = (ChunkPacketEntry)this.entries.remove(0);
      synchronized(var1.getEvent().getAsyncMarker().getProcessingLock()) {
         try {
            this.protutils.readPacket(var1.getEvent().getPacket(), var1.getSeason(), var1.isBedrock(), var1.getPhase(), var1.getySectionCount(), var1.getAccessor(), var1.isChristmasMode(), var1.getWeatherStatus(), var1.isSeasonchanges());
         } catch (IndexOutOfBoundsException var9) {
            Bukkit.getLogger().warning("[RealisticSeasons] Exception thrown in packet processing[" + var1.getEvent().getPacket().toString() + "], ignore this message if you're not experiencing any other issues.");
         } finally {
            ProtocolLibrary.getProtocolManager().getAsynchronousManager().signalPacketTransmission(var1.getEvent());
            ChunkPacketEventProtocolLibAsync.queue.decrementAndGet();
         }

      }
   }

   public synchronized void addEntry(PacketEvent var1, int var2, boolean var3, int var4, int var5, HeightAccessor var6, boolean var7, int var8, boolean var9) {
      this.entries.add(new ChunkPacketEntry(var1, var2, var3, var4, var5, var6, var7, var8, var9));
      this.notify();
   }
}
