package ac.grim.grimac.utils.collisions.datatypes;

public class HexCollisionBox extends SimpleCollisionBox {
   public HexCollisionBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
      this.minX = minX / 16.0D;
      this.minY = minY / 16.0D;
      this.minZ = minZ / 16.0D;
      this.maxX = maxX / 16.0D;
      this.maxY = maxY / 16.0D;
      this.maxZ = maxZ / 16.0D;
   }
}
