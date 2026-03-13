package me.casperge.realisticseasons.particle.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import me.casperge.interfaces.FakeArmorStand;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.utils.BlockUtils;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;

public class FireworkBox implements SeasonEntity {
   private int counter = 0;
   private Location currentLocation;
   private FakeArmorStand fakearm;
   private boolean isDestroyed = false;
   private Location fireworkSpawn;
   Random r = new Random();
   private FireworkBox.BoxType boxType;
   private static List<List<Color>> colorCombinations = new ArrayList();
   List<Color> col;
   int subCounter = 0;

   public FireworkBox(Location var1, List<Player> var2) {
      this.currentLocation = var1.clone();
      int var3 = this.r.nextInt(90);
      this.fakearm = NMSEntityCreator.createFakeArmorStand(var1.getWorld(), var1.getX(), var1.getY(), var1.getZ(), (double)var3, true, false, false, true);
      RealisticSeasons.getInstance().getBlockUtils();
      ItemStack var4 = BlockUtils.getSkullFromURL("https://textures.minecraft.net/texture/38d80770db8e1a98b6f9e6d267f3f2af9aea34deb3ec4f8da7c37d6793d794");
      this.fakearm.setItemSlot(5, var4);
      this.fireworkSpawn = this.fakearm.getLocation().add(new Location(this.fakearm.getLocation().getWorld(), 0.0D, 1.5D, 0.0D));
      this.fakearm.sendSpawnPacket(var2);
      this.currentLocation.setYaw((float)var3);
      this.boxType = FireworkBox.BoxType.values()[this.r.nextInt(FireworkBox.BoxType.values().length)];
   }

   public void tick(List<Player> var1) {
      if (!this.isDestroyed) {
         int var2;
         switch(this.boxType.ordinal()) {
         case 0:
            if (this.col == null) {
               this.col = Arrays.asList(Color.fromBGR(this.r.nextInt(255), this.r.nextInt(255), this.r.nextInt(255)));
            }

            if (this.counter % 20 == 0) {
               this.shootFirework(4, false, true, Type.BALL_LARGE, this.col);
            }
            break;
         case 1:
            if (this.col == null || this.counter % 150 == 0) {
               var2 = this.r.nextInt(2);
               if (var2 == 0) {
                  this.col = (List)colorCombinations.get(this.r.nextInt(colorCombinations.size()));
               } else {
                  this.col = Arrays.asList(Color.fromBGR(this.r.nextInt(255), this.r.nextInt(255), this.r.nextInt(255)));
               }
            }

            if (this.counter % 50 < 7) {
               this.shootFirework(3, false, false, Type.BALL_LARGE, this.col);
            }
            break;
         case 2:
            if (this.counter % 10 == 0) {
               if (this.subCounter == 0) {
                  this.shootFirework(2, true, false, Type.BALL, Arrays.asList(Color.RED));
                  ++this.subCounter;
               } else if (this.subCounter == 1) {
                  this.shootFirework(2, true, false, Type.BALL, Arrays.asList(Color.ORANGE));
                  ++this.subCounter;
               } else if (this.subCounter == 2) {
                  this.shootFirework(2, true, false, Type.BALL, Arrays.asList(Color.YELLOW));
                  ++this.subCounter;
               } else if (this.subCounter == 3) {
                  this.shootFirework(2, true, false, Type.BALL, Arrays.asList(Color.GREEN));
                  ++this.subCounter;
               } else if (this.subCounter == 4) {
                  this.shootFirework(2, true, false, Type.BALL, Arrays.asList(Color.BLUE));
                  ++this.subCounter;
               } else if (this.subCounter == 5) {
                  this.shootFirework(2, true, false, Type.BALL, Arrays.asList(Color.PURPLE));
                  this.subCounter = 0;
               }
            }
            break;
         case 3:
            if (this.col == null || this.counter % 75 == 0) {
               int var5 = this.r.nextInt(2);
               if (var5 == 0) {
                  this.col = (List)colorCombinations.get(this.r.nextInt(colorCombinations.size()));
               } else {
                  this.col = Arrays.asList(Color.fromBGR(this.r.nextInt(255), this.r.nextInt(255), this.r.nextInt(255)));
               }
            }

            if (this.counter % 20 < 3) {
               this.shootFirework(1, true, false, Type.BURST, this.col);
            }
            break;
         case 4:
            var2 = this.r.nextInt(2);
            if (var2 == 0) {
               this.col = (List)colorCombinations.get(this.r.nextInt(colorCombinations.size()));
            } else {
               this.col = Arrays.asList(Color.fromBGR(this.r.nextInt(255), this.r.nextInt(255), this.r.nextInt(255)));
            }

            if (this.counter % 20 == 0) {
               boolean var3 = true;
               boolean var4 = true;
               if (this.r.nextInt(2) == 0) {
                  var3 = false;
               }

               if (this.r.nextInt(2) == 0) {
                  var4 = false;
               }

               this.shootFirework(this.r.nextInt(4) + 1, var3, var4, Type.values()[this.r.nextInt(Type.values().length)], this.col);
            }
            break;
         case 5:
            if (this.col == null || this.counter % 75 == 0) {
               this.col = (List)colorCombinations.get(this.r.nextInt(colorCombinations.size()));
            }

            if (this.counter % 4 == 0) {
               this.shootFirework(1, false, false, Type.BALL, this.col);
            }
            break;
         case 6:
            if (this.col == null || this.counter % 100 == 0) {
               var2 = this.r.nextInt(2);
               if (var2 == 0) {
                  this.col = (List)colorCombinations.get(this.r.nextInt(colorCombinations.size()));
               } else {
                  this.col = Arrays.asList(Color.fromBGR(this.r.nextInt(255), this.r.nextInt(255), this.r.nextInt(255)));
               }
            }

            if (this.counter % 10 == 0) {
               this.shootFirework(this.r.nextInt(3), false, false, Type.BURST, this.col);
            }
            break;
         case 7:
            if (this.counter % 30 == 0) {
               this.shootFirework(this.r.nextInt(2) + 1, false, false, Type.CREEPER, Arrays.asList(Color.LIME));
            }
            break;
         case 8:
            if (this.col == null || this.counter % 75 == 0) {
               var2 = this.r.nextInt(2);
               if (var2 == 0) {
                  this.col = (List)colorCombinations.get(this.r.nextInt(colorCombinations.size()));
               } else {
                  this.col = Arrays.asList(Color.fromBGR(this.r.nextInt(255), this.r.nextInt(255), this.r.nextInt(255)));
               }
            }

            if (this.counter % 8 == 0 && this.counter % 90 < 60) {
               this.shootFirework(this.r.nextInt(4) + 1, false, false, Type.BURST, this.col);
            }
         }
      }

      if (this.counter > 450) {
         this.fakearm.destroy(var1);
         this.isDestroyed = true;
      }

      ++this.counter;
   }

