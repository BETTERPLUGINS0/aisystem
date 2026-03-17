package com.bergerkiller.bukkit.tc.controller.global;

import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import com.bergerkiller.bukkit.tc.rails.WorldRailLookup;
import java.util.List;
import org.bukkit.Chunk;
import org.bukkit.block.Block;

public class SignControllerChunk {
   public final long chunkKey;
   private SignController.EntryList entries;
   private SignController.EntryList entriesWithSignActions;
   private SignControllerChunk.LoadLevel neighbouringBlocksLoadLevel;
   private int lastVerifyTick;

   public static long getKeyOf(Chunk chunk) {
      return MathUtil.longHashToLong(chunk.getX(), chunk.getZ());
   }

   public SignControllerChunk(long chunkKey) {
      this.entries = SignController.EntryList.NONE;
      this.entriesWithSignActions = null;
      this.neighbouringBlocksLoadLevel = SignControllerChunk.LoadLevel.NOT_LOADED;
      this.lastVerifyTick = -1;
      this.chunkKey = chunkKey;
   }

   public void initialize(List<SignController.Entry> entries) {
      this.entries = SignController.EntryList.of(entries);
      this.entriesWithSignActions = null;
      this.neighbouringBlocksLoadLevel = SignControllerChunk.LoadLevel.NOT_LOADED;
   }

   public boolean hasSigns() {
      return this.entries.count() > 0;
   }

   public SignController.Entry[] getEntries() {
      return this.entries.unsortedValues();
   }

   public void addEntry(SignController.Entry entry) {
      SignController.EntryList entries = this.entries;
      SignController.EntryList entriesWithSignActions = this.entriesWithSignActions;
      if (entriesWithSignActions == null) {
         this.entries = entries.add(entry);
      } else if (!entry.hasSignActionEvents()) {
         this.entries = entries.add(entry);
      } else if (entries == entriesWithSignActions) {
         this.entries = this.entriesWithSignActions = entries.add(entry);
      } else {
         this.entries = entries.add(entry);
         this.entriesWithSignActions = entriesWithSignActions.add(entry);
      }

      SignControllerChunk.LoadLevel neighbouringBlocksLoadLevel = this.neighbouringBlocksLoadLevel;
      if (neighbouringBlocksLoadLevel != SignControllerChunk.LoadLevel.NOT_LOADED && (neighbouringBlocksLoadLevel == SignControllerChunk.LoadLevel.ALL_SIGNS || entry.hasSignActionEvents())) {
         entry.registerInNeighbouringBlocks();
      }

   }

   public void removeEntry(SignController.Entry entry) {
      this.entries = this.entries.filter((e) -> {
         return e != entry;
      });
      if (entry.hasSignActionEvents()) {
         this.entriesWithSignActions = null;
      }

      entry.unregisterInNeighbouringBlocks();
   }

   public void updateEntryHasSignActions(SignController.Entry entry, boolean hasSignActions) {
      SignController.EntryList entriesWithSignActions = this.entriesWithSignActions;
      if (entriesWithSignActions != null) {
         if (!hasSignActions) {
            this.entriesWithSignActions = entriesWithSignActions.filter((e) -> {
               return e != entry;
            });
         } else if (!entriesWithSignActions.contains(entry)) {
            this.entriesWithSignActions = entriesWithSignActions.add(entry);
         }
      }

      SignControllerChunk.LoadLevel neighbouringBlocksLoadLevel = this.neighbouringBlocksLoadLevel;
      if (neighbouringBlocksLoadLevel != SignControllerChunk.LoadLevel.NOT_LOADED && (neighbouringBlocksLoadLevel == SignControllerChunk.LoadLevel.ALL_SIGNS || hasSignActions)) {
         entry.registerInNeighbouringBlocks();
         WorldRailLookup railLookup = RailLookup.forWorldIfInitialized(entry.world.getWorld());
         if (railLookup != null) {
            railLookup.discoverRailPieceFromSign(entry.sign.getBlock()).forceCacheVerification();
         }
      }

   }

   public void verifyEntries() {
      SignController.Entry[] var1 = this.getEntries();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         SignController.Entry e = var1[var3];
         if (!e.verify()) {
            e.removeInvalidEntry();
         }
      }

   }

   public boolean checkMayHaveSigns(int x, int y, int z, boolean mustHaveSignActions, int currentTick) {
      if (currentTick != this.lastVerifyTick) {
         this.lastVerifyTick = currentTick;
         this.verifyEntries();
      }

      SignController.EntryList entries;
      if (mustHaveSignActions) {
         entries = this.entriesWithSignActions;
         if (entries == null) {
            this.entriesWithSignActions = entries = this.entries.filter(SignController.Entry::hasSignActionEvents);
         }
      } else {
         entries = this.entries;
      }

      int entryCount = entries.count();
      if (entryCount == 0) {
         return false;
      } else {
         int var11;
         if (entryCount <= 20) {
            boolean mayHaveSigns = false;
            SignController.Entry[] var9 = entries.unsortedValues();
            int var10 = var9.length;

            for(var11 = 0; var11 < var10; ++var11) {
               SignController.Entry entry = var9[var11];
               Block b = entry.getBlock();
               if (Math.abs(b.getX() - x) <= 2 && Math.abs(b.getY() - y) <= 2 && Math.abs(b.getZ() - z) <= 2) {
                  mayHaveSigns = true;
                  break;
               }
            }

            if (!mayHaveSigns) {
               return false;
            }
         }

         SignControllerChunk.LoadLevel currentLevel = this.neighbouringBlocksLoadLevel;
         SignControllerChunk.LoadLevel requestedLevel = mustHaveSignActions ? SignControllerChunk.LoadLevel.WITH_SIGN_ACTIONS_ONLY : SignControllerChunk.LoadLevel.ALL_SIGNS;
         if (requestedLevel.level() > currentLevel.level()) {
            SignController.Entry[] var16 = entries.unsortedValues();
            var11 = var16.length;

            for(int var17 = 0; var17 < var11; ++var17) {
               SignController.Entry e = var16[var17];
               e.registerInNeighbouringBlocks();
            }

            this.neighbouringBlocksLoadLevel = requestedLevel;
         }

         return true;
      }
   }

   private static enum LoadLevel {
      NOT_LOADED(0),
      WITH_SIGN_ACTIONS_ONLY(1),
      ALL_SIGNS(2);

      private final int level;

      private LoadLevel(int level) {
         this.level = level;
      }

      public int level() {
         return this.level;
      }

      // $FF: synthetic method
      private static SignControllerChunk.LoadLevel[] $values() {
         return new SignControllerChunk.LoadLevel[]{NOT_LOADED, WITH_SIGN_ACTIONS_ONLY, ALL_SIGNS};
      }
   }
}
