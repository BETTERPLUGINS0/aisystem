package me.casperge.realisticseasons.seasonevent.buildin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.blockscanner.blocksaver.StoredBlockType;
import me.casperge.realisticseasons.season.SeasonChunk;
import me.casperge.realisticseasons.seasonevent.CustomDatedEvent;
import me.casperge.realisticseasons.utils.BlockUtils;
import me.casperge.realisticseasons.utils.ChunkUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.block.data.Rotatable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Rabbit.Type;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;

public class EasterEvent extends CustomDatedEvent implements DefaultEvent, Listener {
   List<World> enabled = new ArrayList();
   RealisticSeasons main = RealisticSeasons.getInstance();
   Random r = new Random();
   private HashSet<SeasonChunk> spawnLocations = new HashSet();
   public int minChunkDistance = 4;
   public RandomItemStack[] easterEggLoot;
   public boolean spawnKillerBunnies = false;
   public double killerBunnySpawnRate = 0.1D;

   public EasterEvent(ConfigurationSection var1) {
      super(var1);
      Bukkit.getPluginManager().registerEvents(this, this.main);
      Bukkit.getScheduler().runTaskTimer(this.main, new Runnable() {
         int counter = 0;

         public void run() {
            ++this.counter;
            if (this.counter > 200) {
               this.counter = 0;
               if (EasterEvent.this.easterEggLoot == null) {
                  return;
               }

               if (EasterEvent.this.easterEggLoot.length == 0) {
                  return;
               }

               EasterEvent.this.tickEggSpawning();
            }

         }
      }, 1L, 1L);
   }

   public void enable(World var1) {
      if (!this.enabled.contains(var1)) {
         this.enabled.add(var1);
      }

   }

   public void disable(World var1) {
      if (this.enabled.contains(var1)) {
         this.enabled.remove(var1);
      }

      ArrayList var2 = new ArrayList();
      String var3 = var1.getName();
      Iterator var4 = this.spawnLocations.iterator();

      while(var4.hasNext()) {
         SeasonChunk var5 = (SeasonChunk)var4.next();
         if (var5.getWorldName().equals(var3)) {
            var2.add(var5);
         }
      }

      this.spawnLocations.removeAll(var2);
   }

   public boolean isEnabled(World var1) {
      return this.enabled.contains(var1);
   }

   public DefaultEventType getType() {
      return DefaultEventType.EASTER;
   }

   public void placeEgg(Location var1) {
      this.main.getBlockStorage().logPlacement(var1, StoredBlockType.EGG);
      SeasonChunk var2 = new SeasonChunk(var1.getChunk());
      if (!this.spawnLocations.contains(var2)) {
         this.spawnLocations.add(var2);
      }

      var1.getBlock().setType(Material.PLAYER_HEAD);
      Skull var3 = (Skull)var1.getBlock().getState();
      int var4 = this.r.nextInt(4);
      if (var4 == 0) {
         var3 = BlockUtils.getSkullStateFromURL("https://textures.minecraft.net/texture/4272fe51124fde2973456393de7a1eb8c1077451e741299ae8858b390716e80e", var3);
      } else if (var4 == 1) {
         var3 = BlockUtils.getSkullStateFromURL("https://textures.minecraft.net/texture/8e13952025a2324a666f7edf65358325ac63baa379902e390fc27201463193e7", var3);
      } else if (var4 == 2) {
         var3 = BlockUtils.getSkullStateFromURL("https://textures.minecraft.net/texture/c5ff25320a842509b7ee5a23673f685291c4672b834d9b577e862a920983c10b", var3);
      } else {
         var3 = BlockUtils.getSkullStateFromURL("https://textures.minecraft.net/texture/a6a6051f7f6f439d8f214c234e8e2c477630052432e42607f0404b840b53ceab", var3);
      }

      Rotatable var5 = (Rotatable)var3.getBlockData();
      var4 = this.r.nextInt(4);
      if (var4 == 0) {
         var5.setRotation(BlockFace.NORTH);
      } else if (var4 == 1) {
         var5.setRotation(BlockFace.NORTH_EAST);
      } else if (var4 == 2) {
         var5.setRotation(BlockFace.NORTH_NORTH_EAST);
      } else {
         var5.setRotation(BlockFace.EAST_NORTH_EAST);
      }

      var3.setBlockData(var5);
      var3.update(true);
   }

