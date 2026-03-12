package ac.grim.grimac.utils.data;

import ac.grim.grimac.utils.math.Vector3dm;

public class VelocityData {
   public final Vector3dm vector;
   public final int entityID;
   public final int transaction;
   public double offset = 2.147483647E9D;
   public final boolean isSetback;

   public VelocityData(int entityID, int transaction, boolean isSetback, Vector3dm vector) {
      this.entityID = entityID;
      this.vector = vector;
      this.transaction = transaction;
      this.isSetback = isSetback;
   }

   public VelocityData(int entityID, int transaction, Vector3dm vector, boolean isSetback, double offset) {
      this.entityID = entityID;
      this.vector = vector;
      this.transaction = transaction;
      this.isSetback = isSetback;
      this.offset = offset;
   }
}
