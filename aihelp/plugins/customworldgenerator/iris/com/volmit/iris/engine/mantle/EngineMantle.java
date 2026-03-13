package com.volmit.iris.engine.mantle;

import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.nms.container.Pair;
import com.volmit.iris.engine.IrisComplex;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.framework.EngineTarget;
import com.volmit.iris.engine.mantle.components.MantleJigsawComponent;
import com.volmit.iris.engine.mantle.components.MantleObjectComponent;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.engine.object.IrisPosition;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.data.B;
import com.volmit.iris.util.documentation.BlockCoordinates;
import com.volmit.iris.util.documentation.ChunkCoordinates;
import com.volmit.iris.util.hunk.Hunk;
import com.volmit.iris.util.mantle.Mantle;
import com.volmit.iris.util.mantle.MantleChunk;
import com.volmit.iris.util.mantle.flag.MantleFlag;
import com.volmit.iris.util.matter.Matter;
import com.volmit.iris.util.matter.MatterCavern;
import com.volmit.iris.util.matter.MatterFluidBody;
import com.volmit.iris.util.matter.MatterMarker;
import com.volmit.iris.util.matter.slices.UpdateMatter;
import com.volmit.iris.util.parallel.MultiBurst;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.UnmodifiableView;

public interface EngineMantle extends MatterGenerator {
   BlockData AIR = B.get("AIR");

   Mantle getMantle();

   Engine getEngine();

   int getRadius();

   int getRealRadius();

   @UnmodifiableView
   List<Pair<List<MantleComponent>, Integer>> getComponents();

   @UnmodifiableView
   Map<MantleFlag, MantleComponent> getRegisteredComponents();

   boolean registerComponent(MantleComponent c);

   @UnmodifiableView
   KList<MantleFlag> getComponentFlags();

   void hotload();

   default int getHighest(int x, int z) {
      return this.getHighest(x, z, this.getData());
   }

   @ChunkCoordinates
   default KList<IrisPosition> findMarkers(int x, int z, MatterMarker marker) {
      KList<IrisPosition> p = new KList();
      this.getMantle().iterateChunk(x, z, MatterMarker.class, (xx, yy, zz, mm) -> {
         if (marker.equals(mm)) {
            p.add((Object)(new IrisPosition(xx + (x << 4), yy, zz + (z << 4))));
         }

      });
      return p;
   }

   default int getHighest(int x, int z, boolean ignoreFluid) {
      return this.getHighest(x, z, this.getData(), ignoreFluid);
   }

   default int getHighest(int x, int z, IrisData data) {
      return this.getHighest(x, z, data, false);
   }

   default int getHighest(int x, int z, IrisData data, boolean ignoreFluid) {
      return ignoreFluid ? this.trueHeight(x, z) : Math.max(this.trueHeight(x, z), this.getEngine().getDimension().getFluidHeight());
   }

   default int trueHeight(int x, int z) {
      return (Integer)this.getComplex().getRoundedHeighteightStream().get((double)x, (double)z);
   }

   /** @deprecated */
   @Deprecated(
      forRemoval = true
   )
   default boolean isCarved(int x, int h, int z) {
      return this.getMantle().get(x, h, z, MatterCavern.class) != null;
   }

   /** @deprecated */
   @Deprecated(
      forRemoval = true
   )
   default BlockData get(int x, int y, int z) {
      BlockData block = (BlockData)this.getMantle().get(x, y, z, BlockData.class);
      return block == null ? AIR : block;
   }

   default boolean isPreventingDecay() {
      return this.getEngine().getDimension().isPreventLeafDecay();
   }

   default boolean isUnderwater(int x, int z) {
      return this.getHighest(x, z, true) <= this.getFluidHeight();
   }

   default int getFluidHeight() {
      return this.getEngine().getDimension().getFluidHeight();
   }

   default boolean isDebugSmartBore() {
      return this.getEngine().getDimension().isDebugSmartBore();
   }

   default void trim(long dur, int limit) {
      this.getMantle().trim(dur, limit);
   }

   default IrisData getData() {
      return this.getEngine().getData();
   }

   default EngineTarget getTarget() {
      return this.getEngine().getTarget();
   }

   default IrisDimension getDimension() {
      return this.getEngine().getDimension();
   }

   default IrisComplex getComplex() {
      return this.getEngine().getComplex();
   }

   default void close() {
      this.getMantle().close();
   }

   default void saveAllNow() {
      this.getMantle().saveAll();
   }

   default void save() {
   }

   default void trim(int limit) {
      this.getMantle().trim(TimeUnit.SECONDS.toMillis((long)IrisSettings.get().getPerformance().getMantleKeepAlive()), limit);
   }

   default int unloadTectonicPlate(int tectonicLimit) {
      return this.getMantle().unloadTectonicPlate(tectonicLimit);
   }

   default MultiBurst burst() {
      return this.getEngine().burst();
   }

   @ChunkCoordinates
   default <T> void insertMatter(int x, int z, Class<T> t, Hunk<T> blocks, boolean multicore) {
      if (this.getEngine().getDimension().isUseMantle()) {
         MantleChunk chunk = this.getMantle().getChunk(x, z).use();

         try {
            Objects.requireNonNull(blocks);
            chunk.iterate(t, blocks::set);
         } finally {
            chunk.release();
         }

      }
   }

   @BlockCoordinates
   default void updateBlock(int x, int y, int z) {
      this.getMantle().set(x, y, z, (Object)UpdateMatter.ON);
   }

   @BlockCoordinates
   default void dropCavernBlock(int x, int y, int z) {
      Matter matter = this.getMantle().getChunk(x & 15, z & 15).get(y & 15);
      if (matter != null) {
         matter.slice(MatterCavern.class).set(x & 15, y & 15, z & 15, (Object)null);
      }

   }

   default boolean queueRegenerate(int x, int z) {
      return false;
   }

   default boolean dequeueRegenerate(int x, int z) {
      return false;
   }

   default int getLoadedRegionCount() {
      return this.getMantle().getLoadedRegionCount();
   }

   MantleJigsawComponent getJigsawComponent();

   MantleObjectComponent getObjectComponent();

   default boolean isCovered(int x, int z) {
      int s = this.getRealRadius();

      for(int i = -s; i <= s; ++i) {
         for(int j = -s; j <= s; ++j) {
            int xx = i + x;
            int zz = j + z;
            if (!this.getMantle().hasFlag(xx, zz, MantleFlag.REAL)) {
               return false;
            }
         }
      }

      return true;
   }

   default void cleanupChunk(int x, int z) {
      if (this.isCovered(x, z)) {
         MantleChunk chunk = this.getMantle().getChunk(x, z).use();

         try {
            chunk.raiseFlagUnchecked(MantleFlag.CLEANED, () -> {
               chunk.deleteSlices(BlockData.class);
               chunk.deleteSlices(String.class);
               chunk.deleteSlices(MatterCavern.class);
               chunk.deleteSlices(MatterFluidBody.class);
            });
         } finally {
            chunk.release();
         }

      }
   }

   default int getUnloadRegionCount() {
      return this.getMantle().getUnloadRegionCount();
   }

   default double getAdjustedIdleDuration() {
      return this.getMantle().getAdjustedIdleDuration();
   }
}
