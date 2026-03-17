package com.bergerkiller.bukkit.tc.signactions.spawner;

import com.bergerkiller.bukkit.common.chunk.ForcedChunk;
import com.bergerkiller.bukkit.common.offline.OfflineBlock;
import com.bergerkiller.bukkit.common.resources.SoundEffect;
import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.ChunkUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.common.utils.PlayerUtil;
import com.bergerkiller.bukkit.common.utils.StringUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.common.wrappers.LongHashMap;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.controller.spawnable.SpawnableGroup;
import com.bergerkiller.bukkit.tc.controller.spawnable.SpawnableMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.offline.sign.OfflineSign;
import com.bergerkiller.bukkit.tc.offline.sign.OfflineSignStore;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.signactions.SignActionMode;
import com.bergerkiller.bukkit.tc.signactions.SignActionSpawn;
import java.util.Iterator;
import java.util.Locale;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class SpawnSign {
   private final TrainCarts plugin;
   private final OfflineSignStore store;
   private final OfflineBlock location;
   private final boolean frontText;
   private SpawnSignManager.SpawnSignMetadata state;
   private int ticksUntilFreeing = 0;
   private double spawnForce = 0.0D;
   private String spawnFormat;
   private LongHashMap<SpawnSign.SignSpawnChunk> chunks = new LongHashMap();
   private int num_chunks_loaded = 0;

   SpawnSign(TrainCarts plugin, OfflineSignStore store, OfflineSign sign, SpawnSignManager.SpawnSignMetadata metadata) {
      this.plugin = plugin;
      this.store = store;
      this.location = sign.getBlock();
      this.frontText = sign.isFrontText();
      this.updateState(sign, metadata);
      int center_cx = MathUtil.toChunk(this.location.getX());
      int center_cz = MathUtil.toChunk(this.location.getZ());

      for(int dx = -2; dx <= 2; ++dx) {
         for(int dz = -2; dz <= 2; ++dz) {
            int cx = center_cx + dx;
            int cz = center_cz + dz;
            this.chunks.put(cx, cz, this.createSpawnChunk(cx, cz));
         }
      }

   }

   public TrainCarts getPlugin() {
      return this.plugin;
   }

   void updateState(OfflineSign sign, SpawnSignManager.SpawnSignMetadata metadata) {
      this.spawnForce = SpawnSign.SpawnOptions.fromOfflineSign(sign).launchVelocity;
      this.spawnFormat = sign.getLine(2) + sign.getLine(3);
      this.state = metadata;
   }

   void updateUsingEvent(SignActionEvent event) {
      this.store.verifySign(event.getSign(), this.frontText, SpawnSignManager.SpawnSignMetadata.class);
      boolean active = event.isPowered();
      if (active != this.state.active) {
         this.store.putIfPresent(this.location, this.frontText, this.state.setActive(active));
      }

   }

   public OfflineBlock getLocation() {
      return this.location;
   }

   public boolean isFrontText() {
      return this.frontText;
   }

   public boolean hasInterval() {
      return this.state.intervalMillis > 0L;
   }

   public long getInterval() {
      return this.state.intervalMillis;
   }

   public long getRemaining(long previousTime, long currentTime) {
      if (this.isActive() && this.hasInterval()) {
         long numIntervalsSkipped = (currentTime - this.state.autoSpawnStartTime) / this.state.intervalMillis;
         long nextSpawnTimestamp = this.state.autoSpawnStartTime + numIntervalsSkipped * this.state.intervalMillis;
         if (nextSpawnTimestamp <= previousTime) {
            nextSpawnTimestamp += this.state.intervalMillis;
         }

         return currentTime >= nextSpawnTimestamp ? 0L : nextSpawnTimestamp - currentTime;
      } else {
         return Long.MAX_VALUE;
      }
   }

   public boolean isActive() {
      return this.state.active;
   }

   public double getSpawnForce() {
      return this.spawnForce;
   }

   public SpawnableGroup getSpawnableGroup() {
      return SpawnableGroup.parse(this.getPlugin(), this.spawnFormat);
   }

   public void resetSpawnTime() {
      if (this.store != null) {
         this.store.putIfPresent(this.location, this.frontText, this.state.setAutoSpawnStart(System.currentTimeMillis() + this.state.intervalMillis));
      }

   }

   public World getWorld() {
      return this.location.getLoadedWorld();
   }

   public void loadChunksAsync(double percent) {
      if (this.getWorld() == null) {
         this.num_chunks_loaded = this.chunks.size();
      } else {
         percent = MathUtil.clamp(percent, 0.0D, 1.0D);
         int num_chunks_loaded_goal = (int)((double)this.chunks.size() * percent);
         Iterator var4 = this.chunks.getValues().iterator();

         while(var4.hasNext()) {
            SpawnSign.SignSpawnChunk chunk = (SpawnSign.SignSpawnChunk)var4.next();
            if (this.num_chunks_loaded >= num_chunks_loaded_goal) {
               break;
            }

            if (!chunk.chunk.isNone()) {
               chunk.loadAsync();
               ++this.num_chunks_loaded;
            }
         }

      }
   }

   public void loadChunksAsyncReset() {
      Iterator var1 = this.chunks.getValues().iterator();

      while(var1.hasNext()) {
         SpawnSign.SignSpawnChunk chunk = (SpawnSign.SignSpawnChunk)var1.next();
         chunk.close();
      }

      this.num_chunks_loaded = 0;
   }

   public void loadChunksAsyncResetAuto() {
      if (this.ticksUntilFreeing > 0 && --this.ticksUntilFreeing == 0) {
         this.loadChunksAsyncReset();
      }

   }

   public void remove() {
      if (this.store != null) {
         this.store.remove(this.location, this.frontText, SpawnSignManager.SpawnSignMetadata.class);
      }

   }

   public void spawn() {
      Block signBlock = this.location.getLoadedBlock();
      if (signBlock != null) {
         Sign bsign = BlockUtil.getSign(signBlock);
         if (bsign == null) {
            this.store.removeAll(signBlock);
            return;
         }

         if (this.store.verifySign(bsign, this.frontText, SpawnSignManager.SpawnSignMetadata.class) == null) {
            return;
         }

         SignActionEvent event = new SignActionEvent(RailLookup.TrackedSign.forRealSign((Sign)bsign, this.frontText, (RailPiece)null));
         if (isValid(event)) {
            this.updateUsingEvent(event);
            this.spawn(event);
         } else {
            this.remove();
         }
      } else {
         this.loadChunksAsyncReset();
      }

   }

   public void spawn(SignActionEvent sign) {
      if (this.store == null || this.store.verifySign(sign.getSign(), this.frontText, SpawnSignManager.SpawnSignMetadata.class) != null) {
         this.ticksUntilFreeing = 2;
         Iterator var2 = this.chunks.getValues().iterator();

         while(var2.hasNext()) {
            SpawnSign.SignSpawnChunk chunk = (SpawnSign.SignSpawnChunk)var2.next();
            chunk.loadSync();
         }

         SpawnableGroup.SpawnLocationList locs = SignActionSpawn.spawn(this, sign);
         if (locs != null && !locs.locations.isEmpty()) {
            LongHashMap<SpawnSign.SignSpawnChunk> new_chunks = new LongHashMap(this.chunks.size());
            Iterator var4 = locs.locations.iterator();

            while(var4.hasNext()) {
               SpawnableMember.SpawnLocation loc = (SpawnableMember.SpawnLocation)var4.next();
               int x = MathUtil.toChunk(loc.location.getX());
               int z = MathUtil.toChunk(loc.location.getZ());

               for(int dx = -2; dx <= 2; ++dx) {
                  for(int dz = -2; dz <= 2; ++dz) {
                     int cx = x + dx;
                     int cz = z + dz;
                     long key = MathUtil.longHashToLong(cx, cz);
                     if (!new_chunks.contains(key)) {
                        SpawnSign.SignSpawnChunk chunk = (SpawnSign.SignSpawnChunk)this.chunks.remove(key);
                        if (chunk == null) {
                           chunk = this.createSpawnChunk(cx, cz);
                           chunk.loadSync();
                        }

                        new_chunks.put(key, chunk);
                     }
                  }
               }
            }

            var4 = this.chunks.getValues().iterator();

            while(var4.hasNext()) {
               SpawnSign.SignSpawnChunk originalChunk = (SpawnSign.SignSpawnChunk)var4.next();
               originalChunk.close();
            }

            this.chunks = new_chunks;
            this.num_chunks_loaded = this.chunks.size();
         }

      }
   }

   public void showFailParticles(Color color) {
      Vector pos = MathUtil.addToVector(this.location.getPosition().toVector(), 0.5D, 0.5D, 0.5D);
      Location loc = pos.toLocation(this.getWorld());
      Iterator var4 = WorldUtil.getNearbyEntities(loc, 64.0D, 64.0D, 64.0D).iterator();

      while(var4.hasNext()) {
         Entity e = (Entity)var4.next();
         if (e instanceof Player) {
            PlayerUtil.spawnDustParticles((Player)e, pos, color);
            PlayerUtil.playSound((Player)e, loc, SoundEffect.EXTINGUISH, 0.2F, 1.0F);
         }
      }

   }

   private SpawnSign.SignSpawnChunk createSpawnChunk(int cx, int cz) {
      return (SpawnSign.SignSpawnChunk)(this.store == null ? new SpawnSign.SignSpawnChunkSync(this.location.getWorldUUID(), cx, cz) : new SpawnSign.SignSpawnChunk(this.location.getWorldUUID(), cx, cz));
   }

   public String toString() {
      long currentTime = System.currentTimeMillis();
      StringBuilder str = new StringBuilder();
      str.append("{");
      str.append("pos=").append(this.location.toString());
      str.append(", interval=").append(this.getInterval());
      str.append(", remaining=").append(this.getRemaining(currentTime, currentTime));
      str.append(", spawnForce=").append(this.getSpawnForce());
      str.append(", spawnable=").append(this.spawnFormat);
      str.append("}");
      return str.toString();
   }

   public static double getSpawnForce(SignActionEvent event) {
      return SpawnSign.SpawnOptions.fromEvent(event).launchVelocity;
   }

   public static long getSpawnTime(SignActionEvent event) {
      return SpawnSign.SpawnOptions.fromEvent(event).autoSpawnInterval;
   }

   public static boolean isValid(SignActionEvent event) {
      return event != null && event.getMode() != SignActionMode.NONE && event.isType("spawn");
   }

   private static class SignSpawnChunk {
      private final ForcedChunk chunk = ForcedChunk.none();
      public final UUID worldUUID;
      public final int x;
      public final int z;

      public SignSpawnChunk(UUID worldUUID, int x, int z) {
         this.worldUUID = worldUUID;
         this.x = x;
         this.z = z;
      }

      public void loadSync() {
         World world = Bukkit.getWorld(this.worldUUID);
         if (world != null) {
            if (this.chunk.isNone()) {
               this.chunk.move(ChunkUtil.forceChunkLoaded(world, this.x, this.z));
            }

            this.chunk.getChunk();
         }
      }

      public void loadAsync() {
         if (this.chunk != null) {
            World world = Bukkit.getWorld(this.worldUUID);
            if (world != null) {
               this.chunk.move(ChunkUtil.forceChunkLoaded(world, this.x, this.z));
            }
         }

      }

      public void close() {
         this.chunk.close();
      }
   }

   public static class SpawnOptions {
      public final double launchVelocity;
      public final long autoSpawnInterval;

      private SpawnOptions(String secondSignLine) {
         String line = secondSignLine.toLowerCase(Locale.ENGLISH);
         int idx = line.indexOf(32);
         String[] args;
         if (idx == -1) {
            args = StringUtil.EMPTY_ARRAY;
         } else {
            args = line.substring(idx + 1).split(" ");
         }

         this.launchVelocity = parseVelocity(args);
         this.autoSpawnInterval = getAutoSpawnInterval(args);
      }

      public static SpawnSign.SpawnOptions fromEvent(SignActionEvent event) {
         return new SpawnSign.SpawnOptions(event.getLine(1));
      }

      public static SpawnSign.SpawnOptions fromOfflineSign(OfflineSign sign) {
         return new SpawnSign.SpawnOptions(sign.getLine(1));
      }

      private static double parseVelocity(String[] args) {
         if (args.length >= 2) {
            return !args[0].contains(":") ? Util.parseVelocity(args[0], 0.0D) : Util.parseVelocity(args[1], 0.0D);
         } else {
            return args.length >= 1 && !args[0].contains(":") ? Util.parseVelocity(args[0], 0.0D) : 0.0D;
         }
      }

      private static long getAutoSpawnInterval(String[] args) {
         if (args.length >= 2) {
            return args[1].contains(":") ? ParseUtil.parseTime(args[1]) : ParseUtil.parseTime(args[0]);
         } else {
            return args.length >= 1 && args[0].contains(":") ? ParseUtil.parseTime(args[0]) : 0L;
         }
      }
   }

   private static class SignSpawnChunkSync extends SpawnSign.SignSpawnChunk {
      public SignSpawnChunkSync(UUID worldUUID, int x, int z) {
         super(worldUUID, x, z);
      }

      public void loadSync() {
         World world = Bukkit.getWorld(this.worldUUID);
         if (world != null) {
            world.getChunkAt(this.x, this.z);
         }

      }

      public void loadAsync() {
      }
   }
}
