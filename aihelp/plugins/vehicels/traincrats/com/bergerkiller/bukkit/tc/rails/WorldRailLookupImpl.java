package com.bergerkiller.bukkit.tc.rails;

import com.bergerkiller.bukkit.common.bases.IntVector3;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.offline.OfflineBlock;
import com.bergerkiller.bukkit.common.offline.OfflineWorld;
import com.bergerkiller.bukkit.common.utils.BlockUtil;
import com.bergerkiller.bukkit.common.utils.MaterialUtil;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.common.wrappers.BlockData;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.components.RailPath;
import com.bergerkiller.bukkit.tc.controller.components.RailPiece;
import com.bergerkiller.bukkit.tc.controller.components.RailState;
import com.bergerkiller.bukkit.tc.controller.global.SignControllerWorld;
import com.bergerkiller.bukkit.tc.detector.DetectorRegion;
import com.bergerkiller.bukkit.tc.rails.type.RailType;
import com.bergerkiller.bukkit.tc.signactions.mutex.MutexZoneCache;
import com.bergerkiller.bukkit.tc.signactions.mutex.MutexZoneCacheWorld;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

final class WorldRailLookupImpl implements WorldRailLookup {
   private static final WorldRailLookupImpl.Bucket[] NO_RAILS_AT_POSITION = new WorldRailLookupImpl.Bucket[0];
   private static final WorldRailLookupImpl.TrackedSignList SIGN_LIST_CACHE = new WorldRailLookupImpl.TrackedSignList();
   private static final Material WALL_SIGN_TYPE = MaterialUtil.getMaterial("LEGACY_WALL_SIGN");
   private static final Material SIGN_POST_TYPE = MaterialUtil.getMaterial("LEGACY_SIGN_POST");
   private static BlockFace[] SIGN_FACES_ORDERED;
   private final TrainCarts traincarts;
   private World world;
   private OfflineWorld offlineWorld;
   private Map<IntVector3, WorldRailLookupImpl.Bucket> cache;
   private List<WorldRailLookupImpl.Bucket> cacheValues;
   private MutexZoneCacheWorld mutexZones;
   private SignControllerWorld signController;
   private int ticksWithEmptyCache;

   WorldRailLookupImpl(TrainCarts traincarts, World world) {
      this.traincarts = traincarts;
      this.offlineWorld = OfflineWorld.of(world);
      this.world = world;
      this.cache = new HashMap();
      this.cacheValues = new ArrayList();
      this.mutexZones = MutexZoneCache.forWorld(this.offlineWorld);
      this.signController = traincarts.getSignController().forWorldSkipInitialization(this.world);
      this.ticksWithEmptyCache = 0;
   }

   void initialize() {
      this.signController.initialize();
      DetectorRegion.fillRailLookup(this);
   }

   public World getWorld() {
      World w = this.world;
      return w == null ? this.offlineWorld.getLoadedWorld() : w;
   }

   public OfflineWorld getOfflineWorld() {
      return this.offlineWorld;
   }

   public MutexZoneCacheWorld getMutexZones() {
      return this.mutexZones;
   }

   public SignControllerWorld getSignController() {
      return this.signController;
   }

   public boolean isValid() {
      return this.world != null;
   }

   public boolean isValidForWorld(World world) {
      return this.world == world;
   }

   boolean checkCanBeRemoved() {
      if (this.offlineWorld.getLoadedWorld() != this.world) {
         return true;
      } else if (!this.cache.isEmpty()) {
         this.ticksWithEmptyCache = 0;
         return false;
      } else {
         return ++this.ticksWithEmptyCache > 12000;
      }
   }

   void close() {
      if (!this.cache.isEmpty()) {
         this.forAllBuckets((b) -> {
            b.rail_life = 0;
         });
         this.cache.clear();
         this.cacheValues.clear();
      }

      this.cache = Collections.emptyMap();
      this.cacheValues = Collections.emptyList();
      this.world = null;
   }

