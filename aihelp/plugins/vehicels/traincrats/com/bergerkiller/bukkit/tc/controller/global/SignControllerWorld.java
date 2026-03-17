package com.bergerkiller.bukkit.tc.controller.global;

import com.bergerkiller.bukkit.common.Task;
import com.bergerkiller.bukkit.common.block.SignChangeTracker;
import com.bergerkiller.bukkit.common.chunk.ChunkFutureProvider;
import com.bergerkiller.bukkit.common.chunk.ChunkFutureProvider.ChunkNeighbourList;
import com.bergerkiller.bukkit.common.chunk.ChunkFutureProvider.ChunkStateListener;
import com.bergerkiller.bukkit.common.chunk.ChunkFutureProvider.ChunkStateTracker;
import com.bergerkiller.bukkit.common.offline.OfflineWorld;
import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.ChunkUtil;
import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.common.utils.FaceUtil;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.common.wrappers.LongHashMap;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.utils.LongBlockCoordinates;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.LongUnaryOperator;
import java.util.function.Predicate;
import java.util.logging.Level;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

public class SignControllerWorld {
   private static final Material WALL_SIGN_TYPE = MaterialUtil.getMaterial("LEGACY_WALL_SIGN");
   private static final Material SIGN_POST_TYPE = MaterialUtil.getMaterial("LEGACY_SIGN_POST");
   private final SignController controller;
   private final World world;
   private final OfflineWorld offlineWorld;
   private final LongHashMap<SignControllerChunk> signChunks = new LongHashMap();
   private final LongHashMap<SignController.EntryList> signsByNeighbouringBlock = new LongHashMap();
   private final ChunkFutureProvider chunkFutureProvider;
   private boolean needsInitialization;

   SignControllerWorld(SignController controller) {
      this.controller = controller;
      this.world = null;
      this.offlineWorld = OfflineWorld.NONE;
      this.chunkFutureProvider = null;
      this.needsInitialization = true;
   }

   SignControllerWorld(SignController controller, World world) {
      this.controller = controller;
      this.world = world;
      this.offlineWorld = OfflineWorld.of(world);
      this.chunkFutureProvider = ChunkFutureProvider.of(controller.getPlugin());
      this.needsInitialization = true;
   }

   public World getWorld() {
      return this.world;
   }

   public SignController getGlobalController() {
      return this.controller;
   }

   public TrainCarts getPlugin() {
      return this.controller.getPlugin();
   }

   public boolean isValid() {
      return this.offlineWorld.getLoadedWorld() == this.world;
   }

   public boolean isEnabled() {
      return true;
   }

