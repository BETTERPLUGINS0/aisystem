package com.volmit.iris.engine.framework;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.events.IrisLootEvent;
import com.volmit.iris.core.gui.components.RenderType;
import com.volmit.iris.core.gui.components.Renderer;
import com.volmit.iris.core.link.Identifier;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.loader.IrisRegistrant;
import com.volmit.iris.core.nms.container.BlockPos;
import com.volmit.iris.core.nms.container.Pair;
import com.volmit.iris.core.pregenerator.ChunkUpdater;
import com.volmit.iris.core.scripting.environment.EngineEnvironment;
import com.volmit.iris.core.service.ExternalDataSVC;
import com.volmit.iris.engine.IrisComplex;
import com.volmit.iris.engine.data.cache.Cache;
import com.volmit.iris.engine.data.chunk.TerrainChunk;
import com.volmit.iris.engine.mantle.EngineMantle;
import com.volmit.iris.engine.object.InventorySlotType;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisColor;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.engine.object.IrisEngineData;
import com.volmit.iris.engine.object.IrisJigsawPiece;
import com.volmit.iris.engine.object.IrisJigsawStructure;
import com.volmit.iris.engine.object.IrisJigsawStructurePlacement;
import com.volmit.iris.engine.object.IrisLootMode;
import com.volmit.iris.engine.object.IrisLootReference;
import com.volmit.iris.engine.object.IrisLootTable;
import com.volmit.iris.engine.object.IrisObject;
import com.volmit.iris.engine.object.IrisObjectPlacement;
import com.volmit.iris.engine.object.IrisPosition;
import com.volmit.iris.engine.object.IrisRegion;
import com.volmit.iris.engine.object.IrisWorld;
import com.volmit.iris.engine.object.TileData;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.context.ChunkContext;
import com.volmit.iris.util.context.IrisContext;
import com.volmit.iris.util.data.B;
import com.volmit.iris.util.data.DataProvider;
import com.volmit.iris.util.data.IrisCustomData;
import com.volmit.iris.util.documentation.BlockCoordinates;
import com.volmit.iris.util.documentation.ChunkCoordinates;
import com.volmit.iris.util.format.C;
import com.volmit.iris.util.function.Function2;
import com.volmit.iris.util.hunk.Hunk;
import com.volmit.iris.util.mantle.Mantle;
import com.volmit.iris.util.mantle.MantleChunk;
import com.volmit.iris.util.mantle.flag.MantleFlag;
import com.volmit.iris.util.math.BlockPosition;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.math.Position2;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.matter.MatterCavern;
import com.volmit.iris.util.matter.MatterUpdate;
import com.volmit.iris.util.matter.TileWrapper;
import com.volmit.iris.util.matter.slices.container.JigsawPieceContainer;
import com.volmit.iris.util.paper.PaperLib;
import com.volmit.iris.util.parallel.BurstExecutor;
import com.volmit.iris.util.parallel.MultiBurst;
import com.volmit.iris.util.reflect.W;
import com.volmit.iris.util.scheduling.ChronoLatch;
import com.volmit.iris.util.scheduling.J;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import com.volmit.iris.util.stream.ProceduralStream;
import java.awt.Color;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface Engine extends DataProvider, Fallible, LootProvider, BlockUpdater, Renderer, Hotloadable {
   IrisComplex getComplex();

   EngineMode getMode();

   int getBlockUpdatesPerSecond();

   void printMetrics(CommandSender sender);

   EngineMantle getMantle();

   void hotloadSilently();

   void hotloadComplex();

   void recycle();

   void close();

   IrisContext getContext();

   EngineEnvironment getExecution();

   double getMaxBiomeObjectDensity();

   double getMaxBiomeDecoratorDensity();

   double getMaxBiomeLayerDensity();

   boolean isClosed();

   EngineWorldManager getWorldManager();

   default UUID getBiomeID(int x, int z) {
      return (UUID)this.getComplex().getBaseBiomeIDStream().get((double)x, (double)z);
   }

   int getParallelism();

   void setParallelism(int parallelism);

   EngineTarget getTarget();

   default int getMaxHeight() {
      return this.getTarget().getWorld().maxHeight();
   }

   default int getMinHeight() {
      return this.getTarget().getWorld().minHeight();
   }

   default void setMinHeight(int min) {
      this.getTarget().getWorld().minHeight(min);
   }

   @BlockCoordinates
   default void generate(int x, int z, TerrainChunk tc, boolean multicore) throws WrongEngineBroException {
      this.generate(x, z, Hunk.view((ChunkData)tc), Hunk.view(tc, tc.getMinHeight(), tc.getMaxHeight()), multicore);
   }

   @BlockCoordinates
   void generate(int x, int z, Hunk<BlockData> blocks, Hunk<Biome> biomes, boolean multicore) throws WrongEngineBroException;

   EngineMetrics getMetrics();

   default void save() {
      this.getMantle().save();
      this.getWorldManager().onSave();
      this.saveEngineData();
   }

   default void saveNow() {
      this.getMantle().saveAllNow();
      this.saveEngineData();
   }

   SeedManager getSeedManager();

   void saveEngineData();

   default String getName() {
      return this.getDimension().getName();
   }

   default IrisData getData() {
      return this.getTarget().getData();
   }

   default IrisWorld getWorld() {
      return this.getTarget().getWorld();
   }

   default IrisDimension getDimension() {
      return this.getTarget().getDimension();
   }

   @BlockCoordinates
   default Color draw(double x, double z) {
      IrisRegion region = this.getRegion((int)x, (int)z);
      IrisBiome biome = this.getSurfaceBiome((int)x, (int)z);
      int height = this.getHeight((int)x, (int)z);
      double heightFactor = M.lerpInverse(0.0D, (double)this.getTarget().getHeight(), (double)height);
      Color irc = region.getColor(this.getComplex(), RenderType.BIOME);
      Color ibc = biome.getColor(this, RenderType.BIOME);
      Color rc = irc != null ? irc : Color.GREEN.darker();
      Color bc = ibc != null ? ibc : (biome.isAquatic() ? Color.BLUE : Color.YELLOW);
      Color f = IrisColor.blend(rc, bc, bc, Color.getHSBColor(0.0F, 0.0F, (float)heightFactor));
      return IrisColor.blend(rc, bc, bc, Color.getHSBColor(0.0F, 0.0F, (float)heightFactor));
   }

   @BlockCoordinates
   default IrisRegion getRegion(int x, int z) {
      return (IrisRegion)this.getComplex().getRegionStream().get((double)x, (double)z);
   }

   void generateMatter(int x, int z, boolean multicore, ChunkContext context);

   @BlockCoordinates
   default IrisBiome getCaveOrMantleBiome(int x, int y, int z) {
      MatterCavern m = (MatterCavern)this.getMantle().getMantle().get(x, y, z, MatterCavern.class);
      if (m != null && m.getCustomBiome() != null && !m.getCustomBiome().isEmpty()) {
         IrisBiome biome = (IrisBiome)this.getData().getBiomeLoader().load(m.getCustomBiome());
         if (biome != null) {
            return biome;
         }
      }

      return this.getCaveBiome(x, z);
   }

   @ChunkCoordinates
   Set<String> getObjectsAt(int x, int z);

   @ChunkCoordinates
   Set<Pair<String, BlockPos>> getPOIsAt(int x, int z);

   @ChunkCoordinates
   IrisJigsawStructure getStructureAt(int x, int z);

   @BlockCoordinates
   IrisJigsawStructure getStructureAt(int x, int y, int z);

   @BlockCoordinates
   default IrisBiome getCaveBiome(int x, int z) {
      return (IrisBiome)this.getComplex().getCaveBiomeStream().get((double)x, (double)z);
   }

   @BlockCoordinates
   default IrisBiome getSurfaceBiome(int x, int z) {
      return (IrisBiome)this.getComplex().getTrueBiomeStream().get((double)x, (double)z);
   }

   @BlockCoordinates
   default int getHeight(int x, int z) {
      return this.getHeight(x, z, true);
   }

   @BlockCoordinates
   default int getHeight(int x, int z, boolean ignoreFluid) {
      return this.getMantle().getHighest(x, z, this.getData(), ignoreFluid);
   }

   @BlockCoordinates
   default void catchBlockUpdates(int x, int y, int z, BlockData data) {
      if (data != null) {
         if (B.isUpdatable(data)) {
            this.getMantle().updateBlock(x, y, z);
         }

         if (data instanceof IrisCustomData) {
            this.getMantle().getMantle().flag(x >> 4, z >> 4, MantleFlag.CUSTOM_ACTIVE, true);
         }

      }
   }

   void blockUpdatedMetric();

   @ChunkCoordinates
   default void updateChunk(Chunk c) {
      for(int x = -1; x <= 1; ++x) {
         for(int z = -1; z <= 1; ++z) {
            if (!c.getWorld().isChunkLoaded(c.getX() + x, c.getZ() + z)) {
               String msg = "Chunk %s, %s [%s, %s] is not loaded".formatted(new Object[]{c.getX() + x, c.getZ() + z, x, z});
               if (W.getStack().getCallerClass().equals(ChunkUpdater.class)) {
                  Iris.warn(msg);
               } else {
                  Iris.debug(msg);
               }

               return;
            }
         }
      }

      Mantle mantle = this.getMantle().getMantle();
      if (!mantle.isLoaded(c)) {
         int var10000 = c.getX();
         String msg = "Mantle Chunk " + var10000 + c.getX() + " is not loaded";
         if (W.getStack().getCallerClass().equals(ChunkUpdater.class)) {
            Iris.warn(msg);
         } else {
            Iris.debug(msg);
         }

      } else {
         MantleChunk chunk = mantle.getChunk(c).use();

         try {
            Semaphore semaphore = new Semaphore(1024);
            chunk.raiseFlagUnchecked(MantleFlag.ETCHED, () -> {
               chunk.raiseFlagUnchecked(MantleFlag.TILE, run(semaphore, () -> {
                  chunk.iterate(TileWrapper.class, (x, y, z, v) -> {
                     Block block = c.getBlock(x & 15, y + this.getWorld().minHeight(), z & 15);
                     if (!TileData.setTileState(block, v.getData())) {
                        Iris.warn("Failed to set tile entity data at [%d %d %d | %s] for tile %s!", block.getX(), block.getY(), block.getZ(), block.getType().getKey(), v.getData().getMaterial().getKey());
                     }

                  });
               }, 0));
               chunk.raiseFlagUnchecked(MantleFlag.CUSTOM, run(semaphore, () -> {
                  chunk.iterate(Identifier.class, (x, y, z, v) -> {
                     ((ExternalDataSVC)Iris.service(ExternalDataSVC.class)).processUpdate(this, c.getBlock(x & 15, y + this.getWorld().minHeight(), z & 15), v);
                  });
               }, 0));
               chunk.raiseFlagUnchecked(MantleFlag.UPDATE, run(semaphore, () -> {
                  PrecisionStopwatch p = PrecisionStopwatch.start();
                  int[][] grid = new int[16][16];

                  int x;
                  for(int xx = 0; xx < 16; ++xx) {
                     for(x = 0; x < 16; ++x) {
                        grid[xx][x] = Integer.MIN_VALUE;
                     }
                  }

                  RNG rng = new RNG(Cache.key(c.getX(), c.getZ()));
                  chunk.iterate(MatterCavern.class, (xxx, yf, zx, v) -> {
                     int y = yf + this.getWorld().minHeight();
                     xxx = xxx & 15;
                     zx = zx & 15;
                     Block block = c.getBlock(xxx, y, zx);
                     if (B.isFluid(block.getBlockData())) {
                        boolean u = B.isAir(block.getRelative(BlockFace.DOWN).getBlockData()) || B.isAir(block.getRelative(BlockFace.WEST).getBlockData()) || B.isAir(block.getRelative(BlockFace.EAST).getBlockData()) || B.isAir(block.getRelative(BlockFace.SOUTH).getBlockData()) || B.isAir(block.getRelative(BlockFace.NORTH).getBlockData());
                        if (u) {
                           grid[xxx][zx] = Math.max(grid[xxx][zx], y);
                        }

                     }
                  });

                  for(x = 0; x < 16; ++x) {
                     for(int z = 0; z < 16; ++z) {
                        if (grid[x][z] != Integer.MIN_VALUE) {
                           this.update(x, grid[x][z], z, c, chunk, rng);
                        }
                     }
                  }

                  chunk.iterate(MatterUpdate.class, (xxx, yf, zx, v) -> {
                     int y = yf + this.getWorld().minHeight();
                     if (v != null && v.isUpdate()) {
                        this.update(xxx, y, zx, c, chunk, rng);
                     }

                  });
                  chunk.deleteSlices(MatterUpdate.class);
                  this.getMetrics().getUpdates().put(p.getMilliseconds());
               }, RNG.r.i(1, 20)));
            });
            chunk.raiseFlagUnchecked(MantleFlag.SCRIPT, () -> {
               KList<String> scripts = this.getDimension().getChunkUpdateScripts();
               if (scripts != null && !scripts.isEmpty()) {
                  Iterator var5 = scripts.iterator();

                  while(var5.hasNext()) {
                     String script = (String)var5.next();
                     this.getExecution().updateChunk(script, chunk, c, (delay, task) -> {
                        return run(semaphore, task, delay);
                     });
                  }

               }
            });

            try {
               semaphore.acquire(1024);
            } catch (InterruptedException var9) {
            }
         } finally {
            chunk.release();
         }

      }
   }

   private static Runnable run(Semaphore semaphore, Runnable runnable, int delay) {
      return () -> {
         try {
            semaphore.acquire();
         } catch (InterruptedException var4) {
            throw new RuntimeException(var4);
         }

         J.s(() -> {
            try {
               runnable.run();
            } finally {
               semaphore.release();
            }

         }, delay);
      };
   }

   @BlockCoordinates
   default void update(int x, int y, int z, Chunk c, MantleChunk mc, RNG rf) {
      Block block = c.getBlock(x, y, z);
      BlockData data = block.getBlockData();
      this.blockUpdatedMetric();
      if (B.isStorage(data)) {
         RNG rx = rf.nextParallelRNG(BlockPosition.toLong(x, y, z));
         InventorySlotType slot = null;
         if (B.isStorageChest(data)) {
            slot = InventorySlotType.STORAGE;
         }

         if (slot != null) {
            KList tables = this.getLootTables(rx, block, mc);

            try {
               Bukkit.getPluginManager().callEvent(new IrisLootEvent(this, block, slot, tables));
               if (tables.isEmpty()) {
                  return;
               }

               InventoryHolder m = (InventoryHolder)block.getState();
               this.addItems(false, m.getInventory(), rx, tables, slot, c.getWorld(), x, y, z, 15);
            } catch (Throwable var13) {
               Iris.reportError(var13);
            }
         }
      } else {
         block.setType(Material.AIR, false);
         block.setBlockData(data, true);
      }

   }

   default void scramble(Inventory inventory, RNG rng) {
      ItemStack[] items = inventory.getContents();
      ItemStack[] nitems = new ItemStack[inventory.getSize()];
      System.arraycopy(items, 0, nitems, 0, items.length);
      boolean packedFull = false;

      int i;
      label47:
      for(i = 0; i < nitems.length; ++i) {
         ItemStack is = nitems[i];
         if (is != null && is.getAmount() > 1 && !packedFull) {
            for(int j = 0; j < nitems.length; ++j) {
               if (nitems[j] == null) {
                  int take = rng.nextInt(is.getAmount());
                  take = take == 0 ? 1 : take;
                  is.setAmount(is.getAmount() - take);
                  nitems[j] = is.clone();
                  nitems[j].setAmount(take);
                  continue label47;
               }
            }

            packedFull = true;
         }
      }

      for(i = nitems.length; i > 1; --i) {
         int j = rng.nextInt(i);
         ItemStack tmp = nitems[i - 1];
         nitems[i - 1] = nitems[j];
         nitems[j] = tmp;
      }

      inventory.setContents(nitems);
   }

   default void injectTables(KList<IrisLootTable> list, IrisLootReference r, boolean fallback) {
      if (!r.getMode().equals(IrisLootMode.FALLBACK) || fallback) {
         if (r.getMode().equals(IrisLootMode.CLEAR) || r.getMode().equals(IrisLootMode.REPLACE)) {
            list.clear();
         }

         list.addAll(r.getLootTables(this.getComplex()));
      }
   }

   @BlockCoordinates
   default KList<IrisLootTable> getLootTables(RNG rng, Block b) {
      MantleChunk mc = this.getMantle().getMantle().getChunk(b.getChunk()).use();

      KList var4;
      try {
         var4 = this.getLootTables(rng, b, mc);
      } finally {
         mc.release();
      }

      return var4;
   }

   @BlockCoordinates
   default KList<IrisLootTable> getLootTables(RNG rng, Block b, MantleChunk mc) {
      int rx = b.getX();
      int rz = b.getZ();
      int ry = b.getY() - this.getWorld().minHeight();
      double he = (Double)this.getComplex().getHeightStream().get((double)rx, (double)rz);
      KList<IrisLootTable> tables = new KList();
      PlacedObject po = this.getObjectPlacement(rx, ry, rz, mc);
      if (po != null && po.getPlacement() != null && B.isStorageChest(b.getBlockData())) {
         IrisLootTable table = po.getPlacement().getTable(b.getBlockData(), this.getData());
         if (table != null) {
            tables.add((Object)table);
            if (po.getPlacement().isOverrideGlobalLoot()) {
               return new KList(new IrisLootTable[]{table});
            }
         }
      }

      IrisRegion region = (IrisRegion)this.getComplex().getRegionStream().get((double)rx, (double)rz);
      IrisBiome biomeSurface = (IrisBiome)this.getComplex().getTrueBiomeStream().get((double)rx, (double)rz);
      IrisBiome biomeUnder = (double)ry < he ? (IrisBiome)this.getComplex().getCaveBiomeStream().get((double)rx, (double)rz) : biomeSurface;
      double multiplier = 1.0D * this.getDimension().getLoot().getMultiplier() * region.getLoot().getMultiplier() * biomeSurface.getLoot().getMultiplier() * biomeUnder.getLoot().getMultiplier();
      boolean fallback = tables.isEmpty();
      this.injectTables(tables, this.getDimension().getLoot(), fallback);
      this.injectTables(tables, region.getLoot(), fallback);
      this.injectTables(tables, biomeSurface.getLoot(), fallback);
      this.injectTables(tables, biomeUnder.getLoot(), fallback);
      if (tables.isNotEmpty()) {
         int target = (int)Math.round((double)tables.size() * multiplier);

         while(tables.size() < target && tables.isNotEmpty()) {
            tables.add((Object)((IrisLootTable)tables.get(rng.i(tables.size() - 1))));
         }

         while(tables.size() > target && tables.isNotEmpty()) {
            tables.remove(rng.i(tables.size() - 1));
         }
      }

      return tables;
   }

   default void addItems(boolean debug, Inventory inv, RNG rng, KList<IrisLootTable> tables, InventorySlotType slot, World world, int x, int y, int z, int mgf) {
      KList<ItemStack> items = new KList();
      Iterator var12 = tables.iterator();

      while(var12.hasNext()) {
         IrisLootTable i = (IrisLootTable)var12.next();
         if (i != null) {
            items.addAll(i.getLoot(debug, rng, slot, world, x, y, z));
         }
      }

      if (!IrisLootEvent.callLootEvent(items, inv, world, x, y, z)) {
         if (PaperLib.isPaper() && this.getWorld().hasRealWorld()) {
            PaperLib.getChunkAtAsync(this.getWorld().realWorld(), x >> 4, z >> 4).thenAccept((c) -> {
               Runnable r = () -> {
                  Iterator var4 = items.iterator();

                  while(var4.hasNext()) {
                     ItemStack i = (ItemStack)var4.next();
                     inv.addItem(new ItemStack[]{i});
                  }

                  this.scramble(inv, rng);
               };
               if (Bukkit.isPrimaryThread()) {
                  r.run();
               } else {
                  J.s(r);
               }

            });
         } else {
            var12 = items.iterator();

            while(var12.hasNext()) {
               ItemStack i = (ItemStack)var12.next();
               inv.addItem(new ItemStack[]{i});
            }

            this.scramble(inv, rng);
         }

      }
   }

   EngineEffects getEffects();

   default MultiBurst burst() {
      return this.getTarget().getBurster();
   }

   /** @deprecated */
   @Deprecated
   default void clean() {
      this.burst().lazy(() -> {
         this.getMantle().trim(10);
      });
   }

   @BlockCoordinates
   default IrisBiome getBiome(Location l) {
      return this.getBiome(l.getBlockX(), l.getBlockY() - this.getWorld().minHeight(), l.getBlockZ());
   }

   @BlockCoordinates
   default IrisRegion getRegion(Location l) {
      return this.getRegion(l.getBlockX(), l.getBlockZ());
   }

   IrisBiome getFocus();

   IrisRegion getFocusRegion();

   IrisEngineData getEngineData();

   default IrisBiome getSurfaceBiome(Chunk c) {
      return this.getSurfaceBiome((c.getX() << 4) + 8, (c.getZ() << 4) + 8);
   }

   default IrisRegion getRegion(Chunk c) {
      return this.getRegion((c.getX() << 4) + 8, (c.getZ() << 4) + 8);
   }

   default KList<IrisBiome> getAllBiomes() {
      KMap<String, IrisBiome> v = new KMap();
      IrisDimension dim = this.getDimension();
      dim.getAllBiomes(this).forEach((i) -> {
         v.put(i.getLoadKey(), i);
      });
      return v.v();
   }

   int getGenerated();

   CompletableFuture<Long> getHash32();

   default <T> IrisPosition lookForStreamResult(T find, ProceduralStream<T> stream, Function2<T, T, Boolean> matcher, long timeout) {
      AtomicInteger checked = new AtomicInteger();
      AtomicLong time = new AtomicLong(M.ms());
      AtomicReference<IrisPosition> r = new AtomicReference();
      BurstExecutor b = this.burst().burst();

      while(M.ms() - time.get() < timeout && r.get() == null) {
         b.queue(() -> {
            for(int i = 0; i < 1000; ++i) {
               if (M.ms() - time.get() > timeout) {
                  return;
               }

               int x = RNG.r.i(-29999970, 29999970);
               int z = RNG.r.i(-29999970, 29999970);
               checked.incrementAndGet();
               if ((Boolean)matcher.apply(stream.get((double)x, (double)z), find)) {
                  r.set(new IrisPosition(x, 120, z));
                  time.set(0L);
               }
            }

         });
      }

      return (IrisPosition)r.get();
   }

   default IrisPosition lookForBiome(IrisBiome biome, long timeout, Consumer<Integer> triesc) {
      if (!this.getWorld().hasRealWorld()) {
         Iris.error("Cannot GOTO without a bound world (headless mode)");
         return null;
      } else {
         ChronoLatch cl = new ChronoLatch(250L, false);
         long s = M.ms();
         int cpus = Runtime.getRuntime().availableProcessors();
         if (!this.getDimension().getAllBiomes(this).contains(biome)) {
            return null;
         } else {
            AtomicInteger tries = new AtomicInteger(0);
            AtomicBoolean found = new AtomicBoolean(false);
            AtomicBoolean running = new AtomicBoolean(true);
            AtomicReference<IrisPosition> location = new AtomicReference();

            for(int i = 0; i < cpus; ++i) {
               J.a(() -> {
                  while(true) {
                     try {
                        if (!found.get() && running.get()) {
                           try {
                              int x = RNG.r.i(-29999970, 29999970);
                              int z = RNG.r.i(-29999970, 29999970);
                              IrisBiome b = this.getSurfaceBiome(x, z);
                              if (b != null && b.getLoadKey() == null) {
                                 continue;
                              }

                              if (b != null && b.getLoadKey().equals(biome.getLoadKey())) {
                                 found.lazySet(true);
                                 location.lazySet(new IrisPosition(x, this.getHeight(x, z), z));
                              }

                              tries.getAndIncrement();
                              continue;
                           } catch (Throwable var11) {
                              Iris.reportError(var11);
                              var11.printStackTrace();
                              return;
                           }
                        }
                     } catch (Throwable var12) {
                        Iris.reportError(var12);
                        var12.printStackTrace();
                     }

                     return;
                  }
               });
            }

            do {
               if (found.get() && location.get() != null) {
                  running.set(false);
                  return (IrisPosition)location.get();
               }

               J.sleep(50L);
               if (cl.flip()) {
                  triesc.accept(tries.get());
               }
            } while(M.ms() - s <= timeout);

            running.set(false);
            return null;
         }
      }
   }

   default IrisPosition lookForRegion(IrisRegion reg, long timeout, Consumer<Integer> triesc) {
      if (!this.getWorld().hasRealWorld()) {
         Iris.error("Cannot GOTO without a bound world (headless mode)");
         return null;
      } else {
         ChronoLatch cl = new ChronoLatch(3000L, false);
         long s = M.ms();
         int cpus = Runtime.getRuntime().availableProcessors();
         if (!this.getDimension().getRegions().contains(reg.getLoadKey())) {
            return null;
         } else {
            AtomicInteger tries = new AtomicInteger(0);
            AtomicBoolean found = new AtomicBoolean(false);
            AtomicBoolean running = new AtomicBoolean(true);
            AtomicReference<IrisPosition> location = new AtomicReference();

            for(int i = 0; i < cpus; ++i) {
               J.a(() -> {
                  while(!found.get() && running.get()) {
                     try {
                        int x = RNG.r.i(-29999970, 29999970);
                        int z = RNG.r.i(-29999970, 29999970);
                        IrisRegion b = this.getRegion(x, z);
                        if (b != null && b.getLoadKey() != null && b.getLoadKey().equals(reg.getLoadKey())) {
                           found.lazySet(true);
                           location.lazySet(new IrisPosition(x, this.getHeight(x, z), z));
                        }

                        tries.getAndIncrement();
                     } catch (Throwable var11) {
                        Iris.reportError(var11);
                        var11.printStackTrace();
                        return;
                     }
                  }

               });
            }

            do {
               if (found.get() && location.get() == null) {
                  triesc.accept(tries.get());
                  running.set(false);
                  return (IrisPosition)location.get();
               }

               J.sleep(50L);
               if (cl.flip()) {
                  triesc.accept(tries.get());
               }
            } while(M.ms() - s <= timeout);

            triesc.accept(tries.get());
            running.set(false);
            return null;
         }
      }
   }

   double getGeneratedPerSecond();

   default int getHeight() {
      return this.getWorld().getHeight();
   }

   boolean isStudio();

   default IrisBiome getBiome(int x, int y, int z) {
      return y <= this.getHeight(x, z) - 2 ? this.getCaveBiome(x, z) : this.getSurfaceBiome(x, z);
   }

   default IrisBiome getBiomeOrMantle(int x, int y, int z) {
      return y <= this.getHeight(x, z) - 2 ? this.getCaveOrMantleBiome(x, y, z) : this.getSurfaceBiome(x, z);
   }

   default String getObjectPlacementKey(int x, int y, int z) {
      PlacedObject o = this.getObjectPlacement(x, y, z);
      if (o != null && o.getObject() != null) {
         String var10000 = o.getObject().getLoadKey();
         return var10000 + "@" + o.getId();
      } else {
         return null;
      }
   }

   default PlacedObject getObjectPlacement(int x, int y, int z) {
      MantleChunk chunk = this.getMantle().getMantle().getChunk(x >> 4, z >> 4).use();

      PlacedObject var5;
      try {
         var5 = this.getObjectPlacement(x, y, z, chunk);
      } finally {
         chunk.release();
      }

      return var5;
   }

   default PlacedObject getObjectPlacement(int x, int y, int z, MantleChunk chunk) {
      String objectAt = (String)chunk.get(x & 15, y, z & 15, String.class);
      if (objectAt != null && !objectAt.isEmpty()) {
         String[] v = objectAt.split("\\Q@\\E");
         String object = v[0];
         int id = Integer.parseInt(v[1]);
         JigsawPieceContainer container = (JigsawPieceContainer)chunk.get(x & 15, y, z & 15, JigsawPieceContainer.class);
         if (container != null) {
            IrisJigsawPiece piece = (IrisJigsawPiece)container.load(this.getData());
            if (piece.getObject().equals(object)) {
               return new PlacedObject(piece.getPlacementOptions(), (IrisObject)this.getData().getObjectLoader().load(object), id, x, z);
            }
         }

         IrisRegion region = this.getRegion(x, z);
         Iterator var11 = region.getObjects().iterator();

         IrisObjectPlacement i;
         do {
            if (!var11.hasNext()) {
               IrisBiome biome = this.getSurfaceBiome(x, z);
               Iterator var16 = biome.getObjects().iterator();

               IrisObjectPlacement i;
               do {
                  if (!var16.hasNext()) {
                     return new PlacedObject((IrisObjectPlacement)null, (IrisObject)this.getData().getObjectLoader().load(object), id, x, z);
                  }

                  i = (IrisObjectPlacement)var16.next();
               } while(!i.getPlace().contains(object));

               return new PlacedObject(i, (IrisObject)this.getData().getObjectLoader().load(object), id, x, z);
            }

            i = (IrisObjectPlacement)var11.next();
         } while(!i.getPlace().contains(object));

         return new PlacedObject(i, (IrisObject)this.getData().getObjectLoader().load(object), id, x, z);
      } else {
         return null;
      }
   }

   int getCacheID();

   default IrisBiome getBiomeOrMantle(Location l) {
      return this.getBiomeOrMantle(l.getBlockX(), l.getBlockY(), l.getBlockZ());
   }

   @Nullable
   @BlockCoordinates
   default Position2 getNearestStronghold(Position2 pos) {
      KList<Position2> p = this.getDimension().getStrongholds(this.getSeedManager().getMantle());
      if (p.isEmpty()) {
         return null;
      } else {
         Position2 pr = null;
         double d = Double.MAX_VALUE;
         Iterator var6 = p.iterator();

         while(var6.hasNext()) {
            Position2 i = (Position2)var6.next();
            double dx = i.distance(pos);
            if (dx < d) {
               d = dx;
               pr = i;
            }
         }

         return pr;
      }
   }

   default void gotoBiome(IrisBiome biome, Player player, boolean teleport) {
      Set<String> regionKeys = (Set)this.getDimension().getAllRegions(this).stream().filter((i) -> {
         return i.getAllBiomeIds().contains(biome.getLoadKey());
      }).map(IrisRegistrant::getLoadKey).collect(Collectors.toSet());
      Locator<IrisBiome> lb = Locator.surfaceBiome(biome.getLoadKey());
      Locator<IrisBiome> locator = (engine, chunk) -> {
         return regionKeys.contains(this.getRegion((chunk.getX() << 4) + 8, (chunk.getZ() << 4) + 8).getLoadKey()) && lb.matches(engine, chunk);
      };
      if (!regionKeys.isEmpty()) {
         locator.find(player, teleport, "Biome " + biome.getName());
      } else {
         String var10001 = String.valueOf(C.RED);
         player.sendMessage(var10001 + biome.getName() + " is not in any defined regions!");
      }

   }

   default void gotoJigsaw(IrisJigsawStructure s, Player player, boolean teleport) {
      if (s.getLoadKey().equals(this.getDimension().getStronghold())) {
         Position2 pr = this.getNearestStronghold(new Position2(player.getLocation().getBlockX(), player.getLocation().getBlockZ()));
         if (pr == null) {
            player.sendMessage(String.valueOf(C.GOLD) + "No strongholds in world.");
         } else {
            Location ll = new Location(player.getWorld(), (double)pr.getX(), 40.0D, (double)pr.getZ());
            J.s(() -> {
               player.teleport(ll);
            });
         }

      } else {
         if (((Set)this.getDimension().getJigsawStructures().stream().map(IrisJigsawStructurePlacement::getStructure).collect(Collectors.toSet())).contains(s.getLoadKey())) {
            Locator.jigsawStructure(s.getLoadKey()).find(player, teleport, "Structure " + s.getLoadKey());
         } else {
            Set<String> biomeKeys = (Set)this.getDimension().getAllBiomes(this).stream().filter((i) -> {
               return i.getJigsawStructures().stream().anyMatch((j) -> {
                  return j.getStructure().equals(s.getLoadKey());
               });
            }).map(IrisRegistrant::getLoadKey).collect(Collectors.toSet());
            Set<String> regionKeys = (Set)this.getDimension().getAllRegions(this).stream().filter((i) -> {
               Stream var10000 = i.getAllBiomeIds().stream();
               Objects.requireNonNull(biomeKeys);
               return var10000.anyMatch(biomeKeys::contains) || i.getJigsawStructures().stream().anyMatch((j) -> {
                  return j.getStructure().equals(s.getLoadKey());
               });
            }).map(IrisRegistrant::getLoadKey).collect(Collectors.toSet());
            Locator<IrisJigsawStructure> sl = Locator.jigsawStructure(s.getLoadKey());
            Locator<IrisBiome> locator = (engine, chunk) -> {
               if (biomeKeys.contains(this.getSurfaceBiome((chunk.getX() << 4) + 8, (chunk.getZ() << 4) + 8).getLoadKey())) {
                  return sl.matches(engine, chunk);
               } else {
                  return regionKeys.contains(this.getRegion((chunk.getX() << 4) + 8, (chunk.getZ() << 4) + 8).getLoadKey()) ? sl.matches(engine, chunk) : false;
               }
            };
            if (!regionKeys.isEmpty()) {
               locator.find(player, teleport, "Structure " + s.getLoadKey());
            } else {
               String var10001 = String.valueOf(C.RED);
               player.sendMessage(var10001 + s.getLoadKey() + " is not in any defined regions, biomes or dimensions!");
            }
         }

      }
   }

   default void gotoObject(String s, Player player, boolean teleport) {
      Set<String> biomeKeys = (Set)this.getDimension().getAllBiomes(this).stream().filter((i) -> {
         return i.getObjects().stream().anyMatch((f) -> {
            return f.getPlace().contains(s);
         });
      }).map(IrisRegistrant::getLoadKey).collect(Collectors.toSet());
      Set<String> regionKeys = (Set)this.getDimension().getAllRegions(this).stream().filter((i) -> {
         Stream var10000 = i.getAllBiomeIds().stream();
         Objects.requireNonNull(biomeKeys);
         return var10000.anyMatch(biomeKeys::contains) || i.getObjects().stream().anyMatch((f) -> {
            return f.getPlace().contains(s);
         });
      }).map(IrisRegistrant::getLoadKey).collect(Collectors.toSet());
      Locator<IrisObject> sl = Locator.object(s);
      Locator<IrisBiome> locator = (engine, chunk) -> {
         if (biomeKeys.contains(this.getSurfaceBiome((chunk.getX() << 4) + 8, (chunk.getZ() << 4) + 8).getLoadKey())) {
            return sl.matches(engine, chunk);
         } else {
            return regionKeys.contains(this.getRegion((chunk.getX() << 4) + 8, (chunk.getZ() << 4) + 8).getLoadKey()) ? sl.matches(engine, chunk) : false;
         }
      };
      if (!regionKeys.isEmpty()) {
         locator.find(player, teleport, "Object " + s);
      } else {
         String var10001 = String.valueOf(C.RED);
         player.sendMessage(var10001 + s + " is not in any defined regions or biomes!");
      }

   }

   default void gotoRegion(IrisRegion r, Player player, boolean teleport) {
      if (!this.getDimension().getRegions().contains(r.getLoadKey())) {
         String var10001 = String.valueOf(C.RED);
         player.sendMessage(var10001 + r.getName() + " is not defined in the dimension!");
      } else {
         Locator.region(r.getLoadKey()).find(player, teleport, "Region " + r.getName());
      }
   }

   default void gotoPOI(String type, Player p, boolean teleport) {
      Locator.poi(type).find(p, teleport, "POI " + type);
   }

   default void cleanupMantleChunk(int x, int z) {
      if (IrisSettings.get().getPerformance().isTrimMantleInStudio() || !this.isStudio()) {
         this.getMantle().cleanupChunk(x, z);
      }

   }
}
