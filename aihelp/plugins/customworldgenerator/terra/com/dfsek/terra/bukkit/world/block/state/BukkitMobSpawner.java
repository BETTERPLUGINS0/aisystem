package com.dfsek.terra.bukkit.world.block.state;

import com.dfsek.terra.api.block.entity.MobSpawner;
import com.dfsek.terra.api.block.entity.SerialState;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.bukkit.util.BukkitUtils;
import com.dfsek.terra.bukkit.world.entity.BukkitEntityType;
import org.bukkit.block.CreatureSpawner;
import org.jetbrains.annotations.NotNull;

public class BukkitMobSpawner extends BukkitBlockEntity implements MobSpawner {
   protected BukkitMobSpawner(CreatureSpawner block) {
      super(block);
   }

   public EntityType getSpawnedType() {
      return new BukkitEntityType(((CreatureSpawner)this.getHandle()).getSpawnedType());
   }

   public void setSpawnedType(@NotNull EntityType creatureType) {
      ((CreatureSpawner)this.getHandle()).setSpawnedType(((BukkitEntityType)creatureType).getHandle());
   }

   public int getDelay() {
      return ((CreatureSpawner)this.getHandle()).getDelay();
   }

   public void setDelay(int delay) {
      ((CreatureSpawner)this.getHandle()).setDelay(delay);
   }

   public int getMinSpawnDelay() {
      return ((CreatureSpawner)this.getHandle()).getMinSpawnDelay();
   }

   public void setMinSpawnDelay(int delay) {
      ((CreatureSpawner)this.getHandle()).setMinSpawnDelay(delay);
   }

   public int getMaxSpawnDelay() {
      return ((CreatureSpawner)this.getHandle()).getMaxSpawnDelay();
   }

   public void setMaxSpawnDelay(int delay) {
      ((CreatureSpawner)this.getHandle()).setMaxSpawnDelay(delay);
   }

   public int getSpawnCount() {
      return ((CreatureSpawner)this.getHandle()).getSpawnCount();
   }

   public void setSpawnCount(int spawnCount) {
      ((CreatureSpawner)this.getHandle()).setSpawnCount(spawnCount);
   }

   public int getMaxNearbyEntities() {
      return ((CreatureSpawner)this.getHandle()).getMaxNearbyEntities();
   }

   public void setMaxNearbyEntities(int maxNearbyEntities) {
      ((CreatureSpawner)this.getHandle()).setMaxNearbyEntities(maxNearbyEntities);
   }

   public int getRequiredPlayerRange() {
      return ((CreatureSpawner)this.getHandle()).getRequiredPlayerRange();
   }

   public void setRequiredPlayerRange(int requiredPlayerRange) {
      ((CreatureSpawner)this.getHandle()).setRequiredPlayerRange(requiredPlayerRange);
   }

   public int getSpawnRange() {
      return ((CreatureSpawner)this.getHandle()).getSpawnRange();
   }

   public void setSpawnRange(int spawnRange) {
      ((CreatureSpawner)this.getHandle()).setSpawnRange(spawnRange);
   }

   public void applyState(String state) {
      SerialState.parse(state).forEach((k, v) -> {
         byte var4 = -1;
         switch(k.hashCode()) {
         case -1658265781:
            if (k.equals("spawn_count")) {
               var4 = 4;
            }
            break;
         case -1644836999:
            if (k.equals("spawn_range")) {
               var4 = 5;
            }
            break;
         case -248633624:
            if (k.equals("max_delay")) {
               var4 = 3;
            }
            break;
         case 3575610:
            if (k.equals("type")) {
               var4 = 0;
            }
            break;
         case 95467907:
            if (k.equals("delay")) {
               var4 = 1;
            }
            break;
         case 523844950:
            if (k.equals("min_delay")) {
               var4 = 2;
            }
            break;
         case 1168271802:
            if (k.equals("max_nearby")) {
               var4 = 6;
            }
            break;
         case 1553605375:
            if (k.equals("required_player_range")) {
               var4 = 7;
            }
         }

         switch(var4) {
         case 0:
            this.setSpawnedType(BukkitUtils.getEntityType(v));
            break;
         case 1:
            this.setDelay(Integer.parseInt(v));
            break;
         case 2:
            this.setMinSpawnDelay(Integer.parseInt(v));
            break;
         case 3:
            this.setMaxSpawnDelay(Integer.parseInt(v));
            break;
         case 4:
            this.setSpawnCount(Integer.parseInt(v));
            break;
         case 5:
            this.setSpawnRange(Integer.parseInt(v));
            break;
         case 6:
            this.setMaxNearbyEntities(Integer.parseInt(v));
            break;
         case 7:
            this.setRequiredPlayerRange(Integer.parseInt(v));
            break;
         default:
            throw new IllegalArgumentException("Invalid property: " + k);
         }

      });
   }
}
