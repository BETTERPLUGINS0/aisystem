package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public final class Dummy {
   public static final Dummy DUMMY = new Dummy();

   private Dummy() {
   }

   public static Dummy dummyRead(PacketWrapper<?> wrapper) {
      return DUMMY;
   }

   public static Dummy dummyReadNbt(PacketWrapper<?> wrapper) {
      wrapper.readNBTRaw();
      return DUMMY;
   }

   public static void dummyWrite(PacketWrapper<?> wrapper, Dummy dummy) {
   }

   public static void dummyWriteNbt(PacketWrapper<?> wrapper, Dummy dummy) {
      wrapper.writeByte(10);
      wrapper.writeByte(0);
   }

   public static Dummy dummy() {
      return DUMMY;
   }

   public String toString() {
      return "Dummy{}";
   }
}
