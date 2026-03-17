package com.bergerkiller.bukkit.tc.offline.sign;

import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.config.DataReader;
import com.bergerkiller.bukkit.common.config.DataWriter;
import com.bergerkiller.bukkit.common.offline.OfflineBlock;
import com.bergerkiller.bukkit.common.offline.OfflineWorld;
import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.utils.StreamUtil;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.detector.DetectorRegion;
import com.bergerkiller.bukkit.tc.signactions.detector.DetectorSign;
import com.bergerkiller.bukkit.tc.signactions.spawner.SpawnSignManager;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

class OfflineSignLegacyImporter {
   private final OfflineSignStore store;
   private final TrainCarts plugin;
   private final Map<String, OfflineSignLegacyImporter.WorldLegacyData> byWorldName = new HashMap();
   private final File spawnSignsFile;
   private final File detectorSignsFile;

   public OfflineSignLegacyImporter(OfflineSignStore store, TrainCarts plugin) {
      this.store = store;
      this.plugin = plugin;
      this.spawnSignsFile = plugin.getDataFile(new String[]{"spawnsigns.dat"});
      this.detectorSignsFile = plugin.getDataFile(new String[]{"detectorsigns.dat"});
   }

   public void enable() {
      this.load();
      boolean imported = false;
      Iterator var2 = Bukkit.getWorlds().iterator();

      while(var2.hasNext()) {
         World world = (World)var2.next();
         OfflineSignLegacyImporter.WorldLegacyData legacyData = (OfflineSignLegacyImporter.WorldLegacyData)this.byWorldName.remove(world.getName());
         if (legacyData != null) {
            if (!imported) {
               imported = true;
               this.plugin.getLogger().log(Level.WARNING, "Importing legacy sign metadata...");
            }

            legacyData.importData(this.store, world);
         }
      }

      if (imported) {
         this.plugin.getLogger().log(Level.WARNING, "Legacy sign metadata imported!");
         this.save();
      }

      if (!this.byWorldName.isEmpty()) {
         Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onWorldLoad(WorldLoadEvent event) {
               OfflineSignLegacyImporter.WorldLegacyData legacyData = (OfflineSignLegacyImporter.WorldLegacyData)OfflineSignLegacyImporter.this.byWorldName.remove(event.getWorld().getName());
               if (legacyData != null) {
                  OfflineSignLegacyImporter.this.plugin.getLogger().log(Level.WARNING, "Importing legacy sign metadata for world " + event.getWorld().getName() + "...");
                  legacyData.importData(OfflineSignLegacyImporter.this.store, event.getWorld());
                  OfflineSignLegacyImporter.this.save();
                  OfflineSignLegacyImporter.this.plugin.getLogger().log(Level.WARNING, "Legacy sign metadata for world " + event.getWorld().getName() + " imported!");
               }

            }
         }, this.plugin);
      }

   }

   private void load() {
      if (this.spawnSignsFile.exists()) {
         (new DataReader(this.spawnSignsFile) {
            public void read(DataInputStream stream) throws IOException {
               for(int count = stream.readInt(); count > 0; --count) {
                  OfflineSignLegacyImporter.LegacySpawnSignData spawnSign = OfflineSignLegacyImporter.LegacySpawnSignData.read(stream);
                  OfflineSignLegacyImporter.this.dataOnWorld(spawnSign.signWorldName).spawnSigns.add(spawnSign);
               }

            }
         }).read();
      }

      if (this.detectorSignsFile.exists()) {
         (new DataReader(this.detectorSignsFile) {
            public void read(DataInputStream stream) throws IOException {
               for(int count = stream.readInt(); count > 0; --count) {
                  OfflineSignLegacyImporter.LegacyDetectorSignPairData pair = OfflineSignLegacyImporter.LegacyDetectorSignPairData.read(stream);
                  DetectorRegion region = DetectorRegion.getRegion(pair.detectorRegionUUID);
                  if (region != null) {
                     OfflineSignLegacyImporter.this.dataOnWorld(region.getWorldName()).detectorSignPairs.add(pair);
                  }
               }

            }
         }).read();
      }

   }

   private void save() {
      if (this.byWorldName.isEmpty()) {
         this.spawnSignsFile.delete();
         this.detectorSignsFile.delete();
      } else {
         (new DataWriter(this.spawnSignsFile) {
            public void write(DataOutputStream stream) throws IOException {
               List<OfflineSignLegacyImporter.LegacySpawnSignData> spawnSigns = (List)OfflineSignLegacyImporter.this.byWorldName.values().stream().flatMap((data) -> {
                  return data.spawnSigns.stream();
               }).collect(Collectors.toList());
               stream.writeInt(spawnSigns.size());
               Iterator var3 = spawnSigns.iterator();

               while(var3.hasNext()) {
                  OfflineSignLegacyImporter.LegacySpawnSignData spawnSign = (OfflineSignLegacyImporter.LegacySpawnSignData)var3.next();
                  spawnSign.write(stream);
               }

            }
         }).write();
         (new DataWriter(this.detectorSignsFile) {
            public void write(DataOutputStream stream) throws IOException {
               List<OfflineSignLegacyImporter.LegacyDetectorSignPairData> detectorSignPairs = (List)OfflineSignLegacyImporter.this.byWorldName.values().stream().flatMap((data) -> {
                  return data.detectorSignPairs.stream();
               }).collect(Collectors.toList());
               stream.writeInt(detectorSignPairs.size());
               Iterator var3 = detectorSignPairs.iterator();

               while(var3.hasNext()) {
                  OfflineSignLegacyImporter.LegacyDetectorSignPairData detectorSignPair = (OfflineSignLegacyImporter.LegacyDetectorSignPairData)var3.next();
                  detectorSignPair.write(stream);
               }

            }
         }).write();
      }

   }

   private OfflineSignLegacyImporter.WorldLegacyData dataOnWorld(String worldName) {
      return (OfflineSignLegacyImporter.WorldLegacyData)this.byWorldName.computeIfAbsent(worldName, (name) -> {
         return new OfflineSignLegacyImporter.WorldLegacyData();
      });
   }

   private static class WorldLegacyData {
      public final List<OfflineSignLegacyImporter.LegacySpawnSignData> spawnSigns;
      public final List<OfflineSignLegacyImporter.LegacyDetectorSignPairData> detectorSignPairs;

      private WorldLegacyData() {
         this.spawnSigns = new ArrayList();
         this.detectorSignPairs = new ArrayList();
      }

      public void importData(OfflineSignStore store, World world) {
         Iterator var3 = this.spawnSigns.iterator();

         Sign sign1;
         while(var3.hasNext()) {
            OfflineSignLegacyImporter.LegacySpawnSignData spawnSign = (OfflineSignLegacyImporter.LegacySpawnSignData)var3.next();
            sign1 = this.findSign(world, spawnSign.signLocation, "spawn");
            if (sign1 != null) {
               SpawnSignManager.SpawnSignMetadata metadata = new SpawnSignManager.SpawnSignMetadata(spawnSign.interval, System.currentTimeMillis() + spawnSign.remaining - spawnSign.interval, spawnSign.active);
               store.put((Sign)sign1, true, metadata);
            }
         }

         var3 = this.detectorSignPairs.iterator();

         while(true) {
            while(var3.hasNext()) {
               OfflineSignLegacyImporter.LegacyDetectorSignPairData detectorSignPair = (OfflineSignLegacyImporter.LegacyDetectorSignPairData)var3.next();
               sign1 = this.findSign(world, detectorSignPair.sign1Location, "detector");
               Sign sign2 = this.findSign(world, detectorSignPair.sign2Location, "detector");
               DetectorRegion region;
               if (sign1 != null && sign2 != null) {
                  region = DetectorRegion.getRegion(detectorSignPair.detectorRegionUUID);
                  if (region != null) {
                     OfflineBlock sign1Block = OfflineWorld.of(world).getBlockAt(detectorSignPair.sign1Location);
                     OfflineBlock sign2Block = OfflineWorld.of(world).getBlockAt(detectorSignPair.sign2Location);
                     store.put((Sign)sign1, true, new DetectorSign.Metadata(sign2Block, true, region, detectorSignPair.sign1LeverDown));
                     store.put((Sign)sign2, true, new DetectorSign.Metadata(sign1Block, true, region, detectorSignPair.sign2LeverDown));
                  }
               } else {
                  region = DetectorRegion.getRegion(detectorSignPair.detectorRegionUUID);
                  if (region != null && !region.isRegistered()) {
                     region.remove();
                  }
               }
            }

            return;
         }
      }

      private Sign findSign(World world, IntVector3 signLocation, String type) {
         Block signBlock = signLocation.toBlock(world);
         world.getChunkAt(MathUtil.toChunk(signBlock.getX()), MathUtil.toChunk(signBlock.getZ()));
         Sign sign = BlockUtil.getSign(signBlock);
         return sign != null && sign.getLine(1).toLowerCase(Locale.ENGLISH).trim().startsWith(type) ? sign : null;
      }

      // $FF: synthetic method
      WorldLegacyData(Object x0) {
         this();
      }
   }

   private static class LegacyDetectorSignPairData {
      public final IntVector3 sign1Location;
      public final IntVector3 sign2Location;
      public final boolean sign1LeverDown;
      public final boolean sign2LeverDown;
      public final UUID detectorRegionUUID;

      public static OfflineSignLegacyImporter.LegacyDetectorSignPairData read(DataInputStream stream) throws IOException {
         return new OfflineSignLegacyImporter.LegacyDetectorSignPairData(stream);
      }

      private LegacyDetectorSignPairData(DataInputStream stream) throws IOException {
         this.detectorRegionUUID = StreamUtil.readUUID(stream);
         this.sign1Location = IntVector3.read(stream);
         this.sign2Location = IntVector3.read(stream);
         this.sign1LeverDown = stream.readBoolean();
         this.sign2LeverDown = stream.readBoolean();
      }

      public void write(DataOutputStream stream) throws IOException {
         StreamUtil.writeUUID(stream, this.detectorRegionUUID);
         this.sign1Location.write(stream);
         this.sign2Location.write(stream);
         stream.writeBoolean(this.sign1LeverDown);
         stream.writeBoolean(this.sign2LeverDown);
      }
   }

   private static class LegacySpawnSignData {
      public final IntVector3 signLocation;
      public final String signWorldName;
      public final long interval;
      public final long remaining;
      public final boolean active;

      public static OfflineSignLegacyImporter.LegacySpawnSignData read(DataInputStream stream) throws IOException {
         return new OfflineSignLegacyImporter.LegacySpawnSignData(stream);
      }

      private LegacySpawnSignData(DataInputStream stream) throws IOException {
         this.signLocation = IntVector3.read(stream);
         this.signWorldName = stream.readUTF();
         this.interval = stream.readLong();
         long remainingVal = stream.readLong();
         if (remainingVal == Long.MAX_VALUE) {
            this.remaining = 0L;
            this.active = false;
         } else {
            this.remaining = remainingVal;
            this.active = true;
         }

      }

      public void write(DataOutputStream stream) throws IOException {
         this.signLocation.write(stream);
         stream.writeUTF(this.signWorldName);
         stream.writeLong(this.interval);
         if (this.active) {
            stream.writeLong(this.remaining);
         } else {
            stream.writeLong(Long.MAX_VALUE);
         }

      }
   }
}
