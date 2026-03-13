package me.casperge.realisticseasons.seasonevent.buildin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.seasonevent.CustomDatedEvent;
import me.casperge.realisticseasons.utils.ChunkUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Villager;

public class NewYearEvent extends CustomDatedEvent implements DefaultEvent {
   RealisticSeasons main = RealisticSeasons.getInstance();
   Random r = new Random();
   List<World> enabled = new ArrayList();
   public int fireworkDistance = 34;
   public boolean spawnFireworkBoxes = false;
   HashMap<NewYearEvent.CoordPair, Long> registeredBoxes = new HashMap();

   public NewYearEvent(ConfigurationSection var1) {
      super(var1);
      Bukkit.getScheduler().runTaskTimer(this.main, new Runnable() {
         public void run() {
            ArrayList var1 = new ArrayList();
            Iterator var2 = NewYearEvent.this.enabled.iterator();

            while(var2.hasNext()) {
               World var3 = (World)var2.next();
               if (!Bukkit.getWorlds().contains(var3)) {
                  var1.add(var3);
               } else if (NewYearEvent.this.spawnFireworkBoxes) {
                  NewYearEvent.this.tickBoxSpawning(var3);
               }
            }

            NewYearEvent.this.enabled.removeAll(var1);
            var1.clear();
         }
      }, 20L, 20L);
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

   }

   public DefaultEventType getType() {
      return DefaultEventType.NEWYEAR;
   }

   private void tickBoxSpawning(World var1) {
      String var2 = var1.getName();
      if (var1.getTime() >= 13000L && var1.getTime() <= 23000L) {
         if (this.main.getTimeManager().getHours(var1) <= 12) {
            Iterator var3 = var1.getEntitiesByClass(Villager.class).iterator();

            while(true) {
               Villager var4;
               do {
                  if (!var3.hasNext()) {
                     return;
                  }

                  var4 = (Villager)var3.next();
               } while(!var4.hasAI());

               int var5 = var4.getLocation().getBlockX();
               int var6 = var4.getLocation().getBlockZ();
               boolean var7 = true;
               ArrayList var8 = null;
               Iterator var9 = this.registeredBoxes.keySet().iterator();

               while(var9.hasNext()) {
                  NewYearEvent.CoordPair var10 = (NewYearEvent.CoordPair)var9.next();
                  if (var2.equals(var10.w) && ChunkUtils.get2dDistance(var10.x, var10.z, var5, var6) < (double)this.fireworkDistance) {
                     if ((Long)this.registeredBoxes.get(var10) + 45000L > System.currentTimeMillis()) {
                        var7 = false;
                        break;
                     }

                     if (var8 == null) {
                        var8 = new ArrayList();
                     }

                     var8.add(var10);
                  }
               }

               if (var7) {
                  int var12 = var4.getWorld().getHighestBlockAt(var4.getLocation()).getY();
                  int var13 = (int)var4.getLocation().getY();
                  if (var12 <= var13 + 8) {
                     Block var11 = var4.getWorld().getHighestBlockAt(var4.getLocation());
                     this.registeredBoxes.put(new NewYearEvent.CoordPair(var2, var11.getX(), var11.getZ()), System.currentTimeMillis());
                     this.main.getParticleManager().spawnFireworkBox(var11.getLocation().add(0.0D, 1.0D, 0.0D));
                  }
               }
            }
         }
      }
   }

   public boolean isEnabled(World var1) {
      return this.enabled.contains(var1);
   }

   class CoordPair {
      int x;
      int z;
      String w;

      CoordPair(String param2, int param3, int param4) {
         this.x = var3;
         this.z = var4;
         this.w = var2;
      }
   }
}
