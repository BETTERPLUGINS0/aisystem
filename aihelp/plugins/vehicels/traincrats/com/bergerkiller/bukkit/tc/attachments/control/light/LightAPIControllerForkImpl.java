package com.bergerkiller.bukkit.tc.attachments.control.light;

import com.bergerkiller.bukkit.common.bases.IntVector3;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.bukkit.World;
import ru.beykerykt.lightapi.LightAPI;
import ru.beykerykt.lightapi.LightType;
import ru.beykerykt.lightapi.chunks.ChunkInfo;

class LightAPIControllerForkImpl extends LightAPIController {
   private final World world;
   private final LightType lightType;
   private final Map<IntVector3, LightAPIControllerForkImpl.LevelList> levels;
   private final Map<IntVector3, LightAPIControllerForkImpl.LevelList> dirty;

   public static LightAPIController forSkyLight(World world) {
      return new LightAPIControllerForkImpl(world, LightType.SKY);
   }

   public static LightAPIController forBlockLight(World world) {
      return new LightAPIControllerForkImpl(world, LightType.BLOCK);
   }

   private LightAPIControllerForkImpl(World world, LightType lightType) {
      this.world = world;
      this.lightType = lightType;
      this.levels = new HashMap();
      this.dirty = new HashMap();
   }

   public void add(IntVector3 position, int level) {
      if (level >= 1 && level <= 15) {
         LightAPIControllerForkImpl.LevelList list = (LightAPIControllerForkImpl.LevelList)this.levels.computeIfAbsent(position, (p) -> {
            return new LightAPIControllerForkImpl.LevelList();
         });
         if (list.add(level)) {
            this.dirty.put(position, list);
            this.schedule();
         }
      }

   }

   public void remove(IntVector3 position, int level) {
      LightAPIControllerForkImpl.LevelList list = (LightAPIControllerForkImpl.LevelList)this.levels.get(position);
      if (list != null && list.remove(level)) {
         this.dirty.put(position, list);
         this.schedule();
      }

   }

   public void move(IntVector3 old_position, IntVector3 new_position, int level) {
      this.remove(old_position, level);
      this.add(new_position, level);
   }

   public void update(IntVector3 position, int old_level, int new_level) {
      LightAPIControllerForkImpl.LevelList list = (LightAPIControllerForkImpl.LevelList)this.levels.get(position);
      if (list != null && list.remove(old_level) | list.add(new_level)) {
         this.dirty.put(position, list);
         this.schedule();
      }

   }

   public boolean onSync() {
      if (this.dirty.isEmpty()) {
         return false;
      } else {
         boolean async = true;
         Set<ChunkInfo> chunks = new HashSet();
         Iterator var3 = this.dirty.entrySet().iterator();

         Entry dirty_entry;
         while(var3.hasNext()) {
            dirty_entry = (Entry)var3.next();
            if (((LightAPIControllerForkImpl.LevelList)dirty_entry.getValue()).needsRemoving()) {
               IntVector3 pos = (IntVector3)dirty_entry.getKey();
               LightAPI.deleteLight(this.world, pos.x, pos.y, pos.z, this.lightType, true);
               chunks.addAll(LightAPI.collectChunks(this.world, pos.x, pos.y, pos.z, this.lightType, 15));
            }
         }

         var3 = this.dirty.entrySet().iterator();

         while(var3.hasNext()) {
            dirty_entry = (Entry)var3.next();
            LightAPIControllerForkImpl.LevelList list = (LightAPIControllerForkImpl.LevelList)dirty_entry.getValue();
            if (list.isEmpty()) {
               this.levels.remove(dirty_entry.getKey());
            } else {
               IntVector3 pos = (IntVector3)dirty_entry.getKey();
               int level = list.sync();
               LightAPI.createLight(this.world, pos.x, pos.y, pos.z, this.lightType, level, true);
               chunks.addAll(LightAPI.collectChunks(this.world, pos.x, pos.y, pos.z, this.lightType, level));
            }
         }

         this.dirty.clear();
         var3 = chunks.iterator();

         while(var3.hasNext()) {
            ChunkInfo chunk = (ChunkInfo)var3.next();
            LightAPI.updateChunk(chunk, this.lightType);
         }

         return true;
      }
   }

   private static final class LevelList {
      private static final int[] NO_LEVELS = new int[0];
      private static final int[][] SINGLE_LEVEL = new int[16][1];
      private int sync;
      private int[] levels;

      private LevelList() {
         this.sync = 0;
         this.levels = NO_LEVELS;
      }

      public boolean needsRemoving() {
         return this.levels == NO_LEVELS ? this.sync > 0 : this.sync > this.levels[0];
      }

      public boolean isEmpty() {
         return this.levels == NO_LEVELS;
      }

      public int sync() {
         return this.sync = this.levels[0];
      }

      public boolean add(int level) {
         if (this.levels == NO_LEVELS) {
            this.levels = SINGLE_LEVEL[level];
            return true;
         } else {
            int[] new_levels;
            if (level > this.levels[0]) {
               new_levels = new int[this.levels.length + 1];
               new_levels[0] = level;
               System.arraycopy(this.levels, 0, new_levels, 1, this.levels.length);
               this.levels = new_levels;
               return true;
            } else {
               new_levels = new int[this.levels.length + 1];

               for(int i = 0; i < this.levels.length; ++i) {
                  int other_level = this.levels[i];
                  if (level > other_level) {
                     new_levels[i] = level;
                     System.arraycopy(this.levels, i, new_levels, i + 1, this.levels.length - i);
                     this.levels = new_levels;
                     return false;
                  }

                  new_levels[i] = other_level;
               }

               new_levels[this.levels.length] = level;
               this.levels = new_levels;
               return false;
            }
         }
      }

      public boolean remove(int level) {
         int len = this.levels.length;
         if (len == 1) {
            if (this.levels[0] == level) {
               this.levels = NO_LEVELS;
               return true;
            } else {
               return false;
            }
         } else if (len == 2) {
            if (this.levels[1] == level) {
               this.levels = SINGLE_LEVEL[this.levels[0]];
               return false;
            } else if (this.levels[0] == level) {
               this.levels = SINGLE_LEVEL[this.levels[1]];
               return true;
            } else {
               return false;
            }
         } else if (this.levels[0] == level) {
            int[] new_levels = new int[len - 1];
            System.arraycopy(this.levels, 1, new_levels, 0, len - 1);
            this.levels = new_levels;
            return new_levels[0] != level;
         } else {
            for(byte i = 1; i < len; ++len) {
               if (this.levels[i] == level) {
                  int[] new_levels = new int[len - 1];
                  System.arraycopy(this.levels, 0, new_levels, 0, i);
                  System.arraycopy(this.levels, i + 1, new_levels, i, len - i - 1);
                  this.levels = new_levels;
                  return false;
               }
            }

            return false;
         }
      }

      // $FF: synthetic method
      LevelList(Object x0) {
         this();
      }

      static {
         for(int level = 0; level <= 15; SINGLE_LEVEL[level][0] = level++) {
         }

      }
   }
}