   public boolean isDestroyed() {
      return this.isDestroyed;
   }

   public Location getLocation() {
      return this.fakearm.getLocation();
   }

   public void shootFirework(int var1, boolean var2, boolean var3, Type var4, List<Color> var5) {
      double var6 = (double)this.r.nextFloat() / 10.0D - 0.05D;
      double var8 = (double)this.r.nextFloat() / 10.0D - 0.05D;
      Firework var10 = (Firework)this.fireworkSpawn.getWorld().spawnEntity(this.fireworkSpawn.add(new Location(this.fireworkSpawn.getWorld(), var6, 0.0D, var8)), EntityType.FIREWORK);
      var10.setMetadata("nodamage", new FixedMetadataValue(RealisticSeasons.getInstance(), true));
      FireworkMeta var11 = var10.getFireworkMeta();
      var11.setPower(var1);
      var11.addEffect(FireworkEffect.builder().flicker(var2).trail(var3).with(var4).withColor(var5).build());
      var10.setFireworkMeta(var11);
      this.fireworkSpawn.getWorld().spawnParticle(Particle.CLOUD, this.fireworkSpawn.getX(), this.fireworkSpawn.getY(), this.fireworkSpawn.getZ(), 5, 0.0D, 0.0D, 0.0D, 0.2D, (Object)null);
      this.fireworkSpawn.getWorld().playSound(this.fireworkSpawn, Sound.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 3.0F, 0.4F);
   }

   static {
      colorCombinations.add(new ArrayList(Arrays.asList(Color.AQUA, Color.RED)));
      colorCombinations.add(new ArrayList(Arrays.asList(Color.LIME, Color.ORANGE)));
      colorCombinations.add(new ArrayList(Arrays.asList(Color.PURPLE, Color.YELLOW)));
      colorCombinations.add(new ArrayList(Arrays.asList(Color.ORANGE, Color.YELLOW, Color.LIME)));
      colorCombinations.add(new ArrayList(Arrays.asList(Color.BLUE, Color.ORANGE)));
   }

   static enum BoxType {
      LONG_ROCKETS,
      LONG_BURST,
      RAINBOW,
      SHORT_BURST,
      RANDOM,
      BALL_FOUNTAIN,
      BURSTEXPLOSIONS,
      CREEPERFACE,
      PAUSES;

      // $FF: synthetic method
      private static FireworkBox.BoxType[] $values() {
         return new FireworkBox.BoxType[]{LONG_ROCKETS, LONG_BURST, RAINBOW, SHORT_BURST, RANDOM, BALL_FOUNTAIN, BURSTEXPLOSIONS, CREEPERFACE, PAUSES};
      }
   }
}