   public void initialize() {
      if (this.needsInitialization) {
         this.needsInitialization = false;
         if (this.isEnabled()) {
            Chunk[] var1 = this.world.getLoadedChunks();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               Chunk chunk = var1[var3];
               this.loadChunk(chunk);
            }
         }
      }

   }

   public SignController.Entry[] findNearby(Block block, boolean mustHaveSignActions) {
      return !this.initializeNearbySigns(block.getX(), block.getY(), block.getZ(), 1, mustHaveSignActions) ? SignController.EntryList.NONE.values() : this.getNearbySignsUnsafe(LongBlockCoordinates.map(block.getX(), block.getY(), block.getZ()));
   }

   SignController.Entry[] getNearbySignsUnsafe(long blockCoordinatesKey) {
      return ((SignController.EntryList)this.signsByNeighbouringBlock.getOrDefault(blockCoordinatesKey, SignController.EntryList.NONE)).values();
   }

   public SignController.Entry findForSign(Block signBlock, boolean mustHaveSignActions) {
      SignController.Entry[] var3 = this.findNearby(signBlock, mustHaveSignActions);
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         SignController.Entry entry = var3[var5];
         if (entry.sign.getBlock().equals(signBlock)) {
            return entry;
         }
      }

      return null;
   }

   public void forEachNearbyVerify(Block block, boolean mustHaveSignActions, Consumer<SignController.Entry> handler) {
      SignController.Entry[] var4 = this.findNearby(block, mustHaveSignActions);
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         SignController.Entry entry = var4[var6];
         if (!mustHaveSignActions || entry.hasSignActionEvents()) {
            handler.accept(entry);
         }
      }

   }

   public void forEachSignInColumn(Block block, BlockFace direction, boolean mustHaveSignActions, Consumer<SignChangeTracker> handler) {
      int bx = block.getX();
      int by = block.getY();
      int bz = block.getZ();
      int checkBorder = FaceUtil.isVertical(direction) ? 0 : 1;
      if (this.initializeNearbySigns(bx, by, bz, checkBorder, mustHaveSignActions)) {
         long key = LongBlockCoordinates.map(block.getX(), block.getY(), block.getZ());
         LongUnaryOperator shift = LongBlockCoordinates.shiftOperator(direction);
         int steps = 0;

         while(true) {
            boolean foundSigns = false;
            SignController.Entry[] var14 = this.getNearbySignsUnsafe(key);
            int var15 = var14.length;

            for(int var16 = 0; var16 < var15; ++var16) {
               SignController.Entry entry = var14[var16];
               if (this.verifySignColumnSlice(key, direction, steps == 0, entry)) {
                  foundSigns = true;
                  if (!mustHaveSignActions || entry.hasSignActionEvents()) {
                     handler.accept(entry.sign);
                  }
               }
            }

            if (!foundSigns && steps > 1) {
               break;
            }

            key = shift.applyAsLong(key);
            ++steps;
            if (!FaceUtil.isVertical(direction)) {
               bx += direction.getModX();
               bz += direction.getModZ();
               if (!this.initializeNearbySigns(bx, by, bz, checkBorder, mustHaveSignActions)) {
                  break;
               }
            }
         }

      }
   }

   public boolean hasSignsAroundColumn(Block block, BlockFace direction, boolean mustHaveSignActions) {
      int checkBorder = FaceUtil.isVertical(direction) ? 0 : 1;
      if (!this.initializeNearbySigns(block.getX(), block.getY(), block.getZ(), checkBorder, mustHaveSignActions)) {
         return false;
      } else {
         long key = LongBlockCoordinates.map(block.getX(), block.getY(), block.getZ());
         SignController.Entry[] var7 = this.getNearbySignsUnsafe(key);
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            SignController.Entry entry = var7[var9];
            if (this.verifySignColumnSlice(key, direction, true, entry)) {
               return true;
            }
         }

         return false;
      }
   }

   private boolean initializeNearbySigns(int x, int y, int z, int border, boolean mustHaveSignActions) {
      int serverTick = CommonUtil.getServerTicks();
      int cx = x >> 4;
      int cz = z >> 4;
      boolean result = this.getSignChunk(cx, cz).checkMayHaveSigns(x, y, z, mustHaveSignActions, serverTick);
      int bx = x & 15;
      if (bx <= border) {
         result |= this.getSignChunk(cx - 1, cz).checkMayHaveSigns(x, y, z, mustHaveSignActions, serverTick);
      } else if (bx >= 15 - border) {
         result |= this.getSignChunk(cx + 1, cz).checkMayHaveSigns(x, y, z, mustHaveSignActions, serverTick);
      }

      int bz = z & 15;
      if (bz <= border) {
         result |= this.getSignChunk(cx, cz - 1).checkMayHaveSigns(x, y, z, mustHaveSignActions, serverTick);
      } else if (bz >= 15 - border) {
         result |= this.getSignChunk(cx, cz + 1).checkMayHaveSigns(x, y, z, mustHaveSignActions, serverTick);
      }

      return result;
   }

   private boolean verifySignColumnSlice(long key, BlockFace direction, boolean firstLayer, SignController.Entry entry) {
      BlockFace offset = LongBlockCoordinates.findDirection(entry.blockKey, key);
      if (offset == null) {
         return false;
      } else {
         BlockData blockData = entry.sign.getBlockData();
         if (blockData.isType(SIGN_POST_TYPE)) {
            if (direction == BlockFace.DOWN) {
               return offset == BlockFace.SELF || firstLayer && offset == BlockFace.DOWN;
            } else {
               return offset == BlockFace.DOWN || firstLayer && offset == BlockFace.SELF;
            }
         } else if (!blockData.isType(WALL_SIGN_TYPE)) {
            entry.removeInvalidEntry();
            return false;
         } else if (offset != direction && offset != direction.getOppositeFace()) {
            BlockFace facing = blockData.getAttachedFace();
            return facing == offset || facing == direction.getOppositeFace();
         } else {
            return false;
         }
      }
   }

   public void detectNewSigns(Block around) {
      long blockKey = LongBlockCoordinates.map(around);
      SignController.Entry[] nearby = this.initializeNearbySigns(around.getX(), around.getY(), around.getZ(), 1, false) ? this.getNearbySignsUnsafe(blockKey) : SignController.EntryList.NONE.unsortedValues();
      LongBlockCoordinates.forAllBlockSidesAndSelf(blockKey, (face, key) -> {
         SignController.Entry[] var6 = nearby;
         int by = nearby.length;

         int bz;
         for(bz = 0; bz < by; ++bz) {
            SignController.Entry e = var6[bz];
            if (e.blockKey == key) {
               return;
            }
         }

         int bx = around.getX() + face.getModX();
         by = around.getY() + face.getModY();
         bz = around.getZ() + face.getModZ();
         Chunk chunk = WorldUtil.getChunk(this.world, bx >> 4, bz >> 4);
         if (chunk != null) {
            if (MaterialUtil.ISSIGN.get(ChunkUtil.getBlockData(chunk, bx, by, bz))) {
               final Block potentialSign = around.getRelative(face);
               (new Task(this.controller.getPlugin()) {
                  public void run() {
                     if ((Boolean)MaterialUtil.ISSIGN.get(potentialSign)) {
                        SignControllerWorld.this.addSign(potentialSign, false, true);
                     }

                  }
               }).start();
            }
         }
      });
   }

   public void redetectSignActions() {
      Iterator var1 = this.signChunks.values().iterator();

      while(var1.hasNext()) {
         SignControllerChunk chunk = (SignControllerChunk)var1.next();
         SignController.Entry[] var3 = chunk.getEntries();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            SignController.Entry entry = var3[var5];
            entry.redetectSignActions();
         }
      }

   }

   public SignController.Entry addSign(Block signBlock, boolean isSignChange, boolean frontText) {
      SignController.Entry existing = this.findForSign(signBlock, false);
      if (existing != null) {
         if (isSignChange) {
            if (existing.verifyBeforeSignChange(frontText)) {
               return existing;
            }

            existing.removeInvalidEntry();
            existing = null;
         }

         if (existing != null) {
            if (existing.verify()) {
               this.controller.activateEntry(existing, true, true);
               return existing;
            }

            existing.removeInvalidEntry();
            existing = null;
         }
      }

      Sign sign = BlockUtil.getSign(signBlock);
      return sign == null ? null : this.createNewSign(sign, isSignChange);
   }

   private SignController.Entry createNewSign(Sign sign, boolean isSignChange) {
      Block signBlock = sign.getBlock();
      SignControllerChunk signChunk = this.getSignChunk(MathUtil.toChunk(signBlock.getX()), MathUtil.toChunk(signBlock.getZ()));
      SignController.Entry entry = this.controller.createEntry(sign, this, signChunk, LongBlockCoordinates.map(signBlock.getX(), signBlock.getY(), signBlock.getZ()));
      signChunk.addEntry(entry);
      this.controller.activateEntry(entry, true, !isSignChange);
      return entry;
   }

   public SignControllerWorld.RefreshResult refreshInChunk(Chunk chunk) {
      long chunkKey = SignControllerChunk.getKeyOf(chunk);
      int numRemoved = 0;
      SignControllerChunk signChunk = (SignControllerChunk)this.signChunks.get(chunkKey);
      SignController.Entry existing;
      if (signChunk != null && signChunk.hasSigns()) {
         SignController.Entry[] var6 = signChunk.getEntries();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            existing = var6[var8];
            if (!existing.verify()) {
               signChunk.removeEntry(existing);
               this.controller.getPlugin().getOfflineSigns().removeAll(existing.sign.getBlock());
               ++numRemoved;
               existing.onRemoved();
            }
         }
      }

      int numAdded = 0;
      Iterator var11 = this.getBlockStatesSafe(chunk).iterator();

      while(var11.hasNext()) {
         BlockState blockState = (BlockState)var11.next();
         if (blockState instanceof Sign) {
            Block signBlock = blockState.getBlock();
            existing = this.findForSign(signBlock, false);
            if (existing != null) {
               this.controller.activateEntry(existing);
            } else {
               this.createNewSign((Sign)blockState, false);
               ++numAdded;
            }
         }
      }

      return new SignControllerWorld.RefreshResult(numAdded, numRemoved);
   }

   void clear() {
      Iterator var1 = this.signChunks.values().iterator();

      while(var1.hasNext()) {
         SignControllerChunk chunk = (SignControllerChunk)var1.next();
         SignController.Entry[] var3 = chunk.getEntries();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            SignController.Entry e = var3[var5];
            e.onRemoved();
         }
      }

      this.signChunks.clear();
      this.signsByNeighbouringBlock.clear();
   }

   private void activateSignsInChunk(Chunk chunk) {
      Predicate var10003 = SignController.Entry::verify;
      SignController var10004 = this.controller;
      Objects.requireNonNull(var10004);
      this.changeActiveForEntriesInChunk(chunk, true, var10003, var10004::activateEntry);
   }

   private void deactivateSignsInChunk(Chunk chunk) {
      this.changeActiveForEntriesInChunk(chunk, false, (e) -> {
         return !e.sign.isRemoved();
      }, SignController.Entry::deactivate);
   }

   private void changeActiveForEntriesInChunk(Chunk chunk, boolean activating, Predicate<SignController.Entry> verify, Consumer<SignController.Entry> handler) {
      SignControllerChunk signChunk = (SignControllerChunk)this.signChunks.get(chunk.getX(), chunk.getZ());
      if (signChunk != null && signChunk.hasSigns()) {
         int retryLimit = 100;

         while(true) {
            SignController.Entry[] entries = signChunk.getEntries();
            boolean hasEntriesToHandle = false;
            SignController.Entry[] var9 = entries;
            int var10 = entries.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               SignController.Entry entry = var9[var11];
               if (entry.front.activated != activating || entry.back.activated != activating) {
                  hasEntriesToHandle = true;
                  break;
               }
            }

            if (!hasEntriesToHandle) {
               return;
            }

            --retryLimit;
            SignController.Entry[] var13;
            int var14;
            SignController.Entry entry;
            if (retryLimit == 0) {
               this.controller.getPlugin().log(Level.SEVERE, "Infinite loop " + (activating ? "activating" : "de-activating") + " signs in chunk [" + chunk.getX() + "/" + chunk.getZ() + "]. Signs:");
               var13 = entries;
               var14 = entries.length;

               for(var10 = 0; var10 < var14; ++var10) {
                  entry = var13[var10];
                  this.controller.getPlugin().log(Level.SEVERE, "- at " + entry.sign.getBlock());
               }

               return;
            }

            var13 = entries;
            var14 = entries.length;

            for(var10 = 0; var10 < var14; ++var10) {
               entry = var13[var10];
               if (entry.front.activated != activating || entry.back.activated != activating) {
                  if (verify.test(entry)) {
                     handler.accept(entry);
                  } else {
                     entry.removeInvalidEntry();
                  }
               }
            }
         }
      }
   }

   private SignControllerChunk getSignChunk(int cx, int cz) {
      long key = MathUtil.longHashToLong(cx, cz);
      SignControllerChunk signChunk;
      if ((signChunk = (SignControllerChunk)this.signChunks.get(key)) == null) {
         signChunk = this.loadChunk(this.world.getChunkAt(cx, cz));
      }

      return signChunk;
   }

   SignControllerChunk loadChunk(Chunk chunk) {
      long chunkKey = SignControllerChunk.getKeyOf(chunk);
      if (this.needsInitialization) {
         return new SignControllerChunk(chunkKey);
      } else {
         SignControllerChunk newSignChunk = (SignControllerChunk)this.signChunks.get(chunkKey);
         if (newSignChunk != null) {
            return newSignChunk;
         } else {
            newSignChunk = new SignControllerChunk(chunkKey);
            List<SignController.Entry> newEntriesAtChunk = Collections.emptyList();
            Iterator var6 = this.getBlockStatesSafe(chunk).iterator();

            while(var6.hasNext()) {
               BlockState blockState = (BlockState)var6.next();
               if (blockState instanceof Sign) {
                  SignController.Entry entry = this.controller.createEntry((Sign)blockState, this, newSignChunk, LongBlockCoordinates.map(blockState.getX(), blockState.getY(), blockState.getZ()));
                  if (((List)newEntriesAtChunk).isEmpty()) {
                     newEntriesAtChunk = new ArrayList();
                  }

                  ((List)newEntriesAtChunk).add(entry);
               }
            }

            newSignChunk.initialize((List)newEntriesAtChunk);
            this.signChunks.put(chunkKey, newSignChunk);
            this.chunkFutureProvider.trackNeighboursLoaded(chunk, ChunkNeighbourList.neighboursOf(chunk, 1), new ChunkStateListener() {
               public void onRegistered(ChunkStateTracker tracker) {
                  if (tracker.isLoaded()) {
                     this.onLoaded(tracker);
                  }

               }

               public void onCancelled(ChunkStateTracker tracker) {
               }

               public void onLoaded(ChunkStateTracker tracker) {
                  SignControllerWorld.this.activateSignsInChunk(tracker.getChunk());
               }

               public void onUnloaded(ChunkStateTracker tracker) {
                  SignControllerWorld.this.deactivateSignsInChunk(tracker.getChunk());
               }
            });
            return newSignChunk;
         }
      }
   }

   private Collection<BlockState> getBlockStatesSafe(Chunk chunk) {
      try {
         return WorldUtil.getBlockStates(chunk);
      } catch (Throwable var3) {
         this.controller.getPlugin().getLogger().log(Level.SEVERE, "Error reading sign block states in chunk " + chunk.getWorld().getName() + " [" + chunk.getX() + "/" + chunk.getZ() + "]", var3);
         return Collections.emptyList();
      }
   }

   void addChunkByBlockEntry(SignController.Entry entry, long key) {
      this.signsByNeighbouringBlock.merge(key, entry.singletonList, (a, b) -> {
         return a.add(entry);
      });
   }

   void unloadChunk(Chunk chunk) {
      if (!this.needsInitialization) {
         SignControllerChunk signChunk = (SignControllerChunk)this.signChunks.remove(chunk.getX(), chunk.getZ());
         if (signChunk != null && signChunk.hasSigns()) {
            SignController.Entry[] var3 = signChunk.getEntries();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               SignController.Entry entry = var3[var5];
               if (!entry.sign.isRemoved()) {
                  entry.deactivate();
               }

               entry.unregisterInNeighbouringBlocks(true);
            }
         }

      }
   }

   protected void removeChunkByBlockEntry(SignController.Entry entry, long key) {
      this.removeChunkByBlockEntry(entry, key, false);
   }

   protected void removeChunkByBlockEntry(SignController.Entry entry, long key, boolean purgeAllInSameChunk) {
      SignController.EntryList oldEntryList = (SignController.EntryList)this.signsByNeighbouringBlock.remove(key);
      if (oldEntryList != null && (!purgeAllInSameChunk || LongBlockCoordinates.getChunkEdgeDistance(key) < 2)) {
         SignController.EntryList newEntryList = oldEntryList.filter((e) -> {
            return e != entry && (!purgeAllInSameChunk || e.chunk != entry.chunk);
         });
         if (newEntryList.count() > 0) {
            this.signsByNeighbouringBlock.put(key, newEntryList);
         }

      }
   }

   public static class RefreshResult {
      public static final SignControllerWorld.RefreshResult NONE = new SignControllerWorld.RefreshResult(0, 0);
      public final int numAdded;
      public final int numRemoved;

      public RefreshResult(int numAdded, int numRemoved) {
         this.numAdded = numAdded;
         this.numRemoved = numRemoved;
      }

      public SignControllerWorld.RefreshResult add(SignControllerWorld.RefreshResult other) {
         return new SignControllerWorld.RefreshResult(this.numAdded + other.numAdded, this.numRemoved + other.numRemoved);
      }
   }

   static class SignControllerWorldDisabled extends SignControllerWorld {
      SignControllerWorldDisabled(SignController controller, World world) {
         super(controller, world);
      }

      public boolean isEnabled() {
         return false;
      }

      public SignController.Entry[] findNearby(Block block, boolean mustHaveSignActions) {
         return SignController.EntryList.NONE.values();
      }

      public SignController.Entry addSign(Block signBlock, boolean handleLoadChange, boolean frontText) {
         return null;
      }

      public SignControllerWorld.RefreshResult refreshInChunk(Chunk chunk) {
         return SignControllerWorld.RefreshResult.NONE;
      }

      SignControllerChunk loadChunk(Chunk chunk) {
         return new SignControllerChunk(SignControllerChunk.getKeyOf(chunk));
      }

      void unloadChunk(Chunk chunk) {
      }
   }
}
