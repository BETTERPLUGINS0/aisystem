package me.casperge.realisticseasons.seasonevent.buildin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.blockscanner.blocksaver.StoredBlockType;
import me.casperge.realisticseasons.particle.ParticleManager;
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
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

public class ChristmasEvent extends CustomDatedEvent implements DefaultEvent {
   List<World> enabled = new ArrayList();
   RealisticSeasons main = RealisticSeasons.getInstance();
   Random r = new Random();
   private HashSet<SeasonChunk> spawnLocations = new HashSet();
   public int minChunkDistance = 10;
   public boolean nightParticles = false;
   public boolean christmasTreesEnabled = false;
   public boolean presentsEnabled = false;
   public RandomItemStack[] presentLoot;

   public ChristmasEvent(ConfigurationSection var1) {
      super(var1);
      Bukkit.getScheduler().runTaskTimer(this.main, new Runnable() {
         int counter = 0;

         public void run() {
            ++this.counter;
            ArrayList var1 = new ArrayList();
            Iterator var2 = ChristmasEvent.this.enabled.iterator();

            while(true) {
               while(var2.hasNext()) {
                  World var3 = (World)var2.next();
                  if (!Bukkit.getWorlds().contains(var3)) {
                     var1.add(var3);
                  } else if (var3.getFullTime() % 24000L > 13670L && var3.getFullTime() % 24000L < 22812L && !var3.hasStorm()) {
                     Iterator var4 = var3.getPlayers().iterator();

                     while(var4.hasNext()) {
                        Player var5 = (Player)var4.next();
                        if (ChristmasEvent.this.nightParticles) {
                           ChristmasEvent.this.playNightParticles(var5);
                        }
                     }
                  }
               }

               ChristmasEvent.this.enabled.removeAll(var1);
               var1.clear();
               if (this.counter > 200) {
                  this.counter = 0;
                  if (ChristmasEvent.this.presentsEnabled) {
                     ChristmasEvent.this.tickPresentSpawning();
                  }
               }

               return;
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
      return DefaultEventType.CHRISTMAS;
   }

   private void playNightParticles(Player var1) {
      if (!ParticleManager.disabledParticles.contains(var1.getUniqueId())) {
         var1.spawnParticle(Particle.FIREWORKS_SPARK, var1.getLocation().getX(), var1.getLocation().getY() + 25.0D, var1.getLocation().getZ(), 30, 40.0D, 15.0D, 40.0D, 0.0D);
         var1.spawnParticle(Particle.SCRAPE, var1.getLocation().getX(), var1.getLocation().getY() + 25.0D, var1.getLocation().getZ(), 30, 40.0D, 5.0D, 40.0D, 0.0D);
         var1.spawnParticle(Particle.VILLAGER_HAPPY, var1.getLocation().getX(), var1.getLocation().getY() + 25.0D, var1.getLocation().getZ(), 30, 40.0D, 5.0D, 40.0D, 0.0D);
      }
   }

   public void placePresent(Location var1) {
      this.main.getBlockStorage().logPlacement(var1, StoredBlockType.PRESENT);
      SeasonChunk var2 = new SeasonChunk(var1.getChunk());
      if (!this.spawnLocations.contains(var2)) {
         this.spawnLocations.add(var2);
      }

      var1.getBlock().setType(Material.PLAYER_HEAD);
      Skull var3 = (Skull)var1.getBlock().getState();
      if (this.r.nextInt(2) == 0) {
         var3 = BlockUtils.getSkullStateFromURL("https://textures.minecraft.net/texture/79af6ba11cad246cfdcfc2063d264c4e1738993138c3af3402f132373515195c", var3);
      } else {
         var3 = BlockUtils.getSkullStateFromURL("https://textures.minecraft.net/texture/ed90816936569293a3824e6f1e7ea34a552fe90264b0352d914f8f645528b93d", var3);
      }

      Rotatable var4 = (Rotatable)var3.getBlockData();
      int var5 = this.r.nextInt(4);
      if (var5 == 0) {
         var4.setRotation(BlockFace.NORTH);
      } else if (var5 == 1) {
         var4.setRotation(BlockFace.NORTH_EAST);
      } else if (var5 == 2) {
         var4.setRotation(BlockFace.NORTH_NORTH_EAST);
      } else {
         var4.setRotation(BlockFace.EAST_NORTH_EAST);
      }

      var3.setBlockData(var4);
      var3.update(true);
   }

   public void presentOpened(Player var1, Location var2) {
      var2 = var2.add(0.5D, 0.0D, 0.5D);
      this.main.getBlockStorage().logBreak(var2, StoredBlockType.PRESENT);
      List var3 = this.getPresentLoot();
      var1.getWorld().playSound(var2, Sound.ENTITY_CHICKEN_EGG, 1.0F, this.r.nextFloat() / 3.0F + 0.3F);
      var1.getWorld().spawnParticle(Particle.SMOKE_NORMAL, var2, 20, 0.0D, 0.1D, 0.0D, 0.05D);
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         ItemStack var5 = (ItemStack)var4.next();
         var1.getWorld().dropItem(var2, var5);
      }

   }

   private List<ItemStack> getPresentLoot() {
      ItemStack var1 = DefaultEvent.randomItemFromList(this.presentLoot);
      return Arrays.asList(var1);
   }

   private boolean canPresentSpawn(Chunk var1) {
      String var2 = var1.getWorld().getName();
      Iterator var3 = this.spawnLocations.iterator();

      SeasonChunk var4;
      do {
         if (!var3.hasNext()) {
            if (this.main.getBlockStorage().getPresents(var1).size() > 0) {
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

   public void tickPresentSpawning() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.enabled.iterator();

      while(true) {
         label64:
         while(var2.hasNext()) {
            World var3 = (World)var2.next();
            if (!Bukkit.getWorlds().contains(var3)) {
               var1.add(var3);
            } else {
               HashSet var4 = new HashSet();
               Iterator var5 = var3.getEntitiesByClass(Villager.class).iterator();

               while(true) {
                  Chunk var9;
                  do {
                     do {
                        Villager var6;
                        do {
                           if (!var5.hasNext()) {
                              continue label64;
                           }

                           var6 = (Villager)var5.next();
                        } while(!var6.hasAI());

                        int var7 = this.r.nextInt(3) - 1;
                        int var8 = this.r.nextInt(3) - 1;
                        var9 = var6.getWorld().getChunkAt(var6.getLocation().getChunk().getX() + var7, var6.getLocation().getChunk().getZ() + var8);
                     } while(var4.contains(var9));

                     var4.add(var9);
                  } while(!this.canPresentSpawn(var9));

                  for(int var10 = 0; var10 < 5; ++var10) {
                     Block var11 = var3.getHighestBlockAt(var9.getBlock(this.r.nextInt(16), 0, this.r.nextInt(16)).getLocation());
                     if (var11.getType().toString().contains("LEAVE")) {
                        for(int var12 = -1; var12 + var11.getLocation().getBlockY() - 1 > this.main.getNMSUtils().getMinHeight(var11.getLocation().getWorld()); --var12) {
                           if (var11.getRelative(0, var12, 0).getType() == Material.AIR && this.isSolidBlock(var11.getRelative(0, var12 - 1, 0))) {
                              this.generatePresentsUnderneathTree(var3, var11.getX(), var11.getZ());
                              break;
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

   private void generatePresentsUnderneathTree(World var1, int var2, int var3) {
      for(int var4 = -4; var4 <= 4; ++var4) {
         for(int var5 = -4; var5 <= 4; ++var5) {
            if (this.r.nextInt(4) == 0) {
               Block var6 = var1.getHighestBlockAt(var2 + var4, var3 + var5);
               if (var6.getType().toString().contains("LEAVE")) {
                  for(int var7 = -1; var7 + var6.getLocation().getBlockY() - 1 > this.main.getNMSUtils().getMinHeight(var6.getLocation().getWorld()); --var7) {
                     if (var6.getRelative(0, var7, 0).getType() == Material.AIR && this.isSolidBlock(var6.getRelative(0, var7 - 1, 0))) {
                        this.placePresent(var6.getRelative(0, var7, 0).getLocation());
                     }
                  }
               }
            }
         }
      }

   }

   private boolean isSolidBlock(Block var1) {
      return var1.getType().isOccluding() && var1.getType().isSolid() && var1.getType() != Material.WATER && var1.getType() != Material.LAVA && var1.getType() != Material.FARMLAND && var1.getType() != Material.ICE && !var1.getType().toString().contains("STAIR") && !var1.getType().toString().contains("SLAB") && !var1.getType().toString().contains("FENCE") && var1.getType() != Material.CRIMSON_NYLIUM && var1.getType() != Material.WARPED_NYLIUM && var1.getType() != Material.BARRIER;
   }
}
