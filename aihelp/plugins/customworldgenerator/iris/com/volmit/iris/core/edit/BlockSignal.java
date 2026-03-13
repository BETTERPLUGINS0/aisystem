package com.volmit.iris.core.edit;

import com.volmit.iris.util.parallel.MultiBurst;
import com.volmit.iris.util.scheduling.J;
import com.volmit.iris.util.scheduling.SR;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class BlockSignal {
   public static final AtomicInteger active = new AtomicInteger(0);

   public BlockSignal(Block block, int ticks) {
      active.incrementAndGet();
      Location var3 = var1.getLocation().clone().add(0.5D, 0.0D, 0.5D);
      FallingBlock var4 = var1.getWorld().spawnFallingBlock(var3, var1.getBlockData());
      var4.setGravity(false);
      var4.setInvulnerable(true);
      var4.setGlowing(true);
      var4.setDropItem(false);
      var4.setHurtEntities(false);
      var4.setSilent(true);
      var4.setTicksLived(1);
      var4.setVelocity(new Vector(0, 0, 0));
      J.s(() -> {
         var4.remove();
         active.decrementAndGet();
         BlockData var2 = var1.getBlockData();
         MultiBurst.burst.lazy(() -> {
            Iterator var1x = var1.getWorld().getPlayers().iterator();

            while(var1x.hasNext()) {
               Player var2 = (Player)var1x.next();
               var2.sendBlockChange(var1.getLocation(), var1.getBlockData());
            }

         });
      }, var2);
   }

   public static void of(Block block, int ticks) {
      new BlockSignal(var0, var1);
   }

   public static void of(Block block) {
      of(var0, 100);
   }

   public static Runnable forever(Block block) {
      final Location var1 = var0.getLocation().clone().add(0.5D, 0.0D, 0.5D).clone();
      final FallingBlock var2 = var0.getWorld().spawnFallingBlock(var1.clone(), var0.getBlockData());
      var2.setGravity(false);
      var2.setInvulnerable(true);
      var2.setGlowing(true);
      var2.teleport(var1.clone());
      var2.setDropItem(false);
      var2.setHurtEntities(false);
      var2.setSilent(true);
      var2.setTicksLived(1);
      var2.setVelocity(new Vector(0, 0, 0));
      SR var10001 = new SR(20) {
         public void run() {
            if (var2.isDead()) {
               this.cancel();
            } else {
               var2.setTicksLived(1);
               var2.teleport(var1.clone());
               var2.setVelocity(new Vector(0, 0, 0));
            }
         }
      };
      return () -> {
         var2.remove();
         BlockData var2x = var0.getBlockData();
         MultiBurst.burst.lazy(() -> {
            Iterator var1 = var0.getWorld().getPlayers().iterator();

            while(var1.hasNext()) {
               Player var2 = (Player)var1.next();
               var2.sendBlockChange(var0.getLocation(), var0.getBlockData());
            }

         });
      };
   }
}