   public RailPiece[] findAtStatePosition(RailState state) {
      RailPath.Position pos = state.position();
      IntVector3 coordinates;
      if (pos.relative) {
         coordinates = state.railPiece().blockPosition().add(MathUtil.floor(pos.posX), MathUtil.floor(pos.posY), MathUtil.floor(pos.posZ));
      } else {
         coordinates = new IntVector3(MathUtil.floor(pos.posX), MathUtil.floor(pos.posY), MathUtil.floor(pos.posZ));
      }

      IntVector3 cacheKey = createCacheKey(coordinates);
      WorldRailLookupImpl.Bucket inCache = (WorldRailLookupImpl.Bucket)this.cache.get(cacheKey);
      return inCache != null ? inCache.getRailsAtPosition() : this.discoverBucketsAtPositionBlock(cacheKey, this.offlineWorld.getBlockAt(coordinates));
   }

   public RailPiece[] findAtBlockPosition(OfflineBlock positionBlock) {
      IntVector3 cacheKey = createCacheKey(positionBlock);
      WorldRailLookupImpl.Bucket inCache = (WorldRailLookupImpl.Bucket)this.cache.get(cacheKey);
      return inCache != null ? inCache.getRailsAtPosition() : this.discoverBucketsAtPositionBlock(cacheKey, positionBlock);
   }

   public RailLookup.CachedRailPiece lookupCachedRailPieceIfCached(OfflineBlock railOfflineBlock, RailType railType) {
      IntVector3 cacheKey = createCacheKey(railOfflineBlock);
      WorldRailLookupImpl.Bucket inCache = (WorldRailLookupImpl.Bucket)this.cache.get(cacheKey);
      if (inCache != null) {
         RailType inCacheType = inCache.type();
         if (inCacheType == railType) {
            return inCache;
         }

         if (inCacheType != RailType.NONE) {
            while((inCache = inCache.next) != null) {
               if (inCache.type() == railType) {
                  return inCache;
               }
            }
         }
      }

      return RailLookup.CachedRailPiece.NONE;
   }

   public List<RailLookup.CachedRailPiece> lookupCachedRailPieces(OfflineBlock railOfflineBlock) {
      IntVector3 cacheKey = createCacheKey(railOfflineBlock);
      WorldRailLookupImpl.Bucket inCache = (WorldRailLookupImpl.Bucket)this.cache.get(cacheKey);
      if (inCache == null) {
         return Collections.emptyList();
      } else if (inCache.next == null) {
         return Collections.singletonList(inCache);
      } else {
         List<RailLookup.CachedRailPiece> result = new ArrayList(5);
         result.add(inCache);

         while((inCache = inCache.next) != null) {
            result.add(inCache);
         }

         return result;
      }
   }

   public RailLookup.CachedRailPiece lookupCachedRailPiece(OfflineBlock railOfflineBlock, Block railBlock, RailType railType) {
      return this.lookupRailBucket(railOfflineBlock, railBlock, railType);
   }

   private WorldRailLookupImpl.Bucket lookupRailBucket(OfflineBlock railOfflineBlock, Block railBlock, RailType railType) {
      IntVector3 cacheKey = createCacheKey(railOfflineBlock);
      WorldRailLookupImpl.Bucket inCache = (WorldRailLookupImpl.Bucket)this.cache.get(cacheKey);
      if (inCache == null) {
         if (!railType.isRegistered()) {
            throw new RailLookup.RailTypeNotRegisteredException(railType);
         } else {
            inCache = new WorldRailLookupImpl.Bucket(railOfflineBlock, railBlock, railType);
            this.addToCache(cacheKey, inCache);
            inCache.signs = RailLookup.discoverSignsAtRailPiece(inCache);
            return inCache;
         }
      } else {
         RailType inCacheType = inCache.type();
         if (inCacheType == railType) {
            return inCache;
         } else {
            return inCacheType == RailType.NONE ? inCache.swapOutNoneType(railType) : inCache.findOrAppendToChain(railType);
         }
      }
   }

   public List<MinecartMember<?>> findMembersOnRail(IntVector3 railCoordinates) {
      WorldRailLookupImpl.Bucket bucket = (WorldRailLookupImpl.Bucket)this.cache.get(createCacheKey(railCoordinates));
      return bucket == null ? Collections.emptyList() : bucket.members;
   }

