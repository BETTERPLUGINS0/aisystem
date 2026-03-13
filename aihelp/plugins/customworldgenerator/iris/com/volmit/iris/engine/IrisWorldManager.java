package com.volmit.iris.engine;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.link.Identifier;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.service.ExternalDataSVC;
import com.volmit.iris.engine.data.cache.Cache;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.framework.EngineAssignedWorldManager;
import com.volmit.iris.engine.object.IRare;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisBlockDrops;
import com.volmit.iris.engine.object.IrisEntitySpawn;
import com.volmit.iris.engine.object.IrisMarker;
import com.volmit.iris.engine.object.IrisPosition;
import com.volmit.iris.engine.object.IrisRegion;
import com.volmit.iris.engine.object.IrisSpawner;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.collection.KSet;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.mantle.Mantle;
import com.volmit.iris.util.mantle.MantleChunk;
import com.volmit.iris.util.mantle.flag.MantleFlag;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.math.Position2;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.matter.MatterMarker;
import com.volmit.iris.util.paper.PaperLib;
import com.volmit.iris.util.parallel.MultiBurst;
import com.volmit.iris.util.plugin.Chunks;
import com.volmit.iris.util.plugin.VolmitSender;
import com.volmit.iris.util.scheduling.ChronoLatch;
import com.volmit.iris.util.scheduling.J;
import com.volmit.iris.util.scheduling.Looper;
import com.volmit.iris.util.scheduling.jobs.QueueJob;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Generated;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class IrisWorldManager extends EngineAssignedWorldManager {
   private final Looper looper;
   private final int id;
   private final KList<Runnable> updateQueue = new KList();
   private final ChronoLatch cl;
   private final ChronoLatch clw;
   private final ChronoLatch ecl;
   private final ChronoLatch cln;
   private final ChronoLatch chunkUpdater;
   private final ChronoLatch chunkDiscovery;
   private final KMap<Long, Future<?>> cleanup = new KMap();
   private final ScheduledExecutorService cleanupService;
   private double energy = 25.0D;
   private int entityCount = 0;
   private long charge = 0L;
   private int actuallySpawned = 0;
   private int cooldown = 0;
   private List<Entity> precount = new KList();
   private KSet<Position2> injectBiomes = new KSet(new Position2[0]);

   public IrisWorldManager() {
      super((Engine)null);
      this.cl = null;
      this.ecl = null;
      this.cln = null;
      this.clw = null;
      this.looper = null;
      this.chunkUpdater = null;
      this.chunkDiscovery = null;
      this.cleanupService = null;
      this.id = -1;
   }

   public IrisWorldManager(Engine engine) {
      super(var1);
      this.chunkUpdater = new ChronoLatch(3000L);
      this.chunkDiscovery = new ChronoLatch(5000L);
      this.cln = new ChronoLatch(60000L);
      this.cl = new ChronoLatch(3000L);
      this.ecl = new ChronoLatch(250L);
      this.clw = new ChronoLatch(1000L, true);
      this.cleanupService = Executors.newSingleThreadScheduledExecutor((var1x) -> {
         Thread var2 = new Thread(var1x, "Iris Mantle Cleanup " + this.getTarget().getWorld().name());
         var2.setPriority(1);
         return var2;
      });
      this.id = var1.getCacheID();
      this.energy = 25.0D;
      this.looper = new Looper() {
         protected long loop() {
            if (IrisWorldManager.this.getEngine().isClosed() || IrisWorldManager.this.getEngine().getCacheID() != IrisWorldManager.this.id) {
               this.interrupt();
            }

            if (!IrisWorldManager.this.getEngine().getWorld().hasRealWorld() && IrisWorldManager.this.clw.flip()) {
               IrisWorldManager.this.getEngine().getWorld().tryGetRealWorld();
            }

            if (IrisWorldManager.this.getEngine().getWorld().hasRealWorld()) {
               if (IrisWorldManager.this.getEngine().getWorld().getPlayers().isEmpty()) {
                  return 5000L;
               }

               if (IrisWorldManager.this.chunkUpdater.flip()) {
                  IrisWorldManager.this.updateChunks();
               }

               if (IrisWorldManager.this.chunkDiscovery.flip()) {
                  IrisWorldManager.this.discoverChunks();
               }

               if (IrisWorldManager.this.cln.flip()) {
                  var1.getEngineData().cleanup(IrisWorldManager.this.getEngine());
               }

               if (!IrisSettings.get().getWorld().isMarkerEntitySpawningSystem() && !IrisSettings.get().getWorld().isAnbientEntitySpawningSystem()) {
                  return 3000L;
               }

               IrisWorldManager var10000;
               if (IrisWorldManager.this.getDimension().isInfiniteEnergy()) {
                  var10000 = IrisWorldManager.this;
                  var10000.energy += 1000.0D;
                  IrisWorldManager.this.fixEnergy();
               }

               if (M.ms() < IrisWorldManager.this.charge) {
                  var10000 = IrisWorldManager.this;
                  var10000.energy += 70.0D;
                  IrisWorldManager.this.fixEnergy();
               }

               if (IrisWorldManager.this.precount != null) {
                  IrisWorldManager.this.entityCount = 0;
                  Iterator var1x = IrisWorldManager.this.precount.iterator();

                  while(var1x.hasNext()) {
                     Entity var2 = (Entity)var1x.next();
                     if (var2 instanceof LivingEntity && !var2.isDead()) {
                        ++IrisWorldManager.this.entityCount;
                     }
                  }

                  IrisWorldManager.this.precount = null;
               }

               if (IrisWorldManager.this.energy < 650.0D && IrisWorldManager.this.ecl.flip()) {
                  var10000 = IrisWorldManager.this;
                  var10000.energy *= 1.0D + 0.02D * (Double)M.clip(1.0D - IrisWorldManager.this.getEntitySaturation(), 0.0D, 1.0D);
                  IrisWorldManager.this.fixEnergy();
               }

               IrisWorldManager.this.onAsyncTick();
            }

            return IrisSettings.get().getWorld().getAsyncTickIntervalMS();
         }
      };
      this.looper.setPriority(1);
      this.looper.setName("Iris World Manager " + this.getTarget().getWorld().name());
      this.looper.start();
   }

   private void discoverChunks() {
      Mantle var1 = this.getEngine().getMantle().getMantle();
      Iterator var2 = this.getEngine().getWorld().realWorld().getPlayers().iterator();

      while(var2.hasNext()) {
         Player var3 = (Player)var2.next();
         byte var4 = 1;

         for(int var5 = -var4; var5 <= var4; ++var5) {
            for(int var6 = -var4; var6 <= var4; ++var6) {
               var1.getChunk(var3.getLocation().getChunk()).flag(MantleFlag.DISCOVERED, true);
            }
         }
      }

   }

   private void updateChunks() {
      Iterator var1 = this.getEngine().getWorld().realWorld().getPlayers().iterator();

      while(var1.hasNext()) {
         Player var2 = (Player)var1.next();
         byte var3 = 1;
         Chunk var4 = var2.getLocation().getChunk();

         for(int var5 = -var3; var5 <= var3; ++var5) {
            for(int var6 = -var3; var6 <= var3; ++var6) {
               if (var4.getWorld().isChunkLoaded(var4.getX() + var5, var4.getZ() + var6) && Chunks.isSafe(this.getEngine().getWorld().realWorld(), var4.getX() + var5, var4.getZ() + var6)) {
                  if (IrisSettings.get().getWorld().isPostLoadBlockUpdates()) {
                     this.getEngine().updateChunk(var4.getWorld().getChunkAt(var4.getX() + var5, var4.getZ() + var6));
                  }

                  if (IrisSettings.get().getWorld().isMarkerEntitySpawningSystem()) {
                     Chunk var7 = this.getEngine().getWorld().realWorld().getChunkAt(var4.getX() + var5, var4.getZ() + var6);
                     int var8 = var4.getX() + var5;
                     int var9 = var4.getZ() + var6;
                     J.a(() -> {
                        this.getMantle().raiseFlag(var8, var9, MantleFlag.INITIAL_SPAWNED_MARKER, () -> {
                           J.a(() -> {
                              this.spawnIn(var7, true);
                           }, RNG.r.i(5, 200));
                           this.getSpawnersFromMarkers(var7).forEach((var1, var2) -> {
                              if (!var2.isEmpty()) {
                                 IrisPosition var3 = new IrisPosition(var1.getX(), var1.getY() + this.getEngine().getWorld().minHeight(), var1.getZ());
                                 IrisSpawner var4 = (IrisSpawner)(new KList(var2)).getRandom();
                                 this.spawn(var3, var4, true);
                              }
                           });
                        });
                     });
                  }
               }
            }
         }
      }

   }

   private boolean onAsyncTick() {
      if (this.getEngine().isClosed()) {
         return false;
      } else {
         this.actuallySpawned = 0;
         if (this.energy < 100.0D) {
            J.sleep(200L);
            return false;
         } else if (!this.getEngine().getWorld().hasRealWorld()) {
            Iris.debug("Can't spawn. No real world");
            J.sleep(5000L);
            return false;
         } else {
            double var1 = this.getEntitySaturation();
            if (var1 > IrisSettings.get().getWorld().getTargetSpawnEntitiesPerChunk()) {
               String var7 = Form.pc(var1, 2);
               Iris.debug("Can't spawn. The entity per chunk ratio is at " + var7 + " > 100% (total entities " + this.entityCount + ")");
               J.sleep(5000L);
               return false;
            } else {
               if (this.cl.flip()) {
                  try {
                     J.s(() -> {
                        this.precount = this.getEngine().getWorld().realWorld().getEntities();
                     });
                  } catch (Throwable var6) {
                     this.close();
                  }
               }

               int var3 = RNG.r.i(2, 12);
               Chunk[] var4 = this.getEngine().getWorld().realWorld().getLoadedChunks();

               while(var3-- > 0) {
                  if (var4.length == 0) {
                     Iris.debug("Can't spawn. No chunks!");
                     return false;
                  }

                  Chunk var5 = var4[RNG.r.nextInt(var4.length)];
                  if (var5.isLoaded() && Chunks.isSafe(var5.getWorld(), var5.getX(), var5.getZ())) {
                     this.spawnIn(var5, false);
                  }
               }

               this.energy -= (double)this.actuallySpawned / 2.0D;
               return this.actuallySpawned > 0;
            }
         }
      }
   }

   private void fixEnergy() {
      this.energy = (Double)M.clip(this.energy, 1.0D, this.getDimension().getMaximumEnergy());
   }

   private void spawnIn(Chunk c, boolean initial) {
      if (!this.getEngine().isClosed()) {
         if (var2) {
            ++this.energy;
         }

         if (IrisSettings.get().getWorld().isMarkerEntitySpawningSystem()) {
            this.getSpawnersFromMarkers(var1).forEach((var2x, var3x) -> {
               if (!var3x.isEmpty()) {
                  IrisPosition var4 = new IrisPosition(var2x.getX(), var2x.getY() + this.getEngine().getWorld().minHeight(), var2x.getZ());
                  IrisSpawner var5 = (IrisSpawner)(new KList(var3x)).getRandom();
                  this.spawn(var4, var5, false);
                  J.a(() -> {
                     this.getMantle().raiseFlag(var1.getX(), var1.getZ(), MantleFlag.INITIAL_SPAWNED_MARKER, () -> {
                        this.spawn(var4, var5, true);
                     });
                  });
               }
            });
         }

         if (IrisSettings.get().getWorld().isAnbientEntitySpawningSystem()) {
            Predicate var3 = (var2x) -> {
               return var2x.canSpawn(this.getEngine(), var1.getX(), var1.getZ());
            };
            IrisWorldManager.ChunkCounter var4 = new IrisWorldManager.ChunkCounter(var1.getEntities());
            IrisBiome var5 = this.getEngine().getSurfaceBiome(var1);
            IrisEntitySpawn var6 = (IrisEntitySpawn)this.spawnRandomly((List)Stream.concat(this.getData().getSpawnerLoader().loadAll(this.getDimension().getEntitySpawners()).shuffleCopy(RNG.r).stream().filter(var3).filter((var1x) -> {
               return var1x.isValid(var5);
            }), Stream.concat(this.getData().getSpawnerLoader().loadAll(this.getEngine().getRegion(var1.getX() << 4, var1.getZ() << 4).getEntitySpawners()).shuffleCopy(RNG.r).stream().filter(var3), this.getData().getSpawnerLoader().loadAll(this.getEngine().getSurfaceBiome(var1.getX() << 4, var1.getZ() << 4).getEntitySpawners()).shuffleCopy(RNG.r).stream().filter(var3))).filter(var4).flatMap((var2x) -> {
               return this.stream(var2x, var2);
            }).collect(Collectors.toList())).getRandom();
            if (var6 != null && var6.getReferenceSpawner() != null) {
               try {
                  this.spawn(var1, var6);
               } catch (Throwable var8) {
                  J.s(() -> {
                     this.spawn(var1, var6);
                  });
               }

            }
         }
      }
   }

   private void spawn(Chunk c, IrisEntitySpawn i) {
      IrisSpawner var3 = var2.getReferenceSpawner();
      int var4 = var2.spawn(this.getEngine(), var1, RNG.r);
      this.actuallySpawned += var4;
      if (var4 > 0) {
         var3.spawn(this.getEngine(), var1.getX(), var1.getZ());
         this.energy -= (double)var4 * var2.getEnergyMultiplier() * var3.getEnergyMultiplier() * 1.0D;
      }

   }

   private void spawn(IrisPosition pos, IrisEntitySpawn i) {
      IrisSpawner var3 = var2.getReferenceSpawner();
      if (var3.canSpawn(this.getEngine(), var1.getX() >> 4, var1.getZ() >> 4)) {
         int var4 = var2.spawn(this.getEngine(), var1, RNG.r);
         this.actuallySpawned += var4;
         if (var4 > 0) {
            var3.spawn(this.getEngine(), var1.getX() >> 4, var1.getZ() >> 4);
            this.energy -= (double)var4 * var2.getEnergyMultiplier() * var3.getEnergyMultiplier() * 1.0D;
         }

      }
   }

   private Stream<IrisEntitySpawn> stream(IrisSpawner s, boolean initial) {
      Iterator var3 = (var2 ? var1.getInitialSpawns() : var1.getSpawns()).iterator();

      while(var3.hasNext()) {
         IrisEntitySpawn var4 = (IrisEntitySpawn)var3.next();
         var4.setReferenceSpawner(var1);
         var4.setReferenceMarker(var1.getReferenceMarker());
      }

      return (var2 ? var1.getInitialSpawns() : var1.getSpawns()).stream();
   }

   private KList<IrisEntitySpawn> spawnRandomly(List<IrisEntitySpawn> types) {
      KList var2 = new KList();
      int var3 = 0;

      Iterator var4;
      IrisEntitySpawn var5;
      for(var4 = var1.iterator(); var4.hasNext(); var3 += IRare.get(var5)) {
         var5 = (IrisEntitySpawn)var4.next();
      }

      var4 = var1.iterator();

      while(var4.hasNext()) {
         var5 = (IrisEntitySpawn)var4.next();
         var2.addMultiple(var5, var3 / IRare.get(var5));
      }

      return var2;
   }

   public void onTick() {
   }

   public void onSave() {
      this.getEngine().getMantle().save();
   }

   public void requestBiomeInject(Position2 p) {
      this.injectBiomes.add(var1);
   }

   public void onChunkLoad(Chunk e, boolean generated) {
      if (!this.getEngine().isClosed()) {
         int var3 = var1.getX();
         int var4 = var1.getZ();
         Long var5 = Cache.key(var1);
         this.cleanup.put(var5, this.cleanupService.schedule(() -> {
            this.cleanup.remove(var5);
            this.energy += 0.3D;
            this.fixEnergy();
            this.getEngine().cleanupMantleChunk(var3, var4);
         }, Math.max((long)IrisSettings.get().getPerformance().mantleCleanupDelay * 50L, 0L), TimeUnit.MILLISECONDS));
         if (var2) {
            if (!IrisSettings.get().getGenerator().earlyCustomBlocks) {
               return;
            }

            Iris.tickets.addTicket(var1);
            J.s(() -> {
               MantleChunk var2 = this.getMantle().getChunk(var1).use();
               int var3 = this.getTarget().getWorld().minHeight();

               try {
                  var2.raiseFlagUnchecked(MantleFlag.CUSTOM, () -> {
                     var2.iterate(Identifier.class, (var3x, var4, var5, var6) -> {
                        ((ExternalDataSVC)Iris.service(ExternalDataSVC.class)).processUpdate(this.getEngine(), var1.getBlock(var3x & 15, var4 + var3, var5 & 15), var6);
                     });
                  });
               } finally {
                  var2.release();
                  Iris.tickets.removeTicket(var1);
               }

            }, RNG.r.i(20, 60));
         }

      }
   }

   public void onChunkUnload(Chunk e) {
      Future var2 = (Future)this.cleanup.remove(Cache.key(var1));
      if (var2 != null) {
         var2.cancel(false);
      }

   }

   private void spawn(IrisPosition block, IrisSpawner spawner, boolean initial) {
      if (!this.getEngine().isClosed()) {
         if (var2 != null) {
            KList var4 = var3 ? var2.getInitialSpawns() : var2.getSpawns();
            if (!var4.isEmpty()) {
               IrisEntitySpawn var5 = (IrisEntitySpawn)this.spawnRandomly(var4).getRandom();
               var5.setReferenceSpawner(var2);
               var5.setReferenceMarker(var2.getReferenceMarker());
               this.spawn(var1, var5);
            }
         }
      }
   }

   public Mantle getMantle() {
      return this.getEngine().getMantle().getMantle();
   }

   public void chargeEnergy() {
      this.charge = M.ms() + 3000L;
   }

   public void teleportAsync(PlayerTeleportEvent e) {
      if (IrisSettings.get().getWorld().getAsyncTeleport().isEnabled()) {
         var1.setCancelled(true);
         this.warmupAreaAsync(var1.getPlayer(), var1.getTo(), () -> {
            J.s(() -> {
               this.ignoreTP.set(true);
               var1.getPlayer().teleport(var1.getTo(), var1.getCause());
               this.ignoreTP.set(false);
            });
         });
      }

   }

   private void warmupAreaAsync(Player player, Location to, Runnable r) {
      J.a(() -> {
         int var4 = IrisSettings.get().getWorld().getAsyncTeleport().getLoadViewDistance();
         KList var5 = new KList();

         for(int var6 = -var4; var6 <= var4; ++var6) {
            for(int var7 = -var4; var7 <= var4; ++var7) {
               if (var2.getWorld().isChunkLoaded((var2.getBlockX() >> 4) + var6, (var2.getBlockZ() >> 4) + var7)) {
                  var5.add((Object)CompletableFuture.completedFuture((Object)null));
               } else {
                  var5.add((Object)MultiBurst.burst.completeValue(() -> {
                     return (Chunk)PaperLib.getChunkAtAsync(var2.getWorld(), (var2.getBlockX() >> 4) + var6, (var2.getBlockZ() >> 4) + var7, true, IrisSettings.get().getWorld().getAsyncTeleport().isUrgent()).get();
                  }));
               }
            }
         }

         (new QueueJob<Future<Chunk>>(this) {
            public void execute(Future<Chunk> chunkFuture) {
               try {
                  var1.get();
               } catch (ExecutionException | InterruptedException var3) {
               }

            }

            public String getName() {
               return "Loading Chunks";
            }
         }).queue(var5).execute(new VolmitSender(var1), true, var3);
      });
   }

   public Map<IrisPosition, KSet<IrisSpawner>> getSpawnersFromMarkers(Chunk c) {
      KMap var2 = new KMap();
      KSet var3 = new KSet(new IrisPosition[0]);
      this.getMantle().iterateChunk(var1.getX(), var1.getZ(), MatterMarker.class, (var4x, var5x, var6, var7) -> {
         if (!var7.getTag().equals("cave_floor") && !var7.getTag().equals("cave_ceiling")) {
            IrisMarker var8 = (IrisMarker)this.getData().getMarkerLoader().load(var7.getTag());
            IrisPosition var9 = new IrisPosition((var1.getX() << 4) + var4x, var5x, (var1.getZ() << 4) + var6);
            if (var8.isEmptyAbove()) {
               AtomicBoolean var10 = new AtomicBoolean(false);

               try {
                  J.sfut(() -> {
                     if (var1.getBlock(var4x, var5x + 1, var6).getBlockData().getMaterial().isSolid() || var1.getBlock(var4x, var5x + 2, var6).getBlockData().getMaterial().isSolid()) {
                        var10.set(true);
                     }

                  }).get();
               } catch (ExecutionException | InterruptedException var13) {
                  var13.printStackTrace();
               }

               if (var10.get()) {
                  var3.add(var9);
                  return;
               }
            }

            Iterator var14 = var8.getSpawners().iterator();

            while(var14.hasNext()) {
               String var11 = (String)var14.next();
               IrisSpawner var12 = (IrisSpawner)this.getData().getSpawnerLoader().load(var11);
               if (var12 == null) {
                  Iris.error("Cannot load spawner: " + var11 + " for marker on " + this.getName());
               } else {
                  var12.setReferenceMarker(var8);
                  if (var12 != null) {
                     ((KSet)var2.computeIfAbsent(var9, (var0) -> {
                        return new KSet(new IrisSpawner[0]);
                     })).add(var12);
                  }
               }
            }

         }
      });
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         IrisPosition var5 = (IrisPosition)var4.next();
         this.getEngine().getMantle().getMantle().remove(var5.getX(), var5.getY(), var5.getZ(), MatterMarker.class);
      }

      return var2;
   }

   public void onBlockBreak(BlockBreakEvent e) {
      if (var1.getBlock().getWorld().equals(this.getTarget().getWorld().realWorld())) {
         J.a(() -> {
            MatterMarker var2 = (MatterMarker)this.getMantle().get(var1.getBlock().getX(), var1.getBlock().getY(), var1.getBlock().getZ(), MatterMarker.class);
            if (var2 != null) {
               if (var2.getTag().equals("cave_floor") || var2.getTag().equals("cave_ceiling")) {
                  return;
               }

               IrisMarker var3 = (IrisMarker)this.getData().getMarkerLoader().load(var2.getTag());
               if (var3 == null || var3.isRemoveOnChange()) {
                  this.getMantle().remove(var1.getBlock().getX(), var1.getBlock().getY(), var1.getBlock().getZ(), MatterMarker.class);
               }
            }

         });
         KList var2 = new KList();
         IrisBiome var3 = this.getEngine().getBiome(var1.getBlock().getLocation().clone().subtract(0.0D, (double)this.getEngine().getWorld().minHeight(), 0.0D));
         List var4 = this.filterDrops(var3.getBlockDrops(), var1, this.getData());
         if (var4.stream().noneMatch(IrisBlockDrops::isSkipParents)) {
            IrisRegion var5 = this.getEngine().getRegion(var1.getBlock().getLocation());
            var4.addAll(this.filterDrops(var5.getBlockDrops(), var1, this.getData()));
            var4.addAll(this.filterDrops(this.getEngine().getDimension().getBlockDrops(), var1, this.getData()));
         }

         var4.forEach((var1x) -> {
            var1x.fillDrops(false, var2);
         });
         if (var4.stream().anyMatch(IrisBlockDrops::isReplaceVanillaDrops)) {
            var1.setDropItems(false);
         }

         if (var2.isNotEmpty()) {
            World var6 = var1.getBlock().getWorld();
            J.s(() -> {
               var2.forEach((var2x) -> {
                  var6.dropItemNaturally(var1.getBlock().getLocation().clone().add(0.5D, 0.5D, 0.5D), var2x);
               });
            });
         }
      }

   }

   private List<IrisBlockDrops> filterDrops(KList<IrisBlockDrops> drops, BlockBreakEvent e, IrisData data) {
      return new KList(var1.stream().filter((var2x) -> {
         return var2x.shouldDropFor(var2.getBlock().getBlockData(), var3);
      }).toList());
   }

   public void onBlockPlace(BlockPlaceEvent e) {
   }

   public void close() {
      super.close();
      this.looper.interrupt();
   }

   public int getChunkCount() {
      return this.getEngine().getWorld().realWorld().getLoadedChunks().length;
   }

   public double getEntitySaturation() {
      return !this.getEngine().getWorld().hasRealWorld() ? 1.0D : (double)this.entityCount / (double)(this.getEngine().getWorld().realWorld().getLoadedChunks().length + 1) * 1.28D;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisWorldManager)) {
         return false;
      } else {
         IrisWorldManager var2 = (IrisWorldManager)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (!super.equals(var1)) {
            return false;
         } else if (this.getId() != var2.getId()) {
            return false;
         } else if (Double.compare(this.getEnergy(), var2.getEnergy()) != 0) {
            return false;
         } else if (this.getEntityCount() != var2.getEntityCount()) {
            return false;
         } else if (this.getCharge() != var2.getCharge()) {
            return false;
         } else if (this.getActuallySpawned() != var2.getActuallySpawned()) {
            return false;
         } else if (this.getCooldown() != var2.getCooldown()) {
            return false;
         } else {
            label172: {
               Looper var3 = this.getLooper();
               Looper var4 = var2.getLooper();
               if (var3 == null) {
                  if (var4 == null) {
                     break label172;
                  }
               } else if (var3.equals(var4)) {
                  break label172;
               }

               return false;
            }

            KList var5 = this.getUpdateQueue();
            KList var6 = var2.getUpdateQueue();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            ChronoLatch var7 = this.getCl();
            ChronoLatch var8 = var2.getCl();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            label151: {
               ChronoLatch var9 = this.getClw();
               ChronoLatch var10 = var2.getClw();
               if (var9 == null) {
                  if (var10 == null) {
                     break label151;
                  }
               } else if (var9.equals(var10)) {
                  break label151;
               }

               return false;
            }

            label144: {
               ChronoLatch var11 = this.getEcl();
               ChronoLatch var12 = var2.getEcl();
               if (var11 == null) {
                  if (var12 == null) {
                     break label144;
                  }
               } else if (var11.equals(var12)) {
                  break label144;
               }

               return false;
            }

            ChronoLatch var13 = this.getCln();
            ChronoLatch var14 = var2.getCln();
            if (var13 == null) {
               if (var14 != null) {
                  return false;
               }
            } else if (!var13.equals(var14)) {
               return false;
            }

            ChronoLatch var15 = this.getChunkUpdater();
            ChronoLatch var16 = var2.getChunkUpdater();
            if (var15 == null) {
               if (var16 != null) {
                  return false;
               }
            } else if (!var15.equals(var16)) {
               return false;
            }

            label123: {
               ChronoLatch var17 = this.getChunkDiscovery();
               ChronoLatch var18 = var2.getChunkDiscovery();
               if (var17 == null) {
                  if (var18 == null) {
                     break label123;
                  }
               } else if (var17.equals(var18)) {
                  break label123;
               }

               return false;
            }

            KMap var19 = this.getCleanup();
            KMap var20 = var2.getCleanup();
            if (var19 == null) {
               if (var20 != null) {
                  return false;
               }
            } else if (!var19.equals(var20)) {
               return false;
            }

            ScheduledExecutorService var21 = this.getCleanupService();
            ScheduledExecutorService var22 = var2.getCleanupService();
            if (var21 == null) {
               if (var22 != null) {
                  return false;
               }
            } else if (!var21.equals(var22)) {
               return false;
            }

            label102: {
               List var23 = this.getPrecount();
               List var24 = var2.getPrecount();
               if (var23 == null) {
                  if (var24 == null) {
                     break label102;
                  }
               } else if (var23.equals(var24)) {
                  break label102;
               }

               return false;
            }

            KSet var25 = this.getInjectBiomes();
            KSet var26 = var2.getInjectBiomes();
            if (var25 == null) {
               if (var26 != null) {
                  return false;
               }
            } else if (!var25.equals(var26)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisWorldManager;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      int var2 = super.hashCode();
      var2 = var2 * 59 + this.getId();
      long var3 = Double.doubleToLongBits(this.getEnergy());
      var2 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      var2 = var2 * 59 + this.getEntityCount();
      long var5 = this.getCharge();
      var2 = var2 * 59 + (int)(var5 >>> 32 ^ var5);
      var2 = var2 * 59 + this.getActuallySpawned();
      var2 = var2 * 59 + this.getCooldown();
      Looper var7 = this.getLooper();
      var2 = var2 * 59 + (var7 == null ? 43 : var7.hashCode());
      KList var8 = this.getUpdateQueue();
      var2 = var2 * 59 + (var8 == null ? 43 : var8.hashCode());
      ChronoLatch var9 = this.getCl();
      var2 = var2 * 59 + (var9 == null ? 43 : var9.hashCode());
      ChronoLatch var10 = this.getClw();
      var2 = var2 * 59 + (var10 == null ? 43 : var10.hashCode());
      ChronoLatch var11 = this.getEcl();
      var2 = var2 * 59 + (var11 == null ? 43 : var11.hashCode());
      ChronoLatch var12 = this.getCln();
      var2 = var2 * 59 + (var12 == null ? 43 : var12.hashCode());
      ChronoLatch var13 = this.getChunkUpdater();
      var2 = var2 * 59 + (var13 == null ? 43 : var13.hashCode());
      ChronoLatch var14 = this.getChunkDiscovery();
      var2 = var2 * 59 + (var14 == null ? 43 : var14.hashCode());
      KMap var15 = this.getCleanup();
      var2 = var2 * 59 + (var15 == null ? 43 : var15.hashCode());
      ScheduledExecutorService var16 = this.getCleanupService();
      var2 = var2 * 59 + (var16 == null ? 43 : var16.hashCode());
      List var17 = this.getPrecount();
      var2 = var2 * 59 + (var17 == null ? 43 : var17.hashCode());
      KSet var18 = this.getInjectBiomes();
      var2 = var2 * 59 + (var18 == null ? 43 : var18.hashCode());
      return var2;
   }

   @Generated
   public Looper getLooper() {
      return this.looper;
   }

   @Generated
   public int getId() {
      return this.id;
   }

   @Generated
   public KList<Runnable> getUpdateQueue() {
      return this.updateQueue;
   }

   @Generated
   public ChronoLatch getCl() {
      return this.cl;
   }

   @Generated
   public ChronoLatch getClw() {
      return this.clw;
   }

   @Generated
   public ChronoLatch getEcl() {
      return this.ecl;
   }

   @Generated
   public ChronoLatch getCln() {
      return this.cln;
   }

   @Generated
   public ChronoLatch getChunkUpdater() {
      return this.chunkUpdater;
   }

   @Generated
   public ChronoLatch getChunkDiscovery() {
      return this.chunkDiscovery;
   }

   @Generated
   public KMap<Long, Future<?>> getCleanup() {
      return this.cleanup;
   }

   @Generated
   public ScheduledExecutorService getCleanupService() {
      return this.cleanupService;
   }

   @Generated
   public double getEnergy() {
      return this.energy;
   }

   @Generated
   public int getEntityCount() {
      return this.entityCount;
   }

   @Generated
   public long getCharge() {
      return this.charge;
   }

   @Generated
   public int getActuallySpawned() {
      return this.actuallySpawned;
   }

   @Generated
   public int getCooldown() {
      return this.cooldown;
   }

   @Generated
   public List<Entity> getPrecount() {
      return this.precount;
   }

   @Generated
   public KSet<Position2> getInjectBiomes() {
      return this.injectBiomes;
   }

   @Generated
   public void setEnergy(final double energy) {
      this.energy = var1;
   }

   @Generated
   public void setEntityCount(final int entityCount) {
      this.entityCount = var1;
   }

   @Generated
   public void setCharge(final long charge) {
      this.charge = var1;
   }

   @Generated
   public void setActuallySpawned(final int actuallySpawned) {
      this.actuallySpawned = var1;
   }

   @Generated
   public void setCooldown(final int cooldown) {
      this.cooldown = var1;
   }

   @Generated
   public void setPrecount(final List<Entity> precount) {
      this.precount = var1;
   }

   @Generated
   public void setInjectBiomes(final KSet<Position2> injectBiomes) {
      this.injectBiomes = var1;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getLooper());
      return "IrisWorldManager(looper=" + var10000 + ", id=" + this.getId() + ", updateQueue=" + String.valueOf(this.getUpdateQueue()) + ", cl=" + String.valueOf(this.getCl()) + ", clw=" + String.valueOf(this.getClw()) + ", ecl=" + String.valueOf(this.getEcl()) + ", cln=" + String.valueOf(this.getCln()) + ", chunkUpdater=" + String.valueOf(this.getChunkUpdater()) + ", chunkDiscovery=" + String.valueOf(this.getChunkDiscovery()) + ", cleanup=" + String.valueOf(this.getCleanup()) + ", cleanupService=" + String.valueOf(this.getCleanupService()) + ", energy=" + this.getEnergy() + ", entityCount=" + this.getEntityCount() + ", charge=" + this.getCharge() + ", actuallySpawned=" + this.getActuallySpawned() + ", cooldown=" + this.getCooldown() + ", precount=" + String.valueOf(this.getPrecount()) + ", injectBiomes=" + String.valueOf(this.getInjectBiomes()) + ")";
   }

   private static class ChunkCounter implements Predicate<IrisSpawner> {
      private final Entity[] entities;
      private transient int index = 0;
      private transient int count = 0;

      public boolean test(IrisSpawner spawner) {
         int var2 = var1.getMaxEntitiesPerChunk();
         if (var2 <= this.count) {
            return false;
         } else {
            do {
               if (this.index >= this.entities.length) {
                  return true;
               }
            } while(!(this.entities[this.index++] instanceof LivingEntity) || ++this.count < var2);

            return false;
         }
      }

      @Generated
      public ChunkCounter(final Entity[] entities) {
         this.entities = var1;
      }

      @Generated
      public Entity[] getEntities() {
         return this.entities;
      }

      @Generated
      public int getIndex() {
         return this.index;
      }

      @Generated
      public int getCount() {
         return this.count;
      }

      @Generated
      public void setIndex(final int index) {
         this.index = var1;
      }

      @Generated
      public void setCount(final int count) {
         this.count = var1;
      }

      @Generated
      public boolean equals(final Object o) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof IrisWorldManager.ChunkCounter)) {
            return false;
         } else {
            IrisWorldManager.ChunkCounter var2 = (IrisWorldManager.ChunkCounter)var1;
            if (!var2.canEqual(this)) {
               return false;
            } else {
               return Arrays.deepEquals(this.getEntities(), var2.getEntities());
            }
         }
      }

      @Generated
      protected boolean canEqual(final Object other) {
         return var1 instanceof IrisWorldManager.ChunkCounter;
      }

      @Generated
      public int hashCode() {
         boolean var1 = true;
         byte var2 = 1;
         int var3 = var2 * 59 + Arrays.deepHashCode(this.getEntities());
         return var3;
      }

      @Generated
      public String toString() {
         String var10000 = Arrays.deepToString(this.getEntities());
         return "IrisWorldManager.ChunkCounter(entities=" + var10000 + ", index=" + this.getIndex() + ", count=" + this.getCount() + ")";
      }
   }
}
