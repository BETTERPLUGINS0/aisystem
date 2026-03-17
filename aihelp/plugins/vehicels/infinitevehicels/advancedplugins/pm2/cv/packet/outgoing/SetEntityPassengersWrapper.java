package advancedplugins.pm2.cv.packet.outgoing;

import advancedplugins.pm2.cv.packet.PacketWrapper;

public class SetEntityPassengersWrapper extends PacketWrapper {
   private final int entityId;
   private final int[] passengers;

   public SetEntityPassengersWrapper(int entityId, int[] passengers) {
      this.entityId = var1;
      this.passengers = var2;
   }

   public int getEntityId() {
      return this.entityId;
   }

   public int[] getPassengers() {
      return this.passengers;
   }
}