   public List<MinecartMember<?>> findMembersOnRail(OfflineBlock railOfflineBlock) {
      WorldRailLookupImpl.Bucket bucket = (WorldRailLookupImpl.Bucket)this.cache.get(createCacheKey(railOfflineBlock));
      return bucket == null ? Collections.emptyList() : bucket.members;
   }

   public void removeMemberFromAll(MinecartMember<?> member) {
      this.forAllBuckets((b) -> {
         List<MinecartMember<?>> members = b.members;
         if (!members.isEmpty()) {
            members.remove(member);
         }

      });
   }

   public RailLookup.TrackedSign[] discoverSignsAtRailPiece(RailPiece rail) {
      WorldRailLookupImpl.TrackedSignList cache = SIGN_LIST_CACHE.start(rail);

      RailLookup.TrackedSign[] var4;
      label46: {
         try {
            try {
               RailType type = rail.type();
               if (!type.isRegistered()) {
                  throw new RailLookup.RailTypeNotRegisteredException(type);
               }

               type.discoverSigns(rail, this.signController, cache.signs);
               var4 = cache.build();
            } catch (Throwable var6) {
               this.traincarts.getLogger().log(Level.SEVERE, "Failed discover signs for " + rail, var6);
               var4 = RailLookup.NO_SIGNS;
               break label46;
            }
         } catch (Throwable var7) {
            if (cache != null) {
               try {
                  cache.close();
               } catch (Throwable var5) {
                  var7.addSuppressed(var5);
               }
            }

            throw var7;
         }

         if (cache != null) {
            cache.close();
         }

         return var4;
      }

      if (cache != null) {
         cache.close();
      }

      return var4;
   }

