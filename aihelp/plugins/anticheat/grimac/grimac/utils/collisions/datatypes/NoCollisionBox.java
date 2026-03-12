package ac.grim.grimac.utils.collisions.datatypes;

import java.util.List;

public class NoCollisionBox implements CollisionBox {
   public static final NoCollisionBox INSTANCE = new NoCollisionBox();

   private NoCollisionBox() {
   }

   public CollisionBox union(SimpleCollisionBox other) {
      return other;
   }

   public boolean isCollided(SimpleCollisionBox other) {
      return false;
   }

   public boolean isIntersected(SimpleCollisionBox other) {
      return false;
   }

   public CollisionBox offset(double x, double y, double z) {
      return this;
   }

   public void downCast(List<SimpleCollisionBox> list) {
   }

   public int downCast(SimpleCollisionBox[] list) {
      return 0;
   }

   public boolean isNull() {
      return true;
   }

   public boolean isFullBlock() {
      return false;
   }

   public CollisionBox copy() {
      return this;
   }
}
