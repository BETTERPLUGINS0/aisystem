package com.bergerkiller.bukkit.tc.debug.particles;

import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.common.utils.PlayerUtil;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

class DebugParticlesLegacy extends DebugParticles {
   private static final double PARTICLE_SPACING = 0.3D;
   private static final int PARTICLE_ITERATIONS = 20;
   private static final int PARTICLE_INTERVAL = 4;
   private final List<DebugParticlesLegacy.Element> elements = new ArrayList();

   public DebugParticlesLegacy(Player player) {
      super(player);
   }

   public void line(Color color, double x1, double y1, double z1, double x2, double y2, double z2) {
      double dist = MathUtil.distance(x1, y1, z1, x2, y2, z2);
      if (dist >= 1.0E-8D) {
         int n = MathUtil.ceil(dist / 0.3D);
         this.elements.add(new DebugParticlesLegacy.Line(color, x1, y1, z1, x2, y2, z2, n));
         this.startUpdating();
      }

   }

   public void point(Color color, double x, double y, double z) {
      this.elements.add(new DebugParticlesLegacy.Point(color, x, y, z));
      this.startUpdating();
   }

   protected boolean update() {
      this.elements.removeIf((l) -> {
         return l.update(this.player);
      });
      return this.elements.isEmpty();
   }

   private static class Line extends DebugParticlesLegacy.Element {
      public final Color color;
      public final double x;
      public final double y;
      public final double z;
      public final double dx;
      public final double dy;
      public final double dz;
      public final int count;

      public Line(Color color, double x1, double y1, double z1, double x2, double y2, double z2, int count) {
         this.color = color;
         this.x = x1;
         this.y = y1;
         this.z = z1;
         this.dx = x2 - x1;
         this.dy = y2 - y1;
         this.dz = z2 - z1;
         this.count = count;
      }

      public void spawn(Player viewer) {
         Vector position = new Vector();
         int n = this.count;

         for(int i = 0; i < n; ++i) {
            double t = (double)i / (double)(n - 1);
            position.setX(this.x + this.dx * t);
            position.setY(this.y + this.dy * t);
            position.setZ(this.z + this.dz * t);
            PlayerUtil.spawnDustParticles(viewer, position, this.color);
         }

      }
   }

   private static class Point extends DebugParticlesLegacy.Element {
      public final Color color;
      public final Vector pos;

      public Point(Color color, double x, double y, double z) {
         this.color = color;
         this.pos = new Vector(x, y, z);
      }

      public void spawn(Player viewer) {
         PlayerUtil.spawnDustParticles(viewer, this.pos, this.color);
      }
   }

   private abstract static class Element {
      public int age = 0;
      public int skip = 4;

      public Element() {
      }

      public abstract void spawn(Player var1);

      public boolean update(Player viewer) {
         if (++this.skip >= 4) {
            this.skip = 0;
            this.spawn(viewer);
            return ++this.age >= 20;
         } else {
            return false;
         }
      }
   }
}