   public RailPiece discoverRailPieceFromSign(Block signblock) {
      if (signblock == null) {
         return RailPiece.NONE;
      } else {
         BlockData signblock_data = WorldUtil.getBlockData(signblock);
         Block mainBlock;
         boolean isSignPost;
         if (signblock_data.isType(WALL_SIGN_TYPE)) {
            mainBlock = signblock.getRelative(signblock_data.getAttachedFace());
            isSignPost = false;
         } else {
            if (!signblock_data.isType(SIGN_POST_TYPE)) {
               return RailPiece.NONE;
            }

            mainBlock = signblock.getRelative(signblock_data.getAttachedFace());
            isSignPost = true;
         }

         RailType railType = RailType.getType(mainBlock);
         if (railType != RailType.NONE) {
            return RailPiece.create(railType, mainBlock);
         } else {
            BlockFace[] var6 = SIGN_FACES_ORDERED;
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               BlockFace dir = var6[var8];
               Block block = mainBlock;
               if (isSignPost && dir == BlockFace.DOWN) {
                  block = signblock;
               }

               boolean hasSigns = true;

               while(true) {
                  block = block.getRelative(dir);
                  BlockData blockData = WorldUtil.getBlockData(block);
                  railType = RailType.getType(block, blockData);
                  BlockFace columnDir = railType.getSignColumnDirection(block);
                  if (dir == columnDir.getOppositeFace()) {
                     return RailPiece.create(railType, block);
                  }

                  if (!hasSigns) {
                     break;
                  }

                  if (blockData.isType(SIGN_POST_TYPE)) {
                     hasSigns = true;
                  } else {
                     hasSigns = this.signController.hasSignsAroundColumn(block, dir.getOppositeFace(), false);
                  }
               }
            }

            return RailPiece.NONE;
         }
      }
   }

   public void redetectSignActions() {
      this.forAllBuckets(RailLookup.CachedRailPiece::redetectSignActions);
   }

   private void forAllBuckets(Consumer<WorldRailLookupImpl.Bucket> callback) {
      Iterator var2 = this.cacheValues.iterator();

      while(var2.hasNext()) {
         WorldRailLookupImpl.Bucket bucket = (WorldRailLookupImpl.Bucket)var2.next();

         for(WorldRailLookupImpl.Bucket next = bucket; next != null; next = next.next) {
            callback.accept(next);
         }
      }

   }

   void unloadRailType(RailType type) {
      this.refreshBuckets((bucket) -> {
         return bucket.type() != type;
      }, true);
   }

   void refreshAllBuckets() {
      this.refreshBuckets((bucket) -> {
         bucket.rail_life = 1;
         bucket.rails_at_position_life = 0;
         bucket.rails_at_position = NO_RAILS_AT_POSITION;
         bucket.signs = RailLookup.MISSING_RAILS_NO_SIGNS;
         return false;
      }, false);
   }

   void update(int deadTimeout) {
      this.refreshBuckets((b) -> {
         return b.checkStillValid(deadTimeout);
      }, false);
   }

   private void refreshBuckets(Predicate<WorldRailLookupImpl.Bucket> validChecker, boolean ignoreCanBePurged) {
      ListIterator iter = this.cacheValues.listIterator();

      while(true) {
         while(true) {
            label32:
            while(iter.hasNext()) {
               WorldRailLookupImpl.Bucket bucket = (WorldRailLookupImpl.Bucket)iter.next();
               if (!validChecker.test(bucket) && (ignoreCanBePurged || bucket.canBePurged(bucket.next == null))) {
                  IntVector3 cacheKey = createCacheKey(bucket.blockPosition());

                  do {
                     bucket.rail_life = 0;
                     bucket = bucket.next;
                     if (bucket == null) {
                        iter.remove();
                        this.cache.remove(cacheKey);
                        continue label32;
                     }
                  } while(!validChecker.test(bucket) && (ignoreCanBePurged || bucket.canBePurged(true)));

                  bucket.removeInvalidBucketsFromChain(validChecker, ignoreCanBePurged);
                  iter.set(bucket);
                  this.cache.put(cacheKey, bucket);
               } else {
                  bucket.removeInvalidBucketsFromChain(validChecker, ignoreCanBePurged);
               }
            }

            return;
         }
      }
   }

   public void storeDetectorRegions(IntVector3 coordinates, DetectorRegion[] regions) {
      for(WorldRailLookupImpl.Bucket b = this.getOrCreateAtCoordinates(coordinates); b != null; b = b.next) {
         b.detectorRegions = regions != null && regions.length != 0 ? regions : RailLookup.NO_DETECTOR_REGIONS;
      }

   }

   public DetectorRegion[] getDetectorRegions(IntVector3 coordinates) {
      WorldRailLookupImpl.Bucket bucket = (WorldRailLookupImpl.Bucket)this.cache.get(createCacheKey(coordinates));
      return bucket == null ? RailLookup.NO_DETECTOR_REGIONS : bucket.detectorRegions;
   }

   public Collection<IntVector3> getBlockIndex() {
      return this.cache.keySet();
   }

   private WorldRailLookupImpl.Bucket getOrCreateAtCoordinates(IntVector3 coordinates) {
      IntVector3 cacheKey = createCacheKey(coordinates);
      WorldRailLookupImpl.Bucket bucket = (WorldRailLookupImpl.Bucket)this.cache.get(cacheKey);
      if (bucket == null) {
         bucket = new WorldRailLookupImpl.Bucket(this.offlineWorld.getBlockAt(coordinates), BlockUtil.getBlock(this.world, coordinates));
         this.cache.put(cacheKey, bucket);
         this.cacheValues.add(bucket);
      }

      return bucket;
   }

   private WorldRailLookupImpl.Bucket[] discoverBucketsAtPositionBlock(IntVector3 cacheKey, OfflineBlock positionOfflineBlock) {
      Block positionBlock = positionOfflineBlock.getLoadedBlock();
      if (positionBlock == null) {
         if (this.isValid()) {
            return NO_RAILS_AT_POSITION;
         } else {
            throw new WorldRailLookup.ClosedException();
         }
      } else {
         Iterator var4 = RailType.values().iterator();

         while(var4.hasNext()) {
            RailType type = (RailType)var4.next();

            try {
               List<Block> rails = type.findRails(positionBlock);
               if (!rails.isEmpty()) {
                  WorldRailLookupImpl.Bucket bucketInCache = null;
                  WorldRailLookupImpl.Bucket[] newRailsAtPosition = new WorldRailLookupImpl.Bucket[rails.size()];
                  int index = 0;
                  Iterator var10 = rails.iterator();

                  while(true) {
                     while(var10.hasNext()) {
                        Block railsBlock = (Block)var10.next();
                        if (railsBlock.getX() == positionBlock.getX() && railsBlock.getY() == positionBlock.getY() && railsBlock.getZ() == positionBlock.getZ()) {
                           bucketInCache = new WorldRailLookupImpl.Bucket(positionOfflineBlock, positionBlock, type);
                           newRailsAtPosition[index++] = bucketInCache;
                        } else {
                           OfflineBlock railsOfflineBlock = this.offlineWorld.getBlockAt(railsBlock.getX(), railsBlock.getY(), railsBlock.getZ());
                           newRailsAtPosition[index++] = this.lookupRailBucket(railsOfflineBlock, railsBlock, type);
                        }
                     }

                     if (bucketInCache == null) {
                        bucketInCache = new WorldRailLookupImpl.Bucket(positionOfflineBlock, positionBlock);
                     }

                     this.addToCache(cacheKey, bucketInCache);
                     bucketInCache.rails_at_position = newRailsAtPosition;
                     bucketInCache.signs = RailLookup.discoverSignsAtRailPiece(bucketInCache);
                     return newRailsAtPosition;
                  }
               }
            } catch (Throwable var13) {
               RailType.handleCriticalError(type, var13);
            }
         }

         this.addToCache(cacheKey, new WorldRailLookupImpl.Bucket(positionOfflineBlock, positionBlock));
         return NO_RAILS_AT_POSITION;
      }
   }

   private void addToCache(IntVector3 cacheKey, WorldRailLookupImpl.Bucket bucket) {
      this.cache.put(cacheKey, bucket);
      this.cacheValues.add(bucket);
   }

   private static IntVector3 createCacheKey(OfflineBlock block) {
      return block.getPosition();
   }

   private static IntVector3 createCacheKey(IntVector3 coordinates) {
      return coordinates;
   }

   static {
      SIGN_FACES_ORDERED = new BlockFace[]{BlockFace.UP, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.DOWN};
   }

   private final class Bucket extends RailLookup.CachedRailPiece {
      public WorldRailLookupImpl.Bucket next;
      public int rail_life;
      public int rails_at_position_life;
      public WorldRailLookupImpl.Bucket[] rails_at_position;

      public Bucket(OfflineBlock offlineBlock, Block block) {
         this(offlineBlock, block, RailType.NONE);
      }

      public Bucket(OfflineBlock offlineBlock, Block block, RailType type) {
         super(WorldRailLookupImpl.this, offlineBlock, block, type);
         this.next = null;
         this.signs = RailLookup.MISSING_RAILS_NO_SIGNS;
         this.rail_life = RailLookup.lifeTimer;
         this.rails_at_position_life = 0;
         this.rails_at_position = WorldRailLookupImpl.NO_RAILS_AT_POSITION;
      }

      public boolean checkStillValid(int timeoutTicks) {
         if (this.rail_life < timeoutTicks && this.rails_at_position_life < timeoutTicks) {
            List<MinecartMember<?>> members = this.members;
            if (!members.isEmpty()) {
               Iterator iter = members.iterator();

               while(true) {
                  MinecartMember member;
                  do {
                     if (!iter.hasNext()) {
                        return false;
                     }

                     member = (MinecartMember)iter.next();
                  } while(!member.isUnloaded() && !((CommonMinecart)member.getEntity()).isRemoved());

                  iter.remove();
                  WorldRailLookupImpl.this.traincarts.log(Level.WARNING, "Purged unloaded minecart from rail cache at " + this.offlineBlock().getPosition());
               }
            } else {
               return false;
            }
         } else {
            return true;
         }
      }

      private boolean canBePurged(boolean isOnlyBucketAtBlock) {
         if (!this.members.isEmpty()) {
            return false;
         } else {
            return !isOnlyBucketAtBlock || this.detectorRegions == RailLookup.NO_DETECTOR_REGIONS;
         }
      }

      public WorldRailLookupImpl.Bucket swapOutNoneType(RailType railType) {
         WorldRailLookupImpl.Bucket newBucket = this.cloneAsType(railType);
         newBucket.rails_at_position = this.rails_at_position;
         if (this.members.isEmpty()) {
            this.rail_life = 0;
         } else {
            newBucket.next = this;
         }

         boolean found = false;
         ListIterator iter = WorldRailLookupImpl.this.cacheValues.listIterator();

         while(iter.hasNext()) {
            if (iter.next() == this) {
               iter.set(newBucket);
               found = true;
               break;
            }
         }

         if (!found) {
            WorldRailLookupImpl.this.cacheValues.add(newBucket);
         }

         WorldRailLookupImpl.this.cache.put(WorldRailLookupImpl.createCacheKey(newBucket.blockPosition()), newBucket);
         return newBucket;
      }

      public WorldRailLookupImpl.Bucket findOrAppendToChain(RailType railType) {
         WorldRailLookupImpl.Bucket current = this;

         while(true) {
            WorldRailLookupImpl.Bucket next = current.next;
            if (next == null) {
               WorldRailLookupImpl.Bucket newBucket = current.cloneAsType(railType);
               current.next = newBucket;
               newBucket.signs = RailLookup.discoverSignsAtRailPiece(newBucket);
               return newBucket;
            }

            if (next.type() == railType) {
               return next;
            }

            current = next;
         }
      }

      public void removeInvalidBucketsFromChain(Predicate<WorldRailLookupImpl.Bucket> validChecker, boolean ignoreCanBePurged) {
         WorldRailLookupImpl.Bucket curr = this;

         while(true) {
            WorldRailLookupImpl.Bucket next;
            while((next = curr.next) != null) {
               if (!validChecker.test(next) && (ignoreCanBePurged || next.canBePurged(false))) {
                  next.rail_life = 0;
                  curr.next = next.next;
               } else {
                  curr = next;
               }
            }

            return;
         }
      }

      public WorldRailLookupImpl.Bucket[] getRailsAtPosition() {
         int lifeTimerAtPosition = RailLookup.lifeTimerAtPosition;
         if (this.rails_at_position_life >= lifeTimerAtPosition) {
            return this.rails_at_position;
         } else {
            this.rails_at_position_life = lifeTimerAtPosition;
            WorldRailLookupImpl.Bucket[] currAtPosition = this.rails_at_position;
            if (currAtPosition.length == 0) {
               return this.computeRailsAtPosition();
            } else {
               WorldRailLookupImpl.Bucket[] var3 = currAtPosition;
               int var4 = currAtPosition.length;

               for(int var5 = 0; var5 < var4; ++var5) {
                  WorldRailLookupImpl.Bucket b = var3[var5];
                  if (!b.verify()) {
                     return this.computeRailsAtPosition();
                  }
               }

               return currAtPosition;
            }
         }
      }

      private WorldRailLookupImpl.Bucket cloneAsType(RailType railType) {
         if (!railType.isRegistered()) {
            throw new RailLookup.RailTypeNotRegisteredException(railType);
         } else {
            WorldRailLookupImpl.Bucket newBucket = WorldRailLookupImpl.this.new Bucket(this.offlineBlock(), this.block(), railType);
            newBucket.detectorRegions = this.detectorRegions;
            return newBucket;
         }
      }

      private WorldRailLookupImpl.Bucket[] computeRailsAtPosition() {
         OfflineWorld offlineWorld = this.offlineWorld();
         Block positionBlock = this.block();
         WorldRailLookupImpl.Bucket[] newRailsAtPosition = WorldRailLookupImpl.NO_RAILS_AT_POSITION;
         WorldRailLookupImpl.Bucket bucketInCache = this;
         Iterator var5 = RailType.values().iterator();

         label46:
         while(var5.hasNext()) {
            RailType type = (RailType)var5.next();

            try {
               List<Block> rails = type.findRails(positionBlock);
               if (!rails.isEmpty()) {
                  RailType bucketInCacheType = bucketInCache.type();
                  int index = newRailsAtPosition.length;
                  newRailsAtPosition = (WorldRailLookupImpl.Bucket[])Arrays.copyOf(newRailsAtPosition, index + rails.size());
                  Iterator var10 = rails.iterator();

                  while(true) {
                     while(true) {
                        if (!var10.hasNext()) {
                           continue label46;
                        }

                        Block railsBlock = (Block)var10.next();
                        if (railsBlock.getX() == positionBlock.getX() && railsBlock.getY() == positionBlock.getY() && railsBlock.getZ() == positionBlock.getZ()) {
                           if (bucketInCacheType == type) {
                              newRailsAtPosition[index++] = bucketInCache;
                           } else if (bucketInCacheType == RailType.NONE) {
                              bucketInCache = bucketInCache.swapOutNoneType(type);
                              bucketInCacheType = type;
                              newRailsAtPosition[index++] = bucketInCache;
                           } else {
                              newRailsAtPosition[index++] = bucketInCache.findOrAppendToChain(type);
                           }
                        } else {
                           OfflineBlock railsOfflineBlock = offlineWorld.getBlockAt(railsBlock.getX(), railsBlock.getY(), railsBlock.getZ());
                           newRailsAtPosition[index++] = WorldRailLookupImpl.this.lookupRailBucket(railsOfflineBlock, railsBlock, type);
                        }
                     }
                  }
               }
            } catch (Throwable var13) {
               RailType.handleCriticalError(type, var13);
            }
         }

         return bucketInCache.rails_at_position = newRailsAtPosition;
      }

      public boolean verify() {
         int currLife = this.rail_life;
         if (currLife >= RailLookup.lifeTimer) {
            return true;
         } else if (currLife == 0) {
            return false;
         } else if (!this.type().isRail(this.block())) {
            this.signs = RailLookup.MISSING_RAILS_NO_SIGNS;
            this.rail_life = 1;
            return false;
         } else {
            this.rail_life = RailLookup.verifyTimer;
            RailLookup.TrackedSign[] signs = this.signs;
            if (signs == RailLookup.MISSING_RAILS_NO_SIGNS) {
               this.signs = RailLookup.discoverSignsAtRailPiece(this);
            } else {
               RailLookup.TrackedSign[] var3 = signs;
               int var4 = signs.length;

               for(int var5 = 0; var5 < var4; ++var5) {
                  RailLookup.TrackedSign sign = var3[var5];
                  if (!sign.verify()) {
                     this.signs = RailLookup.discoverSignsAtRailPiece(this);
                     break;
                  }
               }
            }

            return true;
         }
      }

      public boolean verifyExists() {
         return this.rail_life != 0;
      }

      public void forceCacheVerification() {
         this.rail_life = 1;
         this.signs = RailLookup.MISSING_RAILS_NO_SIGNS;
      }
   }

   private static final class TrackedSignList implements AutoCloseable {
      private final List<RailLookup.TrackedSign> signs;
      private RailPiece rail;

      private TrackedSignList() {
         this.signs = new ArrayList();
         this.rail = null;
      }

      public WorldRailLookupImpl.TrackedSignList start(RailPiece rail) {
         if (this.rail == null) {
            this.rail = rail;
            return this;
         } else {
            WorldRailLookupImpl.TrackedSignList copy = new WorldRailLookupImpl.TrackedSignList();
            copy.rail = rail;
            return copy;
         }
      }

      public void close() {
         this.signs.clear();
         this.rail = null;
      }

      public RailLookup.TrackedSign[] build() {
         List<RailLookup.TrackedSign> signs = this.signs;
         return signs.isEmpty() ? RailLookup.NO_SIGNS : (RailLookup.TrackedSign[])signs.toArray(new RailLookup.TrackedSign[signs.size()]);
      }

      // $FF: synthetic method
      TrackedSignList(Object x0) {
         this();
      }
   }
}
