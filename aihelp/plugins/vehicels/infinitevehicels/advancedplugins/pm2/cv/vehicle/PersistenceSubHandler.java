package advancedplugins.pm2.cv.vehicle;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import advancedplugins.pm2.cv.api.configuration.Configuration;
import advancedplugins.pm2.cv.api.registry.Registries;
import advancedplugins.pm2.cv.api.vehicle.Vehicle;
import advancedplugins.pm2.cv.api.vehicle.configuration.VehicleConfiguration;
import advancedplugins.pm2.cv.api.vehicle.item.storage.VehicleItemHolder;
import es.outlook.adriansrj.nbt.nbt.io.NBTUtil;
import es.outlook.adriansrj.nbt.nbt.io.NamedTag;
import es.outlook.adriansrj.nbt.nbt.tag.CompoundTag;
import es.outlook.adriansrj.nbt.nbt.tag.Tag;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

class PersistenceSubHandler {
   private static final String STORAGE_FOLDER_NAME = "vehicles";
   final VehicleHandlerImpl vehicleHandler;
   final Set<PersistenceSubHandler.ChunkKey> loadedChunks = ConcurrentHashMap.newKeySet();
   final Set<UUID> unregistered = ConcurrentHashMap.newKeySet();
   final Set<UUID> notPersistent = ConcurrentHashMap.newKeySet();

   PersistenceSubHandler(VehicleHandlerImpl vehicleHandler) {
      this.vehicleHandler = var1;
   }

   void processUnregisteredVehicle(@NotNull Vehicle vehicle) {
      this.unregistered.add(var1.getUniqueId());
   }

   void processVehicleWorldChanged(@NotNull VehicleImpl vehicle, @NotNull World from, @NotNull World to) {
      (new PersistenceSubHandler.Storage(var2)).remove(var1.getUniqueId());
   }

   void processVehiclePersistenceChanged(@NotNull VehicleImpl vehicle, boolean persistent) {
      if (var2) {
         this.notPersistent.remove(var1.getUniqueId());
      } else {
         this.notPersistent.add(var1.getUniqueId());
      }

   }

   void processSave(@NotNull World world) {
      PersistenceSubHandler.Storage var2 = new PersistenceSubHandler.Storage(var1);
      CompoundTag var3 = var2.read();
      if (var3 == null) {
         var3 = new CompoundTag();
      } else {
         this.unregistered.removeIf((var1x) -> {
            return var3.remove(var1x.toString()) != null;
         });
         this.notPersistent.removeIf((var1x) -> {
            return var3.remove(var1x.toString()) != null;
         });
      }

      Set var4 = (Set)this.vehicleHandler.vehiclesByWorld.get(var1.getUID());
      if (var4 != null) {
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            Vehicle var6 = (Vehicle)var5.next();
            var6.getStorage().forEach((var0) -> {
               var0.getHolder().save();
            });
            if (var6.isPersistent()) {
               var3.put(var6.getUniqueId().toString(), PersistenceSubHandler.Storage.serialize(var6));
            }
         }
      }