   public void eggOpened(Player var1, Location var2) {
      var2 = var2.add(0.5D, 0.0D, 0.5D);
      this.main.getBlockStorage().logBreak(var2, StoredBlockType.EGG);
      if (this.easterEggLoot.length != 0) {
         List var3 = this.getEggLoot();
         var1.getWorld().playSound(var2, Sound.ENTITY_CHICKEN_EGG, 1.0F, this.r.nextFloat() / 3.0F + 1.1F);
         var1.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, var2, 20, 0.4D, 0.6D, 0.4D, 1.0D);
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            ItemStack var5 = (ItemStack)var4.next();
            var1.getWorld().dropItem(var2, var5);
         }

      }
   }

   private List<ItemStack> getEggLoot() {
      ItemStack var1 = DefaultEvent.randomItemFromList(this.easterEggLoot);
      return Arrays.asList(var1);
   }

   private boolean canEggSpawn(Chunk var1) {
      String var2 = var1.getWorld().getName();
      Iterator var3 = this.spawnLocations.iterator();

      SeasonChunk var4;
      do {
         if (!var3.hasNext()) {
            if (this.main.getBlockStorage().getEggs(var1).size() > 0) {
               return false;
            }

            if (!this.main.hasBlockChanges(var1.getX(), var1.getZ(), var1.getWorld())) {
               return false;
            }

            return true;
         }

         var4 = (SeasonChunk)var3.next();
      } while(!var4.getWorldName().equals(var2) || !(ChunkUtils.get2dDistance(var1.getX(), var1.getZ(), var4.getX(), var4.getZ()) < (double)this.minChunkDistance));

      return false;
   }

   @EventHandler
   public void onMobSpawn(CreatureSpawnEvent var1) {
      if (this.spawnKillerBunnies) {
         if (this.enabled.size() != 0) {
            if (this.enabled.contains(var1.getLocation().getWorld())) {
               if ((var1.getEntityType() == EntityType.ZOMBIE || var1.getEntityType() == EntityType.SKELETON || var1.getEntityType() == EntityType.CREEPER || var1.getEntityType() == EntityType.SPIDER) && var1.getSpawnReason() == SpawnReason.NATURAL && this.r.nextDouble() < this.killerBunnySpawnRate) {
                  var1.setCancelled(true);
                  Rabbit var2 = (Rabbit)var1.getLocation().getWorld().spawnEntity(var1.getLocation(), EntityType.RABBIT);
                  var2.addScoreboardTag("RSEASTER");
                  var2.setRabbitType(Type.THE_KILLER_BUNNY);
                  var2.setCustomNameVisible(false);
                  var2.setRemoveWhenFarAway(true);
               }

            }
         }
      }
   }

   public void tickEggSpawning() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.enabled.iterator();

      while(true) {
         label62:
         while(var2.hasNext()) {
            World var3 = (World)var2.next();
            if (!Bukkit.getWorlds().contains(var3)) {
               var1.add(var3);
            } else {
               Iterator var4 = var3.getPlayers().iterator();

               while(true) {
                  while(true) {
                     Chunk var10;
                     do {
                        Chunk var6;
                        int var8;
                        int var9;
                        do {
                           if (!var4.hasNext()) {
                              continue label62;
                           }

                           Player var5 = (Player)var4.next();
                           var6 = var5.getLocation().getChunk();
                           int var7 = Bukkit.getViewDistance() / 2;
                           if (var7 < 4) {
                              var7 = 4;
                           }

                           var8 = this.r.nextInt(var7 * 2 + 1) - var7;
                           var9 = this.r.nextInt(var7 * 2 + 1) - var7;
                        } while(!var3.isChunkLoaded(var6.getX() + var8, var6.getZ() + var9));

                        var10 = var3.getChunkAt(var6.getX() + var8, var6.getZ() + var9);
                     } while(!this.canEggSpawn(var10));

                     Block var11 = var3.getHighestBlockAt(var10.getBlock(this.r.nextInt(16), 0, this.r.nextInt(16)).getLocation());
                     if (this.isSolidBlock(var11)) {
                        if (var11.getRelative(0, 1, 0).getType() == Material.AIR) {
                           this.placeEgg(var11.getRelative(0, 1, 0).getLocation());
                        }
                     } else if (var11.getType().toString().contains("LEAVE")) {
                        for(int var12 = -1; var12 + var11.getLocation().getBlockY() - 1 > this.main.getNMSUtils().getMinHeight(var11.getLocation().getWorld()); --var12) {
                           if (var11.getRelative(0, var12, 0).getType() == Material.AIR && this.isSolidBlock(var11.getRelative(0, var12 - 1, 0))) {
                              this.placeEgg(var11.getRelative(0, var12, 0).getLocation());
                           }
                        }
                     }
                  }
               }
            }
         }

         this.enabled.removeAll(var1);
         return;
      }
   }

   private boolean isSolidBlock(Block var1) {
      return var1.getType().isOccluding() && var1.getType().isSolid() && var1.getType() != Material.WATER && var1.getType() != Material.LAVA && var1.getType() != Material.FARMLAND && var1.getType() != Material.ICE && !var1.getType().toString().contains("STAIR") && !var1.getType().toString().contains("SLAB") && !var1.getType().toString().contains("FENCE") && var1.getType() != Material.CRIMSON_NYLIUM && var1.getType() != Material.WARPED_NYLIUM && var1.getType() != Material.BARRIER;
   }
}
