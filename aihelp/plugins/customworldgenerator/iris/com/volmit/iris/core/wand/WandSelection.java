package com.volmit.iris.core.wand;

import com.volmit.iris.util.data.Cuboid;
import com.volmit.iris.util.data.registry.Particles;
import com.volmit.iris.util.math.M;
import java.awt.Color;
import org.bukkit.Location;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class WandSelection {
   private final Cuboid c;
   private final Player p;
   private static final double STEP = 0.1D;

   public WandSelection(Cuboid c, Player p) {
      this.c = var1;
      this.p = var2;
   }

   public void draw() {
      Location var1 = this.p.getLocation();
      double var2 = 65536.0D;
      int var4 = 0;
      Location[][] var5 = new Location[][]{{this.c.getLowerNE(), new Location(this.c.getWorld(), (double)(this.c.getUpperX() + 1), (double)this.c.getLowerY(), (double)this.c.getLowerZ())}, {this.c.getLowerNE(), new Location(this.c.getWorld(), (double)this.c.getLowerX(), (double)(this.c.getUpperY() + 1), (double)this.c.getLowerZ())}, {this.c.getLowerNE(), new Location(this.c.getWorld(), (double)this.c.getLowerX(), (double)this.c.getLowerY(), (double)(this.c.getUpperZ() + 1))}, {new Location(this.c.getWorld(), (double)(this.c.getUpperX() + 1), (double)this.c.getLowerY(), (double)this.c.getLowerZ()), new Location(this.c.getWorld(), (double)(this.c.getUpperX() + 1), (double)(this.c.getUpperY() + 1), (double)this.c.getLowerZ())}, {new Location(this.c.getWorld(), (double)(this.c.getUpperX() + 1), (double)this.c.getLowerY(), (double)this.c.getLowerZ()), new Location(this.c.getWorld(), (double)(this.c.getUpperX() + 1), (double)this.c.getLowerY(), (double)(this.c.getUpperZ() + 1))}, {new Location(this.c.getWorld(), (double)this.c.getLowerX(), (double)(this.c.getUpperY() + 1), (double)this.c.getLowerZ()), new Location(this.c.getWorld(), (double)(this.c.getUpperX() + 1), (double)(this.c.getUpperY() + 1), (double)this.c.getLowerZ())}, {new Location(this.c.getWorld(), (double)this.c.getLowerX(), (double)(this.c.getUpperY() + 1), (double)this.c.getLowerZ()), new Location(this.c.getWorld(), (double)this.c.getLowerX(), (double)(this.c.getUpperY() + 1), (double)(this.c.getUpperZ() + 1))}, {new Location(this.c.getWorld(), (double)this.c.getLowerX(), (double)this.c.getLowerY(), (double)(this.c.getUpperZ() + 1)), new Location(this.c.getWorld(), (double)(this.c.getUpperX() + 1), (double)this.c.getLowerY(), (double)(this.c.getUpperZ() + 1))}, {new Location(this.c.getWorld(), (double)this.c.getLowerX(), (double)this.c.getLowerY(), (double)(this.c.getUpperZ() + 1)), new Location(this.c.getWorld(), (double)this.c.getLowerX(), (double)(this.c.getUpperY() + 1), (double)(this.c.getUpperZ() + 1))}, {new Location(this.c.getWorld(), (double)(this.c.getUpperX() + 1), (double)(this.c.getUpperY() + 1), (double)this.c.getLowerZ()), new Location(this.c.getWorld(), (double)(this.c.getUpperX() + 1), (double)(this.c.getUpperY() + 1), (double)(this.c.getUpperZ() + 1))}, {new Location(this.c.getWorld(), (double)this.c.getLowerX(), (double)(this.c.getUpperY() + 1), (double)(this.c.getUpperZ() + 1)), new Location(this.c.getWorld(), (double)(this.c.getUpperX() + 1), (double)(this.c.getUpperY() + 1), (double)(this.c.getUpperZ() + 1))}, {new Location(this.c.getWorld(), (double)(this.c.getUpperX() + 1), (double)this.c.getLowerY(), (double)(this.c.getUpperZ() + 1)), new Location(this.c.getWorld(), (double)(this.c.getUpperX() + 1), (double)(this.c.getUpperY() + 1), (double)(this.c.getUpperZ() + 1))}};
      Location[][] var6 = var5;
      int var7 = var5.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         Location[] var9 = var6[var8];
         Vector var10 = var9[1].toVector().subtract(var9[0].toVector());
         double var11 = var10.length();
         var10.normalize();

         for(double var13 = 0.0D; var13 <= var11; var13 += 0.1D) {
            Location var15 = var9[0].clone().add(var10.clone().multiply(var13));
            if (!(var1.distanceSquared(var15) > var2)) {
               this.spawnParticle(var15, var1);
               ++var4;
            }
         }
      }

   }

   private void spawnParticle(Location particleLoc, Location playerLoc) {
      double var3 = M.lerpInverse(0.0D, 4096.0D, var2.distanceSquared(var1));
      double var5 = M.lerp(0.125D, 3.5D, var3);
      if (!M.r(Math.min(var5 * 5.0D, 0.9D) * 0.995D)) {
         float var7 = (float)(0.5D + Math.sin((var1.getX() + var1.getY() + var1.getZ() + (double)((float)this.p.getTicksLived() / 2.0F)) / 20.0D) / 2.0D);
         Color var8 = Color.getHSBColor(var7, 1.0F, 1.0F);
         this.p.spawnParticle(Particles.REDSTONE, var1, 0, 0.0D, 0.0D, 0.0D, 1.0D, new DustOptions(org.bukkit.Color.fromRGB(var8.getRed(), var8.getGreen(), var8.getBlue()), (float)var5 * 3.0F));
      }
   }
}