      try {
         var2.write(var3);
      } catch (IOException var7) {
         var7.printStackTrace();
      }

   }

   void processUnloadedWorld(@NotNull World world) {
      this.processSave(var1);
      this.loadedChunks.removeIf((var1x) -> {
         return Objects.equals(var1x.world, var1.getUID());
      });
   }

   void processPlayerConnecting(@NotNull Player player) {
      Location var2 = var1.getLocation();
      int var3 = var2.getBlockX() >> 4;
      int var4 = var2.getBlockZ() >> 4;
      int var5 = Math.max(Configuration.RENDER_FAR_AWAY_CHUNKS.intValueClamp(1, Integer.MAX_VALUE) / 2, 1);
      int var6 = Math.min(var3 - var5, var3 + var5);
      int var7 = Math.max(var3 - var5, var3 + var5);
      int var8 = Math.min(var4 - var5, var4 + var5);
      int var9 = Math.max(var4 - var5, var4 + var5);

      for(var3 = var6; var3 <= var7; ++var3) {
         for(var4 = var8; var4 <= var9; ++var4) {
            this.processLoadedChunk(var1.getWorld(), var3, var4);
         }
      }

   }

   void processPlayerMoving(@NotNull PlayerMoveEvent event) {
      Location var2 = var1.getFrom();
      Location var3 = var1.getTo();
      if (var3 != null) {
         int var4 = var2.getBlockX() >> 4;
         int var5 = var2.getBlockZ() >> 4;
         int var6 = var3.getBlockX() >> 4;
         int var7 = var3.getBlockZ() >> 4;
         if (var4 != var6 || var5 != var7) {
            int var8 = Math.max(Configuration.RENDER_FAR_AWAY_CHUNKS.intValueClamp(1, Integer.MAX_VALUE) / 2, 1);
            int var9 = Math.min(var6 - var8, var6 + var8);
            int var10 = Math.max(var6 - var8, var6 + var8);
            int var11 = Math.min(var7 - var8, var7 + var8);
            int var12 = Math.max(var7 - var8, var7 + var8);

            for(int var13 = var9; var13 <= var10; ++var13) {
               for(int var14 = var11; var14 <= var12; ++var14) {
                  this.processLoadedChunk(var1.getPlayer().getWorld(), var13, var14);
               }
            }

         }
      }
   }

   void processLoadedChunk(@NotNull World world, int chunkX, int chunkZ) {
      this.processLoadedChunk(var1, var1.getWorldFolder(), var2, var3);
   }

   void processLoadedChunk(@NotNull World world, File worldFolder, int chunkX, int chunkZ) {
      if (this.loadedChunks.add(PersistenceSubHandler.ChunkKey.of(var1, var3, var4))) {
         PersistenceSubHandler.Storage var5 = new PersistenceSubHandler.Storage(var2);
         CompoundTag var6 = var5.read();
         if (var6 != null) {
            Iterator var7 = var6.keySet().iterator();

            while(var7.hasNext()) {
               String var8 = (String)var7.next();
               CompoundTag var9 = var6.getCompoundTag(var8);
               if (var9 == null) {
                  return;
               }

               int var10 = Location.locToBlock((double)var9.getFloat("x")) >> 4;
               int var11 = Location.locToBlock((double)var9.getFloat("z")) >> 4;
               if (var10 == var3 && var11 == var4) {
                  this.loadVehicle(var1, var9);
               }
            }

         }
      }
   }

   void loadVehicle(@NotNull World world, @NotNull CompoundTag data) {
      UUID var3 = UUID.fromString(var2.getString("uuid"));
      VehicleConfiguration var4 = (VehicleConfiguration)Registries.getRegistry(VehicleConfiguration.class).get(var2.getString("type"));
      if (var4 != null) {
         float var5 = var2.getFloat("x");
         float var6 = var2.getFloat("y");
         float var7 = var2.getFloat("z");
         float var8 = var2.getFloat("rot");
         float var9 = var2.getFloat("fuel");
         boolean var10 = var2.containsKey("keyed") && var2.getBoolean("keyed");
         if (!InfiniteVehicles.getVehicleHandler().getRegisteredVehicles().stream().anyMatch((var1x) -> {
            return var1x.getUniqueId().equals(var3);
         })) {
            UUID var11 = var2.containsKey("owner") ? UUID.fromString(var2.getString("owner")) : null;
            Vehicle var12 = this.vehicleHandler.spawnVehicle(var4, var1, (double)var5, (double)var6, (double)var7, var3, var11);
            var12.setKey(var10);
            if (var2.containsKey("health")) {
               float var13 = var2.getFloat("health");
               if (var13 > 0.0F) {
                  var12.setHealth(var13);
               }
            }

            var12.setRotation(var8);
            var12.setFuelLevel(var9);
            CompoundTag var14 = var2.getCompoundTag("upgrades");
            if (var14 != null) {
               var12.getUpgradeTiers().clear();
               var14.keySet().forEach((var2x) -> {
                  var12.getUpgradeTiers().put(var2x, var14.getInt(var2x));
               });
            }

            var12.getStorage().addAll(VehicleItemHolder.load(var12));
         }
      }
   }

   void processPluginDisabled() {
      Iterator var1 = Bukkit.getWorlds().iterator();

      while(var1.hasNext()) {
         World var2 = (World)var1.next();
         this.processSave(var2);
      }

   }

   static final class Storage {
      final File file;

      public Storage(@NotNull World world) {
         this(var1.getWorldFolder());
      }

      public Storage(@NotNull File worldFolder) {
         this.file = new File(new File(var1, "vehicles"), "vehicles.sto");
      }

      static CompoundTag serialize(@NotNull Vehicle vehicle) {
         CompoundTag var1 = new CompoundTag();
         var1.putString("uuid", var0.getUniqueId().toString());
         var1.putString("type", var0.getConfiguration().getId());
         var1.putFloat("x", (float)var0.getX());
         var1.putFloat("y", (float)var0.getY());
         var1.putFloat("z", (float)var0.getZ());
         var1.putFloat("health", var0.getHealth());
         var1.putFloat("rot", var0.getRotation());
         var1.putFloat("fuel", var0.getFuelLevel());
         var1.putInt("storageSize", var0.getStorageSize().getSlots());
         var1.putBoolean("keyed", var0.isKeyed());
         if (var0.getUpgradeConfiguration() != null) {
            CompoundTag var2 = new CompoundTag();
            Map var10000 = var0.getUpgradeTiers();
            Objects.requireNonNull(var2);
            var10000.forEach(var2::putInt);
            var1.put("upgrades", var2);
         }

         if (!var0.getStorage().isEmpty()) {
            var0.getStorage().forEach((var0x) -> {
               var0x.getHolder().save();
            });
         }

         UUID var3 = var0.getOwnerUniqueId();
         if (var3 != null) {
            var1.putString("owner", var3.toString());
         }

         return var1;
      }

      void remove(@NotNull UUID vehicleUniqueId) {
         CompoundTag var2 = this.read();
         if (var2 != null && var2.remove(var1.toString()) != null) {
            try {
               this.write(var2);
            } catch (IOException var4) {
               var4.printStackTrace();
            }
         }

      }

      CompoundTag read() {
         if (!this.file.exists()) {
            return null;
         } else {
            try {
               NamedTag var1 = NBTUtil.read(this.file, true);
               Tag var2 = var1.getTag();
               if (var2 instanceof CompoundTag) {
                  return (CompoundTag)var2;
               }
            } catch (IOException var3) {
               var3.printStackTrace();
            }

            return null;
         }
      }

      void write(@NotNull CompoundTag value) {
         if (this.file.exists()) {
            Files.delete(this.file.toPath());
         }

         if (!this.file.getParentFile().exists()) {
            this.file.getParentFile().mkdirs();
         }

         Files.createFile(this.file.toPath());

         try {
            NBTUtil.write((Tag)var1, (File)this.file, true);
         } catch (IOException var3) {
            var3.printStackTrace();
         }

      }
   }

   static record ChunkKey(UUID world, int x, int z) {
      ChunkKey(UUID world, int x, int z) {
         this.world = var1;
         this.x = var2;
         this.z = var3;
      }

      static PersistenceSubHandler.ChunkKey of(@NotNull World world, int chunkX, int chunkZ) {
         return new PersistenceSubHandler.ChunkKey(var0.getUID(), var1, var2);
      }

      public boolean equals(Object obj) {
         if (this == var1) {
            return true;
         } else if (var1 != null && this.getClass() == var1.getClass()) {
            PersistenceSubHandler.ChunkKey var2 = (PersistenceSubHandler.ChunkKey)var1;
            return this.x == var2.x && this.z == var2.z && this.world.equals(var2.world);
         } else {
            return false;
         }
      }

      public UUID world() {
         return this.world;
      }

      public int x() {
         return this.x;
      }

      public int z() {
         return this.z;
      }
   }
}
